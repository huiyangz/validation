package com.dcits.validation.step;

import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.facade.bo.ST005InputBO;
import com.dcits.validation.facade.bo.ST005OutputBO;
import com.dcits.validation.facade.components.IRbBusAcctBcc;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.eo.RbBusAcctEO;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ST005 检查账户是否存在限制 单元测试。
 * 用例来源：outputs/测试用例.md（ST005-TC001～TC003）。
 * 桩按条件 EO 字段值精确匹配，不使用 any() 宽泛匹配。
 */
@ExtendWith(MockitoExtension.class)
public class ST005PbcTest {

    @Mock
    private IRbBusAcctBcc rbBusAcctBcc;

    @Mock
    private IRbBusRestraintsBcc rbBusRestraintsBcc;

    @InjectMocks
    private ST005Pbc st005Pbc;

    private ST005InputBO buildInput(String baseAcctNo) {
        ST005InputBO input = new ST005InputBO();
        input.setBaseAcctNo(baseAcctNo);
        return input;
    }

    private RbBusAcctEO buildAcct(Integer internalKey, String baseAcctNo, String leadAcctFlag, Integer parentInternalKey) {
        RbBusAcctEO eo = new RbBusAcctEO();
        eo.setInternalKey(internalKey);
        eo.setBaseAcctNo(baseAcctNo);
        eo.setLeadAcctFlag(leadAcctFlag);
        eo.setParentInternalKey(parentInternalKey);
        return eo;
    }

    private RbBusRestraintsEO buildRestraint(String baseAcctNo, String resSeqNo, RestraintType restraintType) {
        RbBusRestraintsEO eo = new RbBusRestraintsEO();
        eo.setBaseAcctNo(baseAcctNo);
        eo.setResSeqNo(resSeqNo);
        eo.setRestraintType(restraintType);
        eo.setRestraintsStatus(RestraintsStatus.A);
        return eo;
    }

    /** 账户信息查询条件匹配：条件 EO 的账号等于期望待查账号 */
    private static boolean acctCondition(RbBusAcctEO eo, String expectedAcctNo) {
        return eo != null && expectedAcctNo.equals(eo.getBaseAcctNo());
    }

    /** 账户限制查询条件匹配：条件 EO 的账号等于期望待查账户且限制状态为 A-生效 */
    private static boolean restraintCondition(RbBusRestraintsEO eo, String expectedAcctNo) {
        return eo != null && expectedAcctNo.equals(eo.getBaseAcctNo())
                && eo.getRestraintsStatus() == RestraintsStatus.A;
    }

    // 场景：上送账号非子账户（账户记录无上级账户内部键），子步骤1仅查询一次账户信息，待查账户即上送账号，存在一条限制状态 A-生效 的限制记录；预期成功并输出账户与限制五字段
    @Test
    public void testST005T01() {
        Mockito.lenient().when(rbBusAcctBcc.findByEo(Mockito.argThat(eo -> acctCondition(eo, "6222000011113331"))))
                .thenReturn(Collections.singletonList(
                        buildAcct(13331, "6222000011113331", "1", null)));
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(eo -> restraintCondition(eo, "6222000011113331"))))
                .thenReturn(Collections.singletonList(
                        buildRestraint("6222000011113331", "RS20260901000001", RestraintType.VALUE_13)));

        ST005OutputBO output = st005Pbc.execute(buildInput("6222000011113331"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("6222000011113331", output.getBaseAcctNo());
        assertEquals("1", output.getLeadAcctFlag());
        assertEquals("RS20260901000001", output.getResSeqNo());
        assertSame(RestraintType.VALUE_13, output.getRestraintType());
        assertSame(RestraintsStatus.A, output.getRestraintsStatus());
    }

    // 场景：上送账号为子账户（账户记录 parentInternalKey=13331 有值），经上级账户内部键查得主账户账号作为待查账户，主账户存在一条 A-生效 限制记录；预期成功且输出取主账户记录（账号为主账户账号而非上送账号）与限制记录，并核对限制查询条件 EO 的待查账户与状态传递
    @Test
    public void testST005T02() {
        Mockito.lenient().when(rbBusAcctBcc.findByEo(Mockito.argThat(eo -> acctCondition(eo, "6222000011113332"))))
                .thenReturn(Collections.singletonList(
                        buildAcct(13332, "6222000011113332", "0", 13331)));
        Mockito.lenient().when(rbBusAcctBcc.findByPrimaryKey(13331))
                .thenReturn(buildAcct(13331, "6222000011113331", "1", null));
        final RbBusRestraintsEO[] capturedCondition = new RbBusRestraintsEO[1];
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(eo -> restraintCondition(eo, "6222000011113331"))))
                .thenAnswer(invocation -> {
                    capturedCondition[0] = invocation.getArgument(0);
                    return Collections.singletonList(
                            buildRestraint("6222000011113331", "RS20260905000002", RestraintType.SF1));
                });

        ST005OutputBO output = st005Pbc.execute(buildInput("6222000011113332"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("6222000011113331", output.getBaseAcctNo());
        assertEquals("1", output.getLeadAcctFlag());
        assertEquals("RS20260905000002", output.getResSeqNo());
        assertSame(RestraintType.SF1, output.getRestraintType());
        assertSame(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals("6222000011113331", capturedCondition[0].getBaseAcctNo());
        assertSame(RestraintsStatus.A, capturedCondition[0].getRestraintsStatus());
    }

    // 场景：上送账号非子账户，限制查询返回空集合，子步骤4无账户限制信息可赋值；预期成功且限制编号、限制类型、限制状态三输出为空
    @Test
    public void testST005T03() {
        Mockito.lenient().when(rbBusAcctBcc.findByEo(Mockito.argThat(eo -> acctCondition(eo, "6222000011113333"))))
                .thenReturn(Collections.singletonList(
                        buildAcct(13333, "6222000011113333", "1", null)));
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(eo -> restraintCondition(eo, "6222000011113333"))))
                .thenReturn(Collections.emptyList());

        ST005OutputBO output = st005Pbc.execute(buildInput("6222000011113333"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("6222000011113333", output.getBaseAcctNo());
        assertEquals("1", output.getLeadAcctFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
    }
}

package com.dcits.validation.step;

import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST003InputBO;
import com.dcits.validation.facade.bo.ST003OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ST003 检查有权机关冻结限制 单元测试。
 * 用例来源：outputs/测试用例.md（ST003-TC001～TC005）。
 */
@ExtendWith(MockitoExtension.class)
public class ST003PbcTest {

    private static final String ACCT_NO = "6222000011112222";

    @Mock
    private IRbBusRestraintsBcc rbBusRestraintsBcc;

    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;

    @InjectMocks
    private ST003Pbc st003Pbc;

    private ST003InputBO buildInput() {
        ST003InputBO input = new ST003InputBO();
        input.setBaseAcctNo(ACCT_NO);
        return input;
    }

    private RbBusRestraintsEO buildRestraint(RestraintType restraintType, String resSeqNo) {
        RbBusRestraintsEO eo = new RbBusRestraintsEO();
        eo.setBaseAcctNo(ACCT_NO);
        eo.setRestraintsStatus(RestraintsStatus.A);
        eo.setRestraintType(restraintType);
        eo.setResSeqNo(resSeqNo);
        return eo;
    }

    private RbRestraintTypeEO buildRestraintType(RestraintType restraintType, Status status, String ahBuFlag) {
        RbRestraintTypeEO eo = new RbRestraintTypeEO();
        eo.setRestraintType(restraintType);
        eo.setStatus(status);
        eo.setAhBuFlag(ahBuFlag);
        return eo;
    }

    // 场景：账户无生效限制记录，子步骤1返回空集合，子步骤2不执行；预期步骤成功返回且有权机关冻结标志为空
    @Test
    public void testST003T01() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.any(RbBusRestraintsEO.class)))
                .thenReturn(Collections.emptyList());

        ST003OutputBO output = st003Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertNull(output.getAhBuFlag());
    }

    // 场景：单条生效限制记录，类型SF1在限制类型表中状态A-生效，取得标志Y；并核对子步骤1查询条件（账号+限制状态A-生效）的字段映射
    @Test
    public void testST003T02() {
        final RbBusRestraintsEO[] capturedCondition = new RbBusRestraintsEO[1];
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.any(RbBusRestraintsEO.class)))
                .thenAnswer(invocation -> {
                    capturedCondition[0] = invocation.getArgument(0);
                    return Collections.singletonList(buildRestraint(RestraintType.SF1, "R1000001"));
                });
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.SF1))
                .thenReturn(buildRestraintType(RestraintType.SF1, Status.A, "Y"));

        ST003OutputBO output = st003Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertEquals("Y", output.getAhBuFlag());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals(ACCT_NO, capturedCondition[0].getBaseAcctNo());
        assertSame(RestraintsStatus.A, capturedCondition[0].getRestraintsStatus());
    }

    // 场景：同一账号多条生效记录，首条类型SF1状态A-生效带标志Y，按「查询到即赋值并返回」以首条为准；SF2查询未触达不设桩
    @Test
    public void testST003T03() {
        List<RbBusRestraintsEO> records = Arrays.asList(
                buildRestraint(RestraintType.SF1, "R1000001"),
                buildRestraint(RestraintType.SF2, "R1000002"));
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.any(RbBusRestraintsEO.class)))
                .thenReturn(records);
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.SF1))
                .thenReturn(buildRestraintType(RestraintType.SF1, Status.A, "Y"));

        ST003OutputBO output = st003Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertEquals("Y", output.getAhBuFlag());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
    }

    // 场景：首条类型SF1状态F无效、其标志N不采用，继续查询后续类型SF2命中A-生效标志Y
    @Test
    public void testST003T04() {
        List<RbBusRestraintsEO> records = Arrays.asList(
                buildRestraint(RestraintType.SF1, "R1000001"),
                buildRestraint(RestraintType.SF2, "R1000002"));
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.any(RbBusRestraintsEO.class)))
                .thenReturn(records);
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.SF1))
                .thenReturn(buildRestraintType(RestraintType.SF1, Status.F, "N"));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.SF2))
                .thenReturn(buildRestraintType(RestraintType.SF2, Status.A, "Y"));

        ST003OutputBO output = st003Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertEquals("Y", output.getAhBuFlag());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
    }

    // 场景：存在生效限制记录但限制类型表查无对应类型记录（返回null），均未查询到A-生效标志，标志为空、步骤成功返回
    @Test
    public void testST003T05() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.any(RbBusRestraintsEO.class)))
                .thenReturn(Collections.singletonList(buildRestraint(RestraintType.VALUE_13, "R1000003")));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(null);

        ST003OutputBO output = st003Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getAhBuFlag());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
    }
}

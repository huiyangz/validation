package com.dcits.validation.step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST007InputBO;
import com.dcits.validation.facade.bo.ST007OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/**
 * ST007 检查质押类限制 单元测试
 */
@ExtendWith(MockitoExtension.class)
public class ST007PbcTest {

    @Mock
    private IRbBusRestraintsBcc rbBusRestraintsBcc;
    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;

    @InjectMocks
    private ST007Pbc st007Pbc;

    private ST007InputBO buildInput(String baseAcctNo) {
        ST007InputBO input = new ST007InputBO();
        input.setBaseAcctNo(baseAcctNo);
        return input;
    }

    private RbBusRestraintsEO buildRestraint(String resSeqNo, RestraintType restraintType, String baseAcctNo) {
        RbBusRestraintsEO eo = new RbBusRestraintsEO();
        eo.setResSeqNo(resSeqNo);
        eo.setRestraintType(restraintType);
        eo.setRestraintsStatus(RestraintsStatus.A);
        eo.setBaseAcctNo(baseAcctNo);
        return eo;
    }

    private RbRestraintTypeEO buildRestraintType(RestraintType restraintType, Status status, String pledgedFlag) {
        RbRestraintTypeEO eo = new RbRestraintTypeEO();
        eo.setRestraintType(restraintType);
        eo.setStatus(status);
        eo.setPledgedFlag(pledgedFlag);
        return eo;
    }

    // 账号存在一条生效限制记录（质押止付-系统用），类型表记录状态A-生效且质押标志非空，首个记录即命中，
    // 预期成功返回并输出该记录的限制编号、账户限制类型、限制状态及质押标志、状态
    @Test
    public void testST007T01() {
        lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
                && "1000100010001234".equals(eo.getBaseAcctNo())
                && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(List.of(buildRestraint("RES20260926000001", RestraintType.VALUE_12, "1000100010001234")));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_12))
                .thenReturn(buildRestraintType(RestraintType.VALUE_12, Status.A, "Y"));

        ST007OutputBO out = st007Pbc.execute(buildInput("1000100010001234"));

        assertTrue(out.isSucceed());
        assertNull(out.getErrorCode());
        assertNull(out.getErrorMessage());
        assertEquals("RES20260926000001", out.getResSeqNo());
        assertEquals(RestraintType.VALUE_12, out.getRestraintType());
        assertEquals(RestraintsStatus.A, out.getRestraintsStatus());
        assertEquals("Y", out.getPledgedFlag());
        assertEquals(Status.A, out.getStatus());
    }

    // 两条生效限制记录顺序遍历：首条类型（不允许现金存入）质押标志为null被跳过，第二条（质押部分止付-手工）命中，
    // 预期成功返回且输出为第二条记录数据（resSeqNo为记录2编号，证明空标志记录被跳过且遍历继续）
    @Test
    public void testST007T02() {
        lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
                && "1000100010005678".equals(eo.getBaseAcctNo())
                && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(List.of(
                        buildRestraint("RES20260926000002", RestraintType.VALUE_6, "1000100010005678"),
                        buildRestraint("RES20260926000003", RestraintType.VALUE_50, "1000100010005678")));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_6))
                .thenReturn(buildRestraintType(RestraintType.VALUE_6, Status.A, null));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_50))
                .thenReturn(buildRestraintType(RestraintType.VALUE_50, Status.A, "Y"));

        ST007OutputBO out = st007Pbc.execute(buildInput("1000100010005678"));

        assertTrue(out.isSucceed());
        assertNull(out.getErrorCode());
        assertNull(out.getErrorMessage());
        assertEquals("RES20260926000003", out.getResSeqNo());
        assertEquals(RestraintType.VALUE_50, out.getRestraintType());
        assertEquals(RestraintsStatus.A, out.getRestraintsStatus());
        assertEquals("Y", out.getPledgedFlag());
        assertEquals(Status.A, out.getStatus());
    }

    // 两条生效限制记录均可命中（质押部分止付/质押全额止付，手工），首条命中后停止遍历，
    // 预期成功返回且输出为首条记录数据（若未停止将输出记录2的RES20260926000005，断言失败，即停止遍历的可观测验证）
    @Test
    public void testST007T03() {
        lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
                && "1000100010007890".equals(eo.getBaseAcctNo())
                && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(List.of(
                        buildRestraint("RES20260926000004", RestraintType.VALUE_50, "1000100010007890"),
                        buildRestraint("RES20260926000005", RestraintType.VALUE_51, "1000100010007890")));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_50))
                .thenReturn(buildRestraintType(RestraintType.VALUE_50, Status.A, "Y"));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_51))
                .thenReturn(buildRestraintType(RestraintType.VALUE_51, Status.A, "Y"));

        ST007OutputBO out = st007Pbc.execute(buildInput("1000100010007890"));

        assertTrue(out.isSucceed());
        assertNull(out.getErrorCode());
        assertNull(out.getErrorMessage());
        assertEquals("RES20260926000004", out.getResSeqNo());
        assertEquals(RestraintType.VALUE_50, out.getRestraintType());
        assertEquals(RestraintsStatus.A, out.getRestraintsStatus());
        assertEquals("Y", out.getPledgedFlag());
        assertEquals(Status.A, out.getStatus());
    }

    // 账号无任何生效限制记录，[账户限制信息]为SPEC允许的空集合，循环体不执行，
    // 预期成功返回且五个输出字段均为空（类型表BCC未触达，不设桩）
    @Test
    public void testST007T04() {
        lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
                && "1000100010009012".equals(eo.getBaseAcctNo())
                && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(Collections.emptyList());

        ST007OutputBO out = st007Pbc.execute(buildInput("1000100010009012"));

        assertTrue(out.isSucceed());
        assertNull(out.getErrorCode());
        assertNull(out.getErrorMessage());
        assertNull(out.getResSeqNo());
        assertNull(out.getRestraintType());
        assertNull(out.getRestraintsStatus());
        assertNull(out.getPledgedFlag());
        assertNull(out.getStatus());
    }

    // 两条生效限制记录遍历后均无命中：记录1限制类型（挂失止付）在类型表无记录，记录2类型记录状态为C-非活动状态
    // （质押标志非空但不取），预期遍历结束输出字段均为空且成功返回（验证状态过滤先于取标志）
    @Test
    public void testST007T05() {
        lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
                && "1000100010003456".equals(eo.getBaseAcctNo())
                && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(List.of(
                        buildRestraint("RES20260926000006", RestraintType.VALUE_13, "1000100010003456"),
                        buildRestraint("RES20260926000007", RestraintType.VALUE_51, "1000100010003456")));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(null);
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_51))
                .thenReturn(buildRestraintType(RestraintType.VALUE_51, Status.C, "Y"));

        ST007OutputBO out = st007Pbc.execute(buildInput("1000100010003456"));

        assertTrue(out.isSucceed());
        assertNull(out.getErrorCode());
        assertNull(out.getErrorMessage());
        assertNull(out.getResSeqNo());
        assertNull(out.getRestraintType());
        assertNull(out.getRestraintsStatus());
        assertNull(out.getPledgedFlag());
        assertNull(out.getStatus());
    }
}

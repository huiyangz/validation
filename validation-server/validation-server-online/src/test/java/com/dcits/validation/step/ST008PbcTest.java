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

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST008InputBO;
import com.dcits.validation.facade.bo.ST008OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/** ST008 检查是否存在现金止收限制 单元测试 */
@ExtendWith(MockitoExtension.class)
class ST008PbcTest {

    private static final String BASE_ACCT_NO = "61000100001234";

    @Mock
    private IRbBusRestraintsBcc rbBusRestraintsBcc;

    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;

    @InjectMocks
    private ST008Pbc st008Pbc;

    private ST008InputBO buildInput() {
        ST008InputBO input = new ST008InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);
        return input;
    }

    private RbBusRestraintsEO buildRestraint(String resSeqNo, RestraintType restraintType) {
        RbBusRestraintsEO eo = new RbBusRestraintsEO();
        eo.setResSeqNo(resSeqNo);
        eo.setRestraintType(restraintType);
        eo.setRestraintsStatus(RestraintsStatus.A);
        return eo;
    }

    private RbRestraintTypeEO buildRestraintType(RestraintType restraintType, Status status,
            DrCrCtlFlag drCrCtlFlag, String cashFlag) {
        RbRestraintTypeEO eo = new RbRestraintTypeEO();
        eo.setRestraintType(restraintType);
        eo.setStatus(status);
        eo.setDrCrCtlFlag(drCrCtlFlag);
        eo.setCashFlag(cashFlag);
        return eo;
    }

    private void stubFindByEo(List<RbBusRestraintsEO> records) {
        lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo ->
                BASE_ACCT_NO.equals(eo.getBaseAcctNo()) && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(records);
    }

    private void assertNegativeResult(ST008OutputBO out) {
        assertTrue(out.isSucceed());
        assertNull(out.getErrorCode());
        assertNull(out.getErrorMessage());
        assertEquals("否", out.getCashStopFlag());
        assertNull(out.getResSeqNo());
        assertNull(out.getRestraintType());
        assertNull(out.getRestraintsStatus());
        assertNull(out.getRestraintTypeDef());
        assertNull(out.getStatus());
        assertNull(out.getDrCrCtlFlag());
        assertNull(out.getCashFlag());
    }

    // TC001 单条生效限制记录，其限制类型借贷方控制标志=C-禁止贷方且现金标志=N-不允许现金，存在现金止收限制；
    // 预期 succeed=true、cashStopFlag="是"，输出字段取该限制记录及其限制类型表信息
    @Test
    void testST008T01() {
        RbBusRestraintsEO rec1 = buildRestraint("RS20260926000001", RestraintType.VALUE_6);
        stubFindByEo(List.of(rec1));
        RbRestraintTypeEO typeEO = buildRestraintType(RestraintType.VALUE_6, Status.A, DrCrCtlFlag.C, "N");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_6)).thenReturn(typeEO);

        ST008OutputBO out = st008Pbc.execute(buildInput());

        assertTrue(out.isSucceed());
        assertNull(out.getErrorCode());
        assertNull(out.getErrorMessage());
        assertEquals("是", out.getCashStopFlag());
        assertEquals("RS20260926000001", out.getResSeqNo());
        assertEquals(RestraintType.VALUE_6, out.getRestraintType());
        assertEquals(RestraintsStatus.A, out.getRestraintsStatus());
        assertEquals(RestraintType.VALUE_6, out.getRestraintTypeDef());
        assertEquals(Status.A, out.getStatus());
        assertEquals(DrCrCtlFlag.C, out.getDrCrCtlFlag());
        assertEquals("N", out.getCashFlag());
    }

    // TC002 三条生效限制记录：第1条借贷方控制标志=D-禁止借方不满足、第2条满足、第3条也满足，输出取首条满足记录 rec2；
    // 预期 succeed=true、cashStopFlag="是"，输出取 rec2 及其 VALUE_6 类型信息（非 rec3）
    @Test
    void testST008T02() {
        RbBusRestraintsEO rec1 = buildRestraint("RS20260926000001", RestraintType.VALUE_7);
        RbBusRestraintsEO rec2 = buildRestraint("RS20260926000002", RestraintType.VALUE_6);
        RbBusRestraintsEO rec3 = buildRestraint("RS20260926000003", RestraintType.VALUE_17);
        stubFindByEo(List.of(rec1, rec2, rec3));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_7))
                .thenReturn(buildRestraintType(RestraintType.VALUE_7, Status.A, DrCrCtlFlag.D, "N"));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_6))
                .thenReturn(buildRestraintType(RestraintType.VALUE_6, Status.A, DrCrCtlFlag.C, "N"));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17))
                .thenReturn(buildRestraintType(RestraintType.VALUE_17, Status.A, DrCrCtlFlag.C, "N"));

        ST008OutputBO out = st008Pbc.execute(buildInput());

        assertTrue(out.isSucceed());
        assertNull(out.getErrorCode());
        assertNull(out.getErrorMessage());
        assertEquals("是", out.getCashStopFlag());
        assertEquals("RS20260926000002", out.getResSeqNo());
        assertEquals(RestraintType.VALUE_6, out.getRestraintType());
        assertEquals(RestraintsStatus.A, out.getRestraintsStatus());
        assertEquals(RestraintType.VALUE_6, out.getRestraintTypeDef());
        assertEquals(Status.A, out.getStatus());
        assertEquals(DrCrCtlFlag.C, out.getDrCrCtlFlag());
        assertEquals("N", out.getCashFlag());
    }

    // TC003 账户无生效限制记录（查询返回空列表），子步骤2 未触达；
    // 预期 succeed=true、cashStopFlag="否"，其余 7 个输出字段为 null
    @Test
    void testST008T03() {
        stubFindByEo(Collections.emptyList());

        ST008OutputBO out = st008Pbc.execute(buildInput());

        assertNegativeResult(out);
    }

    // TC004 有限制记录，限制类型借贷方控制标志=C-禁止贷方但现金标志=Y-允许现金，条件不满足；
    // 预期 succeed=true、cashStopFlag="否"，其余输出字段为 null
    @Test
    void testST008T04() {
        RbBusRestraintsEO rec1 = buildRestraint("RS20260926000004", RestraintType.VALUE_13);
        stubFindByEo(List.of(rec1));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(buildRestraintType(RestraintType.VALUE_13, Status.A, DrCrCtlFlag.C, "Y"));

        ST008OutputBO out = st008Pbc.execute(buildInput());

        assertNegativeResult(out);
    }

    // TC005 有限制记录但限制类型表无该类型记录（findByRestraintType 返回 null），无生效类型标志；
    // 预期 succeed=true、cashStopFlag="否"，其余输出字段为 null
    @Test
    void testST008T05() {
        RbBusRestraintsEO rec1 = buildRestraint("RS20260926000005", RestraintType.VALUE_6);
        stubFindByEo(List.of(rec1));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_6)).thenReturn(null);

        ST008OutputBO out = st008Pbc.execute(buildInput());

        assertNegativeResult(out);
    }

    // TC006 有限制记录且类型表有记录，但类型状态=C-非活动（非 A-生效），借贷方/现金标志不参与判断；
    // 预期 succeed=true、cashStopFlag="否"，其余输出字段为 null
    @Test
    void testST008T06() {
        RbBusRestraintsEO rec1 = buildRestraint("RS20260926000006", RestraintType.VALUE_6);
        stubFindByEo(List.of(rec1));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_6))
                .thenReturn(buildRestraintType(RestraintType.VALUE_6, Status.C, DrCrCtlFlag.C, "N"));

        ST008OutputBO out = st008Pbc.execute(buildInput());

        assertNegativeResult(out);
    }
}

package com.dcits.validation.step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST014InputBO;
import com.dcits.validation.facade.bo.ST014OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

@ExtendWith(MockitoExtension.class)
class ST014PbcTest {

    private static final String BASE_ACCT_NO = "2000010123456789";

    @Mock
    private IRbBusRestraintsBcc rbBusRestraintsBcc;

    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;

    @InjectMocks
    private ST014Pbc st014Pbc;

    // 场景：账户存在一条生效限制记录，其限制类型在限制类型表中为生效记录且借贷方控制标志=禁止借贷方、现金标志=禁止现金；预期：判定存在现金不收不付限制，标志为“是”，回显两条记录数据
    @Test
    void testST014T01() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(eo ->
                        BASE_ACCT_NO.equals(eo.getBaseAcctNo()) && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(Collections.singletonList(
                        busRestraint(RestraintType.VALUE_17, "RES20260926000001")));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17))
                .thenReturn(restraintTypeEO(RestraintType.VALUE_17, Status.A, DrCrCtlFlag.A, "N"));

        ST014InputBO input = new ST014InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);

        ST014OutputBO output = st014Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getCashNonRcvPayFlag());
        assertEquals("RES20260926000001", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_17, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.A, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("N", output.getCashFlag());
    }

    // 场景：限制类型生效且借贷方控制标志=禁止借贷方，但现金标志非“N-禁止现金”；预期：判定不存在现金不收不付限制，标志为“否”
    @Test
    void testST014T02() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(eo ->
                        BASE_ACCT_NO.equals(eo.getBaseAcctNo()) && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(Collections.singletonList(
                        busRestraint(RestraintType.VALUE_13, "RES20260926000002")));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(restraintTypeEO(RestraintType.VALUE_13, Status.A, DrCrCtlFlag.A, "Y"));

        ST014InputBO input = new ST014InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);

        ST014OutputBO output = st014Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getCashNonRcvPayFlag());
        assertEquals("RES20260926000002", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_13, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.A, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("Y", output.getCashFlag());
    }

    // 场景：限制类型生效且现金标志=禁止现金，但借贷方控制标志非“A-禁止借贷方”；预期：判定不存在现金不收不付限制，标志为“否”
    @Test
    void testST014T03() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(eo ->
                        BASE_ACCT_NO.equals(eo.getBaseAcctNo()) && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(Collections.singletonList(
                        busRestraint(RestraintType.VALUE_53, "RES20260926000003")));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_53))
                .thenReturn(restraintTypeEO(RestraintType.VALUE_53, Status.A, DrCrCtlFlag.D, "N"));

        ST014InputBO input = new ST014InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);

        ST014OutputBO output = st014Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getCashNonRcvPayFlag());
        assertEquals("RES20260926000003", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_53, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.D, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("N", output.getCashFlag());
    }

    // 场景：账户无生效限制记录（账户限制信息查询返回空列表），无限制类型可查；预期：标志为“否”，六个实体来源输出字段均为 null
    @Test
    void testST014T04() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(eo ->
                        BASE_ACCT_NO.equals(eo.getBaseAcctNo()) && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(Collections.emptyList());

        ST014InputBO input = new ST014InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);

        ST014OutputBO output = st014Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getCashNonRcvPayFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getCashFlag());
    }

    // 场景：账户存在生效限制记录，但限制类型表中无对应记录（findByRestraintType 返回 null）；预期：标志为“否”，限制表三字段回显、类型表三字段为 null
    @Test
    void testST014T05() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(eo ->
                        BASE_ACCT_NO.equals(eo.getBaseAcctNo()) && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(Collections.singletonList(
                        busRestraint(RestraintType.VALUE_35, "RES20260926000004")));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_35))
                .thenReturn(null);

        ST014InputBO input = new ST014InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);

        ST014OutputBO output = st014Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getCashNonRcvPayFlag());
        assertEquals("RES20260926000004", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_35, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getCashFlag());
    }

    // 场景：限制类型表记录存在但其状态非“A-生效”，其借贷方控制标志、现金标志不作为判定与输出数据；预期：标志为“否”，限制表三字段回显、类型表三字段为 null
    @Test
    void testST014T06() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(eo ->
                        BASE_ACCT_NO.equals(eo.getBaseAcctNo()) && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(Collections.singletonList(
                        busRestraint(RestraintType.VALUE_24, "RES20260926000005")));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_24))
                .thenReturn(restraintTypeEO(RestraintType.VALUE_24, Status.C, DrCrCtlFlag.A, "N"));

        ST014InputBO input = new ST014InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);

        ST014OutputBO output = st014Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getCashNonRcvPayFlag());
        assertEquals("RES20260926000005", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_24, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getCashFlag());
    }

    private RbBusRestraintsEO busRestraint(RestraintType restraintType, String resSeqNo) {
        RbBusRestraintsEO eo = new RbBusRestraintsEO();
        eo.setBaseAcctNo(BASE_ACCT_NO);
        eo.setRestraintsStatus(RestraintsStatus.A);
        eo.setRestraintType(restraintType);
        eo.setResSeqNo(resSeqNo);
        return eo;
    }

    private RbRestraintTypeEO restraintTypeEO(RestraintType restraintType, Status status,
            DrCrCtlFlag drCrCtlFlag, String cashFlag) {
        RbRestraintTypeEO eo = new RbRestraintTypeEO();
        eo.setRestraintType(restraintType);
        eo.setStatus(status);
        eo.setDrCrCtlFlag(drCrCtlFlag);
        eo.setCashFlag(cashFlag);
        return eo;
    }
}

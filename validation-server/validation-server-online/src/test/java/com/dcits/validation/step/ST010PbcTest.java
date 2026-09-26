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
import com.dcits.validation.facade.bo.ST010InputBO;
import com.dcits.validation.facade.bo.ST010OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/** ST010 检查是否存在转账不收不付限制 单元测试 */
@ExtendWith(MockitoExtension.class)
class ST010PbcTest {
    private static final String BASE_ACCT_NO = "2000010000001";

    @Mock
    private IRbBusRestraintsBcc rbBusRestraintsBcc;

    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;

    @InjectMocks
    private ST010Pbc st010Pbc;

    // 场景：单条生效限制记录（可疑账户处置-不收不付），类型记录状态有效且借贷方控制标志=A-禁止借贷方、转账标志=N-禁止转账，命中返回"是"及该条记录信息
    @Test
    void testST010T01() {
        RbBusRestraintsEO r1 = buildRestraints("RES2026090100000001", RestraintType.VALUE_17);
        stubFindByEo(List.of(r1));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_17, Status.A, DrCrCtlFlag.A, "N", "Y");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17)).thenReturn(t1);

        ST010OutputBO output = st010Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getTransferNoRecvNoPayFlag());
        assertEquals("RES2026090100000001", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_17, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.A, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("N", output.getTransferFlag());
        assertEquals("Y", output.getStopFlag());
    }

    // 场景：两条生效限制记录逐条判断，第一条（可疑账户处置-部分止付，禁止贷方、转账标志Y）不命中，第二条命中，提前返回第二条记录信息
    @Test
    void testST010T02() {
        RbBusRestraintsEO r1 = buildRestraints("RES2026090100000001", RestraintType.VALUE_18);
        RbBusRestraintsEO r2 = buildRestraints("RES2026090100000002", RestraintType.VALUE_17);
        stubFindByEo(List.of(r1, r2));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_18, Status.A, DrCrCtlFlag.C, "Y", "N");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_18)).thenReturn(t1);
        RbRestraintTypeEO t2 = buildRestraintType(RestraintType.VALUE_17, Status.A, DrCrCtlFlag.A, "N", "Y");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17)).thenReturn(t2);

        ST010OutputBO output = st010Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getTransferNoRecvNoPayFlag());
        assertEquals("RES2026090100000002", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_17, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.A, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("N", output.getTransferFlag());
        assertEquals("Y", output.getStopFlag());
    }

    // 场景：单条生效限制记录，类型记录状态有效、转账标志=N，但借贷方控制标志=C-禁止贷方（条件左操作数不成立），返回"否"且记录信息照常返回
    @Test
    void testST010T03() {
        RbBusRestraintsEO r1 = buildRestraints("RES2026090100000001", RestraintType.VALUE_17);
        stubFindByEo(List.of(r1));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_17, Status.A, DrCrCtlFlag.C, "N", "N");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17)).thenReturn(t1);

        ST010OutputBO output = st010Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getTransferNoRecvNoPayFlag());
        assertEquals("RES2026090100000001", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_17, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.C, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("N", output.getTransferFlag());
        assertEquals("N", output.getStopFlag());
    }

    // 场景：单条生效限制记录，类型记录状态有效、借贷方控制标志=A-禁止借贷方，但转账标志=Y（条件右操作数不成立），返回"否"且记录信息照常返回
    @Test
    void testST010T04() {
        RbBusRestraintsEO r1 = buildRestraints("RES2026090100000001", RestraintType.VALUE_17);
        stubFindByEo(List.of(r1));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_17, Status.A, DrCrCtlFlag.A, "Y", "N");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17)).thenReturn(t1);

        ST010OutputBO output = st010Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getTransferNoRecvNoPayFlag());
        assertEquals("RES2026090100000001", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_17, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.A, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("Y", output.getTransferFlag());
        assertEquals("N", output.getStopFlag());
    }

    // 场景：账号无生效限制记录（查询返回空列表），循环体不执行、类型查询未触达，返回"否"且记录字段全部为空
    @Test
    void testST010T05() {
        stubFindByEo(Collections.emptyList());

        ST010OutputBO output = st010Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getTransferNoRecvNoPayFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getTransferFlag());
        assertNull(output.getStopFlag());
    }

    // 场景：存在生效限制记录，但按其限制类型查询限制类型表无记录（返回 null），条件不成立返回"否"，账户限制信息仍返回、类型来源字段为空
    @Test
    void testST010T06() {
        RbBusRestraintsEO r1 = buildRestraints("RES2026090100000001", RestraintType.VALUE_17);
        stubFindByEo(List.of(r1));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17)).thenReturn(null);

        ST010OutputBO output = st010Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getTransferNoRecvNoPayFlag());
        assertEquals("RES2026090100000001", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_17, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getTransferFlag());
        assertNull(output.getStopFlag());
    }

    // 场景：存在生效限制记录，类型记录存在且借贷方控制标志=A、转账标志=N，但状态=F-无效（非A-生效），其标志不作为判断与输出取值，返回"否"且类型来源字段为空
    @Test
    void testST010T07() {
        RbBusRestraintsEO r1 = buildRestraints("RES2026090100000001", RestraintType.VALUE_17);
        stubFindByEo(List.of(r1));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_17, Status.F, DrCrCtlFlag.A, "N", "Y");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17)).thenReturn(t1);

        ST010OutputBO output = st010Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getTransferNoRecvNoPayFlag());
        assertEquals("RES2026090100000001", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_17, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getTransferFlag());
        assertNull(output.getStopFlag());
    }

    /** 构造输入：账号为唯一必填字段 */
    private ST010InputBO buildInput() {
        ST010InputBO input = new ST010InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);
        return input;
    }

    /** 设桩：按账号+限制状态A-生效查询账户限制信息，返回给定记录列表 */
    private void stubFindByEo(List<RbBusRestraintsEO> resultList) {
        lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
                && BASE_ACCT_NO.equals(eo.getBaseAcctNo())
                && RestraintsStatus.A == eo.getRestraintsStatus()))).thenReturn(resultList);
    }

    /** 构造账户限制信息记录（RB_BUS_RESTRAINTS） */
    private RbBusRestraintsEO buildRestraints(String resSeqNo, RestraintType restraintType) {
        RbBusRestraintsEO eo = new RbBusRestraintsEO();
        eo.setBaseAcctNo(BASE_ACCT_NO);
        eo.setResSeqNo(resSeqNo);
        eo.setRestraintType(restraintType);
        eo.setRestraintsStatus(RestraintsStatus.A);
        return eo;
    }

    /** 构造限制类型记录（RB_RESTRAINT_TYPE） */
    private RbRestraintTypeEO buildRestraintType(RestraintType restraintType, Status status,
            DrCrCtlFlag drCrCtlFlag, String transferFlag, String stopFlag) {
        RbRestraintTypeEO eo = new RbRestraintTypeEO();
        eo.setRestraintType(restraintType);
        eo.setStatus(status);
        eo.setDrCrCtlFlag(drCrCtlFlag);
        eo.setTransferFlag(transferFlag);
        eo.setStopFlag(stopFlag);
        return eo;
    }
}

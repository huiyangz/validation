package com.dcits.validation.step;

import java.util.Collections;
import java.util.List;

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
import com.dcits.validation.facade.bo.ST006InputBO;
import com.dcits.validation.facade.bo.ST006OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ST006 检查是否存在转账止付限制 单元测试。
 */
@ExtendWith(MockitoExtension.class)
public class ST006PbcTest {

    private static final String BASE_ACCT_NO = "6232500010000001";

    @Mock
    private IRbBusRestraintsBcc rbBusRestraintsBcc;

    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;

    @InjectMocks
    private ST006Pbc st006Pbc;

    // 场景：单条挂失止付限制满足 借贷方控制标志=D-禁止借方 且 转账标志=N，判定"是"，输出取该条记录值
    @Test
    public void testST006T01() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(e ->
                        e != null
                                && BASE_ACCT_NO.equals(e.getBaseAcctNo())
                                && RestraintsStatus.A == e.getRestraintsStatus())))
                .thenReturn(List.of(buildRestraint("RS20260926000001", RestraintType.VALUE_13)));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(buildRestraintType(Status.A, DrCrCtlFlag.D, "N"));

        ST006OutputBO output = st006Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getStopFlag());
        assertEquals("RS20260926000001", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_13, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.D, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("N", output.getTransferFlag());
    }

    // 场景：两条记录，第1条借贷方控制标志=C 不满足，第2条满足 D+N，判定"是"，输出取第一条满足条件记录（第2条）
    @Test
    public void testST006T02() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(e ->
                        e != null
                                && BASE_ACCT_NO.equals(e.getBaseAcctNo())
                                && RestraintsStatus.A == e.getRestraintsStatus())))
                .thenReturn(List.of(
                        buildRestraint("RS20260926000011", RestraintType.VALUE_92),
                        buildRestraint("RS20260926000012", RestraintType.VALUE_13)));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_92))
                .thenReturn(buildRestraintType(Status.A, DrCrCtlFlag.C, "N"));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(buildRestraintType(Status.A, DrCrCtlFlag.D, "N"));

        ST006OutputBO output = st006Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getStopFlag());
        assertEquals("RS20260926000012", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_13, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.D, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("N", output.getTransferFlag());
    }

    // 场景：两条记录的限制类型均满足 D+N，判定"是"，输出取最早出现的第一条满足条件记录（第1条）
    @Test
    public void testST006T03() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(e ->
                        e != null
                                && BASE_ACCT_NO.equals(e.getBaseAcctNo())
                                && RestraintsStatus.A == e.getRestraintsStatus())))
                .thenReturn(List.of(
                        buildRestraint("RS20260926000021", RestraintType.VALUE_13),
                        buildRestraint("RS20260926000022", RestraintType.VALUE_57)));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(buildRestraintType(Status.A, DrCrCtlFlag.D, "N"));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_57))
                .thenReturn(buildRestraintType(Status.A, DrCrCtlFlag.D, "N"));

        ST006OutputBO output = st006Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getStopFlag());
        assertEquals("RS20260926000021", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_13, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.D, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("N", output.getTransferFlag());
    }

    // 场景：账户限制信息查询结果为0条，无记录可查，判定"否"，查询类输出字段均为空
    @Test
    public void testST006T04() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(e ->
                        e != null
                                && BASE_ACCT_NO.equals(e.getBaseAcctNo())
                                && RestraintsStatus.A == e.getRestraintsStatus())))
                .thenReturn(Collections.emptyList());

        ST006OutputBO output = st006Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getStopFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getTransferFlag());
    }

    // 场景：单条记录借贷方控制标志=D 但转账标志="Y"，"且"条件不成立，判定"否"，查询类输出字段均为空
    @Test
    public void testST006T05() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(e ->
                        e != null
                                && BASE_ACCT_NO.equals(e.getBaseAcctNo())
                                && RestraintsStatus.A == e.getRestraintsStatus())))
                .thenReturn(List.of(buildRestraint("RS20260926000041", RestraintType.VALUE_91)));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_91))
                .thenReturn(buildRestraintType(Status.A, DrCrCtlFlag.D, "Y"));

        ST006OutputBO output = st006Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getStopFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getTransferFlag());
    }

    // 场景：单条记录的限制类型记录不存在（findByRestraintType 返回 null），该条视为不满足，判定"否"
    @Test
    public void testST006T06() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(e ->
                        e != null
                                && BASE_ACCT_NO.equals(e.getBaseAcctNo())
                                && RestraintsStatus.A == e.getRestraintsStatus())))
                .thenReturn(List.of(buildRestraint("RS20260926000051", RestraintType.VALUE_4)));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_4))
                .thenReturn(null);

        ST006OutputBO output = st006Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getStopFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getTransferFlag());
    }

    // 场景：单条记录的限制类型状态=Status.C（不等于 A-生效），即使 D+N 也视为不满足，判定"否"
    @Test
    public void testST006T07() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(e ->
                        e != null
                                && BASE_ACCT_NO.equals(e.getBaseAcctNo())
                                && RestraintsStatus.A == e.getRestraintsStatus())))
                .thenReturn(List.of(buildRestraint("RS20260926000061", RestraintType.VALUE_13)));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(buildRestraintType(Status.C, DrCrCtlFlag.D, "N"));

        ST006OutputBO output = st006Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getStopFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getTransferFlag());
    }

    // 场景：两条记录全部不满足（第1条类型记录不存在、第2条借贷方控制标志=C），遍历完成判定"否"
    @Test
    public void testST006T08() {
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.argThat(e ->
                        e != null
                                && BASE_ACCT_NO.equals(e.getBaseAcctNo())
                                && RestraintsStatus.A == e.getRestraintsStatus())))
                .thenReturn(List.of(
                        buildRestraint("RS20260926000071", RestraintType.VALUE_4),
                        buildRestraint("RS20260926000072", RestraintType.VALUE_92)));
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_4))
                .thenReturn(null);
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_92))
                .thenReturn(buildRestraintType(Status.A, DrCrCtlFlag.C, "N"));

        ST006OutputBO output = st006Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getStopFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertNull(output.getTransferFlag());
    }

    private ST006InputBO buildInput() {
        ST006InputBO input = new ST006InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);
        return input;
    }

    private RbBusRestraintsEO buildRestraint(String resSeqNo, RestraintType restraintType) {
        RbBusRestraintsEO eo = new RbBusRestraintsEO();
        eo.setBaseAcctNo(BASE_ACCT_NO);
        eo.setResSeqNo(resSeqNo);
        eo.setRestraintType(restraintType);
        eo.setRestraintsStatus(RestraintsStatus.A);
        return eo;
    }

    private RbRestraintTypeEO buildRestraintType(Status status, DrCrCtlFlag drCrCtlFlag, String transferFlag) {
        RbRestraintTypeEO eo = new RbRestraintTypeEO();
        eo.setStatus(status);
        eo.setDrCrCtlFlag(drCrCtlFlag);
        eo.setTransferFlag(transferFlag);
        return eo;
    }
}

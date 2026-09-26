package com.dcits.validation.step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dcits.validation.facade.bo.ST012InputBO;
import com.dcits.validation.facade.bo.ST012OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;
import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;

/**
 * ST012 检查是否存在不收不付限制 单元测试
 */
@ExtendWith(MockitoExtension.class)
public class ST012PbcTest {

    private static final String BASE_ACCT_NO = "6212345678901234567";

    @Mock
    private IRbBusRestraintsBcc rbBusRestraintsBcc;

    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;

    @InjectMocks
    private ST012Pbc st012Pbc;

    private ST012InputBO buildInput() {
        ST012InputBO input = new ST012InputBO();
        input.setBaseAcctNo(BASE_ACCT_NO);
        return input;
    }

    private RbBusRestraintsEO buildRestraints(String resSeqNo, RestraintType restraintType) {
        RbBusRestraintsEO eo = new RbBusRestraintsEO();
        eo.setResSeqNo(resSeqNo);
        eo.setRestraintType(restraintType);
        eo.setRestraintsStatus(RestraintsStatus.A);
        return eo;
    }

    private RbRestraintTypeEO buildRestraintType(RestraintType restraintType, Status status,
            DrCrCtlFlag drCrCtlFlag) {
        RbRestraintTypeEO eo = new RbRestraintTypeEO();
        eo.setRestraintType(restraintType);
        eo.setStatus(status);
        eo.setDrCrCtlFlag(drCrCtlFlag);
        return eo;
    }

    private void stubFindByEo(List<RbBusRestraintsEO> resultList) {
        lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
                && BASE_ACCT_NO.equals(eo.getBaseAcctNo())
                && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenReturn(resultList);
    }

    // 场景：账户存在一条生效限制记录（可疑账户处置-不收不付），限制类型表中该类型状态为A-生效且借贷方控制标志为A-禁止借贷方，判定存在不收不付限制，输出取该记录及类型记录的值
    @Test
    void testST012T01() {
        AtomicReference<RbBusRestraintsEO> capturedQuery = new AtomicReference<>();
        RbBusRestraintsEO r1 = buildRestraints("RES20260926000001", RestraintType.VALUE_17);
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_17, Status.A, DrCrCtlFlag.A);
        lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
                && BASE_ACCT_NO.equals(eo.getBaseAcctNo())
                && RestraintsStatus.A == eo.getRestraintsStatus())))
                .thenAnswer(invocation -> {
                    capturedQuery.set(invocation.getArgument(0));
                    return Arrays.asList(r1);
                });
        AtomicReference<RestraintType> capturedType = new AtomicReference<>();
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17))
                .thenAnswer(invocation -> {
                    capturedType.set(invocation.getArgument(0));
                    return t1;
                });

        ST012OutputBO output = st012Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getNoDebitNoCreditFlag());
        assertEquals("RES20260926000001", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_17, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.A, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals(BASE_ACCT_NO, capturedQuery.get().getBaseAcctNo());
        assertEquals(RestraintsStatus.A, capturedQuery.get().getRestraintsStatus());
        assertEquals(RestraintType.VALUE_17, capturedType.get());
    }

    // 场景：账户无任何生效限制记录（findByEo 返回空列表），返回"否"且其余输出字段为空
    @Test
    void testST012T02() {
        stubFindByEo(Arrays.asList());

        ST012OutputBO output = st012Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getNoDebitNoCreditFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
    }

    // 场景：账户有一条生效限制记录（其他-部分止付），限制类型记录状态A-生效但借贷方控制标志为C-禁止贷方，判定不存在不收不付限制，返回"否"且其余输出字段为空
    @Test
    void testST012T03() {
        RbBusRestraintsEO r1 = buildRestraints("RES20260926000003", RestraintType.VALUE_53);
        stubFindByEo(Arrays.asList(r1));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_53, Status.A, DrCrCtlFlag.C);
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_53))
                .thenReturn(t1);

        ST012OutputBO output = st012Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getNoDebitNoCreditFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
    }

    // 场景：账户有两条生效限制记录，第一条（其他-部分止付，标志D-禁止借方）未命中后继续循环，第二条（未提供有效身份证件-不收不付，标志A-禁止借贷方）命中，输出取第二条记录的值
    @Test
    void testST012T04() {
        RbBusRestraintsEO r1 = buildRestraints("RES20260926000004", RestraintType.VALUE_53);
        RbBusRestraintsEO r2 = buildRestraints("RES20260926000005", RestraintType.VALUE_20);
        stubFindByEo(Arrays.asList(r1, r2));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_53, Status.A, DrCrCtlFlag.D);
        RbRestraintTypeEO t2 = buildRestraintType(RestraintType.VALUE_20, Status.A, DrCrCtlFlag.A);
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_53))
                .thenReturn(t1);
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_20))
                .thenReturn(t2);

        ST012OutputBO output = st012Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getNoDebitNoCreditFlag());
        assertEquals("RES20260926000005", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_20, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.A, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
    }

    // 场景：账户有两条生效限制记录，借贷方控制标志分别为C-禁止贷方与D-禁止借方，循环完整遍历后全部未命中，返回"否"且其余输出字段为空
    @Test
    void testST012T05() {
        RbBusRestraintsEO r1 = buildRestraints("RES20260926000006", RestraintType.VALUE_53);
        RbBusRestraintsEO r2 = buildRestraints("RES20260926000007", RestraintType.VALUE_7);
        stubFindByEo(Arrays.asList(r1, r2));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_53, Status.A, DrCrCtlFlag.C);
        RbRestraintTypeEO t2 = buildRestraintType(RestraintType.VALUE_7, Status.A, DrCrCtlFlag.D);
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_53))
                .thenReturn(t1);
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_7))
                .thenReturn(t2);

        ST012OutputBO output = st012Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getNoDebitNoCreditFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
    }

    // 场景：账户有一条生效限制记录（不允许现金存入），限制类型表记录状态为C-非活动状态（非A-生效），其借贷方控制标志即使为A-禁止借贷方也不参与判定，返回"否"且其余输出字段为空
    @Test
    void testST012T06() {
        RbBusRestraintsEO r1 = buildRestraints("RES20260926000008", RestraintType.VALUE_6);
        stubFindByEo(Arrays.asList(r1));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_6, Status.C, DrCrCtlFlag.A);
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_6))
                .thenReturn(t1);

        ST012OutputBO output = st012Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getNoDebitNoCreditFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
    }

    // 场景：账户有一条生效限制记录（挂失止付），限制类型表查无该类型记录（findByRestraintType 返回 null），无借贷方控制标志可判定，返回"否"且其余输出字段为空
    @Test
    void testST012T07() {
        RbBusRestraintsEO r1 = buildRestraints("RES20260926000009", RestraintType.VALUE_13);
        stubFindByEo(Arrays.asList(r1));
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(null);

        ST012OutputBO output = st012Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getNoDebitNoCreditFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
    }

    // 场景：账户有两条生效限制记录，第一条（可疑账户处置-不收不付，标志A-禁止借贷方）即命中提前返回，第二条不再执行子步骤2、3，输出取第一条记录的值
    @Test
    void testST012T08() {
        RbBusRestraintsEO r1 = buildRestraints("RES20260926000010", RestraintType.VALUE_17);
        RbBusRestraintsEO r2 = buildRestraints("RES20260926000011", RestraintType.VALUE_53);
        stubFindByEo(Arrays.asList(r1, r2));
        RbRestraintTypeEO t1 = buildRestraintType(RestraintType.VALUE_17, Status.A, DrCrCtlFlag.A);
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_17))
                .thenReturn(t1);

        ST012OutputBO output = st012Pbc.execute(buildInput());

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getNoDebitNoCreditFlag());
        assertEquals("RES20260926000010", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_17, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.A, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
    }
}

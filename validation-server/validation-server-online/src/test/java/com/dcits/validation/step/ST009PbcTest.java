package com.dcits.validation.step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST009InputBO;
import com.dcits.validation.facade.bo.ST009OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/** ST009 检查是否存在止付限制 单元测试 */
@ExtendWith(MockitoExtension.class)
public class ST009PbcTest {
    @Mock
    private IRbBusRestraintsBcc rbBusRestraintsBcc;
    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;
    @InjectMocks
    private ST009Pbc st009Pbc;

    // 场景：账户存在一条生效限制记录（限制类型 VALUE_4），类型表中该类型借贷方控制标志为 D-禁止借方；
    // 预期：成功，止付标志"是"，六个业务输出字段分别取自账户限制记录与限制类型记录
    @Test
    public void testST009T01() {
        RbBusRestraintsEO restraintEo = new RbBusRestraintsEO();
        restraintEo.setBaseAcctNo("20000123456789");
        restraintEo.setRestraintsStatus(RestraintsStatus.A);
        restraintEo.setRestraintType(RestraintType.VALUE_4);
        restraintEo.setResSeqNo("RES20260926000001");
        List<RbBusRestraintsEO> receivedRestraintQueries = new ArrayList<>();
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.any(RbBusRestraintsEO.class)))
                .thenAnswer(invocation -> {
                    receivedRestraintQueries.add(invocation.getArgument(0));
                    return Collections.singletonList(restraintEo);
                });

        RbRestraintTypeEO restraintTypeEo = new RbRestraintTypeEO();
        restraintTypeEo.setRestraintType(RestraintType.VALUE_4);
        restraintTypeEo.setStatus(Status.A);
        restraintTypeEo.setDrCrCtlFlag(DrCrCtlFlag.D);
        List<RbRestraintTypeEO> receivedRestraintTypeQueries = new ArrayList<>();
        Mockito.lenient().when(rbRestraintTypeBcc.findByEo(Mockito.any(RbRestraintTypeEO.class)))
                .thenAnswer(invocation -> {
                    receivedRestraintTypeQueries.add(invocation.getArgument(0));
                    return Collections.singletonList(restraintTypeEo);
                });

        ST009InputBO input = new ST009InputBO();
        input.setBaseAcctNo("20000123456789");

        ST009OutputBO output = st009Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("是", output.getStopFlag());
        assertEquals("RES20260926000001", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_4, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.D, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals(1, receivedRestraintQueries.size());
        assertEquals("20000123456789", receivedRestraintQueries.get(0).getBaseAcctNo());
        assertEquals(RestraintsStatus.A, receivedRestraintQueries.get(0).getRestraintsStatus());
        assertEquals(1, receivedRestraintTypeQueries.size());
        assertEquals(RestraintType.VALUE_4, receivedRestraintTypeQueries.get(0).getRestraintType());
        assertEquals(Status.A, receivedRestraintTypeQueries.get(0).getStatus());
    }

    // 场景：账户存在一条生效限制记录（限制类型 VALUE_6），类型表中该类型借贷方控制标志为 C-禁止贷方（非 D）；
    // 预期：成功，止付标志"否"，六个业务输出字段正常填充
    @Test
    public void testST009T02() {
        RbBusRestraintsEO restraintEo = new RbBusRestraintsEO();
        restraintEo.setBaseAcctNo("20000123456790");
        restraintEo.setRestraintsStatus(RestraintsStatus.A);
        restraintEo.setRestraintType(RestraintType.VALUE_6);
        restraintEo.setResSeqNo("RES20260926000002");
        List<RbBusRestraintsEO> receivedRestraintQueries = new ArrayList<>();
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.any(RbBusRestraintsEO.class)))
                .thenAnswer(invocation -> {
                    receivedRestraintQueries.add(invocation.getArgument(0));
                    return Collections.singletonList(restraintEo);
                });

        RbRestraintTypeEO restraintTypeEo = new RbRestraintTypeEO();
        restraintTypeEo.setRestraintType(RestraintType.VALUE_6);
        restraintTypeEo.setStatus(Status.A);
        restraintTypeEo.setDrCrCtlFlag(DrCrCtlFlag.C);
        List<RbRestraintTypeEO> receivedRestraintTypeQueries = new ArrayList<>();
        Mockito.lenient().when(rbRestraintTypeBcc.findByEo(Mockito.any(RbRestraintTypeEO.class)))
                .thenAnswer(invocation -> {
                    receivedRestraintTypeQueries.add(invocation.getArgument(0));
                    return Collections.singletonList(restraintTypeEo);
                });

        ST009InputBO input = new ST009InputBO();
        input.setBaseAcctNo("20000123456790");

        ST009OutputBO output = st009Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getStopFlag());
        assertEquals("RES20260926000002", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_6, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertEquals(DrCrCtlFlag.C, output.getDrCrCtlFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals(1, receivedRestraintQueries.size());
        assertEquals(1, receivedRestraintTypeQueries.size());
        assertEquals(RestraintType.VALUE_6, receivedRestraintTypeQueries.get(0).getRestraintType());
        assertEquals(Status.A, receivedRestraintTypeQueries.get(0).getStatus());
    }

    // 场景：账号无任何状态为 A-生效的限制记录，账户限制查询返回空列表，子步骤2未触达；
    // 预期：成功，止付标志"否"，五个实体来源输出字段均为 null
    @Test
    public void testST009T03() {
        List<RbBusRestraintsEO> receivedRestraintQueries = new ArrayList<>();
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.any(RbBusRestraintsEO.class)))
                .thenAnswer(invocation -> {
                    receivedRestraintQueries.add(invocation.getArgument(0));
                    return Collections.emptyList();
                });

        ST009InputBO input = new ST009InputBO();
        input.setBaseAcctNo("20000987654321");

        ST009OutputBO output = st009Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getStopFlag());
        assertNull(output.getResSeqNo());
        assertNull(output.getRestraintType());
        assertNull(output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertEquals(1, receivedRestraintQueries.size());
        assertEquals("20000987654321", receivedRestraintQueries.get(0).getBaseAcctNo());
        assertEquals(RestraintsStatus.A, receivedRestraintQueries.get(0).getRestraintsStatus());
    }

    // 场景：账户存在一条生效限制记录（限制类型 VALUE_22），但按该类型 + 状态 A-生效 查询限制类型表返回空列表；
    // 预期：成功，止付标志"否"，账户限制来源字段填充、限制类型表来源字段为 null
    @Test
    public void testST009T04() {
        RbBusRestraintsEO restraintEo = new RbBusRestraintsEO();
        restraintEo.setBaseAcctNo("20000555000111");
        restraintEo.setRestraintsStatus(RestraintsStatus.A);
        restraintEo.setRestraintType(RestraintType.VALUE_22);
        restraintEo.setResSeqNo("RES20260926000003");
        List<RbBusRestraintsEO> receivedRestraintQueries = new ArrayList<>();
        Mockito.lenient().when(rbBusRestraintsBcc.findByEo(Mockito.any(RbBusRestraintsEO.class)))
                .thenAnswer(invocation -> {
                    receivedRestraintQueries.add(invocation.getArgument(0));
                    return Collections.singletonList(restraintEo);
                });

        List<RbRestraintTypeEO> receivedRestraintTypeQueries = new ArrayList<>();
        Mockito.lenient().when(rbRestraintTypeBcc.findByEo(Mockito.any(RbRestraintTypeEO.class)))
                .thenAnswer(invocation -> {
                    receivedRestraintTypeQueries.add(invocation.getArgument(0));
                    return Collections.emptyList();
                });

        ST009InputBO input = new ST009InputBO();
        input.setBaseAcctNo("20000555000111");

        ST009OutputBO output = st009Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("否", output.getStopFlag());
        assertEquals("RES20260926000003", output.getResSeqNo());
        assertEquals(RestraintType.VALUE_22, output.getRestraintType());
        assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
        assertNull(output.getDrCrCtlFlag());
        assertNull(output.getStatus());
        assertEquals(1, receivedRestraintQueries.size());
        assertEquals(1, receivedRestraintTypeQueries.size());
        assertEquals(RestraintType.VALUE_22, receivedRestraintTypeQueries.get(0).getRestraintType());
        assertEquals(Status.A, receivedRestraintTypeQueries.get(0).getStatus());
    }
}

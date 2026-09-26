package com.dcits.validation.step;

import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.facade.bo.ST001InputBO;
import com.dcits.validation.facade.bo.ST001OutputBO;
import com.dcits.validation.facade.components.IRbClientRestraintsBcc;
import com.dcits.validation.facade.eo.RbClientRestraintsEO;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ST001PbcTest {

    @Mock
    private IRbClientRestraintsBcc rbClientRestraintsBcc;

    @InjectMocks
    private ST001Pbc st001Pbc;

    // 场景：客户存在多条生效限制，完整执行查询与赋值返回；预期 succeed=true、错误字段为 null，restraints 含全部返回记录且字段一致，查询条件为客户号+限制状态A
    @Test
    public void testST001T01() {
        RbClientRestraintsEO record1 = new RbClientRestraintsEO();
        record1.setResSeqNo("RS20260924000001");
        record1.setRestraintType(RestraintType.VALUE_13);
        record1.setRestraintsStatus(RestraintsStatus.A);
        RbClientRestraintsEO record2 = new RbClientRestraintsEO();
        record2.setResSeqNo("RS20260924000002");
        record2.setRestraintType(RestraintType.EMR);
        record2.setRestraintsStatus(RestraintsStatus.A);
        final RbClientRestraintsEO[] capturedCondition = new RbClientRestraintsEO[1];
        Mockito.lenient().when(rbClientRestraintsBcc.findByEo(Mockito.any(RbClientRestraintsEO.class)))
                .thenAnswer(invocation -> {
                    capturedCondition[0] = invocation.getArgument(0);
                    return Arrays.asList(record1, record2);
                });

        ST001InputBO input = new ST001InputBO();
        input.setClientNo("C2026092400001");

        ST001OutputBO output = st001Pbc.execute(input);

        Assertions.assertTrue(output.isSucceed());
        Assertions.assertNull(output.getErrorCode());
        Assertions.assertNull(output.getErrorMessage());
        Assertions.assertEquals(2, output.getRestraints().size());
        Assertions.assertEquals("RS20260924000001", output.getRestraints().get(0).getResSeqNo());
        Assertions.assertSame(RestraintType.VALUE_13, output.getRestraints().get(0).getRestraintType());
        Assertions.assertSame(RestraintsStatus.A, output.getRestraints().get(0).getRestraintsStatus());
        Assertions.assertEquals("RS20260924000002", output.getRestraints().get(1).getResSeqNo());
        Assertions.assertSame(RestraintType.EMR, output.getRestraints().get(1).getRestraintType());
        Assertions.assertSame(RestraintsStatus.A, output.getRestraints().get(1).getRestraintsStatus());
        Assertions.assertEquals("C2026092400001", capturedCondition[0].getClientNo());
        Assertions.assertSame(RestraintsStatus.A, capturedCondition[0].getRestraintsStatus());
    }

    // 场景：客户存在单条生效限制，覆盖单元素映射；预期 succeed=true、错误字段为 null，restraints 为 1 条且字段与查询结果一致
    @Test
    public void testST001T02() {
        RbClientRestraintsEO record = new RbClientRestraintsEO();
        record.setResSeqNo("RS20260924000003");
        record.setRestraintType(RestraintType.VALUE_24);
        record.setRestraintsStatus(RestraintsStatus.A);
        Mockito.lenient()
                .when(rbClientRestraintsBcc.findByEo(
                        Mockito.argThat(eo -> "C2026092400002".equals(eo.getClientNo()))))
                .thenReturn(Collections.singletonList(record));

        ST001InputBO input = new ST001InputBO();
        input.setClientNo("C2026092400002");

        ST001OutputBO output = st001Pbc.execute(input);

        Assertions.assertTrue(output.isSucceed());
        Assertions.assertNull(output.getErrorCode());
        Assertions.assertNull(output.getErrorMessage());
        Assertions.assertEquals(1, output.getRestraints().size());
        Assertions.assertEquals("RS20260924000003", output.getRestraints().get(0).getResSeqNo());
        Assertions.assertSame(RestraintType.VALUE_24, output.getRestraints().get(0).getRestraintType());
        Assertions.assertSame(RestraintsStatus.A, output.getRestraints().get(0).getRestraintsStatus());
    }

    // 场景：客户无生效限制，findByEo 返回空 List，覆盖合法空集合；预期 succeed=true、错误字段为 null，restraints 为空集合
    @Test
    public void testST001T03() {
        Mockito.lenient()
                .when(rbClientRestraintsBcc.findByEo(Mockito.any(RbClientRestraintsEO.class)))
                .thenReturn(Collections.emptyList());

        ST001InputBO input = new ST001InputBO();
        input.setClientNo("C2026092400003");

        ST001OutputBO output = st001Pbc.execute(input);

        Assertions.assertTrue(output.isSucceed());
        Assertions.assertNull(output.getErrorCode());
        Assertions.assertNull(output.getErrorMessage());
        Assertions.assertNotNull(output.getRestraints());
        Assertions.assertEquals(0, output.getRestraints().size());
    }
}

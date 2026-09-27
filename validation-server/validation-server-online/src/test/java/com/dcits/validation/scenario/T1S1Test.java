package com.dcits.validation.scenario;

import com.dcits.common.task.RespHeader;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.facade.bo.ST001InputBO;
import com.dcits.validation.facade.bo.ST001OutputBO;
import com.dcits.validation.step.IST001;
import com.dcits.validation.task.dto.T1S1InputDTO;
import com.dcits.validation.task.dto.T1S1OutputDTO;
import com.dcits.validation.task.scenario.T1S1;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class T1S1Test {

    @Mock
    private IST001 ist001;

    @InjectMocks
    private T1S1 t1s1;

    // 场景：客户存在两条生效限制，ST001 成功返回多条记录，场景完成输出映射并显式置成功标志；预期 succeed=true、旧错误被清理、restraints 两条且枚举按业务值（VALUE_16→"16"）转换为 String
    @Test
    public void testT1S1T01() {
        ST001OutputBO.RestraintDTO restraint1 = new ST001OutputBO.RestraintDTO();
        restraint1.setResSeqNo("RS20260924000001");
        restraint1.setRestraintType(RestraintType.VALUE_16);
        restraint1.setRestraintsStatus(RestraintsStatus.A);
        ST001OutputBO.RestraintDTO restraint2 = new ST001OutputBO.RestraintDTO();
        restraint2.setResSeqNo("RS20260924000002");
        restraint2.setRestraintType(RestraintType.EMR);
        restraint2.setRestraintsStatus(RestraintsStatus.A);
        ST001OutputBO st001Output = new ST001OutputBO();
        st001Output.setSucceed(true);
        st001Output.setRestraints(new ArrayList<>(List.of(restraint1, restraint2)));
        final String[] capturedClientNo = new String[1];
        Mockito.lenient().when(ist001.execute(Mockito.any(ST001InputBO.class)))
                .thenAnswer(invocation -> {
                    capturedClientNo[0] = ((ST001InputBO) invocation.getArgument(0)).getClientNo();
                    return st001Output;
                });

        RespHeader header = new RespHeader();
        header.setErrorCode("ER0001");
        header.setErrorMessage("旧错误");
        T1S1InputDTO input = new T1S1InputDTO();
        input.setClientNo("C2026092400001");

        T1S1OutputDTO output = t1s1.execute(header, input);

        Assertions.assertEquals("C2026092400001", capturedClientNo[0]);
        Assertions.assertTrue(header.isSucceed());
        Assertions.assertNull(header.getErrorCode());
        Assertions.assertNull(header.getErrorMessage());
        Assertions.assertEquals(2, output.getRestraints().size());
        Assertions.assertEquals("RS20260924000001", output.getRestraints().get(0).getResSeqNo());
        Assertions.assertEquals("16", output.getRestraints().get(0).getRestraintType());
        Assertions.assertEquals("A", output.getRestraints().get(0).getRestraintsStatus());
        Assertions.assertEquals("RS20260924000002", output.getRestraints().get(1).getResSeqNo());
        Assertions.assertEquals("EMR", output.getRestraints().get(1).getRestraintType());
        Assertions.assertEquals("A", output.getRestraints().get(1).getRestraintsStatus());
    }

    // 场景：客户无任何生效限制，ST001 成功返回空集合，场景成功完成；预期 succeed=true、错误字段为 null，restraints 为空集合
    @Test
    public void testT1S1T02() {
        ST001OutputBO st001Output = new ST001OutputBO();
        st001Output.setSucceed(true);
        st001Output.setRestraints(new ArrayList<ST001OutputBO.RestraintDTO>());
        final String[] capturedClientNo = new String[1];
        Mockito.lenient().when(ist001.execute(Mockito.any(ST001InputBO.class)))
                .thenAnswer(invocation -> {
                    capturedClientNo[0] = ((ST001InputBO) invocation.getArgument(0)).getClientNo();
                    return st001Output;
                });

        RespHeader header = new RespHeader();
        T1S1InputDTO input = new T1S1InputDTO();
        input.setClientNo("C2026092400002");

        T1S1OutputDTO output = t1s1.execute(header, input);

        Assertions.assertEquals("C2026092400002", capturedClientNo[0]);
        Assertions.assertTrue(header.isSucceed());
        Assertions.assertNull(header.getErrorCode());
        Assertions.assertNull(header.getErrorMessage());
        Assertions.assertNotNull(output.getRestraints());
        Assertions.assertEquals(0, output.getRestraints().size());
    }
}

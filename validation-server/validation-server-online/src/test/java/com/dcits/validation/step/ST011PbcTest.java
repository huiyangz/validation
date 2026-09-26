package com.dcits.validation.step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

import com.dcits.validation.enums.OthTranType;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.facade.bo.ST011InputBO;
import com.dcits.validation.facade.bo.ST011OutputBO;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.components.IRbTranDefBcc;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;
import com.dcits.validation.facade.eo.RbTranDefEO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ST011PbcTest {
    @Mock
    private IRbTranDefBcc rbTranDefBcc;
    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;
    @InjectMocks
    private ST011Pbc st011Pbc;

    // ST011-TC001：交易限制级别数值大于冻结级别（现金存入“3”>统一查控平台冻结“1”），返回“不检查限制”
    @Test
    public void testST011T01() {
        ST011InputBO input = new ST011InputBO();
        input.setTranType(OthTranType.VALUE_1000);
        input.setRestraintType(RestraintType.VALUE_5);

        RbTranDefEO tranDef = new RbTranDefEO();
        tranDef.setResPriority("3");
        lenient().when(rbTranDefBcc.findByTranType(OthTranType.VALUE_1000)).thenReturn(tranDef);

        RbRestraintTypeEO restraintTypeDef = new RbRestraintTypeEO();
        restraintTypeDef.setResPriority("1");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_5)).thenReturn(restraintTypeDef);

        ST011OutputBO output = st011Pbc.execute(input);
        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("不检查限制", output.getCheckResult());
    }

    // ST011-TC002：交易限制级别数值等于冻结级别（现金支取“2”=挂失止付“2”），“大于”不成立走否则分支，返回“继续检查”
    @Test
    public void testST011T02() {
        ST011InputBO input = new ST011InputBO();
        input.setTranType(OthTranType.VALUE_1003);
        input.setRestraintType(RestraintType.VALUE_13);

        RbTranDefEO tranDef = new RbTranDefEO();
        tranDef.setResPriority("2");
        lenient().when(rbTranDefBcc.findByTranType(OthTranType.VALUE_1003)).thenReturn(tranDef);

        RbRestraintTypeEO restraintTypeDef = new RbRestraintTypeEO();
        restraintTypeDef.setResPriority("2");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13)).thenReturn(restraintTypeDef);

        ST011OutputBO output = st011Pbc.execute(input);
        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("继续检查", output.getCheckResult());
    }

    // ST011-TC003：交易限制级别数值小于冻结级别（现金支票支取“1”<统一查控平台冻结“4”），走否则分支，返回“继续检查”
    @Test
    public void testST011T03() {
        ST011InputBO input = new ST011InputBO();
        input.setTranType(OthTranType.VALUE_1006);
        input.setRestraintType(RestraintType.VALUE_5);

        RbTranDefEO tranDef = new RbTranDefEO();
        tranDef.setResPriority("1");
        lenient().when(rbTranDefBcc.findByTranType(OthTranType.VALUE_1006)).thenReturn(tranDef);

        RbRestraintTypeEO restraintTypeDef = new RbRestraintTypeEO();
        restraintTypeDef.setResPriority("4");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_5)).thenReturn(restraintTypeDef);

        ST011OutputBO output = st011Pbc.execute(input);
        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("继续检查", output.getCheckResult());
    }

    // ST011-TC004：多位数字编码按数值比较（存款开户存入“10”>挂失止付“9”，字典序会误判为小于），返回“不检查限制”
    @Test
    public void testST011T04() {
        ST011InputBO input = new ST011InputBO();
        input.setTranType(OthTranType.VALUE_4308);
        input.setRestraintType(RestraintType.VALUE_13);

        RbTranDefEO tranDef = new RbTranDefEO();
        tranDef.setResPriority("10");
        lenient().when(rbTranDefBcc.findByTranType(OthTranType.VALUE_4308)).thenReturn(tranDef);

        RbRestraintTypeEO restraintTypeDef = new RbRestraintTypeEO();
        restraintTypeDef.setResPriority("9");
        lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13)).thenReturn(restraintTypeDef);

        ST011OutputBO output = st011Pbc.execute(input);
        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("不检查限制", output.getCheckResult());
    }
}

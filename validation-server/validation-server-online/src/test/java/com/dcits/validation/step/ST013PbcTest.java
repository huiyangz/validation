package com.dcits.validation.step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dcits.validation.enums.RestraintLevel;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.facade.bo.ST013InputBO;
import com.dcits.validation.facade.bo.ST013OutputBO;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/** ST013 检查账户是否存在不允许销户的限制 - 单元测试 */
@ExtendWith(MockitoExtension.class)
public class ST013PbcTest {

    @Mock
    private IRbRestraintTypeBcc rbRestraintTypeBcc;

    @InjectMocks
    private ST013Pbc st013Pbc;

    /** 按字段契约构造账户限制信息DTO元素 */
    private ST013InputBO.RestraintInfoDTO buildRestraintInfo(String resSeqNo, RestraintType restraintType,
            RestraintsStatus restraintsStatus, RestraintLevel restraintLevel) {
        ST013InputBO.RestraintInfoDTO restraintInfo = new ST013InputBO.RestraintInfoDTO();
        restraintInfo.setResSeqNo(resSeqNo);
        restraintInfo.setRestraintType(restraintType);
        restraintInfo.setRestraintsStatus(restraintsStatus);
        restraintInfo.setRestraintLevel(restraintLevel);
        return restraintInfo;
    }

    // 场景：账户限制信息集合为合法空集合，遍历循环体不执行，直接结束遍历；预期成功且允许销户标志为“允许销户”
    @Test
    public void testST013T01() {
        ST013InputBO input = new ST013InputBO();
        input.setRestraintList(new ArrayList<>());

        ST013OutputBO output = st013Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("允许销户", output.getAllowCloseAcctFlag());
    }

    // 场景：单条账户限制记录，查询所得销户标志为非“N”（“Y”），继续遍历后集合结束；预期成功且允许销户标志为“允许销户”
    @Test
    public void testST013T02() {
        RbRestraintTypeEO restraintTypeEO = new RbRestraintTypeEO();
        restraintTypeEO.setRestraintType(RestraintType.VALUE_13);
        restraintTypeEO.setCloseAcctFlag("Y");
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_13))
                .thenReturn(restraintTypeEO);

        ST013InputBO input = new ST013InputBO();
        input.setRestraintList(Arrays.asList(
                buildRestraintInfo("R0001", RestraintType.VALUE_13, RestraintsStatus.A, RestraintLevel.ACCT)));

        ST013OutputBO output = st013Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("允许销户", output.getAllowCloseAcctFlag());
    }

    // 场景：三条账户限制记录，销户标志均为非“N”，遍历完整执行不中断，三条记录依次作为查询输入；预期成功且允许销户标志为“允许销户”
    @Test
    public void testST013T03() {
        List<RestraintType> queriedRestraintTypes = new ArrayList<>();
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(Mockito.any(RestraintType.class)))
                .thenAnswer(invocation -> {
                    RestraintType requested = invocation.getArgument(0);
                    queriedRestraintTypes.add(requested);
                    RbRestraintTypeEO restraintTypeEO = new RbRestraintTypeEO();
                    restraintTypeEO.setRestraintType(requested);
                    restraintTypeEO.setCloseAcctFlag("Y");
                    return restraintTypeEO;
                });

        ST013InputBO input = new ST013InputBO();
        input.setRestraintList(Arrays.asList(
                buildRestraintInfo("R0001", RestraintType.VALUE_16, RestraintsStatus.A, RestraintLevel.ACCT),
                buildRestraintInfo("R0002", RestraintType.VALUE_13, RestraintsStatus.A, RestraintLevel.ACCT),
                buildRestraintInfo("R0003", RestraintType.VALUE_17, RestraintsStatus.A, RestraintLevel.CARD)));

        ST013OutputBO output = st013Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("允许销户", output.getAllowCloseAcctFlag());
        assertEquals(Arrays.asList(RestraintType.VALUE_16, RestraintType.VALUE_13, RestraintType.VALUE_17),
                queriedRestraintTypes);
    }

    // 场景：三条记录中第2条（VALUE_13）查询所得销户标志为“N-否”，返回“不允许销户”并中断遍历，第3条记录不再被处理；预期成功且允许销户标志为“不允许销户”
    @Test
    public void testST013T04() {
        List<RestraintType> queriedRestraintTypes = new ArrayList<>();
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(Mockito.any(RestraintType.class)))
                .thenAnswer(invocation -> {
                    RestraintType requested = invocation.getArgument(0);
                    queriedRestraintTypes.add(requested);
                    RbRestraintTypeEO restraintTypeEO = new RbRestraintTypeEO();
                    restraintTypeEO.setRestraintType(requested);
                    restraintTypeEO.setCloseAcctFlag(requested == RestraintType.VALUE_13 ? "N" : "Y");
                    return restraintTypeEO;
                });

        ST013InputBO input = new ST013InputBO();
        input.setRestraintList(Arrays.asList(
                buildRestraintInfo("R0001", RestraintType.VALUE_16, RestraintsStatus.A, RestraintLevel.ACCT),
                buildRestraintInfo("R0002", RestraintType.VALUE_13, RestraintsStatus.A, RestraintLevel.ACCT),
                buildRestraintInfo("R0003", RestraintType.VALUE_17, RestraintsStatus.A, RestraintLevel.CARD)));

        ST013OutputBO output = st013Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("不允许销户", output.getAllowCloseAcctFlag());
        assertEquals(Arrays.asList(RestraintType.VALUE_16, RestraintType.VALUE_13), queriedRestraintTypes);
    }

    // 场景：单条账户限制记录，查询所得销户标志为“N-否”，首次迭代即返回“不允许销户”并中断遍历；预期成功且允许销户标志为“不允许销户”
    @Test
    public void testST013T05() {
        RbRestraintTypeEO restraintTypeEO = new RbRestraintTypeEO();
        restraintTypeEO.setRestraintType(RestraintType.VALUE_16);
        restraintTypeEO.setCloseAcctFlag("N");
        Mockito.lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_16))
                .thenReturn(restraintTypeEO);

        ST013InputBO input = new ST013InputBO();
        input.setRestraintList(Arrays.asList(
                buildRestraintInfo("R0001", RestraintType.VALUE_16, RestraintsStatus.A, RestraintLevel.ACCT)));

        ST013OutputBO output = st013Pbc.execute(input);

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("不允许销户", output.getAllowCloseAcctFlag());
    }
}

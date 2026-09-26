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

import com.dcits.validation.enums.OthTranType;
import com.dcits.validation.enums.ResBranchRange;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.SourceType;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST002InputBO;
import com.dcits.validation.facade.bo.ST002OutputBO;
import com.dcits.validation.facade.components.IFmChannelBcc;
import com.dcits.validation.facade.components.IRbRestraintControlDetailsBcc;
import com.dcits.validation.facade.eo.FmChannelEO;
import com.dcits.validation.facade.eo.RbRestraintControlDetailsEO;

/** ST002 检查限制豁免 单元测试 */
@ExtendWith(MockitoExtension.class)
class ST002PbcTest {

    @Mock
    private IFmChannelBcc fmChannelBcc;

    @Mock
    private IRbRestraintControlDetailsBcc rbRestraintControlDetailsBcc;

    @InjectMocks
    private ST002Pbc st002Pbc;

    /** 构造渠道类型表记录 */
    private FmChannelEO channel(SourceType channel, String counterFlag) {
        FmChannelEO channelEO = new FmChannelEO();
        channelEO.setChannel(channel);
        channelEO.setCounterFlag(counterFlag);
        return channelEO;
    }

    /** 构造生效限制控制明细记录（restraintType=VALUE_6、status=A、batchFlag='N'、expression='E001'，时间戳固定） */
    private RbRestraintControlDetailsEO detail(String prodNo, String tranTypeLink, String narrativeCode,
            String channelMuster, ResBranchRange resBranchRange, String counterFlag) {
        RbRestraintControlDetailsEO eo = new RbRestraintControlDetailsEO();
        eo.setRestraintType(RestraintType.VALUE_6);
        eo.setStatus(Status.A);
        eo.setProdNo(prodNo);
        eo.setTranTypeLink(tranTypeLink);
        eo.setNarrativeCode(narrativeCode);
        eo.setChannelMuster(channelMuster);
        eo.setResBranchRange(resBranchRange);
        eo.setBatchFlag("N");
        eo.setCounterFlag(counterFlag);
        eo.setExpression("E001");
        eo.setCreateTimestamp("20260924000000");
        eo.setLastUpdTimestamp("20260924000000");
        return eo;
    }

    /** 构造步骤输入 */
    private ST002InputBO input(SourceType sourceType, RestraintType restraintType, OthTranType tranType,
            String narrativeCode, String prodType) {
        ST002InputBO input = new ST002InputBO();
        input.setSourceType(sourceType);
        input.setRestraintType(restraintType);
        input.setTranType(tranType);
        input.setNarrativeCode(narrativeCode);
        input.setProdType(prodType);
        return input;
    }

    /** 柜面渠道、明细含柜面标志 Y 且三项匹配，跳子步骤4 命中，返回“不检查限制”并回显渠道与明细字段 */
    @Test
    void testST002T01() {
        lenient().when(fmChannelBcc.findByChannel(SourceType.MT)).thenReturn(channel(SourceType.MT, "Y"));
        lenient().when(rbRestraintControlDetailsBcc.findByEo(
                argThat(eo -> eo != null && eo.getRestraintType() == RestraintType.VALUE_6)))
                .thenReturn(List.of(detail("010001", "1000", "2001", "MT", ResBranchRange.A, "Y")));

        ST002OutputBO output = st002Pbc.execute(
                input(SourceType.MT, RestraintType.VALUE_6, OthTranType.VALUE_1000, "2001", "010001"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("不检查限制", output.getCheckResult());
        assertEquals("Y", output.getChannelCounterFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("010001", output.getProdNo());
        assertEquals("1000", output.getTranTypeLink());
        assertEquals("MT", output.getChannelMuster());
        assertEquals("2001", output.getNarrativeCode());
        assertEquals(ResBranchRange.A, output.getResBranchRange());
        assertEquals("Y", output.getDetailCounterFlag());
    }

    /** 柜面渠道、三项匹配中摘要码不匹配（输入 2999 与明细 2001），子步骤4 未命中，返回“需检查限制” */
    @Test
    void testST002T02() {
        lenient().when(fmChannelBcc.findByChannel(SourceType.MT)).thenReturn(channel(SourceType.MT, "Y"));
        lenient().when(rbRestraintControlDetailsBcc.findByEo(
                argThat(eo -> eo != null && eo.getRestraintType() == RestraintType.VALUE_6)))
                .thenReturn(List.of(detail("010001", "1000", "2001", "MT", ResBranchRange.A, "Y")));

        ST002OutputBO output = st002Pbc.execute(
                input(SourceType.MT, RestraintType.VALUE_6, OthTranType.VALUE_1000, "2999", "010001"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("需检查限制", output.getCheckResult());
        assertEquals("Y", output.getChannelCounterFlag());
    }

    /** 渠道柜面标志 Y 但生效明细中无柜面标志 Y 记录（明细柜面标志 N），跳子步骤5 且三项匹配，返回“豁免”并回显唯一匹配记录 */
    @Test
    void testST002T03() {
        lenient().when(fmChannelBcc.findByChannel(SourceType.MT)).thenReturn(channel(SourceType.MT, "Y"));
        lenient().when(rbRestraintControlDetailsBcc.findByEo(
                argThat(eo -> eo != null && eo.getRestraintType() == RestraintType.VALUE_6)))
                .thenReturn(List.of(detail("010001", "1000", "2001", "MT", ResBranchRange.C, "N")));

        ST002OutputBO output = st002Pbc.execute(
                input(SourceType.MT, RestraintType.VALUE_6, OthTranType.VALUE_1000, "2001", "010001"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("豁免", output.getCheckResult());
        assertEquals("Y", output.getChannelCounterFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("010001", output.getProdNo());
        assertEquals("1000", output.getTranTypeLink());
        assertEquals("2001", output.getNarrativeCode());
        assertEquals(ResBranchRange.C, output.getResBranchRange());
        assertEquals("N", output.getDetailCounterFlag());
    }

    /** 非柜面渠道（手机银行、柜面标志 N）、明细含柜面标志 Y 且三项匹配，跳子步骤5 命中，返回“豁免”并回显匹配记录 */
    @Test
    void testST002T04() {
        lenient().when(fmChannelBcc.findByChannel(SourceType.M)).thenReturn(channel(SourceType.M, "N"));
        lenient().when(rbRestraintControlDetailsBcc.findByEo(
                argThat(eo -> eo != null && eo.getRestraintType() == RestraintType.VALUE_6)))
                .thenReturn(List.of(detail("010001", "1000", "2001", "M", ResBranchRange.A, "Y")));

        ST002OutputBO output = st002Pbc.execute(
                input(SourceType.M, RestraintType.VALUE_6, OthTranType.VALUE_1000, "2001", "010001"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("豁免", output.getCheckResult());
        assertEquals("N", output.getChannelCounterFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("010001", output.getProdNo());
        assertEquals("1000", output.getTranTypeLink());
        assertEquals("M", output.getChannelMuster());
        assertEquals("2001", output.getNarrativeCode());
        assertEquals(ResBranchRange.A, output.getResBranchRange());
        assertEquals("Y", output.getDetailCounterFlag());
    }

    /** 非柜面渠道、交易类型 1003 不包含于多交易类型 1000，子步骤5 未命中，返回“不豁免” */
    @Test
    void testST002T05() {
        lenient().when(fmChannelBcc.findByChannel(SourceType.M)).thenReturn(channel(SourceType.M, "N"));
        lenient().when(rbRestraintControlDetailsBcc.findByEo(
                argThat(eo -> eo != null && eo.getRestraintType() == RestraintType.VALUE_6)))
                .thenReturn(List.of(detail("010001", "1000", "2001", "M", ResBranchRange.A, "Y")));

        ST002OutputBO output = st002Pbc.execute(
                input(SourceType.M, RestraintType.VALUE_6, OthTranType.VALUE_1003, "2001", "010001"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("不豁免", output.getCheckResult());
        assertEquals("N", output.getChannelCounterFlag());
    }

    /** 非柜面渠道、账户限制类型 VALUE_7 无生效明细（空列表），返回“不豁免”，明细回显字段为 null */
    @Test
    void testST002T06() {
        lenient().when(fmChannelBcc.findByChannel(SourceType.M)).thenReturn(channel(SourceType.M, "N"));
        lenient().when(rbRestraintControlDetailsBcc.findByEo(
                argThat(eo -> eo != null && eo.getRestraintType() == RestraintType.VALUE_7)))
                .thenReturn(Collections.emptyList());

        ST002OutputBO output = st002Pbc.execute(
                input(SourceType.M, RestraintType.VALUE_7, OthTranType.VALUE_1000, "2001", "010001"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("不豁免", output.getCheckResult());
        assertEquals("N", output.getChannelCounterFlag());
        assertNull(output.getStatus());
        assertNull(output.getProdNo());
        assertNull(output.getTranTypeLink());
        assertNull(output.getChannelMuster());
        assertNull(output.getNarrativeCode());
        assertNull(output.getResBranchRange());
        assertNull(output.getDetailCounterFlag());
    }

    /** 渠道类型 AD 未在渠道类型表配置（findByChannel 返回 null），柜面标志不等于 Y，跳子步骤5 且三项匹配，返回“豁免”，channelCounterFlag 为 null */
    @Test
    void testST002T07() {
        lenient().when(fmChannelBcc.findByChannel(SourceType.AD)).thenReturn(null);
        lenient().when(rbRestraintControlDetailsBcc.findByEo(
                argThat(eo -> eo != null && eo.getRestraintType() == RestraintType.VALUE_6)))
                .thenReturn(List.of(detail("010001", "1000", "2001", "AD", ResBranchRange.A, "Y")));

        ST002OutputBO output = st002Pbc.execute(
                input(SourceType.AD, RestraintType.VALUE_6, OthTranType.VALUE_1000, "2001", "010001"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("豁免", output.getCheckResult());
        assertNull(output.getChannelCounterFlag());
        assertEquals("010001", output.getProdNo());
        assertEquals("1000", output.getTranTypeLink());
        assertEquals("2001", output.getNarrativeCode());
        assertEquals("Y", output.getDetailCounterFlag());
    }

    /** 柜面渠道、两条生效明细均柜面标志 Y，首条产品类型不匹配、次条三项匹配，子步骤4 遍历命中次条，返回“不检查限制”并回显次条记录 */
    @Test
    void testST002T08() {
        lenient().when(fmChannelBcc.findByChannel(SourceType.MT)).thenReturn(channel(SourceType.MT, "Y"));
        lenient().when(rbRestraintControlDetailsBcc.findByEo(
                argThat(eo -> eo != null && eo.getRestraintType() == RestraintType.VALUE_6)))
                .thenReturn(List.of(
                        detail("090009", "1000", "2001", "MT", ResBranchRange.A, "Y"),
                        detail("010001", "1000", "2001", "ALL", ResBranchRange.C, "Y")));

        ST002OutputBO output = st002Pbc.execute(
                input(SourceType.MT, RestraintType.VALUE_6, OthTranType.VALUE_1000, "2001", "010001"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("不检查限制", output.getCheckResult());
        assertEquals("Y", output.getChannelCounterFlag());
        assertEquals(Status.A, output.getStatus());
        assertEquals("010001", output.getProdNo());
        assertEquals("1000", output.getTranTypeLink());
        assertEquals("ALL", output.getChannelMuster());
        assertEquals("2001", output.getNarrativeCode());
        assertEquals(ResBranchRange.C, output.getResBranchRange());
        assertEquals("Y", output.getDetailCounterFlag());
    }

    /** 柜面渠道、三项匹配中产品类型不匹配（输入 070007 与明细 010001），子步骤4 未命中，返回“需检查限制” */
    @Test
    void testST002T09() {
        lenient().when(fmChannelBcc.findByChannel(SourceType.MT)).thenReturn(channel(SourceType.MT, "Y"));
        lenient().when(rbRestraintControlDetailsBcc.findByEo(
                argThat(eo -> eo != null && eo.getRestraintType() == RestraintType.VALUE_6)))
                .thenReturn(List.of(detail("010001", "1000", "2001", "MT", ResBranchRange.A, "Y")));

        ST002OutputBO output = st002Pbc.execute(
                input(SourceType.MT, RestraintType.VALUE_6, OthTranType.VALUE_1000, "2001", "070007"));

        assertTrue(output.isSucceed());
        assertNull(output.getErrorCode());
        assertNull(output.getErrorMessage());
        assertEquals("需检查限制", output.getCheckResult());
        assertEquals("Y", output.getChannelCounterFlag());
    }
}

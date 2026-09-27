package com.dcits.validation.scenario;

import com.dcits.common.task.RespHeader;
import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.OthTranType;
import com.dcits.validation.enums.ResBranchRange;
import com.dcits.validation.enums.RestraintLevel;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.SourceType;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST002InputBO;
import com.dcits.validation.facade.bo.ST002OutputBO;
import com.dcits.validation.facade.bo.ST003InputBO;
import com.dcits.validation.facade.bo.ST003OutputBO;
import com.dcits.validation.facade.bo.ST004InputBO;
import com.dcits.validation.facade.bo.ST004OutputBO;
import com.dcits.validation.facade.bo.ST005InputBO;
import com.dcits.validation.facade.bo.ST005OutputBO;
import com.dcits.validation.facade.bo.ST006InputBO;
import com.dcits.validation.facade.bo.ST006OutputBO;
import com.dcits.validation.facade.bo.ST007InputBO;
import com.dcits.validation.facade.bo.ST007OutputBO;
import com.dcits.validation.facade.bo.ST008InputBO;
import com.dcits.validation.facade.bo.ST008OutputBO;
import com.dcits.validation.facade.bo.ST009InputBO;
import com.dcits.validation.facade.bo.ST009OutputBO;
import com.dcits.validation.facade.bo.ST010InputBO;
import com.dcits.validation.facade.bo.ST010OutputBO;
import com.dcits.validation.facade.bo.ST011InputBO;
import com.dcits.validation.facade.bo.ST011OutputBO;
import com.dcits.validation.facade.bo.ST012InputBO;
import com.dcits.validation.facade.bo.ST012OutputBO;
import com.dcits.validation.facade.bo.ST013InputBO;
import com.dcits.validation.facade.bo.ST013OutputBO;
import com.dcits.validation.facade.bo.ST014InputBO;
import com.dcits.validation.facade.bo.ST014OutputBO;
import com.dcits.validation.facade.bo.ST015InputBO;
import com.dcits.validation.facade.bo.ST015OutputBO;
import com.dcits.validation.step.IST002;
import com.dcits.validation.step.IST003;
import com.dcits.validation.step.IST004;
import com.dcits.validation.step.IST005;
import com.dcits.validation.step.IST006;
import com.dcits.validation.step.IST007;
import com.dcits.validation.step.IST008;
import com.dcits.validation.step.IST009;
import com.dcits.validation.step.IST010;
import com.dcits.validation.step.IST011;
import com.dcits.validation.step.IST012;
import com.dcits.validation.step.IST013;
import com.dcits.validation.step.IST014;
import com.dcits.validation.step.IST015;
import com.dcits.validation.task.dto.T2S1InputDTO;
import com.dcits.validation.task.dto.T2S1OutputDTO;
import com.dcits.validation.task.scenario.T2S1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class T2S1Test {

    @Mock
    private IST002 ist002;
    @Mock
    private IST003 ist003;
    @Mock
    private IST004 ist004;
    @Mock
    private IST005 ist005;
    @Mock
    private IST006 ist006;
    @Mock
    private IST007 ist007;
    @Mock
    private IST008 ist008;
    @Mock
    private IST009 ist009;
    @Mock
    private IST010 ist010;
    @Mock
    private IST011 ist011;
    @Mock
    private IST012 ist012;
    @Mock
    private IST013 ist013;
    @Mock
    private IST014 ist014;
    @Mock
    private IST015 ist015;

    @InjectMocks
    private T2S1 t2s1;

    // 场景：T2S1-TC001 全部 14 步顺序成功，各检查命中并返回互不相同的记录值；预期 succeed=true 且旧错误被清理，场景输入到步骤入参（含 String→枚举转换）与步骤输出到场景输出逐项映射符合用例表，无来源 5 项为 null
    @Test
    public void testT2S1T01() {
        ST002OutputBO st002Out = new ST002OutputBO();
        st002Out.setSucceed(true);
        st002Out.setChannelCounterFlag("Y");
        st002Out.setStatus(Status.A);
        st002Out.setProdNo("11002");
        st002Out.setTranTypeLink("1000|1003");
        st002Out.setChannelMuster("MT|MC");
        st002Out.setNarrativeCode("N001");
        st002Out.setResBranchRange(ResBranchRange.A);
        st002Out.setDetailCounterFlag("Y");
        st002Out.setCheckResult("不检查限制");
        ST003OutputBO st003Out = new ST003OutputBO();
        st003Out.setSucceed(true);
        st003Out.setAhBuFlag("Y");
        ST004OutputBO st004Out = new ST004OutputBO();
        st004Out.setSucceed(true);
        st004Out.setResSeqNo("RES20260004");
        st004Out.setRestraintType(RestraintType.VALUE_4);
        st004Out.setRestraintsStatus(RestraintsStatus.A);
        st004Out.setDrCrCtlFlag(DrCrCtlFlag.C);
        st004Out.setStatus(Status.A);
        st004Out.setTransferFlag("N");
        st004Out.setStopFlag("Y");
        st004Out.setTransferStopFlag("是");
        ST005OutputBO st005Out = new ST005OutputBO();
        st005Out.setSucceed(true);
        st005Out.setBaseAcctNo("6100100012345678");
        st005Out.setLeadAcctFlag("Y");
        st005Out.setResSeqNo("RES20260005");
        st005Out.setRestraintType(RestraintType.VALUE_5);
        st005Out.setRestraintsStatus(RestraintsStatus.A);
        ST006OutputBO st006Out = new ST006OutputBO();
        st006Out.setSucceed(true);
        st006Out.setResSeqNo("RES20260006");
        st006Out.setRestraintType(RestraintType.VALUE_6);
        st006Out.setRestraintsStatus(RestraintsStatus.A);
        st006Out.setDrCrCtlFlag(DrCrCtlFlag.D);
        st006Out.setStatus(Status.A);
        st006Out.setTransferFlag("N");
        st006Out.setStopFlag("是");
        ST007OutputBO st007Out = new ST007OutputBO();
        st007Out.setSucceed(true);
        st007Out.setResSeqNo("RES20260007");
        st007Out.setRestraintType(RestraintType.VALUE_7);
        st007Out.setRestraintsStatus(RestraintsStatus.A);
        st007Out.setPledgedFlag("Y");
        st007Out.setStatus(Status.A);
        ST008OutputBO st008Out = new ST008OutputBO();
        st008Out.setSucceed(true);
        st008Out.setResSeqNo("RES20260008");
        st008Out.setRestraintType(RestraintType.VALUE_8);
        st008Out.setRestraintsStatus(RestraintsStatus.A);
        st008Out.setRestraintTypeDef(RestraintType.VALUE_8);
        st008Out.setStatus(Status.A);
        st008Out.setDrCrCtlFlag(DrCrCtlFlag.C);
        st008Out.setCashFlag("N");
        st008Out.setCashStopFlag("是");
        ST009OutputBO st009Out = new ST009OutputBO();
        st009Out.setSucceed(true);
        st009Out.setStopFlag("是");
        st009Out.setResSeqNo("RES20260009");
        st009Out.setRestraintType(RestraintType.VALUE_9);
        st009Out.setRestraintsStatus(RestraintsStatus.A);
        st009Out.setDrCrCtlFlag(DrCrCtlFlag.D);
        st009Out.setStatus(Status.A);
        ST010OutputBO st010Out = new ST010OutputBO();
        st010Out.setSucceed(true);
        st010Out.setTransferNoRecvNoPayFlag("是");
        st010Out.setResSeqNo("RES20260010");
        st010Out.setRestraintType(RestraintType.VALUE_10);
        st010Out.setRestraintsStatus(RestraintsStatus.A);
        st010Out.setDrCrCtlFlag(DrCrCtlFlag.A);
        st010Out.setStatus(Status.A);
        st010Out.setTransferFlag("N");
        st010Out.setStopFlag("N");
        ST011OutputBO st011Out = newSt011ContinueCheck();
        ST012OutputBO st012Out = new ST012OutputBO();
        st012Out.setSucceed(true);
        st012Out.setNoDebitNoCreditFlag("是");
        st012Out.setResSeqNo("RES20260012");
        st012Out.setRestraintType(RestraintType.VALUE_12);
        st012Out.setRestraintsStatus(RestraintsStatus.A);
        st012Out.setDrCrCtlFlag(DrCrCtlFlag.A);
        st012Out.setStatus(Status.A);
        ST013OutputBO st013Out = newSt013AllowClose();
        ST014OutputBO st014Out = new ST014OutputBO();
        st014Out.setSucceed(true);
        st014Out.setCashNonRcvPayFlag("是");
        st014Out.setResSeqNo("RES20260014");
        st014Out.setRestraintType(RestraintType.VALUE_14);
        st014Out.setRestraintsStatus(RestraintsStatus.A);
        st014Out.setDrCrCtlFlag(DrCrCtlFlag.A);
        st014Out.setStatus(Status.A);
        st014Out.setCashFlag("N");
        ST015OutputBO st015Out = new ST015OutputBO();
        st015Out.setSucceed(true);
        st015Out.setNatureRestraintFlag("是");
        st015Out.setResSeqNo("RES20260015");
        st015Out.setRestraintType(RestraintType.VALUE_15);
        st015Out.setRestraintsStatus(RestraintsStatus.A);
        st015Out.setRestraintLevel(RestraintLevel.NATURE);

        final ST002InputBO[] st002In = new ST002InputBO[1];
        Mockito.lenient().when(ist002.execute(Mockito.any(ST002InputBO.class)))
                .thenAnswer(invocation -> {
                    st002In[0] = invocation.getArgument(0);
                    return st002Out;
                });
        final ST003InputBO[] st003In = new ST003InputBO[1];
        Mockito.lenient().when(ist003.execute(Mockito.any(ST003InputBO.class)))
                .thenAnswer(invocation -> {
                    st003In[0] = invocation.getArgument(0);
                    return st003Out;
                });
        final ST004InputBO[] st004In = new ST004InputBO[1];
        Mockito.lenient().when(ist004.execute(Mockito.any(ST004InputBO.class)))
                .thenAnswer(invocation -> {
                    st004In[0] = invocation.getArgument(0);
                    return st004Out;
                });
        final ST005InputBO[] st005In = new ST005InputBO[1];
        Mockito.lenient().when(ist005.execute(Mockito.any(ST005InputBO.class)))
                .thenAnswer(invocation -> {
                    st005In[0] = invocation.getArgument(0);
                    return st005Out;
                });
        final ST006InputBO[] st006In = new ST006InputBO[1];
        Mockito.lenient().when(ist006.execute(Mockito.any(ST006InputBO.class)))
                .thenAnswer(invocation -> {
                    st006In[0] = invocation.getArgument(0);
                    return st006Out;
                });
        final ST007InputBO[] st007In = new ST007InputBO[1];
        Mockito.lenient().when(ist007.execute(Mockito.any(ST007InputBO.class)))
                .thenAnswer(invocation -> {
                    st007In[0] = invocation.getArgument(0);
                    return st007Out;
                });
        final ST008InputBO[] st008In = new ST008InputBO[1];
        Mockito.lenient().when(ist008.execute(Mockito.any(ST008InputBO.class)))
                .thenAnswer(invocation -> {
                    st008In[0] = invocation.getArgument(0);
                    return st008Out;
                });
        final ST009InputBO[] st009In = new ST009InputBO[1];
        Mockito.lenient().when(ist009.execute(Mockito.any(ST009InputBO.class)))
                .thenAnswer(invocation -> {
                    st009In[0] = invocation.getArgument(0);
                    return st009Out;
                });
        final ST010InputBO[] st010In = new ST010InputBO[1];
        Mockito.lenient().when(ist010.execute(Mockito.any(ST010InputBO.class)))
                .thenAnswer(invocation -> {
                    st010In[0] = invocation.getArgument(0);
                    return st010Out;
                });
        final ST011InputBO[] st011In = new ST011InputBO[1];
        Mockito.lenient().when(ist011.execute(Mockito.any(ST011InputBO.class)))
                .thenAnswer(invocation -> {
                    st011In[0] = invocation.getArgument(0);
                    return st011Out;
                });
        final ST012InputBO[] st012In = new ST012InputBO[1];
        Mockito.lenient().when(ist012.execute(Mockito.any(ST012InputBO.class)))
                .thenAnswer(invocation -> {
                    st012In[0] = invocation.getArgument(0);
                    return st012Out;
                });
        final ST013InputBO[] st013In = new ST013InputBO[1];
        Mockito.lenient().when(ist013.execute(Mockito.any(ST013InputBO.class)))
                .thenAnswer(invocation -> {
                    st013In[0] = invocation.getArgument(0);
                    return st013Out;
                });
        final ST014InputBO[] st014In = new ST014InputBO[1];
        Mockito.lenient().when(ist014.execute(Mockito.any(ST014InputBO.class)))
                .thenAnswer(invocation -> {
                    st014In[0] = invocation.getArgument(0);
                    return st014Out;
                });
        final ST015InputBO[] st015In = new ST015InputBO[1];
        Mockito.lenient().when(ist015.execute(Mockito.any(ST015InputBO.class)))
                .thenAnswer(invocation -> {
                    st015In[0] = invocation.getArgument(0);
                    return st015Out;
                });

        RespHeader header = new RespHeader();
        header.setErrorCode("ER0001");
        header.setErrorMessage("旧错误");
        T2S1InputDTO input = new T2S1InputDTO();
        input.setBaseAcctNo("6100100012345678");
        input.setTranType("1000");
        input.setSourceType("MT");
        input.setRestraintType("13");
        input.setNarrativeCode("N001");
        input.setProdType("11002");

        T2S1OutputDTO output = t2s1.execute(header, input);

        Assertions.assertTrue(header.isSucceed());
        Assertions.assertNull(header.getErrorCode());
        Assertions.assertNull(header.getErrorMessage());
        Assertions.assertEquals(SourceType.MT, st002In[0].getSourceType());
        Assertions.assertEquals(RestraintType.VALUE_13, st002In[0].getRestraintType());
        Assertions.assertEquals(OthTranType.VALUE_1000, st002In[0].getTranType());
        Assertions.assertEquals("N001", st002In[0].getNarrativeCode());
        Assertions.assertEquals("11002", st002In[0].getProdType());
        Assertions.assertEquals(OthTranType.VALUE_1000, st011In[0].getTranType());
        Assertions.assertEquals(RestraintType.VALUE_13, st011In[0].getRestraintType());
        Assertions.assertEquals("6100100012345678", st003In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st004In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st005In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st006In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st007In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st008In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st009In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st010In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st012In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st014In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100012345678", st015In[0].getBaseAcctNo());
        Assertions.assertEquals("是", output.getNatureRestraintFlag());
        Assertions.assertEquals("是", output.getStopFlag());
        Assertions.assertEquals("是", output.getNoDebitNoCreditFlag());
        Assertions.assertEquals("是", output.getCashNonRcvPayFlag());
        Assertions.assertEquals("是", output.getTransferNoRecvNoPayFlag());
        Assertions.assertEquals("Y", output.getChannelCounterFlag());
        Assertions.assertEquals("6100100012345678", output.getBaseAcctNo());
        Assertions.assertEquals("Y", output.getLeadAcctFlag());
        Assertions.assertEquals("RES20260015", output.getResSeqNo());
        Assertions.assertEquals("15", output.getRestraintType());
        Assertions.assertEquals("A", output.getRestraintsStatus());
        Assertions.assertEquals("NATURE", output.getRestraintLevel());
        Assertions.assertEquals("A", output.getDrCrCtlFlag());
        Assertions.assertEquals("A", output.getStatus());
        Assertions.assertEquals("N", output.getTransferFlag());
        Assertions.assertEquals("N", output.getRestraintTypeStopFlag());
        Assertions.assertEquals("8", output.getRestraintTypeDef());
        Assertions.assertEquals("N", output.getCashFlag());
        Assertions.assertEquals("Y", output.getPledgedFlag());
        Assertions.assertEquals("A", output.getDetailStatus());
        Assertions.assertEquals("11002", output.getProdNo());
        Assertions.assertEquals("1000|1003", output.getTranTypeLink());
        Assertions.assertEquals("MT|MC", output.getChannelMuster());
        Assertions.assertEquals("N001", output.getNarrativeCode());
        Assertions.assertEquals("A", output.getResBranchRange());
        Assertions.assertEquals("Y", output.getDetailCounterFlag());
        Assertions.assertNull(output.getClientResSeqNo());
        Assertions.assertNull(output.getClientRestraintType());
        Assertions.assertNull(output.getClientRestraintsStatus());
        Assertions.assertNull(output.getTranDefResPriority());
        Assertions.assertNull(output.getRestraintTypeResPriority());
    }

    // 场景：T2S1-TC002 账户无任何生效限制记录，各检查步骤按空集合语义返回“否”/空字段并成功；预期 succeed=true，五个判定标志为“否”，查询类输出映射为 null，baseAcctNo/leadAcctFlag/channelCounterFlag 仍有值，无来源 5 项为 null
    @Test
    public void testT2S1T02() {
        ST002OutputBO st002Out = new ST002OutputBO();
        st002Out.setSucceed(true);
        st002Out.setChannelCounterFlag("Y");
        st002Out.setCheckResult("不豁免");
        ST005OutputBO st005Out = new ST005OutputBO();
        st005Out.setSucceed(true);
        st005Out.setBaseAcctNo("6100100076543210");
        st005Out.setLeadAcctFlag("Y");

        Mockito.lenient().when(ist002.execute(Mockito.any(ST002InputBO.class))).thenReturn(st002Out);
        Mockito.lenient().when(ist003.execute(Mockito.any(ST003InputBO.class))).thenReturn(newSt003NoHit());
        Mockito.lenient().when(ist004.execute(Mockito.any(ST004InputBO.class))).thenReturn(newSt004NoHit());
        Mockito.lenient().when(ist005.execute(Mockito.any(ST005InputBO.class))).thenReturn(st005Out);
        Mockito.lenient().when(ist006.execute(Mockito.any(ST006InputBO.class))).thenReturn(newSt006NoHit());
        Mockito.lenient().when(ist007.execute(Mockito.any(ST007InputBO.class))).thenReturn(newSt007NoHit());
        Mockito.lenient().when(ist008.execute(Mockito.any(ST008InputBO.class))).thenReturn(newSt008NoHit());
        Mockito.lenient().when(ist009.execute(Mockito.any(ST009InputBO.class))).thenReturn(newSt009NoHit());
        Mockito.lenient().when(ist010.execute(Mockito.any(ST010InputBO.class))).thenReturn(newSt010NoHit());
        Mockito.lenient().when(ist011.execute(Mockito.any(ST011InputBO.class))).thenReturn(newSt011ContinueCheck());
        Mockito.lenient().when(ist012.execute(Mockito.any(ST012InputBO.class))).thenReturn(newSt012NoHit());
        Mockito.lenient().when(ist013.execute(Mockito.any(ST013InputBO.class))).thenReturn(newSt013AllowClose());
        Mockito.lenient().when(ist014.execute(Mockito.any(ST014InputBO.class))).thenReturn(newSt014NoHit());
        Mockito.lenient().when(ist015.execute(Mockito.any(ST015InputBO.class))).thenReturn(newSt015NoHit());

        RespHeader header = new RespHeader();
        T2S1InputDTO input = new T2S1InputDTO();
        input.setBaseAcctNo("6100100076543210");
        input.setTranType("1000");
        input.setSourceType("MT");
        input.setRestraintType("13");
        input.setNarrativeCode("N001");
        input.setProdType("11002");

        T2S1OutputDTO output = t2s1.execute(header, input);

        Assertions.assertTrue(header.isSucceed());
        Assertions.assertNull(header.getErrorCode());
        Assertions.assertNull(header.getErrorMessage());
        Assertions.assertEquals("否", output.getNatureRestraintFlag());
        Assertions.assertEquals("否", output.getStopFlag());
        Assertions.assertEquals("否", output.getNoDebitNoCreditFlag());
        Assertions.assertEquals("否", output.getCashNonRcvPayFlag());
        Assertions.assertEquals("否", output.getTransferNoRecvNoPayFlag());
        Assertions.assertEquals("Y", output.getChannelCounterFlag());
        Assertions.assertEquals("6100100076543210", output.getBaseAcctNo());
        Assertions.assertEquals("Y", output.getLeadAcctFlag());
        Assertions.assertNull(output.getResSeqNo());
        Assertions.assertNull(output.getRestraintType());
        Assertions.assertNull(output.getRestraintsStatus());
        Assertions.assertNull(output.getRestraintLevel());
        Assertions.assertNull(output.getDrCrCtlFlag());
        Assertions.assertNull(output.getStatus());
        Assertions.assertNull(output.getTransferFlag());
        Assertions.assertNull(output.getRestraintTypeStopFlag());
        Assertions.assertNull(output.getRestraintTypeDef());
        Assertions.assertNull(output.getCashFlag());
        Assertions.assertNull(output.getPledgedFlag());
        Assertions.assertNull(output.getDetailStatus());
        Assertions.assertNull(output.getProdNo());
        Assertions.assertNull(output.getTranTypeLink());
        Assertions.assertNull(output.getChannelMuster());
        Assertions.assertNull(output.getNarrativeCode());
        Assertions.assertNull(output.getResBranchRange());
        Assertions.assertNull(output.getDetailCounterFlag());
        Assertions.assertNull(output.getClientResSeqNo());
        Assertions.assertNull(output.getClientRestraintType());
        Assertions.assertNull(output.getClientRestraintsStatus());
        Assertions.assertNull(output.getTranDefResPriority());
        Assertions.assertNull(output.getRestraintTypeResPriority());
    }

    // 场景：T2S1-TC003 场景输入取另一组真实枚举业务值（手机银行、现金支取、可疑账户处置-全额止付），ST002 走非柜面分支；预期 String→枚举转换与入参直传对取值不敏感，ST002 非柜面输出映射正确，五个判定标志为“否”，无来源 5 项为 null
    @Test
    public void testT2S1T03() {
        ST002OutputBO st002Out = new ST002OutputBO();
        st002Out.setSucceed(true);
        st002Out.setChannelCounterFlag("N");
        st002Out.setStatus(Status.A);
        st002Out.setProdNo("11002");
        st002Out.setTranTypeLink("1003");
        st002Out.setChannelMuster("M");
        st002Out.setNarrativeCode("N002");
        st002Out.setResBranchRange(ResBranchRange.C);
        st002Out.setDetailCounterFlag("N");
        st002Out.setCheckResult("不豁免");
        ST005OutputBO st005Out = new ST005OutputBO();
        st005Out.setSucceed(true);
        st005Out.setBaseAcctNo("6100100099887766");
        st005Out.setLeadAcctFlag("Y");

        final ST002InputBO[] st002In = new ST002InputBO[1];
        Mockito.lenient().when(ist002.execute(Mockito.any(ST002InputBO.class)))
                .thenAnswer(invocation -> {
                    st002In[0] = invocation.getArgument(0);
                    return st002Out;
                });
        final ST003InputBO[] st003In = new ST003InputBO[1];
        Mockito.lenient().when(ist003.execute(Mockito.any(ST003InputBO.class)))
                .thenAnswer(invocation -> {
                    st003In[0] = invocation.getArgument(0);
                    return newSt003NoHit();
                });
        final ST004InputBO[] st004In = new ST004InputBO[1];
        Mockito.lenient().when(ist004.execute(Mockito.any(ST004InputBO.class)))
                .thenAnswer(invocation -> {
                    st004In[0] = invocation.getArgument(0);
                    return newSt004NoHit();
                });
        final ST005InputBO[] st005In = new ST005InputBO[1];
        Mockito.lenient().when(ist005.execute(Mockito.any(ST005InputBO.class)))
                .thenAnswer(invocation -> {
                    st005In[0] = invocation.getArgument(0);
                    return st005Out;
                });
        final ST006InputBO[] st006In = new ST006InputBO[1];
        Mockito.lenient().when(ist006.execute(Mockito.any(ST006InputBO.class)))
                .thenAnswer(invocation -> {
                    st006In[0] = invocation.getArgument(0);
                    return newSt006NoHit();
                });
        final ST007InputBO[] st007In = new ST007InputBO[1];
        Mockito.lenient().when(ist007.execute(Mockito.any(ST007InputBO.class)))
                .thenAnswer(invocation -> {
                    st007In[0] = invocation.getArgument(0);
                    return newSt007NoHit();
                });
        final ST008InputBO[] st008In = new ST008InputBO[1];
        Mockito.lenient().when(ist008.execute(Mockito.any(ST008InputBO.class)))
                .thenAnswer(invocation -> {
                    st008In[0] = invocation.getArgument(0);
                    return newSt008NoHit();
                });
        final ST009InputBO[] st009In = new ST009InputBO[1];
        Mockito.lenient().when(ist009.execute(Mockito.any(ST009InputBO.class)))
                .thenAnswer(invocation -> {
                    st009In[0] = invocation.getArgument(0);
                    return newSt009NoHit();
                });
        final ST010InputBO[] st010In = new ST010InputBO[1];
        Mockito.lenient().when(ist010.execute(Mockito.any(ST010InputBO.class)))
                .thenAnswer(invocation -> {
                    st010In[0] = invocation.getArgument(0);
                    return newSt010NoHit();
                });
        final ST011InputBO[] st011In = new ST011InputBO[1];
        Mockito.lenient().when(ist011.execute(Mockito.any(ST011InputBO.class)))
                .thenAnswer(invocation -> {
                    st011In[0] = invocation.getArgument(0);
                    return newSt011ContinueCheck();
                });
        final ST012InputBO[] st012In = new ST012InputBO[1];
        Mockito.lenient().when(ist012.execute(Mockito.any(ST012InputBO.class)))
                .thenAnswer(invocation -> {
                    st012In[0] = invocation.getArgument(0);
                    return newSt012NoHit();
                });
        Mockito.lenient().when(ist013.execute(Mockito.any(ST013InputBO.class))).thenReturn(newSt013AllowClose());
        final ST014InputBO[] st014In = new ST014InputBO[1];
        Mockito.lenient().when(ist014.execute(Mockito.any(ST014InputBO.class)))
                .thenAnswer(invocation -> {
                    st014In[0] = invocation.getArgument(0);
                    return newSt014NoHit();
                });
        final ST015InputBO[] st015In = new ST015InputBO[1];
        Mockito.lenient().when(ist015.execute(Mockito.any(ST015InputBO.class)))
                .thenAnswer(invocation -> {
                    st015In[0] = invocation.getArgument(0);
                    return newSt015NoHit();
                });

        RespHeader header = new RespHeader();
        T2S1InputDTO input = new T2S1InputDTO();
        input.setBaseAcctNo("6100100099887766");
        input.setTranType("1003");
        input.setSourceType("M");
        input.setRestraintType("19");
        input.setNarrativeCode("N002");
        input.setProdType("11002");

        T2S1OutputDTO output = t2s1.execute(header, input);

        Assertions.assertEquals(SourceType.M, st002In[0].getSourceType());
        Assertions.assertEquals(RestraintType.VALUE_19, st002In[0].getRestraintType());
        Assertions.assertEquals(OthTranType.VALUE_1003, st002In[0].getTranType());
        Assertions.assertEquals("N002", st002In[0].getNarrativeCode());
        Assertions.assertEquals("11002", st002In[0].getProdType());
        Assertions.assertEquals(OthTranType.VALUE_1003, st011In[0].getTranType());
        Assertions.assertEquals(RestraintType.VALUE_19, st011In[0].getRestraintType());
        Assertions.assertEquals("6100100099887766", st003In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st004In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st005In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st006In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st007In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st008In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st009In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st010In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st012In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st014In[0].getBaseAcctNo());
        Assertions.assertEquals("6100100099887766", st015In[0].getBaseAcctNo());
        Assertions.assertTrue(header.isSucceed());
        Assertions.assertNull(header.getErrorCode());
        Assertions.assertNull(header.getErrorMessage());
        Assertions.assertEquals("N", output.getChannelCounterFlag());
        Assertions.assertEquals("6100100099887766", output.getBaseAcctNo());
        Assertions.assertEquals("Y", output.getLeadAcctFlag());
        Assertions.assertEquals("A", output.getDetailStatus());
        Assertions.assertEquals("11002", output.getProdNo());
        Assertions.assertEquals("1003", output.getTranTypeLink());
        Assertions.assertEquals("M", output.getChannelMuster());
        Assertions.assertEquals("N002", output.getNarrativeCode());
        Assertions.assertEquals("C", output.getResBranchRange());
        Assertions.assertEquals("N", output.getDetailCounterFlag());
        Assertions.assertEquals("否", output.getNatureRestraintFlag());
        Assertions.assertEquals("否", output.getStopFlag());
        Assertions.assertEquals("否", output.getNoDebitNoCreditFlag());
        Assertions.assertEquals("否", output.getCashNonRcvPayFlag());
        Assertions.assertEquals("否", output.getTransferNoRecvNoPayFlag());
        Assertions.assertNull(output.getResSeqNo());
        Assertions.assertNull(output.getRestraintType());
        Assertions.assertNull(output.getRestraintsStatus());
        Assertions.assertNull(output.getRestraintLevel());
        Assertions.assertNull(output.getDrCrCtlFlag());
        Assertions.assertNull(output.getStatus());
        Assertions.assertNull(output.getTransferFlag());
        Assertions.assertNull(output.getRestraintTypeStopFlag());
        Assertions.assertNull(output.getRestraintTypeDef());
        Assertions.assertNull(output.getCashFlag());
        Assertions.assertNull(output.getPledgedFlag());
        Assertions.assertNull(output.getClientResSeqNo());
        Assertions.assertNull(output.getClientRestraintType());
        Assertions.assertNull(output.getClientRestraintsStatus());
        Assertions.assertNull(output.getTranDefResPriority());
        Assertions.assertNull(output.getRestraintTypeResPriority());
    }

    /** ST003 无命中输出：ahBuFlag 为空 */
    private ST003OutputBO newSt003NoHit() {
        ST003OutputBO bo = new ST003OutputBO();
        bo.setSucceed(true);
        return bo;
    }

    /** ST004 无命中输出：转账止收标志“否”，查询字段为空 */
    private ST004OutputBO newSt004NoHit() {
        ST004OutputBO bo = new ST004OutputBO();
        bo.setSucceed(true);
        bo.setTransferStopFlag("否");
        return bo;
    }

    /** ST006 无命中输出：转账止付标志“否”，查询字段为空 */
    private ST006OutputBO newSt006NoHit() {
        ST006OutputBO bo = new ST006OutputBO();
        bo.setSucceed(true);
        bo.setStopFlag("否");
        return bo;
    }

    /** ST007 无命中输出：全部字段为空 */
    private ST007OutputBO newSt007NoHit() {
        ST007OutputBO bo = new ST007OutputBO();
        bo.setSucceed(true);
        return bo;
    }

    /** ST008 无命中输出：现金止收标志“否”，查询字段为空 */
    private ST008OutputBO newSt008NoHit() {
        ST008OutputBO bo = new ST008OutputBO();
        bo.setSucceed(true);
        bo.setCashStopFlag("否");
        return bo;
    }

    /** ST009 无命中输出：止付标志“否”，查询字段为空 */
    private ST009OutputBO newSt009NoHit() {
        ST009OutputBO bo = new ST009OutputBO();
        bo.setSucceed(true);
        bo.setStopFlag("否");
        return bo;
    }

    /** ST010 无命中输出：转账不收不付标志“否”，查询字段为空 */
    private ST010OutputBO newSt010NoHit() {
        ST010OutputBO bo = new ST010OutputBO();
        bo.setSucceed(true);
        bo.setTransferNoRecvNoPayFlag("否");
        return bo;
    }

    /** ST011 输出：检查结果“继续检查” */
    private ST011OutputBO newSt011ContinueCheck() {
        ST011OutputBO bo = new ST011OutputBO();
        bo.setSucceed(true);
        bo.setCheckResult("继续检查");
        return bo;
    }

    /** ST012 无命中输出：不收不付标志“否”，查询字段为空 */
    private ST012OutputBO newSt012NoHit() {
        ST012OutputBO bo = new ST012OutputBO();
        bo.setSucceed(true);
        bo.setNoDebitNoCreditFlag("否");
        return bo;
    }

    /** ST013 输出：允许销户标志“允许销户”（空集合遍历结束的步骤语义） */
    private ST013OutputBO newSt013AllowClose() {
        ST013OutputBO bo = new ST013OutputBO();
        bo.setSucceed(true);
        bo.setAllowCloseAcctFlag("允许销户");
        return bo;
    }

    /** ST014 无命中输出：现金不收不付标志“否”，查询字段为空 */
    private ST014OutputBO newSt014NoHit() {
        ST014OutputBO bo = new ST014OutputBO();
        bo.setSucceed(true);
        bo.setCashNonRcvPayFlag("否");
        return bo;
    }

    /** ST015 无命中输出：属性限制标志“否”，查询字段为空 */
    private ST015OutputBO newSt015NoHit() {
        ST015OutputBO bo = new ST015OutputBO();
        bo.setSucceed(true);
        bo.setNatureRestraintFlag("否");
        return bo;
    }
}

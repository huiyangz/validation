package com.dcits.validation.task.dto;

/**
 * T2S1 检查账户限制 输出DTO
 *
 * <p>输出表存在同名多行（来源实体不同），物理字段按来源实体加限定词区分：
 * 客户限制表（RB_CLIENT_RESTRAINTS）行前缀 client、交易类型定义表（RB_TRAN_DEF）行前缀 tranDef、
 * 存款限制类型表（RB_RESTRAINT_TYPE）行前缀 restraintType、存款限制检查控制详情
 * （RB_RESTRAINT_CONTROL_DETAILS）行前缀 detail（与 SPEC 已区分的 channelCounterFlag/detailCounterFlag
 * 命名方式一致）；对公存款账户限制表（RB_BUS_RESTRAINTS）行及场景级判定行保留原字段名。
 * 各字段注释标注来源实体与赋值步骤，未标注步骤的为无来源字段（已接受结论：ST002–ST015
 * 正式输出与本场景输入均不产生，预期未赋值）。</p>
 */
public class T2S1OutputDTO {
    /** 属性限制标志（来源：ST015 检查是否存在属性限制） */
    private String natureRestraintFlag;
    /** 止付标志（场景级判定，来源：ST009 检查是否存在止付限制） */
    private String stopFlag;
    /** 不收不付标志（来源：ST012 检查是否存在不收不付限制） */
    private String noDebitNoCreditFlag;
    /** 现金不收不付限制标志（来源：ST014 检查是否存在现金不收不付限制） */
    private String cashNonRcvPayFlag;
    /** 转账不收不付标志（来源：ST010 检查是否存在转账不收不付限制） */
    private String transferNoRecvNoPayFlag;
    /** 柜面标志（来源：渠道类型表 FM_CHANNEL，ST002 检查限制豁免） */
    private String channelCounterFlag;
    /** 账号（来源：对公存款账户主表 RB_BUS_ACCT，ST005 检查账户是否存在限制） */
    private String baseAcctNo;
    /** 主账户标志（来源：对公存款账户主表 RB_BUS_ACCT，ST005 检查账户是否存在限制） */
    private String leadAcctFlag;
    /** 限制编号（来源：客户限制表 RB_CLIENT_RESTRAINTS，无步骤来源，预期未赋值） */
    private String clientResSeqNo;
    /** 账户限制类型（来源：客户限制表 RB_CLIENT_RESTRAINTS，无步骤来源，预期未赋值） */
    private String clientRestraintType;
    /** 限制状态（来源：客户限制表 RB_CLIENT_RESTRAINTS，无步骤来源，预期未赋值） */
    private String clientRestraintsStatus;
    /** 冻结级别（来源：交易类型定义表 RB_TRAN_DEF，无步骤来源，预期未赋值） */
    private String tranDefResPriority;
    /** 限制编号（来源：对公存款账户限制表 RB_BUS_RESTRAINTS，ST015 检查是否存在属性限制） */
    private String resSeqNo;
    /** 账户限制类型（来源：对公存款账户限制表 RB_BUS_RESTRAINTS，ST015 检查是否存在属性限制） */
    private String restraintType;
    /** 限制状态（来源：对公存款账户限制表 RB_BUS_RESTRAINTS，ST015 检查是否存在属性限制） */
    private String restraintsStatus;
    /** 限制级别（来源：对公存款账户限制表 RB_BUS_RESTRAINTS，ST015 检查是否存在属性限制） */
    private String restraintLevel;
    /** 冻结级别（来源：存款限制类型表 RB_RESTRAINT_TYPE，无步骤来源，预期未赋值） */
    private String restraintTypeResPriority;
    /** 借方贷方控制标志（来源：存款限制类型表 RB_RESTRAINT_TYPE，ST014 检查是否存在现金不收不付限制） */
    private String drCrCtlFlag;
    /** 状态（来源：存款限制类型表 RB_RESTRAINT_TYPE，ST014 检查是否存在现金不收不付限制） */
    private String status;
    /** 转账标志（来源：存款限制类型表 RB_RESTRAINT_TYPE，ST010 检查是否存在转账不收不付限制） */
    private String transferFlag;
    /** 止付标志（来源：存款限制类型表 RB_RESTRAINT_TYPE，ST010 检查是否存在转账不收不付限制） */
    private String restraintTypeStopFlag;
    /** 账户限制类型（限制类型表侧，来源：存款限制类型表 RB_RESTRAINT_TYPE，ST008 检查是否存在现金止收限制） */
    private String restraintTypeDef;
    /** 现金标志（来源：存款限制类型表 RB_RESTRAINT_TYPE，ST014 检查是否存在现金不收不付限制） */
    private String cashFlag;
    /** 质押标志（来源：存款限制类型表 RB_RESTRAINT_TYPE，ST007 检查质押类限制） */
    private String pledgedFlag;
    /** 状态（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS，ST002 检查限制豁免） */
    private String detailStatus;
    /** 产品编号（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS，ST002 检查限制豁免） */
    private String prodNo;
    /** 多交易类型（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS，ST002 检查限制豁免） */
    private String tranTypeLink;
    /** 渠道集合（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS，ST002 检查限制豁免） */
    private String channelMuster;
    /** 摘要码（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS，ST002 检查限制豁免） */
    private String narrativeCode;
    /** 限制机构范围（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS，ST002 检查限制豁免） */
    private String resBranchRange;
    /** 柜面标志（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS，ST002 检查限制豁免） */
    private String detailCounterFlag;

    public String getNatureRestraintFlag() {
        return natureRestraintFlag;
    }

    public void setNatureRestraintFlag(String natureRestraintFlag) {
        this.natureRestraintFlag = natureRestraintFlag;
    }

    public String getStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(String stopFlag) {
        this.stopFlag = stopFlag;
    }

    public String getNoDebitNoCreditFlag() {
        return noDebitNoCreditFlag;
    }

    public void setNoDebitNoCreditFlag(String noDebitNoCreditFlag) {
        this.noDebitNoCreditFlag = noDebitNoCreditFlag;
    }

    public String getCashNonRcvPayFlag() {
        return cashNonRcvPayFlag;
    }

    public void setCashNonRcvPayFlag(String cashNonRcvPayFlag) {
        this.cashNonRcvPayFlag = cashNonRcvPayFlag;
    }

    public String getTransferNoRecvNoPayFlag() {
        return transferNoRecvNoPayFlag;
    }

    public void setTransferNoRecvNoPayFlag(String transferNoRecvNoPayFlag) {
        this.transferNoRecvNoPayFlag = transferNoRecvNoPayFlag;
    }

    public String getChannelCounterFlag() {
        return channelCounterFlag;
    }

    public void setChannelCounterFlag(String channelCounterFlag) {
        this.channelCounterFlag = channelCounterFlag;
    }

    public String getBaseAcctNo() {
        return baseAcctNo;
    }

    public void setBaseAcctNo(String baseAcctNo) {
        this.baseAcctNo = baseAcctNo;
    }

    public String getLeadAcctFlag() {
        return leadAcctFlag;
    }

    public void setLeadAcctFlag(String leadAcctFlag) {
        this.leadAcctFlag = leadAcctFlag;
    }

    public String getClientResSeqNo() {
        return clientResSeqNo;
    }

    public void setClientResSeqNo(String clientResSeqNo) {
        this.clientResSeqNo = clientResSeqNo;
    }

    public String getClientRestraintType() {
        return clientRestraintType;
    }

    public void setClientRestraintType(String clientRestraintType) {
        this.clientRestraintType = clientRestraintType;
    }

    public String getClientRestraintsStatus() {
        return clientRestraintsStatus;
    }

    public void setClientRestraintsStatus(String clientRestraintsStatus) {
        this.clientRestraintsStatus = clientRestraintsStatus;
    }

    public String getTranDefResPriority() {
        return tranDefResPriority;
    }

    public void setTranDefResPriority(String tranDefResPriority) {
        this.tranDefResPriority = tranDefResPriority;
    }

    public String getResSeqNo() {
        return resSeqNo;
    }

    public void setResSeqNo(String resSeqNo) {
        this.resSeqNo = resSeqNo;
    }

    public String getRestraintType() {
        return restraintType;
    }

    public void setRestraintType(String restraintType) {
        this.restraintType = restraintType;
    }

    public String getRestraintsStatus() {
        return restraintsStatus;
    }

    public void setRestraintsStatus(String restraintsStatus) {
        this.restraintsStatus = restraintsStatus;
    }

    public String getRestraintLevel() {
        return restraintLevel;
    }

    public void setRestraintLevel(String restraintLevel) {
        this.restraintLevel = restraintLevel;
    }

    public String getRestraintTypeResPriority() {
        return restraintTypeResPriority;
    }

    public void setRestraintTypeResPriority(String restraintTypeResPriority) {
        this.restraintTypeResPriority = restraintTypeResPriority;
    }

    public String getDrCrCtlFlag() {
        return drCrCtlFlag;
    }

    public void setDrCrCtlFlag(String drCrCtlFlag) {
        this.drCrCtlFlag = drCrCtlFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(String transferFlag) {
        this.transferFlag = transferFlag;
    }

    public String getRestraintTypeStopFlag() {
        return restraintTypeStopFlag;
    }

    public void setRestraintTypeStopFlag(String restraintTypeStopFlag) {
        this.restraintTypeStopFlag = restraintTypeStopFlag;
    }

    public String getRestraintTypeDef() {
        return restraintTypeDef;
    }

    public void setRestraintTypeDef(String restraintTypeDef) {
        this.restraintTypeDef = restraintTypeDef;
    }

    public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }

    public String getPledgedFlag() {
        return pledgedFlag;
    }

    public void setPledgedFlag(String pledgedFlag) {
        this.pledgedFlag = pledgedFlag;
    }

    public String getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(String detailStatus) {
        this.detailStatus = detailStatus;
    }

    public String getProdNo() {
        return prodNo;
    }

    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }

    public String getTranTypeLink() {
        return tranTypeLink;
    }

    public void setTranTypeLink(String tranTypeLink) {
        this.tranTypeLink = tranTypeLink;
    }

    public String getChannelMuster() {
        return channelMuster;
    }

    public void setChannelMuster(String channelMuster) {
        this.channelMuster = channelMuster;
    }

    public String getNarrativeCode() {
        return narrativeCode;
    }

    public void setNarrativeCode(String narrativeCode) {
        this.narrativeCode = narrativeCode;
    }

    public String getResBranchRange() {
        return resBranchRange;
    }

    public void setResBranchRange(String resBranchRange) {
        this.resBranchRange = resBranchRange;
    }

    public String getDetailCounterFlag() {
        return detailCounterFlag;
    }

    public void setDetailCounterFlag(String detailCounterFlag) {
        this.detailCounterFlag = detailCounterFlag;
    }
}

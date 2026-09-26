package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.ResBranchRange;
import com.dcits.validation.enums.Status;

/** ST002 检查限制豁免 输出BO */
public class ST002OutputBO extends StepResult {
    /** 渠道柜面标志（来源：渠道类型表 FM_CHANNEL） */
    private String channelCounterFlag;
    /** 状态（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS） */
    private Status status;
    /** 产品编号（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS） */
    private String prodNo;
    /** 多交易类型（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS） */
    private String tranTypeLink;
    /** 渠道集合（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS） */
    private String channelMuster;
    /** 摘要码（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS） */
    private String narrativeCode;
    /** 限制机构范围（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS） */
    private ResBranchRange resBranchRange;
    /** 明细柜面标志（来源：存款限制检查控制详情 RB_RESTRAINT_CONTROL_DETAILS） */
    private String detailCounterFlag;
    /** 检查结果（不检查限制/需检查限制/豁免/不豁免） */
    private String checkResult;

    public String getChannelCounterFlag() {
        return channelCounterFlag;
    }

    public void setChannelCounterFlag(String channelCounterFlag) {
        this.channelCounterFlag = channelCounterFlag;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public ResBranchRange getResBranchRange() {
        return resBranchRange;
    }

    public void setResBranchRange(ResBranchRange resBranchRange) {
        this.resBranchRange = resBranchRange;
    }

    public String getDetailCounterFlag() {
        return detailCounterFlag;
    }

    public void setDetailCounterFlag(String detailCounterFlag) {
        this.detailCounterFlag = detailCounterFlag;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }
}

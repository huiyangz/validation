package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;

/**
 * ST007 检查质押类限制 步骤输出BO
 */
public class ST007OutputBO extends StepResult {
    /** 限制编号 */
    private String resSeqNo;
    /** 账户限制类型 */
    private RestraintType restraintType;
    /** 限制状态 */
    private RestraintsStatus restraintsStatus;
    /** 质押标志 */
    private String pledgedFlag;
    /** 状态 */
    private Status status;

    public String getResSeqNo() {
        return resSeqNo;
    }

    public void setResSeqNo(String resSeqNo) {
        this.resSeqNo = resSeqNo;
    }

    public RestraintType getRestraintType() {
        return restraintType;
    }

    public void setRestraintType(RestraintType restraintType) {
        this.restraintType = restraintType;
    }

    public RestraintsStatus getRestraintsStatus() {
        return restraintsStatus;
    }

    public void setRestraintsStatus(RestraintsStatus restraintsStatus) {
        this.restraintsStatus = restraintsStatus;
    }

    public String getPledgedFlag() {
        return pledgedFlag;
    }

    public void setPledgedFlag(String pledgedFlag) {
        this.pledgedFlag = pledgedFlag;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

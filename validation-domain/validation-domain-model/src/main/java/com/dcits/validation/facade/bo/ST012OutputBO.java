package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;

/**
 * ST012 检查是否存在不收不付限制 输出BO
 */
public class ST012OutputBO extends StepResult {
    /** 不收不付标志 */
    private String noDebitNoCreditFlag;
    /** 限制编号 */
    private String resSeqNo;
    /** 账户限制类型 */
    private RestraintType restraintType;
    /** 限制状态 */
    private RestraintsStatus restraintsStatus;
    /** 借方贷方控制标志 */
    private DrCrCtlFlag drCrCtlFlag;
    /** 状态 */
    private Status status;

    public String getNoDebitNoCreditFlag() {
        return noDebitNoCreditFlag;
    }

    public void setNoDebitNoCreditFlag(String noDebitNoCreditFlag) {
        this.noDebitNoCreditFlag = noDebitNoCreditFlag;
    }

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

    public DrCrCtlFlag getDrCrCtlFlag() {
        return drCrCtlFlag;
    }

    public void setDrCrCtlFlag(DrCrCtlFlag drCrCtlFlag) {
        this.drCrCtlFlag = drCrCtlFlag;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

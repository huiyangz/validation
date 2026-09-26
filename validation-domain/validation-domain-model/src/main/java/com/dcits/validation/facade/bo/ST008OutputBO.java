package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;

/** ST008 检查是否存在现金止收限制 输出BO */
public class ST008OutputBO extends StepResult {
    /** 限制编号 */
    private String resSeqNo;
    /** 账户限制类型 */
    private RestraintType restraintType;
    /** 限制状态 */
    private RestraintsStatus restraintsStatus;
    /** 账户限制类型（限制类型表侧） */
    private RestraintType restraintTypeDef;
    /** 状态 */
    private Status status;
    /** 借方贷方控制标志 */
    private DrCrCtlFlag drCrCtlFlag;
    /** 现金标志 */
    private String cashFlag;
    /** 现金止收标志（"是"/"否"） */
    private String cashStopFlag;

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

    public RestraintType getRestraintTypeDef() {
        return restraintTypeDef;
    }

    public void setRestraintTypeDef(RestraintType restraintTypeDef) {
        this.restraintTypeDef = restraintTypeDef;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DrCrCtlFlag getDrCrCtlFlag() {
        return drCrCtlFlag;
    }

    public void setDrCrCtlFlag(DrCrCtlFlag drCrCtlFlag) {
        this.drCrCtlFlag = drCrCtlFlag;
    }

    public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }

    public String getCashStopFlag() {
        return cashStopFlag;
    }

    public void setCashStopFlag(String cashStopFlag) {
        this.cashStopFlag = cashStopFlag;
    }
}

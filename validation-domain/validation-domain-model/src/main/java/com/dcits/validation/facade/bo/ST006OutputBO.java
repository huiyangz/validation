package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;

/**
 * ST006 检查是否存在转账止付限制 步骤输出BO。
 */
public class ST006OutputBO extends StepResult {
    /** 限制编号（来源：对公存款账户限制表 RB_BUS_RESTRAINTS） */
    private String resSeqNo;
    /** 账户限制类型（来源：对公存款账户限制表 RB_BUS_RESTRAINTS） */
    private RestraintType restraintType;
    /** 限制状态（来源：对公存款账户限制表 RB_BUS_RESTRAINTS） */
    private RestraintsStatus restraintsStatus;
    /** 借方贷方控制标志（来源：存款限制类型表 RB_RESTRAINT_TYPE） */
    private DrCrCtlFlag drCrCtlFlag;
    /** 状态（来源：存款限制类型表 RB_RESTRAINT_TYPE） */
    private Status status;
    /** 转账标志（来源：存款限制类型表 RB_RESTRAINT_TYPE） */
    private String transferFlag;
    /** 转账止付标志（子步骤3判定结果，取值：是/否） */
    private String stopFlag;

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

    public String getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(String transferFlag) {
        this.transferFlag = transferFlag;
    }

    public String getStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(String stopFlag) {
        this.stopFlag = stopFlag;
    }
}

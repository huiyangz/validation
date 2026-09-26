package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;

/** ST010 检查是否存在转账不收不付限制 输出BO */
public class ST010OutputBO extends StepResult {
    /** 转账不收不付标志 */
    private String transferNoRecvNoPayFlag;
    /** 限制编号（对公存款账户限制表 RB_BUS_RESTRAINTS） */
    private String resSeqNo;
    /** 账户限制类型（对公存款账户限制表 RB_BUS_RESTRAINTS） */
    private RestraintType restraintType;
    /** 限制状态（对公存款账户限制表 RB_BUS_RESTRAINTS） */
    private RestraintsStatus restraintsStatus;
    /** 借方贷方控制标志（存款限制类型表 RB_RESTRAINT_TYPE，仅状态A-生效的类型记录提供） */
    private DrCrCtlFlag drCrCtlFlag;
    /** 状态（存款限制类型表 RB_RESTRAINT_TYPE，仅状态A-生效的类型记录提供） */
    private Status status;
    /** 转账标志（存款限制类型表 RB_RESTRAINT_TYPE，仅状态A-生效的类型记录提供） */
    private String transferFlag;
    /** 止付标志（存款限制类型表 RB_RESTRAINT_TYPE，仅状态A-生效的类型记录提供） */
    private String stopFlag;

    public String getTransferNoRecvNoPayFlag() {
        return transferNoRecvNoPayFlag;
    }

    public void setTransferNoRecvNoPayFlag(String transferNoRecvNoPayFlag) {
        this.transferNoRecvNoPayFlag = transferNoRecvNoPayFlag;
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

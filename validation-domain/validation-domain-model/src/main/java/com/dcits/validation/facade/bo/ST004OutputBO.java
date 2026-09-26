package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;

/**
 * ST004 检查转账止收限制 输出BO
 */
public class ST004OutputBO extends StepResult {

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
	/** 转账标志 */
	private String transferFlag;
	/** 止付标志 */
	private String stopFlag;
	/** 转账止收标志，取值“是”或“否” */
	private String transferStopFlag;

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

	public String getTransferStopFlag() {
		return transferStopFlag;
	}

	public void setTransferStopFlag(String transferStopFlag) {
		this.transferStopFlag = transferStopFlag;
	}
}

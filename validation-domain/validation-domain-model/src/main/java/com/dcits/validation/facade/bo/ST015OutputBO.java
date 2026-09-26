package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.RestraintLevel;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.RestraintType;

/**
 * ST015 检查是否存在属性限制 步骤输出BO。
 */
public class ST015OutputBO extends StepResult {
	/** 属性限制标志（是/否） */
	private String natureRestraintFlag;
	/** 限制编号（来源：对公存款账户限制表 RB_BUS_RESTRAINTS） */
	private String resSeqNo;
	/** 账户限制类型（来源：对公存款账户限制表 RB_BUS_RESTRAINTS） */
	private RestraintType restraintType;
	/** 限制状态（来源：对公存款账户限制表 RB_BUS_RESTRAINTS） */
	private RestraintsStatus restraintsStatus;
	/** 限制级别（来源：对公存款账户限制表 RB_BUS_RESTRAINTS） */
	private RestraintLevel restraintLevel;

	public String getNatureRestraintFlag() {
		return natureRestraintFlag;
	}

	public void setNatureRestraintFlag(String natureRestraintFlag) {
		this.natureRestraintFlag = natureRestraintFlag;
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

	public RestraintLevel getRestraintLevel() {
		return restraintLevel;
	}

	public void setRestraintLevel(RestraintLevel restraintLevel) {
		this.restraintLevel = restraintLevel;
	}
}

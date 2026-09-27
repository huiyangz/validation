package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;

/** ST005 检查账户是否存在限制 输出BO */
public class ST005OutputBO extends StepResult {
    /** 账号（[待查账户]对应【账户信息】记录的账号） */
    private String baseAcctNo;
    /** 主账户标志（[待查账户]对应【账户信息】记录的主账户标志） */
    private String leadAcctFlag;
    /** 限制编号 */
    private String resSeqNo;
    /** 账户限制类型 */
    private RestraintType restraintType;
    /** 限制状态 */
    private RestraintsStatus restraintsStatus;

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
}

package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;

/** ST013 检查账户是否存在不允许销户的限制 - 步骤输出BO */
public class ST013OutputBO extends StepResult {
    /** 允许销户标志；取值：“允许销户”、“不允许销户” */
    private String allowCloseAcctFlag;

    public String getAllowCloseAcctFlag() {
        return allowCloseAcctFlag;
    }

    public void setAllowCloseAcctFlag(String allowCloseAcctFlag) {
        this.allowCloseAcctFlag = allowCloseAcctFlag;
    }
}

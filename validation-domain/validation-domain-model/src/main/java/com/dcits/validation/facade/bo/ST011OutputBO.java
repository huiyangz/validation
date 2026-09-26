package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;

/** ST011 检查限制优先级 步骤输出BO */
public class ST011OutputBO extends StepResult {
    /** 检查结果：“不检查限制”或“继续检查” */
    private String checkResult;

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }
}

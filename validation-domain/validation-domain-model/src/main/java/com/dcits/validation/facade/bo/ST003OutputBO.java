package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;

/** ST003 检查有权机关冻结限制 输出BO */
public class ST003OutputBO extends StepResult {
    /** 有权机关冻结标志 */
    private String ahBuFlag;

    public String getAhBuFlag() {
        return ahBuFlag;
    }

    public void setAhBuFlag(String ahBuFlag) {
        this.ahBuFlag = ahBuFlag;
    }
}

package com.dcits.validation.facade.bo;

import com.dcits.validation.enums.OthTranType;
import com.dcits.validation.enums.RestraintType;

/** ST011 检查限制优先级 步骤输入BO */
public class ST011InputBO {
    /** 交易类型 */
    private OthTranType tranType;
    /** 账户限制类型 */
    private RestraintType restraintType;

    public OthTranType getTranType() {
        return tranType;
    }

    public void setTranType(OthTranType tranType) {
        this.tranType = tranType;
    }

    public RestraintType getRestraintType() {
        return restraintType;
    }

    public void setRestraintType(RestraintType restraintType) {
        this.restraintType = restraintType;
    }
}

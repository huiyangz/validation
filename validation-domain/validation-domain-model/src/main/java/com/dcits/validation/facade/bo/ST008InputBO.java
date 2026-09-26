package com.dcits.validation.facade.bo;

/** ST008 检查是否存在现金止收限制 输入BO */
public class ST008InputBO {
    /** 账号 */
    private String baseAcctNo;

    public String getBaseAcctNo() {
        return baseAcctNo;
    }

    public void setBaseAcctNo(String baseAcctNo) {
        this.baseAcctNo = baseAcctNo;
    }
}

package com.dcits.validation.facade.bo;

/** ST005 检查账户是否存在限制 输入BO */
public class ST005InputBO {
    /** 账号 */
    private String baseAcctNo;

    public String getBaseAcctNo() {
        return baseAcctNo;
    }

    public void setBaseAcctNo(String baseAcctNo) {
        this.baseAcctNo = baseAcctNo;
    }
}

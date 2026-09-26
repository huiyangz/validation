package com.dcits.validation.facade.bo;

/** ST014 检查是否存在现金不收不付限制 输入BO */
public class ST014InputBO {
    /** 账号 */
    private String baseAcctNo;

    public String getBaseAcctNo() {
        return baseAcctNo;
    }

    public void setBaseAcctNo(String baseAcctNo) {
        this.baseAcctNo = baseAcctNo;
    }
}

package com.dcits.validation.facade.bo;

/**
 * ST012 检查是否存在不收不付限制 输入BO
 */
public class ST012InputBO {
    /** 账号 */
    private String baseAcctNo;

    public String getBaseAcctNo() {
        return baseAcctNo;
    }

    public void setBaseAcctNo(String baseAcctNo) {
        this.baseAcctNo = baseAcctNo;
    }
}

package com.dcits.validation.facade.bo;

/** ST010 检查是否存在转账不收不付限制 输入BO */
public class ST010InputBO {
    /** 账号 */
    private String baseAcctNo;

    public String getBaseAcctNo() {
        return baseAcctNo;
    }

    public void setBaseAcctNo(String baseAcctNo) {
        this.baseAcctNo = baseAcctNo;
    }
}

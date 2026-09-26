package com.dcits.validation.facade.bo;

/** ST009 检查是否存在止付限制 输入BO */
public class ST009InputBO {
    /** 账号 */
    private String baseAcctNo;

    public String getBaseAcctNo() {
        return baseAcctNo;
    }

    public void setBaseAcctNo(String baseAcctNo) {
        this.baseAcctNo = baseAcctNo;
    }
}

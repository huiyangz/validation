package com.dcits.validation.facade.bo;

/**
 * ST006 检查是否存在转账止付限制 步骤输入BO。
 */
public class ST006InputBO {
    /** 账号 */
    private String baseAcctNo;

    public String getBaseAcctNo() {
        return baseAcctNo;
    }

    public void setBaseAcctNo(String baseAcctNo) {
        this.baseAcctNo = baseAcctNo;
    }
}

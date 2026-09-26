package com.dcits.validation.facade.bo;

/**
 * ST015 检查是否存在属性限制 步骤输入BO。
 */
public class ST015InputBO {
	/** 账号（必填） */
	private String baseAcctNo;

	public String getBaseAcctNo() {
		return baseAcctNo;
	}

	public void setBaseAcctNo(String baseAcctNo) {
		this.baseAcctNo = baseAcctNo;
	}
}

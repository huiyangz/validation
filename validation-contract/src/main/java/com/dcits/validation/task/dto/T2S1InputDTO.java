package com.dcits.validation.task.dto;

import jakarta.validation.constraints.NotNull;

/**
 * T2S1 检查账户限制 输入DTO
 */
public class T2S1InputDTO {
    /** 账号 */
    @NotNull
    private String baseAcctNo;
    /** 交易类型 */
    @NotNull
    private String tranType;
    /** 渠道类型 */
    @NotNull
    private String sourceType;
    /** 账户限制类型 */
    @NotNull
    private String restraintType;
    /** 摘要码 */
    @NotNull
    private String narrativeCode;
    /** 产品类型 */
    @NotNull
    private String prodType;

    public String getBaseAcctNo() {
        return baseAcctNo;
    }

    public void setBaseAcctNo(String baseAcctNo) {
        this.baseAcctNo = baseAcctNo;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getRestraintType() {
        return restraintType;
    }

    public void setRestraintType(String restraintType) {
        this.restraintType = restraintType;
    }

    public String getNarrativeCode() {
        return narrativeCode;
    }

    public void setNarrativeCode(String narrativeCode) {
        this.narrativeCode = narrativeCode;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }
}

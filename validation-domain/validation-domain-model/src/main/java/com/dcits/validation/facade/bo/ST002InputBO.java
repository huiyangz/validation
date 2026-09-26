package com.dcits.validation.facade.bo;

import com.dcits.validation.enums.OthTranType;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.SourceType;

/** ST002 检查限制豁免 输入BO */
public class ST002InputBO {
    /** 渠道类型 */
    private SourceType sourceType;
    /** 账户限制类型 */
    private RestraintType restraintType;
    /** 交易类型 */
    private OthTranType tranType;
    /** 摘要码 */
    private String narrativeCode;
    /** 产品类型 */
    private String prodType;

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public RestraintType getRestraintType() {
        return restraintType;
    }

    public void setRestraintType(RestraintType restraintType) {
        this.restraintType = restraintType;
    }

    public OthTranType getTranType() {
        return tranType;
    }

    public void setTranType(OthTranType tranType) {
        this.tranType = tranType;
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

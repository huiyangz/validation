package com.dcits.validation.facade.components;

import com.dcits.validation.enums.AcctCcy;
import com.dcits.validation.enums.AcctNatureNo;
import com.dcits.validation.enums.AcctRiskLevel;
import com.dcits.validation.enums.AcctStatus;
import com.dcits.validation.enums.AcctVerifyFlag;
import com.dcits.validation.enums.AcctVerifyResult;
import com.dcits.validation.enums.AllDepInd;
import com.dcits.validation.enums.AllDraInd;
import com.dcits.validation.enums.AllDraRange;
import com.dcits.validation.enums.AnnualStatus;
import com.dcits.validation.enums.AutoRenewInd;
import com.dcits.validation.enums.BalType;
import com.dcits.validation.enums.CheckCertificateType;
import com.dcits.validation.enums.ClientType;
import com.dcits.validation.enums.DepositNature;
import com.dcits.validation.enums.FarmerFlag;
import com.dcits.validation.enums.FixedCall;
import com.dcits.validation.enums.IntIndFlag;
import com.dcits.validation.enums.ManageType;
import com.dcits.validation.enums.OsaFlag;
import com.dcits.validation.enums.RbAcctType;
import com.dcits.validation.enums.RbBusAcctPurpose;
import com.dcits.validation.enums.RenewMethod;
import com.dcits.validation.enums.SimpleAcct;
import com.dcits.validation.enums.SourceType;
import com.dcits.validation.enums.SpecAcctFlag;
import com.dcits.validation.enums.TermType;
import com.dcits.validation.enums.TranBranch;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dcits.validation.facade.eo.RbBusAcctEO;

/*实体表【对公存款账户主表(RB_BUS_ACCT)】数据服务接口*/
public interface IRbBusAcctBcc {
    /** count数据库表记录根据入参com.dcits.validation.facade.eo.RbBusAcctEO中的属性字段组合 **/
    long countByEo(RbBusAcctEO eo);

    /** remove数据库表记录根据入参com.dcits.validation.facade.eo.RbBusAcctEO中的属性字段组合 **/
    int removeByEo(RbBusAcctEO eo);

    /** remove 根据主键: 账户内部键值 **/
    int removeByPrimaryKey(Integer internalKey);

    int create(RbBusAcctEO eo);

    /** create数据库表记录，主键和EO对象中不允许为空的字段必填，其它可为空字段可选填，执行时根据com.dcits.validation.facade.eo.RbBusAcctEO中不为空的属性写入数据库**/
    int createSelective(RbBusAcctEO eo);

    /** find数据库表记录根据入参com.dcits.validation.facade.eo.RbBusAcctEO中的属性字段组合 **/
    List<RbBusAcctEO> findByEo(RbBusAcctEO eo);

    /** find 根据主键: 账户内部键值 **/
    RbBusAcctEO findByPrimaryKey(Integer internalKey);

    /**  根据主键: 账户内部键值执行更新记录操作，仅更新入参com.dcits.validation.facade.eo.RbBusAcctEO中不为空的属性字段 **/
    int modifyByPrimaryKeySelective(RbBusAcctEO eo);

    /** modify 根据主键: 账户内部键值 **/
    int modifyByPrimaryKey(RbBusAcctEO eo);
}
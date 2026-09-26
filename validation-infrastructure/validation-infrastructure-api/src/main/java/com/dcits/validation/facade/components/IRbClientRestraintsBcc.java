package com.dcits.validation.facade.components;

import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.SourceType;
import com.dcits.validation.enums.TermType;
import com.dcits.validation.enums.TranBranch;
import java.util.Date;
import java.util.List;

import com.dcits.validation.facade.eo.RbClientRestraintsEO;

/*实体表【客户限制表(RB_CLIENT_RESTRAINTS)】数据服务接口*/
public interface IRbClientRestraintsBcc {
    /** count数据库表记录根据入参com.dcits.validation.facade.eo.RbClientRestraintsEO中的属性字段组合 **/
    long countByEo(RbClientRestraintsEO eo);

    /** remove数据库表记录根据入参com.dcits.validation.facade.eo.RbClientRestraintsEO中的属性字段组合 **/
    int removeByEo(RbClientRestraintsEO eo);

    /** remove 根据主键: 限制编号、客户号 **/
    int removeByPrimaryKey(String resSeqNo, String clientNo);

    int create(RbClientRestraintsEO eo);

    /** create数据库表记录，主键和EO对象中不允许为空的字段必填，其它可为空字段可选填，执行时根据com.dcits.validation.facade.eo.RbClientRestraintsEO中不为空的属性写入数据库**/
    int createSelective(RbClientRestraintsEO eo);

    /** find数据库表记录根据入参com.dcits.validation.facade.eo.RbClientRestraintsEO中的属性字段组合 **/
    List<RbClientRestraintsEO> findByEo(RbClientRestraintsEO eo);

    /** find 根据主键: 限制编号、客户号 **/
    RbClientRestraintsEO findByPrimaryKey(String resSeqNo, String clientNo);

    /**  根据主键: 限制编号、客户号执行更新记录操作，仅更新入参com.dcits.validation.facade.eo.RbClientRestraintsEO中不为空的属性字段 **/
    int modifyByPrimaryKeySelective(RbClientRestraintsEO eo);

    /** modify 根据主键: 限制编号、客户号 **/
    int modifyByPrimaryKey(RbClientRestraintsEO eo);
}
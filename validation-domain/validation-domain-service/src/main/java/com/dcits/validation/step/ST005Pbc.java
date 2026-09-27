package com.dcits.validation.step;

import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.facade.bo.ST005InputBO;
import com.dcits.validation.facade.bo.ST005OutputBO;
import com.dcits.validation.facade.components.IRbBusAcctBcc;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.eo.RbBusAcctEO;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ST005 检查账户是否存在限制。
 * 上送账号为子账户（账户记录的上级账户内部键有值）时，按上级账户内部键查询【账户信息】取主账户账号作为待查账户，
 * 否则待查账户为上送账号；按待查账户查询限制状态 A-生效 的【账户限制信息】并返回限制编号、账户限制类型、限制状态，
 * 同时返回待查账户对应【账户信息】记录的账号与主账户标志。
 * 子账户判定按上级账户内部键有值绑定（门禁已接受结论 1 豁免判定依据且未补充取值）；
 * 多条生效限制记录映射单值输出按返回首条赋值（门禁已接受结论 2 豁免选取规则且未补充）。
 * 子账户的父账户记录未查到或主账户账号为空时，待查账户未确定：跳过限制查询，限制三输出为空并按成功返回，
 * 不做无账户限定的限制查询（该情形 SPEC 未定义，已上报需求缺口，处理口径待需求侧澄清）。
 * 本步骤无业务失败场景，失败仅由技术异常传播表达。
 */
@Service
public class ST005Pbc implements IST005 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ST005Pbc.class);

    private final IRbBusAcctBcc rbBusAcctBcc;
    private final IRbBusRestraintsBcc rbBusRestraintsBcc;

    public ST005Pbc(IRbBusAcctBcc rbBusAcctBcc, IRbBusRestraintsBcc rbBusRestraintsBcc) {
        this.rbBusAcctBcc = rbBusAcctBcc;
        this.rbBusRestraintsBcc = rbBusRestraintsBcc;
    }

    @Override
    public ST005OutputBO execute(ST005InputBO input) {
        ST005OutputBO output = new ST005OutputBO();
        // 子步骤1 获取主账户账号：按{账号}查询【账户信息】
        RbBusAcctEO acctInfo = findAcctInfo(input.getBaseAcctNo());
        // 若{账号}是子账户（上级账户内部键有值），按$上级账户内部键$查询【账户信息】取$主账户账号$；否则继续执行
        boolean childAcct = acctInfo != null && acctInfo.getParentInternalKey() != null;
        RbBusAcctEO pendingAcctInfo = childAcct
                ? rbBusAcctBcc.findByPrimaryKey(acctInfo.getParentInternalKey())
                : acctInfo;
        // 子步骤2 设置待查账户：是子账户时为[主账户账号]，否则为{账号}
        String pendingAcctNo = childAcct
                ? (pendingAcctInfo == null ? null : pendingAcctInfo.getBaseAcctNo())
                : input.getBaseAcctNo();
        // 输出：账号、主账户标志取[待查账户]对应【账户信息】记录
        if (pendingAcctInfo != null) {
            output.setBaseAcctNo(pendingAcctInfo.getBaseAcctNo());
            output.setLeadAcctFlag(pendingAcctInfo.getLeadAcctFlag());
        }
        // 子步骤3 获取账户限制：按[待查账户]+限制状态 A-生效 查询【对公存款账户限制表】；
        // 待查账户未确定时跳过查询（不按无账户条件查询他账户限制），成功语义与子步骤4无账户限制信息路径一致
        List<RbBusRestraintsEO> restraints = pendingAcctNo == null ? null : findEffectiveRestraints(pendingAcctNo);
        // 子步骤4 检查账户是否存在限制：赋值账户限制信息并返回；无生效记录时限制输出为空
        if (restraints != null && !restraints.isEmpty()) {
            RbBusRestraintsEO restraint = restraints.get(0);
            output.setResSeqNo(restraint.getResSeqNo());
            output.setRestraintType(restraint.getRestraintType());
            output.setRestraintsStatus(restraint.getRestraintsStatus());
        }
        output.setSucceed(true);
        return output;
    }

    /** 子步骤1 按{账号}查询【账户信息】，条件仅设账号，返回首条记录；未查询到时返回 null */
    private RbBusAcctEO findAcctInfo(String baseAcctNo) {
        RbBusAcctEO condition = new RbBusAcctEO();
        condition.setBaseAcctNo(baseAcctNo);
        List<RbBusAcctEO> acctList = rbBusAcctBcc.findByEo(condition);
        LOGGER.debug("账户信息查询完成，记录数：{}", acctList == null ? 0 : acctList.size());
        return acctList == null || acctList.isEmpty() ? null : acctList.get(0);
    }

    /** 子步骤3 按[待查账户]+限制状态 A-生效 组合查询生效限制记录集合 */
    private List<RbBusRestraintsEO> findEffectiveRestraints(String pendingAcctNo) {
        RbBusRestraintsEO condition = new RbBusRestraintsEO();
        condition.setBaseAcctNo(pendingAcctNo);
        condition.setRestraintsStatus(RestraintsStatus.A);
        List<RbBusRestraintsEO> restraints = rbBusRestraintsBcc.findByEo(condition);
        LOGGER.debug("账户限制信息查询完成，生效限制记录数：{}", restraints == null ? 0 : restraints.size());
        return restraints;
    }
}

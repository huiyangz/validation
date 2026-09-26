package com.dcits.validation.step;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST014InputBO;
import com.dcits.validation.facade.bo.ST014OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/**
 * ST014 检查是否存在现金不收不付限制。
 * <p>按账号查询生效账户限制及其限制类型，判定借贷方控制标志=A-禁止借贷方且现金标志=N-禁止现金；
 * 本步骤仅查询本地表，无数据库写入，无事务要求；无业务失败场景。</p>
 */
@Service
public class ST014Pbc implements IST014 {

    private final IRbBusRestraintsBcc rbBusRestraintsBcc;

    private final IRbRestraintTypeBcc rbRestraintTypeBcc;

    public ST014Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc, IRbRestraintTypeBcc rbRestraintTypeBcc) {
        this.rbBusRestraintsBcc = rbBusRestraintsBcc;
        this.rbRestraintTypeBcc = rbRestraintTypeBcc;
    }

    @Override
    public ST014OutputBO execute(ST014InputBO input) {
        ST014OutputBO output = new ST014OutputBO();

        // 子步骤1 获取账户限制信息：根据账号、限制状态=A-生效查询【对公存款账户限制表(RB_BUS_RESTRAINTS)】
        RbBusRestraintsEO restraint = findEffectiveRestraint(input.getBaseAcctNo());

        RbRestraintTypeEO restraintTypeEO = null;
        if (restraint != null) {
            output.setResSeqNo(restraint.getResSeqNo());
            output.setRestraintType(restraint.getRestraintType());
            output.setRestraintsStatus(restraint.getRestraintsStatus());

            // 子步骤2 获取账户限制类型信息：根据账户限制类型查询【存款限制类型表(RB_RESTRAINT_TYPE)】
            restraintTypeEO = rbRestraintTypeBcc.findByRestraintType(restraint.getRestraintType());
        }
        boolean typeEffective = restraintTypeEO != null && Status.A == restraintTypeEO.getStatus();
        if (typeEffective) {
            // 仅状态=A-生效的限制类型记录取用其借贷方控制标志、现金标志
            output.setStatus(restraintTypeEO.getStatus());
            output.setDrCrCtlFlag(restraintTypeEO.getDrCrCtlFlag());
            output.setCashFlag(restraintTypeEO.getCashFlag());
        }

        // 子步骤3 检查是否存在现金不收不付限制：借贷方控制标志=A-禁止借贷方且现金标志=N-禁止现金时为“是”，否则为“否”
        if (typeEffective && DrCrCtlFlag.A == restraintTypeEO.getDrCrCtlFlag()
                && "N".equals(restraintTypeEO.getCashFlag())) {
            output.setCashNonRcvPayFlag("是");
        } else {
            output.setCashNonRcvPayFlag("否");
        }
        output.setSucceed(true);
        return output;
    }

    /**
     * 子步骤1 获取账户限制信息：按账号与限制状态=A-生效查询【对公存款账户限制表(RB_BUS_RESTRAINTS)】，
     * 返回命中的账户限制信息（含账户限制类型、限制编号）；无生效记录时返回 null。
     */
    private RbBusRestraintsEO findEffectiveRestraint(String baseAcctNo) {
        RbBusRestraintsEO query = new RbBusRestraintsEO();
        query.setBaseAcctNo(baseAcctNo);
        query.setRestraintsStatus(RestraintsStatus.A);
        List<RbBusRestraintsEO> restraints = rbBusRestraintsBcc.findByEo(query);
        if (restraints.isEmpty()) {
            return null;
        }
        return restraints.get(0);
    }
}

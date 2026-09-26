package com.dcits.validation.step;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST009InputBO;
import com.dcits.validation.facade.bo.ST009OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/**
 * ST009 检查是否存在止付限制。
 * 子步骤1：根据账号 + 限制状态"A-生效"查询【账户限制信息】（RB_BUS_RESTRAINTS），
 * 取账户限制类型、限制编号；
 * 子步骤2：根据账户限制类型 + 状态"A-生效"查询【限制类型表】（RB_RESTRAINT_TYPE），
 * 取借贷方控制标志；
 * 子步骤3：借贷方控制标志等于"D-禁止借方"则止付标志为"是"，否则为"否"。
 * 本步骤无业务失败场景，失败仅由技术异常传播表达。
 * 需求未定义同一账号存在多条生效限制记录时的判定口径与单值输出来源，
 * 按已确认用例覆盖的单记录数据流取查询结果首条判定与输出。
 */
@Service
public class ST009Pbc implements IST009 {
    /** 止付标志：是 */
    private static final String STOP_FLAG_YES = "是";
    /** 止付标志：否 */
    private static final String STOP_FLAG_NO = "否";

    private final IRbBusRestraintsBcc rbBusRestraintsBcc;
    private final IRbRestraintTypeBcc rbRestraintTypeBcc;

    public ST009Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc, IRbRestraintTypeBcc rbRestraintTypeBcc) {
        this.rbBusRestraintsBcc = rbBusRestraintsBcc;
        this.rbRestraintTypeBcc = rbRestraintTypeBcc;
    }

    @Override
    public ST009OutputBO execute(ST009InputBO input) {
        ST009OutputBO output = new ST009OutputBO();
        // 子步骤1 获取账户限制信息：按{账号}+限制状态"A-生效"查询【账户限制信息】
        List<RbBusRestraintsEO> restraints = findEffectiveRestraints(input.getBaseAcctNo());
        if (restraints == null || restraints.isEmpty()) {
            // 无生效的账户限制记录：无账户限制类型可查限制类型表，子步骤3无借贷方控制标志，按"否则"返回"否"
            output.setStopFlag(STOP_FLAG_NO);
            output.setSucceed(true);
            return output;
        }
        // 需求未定义多条生效限制记录的判定口径，按已确认单记录数据流取首条判定与输出
        RbBusRestraintsEO restraint = restraints.get(0);
        output.setResSeqNo(restraint.getResSeqNo());
        output.setRestraintType(restraint.getRestraintType());
        output.setRestraintsStatus(restraint.getRestraintsStatus());
        // 子步骤2 获取借贷方控制标志：按账户限制类型 + 状态"A-生效"查询【限制类型表】
        RbRestraintTypeEO restraintTypeRow = findEffectiveRestraintType(restraint.getRestraintType());
        if (restraintTypeRow != null) {
            output.setDrCrCtlFlag(restraintTypeRow.getDrCrCtlFlag());
            output.setStatus(restraintTypeRow.getStatus());
        }
        // 子步骤3 检查是否存在止付限制：借贷方控制标志等于"D-禁止借方"则止付标志"是"，否则"否"
        if (DrCrCtlFlag.D.equals(output.getDrCrCtlFlag())) {
            output.setStopFlag(STOP_FLAG_YES);
        } else {
            // 子步骤3"否则"：借贷方控制标志不等于"D-禁止借方"（含类型表无生效行、无标志），止付标志"否"
            output.setStopFlag(STOP_FLAG_NO);
        }
        output.setSucceed(true);
        return output;
    }

    /** 子步骤1：按账号 + 限制状态"A-生效"查询【账户限制信息】（RB_BUS_RESTRAINTS） */
    private List<RbBusRestraintsEO> findEffectiveRestraints(String baseAcctNo) {
        RbBusRestraintsEO queryEo = new RbBusRestraintsEO();
        queryEo.setBaseAcctNo(baseAcctNo);
        queryEo.setRestraintsStatus(RestraintsStatus.A);
        return rbBusRestraintsBcc.findByEo(queryEo);
    }

    /** 子步骤2：按账户限制类型 + 状态"A-生效"查询【限制类型表】（RB_RESTRAINT_TYPE）生效行；账户限制类型为该表主键，结果至多一条，无生效行返回 null */
    private RbRestraintTypeEO findEffectiveRestraintType(RestraintType restraintType) {
        RbRestraintTypeEO queryEo = new RbRestraintTypeEO();
        queryEo.setRestraintType(restraintType);
        queryEo.setStatus(Status.A);
        List<RbRestraintTypeEO> typeRows = rbRestraintTypeBcc.findByEo(queryEo);
        if (typeRows == null || typeRows.isEmpty()) {
            return null;
        }
        return typeRows.get(0);
    }
}

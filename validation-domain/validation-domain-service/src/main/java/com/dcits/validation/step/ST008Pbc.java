package com.dcits.validation.step;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST008InputBO;
import com.dcits.validation.facade.bo.ST008OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/** ST008 检查是否存在现金止收限制：按账号查生效账户限制，结合限制类型表判断是否存在借贷方控制标志=C-禁止贷方且现金标志=N-不允许现金的现金止收限制 */
@Service
public class ST008Pbc implements IST008 {

    private final IRbBusRestraintsBcc rbBusRestraintsBcc;
    private final IRbRestraintTypeBcc rbRestraintTypeBcc;

    public ST008Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc, IRbRestraintTypeBcc rbRestraintTypeBcc) {
        this.rbBusRestraintsBcc = rbBusRestraintsBcc;
        this.rbRestraintTypeBcc = rbRestraintTypeBcc;
    }

    @Override
    public ST008OutputBO execute(ST008InputBO input) {
        ST008OutputBO output = new ST008OutputBO();

        // 子步骤1 获取账户限制信息：根据{账号}、$限制状态$=A-生效查询【账户限制信息】，须包括$账户限制类型$、$限制编号$
        RbBusRestraintsEO queryEo = new RbBusRestraintsEO();
        queryEo.setBaseAcctNo(input.getBaseAcctNo());
        queryEo.setRestraintsStatus(RestraintsStatus.A);
        List<RbBusRestraintsEO> restraints = rbBusRestraintsBcc.findByEo(queryEo);

        // 子步骤2 获取账户限制类型信息 + 子步骤3 检查是否存在现金止收限制：逐条按$账户限制类型$查询【限制类型表】，
        // 仅$状态$=A-生效的类型记录提供$借贷方控制标志$、$现金标志$；任一记录命中"C-禁止贷方且N-不允许现金"即取该首条满足记录
        for (RbBusRestraintsEO restraint : restraints) {
            RbRestraintTypeEO restraintTypeEo = rbRestraintTypeBcc.findByRestraintType(restraint.getRestraintType());
            if (restraintTypeEo == null || restraintTypeEo.getStatus() != Status.A) {
                // 限制类型表无该类型记录或类型状态非A-生效：无生效标志，该限制记录不参与命中判断
                continue;
            }
            if (restraintTypeEo.getDrCrCtlFlag() == DrCrCtlFlag.C && "N".equals(restraintTypeEo.getCashFlag())) {
                output.setCashStopFlag("是");
                output.setResSeqNo(restraint.getResSeqNo());
                output.setRestraintType(restraint.getRestraintType());
                output.setRestraintsStatus(restraint.getRestraintsStatus());
                output.setRestraintTypeDef(restraintTypeEo.getRestraintType());
                output.setStatus(restraintTypeEo.getStatus());
                output.setDrCrCtlFlag(restraintTypeEo.getDrCrCtlFlag());
                output.setCashFlag(restraintTypeEo.getCashFlag());
                output.setSucceed(true);
                return output;
            }
        }

        // 无满足条件的记录：现金止收标志为"否"，其余输出字段为空；本步骤无业务失败场景
        output.setCashStopFlag("否");
        output.setSucceed(true);
        return output;
    }
}

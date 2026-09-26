package com.dcits.validation.step;

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST010InputBO;
import com.dcits.validation.facade.bo.ST010OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/** ST010 检查是否存在转账不收不付限制 */
@Service
public class ST010Pbc implements IST010 {
    private static final Logger LOGGER = LoggerFactory.getLogger(ST010Pbc.class);

    /** 转账不收不付标志：是 */
    private static final String FLAG_YES = "是";
    /** 转账不收不付标志：否 */
    private static final String FLAG_NO = "否";
    /** 转账标志代码：N-禁止转账 */
    private static final String TRANSFER_FLAG_FORBIDDEN = "N";

    private final IRbBusRestraintsBcc rbBusRestraintsBcc;
    private final IRbRestraintTypeBcc rbRestraintTypeBcc;

    public ST010Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc, IRbRestraintTypeBcc rbRestraintTypeBcc) {
        this.rbBusRestraintsBcc = rbBusRestraintsBcc;
        this.rbRestraintTypeBcc = rbRestraintTypeBcc;
    }

    @Override
    public ST010OutputBO execute(ST010InputBO input) {
        ST010OutputBO output = new ST010OutputBO();
        output.setTransferNoRecvNoPayFlag(FLAG_NO);

        // 子步骤1 获取账户限制信息：根据账号、限制状态A-生效查询账户限制信息，可能存在多条记录，逐条执行子步骤2、3的判断
        RbBusRestraintsEO queryEo = new RbBusRestraintsEO();
        queryEo.setBaseAcctNo(input.getBaseAcctNo());
        queryEo.setRestraintsStatus(RestraintsStatus.A);
        List<RbBusRestraintsEO> restraintsList = rbBusRestraintsBcc.findByEo(queryEo);

        if (restraintsList != null) {
            for (RbBusRestraintsEO restraints : restraintsList) {
                fillRestraintsInfo(output, restraints);
                // 子步骤2 获取账户限制类型信息：按本条账户限制类型查询限制类型表，仅状态A-生效的记录提供借贷方控制标志、转账标志
                RbRestraintTypeEO restraintTypeEo = rbRestraintTypeBcc.findByRestraintType(restraints.getRestraintType());
                boolean typeEffective = restraintTypeEo != null && restraintTypeEo.getStatus() == Status.A;
                fillRestraintTypeInfo(output, typeEffective ? restraintTypeEo : null);
                // 子步骤3 检查是否存在转账不收不付限制：借贷方控制标志=A-禁止借贷方且转账标志=N-禁止转账即命中，任一命中即返回"是"及该条账户限制信息
                if (typeEffective
                        && restraintTypeEo.getDrCrCtlFlag() == DrCrCtlFlag.A
                        && TRANSFER_FLAG_FORBIDDEN.equals(restraintTypeEo.getTransferFlag())) {
                    output.setTransferNoRecvNoPayFlag(FLAG_YES);
                    break;
                }
            }
        }

        LOGGER.debug("ST010 账户限制信息查询条数：{}，转账不收不付标志：{}",
                restraintsList == null ? 0 : restraintsList.size(), output.getTransferNoRecvNoPayFlag());
        output.setSucceed(true);
        return output;
    }

    /** 填充当前处理的账户限制信息字段（来自 RB_BUS_RESTRAINTS） */
    private void fillRestraintsInfo(ST010OutputBO output, RbBusRestraintsEO restraints) {
        output.setResSeqNo(restraints.getResSeqNo());
        output.setRestraintType(restraints.getRestraintType());
        output.setRestraintsStatus(restraints.getRestraintsStatus());
    }

    /** 填充限制类型信息字段（来自 RB_RESTRAINT_TYPE）；typeEo 为 null 时对应字段全部置空 */
    private void fillRestraintTypeInfo(ST010OutputBO output, RbRestraintTypeEO typeEo) {
        if (typeEo == null) {
            output.setDrCrCtlFlag(null);
            output.setStatus(null);
            output.setTransferFlag(null);
            output.setStopFlag(null);
            return;
        }
        output.setDrCrCtlFlag(typeEo.getDrCrCtlFlag());
        output.setStatus(typeEo.getStatus());
        output.setTransferFlag(typeEo.getTransferFlag());
        output.setStopFlag(typeEo.getStopFlag());
    }
}

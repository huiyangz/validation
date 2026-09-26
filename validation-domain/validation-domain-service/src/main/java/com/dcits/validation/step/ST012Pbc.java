package com.dcits.validation.step;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dcits.validation.facade.bo.ST012InputBO;
import com.dcits.validation.facade.bo.ST012OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;
import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;

/**
 * ST012 检查是否存在不收不付限制
 *
 * <p>根据账号查询生效的账户限制记录，逐条核对限制类型表中 A-生效记录的
 * 借贷方控制标志；任一记录标志为 A-禁止借贷方即判定存在不收不付限制，
 * 输出取该命中记录的值，否则不收不付标志为"否"、其余输出字段为空。</p>
 */
@Service
public class ST012Pbc implements IST012 {

    /** 不收不付标志：是 */
    private static final String FLAG_YES = "是";
    /** 不收不付标志：否 */
    private static final String FLAG_NO = "否";

    private final IRbBusRestraintsBcc rbBusRestraintsBcc;
    private final IRbRestraintTypeBcc rbRestraintTypeBcc;

    public ST012Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc, IRbRestraintTypeBcc rbRestraintTypeBcc) {
        this.rbBusRestraintsBcc = rbBusRestraintsBcc;
        this.rbRestraintTypeBcc = rbRestraintTypeBcc;
    }

    @Override
    public ST012OutputBO execute(ST012InputBO input) {
        ST012OutputBO output = new ST012OutputBO();
        // 子步骤1 获取账户限制信息：根据{账号}、限制状态等于"A-生效"查询【账户限制信息】
        List<RbBusRestraintsEO> restraintsList = findEffectiveRestraints(input.getBaseAcctNo());
        if (restraintsList == null || restraintsList.isEmpty()) {
            // 无生效限制记录：不收不付标志为"否"，其余输出字段为空
            output.setNoDebitNoCreditFlag(FLAG_NO);
            output.setSucceed(true);
            return output;
        }
        for (RbBusRestraintsEO restraints : restraintsList) {
            // 子步骤2 获取账户限制类型信息：根据账户限制类型查询【限制类型表】，
            // 仅状态等于"A-生效"的记录其借贷方控制标志参与判定
            RbRestraintTypeEO restraintTypeEO =
                    rbRestraintTypeBcc.findByRestraintType(restraints.getRestraintType());
            if (restraintTypeEO == null || restraintTypeEO.getStatus() != Status.A) {
                // 类型记录不存在或状态非"A-生效"：无借贷方控制标志可比较，本条未命中
                continue;
            }
            // 子步骤3 检查是否存在不收不付限制：借贷方控制标志等于"A-禁止借贷方"即命中
            if (restraintTypeEO.getDrCrCtlFlag() == DrCrCtlFlag.A) {
                output.setNoDebitNoCreditFlag(FLAG_YES);
                output.setResSeqNo(restraints.getResSeqNo());
                output.setRestraintType(restraints.getRestraintType());
                output.setRestraintsStatus(restraints.getRestraintsStatus());
                output.setDrCrCtlFlag(restraintTypeEO.getDrCrCtlFlag());
                output.setStatus(restraintTypeEO.getStatus());
                output.setSucceed(true);
                return output;
            }
        }
        // 全部未命中：不收不付标志为"否"，其余输出字段为空
        output.setNoDebitNoCreditFlag(FLAG_NO);
        output.setSucceed(true);
        return output;
    }

    /**
     * 子步骤1 实体查询：按账号和限制状态"A-生效"查询【对公存款账户限制表 RB_BUS_RESTRAINTS】
     */
    private List<RbBusRestraintsEO> findEffectiveRestraints(String baseAcctNo) {
        RbBusRestraintsEO queryEO = new RbBusRestraintsEO();
        queryEO.setBaseAcctNo(baseAcctNo);
        queryEO.setRestraintsStatus(RestraintsStatus.A);
        return rbBusRestraintsBcc.findByEo(queryEO);
    }
}

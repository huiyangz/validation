package com.dcits.validation.step;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST006InputBO;
import com.dcits.validation.facade.bo.ST006OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/**
 * ST006 检查是否存在转账止付限制 步骤实现。
 *
 * <p>子步骤1 按账号与限制状态=A-生效查询【账户限制信息】；子步骤2 按[账户限制信息]中的
 * 账户限制类型查询【限制类型表】；子步骤3 逐条检查（限制类型记录不存在或状态不等于
 * A-生效的记录视为不满足），借贷方控制标志=D-禁止借方且转账标志=N-不允许转账时
 * 返回转账止付标志"是"（输出取第一条满足条件记录的值），否则返回"否"且查询类输出字段为空。
 * 本步骤无业务失败场景，失败仅由技术异常传播表达。
 */
@Service
public class ST006Pbc implements IST006 {

    /** 转账止付标志：是 */
    private static final String STOP_FLAG_YES = "是";
    /** 转账止付标志：否 */
    private static final String STOP_FLAG_NO = "否";
    /** 转账标志取值：N-不允许转账 */
    private static final String TRANSFER_FLAG_NOT_ALLOWED = "N";

    private final IRbBusRestraintsBcc rbBusRestraintsBcc;
    private final IRbRestraintTypeBcc rbRestraintTypeBcc;

    public ST006Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc, IRbRestraintTypeBcc rbRestraintTypeBcc) {
        this.rbBusRestraintsBcc = rbBusRestraintsBcc;
        this.rbRestraintTypeBcc = rbRestraintTypeBcc;
    }

    @Override
    public ST006OutputBO execute(ST006InputBO input) {
        ST006OutputBO output = new ST006OutputBO();

        // 子步骤1：获取账户限制信息——根据{账号}、限制状态等于"A-生效"查询【账户限制信息】，
        // 结果0条或多条，必须包括账户限制类型、限制编号
        RbBusRestraintsEO queryEo = new RbBusRestraintsEO();
        queryEo.setBaseAcctNo(input.getBaseAcctNo());
        queryEo.setRestraintsStatus(RestraintsStatus.A);
        List<RbBusRestraintsEO> restraintList = rbBusRestraintsBcc.findByEo(queryEo);

        boolean stopPaymentFound = false;
        for (RbBusRestraintsEO restraint : restraintList) {
            // 子步骤2：获取账户限制类型——根据[账户限制信息]中的账户限制类型查询【限制类型表】
            RbRestraintTypeEO restraintTypeRecord =
                    rbRestraintTypeBcc.findByRestraintType(restraint.getRestraintType());

            // 子步骤3：限制类型记录不存在或状态不等于"A-生效"的记录视为不满足
            if (restraintTypeRecord == null || !Status.A.equals(restraintTypeRecord.getStatus())) {
                continue;
            }

            // 子步骤3：借贷方控制标志等于"D-禁止借方"且转账标志等于"N-不允许转账"则满足条件
            if (DrCrCtlFlag.D.equals(restraintTypeRecord.getDrCrCtlFlag())
                    && TRANSFER_FLAG_NOT_ALLOWED.equals(restraintTypeRecord.getTransferFlag())) {
                output.setResSeqNo(restraint.getResSeqNo());
                output.setRestraintType(restraint.getRestraintType());
                output.setRestraintsStatus(restraint.getRestraintsStatus());
                output.setDrCrCtlFlag(restraintTypeRecord.getDrCrCtlFlag());
                output.setStatus(restraintTypeRecord.getStatus());
                output.setTransferFlag(restraintTypeRecord.getTransferFlag());
                output.setStopFlag(STOP_FLAG_YES);
                stopPaymentFound = true;
                break;
            }
        }

        // 子步骤3：全部记录均不满足时返回"否"，查询类输出字段保持为空
        if (!stopPaymentFound) {
            output.setStopFlag(STOP_FLAG_NO);
        }

        output.setSucceed(true);
        return output;
    }
}

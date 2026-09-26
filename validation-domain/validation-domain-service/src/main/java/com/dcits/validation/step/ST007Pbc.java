package com.dcits.validation.step;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST007InputBO;
import com.dcits.validation.facade.bo.ST007OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/**
 * ST007 检查质押类限制 步骤实现
 *
 * <p>无业务失败场景，失败仅由技术异常传播表达；本步骤只读本地数据，无事务要求。</p>
 */
@Service
public class ST007Pbc implements IST007 {

    private final IRbBusRestraintsBcc rbBusRestraintsBcc;
    private final IRbRestraintTypeBcc rbRestraintTypeBcc;

    public ST007Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc, IRbRestraintTypeBcc rbRestraintTypeBcc) {
        this.rbBusRestraintsBcc = rbBusRestraintsBcc;
        this.rbRestraintTypeBcc = rbRestraintTypeBcc;
    }

    @Override
    public ST007OutputBO execute(ST007InputBO input) {
        ST007OutputBO output = new ST007OutputBO();

        // 子步骤1 获取账户限制信息：根据{账号}、限制状态等于A-生效查询【账户限制信息】，
        // 结果为该账号全部生效限制记录的集合，可为空；每条记录含账户限制类型、限制编号
        RbBusRestraintsEO query = new RbBusRestraintsEO();
        query.setBaseAcctNo(input.getBaseAcctNo());
        query.setRestraintsStatus(RestraintsStatus.A);
        List<RbBusRestraintsEO> restraints = rbBusRestraintsBcc.findByEo(query);

        // 子步骤2 获取质押标志：按顺序遍历[账户限制信息]，对每条记录按其账户限制类型查询【限制类型表】，
        // 取状态等于A-生效的质押标志；取到首个非空质押标志后停止遍历，该记录的限制编号、账户限制类型、
        // 限制状态与质押标志一并作为输出返回；遍历结束仍无命中时输出字段均为空，步骤以成功状态返回
        for (RbBusRestraintsEO restraint : restraints) {
            RbRestraintTypeEO restraintTypeEO = rbRestraintTypeBcc.findByRestraintType(restraint.getRestraintType());
            if (restraintTypeEO == null || restraintTypeEO.getStatus() != Status.A
                    || restraintTypeEO.getPledgedFlag() == null) {
                continue;
            }
            output.setResSeqNo(restraint.getResSeqNo());
            output.setRestraintType(restraint.getRestraintType());
            output.setRestraintsStatus(restraint.getRestraintsStatus());
            output.setPledgedFlag(restraintTypeEO.getPledgedFlag());
            output.setStatus(restraintTypeEO.getStatus());
            break;
        }

        output.setSucceed(true);
        return output;
    }
}

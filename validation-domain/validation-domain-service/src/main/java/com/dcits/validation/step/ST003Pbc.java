package com.dcits.validation.step;

import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST003InputBO;
import com.dcits.validation.facade.bo.ST003OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ST003 检查有权机关冻结限制。
 * 按账号查询生效的账户限制记录，再逐条按账户限制类型查询限制类型表，
 * 取状态为 A-生效 的有权机关冻结标志；无生效记录或均未查到时标志为空，步骤均成功返回。
 * 本步骤无业务失败场景，失败仅由技术异常传播表达。
 */
@Service
public class ST003Pbc implements IST003 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ST003Pbc.class);

    private final IRbBusRestraintsBcc rbBusRestraintsBcc;
    private final IRbRestraintTypeBcc rbRestraintTypeBcc;

    public ST003Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc, IRbRestraintTypeBcc rbRestraintTypeBcc) {
        this.rbBusRestraintsBcc = rbBusRestraintsBcc;
        this.rbRestraintTypeBcc = rbRestraintTypeBcc;
    }

    @Override
    public ST003OutputBO execute(ST003InputBO input) {
        ST003OutputBO output = new ST003OutputBO();
        // 子步骤1 获取账户限制信息：根据{账号}、限制状态 A-生效 查询【账户限制信息】
        List<RbBusRestraintsEO> restraints = findEffectiveRestraints(input.getBaseAcctNo());
        // 无生效记录时[账户限制信息]为空集合，步骤成功返回，[有权机关冻结标志]为空
        if (restraints == null || restraints.isEmpty()) {
            output.setSucceed(true);
            return output;
        }
        // 子步骤2 获取有权机关冻结标志：逐条按账户限制类型查询【限制类型表】，命中即赋值返回
        output.setAhBuFlag(findAhBuFlag(restraints));
        output.setSucceed(true);
        return output;
    }

    /** 子步骤1 获取账户限制信息：按账号 + 限制状态 A-生效 组合查询生效限制记录集合 */
    private List<RbBusRestraintsEO> findEffectiveRestraints(String baseAcctNo) {
        RbBusRestraintsEO condition = new RbBusRestraintsEO();
        condition.setBaseAcctNo(baseAcctNo);
        condition.setRestraintsStatus(RestraintsStatus.A);
        List<RbBusRestraintsEO> restraints = rbBusRestraintsBcc.findByEo(condition);
        LOGGER.debug("账户限制信息查询完成，生效限制记录数：{}", restraints == null ? 0 : restraints.size());
        return restraints;
    }

    /** 子步骤2 获取有权机关冻结标志：只要查询到状态 A-生效 的标志即赋值返回，均未查询到时为 null */
    private String findAhBuFlag(List<RbBusRestraintsEO> restraints) {
        for (RbBusRestraintsEO restraint : restraints) {
            RbRestraintTypeEO restraintTypeEO = rbRestraintTypeBcc.findByRestraintType(restraint.getRestraintType());
            if (restraintTypeEO != null && restraintTypeEO.getStatus() == Status.A) {
                return restraintTypeEO.getAhBuFlag();
            }
        }
        return null;
    }
}

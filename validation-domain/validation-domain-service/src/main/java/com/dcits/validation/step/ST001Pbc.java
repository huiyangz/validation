package com.dcits.validation.step;

import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.facade.bo.ST001InputBO;
import com.dcits.validation.facade.bo.ST001OutputBO;
import com.dcits.validation.facade.components.IRbClientRestraintsBcc;
import com.dcits.validation.facade.eo.RbClientRestraintsEO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ST001 检查客户是否存在限制
 * <p>根据上送客户号查询【客户限制表】中限制状态等于"A-生效"的全部客户限制信息并返回。</p>
 */
@Service
public class ST001Pbc implements IST001 {

    @Autowired
    private IRbClientRestraintsBcc rbClientRestraintsBcc;

    @Override
    public ST001OutputBO execute(ST001InputBO input) {
        ST001OutputBO output = new ST001OutputBO();
        // 子步骤1 获取客户限制：按上送客户号查询限制状态为"A-生效"的全部客户限制
        List<RbClientRestraintsEO> restraintEos = findEffectiveRestraints(input.getClientNo());
        // 子步骤2 返回客户限制信息：赋值客户限制信息并返回
        output.setRestraints(convertRestraints(restraintEos));
        output.setSucceed(true);
        return output;
    }

    /**
     * 子步骤1 获取客户限制：按上送{客户号}查询【客户限制表】中$限制状态$等于"A-生效"的全部记录
     */
    private List<RbClientRestraintsEO> findEffectiveRestraints(String clientNo) {
        RbClientRestraintsEO condition = new RbClientRestraintsEO();
        condition.setClientNo(clientNo);
        condition.setRestraintsStatus(RestraintsStatus.A);
        return rbClientRestraintsBcc.findByEo(condition);
    }

    /**
     * 子步骤2 返回客户限制信息：将查询结果逐条映射为输出DTO（$限制编号$、$账户限制类型$、$限制状态$）
     */
    private List<ST001OutputBO.RestraintDTO> convertRestraints(List<RbClientRestraintsEO> restraintEos) {
        List<ST001OutputBO.RestraintDTO> restraints = new ArrayList<>(restraintEos.size());
        for (RbClientRestraintsEO restraintEo : restraintEos) {
            ST001OutputBO.RestraintDTO restraint = new ST001OutputBO.RestraintDTO();
            restraint.setResSeqNo(restraintEo.getResSeqNo());
            restraint.setRestraintType(restraintEo.getRestraintType());
            restraint.setRestraintsStatus(restraintEo.getRestraintsStatus());
            restraints.add(restraint);
        }
        return restraints;
    }
}

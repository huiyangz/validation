package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST011InputBO;
import com.dcits.validation.facade.bo.ST011OutputBO;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.components.IRbTranDefBcc;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;
import com.dcits.validation.facade.eo.RbTranDefEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ST011 检查限制优先级
 * 子步骤1：根据{交易类型}查询【交易定义信息】获取交易的$限制级别$；
 * 子步骤2：根据{账户限制类型}查询【限制类型定义信息】获取限制的$冻结级别$；
 * 子步骤3：限制级别数值大于冻结级别返回“不检查限制”，否则返回“继续检查”（RES_PRIORITY 为数字编码，按数值比较）。
 * 本步骤无业务失败场景，失败仅由技术异常传播表达。
 */
@Service
public class ST011Pbc implements IST011 {
    /** 检查结果：不检查限制 */
    private static final String CHECK_RESULT_SKIP = "不检查限制";
    /** 检查结果：继续检查 */
    private static final String CHECK_RESULT_CONTINUE = "继续检查";

    @Autowired
    private IRbTranDefBcc rbTranDefBcc;
    @Autowired
    private IRbRestraintTypeBcc rbRestraintTypeBcc;

    @Override
    public ST011OutputBO execute(ST011InputBO input) {
        // 子步骤1：获取交易的限制级别——根据{交易类型}查询【交易定义信息】
        RbTranDefEO tranDef = rbTranDefBcc.findByTranType(input.getTranType());
        // 子步骤2：获取限制的冻结级别——根据{账户限制类型}查询【限制类型定义信息】
        RbRestraintTypeEO restraintTypeDef = rbRestraintTypeBcc.findByRestraintType(input.getRestraintType());

        // 子步骤3：检查限制优先级——RES_PRIORITY 为数字编码，按数值比较
        int tranResPriority = Integer.parseInt(tranDef.getResPriority());
        int resFreezePriority = Integer.parseInt(restraintTypeDef.getResPriority());

        ST011OutputBO output = new ST011OutputBO();
        if (tranResPriority > resFreezePriority) {
            output.setCheckResult(CHECK_RESULT_SKIP);
        } else {
            output.setCheckResult(CHECK_RESULT_CONTINUE);
        }
        output.setSucceed(true);
        return output;
    }
}

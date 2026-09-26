package com.dcits.validation.step;

import org.springframework.stereotype.Service;

import com.dcits.validation.facade.bo.ST013InputBO;
import com.dcits.validation.facade.bo.ST013OutputBO;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;

/** ST013 检查账户是否存在不允许销户的限制 - 步骤实现 */
@Service
public class ST013Pbc implements IST013 {
    /** 销户标志取值：N-否 */
    private static final String CLOSE_ACCT_FLAG_NO = "N";
    /** 允许销户标志取值：允许销户 */
    private static final String FLAG_ALLOW_CLOSE = "允许销户";
    /** 允许销户标志取值：不允许销户 */
    private static final String FLAG_NOT_ALLOW_CLOSE = "不允许销户";

    private final IRbRestraintTypeBcc rbRestraintTypeBcc;

    public ST013Pbc(IRbRestraintTypeBcc rbRestraintTypeBcc) {
        this.rbRestraintTypeBcc = rbRestraintTypeBcc;
    }

    @Override
    public ST013OutputBO execute(ST013InputBO input) {
        ST013OutputBO output = new ST013OutputBO();
        // 子步骤1 检查销户标志：遍历账户限制信息集合
        for (ST013InputBO.RestraintInfoDTO restraintInfo : input.getRestraintList()) {
            // 子步骤1-1) 取当前遍历记录的账户限制类型查询【限制类型信息】获取销户标志
            RbRestraintTypeEO restraintTypeEO = rbRestraintTypeBcc.findByRestraintType(restraintInfo.getRestraintType());
            // 子步骤1-2) 销户标志为“N-否”时返回允许销户标志为“不允许销户”并中断遍历，否则继续遍历
            if (CLOSE_ACCT_FLAG_NO.equals(restraintTypeEO.getCloseAcctFlag())) {
                output.setAllowCloseAcctFlag(FLAG_NOT_ALLOW_CLOSE);
                output.setSucceed(true);
                return output;
            }
        }
        // 结束遍历：返回允许销户标志为“允许销户”
        output.setAllowCloseAcctFlag(FLAG_ALLOW_CLOSE);
        output.setSucceed(true);
        return output;
    }
}

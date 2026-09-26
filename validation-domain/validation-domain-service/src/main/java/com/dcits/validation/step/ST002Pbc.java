package com.dcits.validation.step;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST002InputBO;
import com.dcits.validation.facade.bo.ST002OutputBO;
import com.dcits.validation.facade.components.IFmChannelBcc;
import com.dcits.validation.facade.components.IRbRestraintControlDetailsBcc;
import com.dcits.validation.facade.eo.FmChannelEO;
import com.dcits.validation.facade.eo.RbRestraintControlDetailsEO;

/** ST002 检查限制豁免 步骤实现 */
@Service
public class ST002Pbc implements IST002 {

    /** 柜面标志：Y-是 */
    private static final String COUNTER_FLAG_YES = "Y";
    /** 检查结果：不检查限制 */
    private static final String CHECK_RESULT_NO_CHECK = "不检查限制";
    /** 检查结果：需检查限制 */
    private static final String CHECK_RESULT_NEED_CHECK = "需检查限制";
    /** 检查结果：豁免 */
    private static final String CHECK_RESULT_EXEMPT = "豁免";
    /** 检查结果：不豁免 */
    private static final String CHECK_RESULT_NOT_EXEMPT = "不豁免";

    private final IFmChannelBcc fmChannelBcc;
    private final IRbRestraintControlDetailsBcc rbRestraintControlDetailsBcc;

    public ST002Pbc(IFmChannelBcc fmChannelBcc, IRbRestraintControlDetailsBcc rbRestraintControlDetailsBcc) {
        this.fmChannelBcc = fmChannelBcc;
        this.rbRestraintControlDetailsBcc = rbRestraintControlDetailsBcc;
    }

    @Override
    public ST002OutputBO execute(ST002InputBO input) {
        ST002OutputBO output = new ST002OutputBO();
        // 子步骤1 获取渠道的柜面标志：根据{渠道类型}查询渠道类型表；渠道未配置时柜面标志保持 null
        FmChannelEO channelEO = fmChannelBcc.findByChannel(input.getSourceType());
        if (channelEO != null) {
            output.setChannelCounterFlag(channelEO.getCounterFlag());
        }
        // 子步骤2 获取限制控制明细：根据{账户限制类型}查询状态等于“A-生效”的限制控制明细
        List<RbRestraintControlDetailsEO> details = findEffectiveDetails(input);
        // 子步骤3 检查柜面标志：渠道柜面标志等于“Y-是”且明细中存在柜面标志等于“Y-是”的记录时跳转子步骤4，否则跳转子步骤5
        if (isCounterChannel(output.getChannelCounterFlag(), details)) {
            // 子步骤4 检查柜面渠道的豁免信息：三项同时匹配返回“不检查限制”，否则返回“需检查限制”
            RbRestraintControlDetailsEO matched = findExemptionMatch(details, input);
            if (matched != null) {
                echoMatchedDetail(matched, output);
                output.setCheckResult(CHECK_RESULT_NO_CHECK);
            } else {
                output.setCheckResult(CHECK_RESULT_NEED_CHECK);
            }
        } else {
            // 子步骤5 检查非柜面渠道的豁免信息：三项同时匹配返回“豁免”，否则返回“不豁免”
            RbRestraintControlDetailsEO matched = findExemptionMatch(details, input);
            if (matched != null) {
                echoMatchedDetail(matched, output);
                output.setCheckResult(CHECK_RESULT_EXEMPT);
            } else {
                output.setCheckResult(CHECK_RESULT_NOT_EXEMPT);
            }
        }
        output.setSucceed(true);
        return output;
    }

    /** 子步骤2：按{账户限制类型}且状态“A-生效”查询限制控制明细 */
    private List<RbRestraintControlDetailsEO> findEffectiveDetails(ST002InputBO input) {
        RbRestraintControlDetailsEO queryEO = new RbRestraintControlDetailsEO();
        queryEO.setRestraintType(input.getRestraintType());
        queryEO.setStatus(Status.A);
        return rbRestraintControlDetailsBcc.findByEo(queryEO);
    }

    /** 子步骤3 判断：渠道柜面标志等于“Y-是”且明细中存在柜面标志等于“Y-是”的记录 */
    private boolean isCounterChannel(String channelCounterFlag, List<RbRestraintControlDetailsEO> details) {
        if (!COUNTER_FLAG_YES.equals(channelCounterFlag) || details == null || details.isEmpty()) {
            return false;
        }
        for (RbRestraintControlDetailsEO detail : details) {
            if (COUNTER_FLAG_YES.equals(detail.getCounterFlag())) {
                return true;
            }
        }
        return false;
    }

    /** 子步骤4/5 匹配：返回{交易类型}包含于$多交易类型$、{摘要码}、{产品类型}同时匹配的第一条明细，未命中返回 null */
    private RbRestraintControlDetailsEO findExemptionMatch(List<RbRestraintControlDetailsEO> details, ST002InputBO input) {
        if (details == null || details.isEmpty()) {
            return null;
        }
        for (RbRestraintControlDetailsEO detail : details) {
            if (matches(detail, input)) {
                return detail;
            }
        }
        return null;
    }

    /** SPEC 未定义$多交易类型$的多值分隔符格式，“{交易类型}包含于$多交易类型$”按字符串包含实现 */
    private boolean matches(RbRestraintControlDetailsEO detail, ST002InputBO input) {
        String tranTypeLink = detail.getTranTypeLink();
        if (tranTypeLink == null || !tranTypeLink.contains(input.getTranType().getValue())) {
            return false;
        }
        if (!Objects.equals(detail.getNarrativeCode(), input.getNarrativeCode())) {
            return false;
        }
        return Objects.equals(detail.getProdNo(), input.getProdType());
    }

    /** 命中后回显匹配明细的状态、产品编号、多交易类型、渠道集合、摘要码、限制机构范围和明细柜面标志 */
    private void echoMatchedDetail(RbRestraintControlDetailsEO matched, ST002OutputBO output) {
        output.setStatus(matched.getStatus());
        output.setProdNo(matched.getProdNo());
        output.setTranTypeLink(matched.getTranTypeLink());
        output.setChannelMuster(matched.getChannelMuster());
        output.setNarrativeCode(matched.getNarrativeCode());
        output.setResBranchRange(matched.getResBranchRange());
        output.setDetailCounterFlag(matched.getCounterFlag());
    }
}

package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST002InputBO;
import com.dcits.validation.facade.bo.ST002OutputBO;

/**
 * ST002 检查限制豁免 步骤接口。
 *
 * <p>根据渠道柜面标志与限制控制明细中的柜面标志选择柜面/非柜面渠道豁免检查，
 * 按{交易类型}包含于$多交易类型$、{摘要码}、{产品类型}同时匹配返回检查结果
 * （不检查限制/需检查限制/豁免/不豁免）。</p>
 *
 * <p>本步骤仅做本地只读查询，无数据库写入，无事务要求；
 * 无业务失败场景，失败仅由技术异常传播表达。</p>
 */
public interface IST002 {

    /** 执行检查限制豁免步骤 */
    ST002OutputBO execute(ST002InputBO input);
}

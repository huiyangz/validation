package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST011InputBO;
import com.dcits.validation.facade.bo.ST011OutputBO;

/** ST011 检查限制优先级 步骤接口 */
public interface IST011 {
    /**
     * 执行检查限制优先级步骤：根据交易类型取交易限制级别、账户限制类型取限制冻结级别，
     * 数值比较后返回检查结果“不检查限制”或“继续检查”。
     *
     * @param input 步骤输入，tranType、restraintType 必填
     * @return 检查结果，本步骤无业务失败场景，失败仅由技术异常传播表达
     */
    ST011OutputBO execute(ST011InputBO input);
}

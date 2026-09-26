package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST001InputBO;
import com.dcits.validation.facade.bo.ST001OutputBO;

/**
 * ST001 检查客户是否存在限制
 * <p>根据上送客户号查询【客户限制表】中限制状态等于"A-生效"的全部客户限制信息并返回。</p>
 * <p>本步骤仅做本地只读查询，无数据库写入，无独立事务要求；
 * 无业务失败场景，失败仅由技术异常传播表达。</p>
 */
public interface IST001 {

    /**
     * 执行 ST001 检查客户是否存在限制
     *
     * @param input 输入BO，clientNo（客户号）必填
     * @return 输出BO，restraints 为限制状态"A-生效"的全部客户限制信息，无生效限制时为空集合
     */
    ST001OutputBO execute(ST001InputBO input);
}

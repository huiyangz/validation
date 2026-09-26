package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST007InputBO;
import com.dcits.validation.facade.bo.ST007OutputBO;

/**
 * ST007 检查质押类限制 步骤接口
 *
 * <p>查询账号全部生效限制记录并按顺序在限制类型表中取首个状态为A-生效且非空的质押标志，
 * 命中即停止遍历并输出该记录的限制编号、账户限制类型、限制状态与质押标志、状态；
 * 遍历结束仍无命中时输出字段均为空，步骤以成功状态返回。</p>
 *
 * <p>本步骤只读本地数据，无本地数据库新增、更新或删除，无事务要求。
 * 无业务失败场景，失败仅由技术异常传播表达。</p>
 */
public interface IST007 {

    /**
     * 执行 ST007 检查质押类限制 步骤
     *
     * @param input 步骤输入，baseAcctNo 必填
     * @return 步骤输出，含命中的限制记录与质押标志信息；无命中时业务字段均为空且 succeed=true
     */
    ST007OutputBO execute(ST007InputBO input);
}

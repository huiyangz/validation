package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST010InputBO;
import com.dcits.validation.facade.bo.ST010OutputBO;

/** ST010 检查是否存在转账不收不付限制 */
public interface IST010 {
    /**
     * 根据账号查询生效的账户限制信息，逐条核对限制类型表中状态A-生效的借贷方控制标志与转账标志，
     * 判断是否存在转账不收不付限制。
     * 本步骤为只读查询，无数据库写入，无事务要求；无业务失败场景，失败仅由技术异常传播表达。
     *
     * @param input 输入BO，baseAcctNo 必填
     * @return 转账不收不付标志及命中的账户限制信息、限制类型信息
     */
    ST010OutputBO execute(ST010InputBO input);
}

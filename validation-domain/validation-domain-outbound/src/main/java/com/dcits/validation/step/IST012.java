package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST012InputBO;
import com.dcits.validation.facade.bo.ST012OutputBO;

/**
 * ST012 检查是否存在不收不付限制 步骤接口
 *
 * <p>只读查询步骤：根据账号查询生效的账户限制记录，逐条核对限制类型表中
 * A-生效记录的借贷方控制标志是否为 A-禁止借贷方。不涉及本地数据库写入，
 * 无事务要求；失败仅由技术异常传播表达，无业务失败场景。</p>
 */
public interface IST012 {

    /**
     * 执行检查是否存在不收不付限制
     *
     * @param input 输入BO，账号必填
     * @return 输出BO，含不收不付标志；命中时含命中限制记录及限制类型记录信息
     */
    ST012OutputBO execute(ST012InputBO input);
}

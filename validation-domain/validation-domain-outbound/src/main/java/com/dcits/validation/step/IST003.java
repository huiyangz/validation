package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST003InputBO;
import com.dcits.validation.facade.bo.ST003OutputBO;

/**
 * ST003 检查有权机关冻结限制 步骤接口。
 * 本步骤为只读查询，不涉及本地数据库写入，无事务要求。
 */
public interface IST003 {

    /** 执行 ST003 检查有权机关冻结限制步骤，返回有权机关冻结标志 */
    ST003OutputBO execute(ST003InputBO input);
}

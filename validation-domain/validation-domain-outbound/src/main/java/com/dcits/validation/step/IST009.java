package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST009InputBO;
import com.dcits.validation.facade.bo.ST009OutputBO;

/** ST009 检查是否存在止付限制 步骤接口 */
public interface IST009 {
    /**
     * 根据账号查询生效的账户限制信息及其限制类型的借贷方控制标志，
     * 判定止付标志为"是"（借贷方控制标志等于 D-禁止借方）或"否"。
     * 本步骤仅做只读查询，无本地数据库写入，无事务要求。
     */
    ST009OutputBO execute(ST009InputBO input);
}

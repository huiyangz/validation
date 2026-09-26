package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST014InputBO;
import com.dcits.validation.facade.bo.ST014OutputBO;

/**
 * ST014 检查是否存在现金不收不付限制 步骤接口。
 * <p>本步骤仅查询本地表 RB_BUS_RESTRAINTS、RB_RESTRAINT_TYPE，无数据库写入，无事务要求。</p>
 */
public interface IST014 {

    /**
     * 根据账号查询生效账户限制及其限制类型，判定是否存在现金不收不付限制。
     *
     * @param input 输入BO，账号必填
     * @return 现金不收不付限制标志及命中记录的回显字段；本步骤无业务失败场景
     */
    ST014OutputBO execute(ST014InputBO input);
}

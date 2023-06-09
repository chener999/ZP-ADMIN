package xyz.chener.zp.common.config.query.annotation;

import java.lang.annotation.*;

/**
 * @Author: chenzp
 * @Date: 2023/02/09/11:19
 * @Email: chen@chener.xyz
 */

/**
 * 用于指定查询的表名
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface QueryTableName {
    String value();
}

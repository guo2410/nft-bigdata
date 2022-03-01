package com.tj.bigdata.analysis.ibatis;

/**
 *  枚举接口
 *
 * @author guoch
 */
public interface EnumType<T> {
    /**
     * 枚举值,用于Mybatis
     *
     * @return 枚举值, 用于Mybatis
     */
    T getValue();
}

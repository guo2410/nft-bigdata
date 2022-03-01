package com.tj.bigdata.analysis.ibatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  处理数据枚举对象
 *
 * @author guoch
 * @param <E>
 * @param <T>
 */
public class EnumTypeHandler<E extends Enum<E> & EnumType<T>, T> extends BaseTypeHandler<E> {
    private final Class<E> type;
    E[] enums;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.type = type;
        }
        enums = this.type.getEnumConstants();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return !rs.wasNull() ? this.convert(rs.getObject(columnName)) : null;
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return !rs.wasNull() ? this.convert(rs.getObject(columnIndex)) : null;
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return !cs.wasNull() ? this.convert(cs.getObject(columnIndex)) : null;
    }

    private E convert(Object value) {
        for (E e : enums) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown enum base type: " + value + ", please check: " + this.type.getSimpleName());
    }
}


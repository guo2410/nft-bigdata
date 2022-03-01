package com.tj.bigdata.analysis.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

/**
 * 环境
 * 工厂
 *
 * @author guoch
 */
@Component
public class MapDataSourceFactory {


    private CurrentPoints currentPoints;

    private LastDayPoints lastDayPoints;

    @Autowired
    public void setLastDayPoints(LastDayPoints lastDayPoints) {
        this.lastDayPoints = lastDayPoints;
    }

    @Autowired
    public void setCurrentPoints(CurrentPoints currentPoints) {
        this.currentPoints = currentPoints;
    }

    public DataSource getDataSource(Integer type) {
        switch (type) {
            case 1:
                return currentPoints;
            case 2:
                return lastDayPoints;
            default:
                throw new InvalidParameterException("24小时数据类型不合法");
        }
    }
}

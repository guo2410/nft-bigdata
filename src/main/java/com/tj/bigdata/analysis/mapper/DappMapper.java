package com.tj.bigdata.analysis.mapper;

import com.tj.bigdata.analysis.pojo.DappPo;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author guoch
 */
@Mapper
public interface DappMapper extends BaseMapper<DappPo>, MySqlMapper<DappPo>, ExampleMapper<DappPo>, SpecialBatchMapper<DappPo>{

    /**
     * 查看合约是否存在
     * @param contract 合约
     * @return 数量
     */
    Integer isExist(String contract);
}

package com.tj.bigdata.analysis.entity;

import com.tj.bigdata.analysis.pojo.DappPo;

import java.util.List;

/**
 * @author guoch
 */
public class OnlineDapps {

    private String Status;

    private List<DappPo>  Result ;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public List<DappPo> getResult() {
        return Result;
    }

    public void setResult(List<DappPo> Result) {
        this.Result = Result;
    }
}

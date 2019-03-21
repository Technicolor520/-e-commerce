package com.pyg.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/9 17:03
 * @description 创建封装分页查询结果的类
 **/


public class PageResult implements Serializable {

    //总页数
    private Long total;

    //查询结果
    private List rows;

    public PageResult(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}

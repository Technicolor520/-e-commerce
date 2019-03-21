package com.pyg.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/11 19:37
 * @description TODO
 **/


public class Specification implements Serializable {

    private TbSpecification tbSpecification;

    private List<TbSpecificationOption> tbSpecificationOptionList;

    public TbSpecification getTbSpecification() {
        return tbSpecification;
    }

    public void setTbSpecification(TbSpecification tbSpecification) {
        this.tbSpecification = tbSpecification;
    }

    public List<TbSpecificationOption> getTbSpecificationOptionList() {
        return tbSpecificationOptionList;
    }

    public void setTbSpecificationOptionList(List<TbSpecificationOption> tbSpecificationOptionList) {
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }
}

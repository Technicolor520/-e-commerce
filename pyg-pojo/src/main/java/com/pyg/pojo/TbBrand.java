package com.pyg.pojo;

import java.io.Serializable;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/7 21:24
 * @description TODO
 **/


public class TbBrand implements Serializable {

    private Long id;

    private String name;

    private String firstChar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }
}

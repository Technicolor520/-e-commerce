package com.pyg.search;

import java.util.Map;


/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/24 9:22
 * @description TODO
 **/


public interface SearchService {

    /**
     * 已进入页面就初始化方法：根据关键字查询
     * @param paramMap
     * @return
     */
    Map searchByParamMap(Map paramMap);

}

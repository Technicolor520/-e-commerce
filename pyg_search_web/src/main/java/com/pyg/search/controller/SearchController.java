package com.pyg.search.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.search.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/24 9:01
 * @description TODO
 **/

@RestController
@RequestMapping("/search")
public class SearchController {

    @Reference
    private SearchService searchService;

    /**
     * 已进入页面就初始化方法：根据关键字查询
     * @param paramMap
     * @return
     */
    @RequestMapping("/searchByParamMap")
    public Map searchByParamMap(@RequestBody Map paramMap){

      return searchService.searchByParamMap(paramMap);
    }
}

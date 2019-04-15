package com.pyg.portal.controller;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/21 22:08
 * @description TODO
 **/

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.content.service.ContentService;
import com.pyg.pojo.TbContent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/index")
public class indexController {

    @Reference
    private ContentService contentService;

    /**
     * 显示轮播图
     * @param categoryId
     * @return
     */
    @RequestMapping("/findByCategotyId/{categoryId}")
    public List<TbContent>findByCategotyId(@PathVariable("categoryId") Long categoryId){
            return contentService.findByCategotyId(categoryId);


    }

}

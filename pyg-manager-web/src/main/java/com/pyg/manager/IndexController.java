package com.pyg.manager;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/15 9:47
 * @description 处理indexController业务
 **/

@RestController
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("/showName")
    public String showName(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
    }

}

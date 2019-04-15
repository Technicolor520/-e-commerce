package com.pyg.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.bean.ResultMessage;
import com.pyg.pojo.TbUser;
import com.pyg.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/31 11:21
 * @description TODO
 **/

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @RequestMapping("/sendCode/{phone}")
    public ResultMessage sendCode(@PathVariable("phone") String phone) {
        try {
            userService.sendCode(phone);
            return new ResultMessage(true, null);
        }catch (RuntimeException e){
            return  new ResultMessage(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "发送失败");
        }

    }

    /*注册*/
    @RequestMapping("/register/{code}")
    public ResultMessage register(@PathVariable("code") String code, @RequestBody TbUser user) {
        try {
            userService.register(code,user);
            return new ResultMessage(true, null);
        } catch (RuntimeException e){
            e.printStackTrace();
            return new ResultMessage(false,e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "注册失败");
        }

    }

    @RequestMapping("/showName")
    public String showName(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        return username;
    }

}

package com.pyg.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.order.service.AddressService;
import com.pyg.pojo.TbAddress;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/9 21:37
 * @description 处理订单地址
 **/

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    @RequestMapping("/findAddressListByUserId")
    public List<TbAddress> findAddressListByUserId(){
        String userId=SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.findAddressListByUserId(userId);
    }

}

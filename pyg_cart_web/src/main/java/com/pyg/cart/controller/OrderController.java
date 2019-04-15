package com.pyg.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.bean.ResultMessage;
import com.pyg.order.service.OrderService;
import com.pyg.pojo.TbOrder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/10 16:42
 * @description 根据购物车的数据生成订单
 **/

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;



    @RequestMapping("/add")
    public ResultMessage add(@RequestBody TbOrder order){

        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            order.setUserId(userId);
            String out_trade_no=orderService.add(order);//下单成功后把交易后的订单号返回给页面
            return new ResultMessage(true,out_trade_no);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false,"保存订单失败");
        }
    }
}

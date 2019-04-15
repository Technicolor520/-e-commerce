package com.pyg.order.service.impl;



import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.groupEntity.Cart;
import com.pyg.mapper.TbOrderItemMapper;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.mapper.TbPayLogMapper;
import com.pyg.order.service.OrderService;
import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbOrderItem;
import com.pyg.pojo.TbPayLog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import utils.IdWorker;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/10 16:54
 * @description 根据购物车的数据生成订单
 **/

@Service
@Transactional
public class OrderServiceimpl implements OrderService {


    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbPayLogMapper payLogMapper;

    @Override
    public String add(TbOrder order) {//order用来传递参数的

        String userId = order.getUserId();
        //从购物车中获取订单数据
        List<Cart> cartList = (List<Cart>) redisTemplate.boundValueOps("cartListFromRedisBy"+userId).get();
        Long totalFee=0L;
        String orderList="";
        for (Cart cart : cartList) {
            TbOrder tbOrder = new TbOrder();

            //获取前端存在redis中的购物车数据
//            tbOrder.setPaymentType(order.getPaymentType());
//            tbOrder.setReceiverAreaName(order.getReceiverAreaName());
//            tbOrder.setReceiver(order.getReceiver());
//            tbOrder.setReceiverMobile(order.getReceiverMobile());
//            tbOrder.setSourceType(order.getSourceType());
            BeanUtils.copyProperties(order,tbOrder);//tbOrder 就有了6个属性值

            //用idWork获取订单id
            long orderId = idWorker.nextId();
            tbOrder.setOrderId(orderId);

//           `order_id` bigint(20) NOT NULL COMMENT '订单id',
//  `payment` decimal(20,2) DEFAULT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
//  `status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价',
//  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
//  `update_time` datetime DEFAULT NULL COMMENT '订单更新时间',
//  `user_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户id',
//  `seller_id` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '商家ID',


        tbOrder.setSellerId(cart.getSellerId());
        tbOrder.setStatus("1");
        tbOrder.setCreateTime(new Date());
        tbOrder.setUpdateTime(new Date());
        tbOrder.setUserId(userId);

            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            BigDecimal payment = new BigDecimal("0");
            for (TbOrderItem orderItem : orderItemList) {
                 payment = payment.add(orderItem.getTotalFee());

                 orderItem.setId(idWorker.nextId());
                 orderItem.setOrderId(orderId);
                 orderItemMapper.insert(orderItem);
            }
            tbOrder.setPayment(payment);
            orderMapper.insert(tbOrder);
            totalFee+=payment.longValue();
            orderList+=orderId+",";
        }

//   `out_trade_no` varchar(30) NOT NULL COMMENT '支付订单号',
//  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
//  `pay_time` datetime DEFAULT NULL COMMENT '支付完成时间',
//  `total_fee` bigint(20) DEFAULT NULL COMMENT '支付金额（分）',
//  `user_id` varchar(50) DEFAULT NULL COMMENT '用户ID',
//  `transaction_id` varchar(30) DEFAULT NULL COMMENT '交易号码',
//  `trade_state` varchar(1) DEFAULT NULL COMMENT '交易状态',
//  `order_list` varchar(200) DEFAULT NULL COMMENT '订单编号列表',
//  `pay_type` varchar(1) DEFAULT NULL COMMENT '支付类型',

        TbPayLog payLog = new TbPayLog();
        payLog.setTradeState("0");//支付状态，  0：未支付，1支付
        payLog.setUserId(userId);//用户id
//        payLog.setPayTime();//TODO 支付时间
//        payLog.setTransactionId();//TODO 支付流水号
        payLog.setTotalFee(totalFee);//支付金额（分）
        payLog.setCreateTime(new Date());//创建时间
        payLog.setOrderList(orderList.substring(0,orderList.length()-1));//订单
        payLog.setOutTradeNo(idWorker.nextId()+"");//主键 IDWoeker中获取
        payLog.setPayType(order.getPaymentType());//支付方式
        payLogMapper.insert(payLog);

        redisTemplate.boundHashOps("payLog"+userId).put(payLog.getOutTradeNo(),payLog);
        //清空购物车
        redisTemplate.delete("cartListFromRedisBy"+userId);
        return payLog.getOutTradeNo();
    }

}

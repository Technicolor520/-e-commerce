package com.pyg.order.service;

import com.pyg.pojo.TbOrder; /**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/10 16:53
 * @description 根据购物车的数据生成订单
 **/


public interface OrderService {


    String add(TbOrder order);
}

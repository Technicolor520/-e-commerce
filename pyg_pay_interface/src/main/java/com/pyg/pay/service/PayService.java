package com.pyg.pay.service;

import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/10 23:01
 * @description TODO
 **/


public interface PayService {


    /**
     * 生成二维码
     * @param out_trade_no
     * @param userId
     * @return
     */
    Map createNative(String out_trade_no, String userId);

    /**
     * 查询订单
     * @param out_trade_no
     * @return
     */
    Map orderQuery(String out_trade_no);

    /**
     * 修改订单状态
     * @param out_trade_no
     * @param transaction_id
     * @param userId
     * @return
     */
    void updateOrder(String out_trade_no, String transaction_id, String userId);
}

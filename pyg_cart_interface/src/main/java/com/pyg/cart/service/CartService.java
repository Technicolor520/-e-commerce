package com.pyg.cart.service;

import com.pyg.groupEntity.Cart;

import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/3 10:39
 * @description 购物车服务接口
 **/


public interface CartService {


    /**
     * 获取购物车数据
     * @param uuid
     * @return
     */
    List<Cart> findCartListFromRedisByKey(String uuid);

    /**
     * 将商品添加到购物车中
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    List<Cart> saveGoodsToCartList(List<Cart> cartList, Long itemId, int num);

    /**
     * 将购物车中的数据添加到redis中
     * @param cartList
     * @param sessionID
     */
    void saveCartListToRedis(List<Cart> cartList, String sessionID,boolean isUserId);

    /**
     *将浏览器中的购物车数据添加到redis购物车中的数据
     * @param cartListFromRedisBySessionID
     * @param cartListFromRedisByUserId
     * @return
     */
    List<Cart> mergeCartList(List<Cart> cartListFromRedisBySessionID, List<Cart> cartListFromRedisByUserId);

    /**
     * 将redis中购物车中的数据删除
     * @param sessionID
     */
    void deleteCartListByKey(String sessionID);
}

package com.pyg.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.cart.service.CartService;
import com.pyg.groupEntity.Cart;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/3 10:40
 * @description 购物车服务实现类
 **/

@Service
public class CartServiceimpl implements CartService {


//1.根据商品SKU ID查询SKU商品信息
    //2.获取商家ID
    //3.根据商家ID判断购物车列表中是否存在该商家的购物车
    //4.如果购物车列表中不存在该商家的购物车
    //4.1 新建购物车对象
    //4.2 将新建的购物车对象添加到购物车列表
    //5.如果购物车列表中存在该商家的购物车
    // 查询购物车明细列表中是否存在该商品
    //5.1. 如果没有，新增购物车明细
    //5.2. 如果有，在原购物车明细上添加数量，更改金额


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbItemMapper itemMapper;


    /**
     * 获取购物车数据
     * @param userKey
     * @return
     */
    @Override
    public List<Cart> findCartListFromRedisByKey(String userKey) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundValueOps("cartListFromRedisBy"+userKey).get();
        if(cartList==null){     //如果为空，返回一个空对象
            cartList=new ArrayList<>();
        }
        return cartList;
    }


    /**
     * 添加购物车
     * @param cartList
     * @param itemId
     * @param num
     * @return
     *
     *     addGoodsToCartList?itemId=XX&num=XXX
            1、根据sku中的sellerId从cartList中查询此商家对应的cart对象
            1.1 如果能查询出cart对象（cart!=null）
            1.1.1 还需要判断此sku是否在此cart对象的orderItemList中
            1.1.1.1如果存在当前的orderItem数量累加小计金额重新计算
            1.1.1.2如果不存在当前的orderItem
            创建一个新的orderItem对象,追加到orderItemList中
            1.2 没有查询出cart对象 （cart==null）
            创建一个新的cart对象
            cart.setSellerId
            cart.setSellerName
            创建一个新的orderItemList
            创建一个新的orderItem对象,追加到orderItemList中
            cart.setOrderItemList
            把新创建的cart放入到cartList中
     */

    @Override
    public List<Cart> saveGoodsToCartList(List<Cart> cartList, Long itemId, int num) {

        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        if(tbItem==null){
            throw new RuntimeException("没有该商品");
        }
        String sellerId = tbItem.getSellerId();
//        1、根据sku中的sellerId从cartList中查询此商家对应的cart对象
        Cart cart=findCartFromCartListBySellerId(cartList,sellerId);
//        1.1 如果能查询出cart对象（cart!=null）
        if(cart!=null){
//            1.1.1 还需要判断此sku是否在此cart对象的orderItemList中
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            TbOrderItem orderItem=findOrderIterListFromOrderItemListByItemId(orderItemList,itemId);
//            1.1.1.1如果存在当前的orderItem数量累加小计金额重新计算
            if(orderItem!=null){
                orderItem.setNum(orderItem.getNum()+num);//数量变化
//                orderItem.getPrice()单价
//                multiply乘
//                new BigDecimal(orderItem.getNum()+"") 创建bigDecimal对象
//                数量*单价
                orderItem.setTotalFee(orderItem.getPrice().multiply(new BigDecimal(orderItem.getNum()+"")));
                if(orderItem.getNum()==0){  //考虑减数量的情况
//                    把orderItem从orderItemList中移除
                    orderItemList.remove(orderItem);
//                    判断此购物车对象中是否有商品
                    if(orderItemList.size()==0){
//                        如果一个商家中没有商品了 应该移除此购物车对象
                        cartList.remove(cart);
                        System.out.println(orderItemList.size());
                    }
                }
            }else {
//                               1.1.1.2如果不存在当前的orderItem
//                创建一个新的orderItem对象,追加到orderItemList中
                orderItem = new TbOrderItem();
                orderItem=createOrderItem(orderItem,num,tbItem);
                orderItemList.add(orderItem);
            }
        }else {
//            1.2 没有查询出cart对象 （cart==null）
//            创建一个新的cart对象
            cart=new Cart();
            cart.setSellerId(tbItem.getSellerId());//cart.setSellerId
            cart.setSellerName(tbItem.getSeller());//cart.setSellerName
//            创建一个新的orderItemList
            ArrayList<TbOrderItem> orderItemList = new ArrayList<>();
//            创建一个新的orderItem对象,追加到orderItemList中
            TbOrderItem orderItem=new TbOrderItem();
            orderItem=createOrderItem(orderItem,num,tbItem);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
//                    把新创建的cart放入到cartList中
            cartList.add(cart);
        }
        return cartList;
    }

    private TbOrderItem createOrderItem(TbOrderItem orderItem,int num,TbItem tbItem){

        orderItem.setNum(num);
        orderItem.setPrice(tbItem.getPrice());
        orderItem.setTotalFee(orderItem.getPrice().multiply(new BigDecimal(orderItem.getNum()+"")));
//                orderItem.setId();  // TODO 插入到mysql时考虑id
//               orderItem.setOrderId(); // TODO 插入到mysql时考虑id
        orderItem.setTitle(tbItem.getTitle());
        orderItem.setSellerId(tbItem.getSellerId());
        orderItem.setPicPath(tbItem.getImage());
        orderItem.setItemId(tbItem.getId());
        orderItem.setGoodsId(tbItem.getGoodsId());
        return orderItem;

    }

//        根据itemId从orderItemList中查找orderItem对象(获取购物车数据抽出的方法)
    private TbOrderItem findOrderIterListFromOrderItemListByItemId(List<TbOrderItem> orderItemList,Long itemId){
        for (TbOrderItem orderItem : orderItemList) {
            if(orderItem.getItemId().longValue()==itemId.longValue()){//==有常量池只能判断-127~128
                return orderItem;
            }
        }
        return null;
    }

    //根据sellerId从cartList中查找cart对象(获取购物车数据抽出的方法)
    private Cart findCartFromCartListBySellerId(List<Cart> cartList,String sellerId){
        for (Cart cart : cartList) {
            if(sellerId.equals(cart.getSellerId())){
                return cart;
            }
        }
        return null;
    }

    /**\
     * 将购物车数据存入redis中
     * @param cartList
     * @param userKey
     */
    @Override
    public void saveCartListToRedis(List<Cart> cartList, String userKey,boolean isUserId) {

        //若是userID时效性为3个月
            //若不是userId时效性为2天
        if(isUserId){
            redisTemplate.boundValueOps("cartListFromRedisBy"+userKey).set(cartList,180, TimeUnit.DAYS);
        }else {
            redisTemplate.boundValueOps("cartListFromRedisBy"+userKey).set(cartList,2, TimeUnit.DAYS);
        }

    }

    /**
     * 将浏览器中的购物车数据添加到redis购物车中的数据
     * @param cartListFromRedisBySessionID
     * @param cartListFromRedisByUserId
     * @return
     */
    @Override
    public List<Cart> mergeCartList(List<Cart> cartListFromRedisBySessionID, List<Cart> cartListFromRedisByUserId) {
        for (Cart cart : cartListFromRedisBySessionID) {
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            for (TbOrderItem orderItem : orderItemList) {
                cartListFromRedisByUserId=saveGoodsToCartList(cartListFromRedisByUserId,orderItem.getItemId(),orderItem.getNum());
            }
        }
        return cartListFromRedisByUserId;
    }

    /**
     * 将redis中购物车中的数据删除
     * @param sessionID
     */
    @Override
    public void deleteCartListByKey(String sessionID) {
        redisTemplate.delete("cartListFromRedisBy"+sessionID);
    }
}

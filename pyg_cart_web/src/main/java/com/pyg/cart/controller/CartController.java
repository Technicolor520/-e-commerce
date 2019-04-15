package com.pyg.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.bean.ResultMessage;
import com.pyg.cart.service.CartService;
import com.pyg.groupEntity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.font.TrueTypeFont;
import utils.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/3 10:42
 * @description TODO
 **/

@RestController
@RequestMapping("/cart")
public class CartController {

//（1）、根据请求中的HTTPSession获取SessionID
//（2）、再根据SessionID，从Redis中获取购物车集合数据
//（3）、把新传递过来的商品添加购物车集合中；
//（4）、添加完后再把购物车集合放回Redis中；


    @Reference
    private CartService cartService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private  HttpServletRequest request;


     private  String getSessionID(){
         //从cookie中获取userkey
         String userkey = CookieUtil.getCookieValue(request, "user-key");
         //如果没有 新建后再放入cookie中
         if(userkey==null){
             userkey = UUID.randomUUID().toString();
             CookieUtil.setCookie(request,response,"user-key",userkey,60*60*24*7);
         }
         return userkey;
     }

    /**
     * 获取购物车数据
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        String sessionID = getSessionID();
//        两份购物车数据合并，把合并后的数据保存到redis中，清除cartListFromRedisByUserId
        String userId=SecurityContextHolder.getContext().getAuthentication().getName();
        List<Cart> cartListFromRedisBySessionID= cartService.findCartListFromRedisByKey(sessionID);
        if(userId.equals("anonymousUser")){//未登录
            return cartListFromRedisBySessionID;
        }else {//已登录
            List<Cart> cartListFromRedisByUserId = cartService.findCartListFromRedisByKey(userId);
            if(cartListFromRedisBySessionID.size()>0){//判断浏览器购物车中是否有数据
                //将浏览器中的购物车数据添加到redis购物车中的数据
               cartListFromRedisByUserId= cartService.mergeCartList(cartListFromRedisBySessionID,cartListFromRedisByUserId);
                //删除redis中的购物车数据
                cartService.deleteCartListByKey(sessionID);
                //将新的购物车数据添加到redis中
                cartService.saveCartListToRedis(cartListFromRedisByUserId,userId,true);
            }
            return cartListFromRedisByUserId;
        }
    }




    /**
     * 添加购物车
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping("/addGoodsToCartList")
//    @CrossOrigin(origins = {"http://item.pinyougou.com","http://search.pinyougou.com"})  只允许item.pinyougou.com异步访问此方法
    @CrossOrigin(origins = "*")
    public ResultMessage addGoodsToCartList(Long itemId,int num){
        try {
            List<Cart> cartList = findCartList();//获取原有购物车数据
            cartList=cartService.saveGoodsToCartList(cartList,itemId,num);//向原购物车数据追加新的商品后形成新的购物车数据
            //重新存入到redis中
            String sessionID = getSessionID();
            //未登录时可以从security中获取用户名：anonymousUser
            //判断是否有当前登录人
            String userId= SecurityContextHolder.getContext().getAuthentication().getName();
            if("anonymousUser".equals(userId)){
                cartService.saveCartListToRedis(cartList,sessionID,false);//未登录时将新的购物车数据放入到redis中
            }else {
                cartService.saveCartListToRedis(cartList, userId,true);
            }
            return new ResultMessage(true,"");
        } catch(RuntimeException e){
            return  new ResultMessage(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false,"添加购物车失败");
        }
    }
}

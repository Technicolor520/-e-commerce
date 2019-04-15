package com.pyg.groupEntity;


import com.pyg.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/3 10:28
 * @description TODO
 **/


public class Cart implements Serializable {

    private String  sellerId;

    private String sellerName;

    private List<TbOrderItem> orderItemList;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
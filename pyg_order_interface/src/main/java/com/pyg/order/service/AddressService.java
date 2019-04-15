package com.pyg.order.service;

import com.pyg.pojo.TbAddress;

import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/9 21:45
 * @description TODO
 **/


public interface AddressService {


    List<TbAddress> findAddressListByUserId(String userId);

}

package com.pyg.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.mapper.TbAddressMapper;
import com.pyg.order.service.AddressService;
import com.pyg.pojo.TbAddress;
import com.pyg.pojo.TbAddressExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/9 21:47
 * @description TODO
 **/

@Service
public class AddressServiceimpl implements AddressService {


    @Autowired
    private TbAddressMapper addressMapper;

    @Override
    public List<TbAddress> findAddressListByUserId(String userId) {

        TbAddressExample example = new TbAddressExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return addressMapper.selectByExample(example);
    }
}

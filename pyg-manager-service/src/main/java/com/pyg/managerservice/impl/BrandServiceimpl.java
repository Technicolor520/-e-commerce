package com.pyg.managerservice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.managerinterface.BrandService;
import com.pyg.pojo.TbBrand;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/7 21:43
 * @description TODO
 **/

@Service
public class BrandServiceimpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.findAll();
    }
}

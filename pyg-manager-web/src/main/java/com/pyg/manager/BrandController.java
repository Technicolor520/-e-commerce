package com.pyg.manager;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.managerinterface.BrandService;
import com.pyg.pojo.TbBrand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/7 21:48
 * @description TODO
 **/

@Controller
@RequestMapping("/brand")
public class BrandController {

    @Reference  //远程注入
    private BrandService brandService;

    @RequestMapping("/findAll")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }
}

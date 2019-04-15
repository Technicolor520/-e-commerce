package com.pyg.datainit;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemCatMapper;
import com.pyg.mapper.TbSpecificationMapper;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.mapper.TbTypeTemplateMapper;
import com.pyg.pojo.*;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/26 9:31
 * @description TODO
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext*.xml")
public class RedisManager {


    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;
    //    把所有分类名称对应的品牌和规格都初始化到redis中

    @Test
    public void Redisinit(){
        List<TbItemCat> categoryList=itemCatMapper.selectByExample(null);
        for (TbItemCat tbItemCat : categoryList) {
            TbTypeTemplate tbTypeTemplate = typeTemplateMapper.selectByPrimaryKey(tbItemCat.getTypeId());
            String itemCatName = tbTypeTemplate.getName();
            String brandIds = tbTypeTemplate.getBrandIds();//[{"id":31,"text":"富光"},{"id":33,"text":"希乐"}]
            List<Map> brandList= JSON.parseArray(brandIds,Map.class);
            redisTemplate.boundHashOps("category_brand").put(itemCatName,brandList);

//            2、初始化分类名称和规格列表数据
            String specIds = tbTypeTemplate.getSpecIds();//[{"id":34,"text":"水杯材质",options:[{},{},{}]},{"id":35,"text":"水杯容量"}]
            List<Map> specList=JSON.parseArray(specIds,Map.class);
            for (Map map : specList) {
                TbSpecificationOptionExample example = new TbSpecificationOptionExample();
                example.createCriteria().andSpecIdEqualTo(Long.parseLong(map.get("id").toString()));
                List<TbSpecificationOption> options=specificationOptionMapper.selectByExample(example);
                map.put("options",options);
            }
            redisTemplate.boundHashOps("category_spec").put(itemCatName,specList);
        }

        System.out.println("redis init is ok");

    }

}

package com.pyg.datainit;



import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/23 9:36
 * @description TODO
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext*.xml")
public class SolrManager {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void initSolrData(){

        //        初始化solr数据
//    select i.* from tb_goods g,tb_item i where i.goods_id=g.id and g.is_marketable='1'

        List<TbItem> itemList = itemMapper.selectGrounding();
        for (TbItem tbItem : itemList) {
            String spec = tbItem.getSpec();
            Map<String,String>map= JSON.parseObject(spec,Map.class);
            tbItem.setSpecMap(map);
        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();



    }

}

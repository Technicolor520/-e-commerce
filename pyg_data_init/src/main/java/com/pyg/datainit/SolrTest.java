package com.pyg.datainit;

import com.pyg.pojo.TbBrandExample;
import com.pyg.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/22 23:03
 * @description TODO
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-solr.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;


    @Test
    public void testAdd(){

        TbItem tbItem = new TbItem();
        tbItem.setId(1L);
        tbItem.setTitle("测试title11111");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();
    }

    @Test
    public void testUpadte(){
        TbItem tbItem = new TbItem();
        tbItem.setId(1L);
        tbItem.setTitle("测试2222");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();
    }

    @Test
    public void testQuery(){

        SimpleQuery query= new SimpleQuery("item_title:测试");
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        List<TbItem> content = scoredPage.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem.getTitle());
        }
    }

    @Test
    public void testdele(){

        SimpleQuery query = new SimpleQuery("item_title:测试");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

}

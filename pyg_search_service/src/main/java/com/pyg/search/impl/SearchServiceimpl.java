package com.pyg.search.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.pojo.TbItem;
import com.pyg.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/24 9:29
 * @description TODO
 */

@Service
public class SearchServiceimpl implements SearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Map searchByParamMap(Map paramMap) {


        Map resultMap = new HashMap();
//        paramMap：  {keyword:"小米"}
//        根据关键字查询
//        Query query = new SimpleQuery("item_title:"+paramMap.get("keyword"));
//        Query query = new SimpleQuery(new Criteria("item_title").is(paramMap.get("keyword")));
//        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);

//       ------------------- 分组查询开始-----------------
//        select category from tb_item where title like '%三星%' group by category
        Query groupQuery = new SimpleQuery(new Criteria("item_keywords").is(paramMap.get("keyword")));//相当于mysql的 from tb_item where title like '%三星%'
//        指定要分组的域名
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");//相当于mysql中的  group by category
        groupQuery.setGroupOptions(groupOptions);
        GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(groupQuery, TbItem.class);
        GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");//相当于 mysql中的 select category
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        List<GroupEntry<TbItem>> groupEntryList = groupEntries.getContent();
        List<String> categoryList = new ArrayList<String>();
        for (GroupEntry<TbItem> tbItemGroupEntry : groupEntryList) {
            String groupValue = tbItemGroupEntry.getGroupValue();
            categoryList.add(groupValue);
        }
        resultMap.put("categoryList",categoryList);


//        从第一个分类数据中获取品牌和规格数据
        if(categoryList.size()>0){
            String categoryName = categoryList.get(0);
            List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("category_brand").get(categoryName);
            List<Map> specList = (List<Map>) redisTemplate.boundHashOps("category_spec").get(categoryName);
            resultMap.put("brandList",brandList);
            resultMap.put("specList",specList);
        }

//       ------------------- 分组查询结束-----------------



//        -------------------高亮查询开始-----------------
        HighlightQuery highlightQuery = new SimpleHighlightQuery(new Criteria("item_keywords").is(paramMap.get("keyword")));
//        设置高亮相关的属性
//
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");//        高亮哪个域
        // 使用什么标签包裹关键字
        highlightOptions.setSimplePrefix("<span style=\"color:red\">");
        highlightOptions.setSimplePostfix("</span>");
        highlightQuery.setHighlightOptions(highlightOptions);
//        {"keyword":"小米","category":"手机","brand":"三星","spec":{"网络":"移动3G","机身内存":"16G","手机屏幕尺寸":"5寸"},"price":"500-1000"}
//        在主查询的基础上添加过滤查询
//        分类
        if(!paramMap.get("category").equals("")){
            highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_category").is(paramMap.get("category"))));
        }
//        品牌
        if(!paramMap.get("brand").equals("")){
            highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_brand").is(paramMap.get("brand"))));
        }
//        "spec":{"网络":"移动3G","机身内存":"16G","手机屏幕尺寸":"5寸"}
        Map specMap = (Map) paramMap.get("spec");
        for(Object key:specMap.keySet()){
            highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_spec_"+key).is(specMap.get(key))));
        }
//        规格
//        highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_spec_机身内存").is("16G")));
//        highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_spec_手机屏幕尺寸").is("5寸")));
//        价格区间
//        0-500  500-1000  2000-3000  3000-*
        if(!paramMap.get("price").equals("")){
            String[] prices = paramMap.get("price").toString().split("-");
            if(!prices[1].equals("*")){
                highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_price").between(prices[0],prices[1],true,true)  ));
            }else{
                highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_price").greaterThanEqual(prices[0]) ));
            }

        }

//       按照价格 排序 默认升序
        if(paramMap.get("order").equals("asc")){
            highlightQuery.addSort(new Sort(Sort.Direction.ASC,"item_price"));
        }else{
            highlightQuery.addSort(new Sort(Sort.Direction.DESC,"item_price"));
        }

//        分页 和mysql的分页是一样 limit 起始位置, 没有显示条数
//          起始位置计算的公式：(当前页-1)*每页显示的条数

        Integer pageNo = (Integer) paramMap.get("pageNo");
        highlightQuery.setRows(60);//每页显示的条数
        highlightQuery.setOffset((pageNo-1)*60);// 起始位置

        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(highlightQuery, TbItem.class);
//        自己把highlightPage打印到控制台
        List<TbItem> itemList = highlightPage.getContent();
        for (TbItem tbItem : itemList) {
            List<HighlightEntry.Highlight> highlights = highlightPage.getHighlights(tbItem);
            if(highlights!=null&&highlights.size()>0){
                HighlightEntry.Highlight highlight = highlights.get(0);
                List<String> snipplets = highlight.getSnipplets();
                if(snipplets!=null&&snipplets.size()>0){
                    String title = snipplets.get(0); //有高亮标签包裹的数据
                    tbItem.setTitle(title);
                }
            }
        }
//        -------------------高亮查询结束-----------------
        resultMap.put("total",highlightPage.getTotalElements());//总条数
        resultMap.put("totalPages",highlightPage.getTotalPages()); //总页数
        resultMap.put("itemList",itemList);  //当前页的数据
        return resultMap;
    }
}




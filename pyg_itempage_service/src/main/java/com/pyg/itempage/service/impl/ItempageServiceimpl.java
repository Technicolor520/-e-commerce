package com.pyg.itempage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.groupEntity.Goods;
import com.pyg.itempage.service.ItempageService;
import com.pyg.mapper.TbGoodsDescMapper;
import com.pyg.mapper.TbGoodsMapper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/27 20:44
 * @description TODO
 **/

@Service
public class ItempageServiceimpl implements ItempageService {

    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public Goods findOne(Long goodsId) {

        Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);

        TbItemExample example = new TbItemExample();
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        List<TbItem> itemList=itemMapper.selectByExample(example);

        goods.setTbGoods(tbGoods);
        goods.setTbGoodsDesc(tbGoodsDesc);
        goods.setItemList(itemList);

        return goods;
    }

    @Override
    public List<Goods> findAll() {

        //获取所有的组合类
        List<Goods> goodsList=new ArrayList<>();
        //获取所有的组合类
        //一个组合类对应一个tbGoods
        List<TbGoods> tbGoodsList=goodsMapper.selectByExample(null);
        for (TbGoods tbGoods : tbGoodsList) {
            Goods goods=findOne(tbGoods.getId());
            goodsList.add(goods);
        }
        return goodsList;
    }
}

package com.pyg.managerservice.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pyg.bean.PageResult;
import com.pyg.groupEntity.Goods;
import com.pyg.managerinterface.GoodsService;
import com.pyg.mapper.*;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsExample;
import com.pyg.pojo.TbGoodsExample.Criteria;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private TbBrandMapper brandMapper;

	@Autowired
	private TbSellerMapper sellerMapper;
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		TbGoods tbGoods = goods.getTbGoods();
		tbGoods.setAuditStatus("0");//状态，默认未审核
		tbGoods.setIsMarketable("0");//是否上架：默认未上架
		tbGoods.setIsDelete("0");//是否删除
		goodsMapper.insert(tbGoods);//插入成功后返回主键Id

		TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
		tbGoodsDesc.setGoodsId(tbGoods.getId());
		goodsDescMapper.insert(tbGoodsDesc);

       /* List<TbItem> itemList = goods.getItemList();
        for (TbItem tbItem : itemList) {
			String title = tbGoods.getGoodsName();
			String spec = tbItem.getSpec();
			Map<String,String> specMap = JSON.parseObject(spec, Map.class);
			for(String key:specMap.keySet()){
				title+=" "+specMap.get(key);
			}
			tbItem.setTitle(title);
			//取自spu的 副标题
			tbItem.setSellPoint(tbGoods.getCaption());
			//取spu 的第一张图片地址
			String itemImages =tbGoodsDesc.getItemImages();
			List<Map> imageMapList=JSON.parseArray(itemImages,Map.class);
			if(imageMapList!=null &&imageMapList.size()>0){
				String image=imageMapList.get(0).get("url").toString();
				tbItem.setImage(image);
			}

			//去spu的第三极分类id
			tbItem.setCategoryid(tbGoods.getCategory3Id());
			//商品状态，1-正常，2-下架，3-删除
			tbItem.setStatus("1");
			//创建时间
			tbItem.setCreateTime(new Date());
			//更新时间
			tbItem.setUpdateTime(new Date());
			//goods_id   spuId
			tbItem.setGoodsId(tbGoods.getId());
			//seller_id  从tbGoods中获取
			tbItem.setSellerId(tbGoods.getSellerId());
			//category  分类名称
			tbItem.setCategory(itemCatMapper.selectByPrimaryKey(tbItem.getCategoryid()).getName());
			//brand  品牌名称
			tbItem.setBrand(brandMapper.selectByPrimaryKey(tbGoods.getBrandId()).getName());
			//seller 商家名称
//			tbItem.setSeller(sellerMapper.selectByPrimaryKey(tbGoods.getSellerId()).getName());
			itemMapper.insert(tbItem);
		}*/
		List<TbItem> itemList = goods.getItemList();
		for (TbItem tbItem : itemList) {
			String title = tbGoods.getGoodsName();
			String spec = tbItem.getSpec(); //{"网络":"移动4G","机身内存":"32G"}
			Map<String,String> specMap = JSON.parseObject(spec, Map.class);
			for(String key:specMap.keySet()){
				title+=" "+specMap.get(key);
			}
			tbItem.setTitle(title);
			tbItem.setSellPoint(tbGoods.getCaption());
			String itemImages = tbGoodsDesc.getItemImages(); //[{color:,url:}]
			List<Map> itemImageMapList = JSON.parseArray(itemImages, Map.class);
			if(itemImageMapList.size()>0){
				String url = (String) itemImageMapList.get(0).get("url");
				tbItem.setImage(url);
			}
			tbItem.setCategoryid(tbGoods.getCategory3Id());
			tbItem.setCreateTime(new Date());
			tbItem.setUpdateTime(new Date());
			tbItem.setGoodsId(tbGoods.getId());
			tbItem.setSellerId(tbGoods.getSellerId());
			tbItem.setCategory(itemCatMapper.selectByPrimaryKey(tbItem.getCategoryid()).getName());
			tbItem.setBrand(brandMapper.selectByPrimaryKey(tbGoods.getBrandId()).getName());
			tbItem.setSeller(sellerMapper.selectByPrimaryKey(tbItem.getSellerId()).getName());
			itemMapper.insert(tbItem);
		}


	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id){
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void dele(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}


		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusEqualTo(goods.getAuditStatus());
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void updateAuditStatus(String status, Long[] ids) {
		for (Long id : ids) {
			TbGoods tbGoods=goodsMapper.selectByPrimaryKey(id);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Destination solrItempageUpdate;
	@Autowired
	private Destination solrItempageDelete;

	@Override
	public void updateIsMarketable(Long[] ids, String market) {
//		market: 1、上架  2、下架
		for (Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsMarketable(market);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}

		if(market.equals("1")){//上架
			for (Long id : ids) {
				jmsTemplate.send(solrItempageUpdate, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(id.toString());
					}
				});
			}
		}

		if(market.equals("2")){//下架
			for (Long id : ids) {
				jmsTemplate.send(solrItempageDelete, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(id.toString());
					}
				});
			}
		}
	}
}

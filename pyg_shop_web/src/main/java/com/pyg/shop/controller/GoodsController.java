package com.pyg.shop.controller;
import java.util.List;

import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.groupEntity.Goods;
import com.pyg.managerinterface.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbGoods;


/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int pageNum, int pageSize){
		return goodsService.findPage(pageNum, pageSize);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public ResultMessage add(@RequestBody Goods goods){
		try {

			String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
			goods.getTbGoods().setSellerId(sellerId);
			goodsService.add(goods);
			return new ResultMessage(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public ResultMessage update(@RequestBody TbGoods goods){
		try {
			goodsService.update(goods);
			return new ResultMessage(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne/{id}")
	public TbGoods findOne(@PathVariable("id") Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/dele")
	public ResultMessage delete(Long [] ids){
		try {
			goodsService.dele(ids);
			return new ResultMessage(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goods
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/query")
	public PageResult query(@RequestBody TbGoods goods, int pageNum, int pageSize  ){
		goods.setSellerId( SecurityContextHolder.getContext().getAuthentication().getName());
		return goodsService.findPage(goods, pageNum, pageSize);		
	}

	@RequestMapping("/updateIsMarketable/{ids}/{market}")
	public ResultMessage updateIsMarketable(@PathVariable("ids") Long [] ids, @PathVariable("market") String market){
		try {
			//market:1为上架，market：2下架
			goodsService.updateIsMarketable(ids,market);
			return new ResultMessage(true,"");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "修改失败");
		}
	}
	
}

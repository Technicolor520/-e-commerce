package com.pyg.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.ItemCatService;
import com.pyg.pojo.TbItemCat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

	@Reference
	private ItemCatService itemCatService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbItemCat> findAll(){			
		return itemCatService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int pageNum, int pageSize){			
		return itemCatService.findPage(pageNum, pageSize);
	}
	
	/**
	 * 增加
	 * @param itemCat
	 * @return
	 */
	@RequestMapping("/add")
	public ResultMessage add(@RequestBody TbItemCat itemCat){
		try {
			itemCatService.add(itemCat);
			return new ResultMessage(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param itemCat
	 * @return
	 */
	@RequestMapping("/update")
	public ResultMessage update(@RequestBody TbItemCat itemCat){
		try {
			itemCatService.update(itemCat);
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
	@RequestMapping("/findOne")
	public TbItemCat findOne(Long id){
		return itemCatService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/dele")
	public ResultMessage delete(Long [] ids){
		try {
			itemCatService.dele(ids);
			return new ResultMessage(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param itemCat
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/query")
	public PageResult query(@RequestBody TbItemCat itemCat, int pageNum, int pageSize  ){
		return itemCatService.findPage(itemCat, pageNum, pageSize);
	}

	/**
	 * 根据parentId查询分类数据
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/findByParentId")
	public List<TbItemCat> findByParentId( Long parentId){
		return itemCatService.findByParentId(parentId);
	}

	
}

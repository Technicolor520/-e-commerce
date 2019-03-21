package com.pyg.shop.controller;
import java.util.List;

import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.ItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbItem;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/item")
public class ItemController {

	@Reference
	private ItemService itemService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbItem> findAll(){			
		return itemService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int pageNum, int pageSize){
		return itemService.findPage(pageNum, pageSize);
	}
	
	/**
	 * 增加
	 * @param item
	 * @return
	 */
	@RequestMapping("/add")
	public ResultMessage add(@RequestBody TbItem item){
		try {
			itemService.add(item);
			return new ResultMessage(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param item
	 * @return
	 */
	@RequestMapping("/update")
	public ResultMessage update(@RequestBody TbItem item){
		try {
			itemService.update(item);
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
	public TbItem findOne(Long id){
		return itemService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/dele")
	public ResultMessage delete(Long [] ids){
		try {
			itemService.dele(ids);
			return new ResultMessage(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param item
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/query")
	public PageResult query(@RequestBody TbItem item, int pageNum, int pageSize  ){
		return itemService.findPage(item, pageNum, pageSize);		
	}
	
}

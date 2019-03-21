package com.pyg.manager;
import java.util.List;

import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.GoodsService;
import com.pyg.pojo.TbGoods;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;


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
	@RequestMapping("/findOne")
	public TbGoods findOne(Long id){
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
		return goodsService.findPage(goods, pageNum, pageSize);		
	}

	/**
	 * 修改指定商品状态
	 * @param status
	 * @param ids
	 * @return
	 */

	@RequestMapping("/updateAuditStatus/{status}/{ids}")
	public ResultMessage updateAuditStatus(@PathVariable("status")String status,@PathVariable("ids") Long[] ids){
		try {
			goodsService.updateAuditStatus(status,ids);
			return new ResultMessage(true, "修改状态成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "修改状态失败");
		}
	}
	
}

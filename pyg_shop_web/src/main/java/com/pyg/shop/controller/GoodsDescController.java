package com.pyg.shop.controller;
import java.util.List;

import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.GoodsDescService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbGoodsDesc;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goodsDesc")
public class GoodsDescController {

	@Reference
	private GoodsDescService goodsDescService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoodsDesc> findAll(){			
		return goodsDescService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int pageNum, int pageSize){
		return goodsDescService.findPage(pageNum, pageSize);
	}
	
	/**
	 * 增加
	 * @param goodsDesc
	 * @return
	 */
	@RequestMapping("/add")
	public ResultMessage add(@RequestBody TbGoodsDesc goodsDesc){
		try {
			goodsDescService.add(goodsDesc);
			return new ResultMessage(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goodsDesc
	 * @return
	 */
	@RequestMapping("/update")
	public ResultMessage update(@RequestBody TbGoodsDesc goodsDesc){
		try {
			goodsDescService.update(goodsDesc);
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
	public TbGoodsDesc findOne(Long id){
		return goodsDescService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/dele")
	public ResultMessage delete(Long [] ids){
		try {
			goodsDescService.dele(ids);
			return new ResultMessage(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goodsDesc
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoodsDesc goodsDesc, int pageNum, int pageSize  ){
		return goodsDescService.findPage(goodsDesc, pageNum, pageSize);		
	}
	
}

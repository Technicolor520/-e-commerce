package com.pyg.shop.controller;
import java.util.List;

import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbSeller;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeller> findAll(){			
		return sellerService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int pageNum, int pageSize){
		return sellerService.findPage(pageNum, pageSize);
	}
	
	/**
	 * 增加
	 * @param seller
	 * @return
	 */
	@RequestMapping("/add")
	public ResultMessage add(@RequestBody TbSeller seller){
		try {
			sellerService.add(seller);
			return new ResultMessage(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seller
	 * @return
	 */
	@RequestMapping("/update")
	public ResultMessage update(@RequestBody TbSeller seller){
		try {
			sellerService.update(seller);
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
	public TbSeller findOne(String id){
		return sellerService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/dele")
	public ResultMessage delete(String [] ids){
		try {
			sellerService.dele(ids);
			return new ResultMessage(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param seller
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/query")
	public PageResult query( int pageNum, int pageSize,@RequestBody TbSeller seller  ){
		return sellerService.findPage( pageNum, pageSize,seller);
	}
	
}

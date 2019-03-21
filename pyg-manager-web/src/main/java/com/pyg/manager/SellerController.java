package com.pyg.manager;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.SellerService;
import com.pyg.pojo.TbSeller;

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
	/*
	 * 修改审核状态
	 * @param ids
	 * @return
	 */
	@RequestMapping("/updateStatus/{status}/{sellerId}")
	public ResultMessage updateStatus(@PathVariable("status") String status,@PathVariable("sellerId") String sellerId){
		try {
			sellerService.updateStatus(status,sellerId);
			return new ResultMessage(true, "");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMessage(false, "状态修改失败");
		}
	}

		/**
	 * 查询+分页
	 * @param seller
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/query")
	public PageResult query( int pageNo, int pageSize ,@RequestBody TbSeller seller ){
		return sellerService.findPage( pageNo, pageSize,seller);
	}
	
}

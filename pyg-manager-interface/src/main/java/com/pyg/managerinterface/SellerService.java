package com.pyg.managerinterface;
import java.util.List;

import com.pyg.bean.PageResult;
import com.pyg.pojo.TbSeller;


/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SellerService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSeller> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbSeller seller);
	
	
	/**
	 * 修改
	 */
	public void update(TbSeller seller);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbSeller findOne(String id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void dele(String[] ids);

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage( int pageNo, int pageSize,TbSeller seller);

	/**
	 * 修改商家的审核状态
	 * @param status
	 * @param sellerId
	 */
    void updateStatus(String status, String sellerId);
}

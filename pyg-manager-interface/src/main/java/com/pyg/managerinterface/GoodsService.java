package com.pyg.managerinterface;
import java.util.List;

import com.pyg.bean.PageResult;
import com.pyg.groupEntity.Goods;
import com.pyg.pojo.TbGoods;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	public void update(TbGoods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbGoods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void dele(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

	/**
	 * 修改指定商品状态
	 * @param status
	 * @param ids
	 */
	void updateAuditStatus(String status, Long[] ids);

	/**
	 *
	 * @param ids
	 * @param market
	 */
	void updateIsMarketable(Long[] ids, String market);
}

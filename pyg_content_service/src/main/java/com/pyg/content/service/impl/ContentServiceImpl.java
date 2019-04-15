package com.pyg.content.service.impl;
import java.util.List;

import com.pyg.bean.PageResult;
import com.pyg.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbContentMapper;
import com.pyg.pojo.TbContent;
import com.pyg.pojo.TbContentExample;
import com.pyg.pojo.TbContentExample.Criteria;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {

		contentMapper.insert(content);
		redisTemplate.boundHashOps("contentList").delete(content.getCategoryId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){

		//tbContent修改之前的id
		TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
		contentMapper.updateByPrimaryKey(content);
		redisTemplate.boundHashOps("contentList").delete(content.getCategoryId());

		if(tbContent.getCategoryId().longValue()!=content.getCategoryId().longValue()){
			redisTemplate.boundHashOps("contentList").delete(tbContent.getCategoryId());
		}

	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void dele(Long[] ids) {
		for(Long id:ids){
			TbContent content = contentMapper.selectByPrimaryKey(id);
			contentMapper.deleteByPrimaryKey(id);
			redisTemplate.boundHashOps("contentList").delete(content.getCategoryId());
		}		
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


	/**
	 * 显示轮播图
	 * @param categoryId
	 * @return
	 */
	@Override
	public List<TbContent> findByCategotyId(Long categoryId) {

		List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("contentList").get(categoryId);
		if(contentList==null){
			//		select  * from tb_content where category_id=? and  status ="1" order by sort_order description
			TbContentExample example = new TbContentExample();
			example.createCriteria().andStatusEqualTo("1").andCategoryIdEqualTo(categoryId);
			example.setOrderByClause("sort_order desc");
			contentList= contentMapper.selectByExample(example);
			redisTemplate.boundHashOps("contentList").put(categoryId,contentList);
			System.out.println("from MYSQL");
		}else {
			System.out.println("from REDIS");
		}

		return contentList;
	}
}

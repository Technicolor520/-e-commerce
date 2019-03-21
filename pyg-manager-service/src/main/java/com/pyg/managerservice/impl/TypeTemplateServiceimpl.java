package com.pyg.managerservice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.TypeTemplateService;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.mapper.TbTypeTemplateMapper;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.pojo.TbSpecificationOptionExample;
import com.pyg.pojo.TbTypeTemplate;
import com.pyg.pojo.TbTypeTemplateExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/7 21:43
 * @description TODO
 **/

@Service
public class TypeTemplateServiceimpl implements TypeTemplateService {

    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;
    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @Override
    public List<TbTypeTemplate> findAll() {
        return typeTemplateMapper.selectByExample(null);
    }

    /**
     * 分页查询(利用分页插件查询所有品牌)
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize) {

        //调用分页插件的方法
        PageHelper.startPage(pageNo, pageSize);
        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
//        page.getResult() 当前页数据
//        page.getTotal() 总条数
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增品牌
     *
     * @param typeTemplate
     * @return
     */
    @Override
    public ResultMessage add(TbTypeTemplate typeTemplate) {

        try {
            typeTemplateMapper.insert(typeTemplate);
            return new ResultMessage(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "添加失败");
        }
    }

    /**
     * 修改数据回现
     *
     * @param id
     * @return
     */
    @Override
    public TbTypeTemplate findOne(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据id修改品牌
     *
     * @param typeTemplate
     * @return
     */

    @Override
    public ResultMessage update(TbTypeTemplate typeTemplate) {

        try {
            typeTemplateMapper.updateByPrimaryKey(typeTemplate);
            return new ResultMessage(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false, "修改失败");
        }
    }

    /**
     * 根据id删除
     *
     * @param ids
     */

    @Override
    public void dele(Long[] ids) {
        for (Long id : ids) {
            typeTemplateMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 模糊分页查询
     * @param pageNo
     * @param pageSize
     * @param typeTemplate
     * @return
     */
    @Override
    public PageResult queryPage(int pageNo, int pageSize, TbTypeTemplate typeTemplate) {
        PageHelper.startPage(pageNo, pageSize);

        TbTypeTemplateExample example=new TbTypeTemplateExample();
        TbTypeTemplateExample.Criteria criteria = example.createCriteria();

        if(typeTemplate!=null){
            if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
                criteria.andNameLike("%"+typeTemplate.getName()+"%");
            }
            if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
                criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
            }
            if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
                criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
            }
            if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
                criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
            }

        }

        Page<TbTypeTemplate> page= (Page<TbTypeTemplate>)typeTemplateMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 根据模板id返回扩展属性值
     * @param id
     * @return
     */
/*    @Override
    public List<Map> findSpecList(Long id) {
        TbTypeTemplate tbTypeTemplate=typeTemplateMapper.selectByPrimaryKey(id);
        String specIds=tbTypeTemplate.getSpecIds();
        List<Map> specMapList= JSON.parseArray(specIds,Map.class);
        for (Map map : specMapList) {
            map.get("id");
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            example.createCriteria().andSpecIdEqualTo(Long.parseLong(map.get("id").toString()));
            List<TbSpecificationOption> options=specificationOptionMapper.selectByExample(example);
            map.put("options",options);
        }
        return specMapList;
    }*/
    @Override
    public List<Map> findSpecList(Long id) {
        TbTypeTemplate tbTypeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        String specIds = tbTypeTemplate.getSpecIds(); //[{"id":27,"text":"网络"},{ "id":32,"text":"机身内存"}]
        List<Map> specMapList = JSON.parseArray(specIds, Map.class);
        for (Map map : specMapList) {
//			select * from tb_specification_option where spec_id=?
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            example.createCriteria().andSpecIdEqualTo(Long.parseLong(map.get("id").toString()) );
            List<TbSpecificationOption> options = specificationOptionMapper.selectByExample(example);
            map.put("options",options); //向map中追加新属性
        }
        return specMapList;
    }



}


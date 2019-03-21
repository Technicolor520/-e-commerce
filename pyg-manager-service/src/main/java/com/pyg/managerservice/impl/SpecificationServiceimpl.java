package com.pyg.managerservice.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.SpecificationService;
import com.pyg.mapper.TbSpecificationMapper;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.pojo.*;
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
public class SpecificationServiceimpl implements SpecificationService {

    @Autowired
    private TbSpecificationMapper specificationMapper;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @Override
    public List<TbSpecification> findAll() {
        return specificationMapper.selectByExample(null);
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
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
//        page.getResult() 当前页数据
//        page.getTotal() 总条数
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增品牌
     *
     * @param specification
     * @return
     */
    @Override
    public void add(Specification specification) {
//        规格管理{"tbSpecificationOptionList":[{"optionName":"玻璃","orders":"1"},
//            {"optionName":"陶瓷","orders":"2"}],"tbSpecification":{"specName":"水杯"}}
        TbSpecification tbSpecification = specification.getTbSpecification();
        specificationMapper.insert(tbSpecification);

        List<TbSpecificationOption> tbSpecificationOptionList = specification.getTbSpecificationOptionList();
        for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptionList) {
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            System.out.println(tbSpecification.getId());
            specificationOptionMapper.insert(tbSpecificationOption);
        }
    }

    /**
     * 修改数据回现
     *
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {

        Specification specification = new Specification();
        TbSpecification tbSpecification=specificationMapper.selectByPrimaryKey(id);
        specification.setTbSpecification(tbSpecification);

        TbSpecificationOptionExample exmple = new TbSpecificationOptionExample();
        exmple.createCriteria().andSpecIdEqualTo(id);
        List<TbSpecificationOption> tbSpecificationOptionList=  specificationOptionMapper.selectByExample(exmple);
        specification.setTbSpecificationOptionList(tbSpecificationOptionList);
        return specification;

    }

    /**
     * 根据id修改品牌
     *
     * @param specification
     * @return
     */

    @Override
    public void update(Specification specification) {
        TbSpecification tbSpecification = specification.getTbSpecification();
        specificationMapper.updateByPrimaryKey(tbSpecification);

        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        example.createCriteria().andSpecIdEqualTo(tbSpecification.getId());
        specificationOptionMapper.deleteByExample(example);

        List<TbSpecificationOption> tbSpecificationOptionList=specification.getTbSpecificationOptionList();
        for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptionList) {
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            specificationOptionMapper.insert(tbSpecificationOption);
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
            specificationMapper.deleteByPrimaryKey(id);
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            example.createCriteria().andSpecIdEqualTo(id);
            specificationOptionMapper.deleteByExample(example);

        }
    }

    /**
     * 模糊分页查询
     * @param pageNo
     * @param pageSize
     * @param specification
     * @return
     */
    @Override
    public PageResult queryPage(Integer pageNo, Integer pageSize, TbSpecification specification) {
        PageHelper.startPage(pageNo, pageSize);
        TbSpecificationExample example = new TbSpecificationExample();
        TbSpecificationExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(specification.getSpecName())){
            criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
        }
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
//        page.getResult() 当前页数据
//        page.getTotal() 总条数
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 此方法是添加模板时要求的
     * @return
     */

    @Override
    public List<Map> findSpecList() {
        return specificationMapper.findSpecList();
    }


}


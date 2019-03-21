package com.pyg.managerservice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.managerinterface.BrandService;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbBrandExample;
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
public class BrandServiceimpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
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
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
//        page.getResult() 当前页数据
//        page.getTotal() 总条数
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增品牌
     *
     * @param brand
     * @return
     */
    @Override
    public ResultMessage add(TbBrand brand) {

        try {
            brandMapper.insert(brand);
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
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据id修改品牌
     *
     * @param brand
     * @return
     */

    @Override
    public ResultMessage update(TbBrand brand) {

        try {
            brandMapper.updateByPrimaryKey(brand);
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
            brandMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 模糊分页查询
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */
    @Override
    public PageResult queryPage(Integer pageNo, Integer pageSize, TbBrand brand) {
        PageHelper.startPage(pageNo, pageSize);
//        Page<TbBrand> page = (Page<TbBrand>) brandMapper.query(brand);
//        page.getResult() 当前页数据
//        page.getTotal() 总条数
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();
        if(com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(brand.getName())){
            criteria.andNameLike("%"+brand.getName()+"%");
        }
        if(com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(brand.getFirstChar())){
            criteria.andFirstCharEqualTo(brand.getFirstChar());
        }
        Page<TbBrand> page= (Page<TbBrand>) brandMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }
    /**
     * 此方法是添加模板时要求的，数据格式为[{"id":1,"text":"联想"},{"id":2,"text":"华为"}]
     * @return
     */
    @Override
    public List<Map> findBrandList() {
        return brandMapper.findBrandList();
    }
}


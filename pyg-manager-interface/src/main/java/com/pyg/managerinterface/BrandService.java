package com.pyg.managerinterface;



import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.pojo.TbBrand;

import java.util.List;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/7 21:39
 * @description
 **/


public interface BrandService {

    /**
     * 查询所有品牌
     * @return
     */
    List<TbBrand> findAll();

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageResult findPage(Integer pageNo, Integer pageSize);

    /**
     * 新增品牌
     * @param brand
     * @return
     */
    ResultMessage add(TbBrand brand);

    /**
     * 修改数据回现
     * @param id
     * @return
     */
    TbBrand findOne(Long id);

    /**
     * 根据id修改品牌
     * @param brand
     * @return
     */
    ResultMessage update(TbBrand brand);

    /**
     * 根据id删除
     * @param ids
     * @return
     */
    void dele(Long[] ids);

    /**
     * 模糊分页查询
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */

    PageResult queryPage(Integer pageNo, Integer pageSize, TbBrand brand);


    /**
     * 此方法是添加模板时要求的，数据格式为[{"id":1,"text":"联想"},{"id":2,"text":"华为"}]
     * @return
     */
    List<Map> findBrandList();

}

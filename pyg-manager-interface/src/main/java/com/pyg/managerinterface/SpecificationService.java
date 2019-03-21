package com.pyg.managerinterface;



import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.pojo.Specification;
import com.pyg.pojo.TbSpecification;

import java.util.List;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/7 21:39
 * @description TODO
 **/


public interface SpecificationService {

    /**
     * 查询所有品牌
     * @return
     */
    List<TbSpecification> findAll();

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageResult findPage(Integer pageNo, Integer pageSize);

    /**
     * 新增品牌
     * @param specification
     * @return
     */
    void add(Specification specification);

    /**
     * 修改数据回现
     * @param id
     * @return
     */
    Specification findOne(Long id);

    /**
     * 根据id修改品牌
     * @param specification
     * @return
     */
    void update(Specification specification);

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
     * @param specification
     * @return
     */

    PageResult queryPage(Integer pageNo, Integer pageSize, TbSpecification specification);


    /**
     * 此方法是添加模板时要求的
     * @return
     */
    List<Map> findSpecList();

}

package com.pyg.managerinterface;



import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.pojo.TbTypeTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/7 21:39
 * @description TODO
 **/


public interface TypeTemplateService {

    /**
     * 查询所有品牌
     * @return
     */
    List<TbTypeTemplate> findAll();

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageResult findPage(Integer pageNo, Integer pageSize);

    /**
     * 新增品牌
     * @param typeTemplate
     * @return
     */
    ResultMessage add(TbTypeTemplate typeTemplate);

    /**
     * 修改数据回现
     * @param id
     * @return
     */
    TbTypeTemplate findOne(Long id);

    /**
     * 根据id修改品牌
     * @param typeTemplate
     * @return
     */
    ResultMessage update(TbTypeTemplate typeTemplate);

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
     * @param typeTemplate
     * @return
     */

    PageResult queryPage(int pageNo, int pageSize, TbTypeTemplate typeTemplate);

    /**
     * 根据模板id返回扩展属性值
     * @param id
     * @return
     */
    List<Map> findSpecList(Long id);
}

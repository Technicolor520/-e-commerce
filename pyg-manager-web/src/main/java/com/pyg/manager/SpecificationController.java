package com.pyg.manager;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.SpecificationService;
import com.pyg.pojo.Specification;
import com.pyg.pojo.TbSpecification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/7 21:48
 * @description 实现品牌管理的Controller
 **/

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference  //远程注入
    private SpecificationService specificationService;



    /**
     * 查询所有品牌
     * @return
     */
    @RequestMapping("/findAll")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public List<TbSpecification> findAll(){
        return specificationService.findAll();
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */

    @RequestMapping("/findPage")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public PageResult findPage(Integer pageNo,Integer pageSize){
        return specificationService.findPage( pageNo,pageSize);
    }

    /**
     * 新增品牌
     * @param specification
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public ResultMessage add(@RequestBody Specification specification){
        try {
            specificationService.add(specification);
            return new ResultMessage(true,"新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false,"新增失败");
        }
    }


    /**
     * 修改数据回现
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public Specification findOne(Long id){
        return specificationService.findOne(id);
    }

    /**
     * 根据id修改品牌
     * @param specification
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public ResultMessage update(@RequestBody Specification specification){

        try {
            specificationService.update(specification);
            return new ResultMessage(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false,"修改失败");
        }
    }

    /**
     * 根据id删除
     * @param Ids
     * @return
     */
    @RequestMapping("/dele")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public ResultMessage dele(Long[] Ids){

        try {
            specificationService.dele(Ids);
            return new ResultMessage(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false,"删除失败");
        }
    }


    /**
     * 模糊分页查询
     * @param pageNo
     * @param pageSize
     * @param specification
     * @return
     */
    @RequestMapping("/query")
    public PageResult query(Integer pageNo,Integer pageSize,@RequestBody TbSpecification specification){
        return specificationService.queryPage( pageNo,pageSize,specification);
    }

    /**
     * 此方法是添加模板时要求的，数据格式为[{"id":1,"text":"联想"},{"id":2,"text":"华为"}]
     * @return
     */
    @RequestMapping("/findSpecList")
    public List<Map> findSpecList(){
        return specificationService.findSpecList();
    }
}

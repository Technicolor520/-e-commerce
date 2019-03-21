package com.pyg.manager;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.TypeTemplateService;
import com.pyg.pojo.TbTypeTemplate;
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
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference  //远程注入
    private TypeTemplateService typeTemplateService;


    /**
     * 查询所有品牌
     * @return
     */
    @RequestMapping("/findAll")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public List<TbTypeTemplate> findAll(){
        return typeTemplateService.findAll();
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
        return typeTemplateService.findPage( pageNo,pageSize);
    }

    /**
     * 新增品牌
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public ResultMessage add(@RequestBody TbTypeTemplate typeTemplate){
        return typeTemplateService.add(typeTemplate);
    }


    /**
     * 修改数据回现
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public TbTypeTemplate findOne(Long id){
        return typeTemplateService.findOne(id);
    }

    /**
     * 根据id修改品牌
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody // 把数据转成json  响应到浏览器上
    public ResultMessage update(@RequestBody TbTypeTemplate typeTemplate){
        return typeTemplateService.update(typeTemplate);
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
            typeTemplateService.dele(Ids);
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
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/query")
    public PageResult query(int pageNo,int pageSize,@RequestBody TbTypeTemplate typeTemplate){
        return typeTemplateService.queryPage( pageNo,pageSize,typeTemplate);
    }

}

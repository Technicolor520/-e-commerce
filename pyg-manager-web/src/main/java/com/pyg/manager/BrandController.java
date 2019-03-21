package com.pyg.manager;



import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.bean.PageResult;
import com.pyg.bean.ResultMessage;
import com.pyg.managerinterface.BrandService;
import com.pyg.pojo.TbBrand;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/brand")
public class BrandController {

    @Reference  //远程注入
    private BrandService brandService;

    /**
     * 查询所有品牌
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */

    @RequestMapping("/findPage")
    public PageResult findPage(Integer pageNo,Integer pageSize){
        return brandService.findPage( pageNo,pageSize);
    }

    /**
     * 新增品牌
     * @param brand
     * @return
     */
    @RequestMapping("/add")
    public ResultMessage add(@RequestBody TbBrand brand){
        return brandService.add(brand);
    }


    /**
     * 修改数据回现
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }

    /**
     * 根据id修改品牌
     * @param brand
     * @return
     */
    @RequestMapping("/update")
    public ResultMessage update(@RequestBody TbBrand brand){
        return brandService.update(brand);
    }

    /**
     * 根据id删除
     * @param Ids
     * @return
     */
    @RequestMapping("/dele")
    public ResultMessage dele(Long[] Ids){

        try {
            brandService.dele(Ids);
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
     * @param brand
     * @return
     */
    @RequestMapping("/query")
    public PageResult query(Integer pageNo,Integer pageSize,@RequestBody TbBrand brand){
        return brandService.queryPage( pageNo,pageSize,brand);
    }
    /**
     * 此方法是添加模板时要求的，数据格式为[{"id":1,"text":"联想"},{"id":2,"text":"华为"}]
     * @return
     */
    @RequestMapping("/findBrandList")
    public List<Map> findBrandList(){
        return brandService.findBrandList();
    }
}

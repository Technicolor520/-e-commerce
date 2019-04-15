package com.pyg.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.bean.ResultMessage;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.seckill.SeckillService;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.xml.security.utils.resolver.implementations.ResolverAnonymous;
import org.apache.zookeeper.data.Id;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/12 22:57
 * @description TODO
 **/
@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @Reference
    private SeckillService seckillService;

    /**
     * 显示商品信息
     *
     * @return
     */
    @RequestMapping("/findSeckillGoods")
    public List<TbSeckillGoods> findSeckillGoods() {
        return seckillService.findSeckillGoodsFromRedis();
    }

    /**
     * 显示秒杀商品的详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public TbSeckillGoods findOne(@PathVariable("id") Long id) {
        return seckillService.findOneSeckillGoodsFromRedis(id);
    }

    /**
     * 秒杀下单
     * @param id
     * @return
     */
    @RequestMapping("/saveOrder/{id}")
    public ResultMessage saveOrder(@PathVariable("id") Long id){
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            if(userId.equals("anonymousUser")){
                return new ResultMessage(false,"请先登录");
            }

            seckillService.saveOrder(id,userId);
            return new ResultMessage(true,"");
        } catch (RuntimeException e){
            return new ResultMessage(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false,"抢购失败");
        }
    }

}



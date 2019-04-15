package com.pyg.seckill;

import com.pyg.bean.ResultMessage;
import com.pyg.pojo.TbSeckillGoods;

import java.util.List;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/12 23:01
 * @description TODO
 **/


public interface SeckillService {

    /**
     * 显示商品信息
     * @return
     */
    List<TbSeckillGoods> findSeckillGoodsFromRedis();

    /**
     * 显示秒杀商品的详情
     * @return
     */
    TbSeckillGoods findOneSeckillGoodsFromRedis(Long id);

    /**
     * 秒杀下单
     * @param id
     * @param userId
     * @return
     */
   void saveOrder(Long id, String userId);
}

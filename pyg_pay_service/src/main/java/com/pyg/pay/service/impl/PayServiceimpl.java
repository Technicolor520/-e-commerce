package com.pyg.pay.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.mapper.TbPayLogMapper;
import com.pyg.pay.service.PayService;
import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbPayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/4/10 23:01
 * @description TODO
 **/

@Service
public class PayServiceimpl implements PayService {

    @Value("${appid}")
    private String appid;  //公众号id
    @Value("${partner}")
    private String partner; // 商户号
    @Value("${partnerkey}")
    private String partnerkey;//商户号密码

    @Value("${notifyurl}")
    private String notifyurl; //回调地址 工信部备案的域名并且不能带参数

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbPayLogMapper payLogMapper;

    @Autowired
    private TbOrderMapper orderMapper;


    /**
     * 生成二维码
     * @param out_trade_no
     * @param userId
     * @return
     */
    @Override
    public Map createNative(String out_trade_no, String userId) {
        try {
            //从redis中获取流水号
            TbPayLog payLog = (TbPayLog) redisTemplate.boundHashOps("payLog" + userId).get(out_trade_no);
            //        调用微信的统一下单API  主要目的是为了获取code_url
            utils.HttpClient httpClient = new utils.HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            Map<String, String> paramMap = new HashMap<>();
//        公众账号ID	appid	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）
            paramMap.put("appid",appid);
//        商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
            paramMap.put("mch_id",partner);
//        随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，长度要求在32位以内。推荐随机数生成算法
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
//        商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值
            paramMap.put("body","品优购支付");
//        商户订单号	out_trade_no	是	String(32)	20150806125346	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号
            paramMap.put("out_trade_no",out_trade_no);
//        标价金额	total_fee	是	Int	88	订单总金额，单位为分，详见支付金额
//        paramMap.put("total_fee",payLog.getTotalFee().toString());//TODO  项目上线
            paramMap.put("total_fee","1");
//        终端IP	spbill_create_ip	是	String(64)	123.12.12.123	支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
            paramMap.put("spbill_create_ip", "127.0.0.1");
//        通知地址	notify_url	是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
            paramMap.put("notify_url",notifyurl);
//        交易类型	trade_type	是	String(16)	JSAPI
            paramMap.put("trade_type","NATIVE  ");
//        签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	通过签名算法计算得出的签名值，详见签名生成算法
            String signedXml = WXPayUtil.generateSignedXml(paramMap, partnerkey);//生成带有签名的xml

            //运用远程调用技术
            httpClient.setXmlParam(signedXml);//设置参数
            httpClient.post();//执行请求
            String content = httpClient.getContent();//获取结果XML
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);//将结果转成map格式
            resultMap.put("totalFee",payLog.getTotalFee().toString());
            return  resultMap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Map orderQuery(String out_trade_no) {
        try {
            //        调用微信的统一下单API  主要目的是为了获取code_url
            utils.HttpClient httpClient = new utils.HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            Map<String, String> paramMap = new HashMap<>();
//        公众账号ID	appid	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）
            paramMap.put("appid", appid);
//        商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
            paramMap.put("mch_id", partner);
//        随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，长度要求在32位以内。推荐随机数生成算法
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
//        商户订单号	out_trade_no	是	String(32)	20150806125346	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号
            paramMap.put("out_trade_no", out_trade_no);
//        签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	通过签名算法计算得出的签名值，详见签名生成算法
            String signedXml = WXPayUtil.generateSignedXml(paramMap, partnerkey);//生成带有签名的xml

            //运用远程调用技术
            httpClient.setXmlParam(signedXml);//设置参数
            httpClient.post();//执行请求
            String content = httpClient.getContent();//获取结果XML
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);//将结果转成map格式
            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 修改订单状态
     * @param out_trade_no
     * @param transaction_id
     * @param userId
     * @return
     */
    @Override
    public void updateOrder(String out_trade_no, String transaction_id, String userId) {

        TbPayLog payLog = (TbPayLog) redisTemplate.boundHashOps("payLog" + userId).get(out_trade_no);


//  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
//  `transaction_id` varchar(30) DEFAULT NULL COMMENT '交易号码',
//  `trade_state` varchar(1) DEFAULT NULL COMMENT '交易状态',

        payLog.setCreateTime(new Date());
        payLog.setTradeState("1");
        payLog.setTransactionId(transaction_id);
        payLogMapper.updateByPrimaryKey(payLog);//修改日志

        String[] orderIds = payLog.getOrderList().split(",");
        for (String orderId : orderIds) {

            TbOrder tbOrder = orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
            tbOrder.setStatus("2");//
            tbOrder.setUpdateTime(new Date());
            tbOrder.setPaymentTime(new Date());
            orderMapper.updateByPrimaryKey(tbOrder);
        }
        redisTemplate.boundHashOps("payLog" + userId).delete(out_trade_no);
    }
}

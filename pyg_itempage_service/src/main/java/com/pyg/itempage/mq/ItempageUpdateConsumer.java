package com.pyg.itempage.mq;

import com.pyg.groupEntity.Goods;
import com.pyg.itempage.service.ItempageService;
import com.pyg.itempage.service.impl.ItempageServiceimpl;
import com.pyg.pojo.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/29 20:57
 * @description TODO
 **/


public class ItempageUpdateConsumer implements MessageListener {

    @Autowired
    private ItempageService itempageService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage= (TextMessage) message;
        try {
            String goodsId = textMessage.getText();
            Goods goods = itempageService.findOne(Long.parseLong(goodsId)); //组合类对象中包含了三张表的数据

//        生成静态页面
//        第一步：创建一个 Configuration 对象，直接 new 一个对象。构造方法的参数就是freemarker 的版本号。
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
//        第二步：设置模板文件所在的路径。
//        第三步：设置模板文件使用的字符集。一般就是 utf-8.
//        第四步：加载一个模板，创建一个模板对象。
            Template template = configuration.getTemplate("item.ftl");
//        第五步：创建一个模板使用的数据集，可以是 pojo 也可以是 map。一般是 Map。

//        数据集包含了哪些表的数据
            List<TbItem> itemList = goods.getItemList();
            for (TbItem tbItem : itemList) {
                //        第六步：创建一个 Writer 对象，一般创建一 FileWriter 对象，指定生成的文件名。
                Writer writer =new FileWriter("F:\\ideaproject\\html\\"+tbItem.getId()+".html");
                Map dataModel = new HashMap();
                dataModel.put("goods",goods);
                dataModel.put("tbItem",tbItem);
//        第七步：调用模板对象的 process 方法输出文件。
                template.process(dataModel,writer);
//        第八步：关闭流
                writer.close();
            }
            System.out.println("itempage is updated");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

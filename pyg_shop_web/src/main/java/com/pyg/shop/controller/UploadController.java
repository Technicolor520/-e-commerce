package com.pyg.shop.controller;

import com.pyg.bean.ResultMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utils.FastDFSClient;

/**
 * @author Technicolor
 * @version v1.0
 * @date 2019/3/18 22:50
 * @description TODO
 **/

/*@RestController
@RequestMapping("/upload")
public class UploadController {
    @Value("${upload_server}")
    private String uploadServer;
    @RequestMapping("uploadFile")
    public ResultMessage uploadFile(MultipartFile file){
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastdfs_client.conf");
            String filename = file.getOriginalFilename();
            String exName = filename.substring(filename.lastIndexOf(".") + 1);
            String fileUrl = fastDFSClient.uploadFile(filename.getBytes(), exName);
            return new  ResultMessage(true,uploadServer+fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false,"上传失败");
        }
    }

}*/
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Value("${upload_server}")  //springmvc从properties中获取变量的方式
    private String uploadServer;
    @RequestMapping("/uploadFile")
    public ResultMessage uploadFile(MultipartFile  file){
        //完成文件上传 上传成功后返回图片对应的url地址
        // http://192.168.25.133/group1/M00/00/00/wKgZhVyLTRqAURmIAAFP0yQoHiA099.jpg
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastdfs_client.conf");
            String filename = file.getOriginalFilename(); //wKgZhVy.LTRqAURm.IAAFP0y.QoHiA099.jpg
            String extName = filename.substring(filename.lastIndexOf(".")+1);  //.jpg
            String fileUrl = fastDFSClient.uploadFile(file.getBytes(), extName);
//            fileUrl = group1/M00/00/00/wKgZhVyLTRqAURmIAAFP0yQoHiA099.jpg
            return new ResultMessage(true,uploadServer+fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMessage(false,"上传失败");
        }


    }
}
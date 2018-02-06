package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片长传controller
 * Created by cxq on 2017/12/9.
 */
@Controller
@RequestMapping("/pic")
public class PictureController {

    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("/upload")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile){
        try {
            //把图片上传到图片服务器，得到图片地址
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/client.conf");
            String filename = uploadFile.getOriginalFilename();
            //截取扩展名
            String ext = filename.substring(filename.lastIndexOf(".") + 1);
            String fileUrl = fastDFSClient.uploadFile(uploadFile.getBytes(), ext);
            //补充完整的url，
            fileUrl = IMAGE_SERVER_URL + fileUrl;

            Map result = new HashMap();
            result.put("error",0);
            result.put("url",fileUrl);

            return JsonUtils.objectToJson(result);
            //封装map返回
        } catch (Exception e) {
            e.printStackTrace();
            Map result = new HashMap();
            result.put("error",1);
            result.put("message","图片上传失败");
            return JsonUtils.objectToJson(result);
        }
    }
}

package com.cxq.test;

import cn.e3mall.common.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * Created by cxq on 2017/10/12.
 */
public class FastDFSTest {
    @Test
    public void testFast() throws Exception{
        //创建配置文件，文件名任意，内容是tracker服务器的地址
        //使用全局变量加载配置文件
        ClientGlobal.init("E:\\Project2\\e3-manager-web\\src\\main\\resources\\config\\client.conf");
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient得到TrackerServer的对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建StorageServer的引用，可以是null
        StorageServer storageServer = null;
        //创建一个创建StorageClient的引用，参数需要TrackerServer和StorageServer
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        //使用StorageClient上传文件。
        String[] jpgs = storageClient.upload_file("G:\\桌面壁纸\\1.jpg", "jpg", null);
        for (String jpg : jpgs) {
            System.out.println(jpg);
        }
    }
@Test
    public void testDFSClent()throws Exception{
        FastDFSClient fastDFSClient = new FastDFSClient("E:\\Project2\\e3-manager-web\\src\\main\\resources\\config\\client.conf");

        String s = fastDFSClient.uploadFile("G:\\桌面壁纸\\2.jpg");

        System.out.println(s);


    }
}

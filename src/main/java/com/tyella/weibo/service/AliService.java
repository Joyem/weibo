package com.tyella.weibo.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.tyella.weibo.util.WeiboUtil.isFillAllowed;

/**
 * Created by tyella on 2019/2/13.
 */
@Service
public class AliService {
    private static final Logger logger = LoggerFactory.getLogger(AliService.class);

    //TODO
    //申请阿里云帐号
    private static String url = "http://oss-cn-hangzhou.aliyuncs.com/";
    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
    private static String accessKeyId = "<yourAccessKeyId>";
    private static String accessKeySecret = "<yourAccessKeySecret>";
    private static String bucketName = "todo";

    /**
     * 当图片量太大时本地存不下，可上传到云
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String saveImage(MultipartFile multipartFile) throws IOException {
        int dotPos = multipartFile.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        String fileExt = multipartFile.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!isFillAllowed(fileExt)) {
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String url = upLoad(fileName, multipartFile);
        return url;
    }

    /**
     * 使用了阿里云的对象存储OSS
     * @param fileName
     * @param multipartFile
     * @return 上传成功返回图片url,上传失败返回null
     */
    public String upLoad(String fileName, MultipartFile multipartFile) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        //上传文件
        PutObjectResult res;
        try {
            res = ossClient.putObject(bucketName, fileName, multipartFile.getInputStream());
            if (res == null) {
                return null;
            }
            System.out.println(res.getETag());
            return url + fileName;
        } catch (IOException e) {
            logger.error("上传失败" + e.getMessage());
        }
        // 关闭OSSClient。
        ossClient.shutdown();
        return null;
    }
}

package com.tyella.weibo.service;


import com.tyella.weibo.dao.NewsDao;
import com.tyella.weibo.model.News;
import com.tyella.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsDao newsDao;

    public int addNews(News news) {
        newsDao.addNews(news);
        return news.getId();
    }

    public News getNews(int newsId) {
        return newsDao.selectById(newsId);
    }

    public List<News> getLastedNews(int id, int offset, int limit) {
        return newsDao.selectByUserIdAndOffset(id, offset, limit);
    }

    public int updateCommentCount(int id, int count) {
        return newsDao.updateCommentCount(id, count);
    }

    public int updateLikeCount(int id, int count) {
        return newsDao.updateLikeCount(id, count);
    }

    /**
     * 上传一张图片
     */
    //TODO
    //MultipartFile上传文件
    public String saveImage(MultipartFile multipartFile) throws IOException {
        //dotPos "."在文件名中的位置
        int dotPos = multipartFile.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        //文件扩展名
        String fileExt = multipartFile.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!WeiboUtil.isFillAllowed(fileExt)) {
            return null;
        }
        //文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        Files.copy(multipartFile.getInputStream(), new File(WeiboUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return WeiboUtil.WEIBO_DOMIN + "image?name=" + fileName;
    }

    /**
     * 上传多张图片
     */
    public String saveImages(MultipartFile[] multipartFiles) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (MultipartFile multipartFile : multipartFiles) {
            String fileUrl = saveImage(multipartFile);
            sb.append(",");
            sb.append(fileUrl);
        }
        return sb.toString();
    }
}

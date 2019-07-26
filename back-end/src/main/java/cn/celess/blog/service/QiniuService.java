package cn.celess.blog.service;

import cn.celess.blog.entity.model.QiniuResponse;
import com.qiniu.storage.model.FileInfo;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author : xiaohai
 * @date : 2019/04/25 18:15
 */
@Service
public interface QiniuService {
    QiniuResponse uploadFile(InputStream is, String fileName);

    FileInfo[] getFileList();

    boolean continueFile(String key);
}

package site.pyyf.utils;

import com.aliyun.oss.OSS;

import com.aliyun.oss.OSSClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.pyyf.entity.PicUploadResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;


public class UploadDir {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class AliyunConfig {

        private String endpoint = "";
        private String accessKeyId ="";
        private String accessKeySecret="";
        private String bucketName="";
        private String urlPrefix="https://pyyf.oss-cn-hangzhou.aliyuncs.com/";

        public OSS oSSClient() {
            return new OSSClient(endpoint, accessKeyId, accessKeySecret);
        }

    }


    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
            ".jpeg", ".gif", ".png"};

    private AliyunConfig aliyunConfig = new AliyunConfig();
    private OSS ossClient = aliyunConfig.oSSClient();
    public PicUploadResult upload(File file) {
        String imgName = file.getName();
        String imgsPath = file.getParent();
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(imgName,
                    type)) {
                isLegal = true;
                break;
            }
        }
        // 封装Result对象，并且将文件的byte数组放置到result对象中
        PicUploadResult fileUploadResult = new PicUploadResult();
        if(!isLegal){
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }

        String remotePath = getFilePath(imgsPath,imgName);
        // 上传到阿里云
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), remotePath, new FileInputStream(imgsPath+"\\"+imgName));
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        fileUploadResult.setStatus("done");
        fileUploadResult.setName(this.aliyunConfig.getUrlPrefix() + remotePath);
        return fileUploadResult;
    }
    private String getFilePath(String fileName,String imgName) {

        return "ebook/" + StringUtils.substringAfterLast(fileName,"\\") + "/"+imgName;
    }


}

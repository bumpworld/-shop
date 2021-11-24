package com.xxx.service;


import com.xxx.common.constant.ImagePathConstant;
import com.xxx.exception.pojo.CustomException;
import com.xxx.exception.pojo.ExceptionEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UploadService {

    List<String> ALLOW_IMAGE_TYPE = Arrays.asList("image/jpeg", "image/png");



    public String brandUpload(MultipartFile file) {
        String savePath = ImagePathConstant.BrandLogoUploadPath;
        String readPath = ImagePathConstant.BrandLogoReadPath;
        return uploadImage(file, savePath, readPath);
    }

    /**
     * @param file
     * @param savePath 保存路径
     * @param readPath 网页访问路径（作为返回值）
     * @return
     */

    public String uploadImage(MultipartFile file, String savePath, String readPath) {

        //不是合法的后缀名
        if (!ALLOW_IMAGE_TYPE.contains(file.getContentType())) {
            throw new CustomException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        //判断是不是图片
        BufferedImage read = null;
        try {
            read = ImageIO.read(file.getInputStream());
            if (read == null) {
                //不是图片资源
                throw new CustomException(ExceptionEnum.INVALID_FILE_TYPE);
            }
        } catch (IOException e) {
            throw new CustomException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File route = new File(savePath, filename);
        try {
            file.transferTo(route);
        } catch (IOException e) {
            throw new CustomException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }

        return readPath + filename;
    }



}

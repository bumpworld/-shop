package com.xxx.controller;

import com.xxx.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     *
     * @param file 参数名必须为file
     * @return
     */
    @PostMapping("/image")
    public ResponseEntity<String> uploadImageToNginx(MultipartFile file){
        String filePath = uploadService.brandUpload(file);
        return ResponseEntity.ok(filePath);
    }

    @GetMapping("/signature")
    public ResponseEntity<Map<String, Object>> getOssSignature(){
        Map<String, Object> result = uploadService.getOssSignature();
        return ResponseEntity.ok(result);
    }
}

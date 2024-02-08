package com.example.controller;

import com.example.dto.Result;
import com.example.utils.AliOss;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileLoadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        //把文件內容存到本地磁盤上
        String originalFilename = file.getOriginalFilename();
        //保證文件的名字是唯一的，從而防止文件覆蓋
        String filename= UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
//        file.transferTo(new File("C:\\Users\\z5314\\Desktop\\files\\"+filename));
        String url = AliOss.upload(filename, file.getInputStream());
        return Result.success(url);
    }
}

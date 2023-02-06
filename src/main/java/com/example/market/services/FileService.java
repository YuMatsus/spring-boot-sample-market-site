package com.example.market.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    // 画像のアップロード
    public void uploadImage(MultipartFile multipartFile, String fileName);
}

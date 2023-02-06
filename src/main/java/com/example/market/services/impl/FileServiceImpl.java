package com.example.market.services.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.market.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private Environment environment;
	
	// 画像のアップロード
	@Transactional
	@Override
    public void uploadImage(MultipartFile multipartFile, String fileName) {
		Path filePath = Paths.get(environment.getProperty("sample.images.imagedir") + fileName);
		System.out.println(filePath);
		try {
			byte[] bytes = multipartFile.getBytes();
			OutputStream stream = Files.newOutputStream(filePath);
			stream.write(bytes);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}    
}

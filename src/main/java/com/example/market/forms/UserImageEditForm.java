package com.example.market.forms;

import org.springframework.web.multipart.MultipartFile;

import com.example.market.validators.CheckFileExtension;
import com.example.market.validators.CheckImageSize;
import com.example.market.validators.ExistsFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserImageEditForm {

    @ExistsFile(message = "画像を選択してください")
    @CheckFileExtension(extensions = {"jpg", "jpeg", "png"})
    @CheckImageSize(min = 50, max = 1000, message = "画像は5～1000pxのものをアップロードしてください")
	private MultipartFile image;
    
}

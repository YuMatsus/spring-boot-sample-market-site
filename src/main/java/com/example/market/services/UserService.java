package com.example.market.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.market.entities.User;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(long id);
    boolean existsByEmail(String email);
    // ユーザー登録
    void register(String name, String email, String  password, String[] roles, String profile, String image);
    // プロフィールの更新
    void update(Long id, String name, String profile);
    // プロフィール画像の更新
    void updateImage(Long id, MultipartFile image);
    // ユーザー情報の更新
    void updateSecurityContext(String email);
    User getUpdatedUser(String email);
}

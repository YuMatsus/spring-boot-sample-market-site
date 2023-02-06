package com.example.market.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.market.entities.Item;
import com.example.market.entities.User;

public interface ItemService {
    // 他のユーザーの出品を取得
    List<Item> getOtherUsersItem(User user);
    // 出品処理
	Item register(User user, long categoryId, String name, String description, int price, MultipartFile image);
    // 商品情報の更新
    Item update(Long id, String name, String description, int price, long categoryId);
    // 商品画像の更新
    Item updateImage(Long id, MultipartFile image);
    // お気に入り処理
    void toggleFavorite(User user, long itemId);
    // 購入処理
    void purchase(User user, long itemId);
    // 削除
	void delete(long id);
}

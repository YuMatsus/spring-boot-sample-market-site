package com.example.market.services.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.market.entities.Category;
import com.example.market.entities.Item;
import com.example.market.entities.User;
import com.example.market.repositories.CategoryRepository;
import com.example.market.repositories.ItemRepository;
import com.example.market.repositories.UserRepository;
import com.example.market.services.FileService;
import com.example.market.services.ItemService;
import com.example.market.services.UserService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private FileService fileService;

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
	private Environment environment;

	// ユーザーIDから出品を新着順に取得
	@Transactional(readOnly = true)
	@Override
	public List<Item> getOtherUsersItem(User user) {
		List<User> otherUsers = userRepository.findByIdNot(user.getId());
		List<Long> ids = new ArrayList();
		for(User otherUser : otherUsers){
			ids.add(otherUser.getId());
		}
		return itemRepository.findByUserIdInOrderByCreatedAtDesc(ids);
	}	

    // 出品処理
    @Transactional
    @Override
    public Item register(User user, long categoryId, String name, String description, int price, MultipartFile image) {
        if(image.getOriginalFilename().isEmpty()) {
			throw new RuntimeException("ファイルが設定されていません");
		}
        Category category = categoryRepository.findById(categoryId).orElseThrow();
		String extension = FilenameUtils.getExtension(image.getOriginalFilename());
		String randomFileName = RandomStringUtils.randomAlphanumeric(20) + "." + extension;
		fileService.uploadImage(image, randomFileName);
		Item item = new Item(null, user, category, null, null, name, description, price, randomFileName, true, null, null);
		itemRepository.saveAndFlush(item);	
		return item;
    }

	// 商品情報の更新
	@Transactional
    @Override
    public Item update(Long id, String name, String description, int price, long categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow();
		Item item = itemRepository.findById(id).orElseThrow();
		item.setName(name);
		item.setDescription(description);
		item.setPrice(price);
		item.setCategory(category);
		itemRepository.saveAndFlush(item);	
		return item;
	}

	// 商品画像の更新
	@Transactional
    @Override
    public Item updateImage(Long id, MultipartFile image) {
		if(image.getOriginalFilename().isEmpty()) {
			throw new RuntimeException("ファイルが設定されていません");
		}
		String extension = FilenameUtils.getExtension(image.getOriginalFilename());
		String randomFileName = RandomStringUtils.randomAlphanumeric(20) + "." + extension;
		fileService.uploadImage(image, randomFileName);
		Item item = itemRepository.findById(id).orElseThrow();
		item.setImage(randomFileName);
		itemRepository.saveAndFlush(item);	
		return item;
	}

	// お気に入り登録のトグル
	@Transactional
	@Override
	public void toggleFavorite(User user, long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow();
		if(item.getFavoritedUsers().contains(user)){
            removeFavorite(user, item);
            return;
        }
        addFavorite(user, item);
	}

	// お気に入りに追加
	private void addFavorite(User user, Item item){
        item.getFavoritedUsers().add(user);
        itemRepository.saveAndFlush(item);
    }

	// お気に入りから削除
    private void removeFavorite(User user, Item item){
        item.getFavoritedUsers().remove(user);
        itemRepository.saveAndFlush(item);
    }

	// 購入処理
	@Transactional
	@Override
    public void purchase(User user, long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow();
		sold(item);
		addOrderToHistory(user, item);
	}

	// 商品の状態を売り切れに変更
	private void sold(Item item){
		item.setOnSale(false);
		itemRepository.saveAndFlush(item);
	}

	// 購入履歴に注文を追加
	private void addOrderToHistory(User user, Item item){	
		item.setOrderedUser(user);	
		itemRepository.saveAndFlush(item);
	}

	// 削除
	@Transactional
	@Override
	public void delete(long id) {
		Item item = itemRepository.findById(id).orElseThrow();
		itemRepository.delete(item);
	}
    
}

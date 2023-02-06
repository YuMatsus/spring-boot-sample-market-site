package com.example.market.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.market.entities.Item;
import com.example.market.entities.User;
import com.example.market.forms.ItemCreateForm;
import com.example.market.forms.ItemEditForm;
import com.example.market.forms.ItemImageEditForm;
import com.example.market.repositories.ItemRepository;
import com.example.market.services.CategoryService;
import com.example.market.services.ItemService;
import com.example.market.services.UserService;

@RequestMapping("/items")
@Controller
public class ItemController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired ItemRepository itemRepository;

    @Autowired
    private CategoryService categoryService;
    
    // 新規出品
    @GetMapping("/create")
	public String create(
        @ModelAttribute("itemCreateForm") ItemCreateForm itemCreateForm,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("categories", categoryService.findAll());
		model.addAttribute("title", "新規出品");
		model.addAttribute("main", "items/create::main");
		return "layout/layout";
	}

    // 新規出品の処理
    @PostMapping("/create")
	public String createProcess(
		@Valid ItemCreateForm itemCreateForm,
        BindingResult bindingResult,
		RedirectAttributes redirectAttributes,
		@AuthenticationPrincipal(expression = "user") User user,
		Model model
    ) {
		if(bindingResult.hasErrors()) {
			return create(itemCreateForm, user, model);
		}
        Item item = itemService.register(
            user,
            itemCreateForm.getCategory(),
            itemCreateForm.getName(),
            itemCreateForm.getDescription(),
            itemCreateForm.getPrice(),
            itemCreateForm.getImage()
        );
        redirectAttributes.addFlashAttribute("successMessage", "出品が完了しました");
		return "redirect:/items/" + item.getId();
	}

    // 商品詳細
    @GetMapping("/{id}")
	public String detail(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id") long id,
        Model model
    ) {
        try {
            Item item = itemRepository.findById(id).orElseThrow();
            model.addAttribute("item", item);
        } catch(NoSuchElementException e) {
            e.getStackTrace();
            model.addAttribute("item", null);
        }
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "商品詳細");
        model.addAttribute("main", "items/detail::main");
        return "layout/layout";
	}

    // 商品情報編集
    @GetMapping("/{id}/edit")
	public String edit(
        @ModelAttribute("itemEditForm") ItemEditForm itemEditForm,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id") long id,
        Model model
    ) {
        Item item = itemRepository.findById(id).orElseThrow();
        itemEditForm.setName(item.getName());
        itemEditForm.setDescription(item.getDescription());
        itemEditForm.setPrice(item.getPrice());
        itemEditForm.setCategory(item.getCategory().getId());
        model.addAttribute("item", item);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("loginUser", loginUser);
		model.addAttribute("title", "商品情報編集");
		model.addAttribute("main", "items/edit::main");
		return "layout/layout";
	}

    // 商品情報編集処理
    @PostMapping("/{id}/edit")
	public String editProcess(
		@Valid ItemEditForm itemEditForm,
        BindingResult bindingResult,
		RedirectAttributes redirectAttributes,
		@AuthenticationPrincipal(expression = "user") User user,
        @PathVariable("id") long id,
		Model model
    ) {
		if(bindingResult.hasErrors()) {
			return edit(itemEditForm, user, id, model);
		}
        Item item = itemService.update(
            id,
            itemEditForm.getName(),
            itemEditForm.getDescription(),
            itemEditForm.getPrice(),
            itemEditForm.getCategory()
        );
        redirectAttributes.addFlashAttribute("successMessage", "商品情報の編集が完了しました");
		return "redirect:/items/" + item.getId();
	}

    // 商品画像の変更
    @GetMapping("/{id}/edit_image")
	public String editImage(
        @ModelAttribute("itemImageEditForm") ItemImageEditForm itemImageEditForm,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id") long id,
        Model model
    ) {
        Item item = itemRepository.findById(id).orElseThrow();
        model.addAttribute("item", item);
        model.addAttribute("loginUser", loginUser);
		model.addAttribute("title", "商品画像の変更");
		model.addAttribute("main", "items/edit_image::main");
		return "layout/layout";
	}

    // 商品画像の変更処理
    @PostMapping("/{id}/edit_image")
	public String editImageProcess(
		@Valid ItemImageEditForm itemImageEditForm,
        BindingResult bindingResult,
		RedirectAttributes redirectAttributes,
		@AuthenticationPrincipal(expression = "user") User user,
        @PathVariable("id") long id,
		Model model
    ) {
		if(bindingResult.hasErrors()) {
			return editImage(itemImageEditForm, user, id, model);
		}
        Item item = itemService.updateImage(id, itemImageEditForm.getImage());
        redirectAttributes.addFlashAttribute("successMessage", "商品画像の変更が完了しました");
		return "redirect:/items/" + item.getId();
	}

    // 購入確認
    @GetMapping("/{id}/confirm")
	public String confirm(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id") long id,
        Model model
    ) {
        Item item = itemRepository.findById(id).orElseThrow();
        model.addAttribute("item", item);
        model.addAttribute("loginUser", loginUser);
		model.addAttribute("title", "購入確認");
		model.addAttribute("main", "items/confirm::main");
		return "layout/layout";
	}

    // 購入処理
    @PostMapping("/{id}/confirm")
	public String editProcess(
		RedirectAttributes redirectAttributes,
		@AuthenticationPrincipal(expression = "user") User user,
        @PathVariable("id") long id,
		Model model
    ) {
        Item item = itemRepository.findById(id).orElseThrow();
        if(item.isOnSale()) {
            itemService.purchase(user, id);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("successMessage", "ご購入ありがとうございました。");
            return "redirect:/items/" + item.getId() + "/finish";
        } else {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("successMessage", "申し訳ありません。ちょっと前に売り切れました。");
            return "redirect:/items/" + item.getId() + "/finish";
        }
	}

    // 購入確定
    @GetMapping("/{id}/finish")
	public String finish(
        @ModelAttribute("success") boolean success,
        @ModelAttribute("successMessage") String successMessage,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id") long id,
        Model model
    ) {
        Item item = itemRepository.findById(id).orElseThrow();
        model.addAttribute("item", item);
        model.addAttribute("loginUser", loginUser);
		model.addAttribute("title", successMessage);
		model.addAttribute("main", "items/finish::main");
		return "layout/layout";
	}

    // お気に入り処理
    @PostMapping("/toggleFavorite/{id}")    
    public String toggleFavorite(
        @PathVariable("id")  Integer id,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        itemService.toggleFavorite(loginUser, id);
        userService.updateSecurityContext(loginUser.getEmail());
        return "redirect:/";  
    }

    // 削除
    @PostMapping("/{id}/delete")
	public String delete(
		@PathVariable("id") Integer id,
		RedirectAttributes redirectAttributes,
		Model model) {
		itemService.delete(id);
		redirectAttributes.addFlashAttribute(
			"successMessage",
			"削除が完了しました。"
		);
		return "redirect:/users/exhibitions";
	}
    
}

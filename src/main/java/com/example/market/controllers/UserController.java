package com.example.market.controllers;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.market.entities.Item;
import com.example.market.entities.User;
import com.example.market.forms.SignUpForm;
import com.example.market.forms.UserEditForm;
import com.example.market.forms.UserImageEditForm;
import com.example.market.repositories.ItemRepository;
import com.example.market.services.UserService;

@RequestMapping("/users")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRepository itemRepository;

    // ユーザー登録
    @GetMapping("/sign_up")    
    public String signUp(
        @ModelAttribute("sign_up") SignUpForm signUpForm,
        Model model
    ) {
        model.addAttribute("signUpForm", signUpForm);
        model.addAttribute("main", "users/sign_up::main");
        return "layout/layout";    
    }

    // ユーザー登録処理
    @PostMapping("/sign_up")
    public String signUpProcess(
        @Valid SignUpForm signUpForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        HttpServletRequest request,
        Model model
    ){
        if(bindingResult.hasErrors()) {
			return signUp(signUpForm, model);
		}
        String[] roles = {"ROLE_USER"};
        userService.register(
            signUpForm.getName(),
            signUpForm.getEmail(),
            signUpForm.getPassword(),
            roles,
            null,
            null);
        try {
            request.login(signUpForm.getEmail(), signUpForm.getPassword());
        } catch (ServletException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("successMessage","アカウントの登録が完了しました");
        return "redirect:/";
    }

    // ログイン
    @GetMapping("/sign_in")
	public String signIn(
        @RequestParam(value = "error", required = false) String error,
        Model model
    ) {
        if (error != null) {
            model.addAttribute("error", "ユーザ名かパスワードが正しくありません");
        }
		model.addAttribute("title", "ログイン");
		model.addAttribute("main", "users/sign_in::main");
		return "layout/layout";
	}

    // プロフィール詳細
    @GetMapping("/detail")
	public String detail(
		@AuthenticationPrincipal(expression = "user") User loginUser,
		Model model
    ) {
        List<Item> items = itemRepository.findByUserId(loginUser.getId());
        List<Item> orderHistories = itemRepository.findByOrderedUserIdOrderByCreatedAtDesc(loginUser.getId());
        model.addAttribute("items", items);
        model.addAttribute("orderHistories", orderHistories);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "プロフィール");
        model.addAttribute("main", "users/detail::main");
        return "layout/layout";
	}

    // プロフィール編集
    @GetMapping("/edit")
	public String edit(
        @ModelAttribute("userEditForm") UserEditForm userEditForm,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        userEditForm.setName(loginUser.getName());
        userEditForm.setProfile(loginUser.getProfile());
        model.addAttribute("userEditForm", userEditForm);
        model.addAttribute("loginUser", loginUser);
		model.addAttribute("title", "プロフィールを編集");
		model.addAttribute("main", "users/edit::main");
		return "layout/layout";
	}

    // プロフィール編集処理
    @PostMapping("/edit")
	public String editProcess(
		@Valid UserEditForm userEditForm,
        BindingResult bindingResult,
		RedirectAttributes redirectAttributes,
		@AuthenticationPrincipal(expression = "user") User loginUser,
		Model model
    ) {
		if(bindingResult.hasErrors()) {
			return edit(userEditForm, loginUser, model);
		}
        userService.update(
            loginUser.getId(),
            userEditForm.getName(),
            userEditForm.getProfile()
        );
        redirectAttributes.addFlashAttribute("successMessage", "プロフィールを編集しました");
		return "redirect:/users/detail";
	}

    // プロフィール画像編集
    @GetMapping("/edit_image")
	public String editImage(
        @ModelAttribute("userImageEditForm") UserImageEditForm userImageEditForm,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        model.addAttribute("loginUser", loginUser);
		model.addAttribute("title", "プロフィール画像を編集");
		model.addAttribute("main", "users/edit_image::main");
		return "layout/layout";
	}

    // プロフィール画像編集処理
    @PostMapping("/edit_image")
	public String editImageProcess(
		@Valid UserImageEditForm userImageEditForm,
        BindingResult bindingResult,
		RedirectAttributes redirectAttributes,
		@AuthenticationPrincipal(expression = "user") User loginUser,
		Model model
    ) {
		if(bindingResult.hasErrors()) {
			return editImage(userImageEditForm, loginUser, model);
		}
        userService.updateImage(
            loginUser.getId(),
            userImageEditForm.getImage()
        );
        redirectAttributes.addFlashAttribute("successMessage", "プロフィール画像を編集しました");
		return "redirect:/users/detail";
	}

    // 出品商品一覧
    @GetMapping("/exhibitions")
	public String exhibitions(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<Item> items = itemRepository.findByUserIdOrderByCreatedAtDesc(loginUser.getId());
        model.addAttribute("items", items);
        model.addAttribute("loginUser", loginUser);
		model.addAttribute("title", "出品商品一覧");
		model.addAttribute("main", "users/exhibitions::main");
		return "layout/layout";
	}

    // お気に入り一覧の表示
    @GetMapping("/bookmarks")
    public String bookmarks(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<Item> items = itemRepository.findByfavoritedUsersIdOrderByCreatedAtDesc(loginUser.getId());
        model.addAttribute("items", items);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "お気に入り一覧");
        model.addAttribute("main", "users/bookmarks::main");
        return "layout/layout";
    }

}

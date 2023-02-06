package com.example.market.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.market.entities.User;
import com.example.market.repositories.UserRepository;
import com.example.market.security.SimpleLoginUser;
import com.example.market.security.SimpleUserDetailsService;
import com.example.market.services.FileService;
import com.example.market.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SimpleUserDetailsService simpleUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileService fileService;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public void register(String name, String email, String  password, String[] roles, String profile, String image) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("該当のメールアドレスは登録済みです。");
        }
        String encodedPassword = passwordEncode(password);
        String joinedRoles = joinRoles(roles);
        User user = new User(null, null, null, null, name, email, encodedPassword, joinedRoles, Boolean.TRUE, profile, image, null, null);
        userRepository.saveAndFlush(user);
    }

    @Transactional
    @Override
    public void update(Long id, String name, String profile) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(name);
        user.setProfile(profile);
        System.out.println(profile);
        userRepository.saveAndFlush(user);
        updateSecurityContext(user.getEmail());
    }

    @Transactional
    @Override
    public void updateImage(Long id, MultipartFile image) {
        if(image.getOriginalFilename().isEmpty()) {
			throw new RuntimeException("ファイルが設定されていません");
		}
		String extension = FilenameUtils.getExtension(image.getOriginalFilename());
		String randomFileName = RandomStringUtils.randomAlphanumeric(20) + "." + extension;
		fileService.uploadImage(image, randomFileName);
        User user = userRepository.findById(id).orElseThrow();
        user.setImage(randomFileName);		
		userRepository.saveAndFlush(user);
        updateSecurityContext(user.getEmail());
    }

    private String passwordEncode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private String joinRoles(String[] roles) {
        if (roles == null || roles.length == 0) {
            return "";
        }
        return Stream.of(roles)
            .map(String::trim)
            .map(String::toUpperCase)
            .collect(Collectors.joining(","));
    }

    // ユーザー情報の更新
    @Override
    @Transactional
    public void updateSecurityContext(String email) {
    SecurityContext context = SecurityContextHolder.getContext();
    UserDetails userDetails = simpleUserDetailsService.loadUserByUsername(email);
    context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }

    @Override
    @Transactional
    public User getUpdatedUser(String email) {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = simpleUserDetailsService.loadUserByUsername(email);
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
        Authentication authentication = context.getAuthentication();
        SimpleLoginUser simpleLoginUser = (SimpleLoginUser)authentication.getPrincipal();
        User loginUser = simpleLoginUser.getUser();
        return loginUser;
    }
}

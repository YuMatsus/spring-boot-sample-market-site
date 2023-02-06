package com.example.market.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.market.validators.ExistsUserEmail;
import com.example.market.validators.MatchFields;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MatchFields(fields = {"password","passwordConfirm"}, message = "確認用のパスワードが一致しません")
public class SignUpForm {

    @NotNull
    @Size(min=1, message = "ユーザー名を入力して下さい")
    @Size(max=255, message = "ユーザー名は255文字以内で入力して下さい")
    private String name;

    @NotNull
    @Size(min=1, message = "メールアドレスを入力して下さい")
    @Size(max=255, message = "メールアドレスは255文字以内で入力して下さい")
    @ExistsUserEmail
    private String email;

    @NotNull
    @Size(min=8, message = "パスワードは8文字以上で入力して下さい")
    @Size(max=100, message = "パスワードは100文字以内で入力して下さい")
    private String password;

    @NotNull
    @Size(min=1, message = "確認用のパスワードを入力して下さい")
    private String passwordConfirm;
    
}

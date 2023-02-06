package com.example.market.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditForm {

    @NotNull
    @Size(min=1, message = "名前を入力して下さい")
    @Size(max=255, message = "名前は255文字以内で入力して下さい")
    private String name;

    @Size(max=1000, message = "プロフィールは1000文字以内で入力して下さい")
    private String profile;
    
}

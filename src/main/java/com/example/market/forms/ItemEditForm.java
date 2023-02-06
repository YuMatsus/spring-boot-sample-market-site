package com.example.market.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import com.example.market.validators.ExistsCategoryId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemEditForm {

    @NotNull
    @Size(min=1, message = "商品名を入力して下さい")
    @Size(max=255, message = "商品名は255文字以内で入力して下さい")
    private String name;

    @NotNull
    @Size(min=1, message = "商品説明を入力して下さい")
    @Size(max=1000, message = "商品説明は1000文字以内で入力して下さい")
    private String description;

    @NotNull
    @Range(min=1, message = "価格は1以上を入力して下さい")
    @Range(max=1000000, message = "価格は1000000以下を入力して下さい")
    private int price;

    @NotNull
    @ExistsCategoryId
	private long category;
    
}

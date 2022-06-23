package com.hcmus.lovelybackend.entity.request;

import com.hcmus.lovelybackend.entity.dao.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    @NotBlank(message = "Name product is mandatory.")
    private String name;

    @NotNull(message = "Image is mandatory.")
    private Image image;

    @NotNull(message = "Reserve Price is mandatory.")
    private Double reservePrice;

    @NotNull(message = "Price Step is mandatory.")
    private Double stepPrice;

    private Double quickPrice;

    @NotNull(message = "Description is mandatory.")
    private String description;

    @NotNull(message = "Auto Bid is mandatory.")
    private Boolean autoBid;
}

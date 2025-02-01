package com.seyitkoc.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoProductIU {

    @NotBlank(message = "Product name is required!")
    @Size(min = 3, max = 50, message = "Product name must be between 3 and 50 characters!")
    private String productName;

    @NotBlank(message = "Description is required!")
    @Size(min = 3, max = 300, message = "Description must be between 3 and 300 characters!")
    private String description;

    @NotNull(message = "Price is required!")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid decimal number with up to 10 integer digits and 2 fractional digits!")
    private BigDecimal price;

    @NotNull(message = "SubCategory is required!")
    private Long categoryId;
}
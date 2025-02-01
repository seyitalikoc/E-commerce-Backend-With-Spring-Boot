package com.seyitkoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoProduct {

    private Long id;

    private String productName;

    private String description;

    private BigDecimal price;

    //private DtoSubCategory subCategory;
}

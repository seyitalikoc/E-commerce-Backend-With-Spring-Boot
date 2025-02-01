package com.seyitkoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoCartItem {

    private Long id;
    private DtoProduct product;
    private int quantity;
}

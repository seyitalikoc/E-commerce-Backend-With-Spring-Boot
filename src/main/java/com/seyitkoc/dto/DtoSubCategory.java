package com.seyitkoc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DtoSubCategory {

    private int id;

    private String categoryName;

    private DtoMainCategory mainCategory;

    private List<DtoProduct> product;
}
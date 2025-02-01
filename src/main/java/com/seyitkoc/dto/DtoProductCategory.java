package com.seyitkoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoProductCategory {

    private Long id;

    private String name;

    private String slug;

    private DtoProductCategory parent;

    private List<DtoProductCategory> subCategories;

    private List<DtoProduct> products;

    private Date updatedAt;
}

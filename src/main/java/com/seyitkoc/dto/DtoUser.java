package com.seyitkoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoUser {

    private String firstName;

    private String lastName;

    private String email;

    private DtoCart cart;

    private String token;

}

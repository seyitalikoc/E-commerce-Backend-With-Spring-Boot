package com.seyitkoc.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exception<E> {

    private String hostName;

    private String path;

    private Date createTime;

    private E message;
}

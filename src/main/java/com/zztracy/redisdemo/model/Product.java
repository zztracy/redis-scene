package com.zztracy.redisdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聚划算商品实体
 *
 * @author 詹泽
 * @since 2024/12/2 10:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private Integer price;
    private String detail;
}

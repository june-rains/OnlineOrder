package com.laioffer.onlineorder.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

// default 方案是当你的这个class的name和你要mapping的数据库中的
// table name一样的时候，你可以省略@Table中的字符串

// 注意这里我们不能将id这个filed使用primitive type，因为当我们新建一个
// 对象的时候，这个id是为止的,而primitive type则没办法表示这种情况
@Table("menu_items")
public record MenuItemEntity(
        @Id Long id,
        Long restaurantId,
        String name,
        String description,
        Double price,
        String imageUrl
) {
}


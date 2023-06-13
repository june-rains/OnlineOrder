package com.laioffer.onlineorder.service;

import com.laioffer.onlineorder.entity.MenuItemEntity;
import com.laioffer.onlineorder.entity.RestaurantEntity;
import com.laioffer.onlineorder.model.MenuItemDto;
import com.laioffer.onlineorder.model.RestaurantDto;
import com.laioffer.onlineorder.repository.MenuItemRepository;
import com.laioffer.onlineorder.repository.RestaurantRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// dependency injection
@Service
public class RestaurantService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    // 因为spring的框架特性，我们可以不用新建对象，spring帮我们创建好了
    // 然后注入到Service中构造函数中
    public RestaurantService(
            MenuItemRepository menuItemRepository,
            RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Cacheable("restaurants")
    public List<RestaurantDto> getRestaurants(){
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        List<MenuItemEntity> menuItemEntities = menuItemRepository.findAll();
        Map<Long, List<MenuItemDto>> groupMenuItems = new HashMap<>();
        for(MenuItemEntity menuItemEntity : menuItemEntities) {
            List<MenuItemDto> group = groupMenuItems.computeIfAbsent(menuItemEntity.restaurantId(), k->new ArrayList<>());
            MenuItemDto menuItemDto = new MenuItemDto(menuItemEntity);
            group.add(menuItemDto);
        }

        List<RestaurantDto> results =  new ArrayList<>();
        for(RestaurantEntity restaurantEntity : restaurantEntities) {
            RestaurantDto restaurantDto = new RestaurantDto(restaurantEntity, groupMenuItems.get(restaurantEntity.id()));
            results.add(restaurantDto);
        }
        return results;
    }
}

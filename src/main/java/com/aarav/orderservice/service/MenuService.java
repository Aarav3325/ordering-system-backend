package com.aarav.orderservice.service;

import com.aarav.orderservice.dto.response.CategoryResponse;
import com.aarav.orderservice.dto.response.MenuItemResponse;
import com.aarav.orderservice.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuService {
    List<CategoryResponse> getAllCategories();

    Page<MenuItemResponse> getMenuItemByCategory(Long categoryId, Pageable pageable);
}

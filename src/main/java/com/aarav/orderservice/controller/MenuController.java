package com.aarav.orderservice.controller;

import com.aarav.orderservice.dto.response.CategoryResponse;
import com.aarav.orderservice.dto.response.MenuItemResponse;
import com.aarav.orderservice.service.MenuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getAllCategories() {
        return menuService.getAllCategories();
    }

    @GetMapping("/items")
    public Page<MenuItemResponse> getMenuItems(@RequestParam Long categoryId, Pageable pageable) {
        return menuService.getMenuItemByCategory(categoryId, pageable);
    }
}

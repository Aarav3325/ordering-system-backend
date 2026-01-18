package com.aarav.orderservice.service.impl;

import com.aarav.orderservice.dto.response.CategoryResponse;
import com.aarav.orderservice.dto.response.MenuItemResponse;
import com.aarav.orderservice.entity.Category;
import com.aarav.orderservice.entity.MenuItem;
import com.aarav.orderservice.repository.CategoryRepository;
import com.aarav.orderservice.repository.MenuItemRepository;
import com.aarav.orderservice.service.MenuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MenuServiceImpl implements MenuService {

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuServiceImpl(CategoryRepository categoryRepository, MenuItemRepository menuItemRepository) {
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream().
                map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();

    }

    @Override
    public Page<MenuItemResponse> getMenuItemByCategory(Long categoryId, Pageable pageable) {

        Page<MenuItem> page = menuItemRepository.findByCategoryIdAndAvailableTrue(categoryId, pageable);

        return page.map(
                item -> new MenuItemResponse(
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        item.isAvailable()
                )
        );
    }
}

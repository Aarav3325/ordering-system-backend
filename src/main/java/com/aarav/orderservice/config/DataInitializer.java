package com.aarav.orderservice.config;

import com.aarav.orderservice.entity.Category;
import com.aarav.orderservice.entity.MenuItem;
import com.aarav.orderservice.repository.CategoryRepository;
import com.aarav.orderservice.repository.MenuItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;

    public DataInitializer(CategoryRepository categoryRepository, MenuItemRepository menuItemRepository) {
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() > 0) return;

        Category pizza = categoryRepository.save(
                new Category("Pizza")
        );

        Category burgers = categoryRepository.save(
                new Category("Burgers")
        );

        Category beverages = categoryRepository.save(
                new Category("Beverages")
        );

        List<MenuItem> items = List.of(
                new MenuItem(
                        "Margherita",
                        BigDecimal.valueOf(249),
                        true,
                        pizza
                ),
                new MenuItem(
                        "Farmhouse",
                        BigDecimal.valueOf(249),
                        true,
                        pizza
                ),
                new MenuItem(
                        "Veg Burger",
                        BigDecimal.valueOf(149),
                        true,
                        burgers
                ),
                new MenuItem(
                        "Coke",
                        BigDecimal.valueOf(49),
                        true,
                        beverages
                )
        );

        menuItemRepository.saveAll(items);
    }
}

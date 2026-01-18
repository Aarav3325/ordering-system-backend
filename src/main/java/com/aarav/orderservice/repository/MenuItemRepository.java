package com.aarav.orderservice.repository;

import com.aarav.orderservice.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    Page<MenuItem> findByCategoryIdAndAvailableTrue(Long categoryId, Pageable pageable);
}

package com.php.design_patten_demo.repository;

import com.php.design_patten_demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory(String category);
    List<Product> findByStockLessThan(Integer threshold);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.author LIKE %:keyword% OR p.publisher LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);
}

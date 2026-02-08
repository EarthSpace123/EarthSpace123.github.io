package com.php.design_patten_demo.service;

import com.php.design_patten_demo.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    List<Product> search(String keyword);
    List<Product> findByCategory(String category);
    List<Product> findLowStockProducts(Integer threshold);
    Product save(Product product);
    void deleteById(Long id);
    void updateStock(Long productId, Integer quantity);
}

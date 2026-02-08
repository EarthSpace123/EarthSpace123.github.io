package com.php.design_patten_demo.service.impl;

import com.php.design_patten_demo.entity.Product;
import com.php.design_patten_demo.repository.ProductRepository;
import com.php.design_patten_demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> search(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    @Override
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> findLowStockProducts(Integer threshold) {
        return productRepository.findByStockLessThan(threshold);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateStock(Long productId, Integer quantity) {
        Product product = findById(productId);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
            productRepository.save(product);
        }
    }
}

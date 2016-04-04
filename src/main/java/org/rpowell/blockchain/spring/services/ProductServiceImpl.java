package org.rpowell.blockchain.spring.services;

import org.rpowell.blockchain.domain.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public Iterable<Product> listAllProducts() {
        return null;
    }

    @Override
    public Product getProductById(Integer id) {
        return null;
    }

    @Override
    public Product saveProduct(Product product) {
        return null;
    }
}

package org.rpowell.blockchain.spring.services;

import org.rpowell.blockchain.domain.Product;

public interface ProductService {
    Iterable<Product> listAllProducts();

    Product getProductById(Integer id);

    Product saveProduct(Product product);
}

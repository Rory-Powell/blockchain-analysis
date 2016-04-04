package org.rpowell.blockchain.spring.services;

import org.rpowell.blockchain.domain.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public Iterable<Product> listAllProducts() {
        Product product = new Product();
        product.setDescription("Description");
        product.setId(10);
        product.setVersion(1);
        product.setPrice(BigDecimal.ONE);
        product.setProductId("10");
//        product.setImageUrl();

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        return productList;
    }

    @Override
    public Product getProductById(Integer id) {

        Product product = new Product();
        product.setDescription("Description");
        product.setId(10);
        product.setVersion(1);
        product.setPrice(BigDecimal.ONE);
        product.setProductId("10");

        return product;
    }

    @Override
    public Product saveProduct(Product product) {
        return null;
    }
}

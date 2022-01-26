package com.example.ordermodule.controller;


import com.example.ordermodule.entity.Product;
import com.example.ordermodule.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductRepo productRepo;
//
//    @RequestMapping(method = RequestMethod.GET)
//    public List<Product> getAll(){
//        return productRepo.findAll();
//    }
}

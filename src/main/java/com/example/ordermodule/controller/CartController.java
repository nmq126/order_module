package com.example.ordermodule.controller;

import com.example.ordermodule.entity.Cart;
import com.example.ordermodule.entity.Product;
import com.example.ordermodule.repo.ProductRepo;
import com.example.ordermodule.response.RESTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("cart")
public class CartController {

    public static HashMap<Long, Cart> cartHashMap = new HashMap<>();

    @Autowired
    ProductRepo productRepo;

    @RequestMapping(method = RequestMethod.POST, path = "add")
    public ResponseEntity addToCart(@RequestBody Cart cartItem) {
        Cart cart = cartHashMap.putIfAbsent(cartItem.getProductId(), cartItem);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + 1);
        }
        return new ResponseEntity<>(new RESTResponse.Success()
                .addData(cartHashMap)
                .build(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE, path = "clear")
    public ResponseEntity clear() {
        cartHashMap.clear();
        return new ResponseEntity<>(new RESTResponse.Success()
                .build(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "detail")
    public ResponseEntity get() {
        return new ResponseEntity<>(new RESTResponse.Success()
                .addData(cartHashMap)
                .build(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "update")
    public ResponseEntity update(@RequestParam(name = "productId") int productId,
                                 @RequestParam(name = "quantity") int quantity
    ) {
        Product product = productRepo.findById((long) productId).orElse(null);

        Cart cart = cartHashMap.get(Long.valueOf(productId));
        if (cart == null || product == null || quantity < 1) {
            return new ResponseEntity<>(new RESTResponse.SimpleError()
                    .build(), HttpStatus.BAD_REQUEST);
        }
        cart.setQuantity(quantity);
        return new ResponseEntity<>(new RESTResponse.Success()
                .addData(cartHashMap)
                .build(), HttpStatus.OK);
    }


}

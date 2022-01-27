package com.example.ordermodule.controller;

import com.example.ordermodule.entity.CartItem;
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
@CrossOrigin("*")
public class CartController {

    public static HashMap<Long, CartItem> cartHashMap = new HashMap<>();

    @Autowired
    ProductRepo productRepo;

    @RequestMapping(method = RequestMethod.POST, path = "add")
    public ResponseEntity addToCart(@RequestParam(name = "productId") int productId) {
        CartItem cartItem = new CartItem();
        Product product = productRepo.findById((long) productId).orElse(null);
        if (product == null) {
            return new ResponseEntity<>(new RESTResponse.SimpleError()
                    .build(), HttpStatus.OK);
        }
        cartItem.setQuantity(1);
        cartItem.setProductId(product.getId());
        cartItem.setName(product.getName());
        cartItem.setUnitPrice(product.getPrice());

        CartItem cart = cartHashMap.putIfAbsent((long) productId, cartItem);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + 1);
        }
        return new ResponseEntity<>(new RESTResponse.Success()
                .addData(cartHashMap)
                .build(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "remove")
    public ResponseEntity removeFromCart(@RequestParam(name = "productId") int productId) {
        Product product = productRepo.findById((long) productId).orElse(null);

        if (!cartHashMap.containsKey((long) productId)){
            return new ResponseEntity<>(new RESTResponse.SimpleError().setMessage("Khong co san pham trong gio")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        CartItem cartItem = cartHashMap.get((long) productId);
        if (cartItem == null || product == null) {
            return new ResponseEntity<>(new RESTResponse.SimpleError()
                    .build(), HttpStatus.BAD_REQUEST);
        }
        cartHashMap.remove((long)productId);
        return new ResponseEntity<>(new RESTResponse.Success()
                .setMessage("Remove success")
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

        CartItem cartItem = cartHashMap.get((long) productId);
        if (cartItem == null || product == null || quantity < 0) {
            return new ResponseEntity<>(new RESTResponse.SimpleError()
                    .build(), HttpStatus.BAD_REQUEST);
        }
        if (quantity == 0){
            cartHashMap.remove((long) productId);
        }else {
            cartItem.setQuantity(quantity);
        }
        return new ResponseEntity<>(new RESTResponse.Success()
                .addData(cartHashMap)
                .build(), HttpStatus.OK);
    }


}

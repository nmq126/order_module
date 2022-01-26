package com.example.ordermodule.dto;

import java.util.HashMap;

public class InventoryDto {

    private Long orderId;
    private String inventoryStatus;
    private HashMap<Long, Integer> products;
    private String device_token;

}

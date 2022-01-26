package com.example.ordermodule.service.impl;

import com.example.ordermodule.controller.CartController;
import com.example.ordermodule.entity.CartItem;
import com.example.ordermodule.entity.Order;
import com.example.ordermodule.entity.OrderDetail;
import com.example.ordermodule.enums.Status;
import com.example.ordermodule.queue.OrderEvent;
import com.example.ordermodule.repo.OrderRepository;
import com.example.ordermodule.response.RESTResponse;
import com.example.ordermodule.response.ResponseHandler;
import com.example.ordermodule.service.OrderService;
import com.example.ordermodule.specification.OrderSpecification;
import com.example.ordermodule.specification.SearchBody;
import com.example.ordermodule.specification.SearchCriteria;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static com.example.ordermodule.queue.Config.DIRECT_EXCHANGE;
import static com.example.ordermodule.queue.Config.DIRECT_SHARE_ROUTING_KEY;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartController cartController;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public ResponseEntity createOrder(Order order) {
        try {
            Order orderSave = orderRepository.save(order);
            BigDecimal totalPrice = BigDecimal.valueOf(0);
            Set<OrderDetail> orderDetailSet = new HashSet<>();
            for (CartItem cartItem : CartController.cartHashMap.values()) {
                totalPrice = totalPrice.add(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                OrderDetail orderDetail = new OrderDetail(cartItem);
                orderDetail.setOrderId(orderSave.getId());
                orderDetailSet.add(orderDetail);
            }
            order.setTotalPrice(totalPrice);
            order.setPaymentStatus(Status.PaymentStatus.PENDING.name());
            order.setOrderStatus(Status.OrderStatus.PENDING.name());
            order.setInventoryStatus(Status.InventoryStatus.PENDING.name());
            order.setOrderDetails(orderDetailSet);

            OrderEvent orderEvent = new OrderEvent(order);
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_SHARE_ROUTING_KEY, orderEvent);
            cartController.clear();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return new ResponseEntity<>(
                new RESTResponse.Success()
                        .addData(order)
                        .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity findAll(SearchBody searchBody) throws Exception {
        Specification specification = Specification.where(null);

        if (searchBody.getName() != null && searchBody.getName().length() > 0) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("name", ":", searchBody.getName())));
        }

        if (searchBody.getId() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("id", ":", searchBody.getId())));
        }

        try {
            Sort sort = Sort.by(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(searchBody.getPage() - 1, searchBody.getLimit(), sort);
            Page<Order> orders = orderRepository.findAll(specification, pageable);
            if (orders.isEmpty()) {
                return new ResponseEntity(ResponseHandler.ResponseHandlerBuilder.aResponseHandler()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withMessage("No product satisfied")
                        .build(), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(ResponseHandler.ResponseHandlerBuilder.aResponseHandler()
                    .withStatus(HttpStatus.OK.value())
                    .withMessage("Okela")
                    .withData("orders", orders)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception();
        }
    }
}

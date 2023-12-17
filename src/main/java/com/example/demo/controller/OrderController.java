package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;


    @GetMapping(value = "/allorders")
    public ModelAndView getAllOrders() {
        return new ModelAndView("allorders",
                Map.of("orderItems", orderService.getOrders()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/order")
    public ModelAndView getOrder() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            User currentUser = userService.findUserById(((User) principal).getId());
            List<Order> orders = orderService.getOrdersByIdUser(currentUser.getId());
            if (orders.isEmpty()) {
                return new ModelAndView("allorders",
                        Map.of("orderItemsError", "Список заказов пуст!"),
                        HttpStatus.OK);
            } else {
                return new ModelAndView("allorders",
                        Map.of("orderItems", orderService.getOrdersByIdUser(currentUser.getId())),
                        HttpStatus.OK);
            }
        } else {
            return new ModelAndView("catalog",
                    Map.of("error", "Ошибка поиска аккаунта пользователя!"),
                    HttpStatus.OK);
        }
    }

    @GetMapping(value = "/done")
    public ResponseEntity<OrderResponse> setOrderStatusDone(@RequestParam(value = "id") Integer id) {
        if (orderService.getOrderById(id) == null) {
            return ResponseEntity.ok(new OrderResponse("Заказ не найден", null));
        } else {
            Order order = orderService.updateOrderStatusById(id, Status.DONE);
            order.getBookItems().forEach(el -> System.out.println(el.getBook().getTitle()));
            return ResponseEntity.ok(new OrderResponse("Статус заказа изменен", order.getStatus()));
        }
    }

    @GetMapping(value = "/deleteorder")
    public ResponseEntity<OrderResponse> deleteOrder(@RequestParam(value = "id") Integer id) {
        if (orderService.getOrderById(id) == null) {
            return ResponseEntity.ok(new OrderResponse("Заказ не найден", null));
        } else {
            orderService.deleteOrderById(id);
            return ResponseEntity.ok(new OrderResponse("Заказ удален", null));
        }
    }
}

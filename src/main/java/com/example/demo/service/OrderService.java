package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Status;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Configurable
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByIdUser(Integer idUser) {
        return getOrders().stream().filter(orderItem ->
                Objects.equals(orderItem.getIdUser(), idUser)).toList();
    }

    public Order getOrderById(Integer idOrder) {
        return orderRepository.findById(idOrder).orElse(null);
    }

    public Order createOrder(Order order) {
        Order newOrder = orderRepository.save(order);
        order.getBookItems().forEach(item -> {
            item.setBookItems(newOrder);
            itemRepository.save(item);
        });
        return newOrder;
    }

    public Order updateOrderStatusById(Integer idOrder, Status newStatus) {
        Optional<Order> order = orderRepository.findById(idOrder);
        if (order.isPresent()) {
            order.get().setStatus(newStatus.getText());
            return orderRepository.save(order.get());
        }
        return null;
    }

    public void deleteOrderById(Integer id) {
        orderRepository.deleteById(id);
    }
}

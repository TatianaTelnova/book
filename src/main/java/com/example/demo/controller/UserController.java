package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.BookService;
import com.example.demo.service.ItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    BookService bookService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;

    @Autowired
    ItemService itemService;

    @GetMapping(value = "/buy")
    public ResponseEntity<ItemResponse> buyBookById(Model model, @RequestParam(value = "id") Integer id, HttpSession session) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            Item addedItem = itemService.addItemToListFromSession(session, book);
            return ResponseEntity.ok(new ItemResponse("Книга добавлена", addedItem));
        }
        return ResponseEntity.ok(new ItemResponse("Книга не найдена", null));
    }

    @GetMapping(value = "/removebook")
    public ResponseEntity<ItemResponse> removeBookById(Model model, @RequestParam(value = "id") Integer id, HttpSession session) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            itemService.removeItemFromListFromSession(session, book);
            return ResponseEntity.ok(new ItemResponse("Книга успешно удалена", null));
        }
        return ResponseEntity.ok(new ItemResponse("Книга не найдена", null));
    }

    @GetMapping(value = "/incbook")
    public ResponseEntity<ItemResponse> incrementBookCountById(Model model, @RequestParam(value = "id") Integer id, HttpSession session) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            Item updatedItem = itemService.incrementItemCountFromSession(session, book);
            if (updatedItem == null) {
                return ResponseEntity.ok(new ItemResponse("Книга удалена", null));
            } else {
                return ResponseEntity.ok(new ItemResponse("Количство изменено", updatedItem));
            }
        }
        return ResponseEntity.ok(new ItemResponse("Книга не найдена", null));
    }

    @GetMapping(value = "/decbook")
    public ResponseEntity<ItemResponse> decBookById(Model model, @RequestParam(value = "id") Integer id, HttpSession session) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            Item updatedItem = itemService.decrementItemCountFromSession(session, book);
            if (updatedItem == null) {
                return ResponseEntity.ok(new ItemResponse("Книга удалена", null));
            } else {
                return ResponseEntity.ok(new ItemResponse("Количство изменено", updatedItem));
            }
        }
        return ResponseEntity.ok(new ItemResponse("Книга не найдена", null));
    }

    @GetMapping(value = "/userorder")
    public ModelAndView getBookById(Model model, HttpSession session) {
        List<Book> books = bookService.getBooks();
        List<Item> itemList = itemService.getItemListFromSession(session);
        if (itemList == null || itemList.size() == 0) {
            return new ModelAndView("userorder",
                    Map.of("userOrderError", "Корзина пуста!"),
                    HttpStatus.OK);
        } else {
            return new ModelAndView("userorder",
                    Map.of("userOrder", itemService.check(itemList, books)),
                    HttpStatus.OK);
        }
    }

    @PostMapping("/confirm")
    public String confirmUserOrder(@ModelAttribute("phone") String phone, Model model, HttpSession session) {
        List<Book> books = bookService.getBooks();
        List<Item> itemList = itemService.getItemListFromSession(session);
        List<Item> checkedItemList = itemService.check(itemList, books);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            User currentUser = userService.findUserById(((User) principal).getId());
            Order order = new Order();
            order.setIdUser(currentUser.getId());
            order.setBookItems(checkedItemList);
            order.setAmount(checkedItemList.stream().mapToInt(item -> item.getBook().getPrice() * item.getCount()).sum());
            order.setPhone(phone);
            order.setStatus(Status.IN_PROGRESS.getText());
            orderService.createOrder(order);
            itemService.clearItemListFromSession(session);
            return "redirect:/userorder";
        } else {
            model.addAttribute("error", "Ошибка поиска аккаунта пользователя!");
            return "confirm";
        }
    }
}

package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.entity.OrderResponse;
import com.example.demo.entity.User;
import com.example.demo.service.BookService;
import com.example.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    ItemService itemService;

    @GetMapping(value = "/catalog")
    public ModelAndView getBooks(HttpSession session) {
        List<Book> books = bookService.getBooks();
        return new ModelAndView("catalog",
                Map.of("booksCatalog", itemService.getItemListOnBookList(session, books)),
                HttpStatus.OK);
    }


    @GetMapping(value = "/book")
    public ModelAndView getBookById(Model model, @RequestParam(value = "id") Integer id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            book = new Book();
            book.setIdBook(0);
            book.setTitle("Информация не найдена");
            book.setAuthor(null);
            book.setPrice(null);
        }
        return new ModelAndView("book",
                Map.of("bookItem", book),
                HttpStatus.OK);
    }

    @GetMapping(value = "/getbook")
    public ResponseEntity<List<Book>> getgetBookById() {
        List<Book> books = bookService.getBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping(value = "/delete")
    public ResponseEntity<OrderResponse> deleteBook(@RequestParam(value = "id") Integer id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok(new OrderResponse("Книга успешно удалена", null));
    }

    @GetMapping(value = "/createbook")
    public String createBook(Model model) {
        model.addAttribute("newBook", new Book());
        return "createbook";
    }

    @PostMapping("/createbook")
    public String productSave(@ModelAttribute("newBook") Book newBook, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        System.out.println(newBook.getTitle());
        if (bindingResult.hasErrors()) {
            return "createBook";
        }
        bookService.createBook(newBook);
        return "redirect:/catalog";
    }
}

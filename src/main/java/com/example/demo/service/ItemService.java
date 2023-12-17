package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getItemListFromSession(HttpSession session) {
        return (List<Item>) session.getAttribute("cart");
    }

    public void clearItemListFromSession(HttpSession session) {
        if (getItemListFromSession(session) != null) {
            session.removeAttribute("cart");
        }
    }

    public Item addItemInNewItemListFromSession(HttpSession session, Book book) {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(book, 1));
        session.setAttribute("cart", itemList);
        return getItemFromList(itemList, book);
    }

    public Item addItemToListFromSession(HttpSession session, Book book) {
        List<Item> itemList = getItemListFromSession(session);
        if (itemList == null) {
            return addItemInNewItemListFromSession(session, book);
        } else {
            int index = getItemIndexFromList(itemList, book);
            if (index == -1) {
                itemList.add(new Item(book, 1));
            } else {
                itemList.get(index).setCount(getItemFromList(itemList, book).getCount() + 1);
            }
            setItemListFromSession(session, itemList);
            return getItemFromList(itemList, book);
        }
    }

    public void removeItemFromListFromSession(HttpSession session, Book book) {
        List<Item> itemList = getItemListFromSession(session);
        int index = getItemIndexFromList(itemList, book);
        if (index != -1) {
            itemList.remove(index);
            setItemListFromSession(session, itemList);
        }
    }

    public Item decrementItemCountFromSession(HttpSession session, Book book) {
        List<Item> itemList = getItemListFromSession(session);
        int index = getItemIndexFromList(itemList, book);
        if (index > -1) {
            int count = getItemFromList(itemList, book).getCount();
            if (count > 1) {
                itemList.get(index).setCount(count - 1);
            } else {
                itemList.remove(index);
            }
            setItemListFromSession(session, itemList);
            return getItemFromList(itemList, book);
        } else {
            return null;
        }
    }

    public Item incrementItemCountFromSession(HttpSession session, Book book) {
        List<Item> itemList = getItemListFromSession(session);
        int index = getItemIndexFromList(itemList, book);
        if (index > -1) {
            itemList.get(index).setCount(getItemFromList(itemList, book).getCount() + 1);
            setItemListFromSession(session, itemList);
            return getItemFromList(itemList, book);
        }
        return null;

    }

    public void setItemListFromSession(HttpSession session, List<Item> itemList) {
        System.out.println("After");
        printList(itemList);
        session.setAttribute("cart", itemList);
    }

    public List<Item> getItemListOnBookList(HttpSession session, List<Book> bookList) {
        List<Item> itemList = getItemListFromSession(session);
        if (itemList == null || itemList.isEmpty()) {
            return bookList.stream().map(book -> new Item(book, 0)).toList();
        } else {
            return bookList.stream().map(book -> new Item(book, getItemCountFromList(itemList, book))).toList();
        }
    }

    private void printList(List<Item> itemList) {
        for (Item it : itemList) {
            System.out.println(it.getBook().getTitle() + " " + it.getCount());
        }
    }

    private Item getItemFromList(List<Item> itemList, Book book) {
        return itemList.stream().filter(item -> item.getBook().equals(book)).findFirst().orElse(null);
    }

    private int getItemIndexFromList(List<Item> itemList, Book book) {
        Optional<Item> findItem = itemList.stream().filter(item -> item.getBook().equals(book)).findFirst();
        return findItem.map(itemList::indexOf).orElse(-1);
    }

    private int getItemCountFromList(List<Item> itemList, Book book) {
        Optional<Item> findItem = itemList.stream().filter(item -> item.getBook().equals(book)).findFirst();
        return findItem.map(Item::getCount).orElse(0);
    }

    public List<Item> check(List<Item> itemList, List<Book> bookList) {
        return itemList.stream().filter(elem -> bookList.contains(elem.getBook())).toList();
    }
}

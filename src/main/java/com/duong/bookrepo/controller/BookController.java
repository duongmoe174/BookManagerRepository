package com.duong.bookrepo.controller;

import com.duong.bookrepo.model.Book;
import com.duong.bookrepo.model.BookForm;
import com.duong.bookrepo.model.Category;
import com.duong.bookrepo.service.book.IBookService;
import com.duong.bookrepo.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class BookController {
    @Value("${file-upload}")
    private String fileUpload;
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IBookService bookService;

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @GetMapping("/create-book")
    public ModelAndView showCreateFormBook() {
        ModelAndView modelAndView = new ModelAndView("book/create");
        modelAndView.addObject("book", new Book());
        return modelAndView;
    }

    @PostMapping("/create-book")
    public ModelAndView createNewBook(@Valid @ModelAttribute("book") BookForm bookForm, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("book/create");
            return modelAndView;
        } else {
            //lấy file ảnh:
            MultipartFile file = bookForm.getImage();
            //lấy tên file:
            String fileName = file.getOriginalFilename();
            //lấy thông tin của book:
            String name = bookForm.getName();
            String author = bookForm.getAuthor();
            int price = bookForm.getPrice();
            Category category = bookForm.getCategory();

            Book book = new Book(name, price, author, category, fileName);

            try {
                FileCopyUtils.copy(file.getBytes(), new File(fileUpload + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            bookService.save(book);
            ModelAndView modelAndView = new ModelAndView("book/create");
            modelAndView.addObject("book", book);
            modelAndView.addObject("message", "new book created!");
            return modelAndView;
        }
    }

    @GetMapping("/books")
    public ModelAndView listBook(@RequestParam("search") Optional<String> search ){
        Iterable<Book> books;
                if(search.isPresent()) {
                    books = bookService.findBooksByName(search.get());
                    ModelAndView modelAndView = new ModelAndView("book/list");
                    modelAndView.addObject("books", books);
                    return modelAndView;
                }
                else {
                    books = bookService.findAll();
                    ModelAndView modelAndView = new ModelAndView("book/list");
                    modelAndView.addObject("books", books);
                    return modelAndView;
                }
    }

    @GetMapping("/edit-book/{id}")
    public ModelAndView showEditFormBook(@PathVariable Long id) {
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("book/edit");
            modelAndView.addObject("book", book.get());
            return modelAndView;

        } else {
            ModelAndView modelAndView = new ModelAndView("error");
            return modelAndView;
        }
    }

    @PostMapping("/edit-book")
    public ModelAndView editBook(@ModelAttribute("book") Book book) {
        bookService.save(book);
        ModelAndView modelAndView = new ModelAndView("book/edit");
        modelAndView.addObject("book", book);
        modelAndView.addObject("message", "Book updated!");
        return modelAndView;
    }

    @GetMapping("/delete-book/{id}")
    public ModelAndView showFormDeleteBook(@PathVariable Long id) {
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("book/delete");
            modelAndView.addObject("book", book.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("error");
            return modelAndView;
        }
    }

    @PostMapping("/delete-book")
    public String deleteBook(@ModelAttribute("book") Book book) {
        bookService.remove(book.getId());
        return "redirect:books";
    }
}

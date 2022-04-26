package com.duong.bookrepo.service.book;

import com.duong.bookrepo.model.Book;
import com.duong.bookrepo.service.IGeneralService;

public interface IBookService extends IGeneralService<Book> {
    Iterable<Book> findBooksByName (String name);
}

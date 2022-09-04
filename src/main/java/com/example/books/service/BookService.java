package com.example.books.service;

import com.example.books.model.Book;
import com.example.books.model.Category;
import com.example.books.model.Language;
import com.example.books.repository.BookRepository;
import com.example.books.repository.CategoryRepository;
import com.example.books.repository.LanguageRepository;
import com.example.books.responses.ManyObjectResponse;
import com.example.books.responses.OneObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class BookService {
    @Autowired private BookRepository bookRepo;
    @Autowired private LanguageRepository languageRepo;
    @Autowired private CategoryRepository categoryRepo;

    @Autowired private AuthenticationService authService;

    public ManyObjectResponse getAll(Integer pageNumber, Integer pageSize, HttpSession session){
        Long totalCount = bookRepo.count();

        if(!authService.isUserLogged(session))
            return new ManyObjectResponse(0l, HttpStatus.UNAUTHORIZED,"Please login to get access to this route", null);

        pageNumber--;
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Book> pagedResult = bookRepo.findAll( paging);

        pageNumber++;
        if(pagedResult.hasContent()) {
            return new ManyObjectResponse(totalCount, HttpStatus.OK,"This is data of all books on page " + pageNumber, pagedResult.getContent());
        } else {
            return new ManyObjectResponse(totalCount, HttpStatus.OK,"This is data of all books on page " + pageNumber, new ArrayList<Book>());
        }
    }

    public OneObjectResponse getOne(Long id, HttpSession session){
        if(!authService.isUserLogged(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "Please login to get access to this route", null);

        if(bookRepo.findById(id).isEmpty())
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Book with this ID doesn't exists", null);

        Book book = bookRepo.findById(id).get();

        return new OneObjectResponse(HttpStatus.OK, "This is data of book with ID " + id, book);

    }

    public OneObjectResponse deleteOne(Long id, HttpSession session){
        if(!authService.isAdmin(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "You don't have permission to this route", null);

        try{
            bookRepo.deleteById(id);
            return new OneObjectResponse(HttpStatus.NO_CONTENT, "Book with ID " + id + " successfully deleted", null);
        } catch (EmptyResultDataAccessException ex) {
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Book with ID " + id + " doesn't exists", null);
        }
    }

    public OneObjectResponse createOne(Book book, HttpSession session){
        if(!authService.isAdmin(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "You don't have permission to this route", null);

        //Get language specified by user from database
        Optional<Language> lang = languageRepo.findByName(book.getLanguage().getName());
        Language newLang;
        //if language not exists we create new language in database
        if(lang.isEmpty()){
            newLang = book.getLanguage();
            languageRepo.save(newLang);
        }else{
            newLang = lang.get();
        }
        book.setLanguage(newLang);

        //Get category specified by user from database
        Optional<Category> category = categoryRepo.findByName(book.getCategory().getName());
        Category newCategory;
        //if category not exists we create new category in database
        if(category.isEmpty()){
            newCategory = book.getCategory();
            categoryRepo.save(newCategory);
        }else{
            newCategory = category.get();
        }
        book.setCategory(newCategory);

        Book savedBook = bookRepo.save(book);
        return new OneObjectResponse(HttpStatus.CREATED, "New book successfully created", savedBook);
    }

    public OneObjectResponse updateOne(Long id, Book book, HttpSession session){
        if(!authService.isAdmin(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "You don't have permission to this route", null);

        if(bookRepo.findById(id).isEmpty())
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Book with this ID doesn't exists", null);

        Optional<Language> lang = languageRepo.findByName(book.getLanguage().getName());
        Language newLang;
        if(lang.isEmpty()){
            newLang = book.getLanguage();
            languageRepo.save(newLang);
        }else{
            newLang = lang.get();
        }
        book.setLanguage(newLang);

        Optional<Category> category = categoryRepo.findByName(book.getCategory().getName());
        Category newCategory;
        if(category.isEmpty()){
            newCategory = book.getCategory();
            categoryRepo.save(newCategory);
        }else{
            newCategory = category.get();
        }
        book.setCategory(newCategory);

        Book editedBook = bookRepo.findById(id).get();

        if(book.getTitle() != null) editedBook.setTitle(book.getTitle());
        if(book.getDescription() != null) editedBook.setDescription(book.getDescription());
        if(book.getCategory() != null) editedBook.setCategory(book.getCategory());
        if(book.getAuthor() != null) editedBook.setAuthor(book.getAuthor());
        if(book.getPages() != null) editedBook.setPages(book.getPages());
        if(book.getPremiereDate() != null) editedBook.setPremiereDate(book.getPremiereDate());
        if(book.getLanguage() != null) editedBook.setLanguage(book.getLanguage());

        bookRepo.save(editedBook);

        return new OneObjectResponse(HttpStatus.OK, "Book with ID " + id + " successfully updated", editedBook);

    }
}

package com.example.books.controller;

import com.example.books.model.Book;
import com.example.books.responses.ManyObjectResponse;
import com.example.books.responses.OneObjectResponse;
import com.example.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BookController {
    @Autowired private BookService service;

    @GetMapping("/api/books")
    @ResponseBody
    public ResponseEntity<ManyObjectResponse> getBooks(HttpServletRequest request,
                                                           @RequestParam(defaultValue = "1") Integer pageNumber,
                                                           @RequestParam(defaultValue = "10") Integer pageSize){
        HttpSession session = request.getSession();
        ManyObjectResponse data = service.getAll(pageNumber,pageSize, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @GetMapping("/api/books/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> getBook(HttpServletRequest request, @PathVariable Long id){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.getOne(id, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> deleteBook(HttpServletRequest request, @PathVariable Long id){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.deleteOne(id, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @PostMapping("/api/books")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> createBook(HttpServletRequest request, @RequestBody Book book){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.createOne(book, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @PatchMapping("/api/books/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> updateBook(HttpServletRequest request, @PathVariable Long id, @RequestBody Book book){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.updateOne(id, book, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }
}

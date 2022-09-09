package com.example.books.controller;

import com.example.books.model.Category;
import com.example.books.responses.ManyObjectResponse;
import com.example.books.responses.OneObjectResponse;
import com.example.books.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class CategoryController {
    @Autowired private CategoryService service;

    @GetMapping("/api/categories")
    @ResponseBody
    public ResponseEntity<ManyObjectResponse> getCategories(HttpServletRequest request,
                                                           @RequestParam(defaultValue = "1") Integer pageNumber,
                                                           @RequestParam(defaultValue = "10") Integer pageSize){
        pageNumber--;
        HttpSession session = request.getSession();
        ManyObjectResponse data = service.getAll(pageNumber,pageSize, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @GetMapping("/api/categories/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> getCategory(HttpServletRequest request, @PathVariable Long id){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.getOne(id, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @DeleteMapping("/api/categories/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> deleteCategory(HttpServletRequest request, @PathVariable Long id){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.deleteOne(id, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @PostMapping("/api/categories")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> createCategory(HttpServletRequest request, @RequestBody Category category){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.createOne(category, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @PatchMapping("/api/categories/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> updateCategory(HttpServletRequest request, @PathVariable Long id, @RequestBody Category category){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.updateOne(id, category, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }
}

package com.example.books.controller;

import com.example.books.model.Language;
import com.example.books.responses.ManyObjectResponse;
import com.example.books.responses.OneObjectResponse;
import com.example.books.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LanguageController {
    @Autowired private LanguageService service;

    @GetMapping("/api/languages")
    @ResponseBody
    public ResponseEntity<ManyObjectResponse> getLanguages(HttpServletRequest request,
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

    @GetMapping("/api/languages/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> getLanguage(HttpServletRequest request, @PathVariable Long id){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.getOne(id, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @DeleteMapping("/api/languages/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> deleteLanguage(HttpServletRequest request, @PathVariable Long id){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.deleteOne(id, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @PostMapping("/api/languages")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> createLanguage(HttpServletRequest request, @RequestBody Language language){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.createOne(language, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @PatchMapping("/api/languages/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> updateLanguage(HttpServletRequest request, @PathVariable Long id, @RequestBody Language language){
        HttpSession session = request.getSession();
        OneObjectResponse data = service.updateOne(id, language, session);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }
}

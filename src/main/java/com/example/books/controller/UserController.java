package com.example.books.controller;

import com.example.books.model.User;
import com.example.books.responses.ManyObjectResponse;
import com.example.books.responses.OneObjectResponse;
import com.example.books.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired private UserService service;

    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<ManyObjectResponse> getUsers(@RequestParam(defaultValue = "1") Integer pageNumber,
                                                      @RequestParam(defaultValue = "10") Integer pageSize){

        ManyObjectResponse data = service.getAll(pageNumber,pageSize);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @GetMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> getUser(@PathVariable Long id){
        OneObjectResponse data = service.getOne(id);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> deleteUser(@PathVariable Long id){
        OneObjectResponse data = service.deleteOne(id);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @PostMapping("/api/users")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> createUser(@RequestBody User user){
        OneObjectResponse data = service.createOne(user);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @PatchMapping("/api/users/{id}")
    @ResponseBody
    public ResponseEntity<OneObjectResponse> updateUser(@PathVariable Long id, @RequestBody User user){
        OneObjectResponse data = service.updateOne(id, user);
        return ResponseEntity
                .status(data.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }
}

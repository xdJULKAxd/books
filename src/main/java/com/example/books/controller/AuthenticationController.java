package com.example.books.controller;

import com.example.books.requests.UserLoginRequest;
import com.example.books.requests.UserRegisterRequest;
import com.example.books.responses.UserActionResponse;
import com.example.books.responses.UserCheckResponse;
import com.example.books.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/api/users/check")
    public ResponseEntity<UserCheckResponse> checkUser(HttpServletRequest request) {
        return ResponseEntity.ok(this.authenticationService.checkUser(request));
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<UserActionResponse> loginUser(HttpServletRequest request, @RequestBody UserLoginRequest data) {
        return ResponseEntity.ok(this.authenticationService.loginUser(request, data));
    }

    @PostMapping("/api/users/logout")
    public ResponseEntity<UserActionResponse> logoutUser(HttpServletRequest request) {
        return ResponseEntity.ok(this.authenticationService.logoutUser(request));
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<UserActionResponse> registerUser(@RequestBody UserRegisterRequest data) {
        return ResponseEntity.ok(this.authenticationService.registerUser(data));
    }
}
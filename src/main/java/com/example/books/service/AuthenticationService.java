package com.example.books.service;

import com.example.books.model.User;
import com.example.books.repository.UserRepository;
import com.example.books.requests.UserLoginRequest;
import com.example.books.requests.UserRegisterRequest;
import com.example.books.responses.UserActionResponse;
import com.example.books.responses.UserCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository usersRepository;

    public boolean isAdmin(HttpSession session){
        String role = (String) session.getAttribute("ROLE");
        return role != null && role.equals("admin");
    }

    public boolean isUserLogged(HttpSession session) {
        Long loggedUserId = this.getLoggedUserId(session);
        return loggedUserId != null;
    }

    public boolean isUserLogged(HttpServletRequest request) {
        return this.isUserLogged(request.getSession());
    }

    public Long getLoggedUserId(HttpSession session) {
        return (Long) session.getAttribute("LOGGED_USER_ID");
    }

    public Long getLoggedUserId(HttpServletRequest request) {
        return this.getLoggedUserId(request.getSession());
    }

    public UserCheckResponse checkUser(HttpServletRequest request) {
        Long loggedUserId = this.getLoggedUserId(request);
        if (loggedUserId == null) {
            return new UserCheckResponse(false, "Currently, You are not logged in.", null);
        }
        Optional<User> userOptional = this.usersRepository.findById(loggedUserId);
        if (userOptional.isPresent()) {
            User userEntity = userOptional.get();
            return new UserCheckResponse(true, "Currently, You are logged in.", userEntity);
        } else {
            return new UserCheckResponse(false, "Logged in user doesn't exists.", null);
        }
    }

    public UserActionResponse loginUser(HttpServletRequest request, UserLoginRequest data) {
        HttpSession session = request.getSession();
        if (this.isUserLogged(session)) {
            return new UserActionResponse(false, "Login operation failed (You are already logged in).");
        }
        Optional<User> userOptional = this.usersRepository.findByUsernameAndPassword(data.getUsername(), data.getPassword());
        if (userOptional.isPresent()) {
            User userEntity = userOptional.get();
            session.setAttribute("LOGGED_USER_ID", userEntity.getId());
            session.setAttribute("ROLE", userEntity.getRole());
            return new UserActionResponse(true, "Login operation succeed.");
        }
        return new UserActionResponse(false, "Login operation failed (Incorrect user credentials).");
    }

    public UserActionResponse logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (this.isUserLogged(session)) {
            session.removeAttribute("LOGGED_USER_ID");
            session.removeAttribute("ROLE");
            return new UserActionResponse(true, "Logout operation succeed.");
        }
        return new UserActionResponse(false, "Logout operation failed (Currently, You are not logged in).");
    }

    public UserActionResponse registerUser(UserRegisterRequest data) {
        User userEntity = new User(null, data.getUsername(), data.getEmail(), "user", data.getPassword());
        try {
            this.usersRepository.save(userEntity);
            return new UserActionResponse(true, "Register operation succeed.");
        } catch (Throwable ex) {
            if (ex instanceof DataIntegrityViolationException) {
                return new UserActionResponse(false, "Register operation failed (username or email is used already by someone).");
            }
            return new UserActionResponse(false, "Register operation failed.");
        }
    }
}
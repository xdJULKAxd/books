package com.example.books.service;

import com.example.books.model.User;
import com.example.books.repository.UserRepository;
import com.example.books.responses.ManyObjectResponse;
import com.example.books.responses.OneObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    @Autowired private UserRepository repo;
    public ManyObjectResponse getAll(Integer pageNumber, Integer pageSize){
        Long totalCount = repo.count();
        pageNumber--;

        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<User> pagedResult = repo.findAll( paging);

        pageNumber++;
        if(pagedResult.hasContent()) {
            return new ManyObjectResponse(totalCount, HttpStatus.OK,"This is data of all users on page " + pageNumber, pagedResult.getContent());
        } else {
            return new ManyObjectResponse(totalCount, HttpStatus.OK,"This is data of all users on page " + pageNumber, new ArrayList<User>());
        }
    }

    public OneObjectResponse getOne(Long id){
        if(repo.findById(id).isEmpty())
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "User with this ID doesn't exists", null);

        User user = repo.findById(id).get();

        return new OneObjectResponse(HttpStatus.OK, "This is data of user with ID " + id, user);

    }

    public OneObjectResponse deleteOne(Long id){
        try{
            repo.deleteById(id);
            return new OneObjectResponse(HttpStatus.NO_CONTENT, "User with ID " + id + " successfully deleted", null);
        } catch (EmptyResultDataAccessException ex) {
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "User with ID " + id + " doesn't exists", null);
        }
    }

    public OneObjectResponse createOne(User user){
        User savedUser = repo.save(user);
        return new OneObjectResponse(HttpStatus.CREATED, "New user successfully created", savedUser);
    }

    public OneObjectResponse updateOne(Long id, User user){
        if(repo.findById(id).isEmpty())
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "User with this ID doesn't exists", null);

        User editedUser = repo.findById(id).get();

        if(user.getUsername() != null) editedUser.setUsername(user.getUsername());
        if(user.getEmail() != null) editedUser.setEmail(user.getEmail());
        if(user.getRole() != null) editedUser.setRole(user.getRole());
        if(user.getPassword() != null) editedUser.setPassword(user.getPassword());

        repo.save(editedUser);

        return new OneObjectResponse(HttpStatus.OK, "User with ID " + id + " successfully updated", editedUser);

    }
}

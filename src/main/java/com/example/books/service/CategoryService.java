package com.example.books.service;

import com.example.books.model.Category;
import com.example.books.repository.CategoryRepository;
import com.example.books.responses.ManyObjectResponse;
import com.example.books.responses.OneObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Service
public class CategoryService {
    @Autowired private CategoryRepository repo;
    @Autowired private AuthenticationService authService;

    public ManyObjectResponse getAll(Integer pageNumber, Integer pageSize, HttpSession session){
        Long totalCount = repo.count();

        if(!authService.isUserLogged(session))
            return new ManyObjectResponse(0l, HttpStatus.UNAUTHORIZED,"Please login to get access to this route", null);

        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Category> pagedResult = repo.findAll( paging);

        pageNumber++;
        if(pagedResult.hasContent()) {
            return new ManyObjectResponse(totalCount, HttpStatus.OK,"This is data of all categories on page " + pageNumber, pagedResult.getContent());
        } else {
            return new ManyObjectResponse(totalCount, HttpStatus.OK,"This is data of all categories on page " + pageNumber, new ArrayList<Category>());
        }
    }

    public OneObjectResponse getOne(Long id, HttpSession session){
        if(!authService.isUserLogged(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "Please login to get access to this route", null);

        if(repo.findById(id).isEmpty())
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Category with this ID doesn't exists", null);

        Category category = repo.findById(id).get();

        return new OneObjectResponse(HttpStatus.OK, "This is data of category with ID " + id, category);

    }

    public OneObjectResponse deleteOne(Long id, HttpSession session){
        if(!authService.isAdmin(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "You don't have permission to this route", null);

        try{
            repo.deleteById(id);
            return new OneObjectResponse(HttpStatus.NO_CONTENT, "Category with ID " + id + " successfully deleted", null);
        } catch(DataIntegrityViolationException ex){
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Category with ID " + id + " can't be deleted before books that are connected to this category", null);
        } catch (EmptyResultDataAccessException ex) {
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Category with ID " + id + " doesn't exists", null);
        }
    }

    public OneObjectResponse createOne(Category category, HttpSession session){
        if(!authService.isAdmin(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "You don't have permission to this route", null);

        Category savedCategory = repo.save(category);
        return new OneObjectResponse(HttpStatus.CREATED, "New category successfully created", savedCategory);
    }

    public OneObjectResponse updateOne(Long id, Category category, HttpSession session){
        if(!authService.isAdmin(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "You don't have permission to this route", null);

        if(repo.findById(id).isEmpty())
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Category with this ID doesn't exists", null);

        Category editedCategory = repo.findById(id).get();

        if(category.getName() != null) editedCategory.setName(category.getName());

        repo.save(editedCategory);

        return new OneObjectResponse(HttpStatus.OK, "Category with ID " + id + " successfully updated", editedCategory);

    }
}

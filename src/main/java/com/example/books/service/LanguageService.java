package com.example.books.service;

import com.example.books.model.Language;
import com.example.books.repository.LanguageRepository;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

@Service
public class LanguageService {
    @Autowired private LanguageRepository repo;
    @Autowired private AuthenticationService authService;

    public ManyObjectResponse getAll(Integer pageNumber, Integer pageSize, HttpSession session){
        Long totalCount = repo.count();

        if(!authService.isUserLogged(session))
            return new ManyObjectResponse(0l, HttpStatus.UNAUTHORIZED,"Please login to get access to this route", null);

        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Language> pagedResult = repo.findAll( paging);

        pageNumber++;
        if(pagedResult.hasContent()) {
            return new ManyObjectResponse(totalCount, HttpStatus.OK,"This is data of all languages on page " + pageNumber, pagedResult.getContent());
        } else {
            return new ManyObjectResponse(totalCount, HttpStatus.OK,"This is data of all languages on page " + pageNumber, new ArrayList<Language>());
        }
    }

    public OneObjectResponse getOne(Long id, HttpSession session){
        if(!authService.isUserLogged(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "Please login to get access to this route", null);

        if(repo.findById(id).isEmpty())
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Language with this ID doesn't exists", null);

        Language language = repo.findById(id).get();

        return new OneObjectResponse(HttpStatus.OK, "This is data of language with ID " + id, language);

    }

    public OneObjectResponse deleteOne(Long id, HttpSession session){
        if(!authService.isAdmin(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "You don't have permission to this route", null);

        try{
            repo.deleteById(id);
            return new OneObjectResponse(HttpStatus.NO_CONTENT, "Language with ID " + id + " successfully deleted", null);
        }catch(DataIntegrityViolationException ex){
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Language with ID " + id + " can't be deleted before books that are connected to this language", null);
        }
        catch (EmptyResultDataAccessException ex) {
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Language with ID " + id + " doesn't exists", null);
        }
    }

    public OneObjectResponse createOne(Language language, HttpSession session){
        if(!authService.isAdmin(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "You don't have permission to this route", null);

        Language savedLanguage = repo.save(language);
        return new OneObjectResponse(HttpStatus.CREATED, "New language successfully created", savedLanguage);
    }

    public OneObjectResponse updateOne(Long id, Language language, HttpSession session){
        if(!authService.isAdmin(session))
            return new OneObjectResponse(HttpStatus.UNAUTHORIZED, "You don't have permission to this route", null);

        if(repo.findById(id).isEmpty())
            return new OneObjectResponse(HttpStatus.BAD_REQUEST, "Language with this ID doesn't exists", null);

        Language editedLanguage = repo.findById(id).get();

        if(language.getName() != null) editedLanguage.setName(language.getName());

        repo.save(editedLanguage);

        return new OneObjectResponse(HttpStatus.OK, "Language with ID " + id + " successfully updated", editedLanguage);

    }
}

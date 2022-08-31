package com.example.books.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManyObjectResponse {
    public Long totalCount;
    @JsonIgnore
    private HttpStatus status;
    private String message;
    private List<?> data;
}
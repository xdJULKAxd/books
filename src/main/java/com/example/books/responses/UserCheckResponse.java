package com.example.books.responses;

import com.example.books.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckResponse {

	private boolean success;
	private String message;

	private User user;
}
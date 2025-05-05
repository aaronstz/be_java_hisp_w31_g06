package com.mercadolibre.socialmeli.exception;

import org.springframework.validation.Errors;

public class ConflictException extends  RuntimeException{
    public ConflictException(String message){ super(message);}
}

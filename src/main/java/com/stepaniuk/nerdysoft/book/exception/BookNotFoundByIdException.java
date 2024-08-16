package com.stepaniuk.nerdysoft.book.exception;

import lombok.Getter;

@Getter
public class BookNotFoundByIdException extends RuntimeException{
    private final Long id;

    public BookNotFoundByIdException(Long id) {
        super("Book not found by id: " + id);
        this.id = id;
    }
}

package com.stepaniuk.nerdysoft.book.exception;

import lombok.Getter;

@Getter
public class BookNotAvailableException extends Throwable {
    private final Long bookId;

    public BookNotAvailableException(Long bookId) {
        super("Book with id " + bookId + " is not available");
        this.bookId = bookId;
    }
}

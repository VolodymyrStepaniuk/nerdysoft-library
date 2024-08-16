package com.stepaniuk.nerdysoft.book.exception;

import lombok.Getter;

@Getter
public class BorrowedBookCannotBeDeletedException extends RuntimeException {
    private final Long id;

    public BorrowedBookCannotBeDeletedException(Long id) {
        super("Borrowed book with id " + id + " cannot be deleted");
        this.id = id;
    }
}

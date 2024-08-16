package com.stepaniuk.nerdysoft.member.exception;

import lombok.Getter;

@Getter
public class MemberHasBorrowedBooksException extends RuntimeException {
    private final Long id;

    public MemberHasBorrowedBooksException(Long id) {
        super("Member with id " + id + " has borrowed books");
        this.id = id;
    }
}

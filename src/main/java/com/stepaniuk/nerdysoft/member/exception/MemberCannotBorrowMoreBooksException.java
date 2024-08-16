package com.stepaniuk.nerdysoft.member.exception;

import lombok.Getter;

@Getter
public class MemberCannotBorrowMoreBooksException extends RuntimeException {

    private final Long memberId;

    public MemberCannotBorrowMoreBooksException(Long memberId) {
        super("Member with id " + memberId + " cannot borrow more books");
        this.memberId = memberId;
    }
}

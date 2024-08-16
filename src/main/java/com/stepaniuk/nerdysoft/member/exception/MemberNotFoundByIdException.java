package com.stepaniuk.nerdysoft.member.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundByIdException extends RuntimeException{
    private final Long id;

    public MemberNotFoundByIdException(Long id) {
        super("Member not found by id: " + id);
        this.id = id;
    }
}

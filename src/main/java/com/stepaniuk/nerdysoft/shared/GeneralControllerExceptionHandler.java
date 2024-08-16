package com.stepaniuk.nerdysoft.shared;

import com.stepaniuk.nerdysoft.book.exception.BookNotAvailableException;
import com.stepaniuk.nerdysoft.book.exception.BookNotFoundByIdException;
import com.stepaniuk.nerdysoft.book.exception.BorrowedBookCannotBeDeletedException;
import com.stepaniuk.nerdysoft.member.exception.MemberCannotBorrowMoreBooksException;
import com.stepaniuk.nerdysoft.member.exception.MemberHasBorrowedBooksException;
import com.stepaniuk.nerdysoft.member.exception.MemberNotFoundByIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class GeneralControllerExceptionHandler {

    @ExceptionHandler(value = {BookNotAvailableException.class})
    public ProblemDetail handleBookNotAvailableException(BookNotAvailableException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "Book with id " + e.getBookId() + " is not available");
        problemDetail.setTitle("Book not available");
        problemDetail.setInstance(URI.create("/books/" + e.getBookId()));
        return problemDetail;
    }

    @ExceptionHandler(value = {BookNotFoundByIdException.class})
    public ProblemDetail handleBookNotFoundByIdException(BookNotFoundByIdException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                "Book with id " + e.getId() + " not found");
        problemDetail.setTitle("Book not found");
        problemDetail.setInstance(URI.create("/books/" + e.getId()));
        return problemDetail;
    }

    @ExceptionHandler(value = {BorrowedBookCannotBeDeletedException.class})
    public ProblemDetail handleBorrowedBookCannotBeDeletedException(BorrowedBookCannotBeDeletedException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "Borrowed book with id " + e.getId() + " cannot be deleted");
        problemDetail.setTitle("Borrowed book cannot be deleted");
        problemDetail.setInstance(URI.create("/books/" + e.getId()));
        return problemDetail;
    }

    @ExceptionHandler(value = {MemberCannotBorrowMoreBooksException.class})
    public ProblemDetail handleMemberCannotBorrowMoreBooksException(MemberCannotBorrowMoreBooksException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "Member with id " + e.getMemberId() + " cannot borrow more books");
        problemDetail.setTitle("Member cannot borrow more books");
        problemDetail.setInstance(URI.create("/members/" + e.getMemberId()));
        return problemDetail;
    }

    @ExceptionHandler(value = {MemberHasBorrowedBooksException.class})
    public ProblemDetail handleMemberHasBorrowedBooksException(MemberHasBorrowedBooksException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                "Member with id " + e.getId() + " has borrowed books");
        problemDetail.setTitle("Member has borrowed books");
        problemDetail.setInstance(URI.create("/members/" + e.getId()));
        return problemDetail;
    }

    @ExceptionHandler(value = {MemberNotFoundByIdException.class})
    public ProblemDetail handleMemberNotFoundByIdException(MemberNotFoundByIdException e) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                "Member with id " + e.getId() + " not found");
        problemDetail.setTitle("Member not found");
        problemDetail.setInstance(URI.create("/members/" + e.getId()));
        return problemDetail;
    }
}

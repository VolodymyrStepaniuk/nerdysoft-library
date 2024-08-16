package com.stepaniuk.nerdysoft.library;

import com.stepaniuk.nerdysoft.book.Book;
import com.stepaniuk.nerdysoft.book.BookRepository;
import com.stepaniuk.nerdysoft.book.borrowed.BorrowedBook;
import com.stepaniuk.nerdysoft.book.borrowed.BorrowedBookRepository;
import com.stepaniuk.nerdysoft.book.exception.BookNotAvailableException;
import com.stepaniuk.nerdysoft.book.exception.BookNotFoundByIdException;
import com.stepaniuk.nerdysoft.member.Member;
import com.stepaniuk.nerdysoft.member.MemberRepository;
import com.stepaniuk.nerdysoft.member.exception.MemberCannotBorrowMoreBooksException;
import com.stepaniuk.nerdysoft.member.exception.MemberNotFoundByIdException;
import com.stepaniuk.nerdysoft.testspecific.ServiceLevelUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ServiceLevelUnitTest
@ContextConfiguration(classes = {LibraryService.class})
@SpringBootTest
public class LibraryServiceTest {
    @Autowired
    private LibraryService libraryService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BorrowedBookRepository borrowedBookRepository;

    @MockBean
    private MemberRepository memberRepository;

    @Test
    void shouldThrowBookNotFoundWhenBorrowingNonExistingBook() {

        var memberId = 1L;
        var bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundByIdException.class, () -> libraryService.borrowBook(bookId, memberId));
    }

    @Test
    void shouldThrowBookNotAvailableWhenBorrowingBookWithAmountZero() {

        var memberId = 1L;
        var bookId = 1L;

        var book = new Book();
        book.setAmount(0);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(BookNotAvailableException.class, () -> libraryService.borrowBook(bookId, memberId));
    }

    @Test
    void shouldThrowMemberNotFoundWhenBorrowingWithNonExistingMember() {

        var memberId = 1L;
        var bookId = 1L;

        var book = new Book();
        book.setAmount(1);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundByIdException.class, () -> libraryService.borrowBook(bookId, memberId));
    }

    @Test
    void shouldThrowMemberCannotBorrowMoreBooksExceptionWhenBorrowingMoreThanLimit() {

        var memberId = 1L;
        var bookId = 1L;

        var book = new Book();
        book.setAmount(1);

        var member = new Member();
        member.setId(memberId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(borrowedBookRepository.findByMemberIdAndReturnedDateIsNull(memberId)).thenReturn(
                java.util.List.of(new BorrowedBook(), new BorrowedBook(), new BorrowedBook(),
                        new BorrowedBook(), new BorrowedBook(), new BorrowedBook(),
                        new BorrowedBook(), new BorrowedBook(), new BorrowedBook(),
                        new BorrowedBook()
        ));

        assertThrows(MemberCannotBorrowMoreBooksException.class, () -> libraryService.borrowBook(bookId, memberId));
    }

    @Test
    void shouldBorrowBookAndChangeAmountOfBooks() throws BookNotAvailableException {
        var memberId = 1L;
        var bookId = 1L;

        var book = new Book();
        book.setAmount(1);

        var member = new Member();
        member.setId(memberId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(borrowedBookRepository.findByMemberIdAndReturnedDateIsNull(memberId)).thenReturn(
                    java.util.List.of(new BorrowedBook()));

        libraryService.borrowBook(bookId, memberId);

        verify(bookRepository).save(book);
        verify(borrowedBookRepository).save(any(BorrowedBook.class));

        assertEquals(0, book.getAmount());
    }

    @Test
    void shouldThrowBookNotFoundWhenBookWasNotBorrowed() {
        var memberId = 1L;
        var bookId = 1L;

        when(borrowedBookRepository.findByMemberIdAndBookIdAndReturnedDateIsNull(memberId, bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundByIdException.class, () -> libraryService.returnBook(bookId, memberId));
    }

    @Test
    void shouldReturnBookAndChangeAmountOfBooks() {
        var memberId = 1L;
        var bookId = 1L;

        var borrowedBook = new BorrowedBook();
        borrowedBook.setBook(new Book());
        borrowedBook.getBook().setAmount(1);

        when(borrowedBookRepository.findByMemberIdAndBookIdAndReturnedDateIsNull(memberId, bookId)).thenReturn(Optional.of(borrowedBook));

        libraryService.returnBook(bookId, memberId);

        verify(borrowedBookRepository).save(borrowedBook);
        verify(bookRepository).save(borrowedBook.getBook());

        assertEquals(2, borrowedBook.getBook().getAmount());
    }
}

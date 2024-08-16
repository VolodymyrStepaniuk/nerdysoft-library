package com.stepaniuk.nerdysoft.book.borrowed;

import com.stepaniuk.nerdysoft.book.Book;
import com.stepaniuk.nerdysoft.book.BookRepository;
import com.stepaniuk.nerdysoft.book.exception.BookNotFoundByIdException;
import com.stepaniuk.nerdysoft.member.Member;
import com.stepaniuk.nerdysoft.member.MemberRepository;
import com.stepaniuk.nerdysoft.member.exception.MemberNotFoundByIdException;
import com.stepaniuk.nerdysoft.testspecific.JpaLevelTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JpaLevelTest
@Sql(scripts = {"classpath:sql/borrowed_books.sql"})
public class BorrowedBookRepositoryTest {

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void shouldSaveBorrowedBook(){
        // given
        Book book = bookRepository.findById(1L).orElseThrow(
                () -> new BookNotFoundByIdException(1L)
        );
        Member member = memberRepository.findById(1L).orElseThrow(
                () -> new MemberNotFoundByIdException(1L)
        );

        BorrowedBook borrowedBookToSave = new BorrowedBook(null, book, member, Instant.MIN, Instant.MAX);

        // when
        BorrowedBook savedBorrowedBook = borrowedBookRepository.save(borrowedBookToSave);

        // then
        assertNotNull(savedBorrowedBook);
        assertNotNull(savedBorrowedBook.getId());
        assertNotNull(savedBorrowedBook.getBook());
        assertNotNull(savedBorrowedBook.getMember());
        assertNotNull(savedBorrowedBook.getBorrowedDate());
        assertNotNull(savedBorrowedBook.getReturnedDate());
        assertEquals(borrowedBookToSave.getBook(), savedBorrowedBook.getBook());
        assertEquals(borrowedBookToSave.getMember(), savedBorrowedBook.getMember());
    }

    @Test
    void shouldReturnBorrowedBookWhenFindById(){
        // when
        Optional<BorrowedBook> optionalBorrowedBook = borrowedBookRepository.findById(1L);

        // then
        assertTrue(optionalBorrowedBook.isPresent());
        BorrowedBook borrowedBook = optionalBorrowedBook.get();

        assertNotNull(borrowedBook);
        assertEquals(1L, borrowedBook.getId());
        assertNotNull(borrowedBook.getBook());
        assertNotNull(borrowedBook.getMember());
        assertNotNull(borrowedBook.getBorrowedDate());
        assertNotNull(borrowedBook.getReturnedDate());
    }

    @Test
    void shouldUpdateBorrowedBookWhenChangingReturnedDate(){
        // given
        var givenBorrowedBookId = 1L;
        BorrowedBook borrowedBookToUpdate = borrowedBookRepository.findById(givenBorrowedBookId).orElseThrow(
                () -> new BookNotFoundByIdException(givenBorrowedBookId)
        );

        borrowedBookToUpdate.setReturnedDate(Instant.now());

        // when
        BorrowedBook updatedBorrowedBook = borrowedBookRepository.save(borrowedBookToUpdate);

        // then
        assertNotNull(updatedBorrowedBook);
        assertEquals(borrowedBookToUpdate.getId(), updatedBorrowedBook.getId());
        assertEquals(borrowedBookToUpdate.getReturnedDate(), updatedBorrowedBook.getReturnedDate());
    }

    @Test
    void shouldUpdateBorrowedBookWhenChangingAllFields() {
        // given
        var givenBorrowedBookId = 1L;
        BorrowedBook borrowedBookToUpdate = borrowedBookRepository.findById(givenBorrowedBookId).orElseThrow(
                () -> new BookNotFoundByIdException(givenBorrowedBookId)
        );

        borrowedBookToUpdate.setBook(bookRepository.findById(2L).orElseThrow(
                () -> new BookNotFoundByIdException(2L)
        ));

        borrowedBookToUpdate.setMember(memberRepository.findById(2L).orElseThrow(
                () -> new MemberNotFoundByIdException(2L)
        ));

        borrowedBookToUpdate.setBorrowedDate(Instant.now());
        borrowedBookToUpdate.setReturnedDate(Instant.now().plus(Duration.ofDays(1)));

        // when
        BorrowedBook updatedBorrowedBook = borrowedBookRepository.save(borrowedBookToUpdate);
        assertNotNull(updatedBorrowedBook);
        assertEquals(borrowedBookToUpdate.getId(), updatedBorrowedBook.getId());
        assertEquals(borrowedBookToUpdate.getBook(), updatedBorrowedBook.getBook());
        assertEquals(borrowedBookToUpdate.getMember(), updatedBorrowedBook.getMember());
        assertEquals(borrowedBookToUpdate.getBorrowedDate(), updatedBorrowedBook.getBorrowedDate());
        assertEquals(borrowedBookToUpdate.getReturnedDate(), updatedBorrowedBook.getReturnedDate());
    }

    @Test
    void shouldDeleteBorrowedBookWhenDeletingByExistingBorrowedBook() {
        // given
        var givenBorrowedBookId = 1L;

        BorrowedBook borrowedBookToUpdate = borrowedBookRepository.findById(givenBorrowedBookId).orElseThrow(
                () -> new BookNotFoundByIdException(givenBorrowedBookId)
        );

        // when
        borrowedBookRepository.delete(borrowedBookToUpdate);

        assertTrue(borrowedBookRepository.findById(givenBorrowedBookId).isEmpty());
    }

    @Test
    void shouldDeleteBorrowedBookByIdWhenDeletingByExistingId() {
        // when
        borrowedBookRepository.deleteById(1L);

        // then
        assertTrue(borrowedBookRepository.findById(1L).isEmpty());
    }

    @Test
    void shouldReturnTrueWhenBorrowedBookExists() {
        // when
        boolean exists = borrowedBookRepository.existsById(1L);

        // then
        assertTrue(exists);
    }

    @Test
    void shouldReturnNonEmptyListWhenFindAll() {
        // when
        var borrowedBooks = borrowedBookRepository.findAll();

        // then
        assertNotNull(borrowedBooks);
        assertFalse(borrowedBooks.isEmpty());
    }
}

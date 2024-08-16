package com.stepaniuk.nerdysoft.book;

import com.stepaniuk.nerdysoft.book.exception.BookNotFoundByIdException;
import com.stepaniuk.nerdysoft.testspecific.JpaLevelTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JpaLevelTest
@Sql(scripts = {"classpath:sql/books.sql"})
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldSaveBook(){
        // given
        Book bookToSave = new Book(null, "Title", "Author", 1);

        // when
        Book savedBook = bookRepository.save(bookToSave);

        // then
        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
        assertEquals(bookToSave.getTitle(), savedBook.getTitle());
        assertEquals(bookToSave.getAuthor(), savedBook.getAuthor());
        assertEquals(bookToSave.getAmount(), savedBook.getAmount());
    }

    @Test
    void shouldReturnBookWhenFindById(){
        // when
        Optional<Book> optionalBook = bookRepository.findById(1L);

        // then
        assertTrue(optionalBook.isPresent());
        Book book = optionalBook.get();

        assertNotNull(book);
        assertEquals(1L, book.getId());
        assertEquals("The Lord of the Rings", book.getTitle());
        assertEquals("J.R.R. Tolkien", book.getAuthor());
        assertEquals(1, book.getAmount());
    }

    @Test
    void shouldUpdateBookWhenChangingTitle(){
        // given
        var givenBookId = 1L;
        Book bookToUpdate = bookRepository.findById(givenBookId).orElseThrow(
                () -> new BookNotFoundByIdException(givenBookId)
        );

        bookToUpdate.setTitle("New Title");

        // when
        Book updatedBook = bookRepository.save(bookToUpdate);

        // then
        assertNotNull(updatedBook);
        assertEquals(bookToUpdate.getId(), updatedBook.getId());
        assertEquals("New Title", updatedBook.getTitle());
    }

    @Test
    void shouldUpdateBookWhenChangingAllFields() {
        // given
        var givenBookId = 1L;
        Book bookToUpdate = bookRepository.findById(givenBookId).orElseThrow(
                () -> new BookNotFoundByIdException(givenBookId)
        );

        bookToUpdate.setTitle("New Title");
        bookToUpdate.setAuthor("New Author");
        bookToUpdate.setAmount(2);

        // when
        Book updatedBook = bookRepository.save(bookToUpdate);

        // then
        assertNotNull(updatedBook);
        assertEquals(bookToUpdate.getId(), updatedBook.getId());
        assertEquals("New Title", updatedBook.getTitle());
        assertEquals("New Author", updatedBook.getAuthor());
        assertEquals(2, updatedBook.getAmount());
    }

    @Test
    void shouldDeleteBookWhenDeletingByExistingBook() {
        // given
        var givenBookId = 1L;
        Book bookToDelete = bookRepository.findById(givenBookId).orElseThrow(
                () -> new BookNotFoundByIdException(givenBookId)
        );

        // when
        bookRepository.delete(bookToDelete);

        // then
        assertTrue(bookRepository.findById(givenBookId).isEmpty());
    }

    @Test
    void shouldDeleteBookByIdWhenDeletingByExistingId() {
        // when
        bookRepository.deleteById(1L);

        // then
        assertTrue(bookRepository.findById(1L).isEmpty());
    }

    @Test
    void shouldReturnTrueWhenBookExists() {
        // when
        boolean exists = bookRepository.existsById(1L);

        // then
        assertTrue(exists);
    }

    @Test
    void shouldReturnNonEmptyListWhenFindAll() {
        // when
        var books = bookRepository.findAll();

        // then
        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    void shouldReturnBookWhenFindByTitleAndAuthor() {
        // when
        var optionalBook = bookRepository.findByTitleAndAuthor("The Lord of the Rings", "J.R.R. Tolkien");

        // then
        assertTrue(optionalBook.isPresent());
        var book = optionalBook.get();

        assertNotNull(book);
        assertEquals(1L, book.getId());
        assertEquals("The Lord of the Rings", book.getTitle());
        assertEquals("J.R.R. Tolkien", book.getAuthor());
        assertEquals(1, book.getAmount());
    }
}

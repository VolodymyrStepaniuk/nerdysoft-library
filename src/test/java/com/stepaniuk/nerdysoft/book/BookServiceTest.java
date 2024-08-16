package com.stepaniuk.nerdysoft.book;

import com.stepaniuk.nerdysoft.book.borrowed.BorrowedBookRepository;
import com.stepaniuk.nerdysoft.book.exception.BookNotFoundByIdException;
import com.stepaniuk.nerdysoft.book.payload.BookCreateRequest;
import com.stepaniuk.nerdysoft.book.payload.BookUpdateRequest;
import com.stepaniuk.nerdysoft.testspecific.ServiceLevelUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ServiceLevelUnitTest
@ContextConfiguration(classes = {BookService.class, BookMapperImpl.class})
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BorrowedBookRepository borrowedBookRepository;

    @Test
    void shouldReturnBookResponseWhenCreatingBook(){
        // given
        var request = new BookCreateRequest("Title", "Author");

        when(bookRepository.save(any())).thenAnswer(answer(getFakeSaveAnswer(1L)));

        // when
        var bookResponse = bookService.createBook(request);

        // then
        assertNotNull(bookResponse);
        assertEquals(request.getTitle(), bookResponse.getTitle());
        assertEquals(request.getAuthor(), bookResponse.getAuthor());
        assertEquals(1, bookResponse.getAmount());
        assertTrue(bookResponse.hasLinks());
        assertTrue(bookResponse.getLinks().hasLink("self"));
        assertTrue(bookResponse.getLinks().hasLink("update"));
        assertTrue(bookResponse.getLinks().hasLink("delete"));

        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void shouldIncreaseAmountOfBookIfAddedBookAlreadyExists(){
        // given
        var request = new BookCreateRequest("Title", "Author");

        when(bookRepository.findByTitleAndAuthor(request.getTitle(), request.getAuthor()))
                .thenReturn(Optional.of(new Book(1L, "Title", "Author", 1)));

        when(bookRepository.save(any())).thenAnswer(answer(getFakeSaveAnswer(1L)));

        // when
        var bookResponse = bookService.createBook(request);

        // then
        assertNotNull(bookResponse);
        assertEquals(request.getTitle(), bookResponse.getTitle());
        assertEquals(request.getAuthor(), bookResponse.getAuthor());
        assertEquals(2, bookResponse.getAmount());
        assertTrue(bookResponse.hasLinks());
        assertTrue(bookResponse.getLinks().hasLink("self"));
        assertTrue(bookResponse.getLinks().hasLink("update"));
        assertTrue(bookResponse.getLinks().hasLink("delete"));

        verify(bookRepository, times(1)).findByTitleAndAuthor(request.getTitle(), request.getAuthor());
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void shouldReturnBookResponseWhenGettingBook(){
        // given
        var bookToFind = new Book(1L, "Title", "Author", 1);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookToFind));

        // when
        var bookResponse = bookService.getBook(1L);

        // then
        assertNotNull(bookResponse);
        assertEquals(bookToFind.getId(), bookResponse.getId());
        assertEquals(bookToFind.getTitle(), bookResponse.getTitle());
        assertEquals(bookToFind.getAuthor(), bookResponse.getAuthor());
        assertEquals(bookToFind.getAmount(), bookResponse.getAmount());
        assertTrue(bookResponse.hasLinks());
        assertTrue(bookResponse.getLinks().hasLink("self"));
        assertTrue(bookResponse.getLinks().hasLink("update"));
        assertTrue(bookResponse.getLinks().hasLink("delete"));

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowBookNotFoundByIdExceptionWhenGettingNonExistingBook(){
        // given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        var exception = assertThrows(BookNotFoundByIdException.class, () -> bookService.getBook(1L));

        // then
        assertEquals(1L, exception.getId());

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void shouldUpdateAndReturnBookResponseWhenChangingTitle() {
        // given
        var bookId = 1L;
        var bookToUpdate = new Book(1L, "Title", "Author", 1);
        var request = new BookUpdateRequest("New Title", null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookToUpdate));
        when(bookRepository.save(any())).thenAnswer(answer(getFakeSaveAnswer(bookId)));

        // when
        var bookResponse = bookService.updateBook(bookId, request);

        // then
        assertNotNull(bookResponse);
        assertEquals(bookToUpdate.getId(), bookResponse.getId());
        assertEquals(request.getTitle(), bookResponse.getTitle());
        assertEquals(bookToUpdate.getAuthor(), bookResponse.getAuthor());
        assertEquals(bookToUpdate.getAmount(), bookResponse.getAmount());
        assertTrue(bookResponse.hasLinks());
        assertTrue(bookResponse.getLinks().hasLink("self"));
        assertTrue(bookResponse.getLinks().hasLink("update"));
        assertTrue(bookResponse.getLinks().hasLink("delete"));

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowBookNotFoundByIdExceptionWhenUpdatingNonExistingBook(){
        // given
        var bookId = 1L;
        var request = new BookUpdateRequest("New Title", null);

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        var exception = assertThrows(BookNotFoundByIdException.class, () -> bookService.updateBook(bookId, request));

        // then
        assertEquals(1L, exception.getId());

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnVoidWhenDeleteBook(){
        // given
        var bookId = 1L;

        var book = new Book(1L, "Title", "Author", 1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowedBookRepository.findByBookIdAndReturnedDateIsNull(1L)).thenReturn(List.of());

        // when
        bookService.deleteBook(bookId);

        // then
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void shouldThrowBookNotFoundByIdExceptionWhenDeletingNonExistingBook(){
        // given
        var bookId = 1L;

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        var exception = assertThrows(BookNotFoundByIdException.class, () -> bookService.deleteBook(bookId));

        // then
        assertEquals(1L, exception.getId());

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnPageOfBookResponsesWhenGettingAllBooks(){
        // given
        var pageable = PageRequest.of(0, 1);
        var bookToFind = new Book(1L, "Title1", "Author1", 1);

        when(bookRepository.findAll(eq(pageable))).thenReturn(new PageImpl<>(List.of(bookToFind)));

        // when
        var booksPage = bookService.getAllBooks(pageable);

        // then
        assertNotNull(booksPage);
        assertEquals(1, booksPage.getTotalElements());
        assertEquals(1, booksPage.getTotalPages());
        assertEquals(1, booksPage.getContent().size());

        var bookResponse = booksPage.getContent().getFirst();

        assertNotNull(bookResponse);
        assertEquals(bookToFind.getId(), bookResponse.getId());
        assertEquals(bookToFind.getTitle(), bookResponse.getTitle());
        assertEquals(bookToFind.getAuthor(), bookResponse.getAuthor());
        assertEquals(bookToFind.getAmount(), bookResponse.getAmount());
        assertTrue(bookResponse.hasLinks());
        assertTrue(bookResponse.getLinks().hasLink("self"));
        assertTrue(bookResponse.getLinks().hasLink("update"));
        assertTrue(bookResponse.getLinks().hasLink("delete"));

        verify(bookRepository, times(1)).findAll(eq(pageable));
    }

    private Answer1<Book, Book> getFakeSaveAnswer(Long id) {
        return book -> {
            book.setId(id);
            return book;
        };
    }
}

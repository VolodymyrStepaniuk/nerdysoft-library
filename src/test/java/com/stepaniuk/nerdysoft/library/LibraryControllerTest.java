package com.stepaniuk.nerdysoft.library;

import com.stepaniuk.nerdysoft.book.Book;
import com.stepaniuk.nerdysoft.book.BookRepository;
import com.stepaniuk.nerdysoft.book.exception.BookNotAvailableException;
import com.stepaniuk.nerdysoft.book.exception.BookNotFoundByIdException;
import com.stepaniuk.nerdysoft.member.Member;
import com.stepaniuk.nerdysoft.member.MemberRepository;
import com.stepaniuk.nerdysoft.member.exception.MemberCannotBorrowMoreBooksException;
import com.stepaniuk.nerdysoft.member.exception.MemberNotFoundByIdException;
import com.stepaniuk.nerdysoft.testspecific.ControllerLevelUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerLevelUnitTest(controllers = LibraryController.class)
public class LibraryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private BookRepository bookRepository;

    @Test
    void shouldReturnErrorResponseWhenBorrowingNonExistingBook() throws Exception, BookNotAvailableException {
        // given
        var memberId = 1L;
        var bookId = 1L;
        // when
        doThrow(new BookNotFoundByIdException(1L)).when(libraryService).borrowBook(bookId, memberId);
        // then

        mockMvc.perform(post("/library/borrowBook")
                        .contentType("application/json")
                        .param("bookId", String.valueOf(bookId))
                        .param("memberId", String.valueOf(memberId))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Book not found")))
                .andExpect(jsonPath("$.detail", is("Book with id 1 not found")))
                .andExpect(jsonPath("$.instance", is("/books/1")));
    }

    @Test
    void shouldReturnErrorResponseWhenBorrowingBookNotAvailable() throws Exception, BookNotAvailableException {
        // given
        var memberId = 1L;
        var bookId = 1L;
        // when
        doThrow(new BookNotAvailableException(1L)).when(libraryService).borrowBook(bookId, memberId);
        // then

        mockMvc.perform(post("/library/borrowBook")
                        .contentType("application/json")
                        .param("bookId", String.valueOf(bookId))
                        .param("memberId", String.valueOf(memberId))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.title", is("Book not available")))
                .andExpect(jsonPath("$.detail", is("Book with id 1 is not available")));
    }

    @Test
    void shouldReturnErrorResponseWhenMemberNotFound()throws Exception, BookNotAvailableException {
        // given
        var memberId = 1L;
        var bookId = 1L;
        // when
        doThrow(new MemberNotFoundByIdException(1L)).when(libraryService).borrowBook(bookId, memberId);
        // then

        mockMvc.perform(post("/library/borrowBook")
                        .contentType("application/json")
                        .param("bookId", String.valueOf(bookId))
                        .param("memberId", String.valueOf(memberId))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Member not found")))
                .andExpect(jsonPath("$.detail", is("Member with id 1 not found")))
                .andExpect(jsonPath("$.instance", is("/members/1")));
    }

    @Test
    void shouldReturnErrorResponseWhenMemberCannotBorrowMoreBooks() throws Exception, BookNotAvailableException {
        // given
        var memberId = 1L;
        var bookId = 1L;
        // when
        when(memberRepository.findById(memberId)).thenReturn(java.util.Optional.of(new Member()));
        doThrow(new MemberCannotBorrowMoreBooksException(1L)).when(libraryService).borrowBook(bookId, memberId);
        // then

        mockMvc.perform(post("/library/borrowBook")
                        .contentType("application/json")
                        .param("bookId", String.valueOf(bookId))
                        .param("memberId", String.valueOf(memberId))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.title", is("Member cannot borrow more books")))
                .andExpect(jsonPath("$.detail", is("Member with id 1 cannot borrow more books")))
                .andExpect(jsonPath("$.instance", is("/members/1")));
    }

    @Test
    void shouldBorrowBookAndChangeAmountOfBooks() throws Exception, BookNotAvailableException {
        // given
        var memberId = 1L;
        var bookId = 1L;
        // when
        when(memberRepository.findById(memberId)).thenReturn(java.util.Optional.of(new Member()));
        when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(new Book()));
        doNothing().when(libraryService).borrowBook(bookId, memberId);
        // then

        mockMvc.perform(post("/library/borrowBook")
                        .contentType("application/json")
                        .param("bookId", String.valueOf(bookId))
                        .param("memberId", String.valueOf(memberId))
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnErrorResponseWhenBookWasNotBorrowed() throws Exception {
        // given
        var memberId = 1L;
        var bookId = 1L;
        // when
        doThrow(new BookNotFoundByIdException(1L)).when(libraryService).returnBook(bookId, memberId);
        // then

        mockMvc.perform(post("/library/returnBook")
                        .contentType("application/json")
                        .param("bookId", String.valueOf(bookId))
                        .param("memberId", String.valueOf(memberId))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Book not found")))
                .andExpect(jsonPath("$.detail", is("Book with id 1 not found")))
                .andExpect(jsonPath("$.instance", is("/books/1")));
    }

    @Test
    void shouldReturnBookAndChangeAmountOfBooks() throws Exception {
        // given
        var memberId = 1L;
        var bookId = 1L;
        // when
        when(memberRepository.findById(memberId)).thenReturn(java.util.Optional.of(new Member()));
        when(bookRepository.findById(bookId)).thenReturn(java.util.Optional.of(new Book()));
        doNothing().when(libraryService).returnBook(bookId, memberId);
        // then

        mockMvc.perform(post("/library/returnBook")
                        .contentType("application/json")
                        .param("bookId", String.valueOf(bookId))
                        .param("memberId", String.valueOf(memberId))
                )
                .andExpect(status().isOk());
    }
}

package com.stepaniuk.nerdysoft.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stepaniuk.nerdysoft.book.exception.BookNotFoundByIdException;
import com.stepaniuk.nerdysoft.book.payload.BookCreateRequest;
import com.stepaniuk.nerdysoft.book.payload.BookResponse;
import com.stepaniuk.nerdysoft.book.payload.BookUpdateRequest;
import com.stepaniuk.nerdysoft.testspecific.ControllerLevelUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerLevelUnitTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    void shouldReturnBookResponseWhenCreatingBook() throws Exception {
        // given
        var bookCreateRequest = new BookCreateRequest("Title", "Author Name");
        var bookResponse = new BookResponse(1L, bookCreateRequest.getTitle(), bookCreateRequest.getAuthor(), 1);
        bookResponse.add(Link.of("http://localhost/books/1", "self"));
        bookResponse.add(Link.of("http://localhost/books/1", "update"));
        bookResponse.add(Link.of("http://localhost/books/1", "delete"));

        when(bookService.createBook(bookCreateRequest)).thenReturn(bookResponse);

        // when & then
        mockMvc.perform(post("/books")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(bookCreateRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookResponse.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(bookResponse.getTitle())))
                .andExpect(jsonPath("$.author", is(bookResponse.getAuthor())))
                .andExpect(jsonPath("$.amount", is(bookResponse.getAmount())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/books/1")))
                .andExpect(jsonPath("$._links.update.href", is("http://localhost/books/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/books/1")));;
    }

    @Test
    void shouldReturnErrorWhenCreatingBookWithWrongAuthor() throws Exception {
        // given
        var bookCreateRequest = new BookCreateRequest("Title", "Author");

        // when & then
        mockMvc.perform(post("/books")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(bookCreateRequest))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnPageOfBookResponsesWhenGettingAllBooks() throws Exception {
        // given
        var response = new BookResponse(1L, "Title", "New Author", 1);
        response.add(Link.of("http://localhost/books/1", "self"));
        response.add(Link.of("http://localhost/books/1", "update"));
        response.add(Link.of("http://localhost/books/1", "delete"));
        // when
        when(bookService.getAllBooks(PageRequest.of(0,1))).thenReturn(new PageImpl<>(List.of(response)));
        // then

        mockMvc.perform(get("/books")
                        .contentType("application/json")
                        .param("page", "0")
                        .param("size", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.content[0].title", is(response.getTitle())))
                .andExpect(jsonPath("$.content[0].author", is(response.getAuthor())))
                .andExpect(jsonPath("$.content[0].amount", is(response.getAmount())))
                .andExpect(jsonPath("$.content[0].links[0].href", is("http://localhost/books/1")))
                .andExpect(jsonPath("$.content[0].links[1].href", is("http://localhost/books/1")))
                .andExpect(jsonPath("$.content[0].links[2].href", is("http://localhost/books/1")));
    }

    @Test
    void shouldReturnBookResponseWhenGetBookById() throws Exception{
        var response = new BookResponse(1L, "Title", "New Author", 1);
        response.add(Link.of("http://localhost/books/1", "self"));
        response.add(Link.of("http://localhost/books/1", "update"));
        response.add(Link.of("http://localhost/books/1", "delete"));

        when(bookService.getBook(1L)).thenReturn(response);

        mockMvc.perform(get("/books/1")
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(response.getTitle())))
                .andExpect(jsonPath("$.author", is(response.getAuthor())))
                .andExpect(jsonPath("$.amount", is(response.getAmount())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/books/1")))
                .andExpect(jsonPath("$._links.update.href", is("http://localhost/books/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/books/1")));
    }

    @Test
    void shouldReturnErrorResponseWhenGetBookByIdAndBookNotFound() throws Exception{
        when(bookService.getBook(1L)).thenThrow(new BookNotFoundByIdException(1L));

        mockMvc.perform(get("/books/1")
                        .contentType("application/json")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Book not found")))
                .andExpect(jsonPath("$.detail", is("Book with id 1 not found")))
                .andExpect(jsonPath("$.instance", is("/books/1")));
    }

    @Test
    void shouldReturnBookResponseWhenUpdatingBook() throws Exception {
        // given
        var bookUpdateRequest = new BookUpdateRequest("New Title", "New Author");
        var bookResponse = new BookResponse(1L, bookUpdateRequest.getTitle(), bookUpdateRequest.getAuthor(), 1);
        bookResponse.add(Link.of("http://localhost/books/1", "self"));
        bookResponse.add(Link.of("http://localhost/books/1", "update"));
        bookResponse.add(Link.of("http://localhost/books/1", "delete"));

        when(bookService.updateBook(1L, bookUpdateRequest)).thenReturn(bookResponse);

        // when & then
        mockMvc.perform(patch("/books/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookUpdateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookResponse.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(bookResponse.getTitle())))
                .andExpect(jsonPath("$.author", is(bookResponse.getAuthor())))
                .andExpect(jsonPath("$.amount", is(bookResponse.getAmount())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/books/1")))
                .andExpect(jsonPath("$._links.update.href", is("http://localhost/books/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/books/1")));
    }

    @Test
    void shouldReturnErrorWhenUpdatingBookWithWrongAuthor() throws Exception {
        // given
        var bookUpdateRequest = new BookUpdateRequest("New Title", "Author");

        // when & then
        mockMvc.perform(patch("/books/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookUpdateRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnErrorResponseWhenUpdateBookByIdAndBookNotFound() throws Exception {
        // given
        var bookUpdateRequest = new BookUpdateRequest("New Title", "New Author");

        doThrow(new BookNotFoundByIdException(1L)).when(bookService).updateBook(1L, bookUpdateRequest);

        // when & then
        mockMvc.perform(patch("/books/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookUpdateRequest))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Book not found")))
                .andExpect(jsonPath("$.detail", is("Book with id 1 not found")))
                .andExpect(jsonPath("$.instance", is("/books/1")));
    }

    @Test
    void shouldReturnNoContentWhenDeleteBook() throws Exception {
        mockMvc.perform(delete("/books/1")
                        .contentType("application/json")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnErrorResponseWhenDeleteBookAndBookNotFound() throws Exception {
        doThrow(new BookNotFoundByIdException(1L)).when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/1")
                        .contentType("application/json")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("Book not found")))
                .andExpect(jsonPath("$.detail", is("Book with id 1 not found")))
                .andExpect(jsonPath("$.instance", is("/books/1")));
    }

}

package com.stepaniuk.nerdysoft.book;

import com.stepaniuk.nerdysoft.book.payload.BookResponse;
import com.stepaniuk.nerdysoft.testspecific.MapperLevelUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@MapperLevelUnitTest
@ContextConfiguration(classes = {BookMapperImpl.class})
public class BookMapperTest {
    @Autowired
    private BookMapper bookMapper;

    @Test
    void shouldMapBookToBookResponse() {
        // given
        Book bookToMap = new Book(1L, "Title", "Author", 1);
        // when
        BookResponse bookResponse = bookMapper.toResponse(bookToMap);
        // then
        assertNotNull(bookResponse);
        assertEquals(bookToMap.getId(), bookResponse.getId());
        assertEquals(bookToMap.getTitle(), bookResponse.getTitle());
        assertEquals(bookToMap.getAuthor(), bookResponse.getAuthor());
        assertEquals(bookToMap.getAmount(), bookResponse.getAmount());
        assertTrue(bookResponse.hasLinks());
        assertTrue(bookResponse.getLinks().hasLink("self"));
        assertTrue(bookResponse.getLinks().hasLink("update"));
        assertTrue(bookResponse.getLinks().hasLink("delete"));
    }
}

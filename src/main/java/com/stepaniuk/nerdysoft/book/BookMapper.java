package com.stepaniuk.nerdysoft.book;

import com.stepaniuk.nerdysoft.book.payload.BookResponse;
import org.mapstruct.*;
import org.springframework.hateoas.Link;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {
    @BeanMapping(qualifiedByName = "addLinks")
    BookResponse toResponse(Book book);

    @AfterMapping
    @Named("addLinks")
    default BookResponse addLinks(Book book, @MappingTarget BookResponse response) {
        response.add(Link.of("/books/" + book.getId()).withSelfRel());
        response.add(Link.of("/books/" + book.getId()).withRel("update"));
        response.add(Link.of("/books/" + book.getId()).withRel("delete"));

        return response;
    }
}

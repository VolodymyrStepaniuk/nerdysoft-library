package com.stepaniuk.nerdysoft.book.payload;

import com.stepaniuk.nerdysoft.validation.book.Author;
import com.stepaniuk.nerdysoft.validation.book.Title;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BookCreateRequest {
    @NotNull
    @Title
    private String title;
    @NotNull
    @Author
    private String author;
}

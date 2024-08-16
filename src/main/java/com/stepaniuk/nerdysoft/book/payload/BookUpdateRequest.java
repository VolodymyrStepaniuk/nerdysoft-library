package com.stepaniuk.nerdysoft.book.payload;

import com.stepaniuk.nerdysoft.validation.book.Author;
import com.stepaniuk.nerdysoft.validation.book.Title;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BookUpdateRequest {
    @Nullable
    @Title
    private String title;
    @Nullable
    @Author
    private String author;
}

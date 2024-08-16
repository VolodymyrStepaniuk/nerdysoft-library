package com.stepaniuk.nerdysoft.book.payload;

import com.stepaniuk.nerdysoft.validation.book.Author;
import com.stepaniuk.nerdysoft.validation.book.Title;
import com.stepaniuk.nerdysoft.validation.shared.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class BookResponse extends RepresentationModel<BookResponse> {
    @NotNull
    @Id
    private final Long id;
    @NotNull
    @Title
    private final String title;
    @NotNull
    @Author
    private final String author;
    @NotNull
    private final Integer amount;
}

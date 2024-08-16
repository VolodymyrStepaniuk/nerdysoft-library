package com.stepaniuk.nerdysoft.library;

import com.stepaniuk.nerdysoft.book.exception.BookNotAvailableException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/library", produces = "application/json")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/borrowBook")
    public void borrowBook(@NotNull @RequestParam Long bookId, @NotNull @RequestParam Long memberId) throws BookNotAvailableException {
        libraryService.borrowBook(bookId, memberId);
    }

    @PostMapping("/returnBook")
    public void returnBook(@NotNull @RequestParam Long bookId, @NotNull @RequestParam Long memberId) {
        libraryService.returnBook(bookId, memberId);
    }
}

package com.stepaniuk.nerdysoft.library;

import com.stepaniuk.nerdysoft.book.BookRepository;
import com.stepaniuk.nerdysoft.book.borrowed.BorrowedBook;
import com.stepaniuk.nerdysoft.book.borrowed.BorrowedBookRepository;
import com.stepaniuk.nerdysoft.book.exception.BookNotAvailableException;
import com.stepaniuk.nerdysoft.book.exception.BookNotFoundByIdException;
import com.stepaniuk.nerdysoft.member.MemberRepository;
import com.stepaniuk.nerdysoft.member.exception.MemberCannotBorrowMoreBooksException;
import com.stepaniuk.nerdysoft.member.exception.MemberNotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LibraryService {

    @Value("${library.member.borrow.limit}")
    private int borrowLimit;

    private final BookRepository bookRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private  final MemberRepository memberRepository;

    public void borrowBook(Long bookId, Long memberId) throws BookNotAvailableException {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundByIdException(bookId));

        if (book.getAmount() == 0) {
            throw new BookNotAvailableException(bookId);
        }

        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundByIdException(memberId));

        if (borrowedBookRepository.findByMemberIdAndReturnedDateIsNull(memberId).size() >= borrowLimit) {
            throw new MemberCannotBorrowMoreBooksException(bookId);
        }

        book.setAmount(book.getAmount() - 1);
        bookRepository.save(book);

        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBook(book);
        borrowedBook.setMember(member);
        borrowedBook.setBorrowedDate(Instant.now());

        borrowedBookRepository.save(borrowedBook);
    }

   public void returnBook(Long bookId, Long memberId) {
        var borrowedBook = borrowedBookRepository.findByMemberIdAndBookIdAndReturnedDateIsNull(memberId, bookId)
                .orElseThrow(() -> new BookNotFoundByIdException(bookId));

        borrowedBook.setReturnedDate(Instant.now());
        borrowedBookRepository.save(borrowedBook);

        var book = borrowedBook.getBook();

        book.setAmount(book.getAmount() + 1);
        bookRepository.save(book);
    }
}

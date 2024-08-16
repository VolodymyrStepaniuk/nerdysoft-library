package com.stepaniuk.nerdysoft.book;

import com.stepaniuk.nerdysoft.book.borrowed.BorrowedBookRepository;
import com.stepaniuk.nerdysoft.book.exception.BookNotFoundByIdException;
import com.stepaniuk.nerdysoft.book.exception.BorrowedBookCannotBeDeletedException;
import com.stepaniuk.nerdysoft.book.payload.BookCreateRequest;
import com.stepaniuk.nerdysoft.book.payload.BookResponse;
import com.stepaniuk.nerdysoft.book.payload.BookUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private final BookMapper bookMapper;

    public BookResponse createBook(BookCreateRequest bookRequest) {
        Optional<Book> bookOptional = bookRepository.findByTitleAndAuthor(bookRequest.getTitle(), bookRequest.getAuthor());

        if (bookOptional.isPresent()) {
            Book bookToUpdate = bookOptional.get();
            bookToUpdate.setAmount(bookToUpdate.getAmount() + 1);
            return bookMapper.toResponse(bookRepository.save(bookToUpdate));
        }

        Book newBook = new Book();
        newBook.setTitle(bookRequest.getTitle());
        newBook.setAuthor(bookRequest.getAuthor());
        newBook.setAmount(1);

        var savedBook = bookRepository.save(newBook);
        return bookMapper.toResponse(savedBook);
    }

    public BookResponse getBook(Long id) {
        Book bookToReturn = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundByIdException(id));

        return bookMapper.toResponse(bookToReturn);
    }

    public Page<BookResponse> getAllBooks(Pageable pageable){
        return bookRepository.findAll(pageable)
                .map(bookMapper::toResponse);
    }

    public BookResponse updateBook(Long id, BookUpdateRequest bookRequest) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundByIdException(id));

       if(bookRequest.getTitle() != null) {
           bookToUpdate.setTitle(bookRequest.getTitle());
       }
       if(bookRequest.getAuthor() != null) {
              bookToUpdate.setAuthor(bookRequest.getAuthor());
       }

       var updatedBook = bookRepository.save(bookToUpdate);

        return bookMapper.toResponse(updatedBook);
    }

    public void deleteBook(Long id){
        if(!borrowedBookRepository.findByBookIdAndReturnedDateIsNull(id).isEmpty()){
            throw new BorrowedBookCannotBeDeletedException(id);
        }
        var bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundByIdException(id));

        bookRepository.delete(bookToDelete);
    }
}

package com.stepaniuk.nerdysoft.book.borrowed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    List<BorrowedBook> findByBookIdAndReturnedDateIsNull(Long bookId);
    List<BorrowedBook> findByMemberIdAndReturnedDateIsNull(Long memberId);
    Optional<BorrowedBook> findByMemberIdAndBookIdAndReturnedDateIsNull(Long memberId, Long bookId);
}

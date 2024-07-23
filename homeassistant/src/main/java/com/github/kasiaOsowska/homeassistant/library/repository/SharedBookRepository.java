package com.github.kasiaOsowska.homeassistant.library.repository;

import com.github.kasiaOsowska.homeassistant.library.model.AppUser;
import com.github.kasiaOsowska.homeassistant.library.model.Book;
import com.github.kasiaOsowska.homeassistant.library.model.SharedBook;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedBookRepository extends JpaRepository<SharedBook, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM SharedBook sb WHERE sb.owner.id = :ownerId AND sb.borrower.id = :borrowerId")
    void deleteAllByOwnerAndBorrower(@Param("ownerId") Long ownerId, @Param("borrowerId") Long borrowerId);

    @Query("SELECT sb.book FROM SharedBook sb WHERE sb.borrower.id = :userId")
    List<Book> findBorrowedBooksByUserId(@Param("userId") Long userId);

    @Query("SELECT sb FROM SharedBook sb WHERE sb.owner.id = :ownerId AND sb.borrower.id = :borrowerId AND sb.book.id = :bookId")
    Optional<SharedBook> findByOwnerIdAndBorrowerIdAndBookId(@Param("ownerId") Long ownerId, @Param("borrowerId") Long borrowerId, @Param("bookId") Long bookId);

    @Query("SELECT sb.borrower FROM SharedBook sb WHERE sb.owner.id = :ownerId")
    List<AppUser> findBorrowersByOwnerId(@Param("ownerId") Long ownerId);
}
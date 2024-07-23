package com.github.kasiaOsowska.homeassistant.library.service;

import com.github.kasiaOsowska.homeassistant.library.model.AppUser;
import com.github.kasiaOsowska.homeassistant.library.model.Book;
import com.github.kasiaOsowska.homeassistant.library.model.SharedBook;
import com.github.kasiaOsowska.homeassistant.library.repository.SharedBookRepository;
import com.github.kasiaOsowska.homeassistant.library.repository.UserRepository;
import com.github.kasiaOsowska.homeassistant.library.other.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SharedBookRepository sharedBookRepository;

    @Autowired
    public UserService(UserRepository userRepository, SharedBookRepository sharedBookRepository) {
        this.userRepository = userRepository;
        this.sharedBookRepository = sharedBookRepository;
    }

    public AppUser registerUser(String username, String password) {
        String hashedPassword = HashUtil.hashPassword(password);
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setGuid(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public Optional<AppUser> loginUser(String username, String password) {
        Optional<AppUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            if (user.getPassword().equals(HashUtil.hashPassword(password))) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<AppUser> getUserByGuid(String guid) {
        return userRepository.findByGuid(guid);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void shareBooks(Long ownerId, Long targetUserId) {
        Optional<AppUser> ownerOpt = userRepository.findById(ownerId);
        Optional<AppUser> targetUserOpt = userRepository.findById(targetUserId);

        if (ownerOpt.isPresent() && targetUserOpt.isPresent()) {
            AppUser owner = ownerOpt.get();
            AppUser targetUser = targetUserOpt.get();

            for (Book book : owner.getBooks()) {
                Optional<SharedBook> existingSharedBook = sharedBookRepository.findByOwnerIdAndBorrowerIdAndBookId(owner.getId(), targetUser.getId(), book.getId());
                if (!existingSharedBook.isPresent()) {
                    SharedBook sharedBook = new SharedBook();
                    sharedBook.setOwner(owner);
                    sharedBook.setBorrower(targetUser);
                    sharedBook.setBook(book);
                    sharedBookRepository.save(sharedBook);
                }
            }
        }
    }

    public void unshareBooks(Long ownerId, Long targetUserId) {
        sharedBookRepository.deleteAllByOwnerAndBorrower(ownerId, targetUserId);
    }
    public List<AppUser> getBorrowers(Long ownerId) {
        return sharedBookRepository.findBorrowersByOwnerId(ownerId);
    }

    public List<AppUser> getAllUsersExcept(Long userId) {
        return userRepository.findAllByIdNot(userId);
    }

}

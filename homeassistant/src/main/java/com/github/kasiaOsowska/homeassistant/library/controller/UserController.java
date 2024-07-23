package com.github.kasiaOsowska.homeassistant.library.controller;

import com.github.kasiaOsowska.homeassistant.library.dto.AppUserDto;
import com.github.kasiaOsowska.homeassistant.library.model.AppUser;
import com.github.kasiaOsowska.homeassistant.library.service.SessionService;
import com.github.kasiaOsowska.homeassistant.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/home-assistant/api/users")
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestParam String username, @RequestParam String password) {
        AppUser user = userService.registerUser(username, password);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Optional<AppUser> userOpt = userService.loginUser(username, password);
        if (userOpt.isPresent()) {
            String sessionId = sessionService.createSession(userOpt.get());
            return ResponseEntity.ok(sessionId);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/{userId}/guid")
    public ResponseEntity<String> getUserGuid(@PathVariable String userId) {
        Optional<AppUser> userOpt = userService.getUserByGuid(userId);
        return userOpt.map(user -> ResponseEntity.ok(user.getGuid())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-session")
    public ResponseEntity<AppUserDto> getUserBySessionId(@RequestParam String sessionId) {
        AppUser user = SessionService.getUserBySessionId(sessionId);
        if (user != null) {
            AppUserDto AppUserDto = new AppUserDto(user.getId(), user.getUsername());
            return ResponseEntity.ok(AppUserDto);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/all-except")
    public ResponseEntity<List<AppUserDto>> getAllUsersExcept (@RequestParam String sessionId) {
        AppUser user = SessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<AppUserDto> users = userService.getAllUsersExcept(user.getId()).stream()
                .map(u -> new AppUserDto(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
    @GetMapping("/borrowers")
    public ResponseEntity<List<AppUserDto>> getBorrowers(@RequestParam String sessionId) {
        AppUser user = SessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<AppUserDto> borrowers = userService.getBorrowers(user.getId()).stream()
                .map(u -> new AppUserDto(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(borrowers);
    }
    @PostMapping("/share")
    public ResponseEntity<Void> shareBooks(@RequestParam Long targetUserId, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            userService.shareBooks(user.getId(), targetUserId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/unshare")
    public ResponseEntity<Void> unshareBooks(@RequestParam Long borrowerId, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            userService.unshareBooks(user.getId(), borrowerId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}

package com.librarySecurity.user;

import com.librarySecurity.exception.UserAlreadyExistsException;
import com.librarySecurity.exception.UserNotFoundException;
import com.librarySecurity.security.LibrarySecurityConfig;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements IUserService{
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
//    private LibrarySecurityConfig librarySecurityConfig;

    @Override
    public User add(User user) {
        Optional<User> theUser = userRepository.findByEmail(user.getEmail());
        if (theUser.isPresent()){
            throw new UserAlreadyExistsException("A user with the " + user.getEmail() + " already exist");
        }
//        user.setPassword(librarySecurityConfig.passwordEncoder().encode(user.getPassword()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<UserRecord> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserRecord(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail())).collect(Collectors.toList());
    }

    @Override
//    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);

    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found"));
    }

    @Override
    public User update(User user) {
        user.setRole(user.getRole());
        return userRepository.save(user);
    }
}

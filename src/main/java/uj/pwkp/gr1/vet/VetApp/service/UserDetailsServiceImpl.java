package uj.pwkp.gr1.vet.VetApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.CreateUserRequest;
import uj.pwkp.gr1.vet.VetApp.controller.rest.response.UserResponse;
import uj.pwkp.gr1.vet.VetApp.entity.User;
import uj.pwkp.gr1.vet.VetApp.entity.UserRole;
import uj.pwkp.gr1.vet.VetApp.exception.VetAppResourceType;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.CreateVetAppException;
import uj.pwkp.gr1.vet.VetApp.exception.exceptions.DeleteVetAppException;
import uj.pwkp.gr1.vet.VetApp.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User '" + username + "' not found");
    }

    public User createUser(CreateUserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()) != null) {
            throw new CreateVetAppException("Username is taken.", VetAppResourceType.USER);
        }
        UserRole userRole;
        try {
            userRole = UserRole.valueOf(userRequest.getRole());
        } catch (IllegalArgumentException e) {
            throw new CreateVetAppException("Wrong role name.", VetAppResourceType.USER);
        }
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        String passwd = passwordEncoder.encode(password);
        return userRepository.save(User.newUser(username, passwd, userRole));
    }

    public List<UserResponse> getAllUsers() {
        log.info("Getting all users - service");
        var users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return users.stream()
                .map(user -> new UserResponse(user.getUsername(), user.getRole().name()))
                .collect(Collectors.toList());
    }

    public UserResponse deleteUser(long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new DeleteVetAppException("User with that id doesn't exist", VetAppResourceType.USER));
        userRepository.delete(user);
        return new UserResponse(user.getUsername(), user.getRole().name());
    }
}

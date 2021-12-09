package com.facuferro.meetup.service;

import com.facuferro.meetup.api.UserRequest;
import com.facuferro.meetup.domain.Role;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserState;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.NotFound;
import com.facuferro.meetup.exception.WrongUserOrPassword;
import com.facuferro.meetup.repository.UserRepository;
import com.facuferro.meetup.security.PasswordUtils;
import com.facuferro.meetup.validator.UserValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserValidator userValidator;



    public User findByEmail(String email) {
        log.info("Find  User for request {}", email);
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFound("User no found"));
    }

    public List<User> findAll() {
        log.info("Find all  ");
        return this.userRepository.findAll();
    }

    public User findById(Long id) {
        log.info("Find  User by id {}", id);
        return this.userRepository.findById(id).orElseThrow(() -> new NotFound("User no found"));
    }

    public List<User> findAllAdmins() {
        log.info("Find all admins ");
        return this.userRepository.findByRole(Role.ROLE_ADMIN);
    }

    public User getAndVerify(String email, String pass) {
        log.info("Get and verify for request {}", email);
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new WrongUserOrPassword("User no found"));
        if (this.verfyPassword(user, pass)) {
            return user;
        } else {
            throw new WrongUserOrPassword("User or password incorrect, or user is not active . User " + email);
        }
    }

    private boolean verfyPassword(User user, String pass) {
        return PasswordUtils.encrypt(pass).equals(user.getPassword())&&user.isActive();

    }

    public User create(UserRequest userRequest, UserState state) {
        log.info("Creating new user for request {}", userRequest.toString());
        User newUser = generateUser(userRequest,state);
        try {
            return userRepository.save(newUser);

        } catch (DataIntegrityViolationException exc) {
            log.error("Concurrent user", exc);
            throw new DataIntegrity("Error creating user", exc);
        }
    }


    public User confirm(User user,String token){
        try {
            User update =  findById(user.getId());
            userValidator.validateUserToken(update,token);
            update.toActive();
            return userRepository.save(update);

        } catch (DataIntegrityViolationException exc) {
            log.error("Concurrent user", exc);
            throw new DataIntegrity("Error updating user", exc);
        }
    }

    private User generateUser(UserRequest user,UserState state) {
        return  User.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .status(state)
                .role(user.getRole()==null?Role.ROLE_USER:user.getRole())
                .pwd(PasswordUtils.encrypt(user.getPassword()))
                .token(UUID.randomUUID().toString())
                .email(user.getEmail()).build();

    }
}

package com.facuferro.meetup.service;

import com.facuferro.meetup.api.UserRequest;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserState;

import java.util.List;

public interface UserService {

    User findByEmail(String email);

    List<User> findAll();

    User findById(Long id);

    List<User> findAllAdmins();

    User getAndVerify(String email, String pass);

    User create(UserRequest userRequest, UserState state);

    User confirm(User user,String token);


}

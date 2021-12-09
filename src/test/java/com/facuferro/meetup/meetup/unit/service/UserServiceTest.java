package com.facuferro.meetup.meetup.unit.service;

import com.facuferro.meetup.domain.Role;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserState;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.NotFound;
import com.facuferro.meetup.exception.WrongState;
import com.facuferro.meetup.exception.WrongUserOrPassword;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.service.UserService;
import com.facuferro.meetup.service.UserServiceImpl;
import com.facuferro.meetup.validator.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends MeetupAbstractTest {

    private User user = getBasicUser();
    private UserService userService;
    private String email = "facundoferro@gmail.com";



    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository,new UserValidator());
    }

    @Test
    public void findAll(){
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        Assertions.assertEquals(userService.findAll().size(),1);
    }

    @Test
    public void findById(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Assertions.assertNotNull(userService.findById(1l));
    }

    @Test
    public void findByIdError(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFound.class,()-> userService.findById(1l));
    }

    @Test
    public void findByEmail(){
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        Assertions.assertNotNull(userService.findByEmail(email));
    }

    @Test
    public void findByEmailError(){
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFound.class,()-> userService.findByEmail(email));
    }


    @Test
    public void create(){
        when(userRepository.save(any(User.class))).thenReturn(user);
        User user = userService.create(getBasicUserRequest(), UserState.ACTIVE);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getEmail());
        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getRole());
        Assertions.assertNotNull(user.getSurname());
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertNotNull(user.getToken());
        Assertions.assertNotNull(user.getStatus());
        Assertions.assertNull(user.getAuthorities());
        Assertions.assertNull(user.getUsersMeetup());
        Assertions.assertNotNull(user.getUsername());
        assert(user.isActive());
        assert(!user.isPending());
        assert(!user.isAccountNonExpired());
        assert(!user.isAccountNonLocked());
        assert(!user.isAccountNonLocked());
        assert(!user.isEnabled());
        assert(!user.isCredentialsNonExpired());



    }

    @Test
    public void createError(){
        when(userRepository.save(any(User.class))).thenThrow(super.getMockDataIntegrityException());
        Assertions.assertThrows(DataIntegrity.class,()-> userService.create(getBasicUserRequest(), UserState.ACTIVE));

    }

    @Test
    public void findAllAdmins(){
        when(userRepository.findByRole(any(Role.class))).thenReturn(Arrays.asList(user));
        Assertions.assertEquals(userService.findAllAdmins().size(),1);
    }

    @Test
    public void getAndVerify(){
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        User user = userService.getAndVerify(email,"password");
        Assertions.assertNotNull(user);
    }

    @Test
    public void getAndVerifyError(){
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        Assertions.assertThrows(WrongUserOrPassword.class,()-> userService.getAndVerify(email,"wrongPassword"));
    }

    @Test
    public void confirmNotFound(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFound.class,()-> userService.confirm(user,"token"));
    }

    @Test
    public void confirm(){
        User userPending = user;
        user.setStatus(UserState.PENDING);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        Assertions.assertNotNull(userService.confirm(userPending,"token"));
    }

    @Test
    public void confirmError(){
        User userPending = user;
        user.setStatus(UserState.PENDING);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenThrow(super.getMockDataIntegrityException());
        Assertions.assertThrows(DataIntegrity.class,()-> userService.confirm(userPending,"token"));
    }

    @Test
    public void confirmErrorBadToken(){
        User userPending = user;
        user.setStatus(UserState.PENDING);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Assertions.assertThrows(WrongState.class,()-> userService.confirm(userPending,"BadToken"));
    }

    @Test
    public void confirmErrorWrongState(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Assertions.assertThrows(WrongState.class,()-> userService.confirm(user,"token"));
    }


}

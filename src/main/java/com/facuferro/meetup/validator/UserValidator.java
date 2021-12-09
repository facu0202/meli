package com.facuferro.meetup.validator;

import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.exception.WrongState;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateUserToken(User user, String token){
        if (!user.getToken().equals(token))
            throw new WrongState("Token is invalid");
        if (!user.isPending())
            throw new WrongState("Status invalid");

    }


}

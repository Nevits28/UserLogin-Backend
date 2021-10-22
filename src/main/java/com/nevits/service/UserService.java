package com.nevits.service;

import com.nevits.controller.user.UserDto;
import com.nevits.exception.UserNotFoundException;
import com.nevits.repository.document.User;

import java.util.List;

public interface UserService
{
    User create( UserDto userDto );

    User findById( String id )
        throws UserNotFoundException;

    User findByEmail( String email )
        throws UserNotFoundException;

    List<User> all();

    boolean deleteById( String id );

    User update( UserDto userDto, String id );
}

package com.rudhuvarshan.MovieBooking.configuration;

import com.rudhuvarshan.MovieBooking.model.Users;
import com.rudhuvarshan.MovieBooking.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUsersDetailsService implements UserDetailsService {

    @Autowired
    private UsersDao usersDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = usersDao.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with email " + username + " not found");
        }

        return new UserPrinciple(user);
    }
}
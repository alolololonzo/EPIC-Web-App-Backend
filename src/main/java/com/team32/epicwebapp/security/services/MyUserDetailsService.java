package com.team32.epicwebapp.security.services;


import com.team32.epicwebapp.models.Users;
import com.team32.epicwebapp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository ur;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = ur.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "Not Found"));

        return MyUserDetails.build(user);
    }
}


package com.blissstock.mappingSite.service;

import java.util.Collections;
import com.blissstock.mappingSite.entity.CustomUser;
import com.blissstock.mappingSite.entity.GuestUser;
import com.blissstock.mappingSite.repository.GuestUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private GuestUserRepository guestUserRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        GuestUser user;
        try {
            user = guestUserRepository.findByMail(email);
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        String grantedAuthority = user.getMail();
        return new CustomUser(
                user.getGuest_id(),
                user.getMail(),
                user.getOne_time_password(),
                Collections.singletonList(new SimpleGrantedAuthority(grantedAuthority)));
    }
}

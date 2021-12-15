package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.entity.CustomUser;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserAccountRepository userAccountRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {
    UserAccount user;
    try {
      user = userAccountRepository.findByMail(email);
      System.out.println(user.getRole());
    } catch (Exception e) {
      throw new UsernameNotFoundException(e.getMessage());
    }

    Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
    

    return new CustomUser(
      user.getId(),
      user.getMail(),
      user.getPassword(),
      grantedAuthorities
    );
  }
}

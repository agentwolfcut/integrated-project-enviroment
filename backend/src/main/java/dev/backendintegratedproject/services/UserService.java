package dev.backendintegratedproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import dev.backendintegratedproject.dtos.users.AccessTokenDTO;
import dev.backendintegratedproject.dtos.users.LoginUserDTO;
import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.exceptions.LoginInvalidException;
import dev.backendintegratedproject.userdatasource.entities.User;
import dev.backendintegratedproject.userdatasource.repositories.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetailsDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username or Password is incorrect.");
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        };
        roles.add(grantedAuthority);
        return new UserDetailsDTO(user.getOid(), user.getName(), user.getEmail(), user.getUsername(), user.getPassword(), user.getRole(), roles);
    }

//        public UserDetailsDTO loadUserByOid (String oid) {
//            Optional<User> user = userRepository.findById(oid);
//            if (user == null) {
//                throw new UsernameNotFoundException("Username or Password is incorrect.");
//            }
//            List<GrantedAuthority> roles = new ArrayList<>();
//            GrantedAuthority grantedAuthority = new GrantedAuthority() {
//                @Override
//                public String getAuthority() {
//                    return user.get().getRole();
//                }
//            };
//            roles.add(grantedAuthority);
//            return new UserDetailsDTO(user.get().getOid(), user.get().getName(), user.get().getEmail(), user.get().getUsername(), user.get().getPassword(), user.get().getRole(), roles);
//        }
}

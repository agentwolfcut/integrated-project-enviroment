package dev.backendintegratedproject.dtos.users;

import dev.backendintegratedproject.primarydatasource.entities.PrimaryUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class UserDetailsDTO extends User implements UserDetails {

    private String oid;
    private String name;
    private String email;
    private String role;

    public UserDetailsDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserDetailsDTO(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UserDetailsDTO(String oid, String name, String email, String username, String password, String role, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.oid = oid;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}


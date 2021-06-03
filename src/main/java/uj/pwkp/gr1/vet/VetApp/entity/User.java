package uj.pwkp.gr1.vet.VetApp.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final String username;
    private final String password;
    private final UserRole role;

    public static User newUser(String username, String password, UserRole role) {
        return User.builder()
                .id(-1L)
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

    protected User() {
        id = 0L;
        username = null;
        password = null;
        role= null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
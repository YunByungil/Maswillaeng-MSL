package Maswillaeng.MSLback.auth;

import Maswillaeng.MSLback.domain.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Slf4j
public class PrincipalDetails implements UserDetails {

    private final User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("해당 User 권한 리턴 = {}", "ROLE_" + user.getRole());
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_"+user.getRole().toString();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 원래는 getUsername
     * 지금은 getEmail로 사용해보자
     */
    @Override
    public String getUsername() {
        return user.getEmail();
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

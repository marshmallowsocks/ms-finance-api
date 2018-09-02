package com.marshmallowsocks.msfinance.user.model;

import com.marshmallowsocks.msfinance.data.user.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class User implements UserDetails {

    private final String id;
    private final String userName;
    private final String password;

    private User(final String id, final String userName, final String password) {
        super();
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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

    public User withoutPassword() {
        return new User(id, userName, "");
    }

    public static User from(UserModel model) {
        //is hashed password, is good
        return new User(model.id, model.userName, model.password);
    }

    public static class Builder {

        private String id;
        private String userName;
        private String password;

        public User build() {
            return new User(id, userName, password);
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }
    }
}

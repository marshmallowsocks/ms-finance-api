package com.marshmallowsocks.msfinance.data.user;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;

public class UserModel {

    @Id
    public String id;

    public String userName;
    public String password;

    public UserModel() {}

    public UserModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "UserModel[id=%s, userName=%s]",
                id, userName
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserModel userModel = (UserModel) o;

        return new EqualsBuilder()
                .append(id, userModel.id)
                .append(userName, userModel.userName)
                .append(password, userModel.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(userName)
                .append(password)
                .toHashCode();
    }
}

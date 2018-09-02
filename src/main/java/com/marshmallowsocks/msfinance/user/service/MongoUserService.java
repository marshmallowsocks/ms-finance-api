package com.marshmallowsocks.msfinance.user.service;

import com.marshmallowsocks.msfinance.data.user.UserModel;
import com.marshmallowsocks.msfinance.data.user.UserRepository;
import com.marshmallowsocks.msfinance.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class MongoUserService implements UserService {

    private final UserRepository repository;

    @Autowired
    public MongoUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserModel userModel = new UserModel();
        userModel.userName = user.getUsername();
        userModel.password = hashPassword(user.getPassword());

        repository.save(userModel);

        return user.withoutPassword();
    }

    @Override
    public Optional<User> find(String id) {
        Optional<UserModel> userModel = repository.findById(id);
        return userModel.map(User::from);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        Optional<UserModel> userModel = repository.findByUserName(userName);
        return userModel.map(User::from);
    }

    private String hashPassword(@NotNull String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
}

package com.marshmallowsocks.msfinance.user.service;

import com.marshmallowsocks.msfinance.data.user.UserModel;
import com.marshmallowsocks.msfinance.data.user.UserRepository;
import com.marshmallowsocks.msfinance.user.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MongoUserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    MongoUserService mongoUserService;

    @Before
    public void setup() {
        UserModel userModel = new UserModel();
        userModel.userName = "user";
        userModel.password = "pass";
        userModel.id = "1";

        when(userRepository.save(any(UserModel.class)))
                .thenReturn(null);

        when(userRepository.findById(anyString()))
                .thenReturn(Optional.of(userModel));

        when(userRepository.findByUserName(anyString()))
                .thenReturn(Optional.of(userModel));
    }

    @Test
    public void saveNewUser_verify() {
        // arrange
        User user = new User.Builder()
                        .withId("1")
                        .withUserName("user")
                        .withPassword("pass")
                        .build();

        // act
        User newUser = mongoUserService.save(user);

        // assert
        Assert.assertEquals(newUser.getId(), "1");
        Assert.assertEquals(newUser.getUsername(), "user");
        Assert.assertEquals(newUser.getPassword(), "");
    }

    @Test
    public void findById() {
        // act
        Optional<User> user = mongoUserService.find("1");

        // assert
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getUsername(), "user");
    }

    @Test
    public void findByUserName() {
        // act
        Optional<User> user = mongoUserService.findByUserName("user");

        // assert
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getId(), "1");
    }
}

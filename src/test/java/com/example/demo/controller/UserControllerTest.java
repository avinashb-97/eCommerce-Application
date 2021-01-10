package com.example.demo.controller;


import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp()
    {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserTest()
    {
        when(encoder.encode("testPassword")).thenReturn("hashedPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("newUser");
        request.setPassword("pass1234");
        request.setConfirmPassword("pass1234");
        ResponseEntity<User> response = userController.createUser(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(0, user.getId());
        Assert.assertEquals("newUser", user.getUsername());
    }

    @Test
    public void findUserByIdTest()
    {
        long id = 1L;
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("pass1234");
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(id);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User actualUser = response.getBody();
        Assert.assertNotNull(actualUser);
        Assert.assertEquals(id, actualUser.getId());
        Assert.assertEquals("newUser", actualUser.getUsername());
        Assert.assertEquals("pass1234", actualUser.getPassword());
    }

    @Test
    public void findUserByUserNameTest()
    {
        long id = 1L;
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("pass1234");
        user.setId(id);
        when(userRepository.findByUsername("newUser")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("newUser");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User actualUser = response.getBody();
        Assert.assertNotNull(actualUser);
        Assert.assertEquals(id, actualUser.getId());
        Assert.assertEquals("newUser", actualUser.getUsername());
        Assert.assertEquals("pass1234", actualUser.getPassword());
    }

}

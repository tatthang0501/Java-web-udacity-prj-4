package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void testCreateUserSuccessfully() {
        when(bCryptPasswordEncoder.encode("testingpassword")).thenReturn("testHashedPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser1");
        request.setPassword("testingpassword");
        request.setCfmPassword("testingpassword");
        final ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("testuser1", user.getUsername());
        assertEquals("testHashedPassword", user.getPassword());
    }

    @Test
    public void testCreateUserWithConfirmPasswordNotMatch() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser1");
        request.setPassword("testPassword");
        request.setCfmPassword("ttestPassword");
        ResponseEntity<?> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testFindByUserId() {
        User userMock = new User();
        userMock.setId(1L);
        when(userRepository.findById(1l)).thenReturn(Optional.of(userMock));
        ResponseEntity<User> user = userController.findById(1l);
        assertEquals(200, user.getStatusCodeValue());
    }

    @Test
    public void testFindByUsername() {
        User userMock = new User();
        userMock.setId(1L);
        userMock.setUsername("testuser1");
        when(userRepository.findByUsername("testuser1")).thenReturn(userMock);
        ResponseEntity<User> user = userController.findByUserName("testuser1");
        assertEquals(200, user.getStatusCodeValue());
    }
}
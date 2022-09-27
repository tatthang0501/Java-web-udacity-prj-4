package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class CartControllerTest {
    private CartController cartController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void testAddToCartSuccessfully() {
        User userMock = new User();
        userMock.setId(1L);
        userMock.setUsername("test user 1");
        userMock.setCart(new Cart());
        when(userRepository.findByUsername("test user 1")).thenReturn(userMock);
        when(cartRepository.save(any())).thenReturn(new Cart());
        when(itemRepository.findById(any())).thenReturn(Optional.of(new Item()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setUsername("test user 1");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testAddCartWithUserNotFound() {
        when(userRepository.findByUsername("not found user")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addTocart(new ModifyCartRequest());
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testAddToCartWithInvalidItem() {
        User userMock = new User();
        userMock.setId(1L);
        userMock.setUsername("test user 1");
        when(userRepository.findByUsername("test user 1")).thenReturn(userMock);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("test user 1");
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCartSuccessfully() {
        User userMock = new User();
        userMock.setId(1L);
        userMock.setUsername("test user 1");
        userMock.setCart(new Cart());
        when(userRepository.findByUsername("test user 1")).thenReturn(userMock);
        when(cartRepository.save(any())).thenReturn(new Cart());
        when(itemRepository.findById(any())).thenReturn(Optional.of(new Item()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setUsername("test user 1");

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCartWithInvalidUsername() {
        when(userRepository.findByUsername("invalid user name")).thenReturn(null);
        ResponseEntity<Cart> response = cartController.removeFromcart(new ModifyCartRequest());
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testRemoveFromCartWithNotFoundItem() {
        User userMock = new User();
        userMock.setId(1L);
        userMock.setUsername("test user 1");
        when(userRepository.findByUsername("test user 1")).thenReturn(userMock);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("test user 1");
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
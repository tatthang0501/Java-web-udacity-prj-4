package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void testSubmitOrderByUsername() {
        User userMock = new User();
        userMock.setUsername("test user 1");
        when(userRepository.findByUsername("test user 1")).thenReturn(userMock);

        Optional<UserOrder> order = Optional.of(new UserOrder());
        when(orderRepository.findById(any())).thenReturn(order);

        // create item
        Item item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal(9.0));
        List<Item> items = new ArrayList<>();
        items.add(item);

        // create cart
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(new BigDecimal(9.0));
        cart.setItems((items));

        // set cart for user order
        userMock.setCart(cart);
        when(userRepository.findByUsername(userMock.getUsername())).thenReturn(userMock);

        ResponseEntity<?> responseOder = orderController.submit("test user 1");
        assertNotNull(responseOder);
        assertEquals(200, responseOder.getStatusCodeValue());
    }

    @Test
    public void testSubmitOrderByUsernameFailed() {
        User userMock = new User();
        userMock.setUsername("test user 1");
        when(userRepository.findByUsername("test user 1")).thenReturn(null);

        ResponseEntity<?> responseOder = orderController.submit("test user 1");
        assertNotNull(responseOder);
        assertEquals(404, responseOder.getStatusCodeValue());
    }

    @Test
    public void testGetOrderForValidUser() {
        User userMock = new User();
        userMock.setUsername("test user 1");
        when(userRepository.findByUsername("test user 1")).thenReturn(userMock);

        Optional<UserOrder> userOrder = Optional.of(new UserOrder());
        when(orderRepository.findById(any())).thenReturn(userOrder);

        ResponseEntity<List<UserOrder>> responseOder = orderController.getOrdersForUser("test user 1");
        assertNotNull(responseOder);
        assertEquals(200, responseOder.getStatusCodeValue());
    }

    @Test
    public void testGetOrderByInvalidUsername() {
        User userMock = new User();
        userMock.setUsername("invalid username");
        when(userRepository.findByUsername("invalid username")).thenReturn(null);

        ResponseEntity<List<UserOrder>> responseOder = orderController.getOrdersForUser("invalid username");
        assertNotNull(responseOder);
        assertEquals(404, responseOder.getStatusCodeValue());
    }
}
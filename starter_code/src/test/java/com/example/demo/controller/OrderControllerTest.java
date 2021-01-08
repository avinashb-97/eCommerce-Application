package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.demo.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setup()
    {
        User user = createUser();
        when(userRepository.findByUsername("ram")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(createOrders());
    }

    @Test
    public void submitOrderTest()
    {
        ResponseEntity<UserOrder> response = orderController.submit("ram");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        Assert.assertEquals(createItems(), order.getItems());
        Assert.assertEquals(createUser().getId(), order.getUser().getId());
        verify(orderRepository, times(1)).save(order);

    }

    @Test
    public void submitInvalidOrderTest()
    {
        ResponseEntity<UserOrder> response = orderController.submit("user123");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull( response.getBody());
        verify(userRepository, times(1)).findByUsername("user123");
    }

    @Test
    public void getOrdersTest()
    {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("ram");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> orders = response.getBody();
        Assert.assertEquals(createOrders().size(), orders.size());

    }

    @Test
    public void getOrdersForInvalidUserTest()
    {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user123");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull( response.getBody());
        verify(userRepository, times(1)).findByUsername("user123");
    }

}
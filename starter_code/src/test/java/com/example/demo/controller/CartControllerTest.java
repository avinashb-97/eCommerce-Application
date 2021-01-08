package com.example.demo.controller;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static com.example.demo.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setup()
    {
        Mockito.when(userRepository.findByUsername("ram")).thenReturn(createUser());
        Mockito.when(itemRepository.findById(any())).thenReturn(Optional.of(createItem(1)));
    }

    @Test
    public void addToCartTest()
    {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("ram");
        request.setItemId(1);
        request.setQuantity(5);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();
        Cart generatedCart = createCart(createUser());

        Assert.assertNotNull(actualCart);

        Item item = createItem(request.getItemId());
        BigDecimal itemPrice = item.getPrice();

        BigDecimal expectedTotal = itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())).add(generatedCart.getTotal());

        Assert.assertEquals("ram", actualCart.getUser().getUsername());
        Assert.assertEquals(generatedCart.getItems().size() + request.getQuantity(), actualCart.getItems().size());
        Assert.assertEquals(createItem(1), actualCart.getItems().get(0));
        Assert.assertEquals(expectedTotal, actualCart.getTotal());
        verify(cartRepository, times(1)).save(actualCart);

    }

    @Test
    public void removeFromCartTest()
    {

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1);
        request.setUsername("ram");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(String.valueOf(200), response.getStatusCodeValue());

        Cart actualCart = response.getBody();

        Cart generatedCart = createCart(createUser());

        Assert.assertNotNull(actualCart);

        Item item = createItem(request.getItemId());
        BigDecimal itemPrice = item.getPrice();

        BigDecimal expectedTotal = generatedCart.getTotal().subtract(itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())));

        Assert.assertEquals("ram", actualCart.getUser().getUsername());
        Assert.assertEquals(generatedCart.getItems().size() - request.getQuantity(), actualCart.getItems().size());
        Assert.assertEquals(createItem(2), actualCart.getItems().get(0));
        Assert.assertEquals(expectedTotal, actualCart.getTotal());

        verify(cartRepository, times(1)).save(actualCart);

    }

    @Test
    public void invalidUsernameTest()
    {

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("user1234");
        request.setQuantity(1);
        request.setItemId(1);

        ResponseEntity<Cart> removeResponse = cartController.removeFromcart(request);
        Assert.assertNotNull(removeResponse);
        Assert.assertEquals(404, removeResponse.getStatusCodeValue());
        Assert.assertNull(removeResponse.getBody());

        ResponseEntity<Cart> addResponse = cartController.addTocart(request);
        Assert.assertNotNull(addResponse);
        Assert.assertEquals(404, addResponse.getStatusCodeValue());
        Assert.assertNull(addResponse.getBody());

        verify(userRepository, times(2)).findByUsername("user1234");

    }



}

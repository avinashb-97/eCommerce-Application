package com.example.demo.controller;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.TestUtils.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setup()
    {
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem(1)));
        Mockito.when(itemRepository.findAll()).thenReturn(createItems());
        Mockito.when(itemRepository.findByName("item")).thenReturn(Arrays.asList(createItem(1), createItem(2)));

    }

    @Test
    public void getItemsTest()
    {
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        Assert.assertEquals(createItems(), items);
        verify(itemRepository , times(1)).findAll();
    }

    @Test
    public void getItemByIdTest()
    {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertNotNull(response);
        Assert. assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        Assert.assertEquals(createItem(1L), item);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    public void getItemByIdInvalidTest()
    {
        ResponseEntity<Item> response = itemController.getItemById(10L);
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull(response.getBody());
        verify(itemRepository, times(1)).findById(10L);
    }

    @Test
    public void getItemByNameTest()
    {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        List<Item> items = Arrays.asList(createItem(1), createItem(2));
        Assert.assertEquals(createItems(), items);
        verify(itemRepository , times(1)).findByName("item");
    }

    @Test
    public void getItemByNameInvalidTest()
    {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("invalid name");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull(response.getBody());
        verify(itemRepository , times(1)).findByName("invalid name");
    }
}

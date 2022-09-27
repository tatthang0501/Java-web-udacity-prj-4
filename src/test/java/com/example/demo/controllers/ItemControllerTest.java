package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void testFindAllItem() {
        ResponseEntity<List<Item>> items = itemController.getItems();
        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());
    }

    @Test
    public void testFindItemById() {
        Item itemMock = new Item();
        itemMock.setId(1l);
        itemMock.setPrice(new BigDecimal(9.0));
        when(itemRepository.findById(1l)).thenReturn(Optional.of(itemMock));

        ResponseEntity<Item> item = itemController.getItemById(1l);
        assertNotNull(item);
        assertEquals(200, item.getStatusCodeValue());
    }

    @Test
    public void testGetAllItemByName() {
        List<Item> itemMocks = new ArrayList<>();
        itemMocks.add(new Item());
        when(itemRepository.findByName("Finding item")).thenReturn(itemMocks);

        ResponseEntity<List<Item>> items = itemController.getItemsByName("Finding item");
        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());
        assertEquals(1, items.getBody().size());
    }
}
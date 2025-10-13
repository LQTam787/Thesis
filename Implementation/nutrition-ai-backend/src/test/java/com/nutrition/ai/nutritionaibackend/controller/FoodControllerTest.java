package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.FoodItemDto;
import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.service.FoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
@WithMockUser
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodService foodService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private FoodItem foodItem1;
    private FoodItemDto foodItemDto1;
    private FoodItem foodItem2;
    private FoodItemDto foodItemDto2;

    @BeforeEach
    void setUp() {
        foodItem1 = new FoodItem();
        foodItem1.setId(1L);
        foodItem1.setName("Apple");

        foodItemDto1 = new FoodItemDto(1L, "Apple", 95.0, 0.5, 25.0, 0.3, "1 medium", null);

        foodItem2 = new FoodItem();
        foodItem2.setId(2L);
        foodItem2.setName("Banana");

        foodItemDto2 = new FoodItemDto(2L, "Banana", 105.0, 1.3, 27.0, 0.3, "1 large", null);
    }

    @Test
    void testCreateFoodItem_Success() throws Exception {
        when(modelMapper.map(any(FoodItemDto.class), eq(FoodItem.class))).thenReturn(foodItem1);
        when(foodService.save(any(FoodItem.class))).thenReturn(foodItem1);
        when(modelMapper.map(any(FoodItem.class), eq(FoodItemDto.class))).thenReturn(foodItemDto1);

        mockMvc.perform(post("/api/foods")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void testGetFoodItem_Found() throws Exception {
        when(foodService.findOne(1L)).thenReturn(Optional.of(foodItem1));
        when(modelMapper.map(foodItem1, FoodItemDto.class)).thenReturn(foodItemDto1);

        mockMvc.perform(get("/api/foods/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void testGetFoodItem_NotFound() throws Exception {
        when(foodService.findOne(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/foods/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllFoodItems_Success() throws Exception {
        List<FoodItem> foodItems = Arrays.asList(foodItem1, foodItem2);
        when(foodService.findAll()).thenReturn(foodItems);
        when(modelMapper.map(foodItem1, FoodItemDto.class)).thenReturn(foodItemDto1);
        when(modelMapper.map(foodItem2, FoodItemDto.class)).thenReturn(foodItemDto2);

        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].name").value("Banana"));
    }

    @Test
    void testUpdateFoodItem_Success() throws Exception {
        FoodItemDto updatedDto = new FoodItemDto(1L, "Golden Apple", 100.0, 0.6, 26.0, 0.4, "1 medium", null);
        FoodItem updatedFoodItem = new FoodItem();
        updatedFoodItem.setId(1L);
        updatedFoodItem.setName("Golden Apple");

        when(foodService.findOne(1L)).thenReturn(Optional.of(foodItem1));
        when(modelMapper.map(any(FoodItemDto.class), eq(FoodItem.class))).thenReturn(updatedFoodItem);
        when(foodService.save(any(FoodItem.class))).thenReturn(updatedFoodItem);
        when(modelMapper.map(updatedFoodItem, FoodItemDto.class)).thenReturn(updatedDto);

        mockMvc.perform(put("/api/foods/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Golden Apple"));
    }

    @Test
    void testUpdateFoodItem_NotFound() throws Exception {
        when(foodService.findOne(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/foods/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemDto1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFoodItem_Success() throws Exception {
        doNothing().when(foodService).delete(1L);

        mockMvc.perform(delete("/api/foods/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}

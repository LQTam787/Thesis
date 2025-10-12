package com.nutrition.ai.nutritionaibackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrition.ai.nutritionaibackend.dto.FoodItemDto;
import com.nutrition.ai.nutritionaibackend.model.domain.FoodItem;
import com.nutrition.ai.nutritionaibackend.service.FoodService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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

    @Test
    void testCreateFoodItem_Success() throws Exception {
        FoodItemDto foodItemDto = new FoodItemDto(null, "Apple", 52, 0.2, 14.0, 0.3, "g", 100.0);

        when(foodService.createFoodItem(any(FoodItemDto.class))).thenReturn(foodItemDto);

        mockMvc.perform(post("/api/v1/foods")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFoodItem_Found() throws Exception {
        Long foodId = 1L;
        FoodItem foodItem = new FoodItem();
        foodItem.setId(foodId);
        foodItem.setName("Banana");
        FoodItemDto responseDto = new FoodItemDto(foodId, "Banana", 105.0, 1.3, 27.0, 0.3, "1 large", null);

        when(foodService.findOne(foodId)).thenReturn(Optional.of(foodItem));
        when(modelMapper.map(foodItem, FoodItemDto.class)).thenReturn(responseDto);

        mockMvc.perform(get("/api/foods/{id}", foodId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(foodId))
                .andExpect(jsonPath("$.name").value("Banana"));
    }

    @Test
    void testGetFoodItem_NotFound() throws Exception {
        Long foodId = 99L;

        when(foodService.findOne(foodId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/foods/{id}", foodId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllFoodItems_Success() throws Exception {
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setId(1L);
        foodItem1.setName("Apple");
        FoodItem foodItem2 = new FoodItem();
        foodItem2.setId(2L);
        foodItem2.setName("Banana");
        List<FoodItem> foodItems = Arrays.asList(foodItem1, foodItem2);

        FoodItemDto dto1 = new FoodItemDto(1L, "Apple", 95.0, 0.5, 25.0, 0.3, "1 medium", null);
        FoodItemDto dto2 = new FoodItemDto(2L, "Banana", 105.0, 1.3, 27.0, 0.3, "1 large", null);
        List<FoodItemDto> foodItemDtos = Arrays.asList(dto1, dto2);

        when(foodService.findAll()).thenReturn(foodItems);
        when(modelMapper.map(foodItem1, FoodItemDto.class)).thenReturn(dto1);
        when(modelMapper.map(foodItem2, FoodItemDto.class)).thenReturn(dto2);

        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].name").value("Banana"));
    }

    @Test
    void testUpdateFoodItem_Success() throws Exception {
        FoodItemDto foodItemDto = new FoodItemDto(1L, "Apple", 52, 0.2, 14.0, 0.3, "g", 100.0);

        when(foodService.updateFoodItem(eq(1L), any(FoodItemDto.class))).thenReturn(foodItemDto);

        mockMvc.perform(put("/api/v1/foods/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateFoodItem_NotFound() throws Exception {
        FoodItemDto foodItemDto = new FoodItemDto(1L, "Apple", 52, 0.2, 14.0, 0.3, "g", 100.0);

        when(foodService.updateFoodItem(eq(1L), any(FoodItemDto.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/foods/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFoodItem_Success() throws Exception {
        doNothing().when(foodService).deleteFoodItem(1L);

        mockMvc.perform(delete("/api/v1/foods/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}

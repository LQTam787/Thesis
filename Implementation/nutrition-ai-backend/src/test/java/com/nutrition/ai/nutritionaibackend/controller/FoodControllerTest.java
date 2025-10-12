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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
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
        FoodItemDto inputDto = new FoodItemDto(null, "Apple", 95, 0.5, 25.0, 0.3, "fruit");
        FoodItem foodItem = new FoodItem(null, "Apple", 95, 0.5, 25.0, 0.3, "fruit");
        FoodItem savedFoodItem = new FoodItem(1L, "Apple", 95, 0.5, 25.0, 0.3, "fruit");
        FoodItemDto responseDto = new FoodItemDto(1L, "Apple", 95, 0.5, 25.0, 0.3, "fruit");

        when(modelMapper.map(inputDto, FoodItem.class)).thenReturn(foodItem);
        when(foodService.save(foodItem)).thenReturn(savedFoodItem);
        when(modelMapper.map(savedFoodItem, FoodItemDto.class)).thenReturn(responseDto);

        mockMvc.perform(post("/api/foods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void testGetFoodItem_Found() throws Exception {
        Long foodId = 1L;
        FoodItem foodItem = new FoodItem(foodId, "Banana", 105, 1.3, 27.0, 0.3, "fruit");
        FoodItemDto responseDto = new FoodItemDto(foodId, "Banana", 105, 1.3, 27.0, 0.3, "fruit");

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
        FoodItem foodItem1 = new FoodItem(1L, "Apple", 95, 0.5, 25.0, 0.3, "fruit");
        FoodItem foodItem2 = new FoodItem(2L, "Banana", 105, 1.3, 27.0, 0.3, "fruit");
        List<FoodItem> foodItems = Arrays.asList(foodItem1, foodItem2);

        FoodItemDto dto1 = new FoodItemDto(1L, "Apple", 95, 0.5, 25.0, 0.3, "fruit");
        FoodItemDto dto2 = new FoodItemDto(2L, "Banana", 105, 1.3, 27.0, 0.3, "fruit");
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
        Long foodId = 1L;
        FoodItemDto inputDto = new FoodItemDto(null, "Updated Apple", 100, 1.0, 26.0, 0.5, "fruit");
        FoodItem existingFoodItem = new FoodItem(foodId, "Apple", 95, 0.5, 25.0, 0.3, "fruit");
        FoodItem updatedFoodItem = new FoodItem(foodId, "Updated Apple", 100, 1.0, 26.0, 0.5, "fruit");
        FoodItemDto responseDto = new FoodItemDto(foodId, "Updated Apple", 100, 1.0, 26.0, 0.5, "fruit");

        when(foodService.findOne(foodId)).thenReturn(Optional.of(existingFoodItem));
        when(modelMapper.map(any(FoodItemDto.class), eq(FoodItem.class))).thenReturn(updatedFoodItem);
        when(foodService.save(any(FoodItem.class))).thenReturn(updatedFoodItem);
        when(modelMapper.map(updatedFoodItem, FoodItemDto.class)).thenReturn(responseDto);

        mockMvc.perform(put("/api/foods/{id}", foodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(foodId))
                .andExpect(jsonPath("$.name").value("Updated Apple"));
    }

    @Test
    void testUpdateFoodItem_NotFound() throws Exception {
        Long foodId = 99L;
        FoodItemDto inputDto = new FoodItemDto(null, "Updated Food", 100, 1.0, 26.0, 0.5, "category");

        when(foodService.findOne(foodId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/foods/{id}", foodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFoodItem_Success() throws Exception {
        Long foodId = 1L;
        doNothing().when(foodService).delete(foodId);

        mockMvc.perform(delete("/api/foods/{id}", foodId))
                .andExpect(status().isNoContent());
    }
}

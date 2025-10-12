package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.dto.ActivityLogDto;
import com.nutrition.ai.nutritionaibackend.dto.FoodLogDto;
import com.nutrition.ai.nutritionaibackend.dto.ProgressReportDto;
import com.nutrition.ai.nutritionaibackend.dto.SuggestionDto;
import com.nutrition.ai.nutritionaibackend.model.domain.*;
import com.nutrition.ai.nutritionaibackend.model.enums.EReportType;
import com.nutrition.ai.nutritionaibackend.repository.*;
import com.nutrition.ai.nutritionaibackend.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final FoodLogRepository foodLogRepository;
    private final ActivityLogRepository activityLogRepository;
    private final ReportRepository reportRepository;
    private final SuggestionRepository suggestionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public FoodLogDto logFood(String userId, FoodLogDto foodLogDto) {
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        FoodLog foodLog = modelMapper.map(foodLogDto, FoodLog.class);
        foodLog.setUser(user);
        foodLog.setLogDate(LocalDateTime.now());
        FoodLog savedLog = foodLogRepository.save(foodLog);
        return modelMapper.map(savedLog, FoodLogDto.class);
    }

    @Override
    public ActivityLogDto logActivity(String userId, ActivityLogDto activityLogDto) {
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        ActivityLog activityLog = modelMapper.map(activityLogDto, ActivityLog.class);
        activityLog.setUser(user);
        activityLog.setLogDate(LocalDateTime.now());
        ActivityLog savedLog = activityLogRepository.save(activityLog);
        return modelMapper.map(savedLog, ActivityLogDto.class);
    }

    @Override
    public ProgressReportDto generateProgressReport(String userId, String reportType) {
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = reportType.equalsIgnoreCase("weekly") ? endDate.minusWeeks(1) : endDate.minusMonths(1);

        List<FoodLog> foodLogs = foodLogRepository.findByUserAndLogDateBetween(user, startDate, endDate);
        List<ActivityLog> activityLogs = activityLogRepository.findByUserAndLogDateBetween(user, startDate, endDate);
        List<Suggestion> suggestions = suggestionRepository.findByUser(user);

        // Create and save the report entity
        Report report = new Report();
        report.setUser(user);
        report.setReportType(EReportType.valueOf(reportType.toUpperCase() + "_PROGRESS"));
        report.setGeneratedDate(LocalDateTime.now());
        report.setContent("Summary for " + reportType + " progress."); // Simplified content
        reportRepository.save(report);

        // Convert entities to DTOs
        List<FoodLogDto> foodLogDtos = foodLogs.stream().map(log -> modelMapper.map(log, FoodLogDto.class)).collect(Collectors.toList());
        List<ActivityLogDto> activityLogDtos = activityLogs.stream().map(log -> modelMapper.map(log, ActivityLogDto.class)).collect(Collectors.toList());
        List<SuggestionDto> suggestionDtos = suggestions.stream().map(sug -> modelMapper.map(sug, SuggestionDto.class)).collect(Collectors.toList());

        return new ProgressReportDto(report.getId(), report.getReportType(), report.getGeneratedDate(), report.getContent(), foodLogDtos, activityLogDtos, suggestionDtos);
    }
}

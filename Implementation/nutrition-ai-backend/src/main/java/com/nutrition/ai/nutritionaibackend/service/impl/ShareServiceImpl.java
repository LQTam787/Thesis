package com.nutrition.ai.nutritionaibackend.service.impl;

import com.nutrition.ai.nutritionaibackend.dto.ContentCommentDto;
import com.nutrition.ai.nutritionaibackend.dto.SharedContentDto;
import com.nutrition.ai.nutritionaibackend.model.shared.*;
import com.nutrition.ai.nutritionaibackend.model.domain.User;
import com.nutrition.ai.nutritionaibackend.repository.*;
import com.nutrition.ai.nutritionaibackend.service.ShareService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ShareService interface.
 * Handles the business logic for sharing, liking, and commenting on content.
 */
@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {

    private final SharedContentRepository sharedContentRepository;
    private final ContentLikeRepository contentLikeRepository;
    private final ContentCommentRepository contentCommentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public SharedContentDto shareContent(Long userId, Long contentId, ContentType contentType, Visibility visibility) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        SharedContent sharedContent = SharedContent.builder()
                .user(user)
                .contentId(contentId)
                .contentType(contentType)
                .visibility(visibility)
                .build();

        SharedContent savedContent = sharedContentRepository.save(sharedContent);
        return convertToDto(savedContent);
    }

    @Override
    public List<SharedContentDto> getAllPublicContent() {
        return sharedContentRepository.findAll().stream()
                .filter(sc -> sc.getVisibility() == Visibility.PUBLIC)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void likeContent(Long sharedContentId, Long userId) {
        SharedContent sharedContent = sharedContentRepository.findById(sharedContentId)
                .orElseThrow(() -> new EntityNotFoundException("Shared content not found with id: " + sharedContentId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        ContentLike like = ContentLike.builder()
                .sharedContent(sharedContent)
                .user(user)
                .build();

        contentLikeRepository.save(like);
    }

    @Override
    public ContentCommentDto commentOnContent(Long sharedContentId, Long userId, String text) {
        SharedContent sharedContent = sharedContentRepository.findById(sharedContentId)
                .orElseThrow(() -> new EntityNotFoundException("Shared content not found with id: " + sharedContentId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        ContentComment comment = ContentComment.builder()
                .sharedContent(sharedContent)
                .user(user)
                .text(text)
                .build();

        ContentComment savedComment = contentCommentRepository.save(comment);
        return modelMapper.map(savedComment, ContentCommentDto.class);
    }

    @Override
    public List<ContentCommentDto> getCommentsForContent(Long sharedContentId) {
        if (!sharedContentRepository.existsById(sharedContentId)) {
            throw new EntityNotFoundException("Shared content not found with id: " + sharedContentId);
        }
        return contentCommentRepository.findAll().stream()
                .filter(comment -> comment.getSharedContent().getId().equals(sharedContentId))
                .map(comment -> modelMapper.map(comment, ContentCommentDto.class))
                .collect(Collectors.toList());
    }

    private SharedContentDto convertToDto(SharedContent sharedContent) {
        SharedContentDto dto = modelMapper.map(sharedContent, SharedContentDto.class);
        dto.setLikeCount(contentLikeRepository.countBySharedContentId(sharedContent.getId()));
        dto.setCommentCount(contentCommentRepository.countBySharedContentId(sharedContent.getId()));
        return dto;
    }
}

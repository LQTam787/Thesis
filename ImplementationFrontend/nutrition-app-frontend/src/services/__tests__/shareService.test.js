// src/services/__tests__/shareService.test.js

import { describe, it, expect, vi, beforeEach } from 'vitest';
import shareService from '../shareService';
import api from '../api';

// Mock the entire api module
vi.mock('../api', () => ({
    default: {
        get: vi.fn(),
        post: vi.fn(),
    },
}));

// Mock console.error to prevent logging during tests
vi.spyOn(console, 'error').mockImplementation(() => {});

describe('shareService', () => {
    beforeEach(() => {
        // Reset mocks before each test
        vi.clearAllMocks();
    });

    // --- Tests for getCommunityFeed ---
    describe('getCommunityFeed', () => {
        it('should fetch the community feed successfully', async () => {
            const mockFeed = [{ id: '1', content: 'Hello World' }];
            api.get.mockResolvedValue({ data: mockFeed });

            const result = await shareService.getCommunityFeed();

            expect(api.get).toHaveBeenCalledWith('/posts/feed');
            expect(result).toEqual(mockFeed);
        });

        it('should throw an error if fetching the feed fails', async () => {
            const mockError = new Error('Network Error');
            api.get.mockRejectedValue(mockError);

            await expect(shareService.getCommunityFeed()).rejects.toThrow('Network Error');
            expect(api.get).toHaveBeenCalledWith('/posts/feed');
        });
    });

    // --- Tests for createPost ---
    describe('createPost', () => {
        it('should create a new post successfully', async () => {
            const postData = { content: 'New post' };
            const createdPost = { id: '2', ...postData };
            api.post.mockResolvedValue({ data: createdPost });

            const result = await shareService.createPost(postData);

            expect(api.post).toHaveBeenCalledWith('/posts', postData);
            expect(result).toEqual(createdPost);
        });

        it('should throw an error if post creation fails', async () => {
            const postData = { content: 'New post' };
            const mockError = new Error('Failed to create');
            api.post.mockRejectedValue(mockError);

            await expect(shareService.createPost(postData)).rejects.toThrow('Failed to create');
            expect(api.post).toHaveBeenCalledWith('/posts', postData);
        });
    });

    // --- Tests for toggleLike ---
    describe('toggleLike', () => {
        const postId = 'post123';

        it('should toggle like on a post successfully', async () => {
            const likeStatus = { likes: 10, isLiked: true };
            api.post.mockResolvedValue({ data: likeStatus });

            const result = await shareService.toggleLike(postId);

            expect(api.post).toHaveBeenCalledWith(`/posts/${postId}/like`);
            expect(result).toEqual(likeStatus);
        });

        it('should throw an error if toggling like fails', async () => {
            const mockError = new Error('API Error');
            api.post.mockRejectedValue(mockError);

            await expect(shareService.toggleLike(postId)).rejects.toThrow('API Error');
            expect(api.post).toHaveBeenCalledWith(`/posts/${postId}/like`);
        });
    });

    // --- Tests for addComment ---
    describe('addComment', () => {
        const postId = 'post123';
        const commentContent = 'This is a comment';

        it('should add a comment to a post successfully', async () => {
            const newComment = { id: 'comment456', content: commentContent };
            api.post.mockResolvedValue({ data: newComment });

            const result = await shareService.addComment(postId, commentContent);

            expect(api.post).toHaveBeenCalledWith(`/posts/${postId}/comments`, { content: commentContent });
            expect(result).toEqual(newComment);
        });

        it('should throw an error if adding a comment fails', async () => {
            const mockError = new Error('Cannot comment');
            api.post.mockRejectedValue(mockError);

            await expect(shareService.addComment(postId, commentContent)).rejects.toThrow('Cannot comment');
            expect(api.post).toHaveBeenCalledWith(`/posts/${postId}/comments`, { content: commentContent });
        });
    });
});

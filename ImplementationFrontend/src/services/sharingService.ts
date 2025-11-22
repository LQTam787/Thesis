import api from './api'; // Assuming api.ts exports a configured axios instance

interface SharedContentData {
    title: string;
    content: string;
    nutritionPlanId?: string;
    isPublic: boolean;
}

interface CommentData {
    noiDungId: string;
    nguoiDungId: string;
    noiDungBinhLuan: string;
}

const sharingService = {
    /**
     * Shares a new nutrition plan or activity.
     * @param data - The data for the shared content.
     * @returns A promise that resolves to the API response.
     */
    shareContent: async (data: SharedContentData) => {
        try {
            const response = await api.post('/api/share/content', data);
            return response.data;
        } catch (error) {
            console.error('Error sharing content:', error);
            throw error;
        }
    },

    /**
     * Fetches all shared content.
     * @returns A promise that resolves to an array of shared content items.
     */
    getAllSharedContent: async () => {
        try {
            const response = await api.get('/api/share/content');
            return response.data;
        } catch (error) {
            console.error('Error fetching all shared content:', error);
            throw error;
        }
    },

    /**
     * Fetches a specific shared content item by ID.
     * @param id - The ID of the shared content.
     * @returns A promise that resolves to the shared content item.
     */
    getSharedContentById: async (id: string) => {
        try {
            const response = await api.get(`/api/share/content/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching shared content with ID ${id}:`, error);
            throw error;
        }
    },

    /**
     * Likes a shared content item.
     * @param noiDungId - The ID of the shared content to like.
     * @param nguoiDungId - The ID of the user liking the content.
     * @returns A promise that resolves to the API response.
     */
    likeContent: async (noiDungId: string, nguoiDungId: string) => {
        try {
            const response = await api.post(`/api/share/content/${noiDungId}/like`, { nguoiDungId });
            return response.data;
        } catch (error) {
            console.error(`Error liking content ${noiDungId}:`, error);
            throw error;
        }
    },

    /**
     * Adds a comment to a shared content item.
     * @param data - The comment data.
     * @returns A promise that resolves to the API response.
     */
    addComment: async (data: CommentData) => {
        try {
            const response = await api.post(`/api/share/content/${data.noiDungId}/comment`, data);
            return response.data;
        } catch (error) {
            console.error(`Error adding comment to content ${data.noiDungId}:`, error);
            throw error;
        }
    },

    /**
     * Saves a shared content item for a user.
     * @param noiDungId - The ID of the shared content to save.
     * @param nguoiDungId - The ID of the user saving the content.
     * @returns A promise that resolves to the API response.
     */
    saveContent: async (noiDungId: string, nguoiDungId: string) => {
        try {
            const response = await api.post(`/api/share/content/${noiDungId}/save`, { nguoiDungId });
            return response.data;
        } catch (error) {
            console.error(`Error saving content ${noiDungId}:`, error);
            throw error;
        }
    },

    /**
     * Updates the share mode of a shared content item.
     * @param noiDungId - The ID of the shared content.
     * @param cheDo - The new share mode (e.g., 'public', 'private').
     * @param danhSachNguoiNhan - Optional list of recipient IDs if sharing privately.
     * @returns A promise that resolves to the API response.
     */
    updateShareMode: async (noiDungId: string, cheDo: string, danhSachNguoiNhan?: string[]) => {
        try {
            const response = await api.put(`/api/share/content/${noiDungId}/mode`, { cheDo, danhSachNguoiNhan });
            return response.data;
        } catch (error) {
            console.error(`Error updating share mode for content ${noiDungId}:`, error);
            throw error;
        }
    },

    /**
     * Approves a shared content item (Admin function).
     * @param contentId - The ID of the content to approve.
     * @returns A promise that resolves to the API response.
     */
    approveSharedContent: async (contentId: string) => {
        try {
            const response = await api.put(`/api/admin/share/content/${contentId}/approve`);
            return response.data;
        } catch (error) {
            console.error(`Error approving content ${contentId}:`, error);
            throw error;
        }
    },

    /**
     * Rejects a shared content item (Admin function).
     * @param contentId - The ID of the content to reject.
     * @returns A promise that resolves to the API response.
     */
    rejectSharedContent: async (contentId: string) => {
        try {
            const response = await api.put(`/api/admin/share/content/${contentId}/reject`);
            return response.data;
        } catch (error) {
            console.error(`Error rejecting content ${contentId}:`, error);
            throw error;
        }
    },

    /**
     * Deletes a shared content item (Admin function).
     * @param contentId - The ID of the content to delete.
     * @returns A promise that resolves to the API response.
     */
    deleteSharedContent: async (contentId: string) => {
        try {
            const response = await api.delete(`/api/admin/share/content/${contentId}`);
            return response.data;
        } catch (error) {
            console.error(`Error deleting content ${contentId}:`, error);
            throw error;
        }
    },
};

export default sharingService;

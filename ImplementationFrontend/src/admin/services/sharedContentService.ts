import { SharedContent } from '../types/SharedContent';

let mockSharedContent: SharedContent[] = [
  {
    id: 'sc1',
    tieuDe: 'Kế hoạch ăn uống lành mạnh',
    noiDung: 'Đây là kế hoạch ăn uống 7 ngày cho người muốn giảm cân.',
    trangThaiChiaSe: 'approved',
    luotXem: 120,
    luotThich: 45,
    luotBinhLuan: 10,
    luotLuu: 20,
  },
  {
    id: 'sc2',
    tieuDe: 'Bài tập thể dục tại nhà',
    noiDung: 'Hướng dẫn các bài tập đơn giản có thể thực hiện tại nhà.',
    trangThaiChiaSe: 'pending',
    luotXem: 80,
    luotThich: 30,
    luotBinhLuan: 5,
    luotLuu: 15,
  },
  {
    id: 'sc3',
    tieuDe: 'Công thức nấu ăn chay',
    noiDung: 'Tuyển tập các công thức nấu ăn chay ngon miệng và dễ làm.',
    trangThaiChiaSe: 'approved',
    luotXem: 200,
    luotThich: 80,
    luotBinhLuan: 25,
    luotLuu: 50,
  },
];

export const sharedContentService = {
  getAllSharedContent: async (): Promise<SharedContent[]> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockSharedContent);
      }, 500);
    });
  },

  getSharedContentById: async (id: string): Promise<SharedContent | undefined> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockSharedContent.find(content => content.id === id));
      }, 500);
    });
  },

  updateSharedContentStatus: async (id: string, status: string): Promise<boolean> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const index = mockSharedContent.findIndex(content => content.id === id);
        if (index !== -1) {
          mockSharedContent[index].trangThaiChiaSe = status;
          resolve(true);
        } else {
          resolve(false);
        }
      }, 500);
    });
  },

  deleteSharedContent: async (id: string): Promise<boolean> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const initialLength = mockSharedContent.length;
        mockSharedContent = mockSharedContent.filter(content => content.id !== id);
        resolve(mockSharedContent.length < initialLength);
      }, 500);
    });
  },

  filterSharedContentByStatus: async (status: string): Promise<SharedContent[]> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockSharedContent.filter(content => content.trangThaiChiaSe === status));
      }, 500);
    });
  },

  searchSharedContent: async (keyword: string): Promise<SharedContent[]> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const lowerCaseKeyword = keyword.toLowerCase();
        resolve(mockSharedContent.filter(content => 
          content.tieuDe.toLowerCase().includes(lowerCaseKeyword) ||
          content.noiDung.toLowerCase().includes(lowerCaseKeyword)
        ));
      }, 500);
    });
  },
};

import { User } from '../types/User';

export let mockUsers: User[] = [
  { id: '1', tenThat: 'Nguyễn Văn A', email: 'nguyenvana@example.com', tenTaiKhoan: 'vana' },
  { id: '2', tenThat: 'Trần Thị B', email: 'tranthib@example.com', tenTaiKhoan: 'thib' },
  { id: '3', tenThat: 'Lê Văn C', email: 'levanc@example.com', tenTaiKhoan: 'vanc' },
];

export const userService = {
  getAllUsers: async (): Promise<User[]> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockUsers);
      }, 500);
    });
  },

  getUserById: async (id: string): Promise<User | undefined> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockUsers.find(user => user.id === id));
      }, 500);
    });
  },

  addUser: async (user: Omit<User, 'id'>): Promise<User> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const newUser = { ...user, id: String(mockUsers.length + 1) };
        mockUsers.push(newUser);
        resolve(newUser);
      }, 500);
    });
  },

  updateUser: async (user: User): Promise<boolean> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const index = mockUsers.findIndex(u => u.id === user.id);
        if (index !== -1) {
          mockUsers[index] = user;
          resolve(true);
        } else {
          resolve(false);
        }
      }, 500);
    });
  },

  deleteUser: async (id: string): Promise<boolean> => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const initialLength = mockUsers.length;
        mockUsers = mockUsers.filter(user => user.id !== id);
        resolve(mockUsers.length < initialLength);
      }, 500);
    });
  },
};

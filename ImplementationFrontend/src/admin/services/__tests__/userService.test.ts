import { userService, mockUsers } from '../userService'; // Import mockUsers
import { User } from '../../types/User';

const initialMockUsersData: User[] = [
  { id: '1', tenThat: 'Nguyễn Văn A', email: 'nguyenvana@example.com', tenTaiKhoan: 'vana' },
  { id: '2', tenThat: 'Trần Thị B', email: 'tranthib@example.com', tenTaiKhoan: 'thib' },
  { id: '3', tenThat: 'Lê Văn C', email: 'levanc@example.com', tenTaiKhoan: 'vanc' },
];
let mockUsers = [...initialMockUsersData];

const mockSetTimeout = jest.spyOn(global, 'setTimeout');

describe('userService', () => {
  let mockPromise: jest.Mock;

  beforeEach(() => {
    // Clear and reset mockUsers for each test
    mockUsers.splice(0, mockUsers.length);
    mockUsers.push(...initialMockUsersData);

    // Mock setTimeout to control async operations
    mockSetTimeout.mockImplementation((fn, delay) => fn()); // Immediately execute the callback
  });

  afterEach(() => {
    jest.restoreAllMocks(); // Restore original setTimeout
  });

  test('getAllUsers returns all users', async () => {
    const users = await userService.getAllUsers();
    expect(users).toEqual(initialMockUsersData);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('getUserById returns a user by id', async () => {
    const user = await userService.getUserById('1');
    expect(user).toEqual(initialMockUsersData[0]);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('getUserById returns undefined if user not found', async () => {
    const user = await userService.getUserById('99');
    expect(user).toBeUndefined();
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('addUser adds a new user', async () => {
    const newUser = { tenThat: 'Đào Thị D', email: 'daothid@example.com', tenTaiKhoan: 'thid' };
    const addedUser = await userService.addUser(newUser);
    expect(addedUser).toEqual({ ...newUser, id: '4' });
    expect(mockUsers).toHaveLength(initialMockUsersData.length + 1);
    expect(mockUsers[3]).toEqual({ ...newUser, id: '4' });
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('updateUser updates an existing user', async () => {
    const updatedUser = { id: '1', tenThat: 'Nguyễn Văn A Updated', email: 'nguyenvana@example.com', tenTaiKhoan: 'vana' };
    const result = await userService.updateUser(updatedUser);
    expect(result).toBe(true);
    expect(mockUsers[0]).toEqual(updatedUser);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('updateUser returns false if user not found', async () => {
    const nonExistentUser = { id: '99', tenThat: 'Non Existent', email: 'nonexistent@example.com', tenTaiKhoan: 'nonexistent' };
    const result = await userService.updateUser(nonExistentUser);
    expect(result).toBe(false);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('deleteUser deletes an existing user', async () => {
    const result = await userService.deleteUser('1');
    expect(result).toBe(true);
    expect(mockUsers).toHaveLength(initialMockUsersData.length - 1);
    expect(mockUsers.find(user => user.id === '1')).toBeUndefined();
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('deleteUser returns false if user not found', async () => {
    const result = await userService.deleteUser('99');
    expect(result).toBe(false);
    expect(mockUsers).toHaveLength(initialMockUsersData.length);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });
});

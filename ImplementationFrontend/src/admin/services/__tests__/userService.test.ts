import { userService, mockUsers } from '../userService'; // Import mockUsers
import { User } from '../../types/User';

describe('userService', () => {
  const initialMockUsersData: User[] = [
    { id: '1', tenThat: 'Nguyễn Văn A', email: 'nguyenvana@example.com', tenTaiKhoan: 'vana' },
    { id: '2', tenThat: 'Trần Thị B', email: 'tranthib@example.com', tenTaiKhoan: 'thib' },
    { id: '3', tenThat: 'Lê Văn C', email: 'levanc@example.com', tenTaiKhoan: 'vanc' },
  ];

  let mockSetTimeout: jest.Mock;
  let mockPromise: jest.Mock;

  beforeEach(() => {
    // Clear and reset mockUsers for each test
    mockUsers.splice(0, mockUsers.length);
    mockUsers.push(...initialMockUsersData);

    // Mock setTimeout to control async operations
    mockSetTimeout = jest.fn((fn, delay) => fn()); // Immediately execute the callback

    jest.spyOn(global, 'setTimeout').mockImplementation(mockSetTimeout);
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
    const user = await userService.getUserById('999');
    expect(user).toBeUndefined();
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('addUser adds a new user', async () => {
    const newUser: Omit<User, 'id'> = { tenThat: 'Đào Thị D', email: 'daothid@example.com', tenTaiKhoan: 'thid' };
    const addedUser = await userService.addUser(newUser);

    expect(addedUser).toEqual({ ...newUser, id: '4' });
    const allUsers = await userService.getAllUsers();
    expect(allUsers).toHaveLength(4);
    expect(allUsers).toContainEqual(addedUser);
    expect(mockSetTimeout).toHaveBeenCalledTimes(2); // One for addUser, one for getAllUsers
  });

  test('updateUser updates an existing user', async () => {
    const updatedUser: User = { ...initialMockUsersData[0], tenThat: 'Nguyễn Văn An Mới' };
    const result = await userService.updateUser(updatedUser);

    expect(result).toBe(true);
    const user = await userService.getUserById('1');
    expect(user).toEqual(updatedUser);
    expect(mockSetTimeout).toHaveBeenCalledTimes(2); // One for updateUser, one for getUserById
  });

  test('updateUser returns false if user not found', async () => {
    const nonExistentUser: User = { id: '999', tenThat: 'Non Existent', email: 'non@example.com', tenTaiKhoan: 'non' };
    const result = await userService.updateUser(nonExistentUser);
    expect(result).toBe(false);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('deleteUser deletes an existing user', async () => {
    const result = await userService.deleteUser('1');

    expect(result).toBe(true);
    const allUsers = await userService.getAllUsers();
    expect(allUsers).toHaveLength(2);
    expect(allUsers.find(u => u.id === '1')).toBeUndefined();
    expect(mockSetTimeout).toHaveBeenCalledTimes(2); // One for deleteUser, one for getAllUsers
  });

  test('deleteUser returns false if user not found', async () => {
    const result = await userService.deleteUser('999');
    expect(result).toBe(false);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });
});

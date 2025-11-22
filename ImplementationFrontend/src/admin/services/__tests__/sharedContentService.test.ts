import { sharedContentService, mockSharedContent } from '../sharedContentService';
import { SharedContent } from '../../types/SharedContent';

describe('sharedContentService', () => {
  const initialMockSharedContentData: SharedContent[] = [
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

  let mockSetTimeout: jest.Mock;

  beforeEach(() => {
    // Clear and reset mockSharedContent for each test with a deep copy
    mockSharedContent.splice(0, mockSharedContent.length);
    mockSharedContent.push(...JSON.parse(JSON.stringify(initialMockSharedContentData)));

    // Mock setTimeout to control async operations
    mockSetTimeout = jest.fn((fn, delay) => fn()); // Immediately execute the callback

    jest.spyOn(global, 'setTimeout').mockImplementation(mockSetTimeout);
  });

  afterEach(() => {
    jest.restoreAllMocks(); // Restore original setTimeout
  });

  test('getAllSharedContent returns all shared content', async () => {
    const content = await sharedContentService.getAllSharedContent();
    expect(content).toEqual(initialMockSharedContentData);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('getSharedContentById returns content by id', async () => {
    const content = await sharedContentService.getSharedContentById('sc1');
    expect(content).toEqual(initialMockSharedContentData[0]);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('getSharedContentById returns undefined if content not found', async () => {
    const content = await sharedContentService.getSharedContentById('nonexistent');
    expect(content).toBeUndefined();
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('updateSharedContentStatus updates content status', async () => {
    const result = await sharedContentService.updateSharedContentStatus('sc2', 'approved');
    expect(result).toBe(true);
    const updatedContent = await sharedContentService.getSharedContentById('sc2');
    expect(updatedContent?.trangThaiChiaSe).toBe('approved');
    expect(mockSetTimeout).toHaveBeenCalledTimes(2);
  });

  test('updateSharedContentStatus returns false if content not found', async () => {
    const result = await sharedContentService.updateSharedContentStatus('nonexistent', 'approved');
    expect(result).toBe(false);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('deleteSharedContent deletes content', async () => {
    const result = await sharedContentService.deleteSharedContent('sc1');
    expect(result).toBe(true);
    const content = await sharedContentService.getSharedContentById('sc1');
    expect(content).toBeUndefined();
    expect(mockSetTimeout).toHaveBeenCalledTimes(2);
  });

  test('deleteSharedContent returns false if content not found', async () => {
    const result = await sharedContentService.deleteSharedContent('nonexistent');
    expect(result).toBe(false);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('filterSharedContentByStatus returns filtered content', async () => {
    const pendingContent = await sharedContentService.filterSharedContentByStatus('pending');
    expect(pendingContent).toHaveLength(1);
    expect(pendingContent[0].id).toBe('sc2');
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('searchSharedContent returns matching content by title or content', async () => {
    const searchResults = await sharedContentService.searchSharedContent('kế hoạch');
    expect(searchResults).toHaveLength(1);
    expect(searchResults[0].id).toBe('sc1');

    const searchResults2 = await sharedContentService.searchSharedContent('bài tập');
    expect(searchResults2).toHaveLength(1);
    expect(searchResults2[0].id).toBe('sc2');

    const searchResults3 = await sharedContentService.searchSharedContent('ăn');
    expect(searchResults3).toHaveLength(2); // sc1 and sc3
    expect(mockSetTimeout).toHaveBeenCalledTimes(3);
  });

  test('searchSharedContent returns empty array if no match', async () => {
    const searchResults = await sharedContentService.searchSharedContent('nonexistent');
    expect(searchResults).toHaveLength(0);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });
});

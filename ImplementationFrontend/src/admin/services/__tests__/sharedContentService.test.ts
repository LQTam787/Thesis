import { sharedContentService, mockSharedContent } from '../sharedContentService';
import { SharedContent, SharedContentStatus } from '../../types/SharedContent';

const initialMockSharedContentData: SharedContent[] = [
  {
    id: 'sc1',
    tieuDe: 'Kế hoạch ăn uống lành mạnh',
    noiDung: 'Đây là kế hoạch ăn uống 7 ngày cho người muốn giảm cân.',
    trangThaiChiaSe: SharedContentStatus.APPROVED,
    luotXem: 120,
    luotThich: 45,
    luotBinhLuan: 10,
    luotLuu: 20,
  },
  {
    id: 'sc2',
    tieuDe: 'Bài tập thể dục tại nhà',
    noiDung: 'Hướng dẫn các bài tập đơn giản có thể thực hiện tại nhà.',
    trangThaiChiaSe: SharedContentStatus.PENDING,
    luotXem: 80,
    luotThich: 30,
    luotBinhLuan: 5,
    luotLuu: 15,
  },
  {
    id: 'sc3',
    tieuDe: 'Công thức nấu ăn chay',
    noiDung: 'Tuyển tập các công thức nấu ăn chay ngon miệng và dễ làm.',
    trangThaiChiaSe: SharedContentStatus.APPROVED,
    luotXem: 200,
    luotThich: 80,
    luotBinhLuan: 25,
    luotLuu: 50,
  },
];
let mockSharedContent = [...initialMockSharedContentData];

const mockSetTimeout = jest.spyOn(global, 'setTimeout');

describe('sharedContentService', () => {
  beforeEach(() => {
    // Clear and reset mockSharedContent for each test with a deep copy
    mockSharedContent.splice(0, mockSharedContent.length);
    mockSharedContent.push(...JSON.parse(JSON.stringify(initialMockSharedContentData)));

    // Mock setTimeout to control async operations
    mockSetTimeout.mockImplementation((fn) => fn() as any);
  });

  afterEach(() => {
    mockSetTimeout.mockRestore();
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
    const content = await sharedContentService.getSharedContentById('sc99');
    expect(content).toBeUndefined();
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('updateSharedContentStatus updates content status', async () => {
    const updatedContent = { ...initialMockSharedContentData[1], trangThaiChiaSe: SharedContentStatus.REJECTED };
    const result = await sharedContentService.updateSharedContentStatus('sc2', SharedContentStatus.REJECTED);
    expect(result).toBe(true);
    expect(mockSharedContent[1].trangThaiChiaSe).toBe(SharedContentStatus.REJECTED);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('updateSharedContentStatus returns false if content not found', async () => {
    const result = await sharedContentService.updateSharedContentStatus('sc99', SharedContentStatus.REJECTED);
    expect(result).toBe(false);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('deleteSharedContent deletes content', async () => {
    const result = await sharedContentService.deleteSharedContent('sc1');
    expect(result).toBe(true);
    expect(mockSharedContent).toHaveLength(initialMockSharedContentData.length - 1);
    expect(mockSharedContent.find(c => c.id === 'sc1')).toBeUndefined();
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('deleteSharedContent returns false if content not found', async () => {
    const result = await sharedContentService.deleteSharedContent('sc99');
    expect(result).toBe(false);
    expect(mockSharedContent).toHaveLength(initialMockSharedContentData.length);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('filterSharedContentByStatus returns filtered content', async () => {
    const approvedContent = await sharedContentService.filterSharedContentByStatus(SharedContentStatus.APPROVED);
    expect(approvedContent).toHaveLength(2);
    expect(approvedContent[0].id).toBe('sc1');
    expect(approvedContent[1].id).toBe('sc3');
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });

  test('searchSharedContent returns matching content by title or content', async () => {
    const searchResults = await sharedContentService.searchSharedContent('Kế hoạch');
    expect(searchResults).toHaveLength(1);
    expect(searchResults[0].id).toBe('sc1');

    const searchResults2 = await sharedContentService.searchSharedContent('bài tập');
    expect(searchResults2).toHaveLength(1);
    expect(searchResults2[0].id).toBe('sc2');

    expect(mockSetTimeout).toHaveBeenCalledTimes(2);
  });

  test('searchSharedContent returns empty array if no match', async () => {
    const searchResults = await sharedContentService.searchSharedContent('Không tìm thấy');
    expect(searchResults).toHaveLength(0);
    expect(mockSetTimeout).toHaveBeenCalledTimes(1);
  });
});

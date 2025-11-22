export interface SharedContent {
  id: string;
  tieuDe: string;
  noiDung: string;
  trangThaiChiaSe: string; // Ví dụ: 'pending', 'approved', 'rejected'
  luotXem: number;
  luotThich: number;
  luotBinhLuan: number;
  luotLuu: number;
  // Thêm các thuộc tính khác của nội dung chia sẻ nếu cần
}

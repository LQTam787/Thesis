export interface CheDoDinhDuong {
  maCheDo: string;
  tenCheDo: string;
  moTa?: string;
  ngayBatDau: string; // ISO Date string
  ngayKetThuc?: string; // ISO Date string
  mucTieu: string;
}

export interface MonAn {
  maMonAn: string;
  tenMonAn: string;
  moTa?: string;
  congThuc?: string;
  caloTongCong: number;
  protein: number;
  chatBeo: number;
  carbohydrate: number;
}

export interface ThucPham {
  maThucPham: string;
  tenThucPham: string;
  donViTinh: string;
  calo: number;
  protein: number;
  chatBeo: number;
  carbohydrate: number;
}

export interface ChiTietCheDoDinhDuong {
  maChiTiet: string;
  maCheDo: string;
  maMonAn?: string;
  maThucPham?: string;
  soLuong: number;
  buaAn: string; // Ví dụ: "Bữa sáng", "Bữa trưa", "Bữa tối"
}

export interface NhuCauDinhDuong {
  maNhuCau: string;
  maNguoiDung: string;
  chieuCao: number;
  canNang: number;
  tuoi: number;
  gioiTinh: 'Nam' | 'Nữ';
  mucDoHoatDong: string;
  mucTieuDinhDuong: string;
  caloKhuyenNghi: number;
  proteinKhuyenNghi: number;
  chatBeoKhuyenNghi: number;
  carbohydrateKhuyenNghi: number;
}

export interface CaloMacronutrientData {
  calo: number;
  protein: number;
  fat: number;
  carb: number;
}

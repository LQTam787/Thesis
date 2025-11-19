## Kịch bản Phiên bản 3: Tạo mới Chế độ Dinh dưỡng

**Mục tiêu:** Người dùng (cụ thể là chuyên gia dinh dưỡng hoặc người dùng tự quản lý) có thể tạo một chế độ dinh dưỡng mới và lưu trữ vào hệ thống.

**Tác nhân chính:** Người dùng (Chuyên gia Dinh dưỡng hoặc Người dùng)

**Các tác nhân liên quan:** Hệ thống

**Tiền điều kiện:**
* Người dùng đã đăng nhập thành công vào hệ thống.
* Người dùng có quyền truy cập vào chức năng quản lý chế độ dinh dưỡng.

**Hậu điều kiện:**
* Một chế độ dinh dưỡng mới được tạo và lưu trữ thành công trong hệ thống.
* Thông tin chế độ dinh dưỡng mới được hiển thị trong danh sách.
* Hoặc, hệ thống hiển thị thông báo lỗi nếu quá trình tạo thất bại.

---

**Luồng chính:**

1.  **Người dùng** truy cập vào trang quản lý chế độ dinh dưỡng.
2.  **Hệ thống** hiển thị giao diện `NutritionPlanPageUI` với các trường nhập liệu cho chế độ dinh dưỡng và danh sách các chế độ dinh dưỡng hiện có.
3.  **Người dùng** chọn chức năng "Tạo mới Chế độ Dinh dưỡng" (ví dụ: nhấn nút "Thêm mới").
4.  **NutritionPlanPageUI** hiển thị form trống để người dùng nhập thông tin.
5.  **Người dùng** nhập các thông tin cần thiết vào form (Mã chế độ, Tên chế độ, Mô tả, Ngày bắt đầu, Ngày kết thúc, Mục tiêu).
6.  **Người dùng** nhấn nút "Lưu" hoặc "Tạo chế độ".
7.  **NutritionPlanPageUI** gọi phương thức `layThongTinCheDo()` để thu thập dữ liệu từ các trường nhập liệu.
8.  **NutritionPlanPageUI** gửi dữ liệu chế độ dinh dưỡng (dưới dạng `CheDoDinhDuongData`) đến `NutritionService` thông qua phương thức `luuCheDoDinhDuong(cheDo: CheDoDinhDuong)`.
9.  **NutritionService** nhận dữ liệu, thực hiện kiểm tra tính hợp lệ và các logic nghiệp vụ cần thiết.
10. **NutritionService** gọi `CheDoDinhDuongDAO` thông qua phương thức `save(cheDo: CheDoDinhDuong)` để lưu dữ liệu vào cơ sở dữ liệu.
11. **CheDoDinhDuongDAO** thực hiện thao tác lưu trữ dữ liệu vào cơ sở dữ liệu và trả về đối tượng `CheDoDinhDuong` đã được lưu (bao gồm cả mã ID nếu được tạo tự động).
12. **NutritionService** nhận kết quả từ `CheDoDinhDuongDAO`.
13. **NutritionService** trả về kết quả `true` (thành công) hoặc `false` (thất bại) cho `NutritionPlanPageUI`.
14. **NutritionPlanPageUI** nhận kết quả:
    *   Nếu thành công: Gọi phương thức `hienThiThongBao("Tạo chế độ thành công", "success")` và phương thức `lamSachForm()` để làm sạch các trường nhập liệu. Sau đó, gọi `NutritionService.layTatCaCheDoDinhDuong()` để lấy danh sách cập nhật và hiển thị bằng phương thức `hienThiDanhSachCheDo(plans: CheDoDinhDuongData[])`.
    *   Nếu thất bại: Gọi phương thức `hienThiThongBao("Tạo chế độ thất bại", "error")`.

---

**Luồng phụ (Alternative Flows):**

*   **AF1: Dữ liệu không hợp lệ:**
    *   Tại bước 9: `NutritionService` phát hiện dữ liệu không hợp lệ (ví dụ: thiếu trường bắt buộc, ngày không hợp lệ).
    *   `NutritionService` trả về `false` cho `NutritionPlanPageUI`.
    *   `NutritionPlanPageUI` hiển thị thông báo lỗi chi tiết (ví dụ: "Vui lòng nhập đầy đủ thông tin") bằng phương thức `hienThiThongBao()`.
    *   Người dùng sửa lỗi và thử lại từ bước 6.

*   **AF2: Lỗi hệ thống khi lưu trữ:**
    *   Tại bước 11: `CheDoDinhDuongDAO` gặp lỗi khi cố gắng lưu dữ liệu vào cơ sở dữ liệu (ví dụ: lỗi kết nối, lỗi ràng buộc dữ liệu).
    *   `CheDoDinhDuongDAO` ném ra một ngoại lệ hoặc trả về kết quả thất bại.
    *   `NutritionService` bắt ngoại lệ/nhận kết quả thất bại và trả về `false` cho `NutritionPlanPageUI`.
    *   `NutritionPlanPageUI` hiển thị thông báo lỗi chung (ví dụ: "Đã xảy ra lỗi hệ thống, vui lòng thử lại sau") bằng phương thức `hienThiThongBao()`.

# Kịch bản: Theo dõi dữ liệu hàng ngày (Daily Log Input)

## Phiên bản 3

**Mục tiêu:** Người dùng có thể nhập, chỉnh sửa và lưu trữ dữ liệu về bữa ăn và hoạt động thể chất hàng ngày một cách hiệu quả.

**Tác nhân chính:** Người dùng

**Các tác nhân liên quan:** Hệ thống

---

### Luồng cơ bản (Basic Flow)

1.  **Người dùng** truy cập vào chức năng "Theo dõi dữ liệu hàng ngày".
2.  **Hệ thống** hiển thị `DailyLogInputPage` với form nhập liệu trống và các trường:
    *   Thời gian bữa ăn
    *   Tên món ăn, định lượng món ăn
    *   Tên hoạt động, thời lượng hoạt động, cường độ hoạt động
    *   Các nút "Thêm món ăn", "Thêm hoạt động", "Lưu", "Hủy".
3.  **Người dùng** nhập "Thời gian bữa ăn".
4.  **Người dùng** nhập "Tên món ăn" (ví dụ: "Cơm") và "Định lượng món ăn" (ví dụ: 200g).
5.  **Người dùng** chọn "Thêm món ăn".
6.  **Hệ thống** gọi `DailyLogInputPage.themMonAn(monAn: ThucPham, dinhLuong: Float)`. Bên trong phương thức này, hệ thống sẽ:
    *   Gọi `IThucPhamDAO.timKiemThucPham(ten: String)` để tìm kiếm thực phẩm theo tên.
    *   Gọi `IThucPhamDAO.layThongTinThucPham(thucPhamId: String)` để lấy thông tin chi tiết của thực phẩm được chọn.
    *   Thêm món ăn vào danh sách tạm thời hiển thị trên `DailyLogInputPage`.
7.  **Người dùng** nhập "Tên hoạt động" (ví dụ: "Chạy bộ"), "Thời lượng hoạt động" (ví dụ: 30 phút), "Cường độ hoạt động" (ví dụ: "Trung bình").
8.  **Người dùng** chọn "Thêm hoạt động".
9.  **Hệ thống** gọi `DailyLogInputPage.themHoatDong(hoatDong: HoatDongTheChat, thoiLuong: Int, cuongDo: String)` và thêm hoạt động vào danh sách tạm thời hiển thị trên `DailyLogInputPage`.
10. **Người dùng** tiếp tục thêm các bữa ăn và hoạt động khác nếu cần.
11. **Người dùng** chọn "Lưu".
12. **Hệ thống** gọi `DailyLogInputPage.luuDuLieuHangNgay()`. Bên trong phương thức này, hệ thống sẽ:
    *   Tạo đối tượng `DuLieuThucTe` từ các dữ liệu đã nhập.
    *   Gọi `IDuLieuThucTeDAO.luuDuLieu(duLieu: DuLieuThucTe)` để lưu dữ liệu bữa ăn.
    *   Gọi `IHoatDongTheChatDAO.themHoatDong(hoatDong: HoatDongTheChat)` để lưu dữ liệu hoạt động thể chất.
13. **Hệ thống** thông báo "Dữ liệu hàng ngày đã được lưu thành công."

---

### Luồng thay thế (Alternative Flows)

**AF1: Chỉnh sửa/Xóa mục đã thêm**

*   **Tại bước 6 hoặc 9 của luồng cơ bản:**
    *   **Người dùng** chọn một món ăn hoặc hoạt động đã thêm.
    *   **Hệ thống** cung cấp tùy chọn "Chỉnh sửa" hoặc "Xóa".
    *   **Người dùng** chọn "Chỉnh sửa":
        *   **Hệ thống** hiển thị lại thông tin của mục đó trong form nhập liệu.
        *   **Người dùng** cập nhật thông tin và chọn "Cập nhật".
        *   **Hệ thống** cập nhật mục trong danh sách tạm thời.
    *   **Người dùng** chọn "Xóa":
        *   **Hệ thống** yêu cầu xác nhận xóa.
        *   **Người dùng** xác nhận xóa.
        *   **Hệ thống** xóa mục khỏi danh sách tạm thời.

**AF2: Hủy bỏ thao tác**

*   **Tại bất kỳ bước nào từ 3 đến 10 của luồng cơ bản:**
    *   **Người dùng** chọn "Hủy".
    *   **Hệ thống** hỏi xác nhận hủy bỏ.
    *   **Người dùng** xác nhận hủy bỏ.
    *   **Hệ thống** đóng `DailyLogInputPage` và không lưu bất kỳ dữ liệu nào đã nhập.

---

### Điều kiện trước (Preconditions)

*   Người dùng đã đăng nhập vào hệ thống.
*   Tồn tại dữ liệu thực phẩm và hoạt động thể chất trong hệ thống để tìm kiếm và thêm.

### Điều kiện sau (Postconditions)

*   Dữ liệu bữa ăn và hoạt động thể chất hàng ngày của người dùng được lưu trữ thành công trong cơ sở dữ liệu.
*   Các thay đổi đã được phản ánh trong hệ thống.

## Kịch bản Phiên bản 3: Xóa Chế độ Dinh dưỡng

**Mục tiêu:** Người dùng có thể xóa một chế độ dinh dưỡng không còn cần thiết khỏi hệ thống.

**Tác nhân chính:** Người dùng (Chuyên gia Dinh dưỡng hoặc Người dùng)

**Các tác nhân liên quan:** Hệ thống

**Tiền điều kiện:**
* Người dùng đã đăng nhập thành công vào hệ thống.
* Đã có ít nhất một chế độ dinh dưỡng được lưu trữ trong hệ thống.
* Người dùng có quyền truy cập vào chức năng quản lý chế độ dinh dưỡng.

**Hậu điều kiện:**
* Chế độ dinh dưỡng được chọn đã được xóa thành công khỏi hệ thống.
* Thông tin chế độ dinh dưỡng không còn hiển thị trong danh sách.
* Hoặc, hệ thống hiển thị thông báo lỗi nếu quá trình xóa thất bại.

---

**Luồng chính:**

1.  **Người dùng** truy cập vào trang quản lý chế độ dinh dưỡng.
2.  **Hệ thống** hiển thị giao diện `NutritionPlanPageUI` với danh sách các chế độ dinh dưỡng hiện có.
3.  **Người dùng** chọn một chế độ dinh dưỡng từ danh sách để xóa (ví dụ: nhấn vào biểu tượng "Xóa").
4.  **NutritionPlanPageUI** hiển thị hộp thoại xác nhận (ví dụ: "Bạn có chắc chắn muốn xóa chế độ dinh dưỡng này không?").
5.  **Người dùng** xác nhận muốn xóa (ví dụ: nhấn nút "Đồng ý").
6.  **NutritionPlanPageUI** gọi phương thức `xoaCheDo(maCheDo: string)`.
7.  **NutritionPlanPageUI** gửi yêu cầu xóa chế độ dinh dưỡng (với `maCheDo`) đến `NutritionService` thông qua phương thức `xoaCheDoDinhDuong(maCheDo: string)`.
8.  **NutritionService** nhận yêu cầu và thực hiện các kiểm tra nghiệp vụ cần thiết (ví dụ: kiểm tra quyền).
9.  **NutritionService** gọi `CheDoDinhDuongDAO` thông qua phương thức `delete(maCheDo: string)` để xóa dữ liệu khỏi cơ sở dữ liệu.
10. **CheDoDinhDuongDAO** thực hiện thao tác xóa dữ liệu khỏi cơ sở dữ liệu và trả về kết quả `true` (thành công) hoặc `false` (thất bại).
11. **NutritionService** nhận kết quả từ `CheDoDinhDuongDAO`.
12. **NutritionService** trả về kết quả `true` hoặc `false` cho `NutritionPlanPageUI`.
13. **NutritionPlanPageUI** nhận kết quả:
    *   Nếu thành công: Gọi phương thức `hienThiThongBao("Xóa chế độ thành công", "success")`. Sau đó, gọi `NutritionService.layTatCaCheDoDinhDuong()` để lấy danh sách cập nhật và hiển thị bằng phương thức `hienThiDanhSachCheDo(plans: CheDoDinhDuongData[])`.
    *   Nếu thất bại: Gọi phương thức `hienThiThongBao("Xóa chế độ thất bại", "error")`.

---

**Luồng phụ (Alternative Flows):**

*   **AF1: Người dùng hủy bỏ:**
    *   Tại bước 5: Người dùng chọn hủy bỏ việc xóa (ví dụ: nhấn nút "Hủy").
    *   Hệ thống đóng hộp thoại xác nhận và không thực hiện hành động xóa nào.
    *   Luồng kết thúc.

*   **AF2: Chế độ dinh dưỡng không tồn tại:**
    *   Tại bước 10: `CheDoDinhDuongDAO` không tìm thấy chế độ dinh dưỡng với `maCheDo` được cung cấp để xóa.
    *   `CheDoDinhDuongDAO` trả về `false`.
    *   `NutritionService` trả về `false` cho `NutritionPlanPageUI`.
    *   `NutritionPlanPageUI` hiển thị thông báo lỗi (ví dụ: "Chế độ dinh dưỡng không tồn tại") bằng phương thức `hienThiThongBao()`.

*   **AF3: Lỗi hệ thống khi xóa:**
    *   Tại bước 10: `CheDoDinhDuongDAO` gặp lỗi khi cố gắng xóa dữ liệu khỏi cơ sở dữ liệu.
    *   `CheDoDinhDuongDAO` ném ra một ngoại lệ hoặc trả về kết quả thất bại.
    *   `NutritionService` bắt ngoại lệ/nhận kết quả thất bại và trả về `false` cho `NutritionPlanPageUI`.
    *   `NutritionPlanPageUI` hiển thị thông báo lỗi chung (ví dụ: "Đã xảy ra lỗi hệ thống, vui lòng thử lại sau") bằng phương thức `hienThiThongBao()`.

## Kịch bản Phiên bản 3: Tìm kiếm Món ăn/Thực phẩm

**Mục tiêu:** Người dùng có thể tìm kiếm các món ăn và thực phẩm trong hệ thống để thêm vào chế độ dinh dưỡng hoặc chỉ để tham khảo.

**Tác nhân chính:** Người dùng

**Các tác nhân liên quan:** Hệ thống

**Tiền điều kiện:**
* Người dùng đã đăng nhập thành công vào hệ thống.
* Đã có dữ liệu về món ăn và thực phẩm trong hệ thống.

**Hậu điều kiện:**
* Hệ thống hiển thị danh sách các món ăn và thực phẩm phù hợp với từ khóa tìm kiếm.
* Hoặc, hệ thống hiển thị thông báo không tìm thấy kết quả.

---

**Luồng chính:**

1.  **Người dùng** truy cập vào trang tìm kiếm món ăn/thực phẩm.
2.  **Hệ thống** hiển thị giao diện `FoodSearchPageUI` với trường nhập liệu từ khóa tìm kiếm và khu vực hiển thị kết quả.
3.  **Người dùng** nhập một từ khóa vào trường tìm kiếm (ví dụ: "gà", "rau cải", "cơm").
4.  **Người dùng** nhấn nút "Tìm kiếm" hoặc phím Enter.
5.  **FoodSearchPageUI** gọi phương thức `layTuKhoaTimKiem()` để lấy từ khóa mà người dùng đã nhập.
6.  **FoodSearchPageUI** gửi từ khóa tìm kiếm đến `NutritionService` thông qua phương thức `timKiemMonAnVaThucPham(tuKhoa: string)`.
7.  **NutritionService** nhận từ khóa:
    a.  Gọi `MonAnDAO.findByName(tenMonAn: string)` để tìm kiếm các món ăn.
    b.  Gọi `ThucPhamDAO.findByName(tenThucPham: string)` để tìm kiếm các thực phẩm.
8.  **MonAnDAO** và **ThucPhamDAO** thực hiện truy vấn cơ sở dữ liệu và trả về danh sách `MonAn` và `ThucPham` tương ứng cho `NutritionService`.
9.  **NutritionService** tổng hợp kết quả từ `MonAnDAO` và `ThucPhamDAO`, có thể chuyển đổi chúng thành định dạng `FoodItem[]`.
10. **NutritionService** trả về danh sách `FoodItem[]` cho `FoodSearchPageUI`.
11. **FoodSearchPageUI** nhận kết quả:
    *   Nếu có kết quả: Gọi phương thức `hienThiKetQuaTimKiem(results: FoodItemData[])` để hiển thị danh sách món ăn/thực phẩm trên giao diện.
    *   Nếu không có kết quả: Gọi phương thức `hienThiThongBao("Không tìm thấy kết quả", "info")`.

---

**Luồng phụ (Alternative Flows):**

*   **AF1: Từ khóa tìm kiếm trống:**
    *   Tại bước 5: `FoodSearchPageUI` phát hiện từ khóa tìm kiếm trống.
    *   `FoodSearchPageUI` hiển thị thông báo lỗi (ví dụ: "Vui lòng nhập từ khóa tìm kiếm") bằng phương thức `hienThiThongBao()`.
    *   Luồng kết thúc, người dùng cần nhập từ khóa để tiếp tục.

*   **AF2: Lỗi hệ thống khi tìm kiếm:**
    *   Tại bước 8: `MonAnDAO` hoặc `ThucPhamDAO` gặp lỗi khi truy vấn cơ sở dữ liệu.
    *   Các DAO ném ra ngoại lệ hoặc trả về kết quả thất bại.
    *   `NutritionService` bắt ngoại lệ/nhận kết quả thất bại và trả về một danh sách rỗng hoặc thông báo lỗi cho `FoodSearchPageUI`.
    *   `FoodSearchPageUI` hiển thị thông báo lỗi chung (ví dụ: "Đã xảy ra lỗi hệ thống khi tìm kiếm") bằng phương thức `hienThiThongBao()`.

# Kịch bản phiên bản 3: Đăng nhập

**Tên kịch bản:** Đăng nhập thành công

**Mục tiêu:** Người dùng có thể đăng nhập vào hệ thống với tên tài khoản và mật khẩu hợp lệ.

**Diễn viên chính:** Người dùng

**Các bên liên quan:** Hệ thống xác thực (AuthService), Giao diện người dùng đăng nhập (LoginPageUI), Cơ sở dữ liệu tài khoản (TaiKhoanDAO)

**Điều kiện tiên quyết:**
* Người dùng đã có tài khoản hợp lệ trong hệ thống.
* Trang đăng nhập đang được hiển thị.

**Kích hoạt:** Người dùng nhấp vào nút "Đăng nhập" sau khi nhập thông tin.

## Luồng chính:

1. Người dùng nhập "tenTaiKhoan" vào trường tên tài khoản trên `LoginPageUI`.
2. Người dùng nhập "matKhau" vào trường mật khẩu trên `LoginPageUI`.
3. Người dùng nhấp vào nút "Đăng nhập".
4. `LoginPageUI` gọi phương thức `layTenTaiKhoan()` để lấy tên tài khoản.
5. `LoginPageUI` gọi phương thức `layMatKhau()` để lấy mật khẩu.
6. `LoginPageUI` gửi yêu cầu đăng nhập đến `AuthService` bằng cách gọi phương thức `login(tenTaiKhoan, matKhau)`.
7. `AuthService` gọi phương thức `findByTenTaiKhoan(tenTaiKhoan)` của `TaiKhoanDAO` để tìm kiếm tài khoản.
8. `TaiKhoanDAO` truy vấn cơ sở dữ liệu và trả về đối tượng `TaiKhoan` (nếu tìm thấy).
9. `AuthService` so sánh mật khẩu được cung cấp với mật khẩu đã lưu trong đối tượng `TaiKhoan`.
10. Nếu mật khẩu khớp, `AuthService` tạo và trả về một `JWTToken` cho `LoginPageUI`.
11. `LoginPageUI` nhận `JWTToken` và chuyển hướng người dùng đến trang chính.

## Luồng thay thế:

### A. Đăng nhập thất bại do tên tài khoản không tồn tại:

* **Tại bước 8:** `TaiKhoanDAO` không tìm thấy tài khoản với "tenTaiKhoan" được cung cấp.
* **Tại bước 9:** `AuthService` nhận được giá trị null hoặc không tìm thấy tài khoản.
* **Tại bước 10:** `AuthService` không thể xác thực và trả về lỗi cho `LoginPageUI`.
* **Tại bước 11:** `LoginPageUI` gọi phương thức `hienThiThongBao("Tên tài khoản hoặc mật khẩu không đúng.")` để hiển thị thông báo lỗi và giữ người dùng trên trang đăng nhập.

### B. Đăng nhập thất bại do mật khẩu không đúng:

* **Tại bước 9:** `AuthService` so sánh mật khẩu được cung cấp với mật khẩu đã lưu trong đối tượng `TaiKhoan` và thấy không khớp.
* **Tại bước 10:** `AuthService` không thể xác thực và trả về lỗi cho `LoginPageUI`.
* **Tại bước 11:** `LoginPageUI` gọi phương thức `hienThiThongBao("Tên tài khoản hoặc mật khẩu không đúng.")` để hiển thị thông báo lỗi và giữ người dùng trên trang đăng nhập.

# Kịch bản 3: Tư vấn Chế độ Dinh dưỡng và Tạo Kế hoạch

## Tên kịch bản: Tư vấn Chế độ Dinh dưỡng và Tạo Kế hoạch Dinh dưỡng

### Mục tiêu:
Người dùng có thể nhận được tư vấn dinh dưỡng dựa trên thông tin cá nhân và mục tiêu của họ, sau đó tạo một kế hoạch dinh dưỡng tùy chỉnh hoặc quản lý các kế hoạch hiện có.

### Diễn viên chính:
Người dùng (User)

### Các bên liên quan:
- Hệ thống (System)
- Cơ sở dữ liệu (Database)
- Dịch vụ khuyến nghị (Recommendation Service)

### Điều kiện tiên quyết:
Người dùng đã đăng nhập thành công vào hệ thống.

### Luồng sự kiện chính:

1.  **Người dùng truy cập trang tư vấn dinh dưỡng:**
    *   Người dùng điều hướng đến trang "Tư vấn Chế độ Dinh dưỡng".
    *   Hệ thống hiển thị giao diện nhập thông tin cá nhân và chọn mục tiêu dinh dưỡng (`NutritionalConsultationPage`).

2.  **Người dùng nhập thông tin cá nhân và mục tiêu:**
    *   Người dùng nhập chiều cao, cân nặng, tuổi, giới tính và mức độ hoạt động (`NutritionalConsultationPage.nhapThongTinCaNhan`).
    *   Người dùng chọn các mục tiêu dinh dưỡng mong muốn (ví dụ: giảm cân, tăng cơ) (`NutritionalConsultationPage.chonMucTieuDinhDuong`).

3.  **Hệ thống tính toán và hiển thị khuyến nghị:**
    *   Hệ thống gọi dịch vụ khuyến nghị để tính toán calo và macro khuyến nghị dựa trên thông tin người dùng (`NutritionalConsultationPage.tinhToanKhuyenNghi`).
    *   Hệ thống hiển thị calo, protein, carb, chất béo khuyến nghị trên giao diện người dùng (`NutritionalConsultationPage.hienThiKhuyenNghi`).

4.  **Người dùng quyết định tạo kế hoạch hoặc lưu nhu cầu:**

    *   **Trường hợp A: Tạo kế hoạch dinh dưỡng từ khuyến nghị**
        *   Người dùng chọn "Tạo kế hoạch từ khuyến nghị".
        *   Hệ thống tạo một bản nháp kế hoạch dinh dưỡng mới dựa trên các khuyến nghị (`NutritionalConsultationPage.taoKeHoachTuKhuyen Nghi`).
        *   Hệ thống yêu cầu xác nhận và lưu kế hoạch.
        *   Hệ thống gọi DAO để lưu kế hoạch dinh dưỡng vào cơ sở dữ liệu, trả về `maKeHoach` (`IKeHoachDinhDuongDAO.taoKeHoachDinhDuong`).
        *   Hệ thống chuyển hướng người dùng đến trang chi tiết kế hoạch (`NutritionalConsultationPage.xemChiTietKeHoach`).
        *   Trên trang chi tiết, hệ thống tải danh sách bữa ăn (`IBuaAnDAO.layBuaAnTheoKeHoach`) và chi tiết từng bữa ăn (thực phẩm, định lượng) (`IChiTietBuaAnDAO.layChiTietBuaAn`) để hiển thị.

    *   **Trường hợp B: Lưu nhu cầu dinh dưỡng (không tạo kế hoạch ngay)**
        *   Người dùng chọn "Lưu nhu cầu dinh dưỡng".
        *   Hệ thống lưu các đặc điểm cá nhân và mục tiêu dinh dưỡng của người dùng vào cơ sở dữ liệu (`NutritionalConsultationPage.luuNhuCauDinhDuong`).

5.  **Người dùng quản lý kế hoạch dinh dưỡng:**
    *   Người dùng chọn "Xem kế hoạch của tôi" từ trang tư vấn hoặc một trang khác.
    *   Hệ thống tải danh sách các kế hoạch dinh dưỡng hiện có của người dùng (`NutritionalConsultationPage.taiDanhSachKeHoach` -> `IKeHoachDinhDuongDAO.layDanhSachKeHoach`).
    *   Hệ thống hiển thị danh sách các kế hoạch (`NutritionalConsultationPage.hienThiDanhSachKeHoach`).

    *   **Trường hợp B.1: Xem chi tiết một kế hoạch**
        *   Người dùng chọn một kế hoạch từ danh sách để xem chi tiết (`NutritionalConsultationPage.xemChiTietKeHoach`).
        *   Hệ thống tải chi tiết kế hoạch (`NutritionPlanDetailPage.taiChiTietKeHoach` -> `IKeHoachDinhDuongDAO.layKeHoachTheoMa`).
        *   Hệ thống tải danh sách bữa ăn cho kế hoạch đó (`IBuaAnDAO.layBuaAnTheoKeHoach`).
        *   Hệ thống tải chi tiết các thực phẩm trong mỗi bữa ăn (`IChiTietBuaAnDAO.layChiTietBuaAn` và `IThucPhamDAO.layThucPhamTheoMa`).
        *   Hệ thống hiển thị thực đơn chi tiết (`NutritionPlanDetailPage.hienThiThucDon`).

    *   **Trường hợp B.2: Xóa một kế hoạch**
        *   Người dùng chọn một kế hoạch từ danh sách và chọn "Xóa".
        *   Hệ thống yêu cầu xác nhận xóa.
        *   Hệ thống gọi DAO để xóa kế hoạch khỏi cơ sở dữ liệu (`NutritionalConsultationPage.xoaKeHoach` -> `IKeHoachDinhDuongDAO.xoaKeHoachDinhDuong`).
        *   Hệ thống cập nhật lại danh sách kế hoạch.

### Luồng sự kiện thay thế:

*   **A1: Lỗi khi tính toán khuyến nghị:**
    *   Nếu dịch vụ khuyến nghị gặp lỗi, hệ thống hiển thị thông báo lỗi thân thiện cho người dùng.

*   **A2: Lỗi khi lưu kế hoạch:**
    *   Nếu có lỗi xảy ra trong quá trình lưu kế hoạch dinh dưỡng, hệ thống hiển thị thông báo lỗi và cho phép người dùng thử lại hoặc hủy bỏ.

*   **A3: Không có kế hoạch nào:**
    *   Nếu người dùng chưa có kế hoạch nào, hệ thống hiển thị thông báo "Chưa có kế hoạch nào" và gợi ý tạo kế hoạch mới.

### Điều kiện hậu kỳ:
*   Nếu kế hoạch dinh dưỡng được tạo thành công, một bản ghi mới sẽ tồn tại trong cơ sở dữ liệu.
*   Nếu nhu cầu dinh dưỡng được lưu, các bản ghi đặc điểm cá nhân và mục tiêu dinh dưỡng được cập nhật hoặc tạo mới.
*   Nếu một kế hoạch bị xóa, bản ghi tương ứng sẽ bị loại bỏ khỏi cơ sở dữ liệu.

### Các yêu cầu đặc biệt:
*   Giao diện người dùng phải thân thiện, dễ sử dụng.
*   Phản hồi của hệ thống phải nhanh chóng.
*   Tính toán khuyến nghị phải chính xác dựa trên các thuật toán dinh dưỡng.
*   Dữ liệu nhạy cảm của người dùng phải được bảo mật.

## Kịch bản Phiên bản 3: Tính toán Nhu cầu Dinh dưỡng

**Mục tiêu:** Người dùng có thể nhập các thông số cá nhân để hệ thống tính toán và hiển thị nhu cầu calo và macronutrient khuyến nghị, đồng thời lưu trữ thông tin nhu cầu dinh dưỡng này.

**Tác nhân chính:** Người dùng

**Các tác nhân liên quan:** Hệ thống

**Tiền điều kiện:**
* Người dùng đã đăng nhập thành công vào hệ thống.
* Người dùng đã có thể truy cập vào trang nhập liệu nhu cầu dinh dưỡng.

**Hậu điều kiện:**
* Nhu cầu calo và macronutrient khuyến nghị được hiển thị cho người dùng.
* Thông tin nhu cầu dinh dưỡng của người dùng được lưu trữ hoặc cập nhật trong hệ thống.
* Hoặc, hệ thống hiển thị thông báo lỗi nếu quá trình tính toán hoặc lưu thất bại.

---

**Luồng chính:**

1.  **Người dùng** truy cập vào trang nhập liệu nhu cầu dinh dưỡng.
2.  **Hệ thống** hiển thị giao diện `NutritionalNeedsInputPageUI` với các trường nhập liệu (chiều cao, cân nặng, tuổi, giới tính, mức độ hoạt động, mục tiêu dinh dưỡng) và khu vực hiển thị kết quả khuyến nghị.
3.  **Người dùng** nhập các thông tin cá nhân cần thiết.
4.  **Người dùng** nhấn nút "Tính toán" hoặc "Lưu".
5.  **NutritionalNeedsInputPageUI** gọi phương thức `layThongTinNhuCau()` để thu thập dữ liệu từ các trường nhập liệu.
6.  **NutritionalNeedsInputPageUI** gửi các thông số (cân nặng, chiều cao, tuổi, giới tính, mức độ hoạt động) đến `NutritionService` thông qua phương thức `tinhToanNhuCauCalo(canNang: number, chieuCao: number, tuoi: number, gioiTinh: string, mucDoHoatDong: string)`.
7.  **NutritionService** nhận các thông số và thực hiện tính toán nhu cầu calo và macronutrient (protein, chất béo, carbohydrate) dựa trên các công thức đã định nghĩa.
8.  **NutritionService** trả về đối tượng `CaloMacronutrientData` (bao gồm calo, protein, fat, carb khuyến nghị) cho `NutritionalNeedsInputPageUI`.
9.  **NutritionalNeedsInputPageUI** nhận kết quả và gọi phương thức `hienThiKhuyenNghi(calo: number, protein: number, fat: number, carb: number)` để hiển thị các giá trị khuyến nghị lên giao diện.

10. **<Điều kiện rẽ nhánh>**
    *   **Người dùng chỉ tính toán:** Luồng kết thúc sau khi hiển thị kết quả.
    *   **Người dùng muốn lưu:**
        a.  **NutritionalNeedsInputPageUI** gửi đối tượng `NhuCauDinhDuongData` (bao gồm cả các giá trị khuyến nghị) đến `NutritionService` thông qua phương thức `luuNhuCauDinhDuong(nhuCau: NhuCauDinhDuong)`.
        b.  **NutritionService** nhận dữ liệu và thực hiện kiểm tra tính hợp lệ.
        c.  **NutritionService** kiểm tra xem `NhuCauDinhDuong` của người dùng này đã tồn tại chưa bằng cách gọi `NhuCauDinhDuongDAO.findByMaNguoiDung(maNguoiDung: string)`.
        d.  Nếu chưa tồn tại: **NutritionService** gọi `NhuCauDinhDuongDAO.save(nhuCau: NhuCauDinhDuong)` để lưu mới.
        e.  Nếu đã tồn tại: **NutritionService** gọi `NhuCauDinhDuongDAO.update(nhuCau: NhuCauDinhDuong)` để cập nhật.
        f.  **NhuCauDinhDuongDAO** thực hiện thao tác lưu/cập nhật dữ liệu vào cơ sở dữ liệu và trả về đối tượng `NhuCauDinhDuong` đã được lưu/cập nhật.
        g.  **NutritionService** nhận kết quả từ `NhuCauDinhDuongDAO`.
        h.  **NutritionService** trả về kết quả `true` (thành công) hoặc `false` (thất bại) cho `NutritionalNeedsInputPageUI`.
        i.  **NutritionalNeedsInputPageUI** nhận kết quả:
            *   Nếu thành công: Gọi phương thức `hienThiThongBao("Lưu nhu cầu thành công", "success")`.
            *   Nếu thất bại: Gọi phương thức `hienThiThongBao("Lưu nhu cầu thất bại", "error")`.

---

**Luồng phụ (Alternative Flows):**

*   **AF1: Dữ liệu nhập không hợp lệ:**
    *   Tại bước 7 (trước khi tính toán) hoặc bước 10.b (trước khi lưu): `NutritionService` phát hiện dữ liệu không hợp lệ (ví dụ: chiều cao, cân nặng âm).
    *   `NutritionService` trả về thông báo lỗi hoặc giá trị đặc biệt.
    *   `NutritionalNeedsInputPageUI` hiển thị thông báo lỗi chi tiết (ví dụ: "Vui lòng nhập các thông số hợp lệ") bằng phương thức `hienThiThongBao()`.

*   **AF2: Lỗi hệ thống khi lưu trữ:**
    *   Tại bước 10.d hoặc 10.e: `NhuCauDinhDuongDAO` gặp lỗi khi cố gắng lưu/cập nhật dữ liệu.
    *   `NhuCauDinhDuongDAO` ném ra một ngoại lệ hoặc trả về kết quả thất bại.
    *   `NutritionService` bắt ngoại lệ/nhận kết quả thất bại và trả về `false` cho `NutritionalNeedsInputPageUI`.
    *   `NutritionalNeedsInputPageUI` hiển thị thông báo lỗi chung (ví dụ: "Đã xảy ra lỗi hệ thống khi lưu nhu cầu") bằng phương thức `hienThiThongBao()`.

# Kịch bản Phiên bản 3: Quản lý Hồ sơ Cá nhân

## Tên kịch bản
Quản lý Hồ sơ Cá nhân

## Mục tiêu
Người dùng có thể xem, chỉnh sửa và cập nhật thông tin hồ sơ cá nhân, tài khoản và đặc điểm cá nhân của mình.

## Tác nhân chính
Người dùng (Người dùng đã đăng nhập)

## Các bước thực hiện

### Luồng cơ bản

1.  **Người dùng** truy cập trang "Quản lý Hồ sơ Cá nhân".
2.  **Hệ thống** gọi `initialize()` của `ProfileController`.
3.  **ProfileController** yêu cầu `NguoiDungDAO` lấy thông tin `NguoiDung` bằng cách gọi `getNguoiDungById(userId)`.
4.  **ProfileController** yêu cầu `DacDiemCaNhanDAO` lấy thông tin `DacDiemCaNhan` bằng cách gọi `getDacDiemCaNhanByUserId(userId)`.
5.  **ProfileController** chuyển thông tin hồ sơ (`NguoiDung` và `DacDiemCaNhan`) cho `ProfileManagementPage`.
6.  **ProfileManagementPage** hiển thị thông tin hồ sơ hiện tại của người dùng bằng cách gọi `displayProfile(profileData, dacDiemCaNhanData)`.
7.  **Người dùng** xem thông tin hồ sơ.
8.  **Người dùng** nhấn nút "Chỉnh sửa" (hoặc tương tự).
9.  **Người dùng** chỉnh sửa các trường thông tin như tên thật, email, chiều cao, cân nặng, mức độ vận động, bệnh lý nền, dị ứng.
10. **Người dùng** nhấn nút "Lưu".
11. **Hệ thống** gọi `saveProfile()` của `ProfileController`.
12. **ProfileController** gọi `getEditedProfileData()` của `ProfileManagementPage` để lấy dữ liệu hồ sơ đã chỉnh sửa (`tenThat`, `email`).
13. **ProfileController** tạo đối tượng `NguoiDung` với dữ liệu mới và gọi `updateNguoiDung(nguoiDung)` của `NguoiDungDAO`.
14. **ProfileController** gọi `getEditedDacDiemCaNhanData()` của `ProfileManagementPage` để lấy dữ liệu đặc điểm cá nhân đã chỉnh sửa (`chieuCao`, `canNang`, `mucDoVanDong`, `benhLyNen`, `diUng`).
15. **ProfileController** kiểm tra xem `DacDiemCaNhan` đã tồn tại chưa bằng cách gọi `getDacDiemCaNhanByUserId(userId)`.
    *   Nếu tồn tại: **ProfileController** tạo đối tượng `DacDiemCaNhan` với dữ liệu mới và gọi `updateDacDiemCaNhan(dacDiemCaNhan)` của `DacDiemCaNhanDAO`.
    *   Nếu không tồn tại: **ProfileController** tạo đối tượng `DacDiemCaNhan` với dữ liệu mới và gọi `createDacDiemCaNhan(dacDiemCaNhan)` của `DacDiemCaNhanDAO`.
16. **ProfileController** hiển thị thông báo thành công thông qua `ProfileManagementPage` bằng cách gọi `showSuccessMessage("Hồ sơ đã được cập nhật thành công!")`.
17. **ProfileManagementPage** hiển thị thông báo thành công cho **Người dùng**.

### Luồng thay thế: Thay đổi mật khẩu

10a. **Người dùng** chọn tùy chọn "Thay đổi mật khẩu".
10b. **Người dùng** nhập mật khẩu mới vào `passwordInput` và xác nhận mật khẩu vào `confirmPasswordInput`.
10c. **Người dùng** nhấn nút "Lưu".
10d. **Hệ thống** gọi `saveProfile()` của `ProfileController`.
10e. **ProfileController** gọi `getEditedAccountData()` của `ProfileManagementPage` để lấy mật khẩu mới.
10f. **ProfileController** xác thực mật khẩu mới (kiểm tra độ mạnh, khớp với xác nhận).
    *   Nếu mật khẩu không hợp lệ: **ProfileController** hiển thị thông báo lỗi thông qua `ProfileManagementPage` bằng cách gọi `showErrorMessage("Mật khẩu không hợp lệ.")`. Kịch bản kết thúc với lỗi.
10g. **ProfileController** tạo đối tượng `TaiKhoan` với mật khẩu mới và gọi `updateTaiKhoan(taiKhoan)` của `TaiKhoanDAO`.
10h. Tiếp tục từ bước 12 của luồng cơ bản.

### Luồng thay thế: Hủy chỉnh sửa

1.  **Người dùng** nhấn nút "Hủy".
2.  **Hệ thống** gọi `cancelEdit()` của `ProfileController`.
3.  **ProfileController** chuyển hướng người dùng hoặc tải lại dữ liệu hồ sơ ban đầu (tùy thuộc vào implement).

### Luồng thay thế: Cập nhật thất bại

1.  Trong bước 13, 15 (cập nhật/tạo `DacDiemCaNhan`) hoặc 10g (cập nhật `TaiKhoan`), nếu thao tác DAO trả về `false` (cập nhật thất bại).
2.  **ProfileController** hiển thị thông báo lỗi thông qua `ProfileManagementPage` bằng cách gọi `showErrorMessage("Đã xảy ra lỗi khi cập nhật hồ sơ. Vui lòng thử lại.")`.
3.  **ProfileManagementPage** hiển thị thông báo lỗi cho **Người dùng**.

# Kịch bản: Xem báo cáo tiến độ và dinh dưỡng (Progress Report)

## Phiên bản 3

**Mục tiêu:** Người dùng có thể xem báo cáo tiến độ về cân nặng, chỉ số cơ thể, phần trăm mục tiêu đạt được, báo cáo dinh dưỡng về macro và vi chất dinh dưỡng đã tiêu thụ, cùng với các gợi ý điều chỉnh để đạt được mục tiêu dinh dưỡng.

**Tác nhân chính:** Người dùng

**Các tác nhân liên quan:** Hệ thống

---

### Luồng cơ bản (Basic Flow)

1.  **Người dùng** truy cập vào chức năng "Xem báo cáo tiến độ và dinh dưỡng".
2.  **Hệ thống** gọi `IKeHoachDinhDuongDAO.layKeHoachHienTai(nguoiDungId: String)` để lấy kế hoạch dinh dưỡng hiện tại của người dùng.
3.  **Hệ thống** lấy `DuLieuThucTe` của người dùng (có thể thông qua `IDuLieuThucTeDAO.layDuLieuTheoNgay(ngay: Date)` hoặc một phương thức tương tự để lấy nhiều dữ liệu).
4.  **Hệ thống** gọi `ProgressReportPage.hienThiBieuDoTienDo()`.
5.  **Hệ thống** gọi `IReportDAO.taoBieuDoTienDo(keHoachId: String, duLieuThucTe: List<DuLieuThucTe>)` để tạo `BieuDoTienDo`.
6.  **Hệ thống** hiển thị `BieuDoTienDo` trên `ProgressReportPage` với các thông tin:
    *   Cân nặng hiện tại
    *   Chỉ số cơ thể (ví dụ: BMI)
    *   Phần trăm mục tiêu đạt được
7.  **Hệ thống** gọi `ProgressReportPage.hienThiBaoCaoDinhDuong()`.
8.  **Hệ thống** gọi `IReportDAO.taoBaoCaoDinhDuong(keHoachId: String, duLieuThucTe: DuLieuThucTe)` để tạo `BaoCaoDinhDuong`.
9.  **Hệ thống** hiển thị `BaoCaoDinhDuong` trên `ProgressReportPage` với các thông tin:
    *   Macro tiêu thụ (Protein, Carb, Fat)
    *   Micro-nutrient tiêu thụ (Vitamin, Khoáng chất)
10. **Hệ thống** gọi `ProgressReportPage.hienThiGoiYDieuChinh()`.
11. **Hệ thống** gọi `IReportDAO.taoGoiYDieuChinh(baoCao: BaoCaoDinhDuong, mucTieu: MucTieuDinhDuong)` để tạo `GoiYDieuChinh`.
12. **Hệ thống** hiển thị `GoiYDieuChinh` trên `ProgressReportPage` với nội dung gợi ý điều chỉnh dựa trên báo cáo và mục tiêu dinh dưỡng của người dùng.

---

### Luồng thay thế (Alternative Flows)

**AF1: Không có dữ liệu để tạo báo cáo**

*   **Tại bước 2 hoặc 3 của luồng cơ bản:**
    *   Nếu không có kế hoạch dinh dưỡng hoặc dữ liệu thực tế nào của người dùng:
    *   **Hệ thống** hiển thị thông báo "Không có đủ dữ liệu để tạo báo cáo. Vui lòng nhập dữ liệu hàng ngày hoặc tạo kế hoạch dinh dưỡng."
    *   **Người dùng** có thể quay lại trang trước hoặc chuyển đến chức năng nhập liệu.

**AF2: Tải lại báo cáo**

*   **Tại bước 12 của luồng cơ bản:**
    *   **Người dùng** chọn nút "Tải lại báo cáo".
    *   **Hệ thống** gọi `ProgressReportPage.taiLaiBaoCao()`. Điều này sẽ kích hoạt lại các bước từ 2 đến 12 để cập nhật báo cáo với dữ liệu mới nhất.
    *   **Hệ thống** hiển thị báo cáo đã được cập nhật.

---

### Điều kiện trước (Preconditions)

*   Người dùng đã đăng nhập vào hệ thống.
*   Người dùng đã có ít nhất một kế hoạch dinh dưỡng và một số dữ liệu theo dõi hàng ngày.

### Điều kiện sau (Postconditions)

*   Người dùng đã xem được báo cáo tiến độ, báo cáo dinh dưỡng và các gợi ý điều chỉnh.

# Kịch bản phiên bản 3: Đăng ký

**Tên kịch bản:** Đăng ký thành công

**Mục tiêu:** Người dùng mới có thể đăng ký tài khoản thành công vào hệ thống.

**Diễn viên chính:** Người dùng

**Các bên liên quan:** Hệ thống xác thực (AuthService), Giao diện người dùng đăng ký (RegisterPageUI), Cơ sở dữ liệu tài khoản (TaiKhoanDAO), Cơ sở dữ liệu người dùng (NguoiDungDAO)

**Điều kiện tiên quyết:**
* Trang đăng ký đang được hiển thị.

**Kích hoạt:** Người dùng nhấp vào nút "Đăng ký" sau khi nhập thông tin.

## Luồng chính:

1. Người dùng nhập "tenThat" vào trường tên thật trên `RegisterPageUI`.
2. Người dùng nhập "email" vào trường email trên `RegisterPageUI`.
3. Người dùng nhập "tenTaiKhoan" vào trường tên tài khoản trên `RegisterPageUI`.
4. Người dùng nhập "matKhau" vào trường mật khẩu trên `RegisterPageUI`.
5. Người dùng nhập "xacNhanMatKhau" vào trường xác nhận mật khẩu trên `RegisterPageUI`.
6. Người dùng nhấp vào nút "Đăng ký".
7. `RegisterPageUI` gọi phương thức `layTenThat()`.
8. `RegisterPageUI` gọi phương thức `layEmail()`.
9. `RegisterPageUI` gọi phương thức `layTenTaiKhoan()`.
10. `RegisterPageUI` gọi phương thức `layMatKhau()`.
11. `RegisterPageUI` gọi phương thức `layXacNhanMatKhau()`.
12. `RegisterPageUI` kiểm tra xem "matKhau" và "xacNhanMatKhau" có khớp nhau không.
13. Nếu khớp, `RegisterPageUI` tạo đối tượng `NguoiDungData` và gửi yêu cầu đăng ký đến `AuthService` bằng cách gọi phương thức `register(nguoiDungData)`.
14. `AuthService` gọi phương thức `save(taiKhoan)` của `TaiKhoanDAO` để lưu thông tin tài khoản.
15. `TaiKhoanDAO` lưu tài khoản vào cơ sở dữ liệu và trả về đối tượng `TaiKhoan` đã lưu.
16. `AuthService` gọi phương thức `save(nguoiDung)` của `NguoiDungDAO` để lưu thông tin người dùng.
17. `NguoiDungDAO` lưu thông tin người dùng vào cơ sở dữ liệu và trả về đối tượng `NguoiDung` đã lưu.
18. `AuthService` trả về `true` (đăng ký thành công) cho `RegisterPageUI`.
19. `RegisterPageUI` gọi phương thức `hienThiThongBao("Đăng ký thành công! Vui lòng đăng nhập.")`.
20. `RegisterPageUI` chuyển hướng người dùng đến trang Đăng nhập.

## Luồng thay thế:

### A. Đăng ký thất bại do mật khẩu không khớp:

* **Tại bước 12:** `RegisterPageUI` kiểm tra và thấy "matKhau" và "xacNhanMatKhau" không khớp.
* **Tại bước 13:** `RegisterPageUI` gọi phương thức `hienThiThongBao("Mật khẩu và xác nhận mật khẩu không khớp.")` và giữ người dùng trên trang đăng ký.

### B. Đăng ký thất bại do tên tài khoản hoặc email đã tồn tại:

* **Tại bước 14 hoặc 16:** `TaiKhoanDAO` hoặc `NguoiDungDAO` phát hiện tên tài khoản hoặc email đã tồn tại và trả về lỗi.
* **Tại bước 18:** `AuthService` nhận được lỗi và trả về `false` (đăng ký thất bại) cho `RegisterPageUI`.
* **Tại bước 19:** `RegisterPageUI` gọi phương thức `hienThiThongBao("Tên tài khoản hoặc email đã tồn tại.")` và giữ người dùng trên trang đăng ký.

# Kịch Bản Chia Sẻ Kế Hoạch/Hoạt Động (Phiên Bản 3)

## 1. Giới Thiệu
Kịch bản này mô tả luồng người dùng và hệ thống khi một người dùng muốn chia sẻ một kế hoạch dinh dưỡng hoặc hoạt động cá nhân lên cộng đồng hoặc chỉ cho một nhóm người dùng cụ thể.

## 2. Mục Tiêu
- Cho phép người dùng chia sẻ kế hoạch dinh dưỡng hoặc hoạt động.
- Đảm bảo tính toàn vẹn và bảo mật của dữ liệu chia sẻ.
- Cung cấp khả năng hiển thị thông báo phản hồi cho người dùng.

## 3. Các Bên Liên Quan
- **Người Dùng:** Cá nhân muốn chia sẻ kế hoạch/hoạt động.
- **Hệ Thống:** Ứng dụng web/di động xử lý yêu cầu chia sẻ.

## 4. Điều Kiện Tiên Quyết
- Người dùng đã đăng nhập vào hệ thống.
- Người dùng đã tạo ít nhất một kế hoạch dinh dưỡng (nếu muốn chia sẻ kế hoạch).
- Người dùng đang ở trên trang Chia Sẻ Kế Hoạch/Hoạt Động.

## 5. Luồng Sự Kiện Chính

| Bước | Tác Nhân | Hành Động |
|---|---|---|
| 1 | Người Dùng | Truy cập trang Chia Sẻ Kế Hoạch/Hoạt Động. |
| 2 | Hệ Thống (UI) | Hiển thị giao diện nhập liệu cho tiêu đề, nội dung, và danh sách các kế hoạch dinh dưỡng có sẵn của người dùng (nếu có). Giao diện cũng bao gồm tùy chọn chọn trạng thái chia sẻ (công khai/riêng tư). |
| 3 | Người Dùng | Nhập **tiêu đề** cho nội dung chia sẻ (ví dụ: "Kế hoạch giảm cân 1 tháng"). |
| 4 | Người Dùng | Nhập **nội dung** chi tiết về kế hoạch/hoạt động (ví dụ: "Đây là kế hoạch tập luyện và ăn uống giúp tôi giảm 5kg trong 1 tháng..."). |
| 5 | Người Dùng | Chọn một **kế hoạch dinh dưỡng** từ danh sách thả xuống (nếu có và muốn chia sẻ kế hoạch). Hành động này là tùy chọn. |
| 6 | Người Dùng | Chọn **trạng thái chia sẻ** (ví dụ: "Công khai" để mọi người có thể xem hoặc "Riêng tư" để chỉ người dùng được phép xem). |
| 7 | Người Dùng | Nhấn nút "Chia Sẻ". |
| 8 | Hệ Thống (UI) | Gọi phương thức `handleSubmit()` để xử lý dữ liệu nhập vào. |
| 9 | Hệ Thống (UI) | `UISharePlanActivityPage.handleSubmit()`: Thực hiện kiểm tra tính hợp lệ của dữ liệu (tiêu đề, nội dung không được trống). |
| 10 | Hệ Thống (UI) | Nếu dữ liệu hợp lệ, tạo một đối tượng `NoiDungChiaSe` và gọi phương thức `INoiDungChiaSeDAO.taoNoiDungChiaSe(noiDung)` để lưu dữ liệu vào cơ sở dữ liệu. Đồng thời, gọi `INoiDungChiaSeDAO.capNhatNoiDungChiaSe(noiDung)` để cập nhật trạng thái nếu cần. |
| 11 | Hệ Thống (DAO) | `INoiDungChiaSeDAO.taoNoiDungChiaSe(noiDung)` và `INoiDungChiaSeDAO.capNhatNoiDungChiaSe(noiDung)`: Thực hiện các thao tác lưu/cập nhật dữ liệu vào cơ sở dữ liệu. |
| 12 | Hệ Thống (DAO) | Trả về kết quả `true` nếu lưu/cập nhật thành công, `false` nếu thất bại. |
| 13 | Hệ Thống (UI) | Nếu kết quả từ DAO là `true`, `UISharePlanActivityPage.displayMessage("success", "Nội dung đã được chia sẻ thành công!")` được gọi để hiển thị thông báo thành công. |
| 14 | Hệ Thống (UI) | Nếu kết quả từ DAO là `false`, `UISharePlanActivityPage.displayMessage("error", "Đã xảy ra lỗi khi chia sẻ nội dung. Vui lòng thử lại.")` được gọi để hiển thị thông báo lỗi. |
| 15 | Người Dùng | Xem thông báo phản hồi từ hệ thống. |

## 6. Luồng Sự Kiện Thay Thế

### 6.1. Dữ liệu nhập không hợp lệ (Tại Bước 9)
| Bước | Tác Nhân | Hành Động |
|---|---|---|
| 9.1 | Hệ Thống (UI) | `UISharePlanActivityPage.handleSubmit()`: Phát hiện dữ liệu nhập vào không hợp lệ (ví dụ: tiêu đề trống). |
| 9.2 | Hệ Thống (UI) | `UISharePlanActivityPage.displayMessage("error", "Lỗi: Vui lòng nhập đầy đủ tiêu đề và nội dung.")` được gọi để hiển thị thông báo lỗi. |
| 9.3 | Người Dùng | Xem thông báo lỗi và sửa lại thông tin. |

### 6.2. Không tải được kế hoạch dinh dưỡng (Tại Bước 2)
| Bước | Tác Nhân | Hành Động |
|---|---|---|
| 2.1 | Hệ Thống (UI) | `UISharePlanActivityPage.loadNutritionPlans(userId)`: Gặp lỗi khi tải danh sách kế hoạch dinh dưỡng. |
| 2.2 | Hệ Thống (UI) | `UISharePlanActivityPage.displayMessage("warning", "Không thể tải danh sách kế hoạch dinh dưỡng. Vui lòng thử lại sau.")` được gọi để hiển thị thông báo. |
| 2.3 | Người Dùng | Trang vẫn hiển thị, nhưng phần chọn kế hoạch dinh dưỡng có thể bị vô hiệu hóa hoặc không có tùy chọn nào. Người dùng có thể tiếp tục chia sẻ mà không cần liên kết với một kế hoạch cụ thể. |

## 7. Điều Kiện Hậu Quyết
- Nếu chia sẻ thành công: Nội dung chia sẻ mới được lưu vào cơ sở dữ liệu, và người dùng nhận được thông báo thành công.
- Nếu chia sẻ thất bại: Người dùng nhận được thông báo lỗi, và nội dung không được lưu.

# Kịch bản Phiên bản 3: Quản lý Nội dung Chia sẻ (Admin)

## Tên kịch bản
Quản lý Nội dung Chia sẻ

## Mục tiêu
Admin có thể xem, duyệt, từ chối, xóa, lọc và tìm kiếm nội dung chia sẻ.

## Tác nhân
Quản trị viên (Admin)

## Điều kiện tiên quyết
Admin đã đăng nhập thành công vào hệ thống và có quyền truy cập module quản lý nội dung chia sẻ.

## Luồng sự kiện chính

1.  **AdminDashboardView hiển thị:** Admin nhìn thấy bảng điều khiển chính.
2.  **Admin chọn "Quản lý Nội dung Chia sẻ":** Admin nhấp vào tùy chọn "Quản lý Nội dung Chia sẻ".
3.  **SharedContentManagementView hiển thị danh sách nội dung:**
    *   `SharedContentManagementView` gọi `SharedContentManagementController.taiDanhSachNoiDung()`.
    *   `SharedContentManagementController` gọi `INoiDungChiaSeDAO.layTatCaNoiDungChiaSe()`.
    *   `INoiDungChiaSeDAO` trả về danh sách `NoiDungChiaSe` cho `SharedContentManagementController`.
    *   `SharedContentManagementController` chuyển danh sách `NoiDungChiaSe` cho `SharedContentManagementView`.
    *   `SharedContentManagementView.hienThiDanhSachNoiDung(noiDung: List<NoiDungChiaSe>)` hiển thị danh sách nội dung trên giao diện.
4.  **Admin thực hiện hành động:** Admin có thể chọn một trong các hành động sau:
    *   **Xem chi tiết nội dung:** Admin chọn một nội dung từ danh sách.
        *   `SharedContentManagementView.hienThiChiTietNoiDung(noiDung: NoiDungChiaSe)` hiển thị thông tin chi tiết của nội dung đó.
    *   **Duyệt nội dung:** Admin chọn một nội dung và nhấp "Duyệt".
        *   `SharedContentManagementView` gọi `SharedContentManagementController.xuLyDuyetNoiDung(id: String)`.
        *   `SharedContentManagementController` gọi `INoiDungChiaSeDAO.capNhatTrangThaiNoiDung(id: String, trangThai: String)` với trạng thái "Đã duyệt".
        *   `INoiDungChiaSeDAO` trả về kết quả (true/false) cho `SharedContentManagementController`.
        *   Nếu thành công, `SharedContentManagementController` gọi `SharedContentManagementView.hienThiThongBao(tinNhan: String)` và `SharedContentManagementController.taiDanhSachNoiDung()` để cập nhật danh sách.
        *   Nếu thất bại, `SharedContentManagementController` gọi `SharedContentManagementView.hienThiThongBao(tinNhan: String)`.
    *   **Từ chối nội dung:** Admin chọn một nội dung và nhấp "Từ chối".
        *   `SharedContentManagementView` gọi `SharedContentManagementController.xuLyTuChoiNoiDung(id: String)`.
        *   `SharedContentManagementController` gọi `INoiDungChiaSeDAO.capNhatTrangThaiNoiDung(id: String, trangThai: String)` với trạng thái "Đã từ chối".
        *   `INoiDungChiaSeDAO` trả về kết quả (true/false) cho `SharedContentManagementController`.
        *   Nếu thành công, `SharedContentManagementController` gọi `SharedContentManagementView.hienThiThongBao(tinNhan: String)` và `SharedContentManagementController.taiDanhSachNoiDung()` để cập nhật danh sách.
        *   Nếu thất bại, `SharedContentManagementController` gọi `SharedContentManagementView.hienThiThongBao(tinNhan: String)`.
    *   **Xóa nội dung:** Admin chọn một nội dung và nhấp "Xóa".
        *   `SharedContentManagementView` yêu cầu xác nhận xóa.
        *   Admin xác nhận xóa.
        *   `SharedContentManagementView` gọi `SharedContentManagementController.xuLyXoaNoiDung(id: String)`.
        *   `SharedContentManagementController` gọi `INoiDungChiaSeDAO.xoaNoiDungChiaSe(id: String)`.
        *   `INoiDungChiaSeDAO` trả về kết quả (true/false) cho `SharedContentManagementController`.
        *   Nếu thành công, `SharedContentManagementController` gọi `SharedContentManagementView.hienThiThongBao(tinNhan: String)` và `SharedContentManagementController.taiDanhSachNoiDung()` để cập nhật danh sách.
        *   Nếu thất bại, `SharedContentManagementController` gọi `SharedContentManagementView.hienThiThongBao(tinNhan: String)`.
    *   **Lọc nội dung:** Admin chọn một trạng thái để lọc (ví dụ: "Đang chờ duyệt").
        *   `SharedContentManagementView` gọi `SharedContentManagementController.xuLyLocNoiDung(trangThai: String)`.
        *   `SharedContentManagementController` gọi `INoiDungChiaSeDAO.locNoiDungTheoTrangThai(trangThai: String)`.
        *   `INoiDungChiaSeDAO` trả về danh sách `NoiDungChiaSe` đã lọc cho `SharedContentManagementController`.
        *   `SharedContentManagementController` chuyển danh sách cho `SharedContentManagementView`.
        *   `SharedContentManagementView.hienThiDanhSachNoiDung(noiDung: List<NoiDungChiaSe>)` hiển thị danh sách nội dung đã lọc.
    *   **Tìm kiếm nội dung:** Admin nhập từ khóa vào ô tìm kiếm và nhấp "Tìm kiếm".
        *   `SharedContentManagementView` gọi `SharedContentManagementController.xuLyTimKiemNoiDung(tuKhoa: String)`.
        *   `SharedContentManagementController` gọi `INoiDungChiaSeDAO.timKiemNoiDung(tuKhoa: String)`.
        *   `INoiDungChiaSeDAO` trả về danh sách `NoiDungChiaSe` đã tìm kiếm cho `SharedContentManagementController`.
        *   `SharedContentManagementController` chuyển danh sách cho `SharedContentManagementView`.
        *   `SharedContentManagementView.hienThiDanhSachNoiDung(noiDung: List<NoiDungChiaSe>)` hiển thị danh sách nội dung đã tìm kiếm.

## Luồng sự kiện thay thế

*   **Không tìm thấy nội dung:** Nếu `INoiDungChiaSeDAO.layNoiDungChiaSeTheoId()` không tìm thấy nội dung, `SharedContentManagementView.hienThiThongBao()` sẽ hiển thị thông báo lỗi.
*   **Lỗi hệ thống:** Nếu có bất kỳ lỗi nào xảy ra trong quá trình gọi DAO, `SharedContentManagementController` sẽ bắt lỗi và gọi `SharedContentManagementView.hienThiThongBao()` với thông báo lỗi thích hợp.

## Điều kiện hậu kỳ
Trạng thái hoặc thông tin nội dung chia sẻ được cập nhật chính xác trong cơ sở dữ liệu và giao diện người dùng phản ánh sự thay đổi.

# Kịch bản Phiên bản 3: Quản lý Người dùng (Admin)

## Tên kịch bản
Quản lý Người dùng

## Mục tiêu
Admin có thể xem, thêm, sửa, xóa thông tin người dùng trong hệ thống.

## Tác nhân
Quản trị viên (Admin)

## Điều kiện tiên quyết
Admin đã đăng nhập thành công vào hệ thống và có quyền truy cập module quản lý người dùng.

## Luồng sự kiện chính

1.  **AdminDashboardView hiển thị:** Admin nhìn thấy bảng điều khiển chính.
2.  **Admin chọn "Quản lý Người dùng":** Admin nhấp vào tùy chọn "Quản lý Người dùng".
3.  **UserManagementView hiển thị danh sách người dùng:**
    *   `UserManagementView` gọi `UserManagementController.taiDanhSachNguoiDung()`.
    *   `UserManagementController` gọi `INguoiDungDAO.layTatCaNguoiDung()`.
    *   `INguoiDungDAO` trả về danh sách `NguoiDung` cho `UserManagementController`.
    *   `UserManagementController` chuyển danh sách `NguoiDung` cho `UserManagementView`.
    *   `UserManagementView.hienThiDanhSachNguoiDung(nguoiDung: List<NguoiDung>)` hiển thị danh sách người dùng trên giao diện.
4.  **Admin thực hiện hành động:** Admin có thể chọn một trong các hành động sau:
    *   **Xem chi tiết người dùng:** Admin chọn một người dùng từ danh sách.
        *   `UserManagementView.hienThiChiTietNguoiDung(nguoiDung: NguoiDung)` hiển thị thông tin chi tiết của người dùng đó.
    *   **Thêm người dùng mới:** Admin nhấp vào nút "Thêm Người Dùng".
        *   `UserManagementView.moFormThemNguoiDung()` hiển thị biểu mẫu thêm người dùng.
        *   Admin nhập thông tin người dùng mới và nhấp "Lưu".
        *   `UserManagementView` gọi `UserManagementController.xuLyThemNguoiDung(nguoiDung: NguoiDung)` với dữ liệu người dùng mới.
        *   `UserManagementController` gọi `INguoiDungDAO.themNguoiDung(nguoiDung: NguoiDung)`.
        *   `INguoiDungDAO` trả về kết quả (true/false) cho `UserManagementController`.
        *   Nếu thành công, `UserManagementController` gọi `UserManagementView.hienThiThongBao(tinNhan: String)` với thông báo thành công. Sau đó gọi `UserManagementController.taiDanhSachNguoiDung()` để cập nhật danh sách.
        *   Nếu thất bại, `UserManagementController` gọi `UserManagementView.hienThiThongBao(tinNhan: String)` với thông báo lỗi.
    *   **Sửa thông tin người dùng:** Admin chọn một người dùng từ danh sách và nhấp vào nút "Sửa".
        *   `UserManagementView.moFormSuaNguoiDung(nguoiDungId: String)` hiển thị biểu mẫu sửa người dùng với thông tin hiện có.
        *   Admin cập nhật thông tin và nhấp "Lưu".
        *   `UserManagementView` gọi `UserManagementController.xuLySuaNguoiDung(nguoiDung: NguoiDung)` với dữ liệu người dùng đã cập nhật.
        *   `UserManagementController` gọi `INguoiDungDAO.capNhatNguoiDung(nguoiDung: NguoiDung)`.
        *   `INguoiDungDAO` trả về kết quả (true/false) cho `UserManagementController`.
        *   Nếu thành công, `UserManagementController` gọi `UserManagementView.hienThiThongBao(tinNhan: String)` với thông báo thành công. Sau đó gọi `UserManagementController.taiDanhSachNguoiDung()` để cập nhật danh sách.
        *   Nếu thất bại, `UserManagementController` gọi `UserManagementView.hienThiThongBao(tinNhan: String)` với thông báo lỗi.
    *   **Xóa người dùng:** Admin chọn một người dùng từ danh sách và nhấp vào nút "Xóa".
        *   `UserManagementView` yêu cầu xác nhận xóa.
        *   Admin xác nhận xóa.
        *   `UserManagementView` gọi `UserManagementController.xuLyXoaNguoiDung(id: String)` với ID của người dùng.
        *   `UserManagementController` gọi `INguoiDungDAO.xoaNguoiDung(id: String)`.
        *   `INguoiDungDAO` trả về kết quả (true/false) cho `UserManagementController`.
        *   Nếu thành công, `UserManagementController` gọi `UserManagementView.hienThiThongBao(tinNhan: String)` với thông báo thành công. Sau đó gọi `UserManagementController.taiDanhSachNguoiDung()` để cập nhật danh sách.
        *   Nếu thất bại, `UserManagementController` gọi `UserManagementView.hienThiThongBao(tinNhan: String)` với thông báo lỗi.

## Luồng sự kiện thay thế

*   **Không tìm thấy người dùng:** Nếu `INguoiDungDAO.layNguoiDungTheoId()` không tìm thấy người dùng, `UserManagementView.hienThiThongBao()` sẽ hiển thị thông báo lỗi.
*   **Lỗi hệ thống:** Nếu có bất kỳ lỗi nào xảy ra trong quá trình gọi DAO, `UserManagementController` sẽ bắt lỗi và gọi `UserManagementView.hienThiThongBao()` với thông báo lỗi thích hợp.

## Điều kiện hậu kỳ
Thông tin người dùng được cập nhật chính xác trong cơ sở dữ liệu và giao diện người dùng phản ánh sự thay đổi.

## Kịch bản Phiên bản 3: Xem và Cập nhật Chế độ Dinh dưỡng

**Mục tiêu:** Người dùng có thể xem chi tiết thông tin của một chế độ dinh dưỡng hiện có và cập nhật thông tin đó nếu cần.

**Tác nhân chính:** Người dùng (Chuyên gia Dinh dưỡng hoặc Người dùng)

**Các tác nhân liên quan:** Hệ thống

**Tiền điều kiện:**
* Người dùng đã đăng nhập thành công vào hệ thống.
* Đã có ít nhất một chế độ dinh dưỡng được lưu trữ trong hệ thống.
* Người dùng có quyền truy cập vào chức năng quản lý chế độ dinh dưỡng.

**Hậu điều kiện:**
* Thông tin của chế độ dinh dưỡng được chọn hiển thị chi tiết cho người dùng.
* Hoặc, thông tin chế độ dinh dưỡng đã được cập nhật thành công và hiển thị trong danh sách.
* Hoặc, hệ thống hiển thị thông báo lỗi nếu quá trình cập nhật thất bại.

---

**Luồng chính:**

1.  **Người dùng** truy cập vào trang quản lý chế độ dinh dưỡng.
2.  **Hệ thống** hiển thị giao diện `NutritionPlanPageUI` với danh sách các chế độ dinh dưỡng hiện có.
3.  **Người dùng** chọn một chế độ dinh dưỡng từ danh sách để xem/cập nhật (ví dụ: nhấn vào biểu tượng "Xem chi tiết" hoặc "Chỉnh sửa").
4.  **NutritionPlanPageUI** gọi `NutritionService.layCheDoDinhDuongTheoMa(maCheDo: string)` để lấy thông tin chi tiết của chế độ dinh dưỡng được chọn.
5.  **NutritionService** gọi `CheDoDinhDuongDAO.findById(maCheDo: string)` để truy xuất dữ liệu từ cơ sở dữ liệu.
6.  **CheDoDinhDuongDAO** thực hiện truy vấn và trả về đối tượng `CheDoDinhDuong` cho `NutritionService`.
7.  **NutritionService** nhận đối tượng `CheDoDinhDuong` và trả về `CheDoDinhDuongData` cho `NutritionPlanPageUI`.
8.  **NutritionPlanPageUI** nhận dữ liệu và gọi phương thức `dienThongTinCheDo(plan: CheDoDinhDuongData)` để điền thông tin chi tiết vào các trường nhập liệu trên form.

9.  **<Điều kiện rẽ nhánh>**
    *   **Người dùng chỉ xem:** Người dùng không thực hiện thay đổi nào và rời khỏi trang.
    *   **Người dùng cập nhật:**
        a.  **Người dùng** chỉnh sửa các thông tin cần thiết trên form (ví dụ: Tên chế độ, Mô tả, Ngày kết thúc, Mục tiêu).
        b.  **Người dùng** nhấn nút "Lưu" hoặc "Cập nhật".
        c.  **NutritionPlanPageUI** gọi phương thức `layThongTinCheDo()` để thu thập dữ liệu đã chỉnh sửa.
        d.  **NutritionPlanPageUI** gửi dữ liệu chế độ dinh dưỡng đã chỉnh sửa (dưới dạng `CheDoDinhDuongData`) đến `NutritionService` thông qua phương thức `capNhatCheDoDinhDuong(cheDo: CheDoDinhDuong)`.
        e.  **NutritionService** nhận dữ liệu, thực hiện kiểm tra tính hợp lệ và các logic nghiệp vụ cần thiết.
        f.  **NutritionService** gọi `CheDoDinhDuongDAO` thông qua phương thức `update(cheDo: CheDoDinhDuong)` để cập nhật dữ liệu vào cơ sở dữ liệu.
        g.  **CheDoDinhDuongDAO** thực hiện thao tác cập nhật dữ liệu vào cơ sở dữ liệu và trả về đối tượng `CheDoDinhDuong` đã được cập nhật.
        h.  **NutritionService** nhận kết quả từ `CheDoDinhDuongDAO`.
        i.  **NutritionService** trả về kết quả `true` (thành công) hoặc `false` (thất bại) cho `NutritionPlanPageUI`.
        j.  **NutritionPlanPageUI** nhận kết quả:
            *   Nếu thành công: Gọi phương thức `hienThiThongBao("Cập nhật chế độ thành công", "success")`. Sau đó, gọi `NutritionService.layTatCaCheDoDinhDuong()` để lấy danh sách cập nhật và hiển thị bằng phương thức `hienThiDanhSachCheDo(plans: CheDoDinhDuongData[])`.
            *   Nếu thất bại: Gọi phương thức `hienThiThongBao("Cập nhật chế độ thất bại", "error")`.

---

**Luồng phụ (Alternative Flows):**

*   **AF1: Chế độ dinh dưỡng không tồn tại:**
    *   Tại bước 7: `NutritionService` không tìm thấy chế độ dinh dưỡng với `maCheDo` được cung cấp.
    *   `NutritionService` trả về `null` hoặc một giá trị báo hiệu không tìm thấy cho `NutritionPlanPageUI`.
    *   `NutritionPlanPageUI` hiển thị thông báo lỗi (ví dụ: "Chế độ dinh dưỡng không tồn tại") bằng phương thức `hienThiThongBao()`.

*   **AF2: Dữ liệu không hợp lệ khi cập nhật:**
    *   Tại bước 9.e: `NutritionService` phát hiện dữ liệu không hợp lệ (ví dụ: thiếu trường bắt buộc, ngày không hợp lệ).
    *   `NutritionService` trả về thông báo lỗi hoặc giá trị đặc biệt.
    *   `NutritionPlanPageUI` hiển thị thông báo lỗi chi tiết (ví dụ: "Vui lòng nhập đầy đủ thông tin") bằng phương thức `hienThiThongBao()`.
    *   Người dùng sửa lỗi và thử lại từ bước 9.b.

*   **AF3: Lỗi hệ thống khi cập nhật:**
    *   Tại bước 9.g: `CheDoDinhDuongDAO` gặp lỗi khi cố gắng cập nhật dữ liệu vào cơ sở dữ liệu.
    *   `CheDoDinhDuongDAO` ném ra một ngoại lệ hoặc trả về kết quả thất bại.
    *   `NutritionService` bắt ngoại lệ/nhận kết quả thất bại và trả về `false` cho `NutritionPlanPageUI`.
    *   `NutritionPlanPageUI` hiển thị thông báo lỗi chung (ví dụ: "Đã xảy ra lỗi hệ thống, vui lòng thử lại sau") bằng phương thức `hienThiThongBao()`.

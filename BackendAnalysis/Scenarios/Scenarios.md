**Chức năng xác thực:**

- Use case: Đăng ký tài khoản
- Actor: Người dùng khách
- Tiền điều kiện: Người dùng khách chưa có tài khoản
- Hậu điều kiện: Người dùng khách đăng ký xong tài khoản mới
- Kịch bản chính:
    - Người dùng chưa có tài khoản truy cập hệ thống
    - Hệ thống hiển thị giao diện đăng ký hoặc đăng nhập
    - Người dùng chọn đăng ký tài khoản mới
    - Hệ thống hiển thị giao diện đăng ký
    - Người dùng nhập thông tin đăng ký tài khoản và xác nhận đăng ký tài khoản. Thông tin đăng ký cụ thể như sau:
        - Tên thật: Tạ Hoàng Công
        - Email: congtahoantoan@gmail.com
        - Tên tài khoản: congtaht223
        - Mật khẩu (hai trường): $$xConGThUToaNDieNx$$
    - Hệ thống kiểm tra yêu cầu đăng ký và hiển thị giao diện chính cho người dùng -
- Ngoại lệ:
    - Bước 2, 4: Lỗi máy chủ khi người dùng truy cập hệ thống
    - Bước 6:
        - Thông tin đăng ký tài khoản không hợp lệ
        - Thông tin đăng ký tài khoản trùng khớp với một tài khoản đã tồn tại trong hệ thống
        - Mật khẩu không đáp ứng các tiêu chí bảo mật tối thiểu
        - Hệ thống không gửi được email chứa mã xác thực một lần cho email được cung cấp
        - Lỗi cơ sở dữ liệu khi đang lưu thông tin tài khoản
        - Lỗi máy chủ khi đang lưu thông tin tài khoản

- Use case: Đăng nhập
- Actor: Người dùng đã có tài khoản
- Tiền điều kiện: Người dùng đã có tài khoản trong hệ thống nhưng chưa đăng nhập thành công vào hệ thống
- Hậu điều kiện: Người dùng đăng nhập thành công vào hệ thống
- Kịch bản chính:
    - Người dùng đã có tài khoản truy cập hệ thống
    - Hệ thống hiển thị giao diện đăng ký hoặc đăng nhập
    - Người dùng chọn đăng nhập
    - Hệ thống hiển thị giao diện đăng nhập
    - Người dùng nhập tên người dùng và mật khẩu cho tài khoản của mình, sau đó xác nhận đăng nhập
    - Hệ thống kiểm tra thông tin đăng nhập và hiển thị giao diện chính cho người dùng -
- Ngoại lệ:
    - Bước 2, 4: Lỗi máy chủ khi người dùng truy cập hệ thống
    - Bước 6:
        - Thông tin đăng nhập không chính xác
        - Thông tin đăng nhập chưa tồn tại trong hệ thống
        - Người dùng đang đăng nhập vào một khu vực không cho phép

**Chức năng tư vấn chế độ dinh dưỡng dựa trên mục tiêu và đặc điểm cá nhân**

- Use case: Tư vấn chế độ dinh dưỡng dựa trên mục tiêu và đặc điểm cá nhân
- Actor: Người dùng đã có tài khoản
- Tiền điều kiện: Người dùng đã có tài khoản muốn nhận tư vấn chế độ dinh dưỡng dựa trên mục tiêu và đặc điểm cá nhân -
- Hậu điều kiện: Người dùng nhận được tư vấn chế độ dinh dưỡng dựa trên mục tiêu và đặc điểm cá nhân -
- Kịch bản chính:
    - Người dùng đăng nhập vào hệ thống
    - Hệ thống hiển thị giao diện chính
    - Người dùng chọn chức năng tư vấn chế độ dinh dưỡng
    - Hệ thống hiển thị giao diện ngôn ngữ tự nhiên (chatbox)
    - Người dùng cung cấp mục tiêu: "Tôi muốn giảm 5kg trong 2 tháng", đặc điểm (chiều cao, cân nặng, mức độ vận động, bệnh lý nền, dị ứng) thông qua chatbox.
    - Hệ thống nhận dữ liệu đầu vào và gọi mô hình NLP
    - Mô hình NLP phân tích câu lệnh/dữ liệu đầu vào. Hệ thống AI (Machine Learning) tính toán nhu cầu năng lượng (TDEE, BMR) và phân bổ macro (Protein, Carb, Fat) phù hợp.
    - Hệ thống truy xuất dữ liệu thực phẩm và tạo ra thực đơn mẫu (ví dụ: 7 ngày) kèm theo định lượng, công thức và lý do đề xuất. Người dùng có thể yêu cầu chỉnh sửa tiếp bằng ngôn ngữ tự nhiên (ví dụ: "Thay món gà bằng cá cho bữa tối")
    - Với mỗi yêu cầu chỉnh sửa, hệ thống truy xuất dữ liệu thực phẩm để tìm món mới đáp ứng yêu cầu và có hàm lượng dinh dưỡng tương tự món cũ đễ chỉnh sửa thực đơn.
- Ngoại lệ:
    - Bước 6:
        - Lỗi máy chủ không nhận được dữ liệu đầu vào
        - Lỗi hệ thống không gọi được mô hình NLP
    - Bước 7:
        - Lỗi mô hình NLP không phân tích được dữ liệu đầu vào
        - Lỗi tính toán nhu cầu dinh dưỡng
    - Bước 8, 9:
        - Lỗi máy chủ khi đang truy xuất dữ liệu thực phẩm
        - Lỗi máy chủ khi đang tạo thực đơn mới

**Chức năng hướng dẫn lập kế hoạch và thực hiện quy trình dinh dưỡng**

- Use case: Hướng dẫn lập kế hoạch và thực hiện quy trình dinh dưỡng
- Actor: Người dùng đã có tài khoản
- Tiền điều kiện: Người dùng đã có tài khoản muốn hướng dẫn lập kế hoạch và thực hiện quy trình dinh dưỡng
- Hậu điều kiện: Người dùng nhận được hướng dẫn lập kế hoạch và thực hiện quy trình dinh dưỡng
- Kịch bản chính:
    - Người dùng đăng nhập vào hệ thống
    - Hệ thống hiển thị giao diện chính
    - Người dùng chọn chức năng lập kế hoạch và thực hiện quy trình dinh dưỡng
    - Hệ thống hiển thị giao diện chọn kế hoạch
    - Người dùng chọn Kế hoạch mẫu từ bước 1 hoặc tự tạo mới
    - Hệ thống hiển thị giao diện lập kế hoạch mới nếu người dùng chon tạo kế hoạch mới
    - Người dùng điền cụ thể các bữa ăn, công thức, thời gian ăn trong Kế hoạch.
    - Hệ thống tự động tính toán tổng Calo và Macro của Kế hoạch, so sánh với mục tiêu và cảnh báo nếu có sự chênh lệch lớn
    - Hệ thống cung cấp Checklist (danh sách mua sắm) dựa trên Kế hoạch và các mẹo/lời nhắc thực hiện hàng ngày (ví dụ: "Nhớ uống đủ nước", "Chuẩn bị bữa trưa") -
- Ngoại lệ:
    - Bước 7: Không có bữa ăn, công thức hay thời gian ăn trong Kế hoạch
    - Bước 8, 9:
        - Lỗi tính toán tổng hàm lượng dinh dưỡng
        - Lỗi so sánh kết quả tính toán với mục tiêu
        - Lỗi máy chủ khi cung cấp Checklist

**Chức năng theo dõi và đánh giá kế hoạch dinh dưỡng**

- Use case: Theo dõi và đánh giá kế hoạch dinh dưỡng
- Actor: Người dùng đã có tài khoản
- Tiền điều kiện: Người dùng đã có tài khoản và đã có kế hoạch dinh dưỡng gắn với tài khoản trong hệ thống muốn theo dõi và đánh giá quá trình thực thi kế hoạch:
- Hậu điều kiện: Người dùng cập nhật được tiến trình thực hiện kế hoạch dinh dưỡng và nhận được hướng dẫn điều chỉnh để tiếp tục đi theo kế hoạch
- Kịch bản chính:
    - Người dùng đăng nhập vào hệ thống
    - Hệ thống hiển thị giao diện chính
    - Người dùng chọn chức năng theo dõi và đánh giá kế hoạch dinh dưỡng
    - Hệ thống hiển thị giao diện theo dõi và đánh giá kế hoạch dinh dưỡng
    - Người dùng nhập (gõ, chụp ảnh thực phẩm, hoặc nói) những gì đã ăn/uống và hoạt động thể chất (calories tiêu hao) hàng ngày
    - AI sử dụng NLP và Vision (nếu có ảnh) để nhận dạng thực phẩm và ước tính dinh dưỡng/calo
    - Hệ thống so sánh dữ liệu thực tế (đã ăn) với dữ liệu Kế hoạch (cần ăn)
    - Tạo Biểu đồ Tiến độ (cân nặng, chỉ số cơ thể, % mục tiêu đạt được), Báo cáo Dinh dưỡng (Macro, Micro nutrient tiêu thụ) và đưa ra các Gợi ý Điều chỉnh (ví dụ: "Tuần này bạn ăn quá nhiều chất béo, nên tăng cường rau xanh")
- Ngoại lệ:
    - Bước 6: Không có thông tin thực phẩm và hoạt động thể chất
    - Bước 7: Không có dữ liệu Kế hoạch
    - Bước 8: Lỗi máy chủ và/hoặc CSDL khi tạo Biểu đồ Tiến độ, Báo cáo Dinh dưỡng và/hoặc Gợi ý Điều chỉnh

**Chức năng chia sẻ kế hoạch và hoạt động dinh dưỡng:**

- Use case: Chia sẻ kế hoạch và hoạt động dinh dưỡng
- Actor: Người dùng đã có tài khoản
- Tiền điều kiện: Người dùng đã có tài khoản và đã có kế hoạch dinh dưỡng gắn với tài khoản trong hệ thống muốn chia sẻ kế hoạch và hoạt động dinh dưỡng
- Hậu điều kiện: Người dùng chia sẻ được kế hoạch và hoạt động dinh dưỡng
- Kịch bản chính:
    - Người dùng đăng nhập vào hệ thống
    - Hệ thống hiển thị giao diện chính
    - Người dùng chọn một trong các chức năng sau khi đăng nhập đã được phân tích
    - Hệ thống hiển thị giao diện của chức năng được chọn
    - Người dùng chọn một đối tượng và đánh dấu đối tượng đó là "Công khai" hoặc "Chia sẻ với Bạn bè"
    - Hệ thống tải nội dung được đánh dấu lên CSDL nội dung chia sẻ
    - Cho phép người dùng xem, thích, bình luận và lưu lại các nội dung chia sẻ
    - Hệ thống AI có thể gợi ý người dùng khác hoặc nhóm có cùng mục tiêu/sở thích để tăng tính động lực và kết nối. -
- Ngoại lệ:
    - Bước 6:
        - Không có đối tượng được đánh dấu để chia sẻ
        - Lỗi máy chủ và/hoặc CSDL khi tạo Biểu đồ Tiến độ, Báo cáo Dinh dưỡng và/hoặc Gợi ý Điều chỉnh

**Các chức năng thêm dành cho quản trị viên**

- Use case: Các chức năng thêm dành cho quản trị viên
- Actor: Quản trị viên
- Tiền điều kiện: Quản trị viên muốn thêm đối tượng vào hệ thống
- Hậu điều kiện: Quản trị viên thêm được đối tượng vào hệ thống
- Kịch bản chính: 
    - Quản trị viên truy cập giao diện quản trị hệ thống
    - Hệ thống hiển thị giao diện quản trị
    - Quản trị viên chọn loại đối tượng quản trị
    - Hệ thống hiển thị giao diện chức năng quản trị loại đối tượng
    - Quản trị viên chọn tùy chọn thêm đối tượng được quản trị
    - Hệ thống hiển thị giao diện thêm đối tượng
    - Quản trị viên nhập thông tin đối tượng để thêm vào và xác nhận thao tác thêm. Thông tin đối tượng phụ thuộc vào loại đối tượng.
    - Hệ thống lưu đối tượng mới và gửi dữ liệu vào hệ thống AI và NLP để tiến hành huấn luyện lại với đối tượng mới
- Ngoại lệ:
    - Bước 2, 4, 6: Lỗi máy chủ khi quản trị viên truy cập giao diện quản trị hệ thống
    - Bước 8:
        - Không có đối tượng mới được khai báo
        - Lỗi CSDL hoặc máy chủ khi đang lưu đối tượng mới
        - Lỗi máy chủ khi đang gửi dữ liệu vào hệ thống AI và NLP
        - Lỗi hệ thống AI và NLP khi đang huấn luyện lại

**Các chức năng xóa hoặc sửa dành cho quản trị viên**

- Use case: Các chức năng xóa hoặc sửa dành cho quản trị viên
- Actor: Quản trị viên
- Tiền điều kiện: Quản trị viên muốn xóa hoặc sửa đối tượng vào hệ thống
- Hậu điều kiện: Quản trị viên xóa hoặc sửa được đối tượng vào hệ thống
- Kịch bản chính:
    - Quản trị viên truy cập giao diện quản trị hệ thống
    - Hệ thống hiển thị giao diện quản trị
    - Quản trị viên chọn loại đối tượng quản trị
    - Hệ thống hiển thị giao diện chức năng quản trị loại đối tượng
    - Quản trị viên chọn đối tượng để xóa hoặc sửa
    - Hệ thống hiển thị giao diện xóa hoặc sửa đối tượng
    - Quản trị viên nhập thông tin đối tượng để sửa và xác nhận thao tác lưu những chỉnh sủa. Ngoài ra, nếu muốn xóa đối tượng, quản trị viên có thể chọn thao tác xóa đối tượng. Thông tin đối tượng phụ thuộc vào loại đối tượng.
    - Hệ thống lưu đối tượng đã được sửa hoặc xóa đối tượng và gửi dữ liệu vào hệ thống AI và NLP để tiến hành huấn luyện lại với thay đổi về đối tượng
- Ngoại lệ:
    - Bước 2, 4, 6: Lỗi máy chủ khi quản trị viên truy cập giao diện quản trị hệ thống
    - Bước 8:
        - Không có thay đổi nào được khai báo
        - Lỗi CSDL hoặc máy chủ khi đang lưu thay đổi
        - Lỗi máy chủ khi đang gửi dữ liệu vào hệ thống AI và NLP
        - Lỗi hệ thống AI và NLP khi đang huấn luyện lại


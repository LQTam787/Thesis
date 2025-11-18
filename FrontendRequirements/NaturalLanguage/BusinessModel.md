1\. Mục đích hệ thống

Hệ thống phục vụ nhu cầu lập kế hoạch, thực hiện và theo dõi chế độ dinh dưỡng một cách hiệu quả, thông minh và đã được cá nhân hóa dựa trên mục tiêu sức khỏe và đặc điểm cá nhân của người dùng. Hệ thống sử dụng trí tuệ nhân tạo (AI) và kỹ thuật xử lý ngôn ngữ tự nhiên (NLP) để:

- Tư vấn và tạo ra các gợi ý thực đơn, chế độ ăn uống phù hợp.  
- Tương tác với người dùng bằng ngôn ngữ tự nhiên, giúp việc nhập liệu, đặt câu hỏi và nhận hướng dẫn trở nên thân thiện và dễ dàng hơn so với giao diện truyền thống.  
- Tối ưu hóa việc đạt được các mục tiêu sức khỏe (giảm cân, tăng cơ, duy trì sức khỏe) thông qua các đề xuất dinh dưỡng chính xác và khoa học.

2\. Phạm vi hệ thống

Những đối tượng được truy cập hệ thống và chức năng mỗi đối tượng được phân quyền thực thi khi truy cập hệ thống được quy định như sau:

- Người dùng chưa có tài khoản trong hệ thống:   
  * Đăng ký tài khoản mới  
- Người dùng cơ bản:  
  * Đăng nhập/đăng xuất  
- Người dùng:  
  * Các chức năng người dùng cơ bản  
  * Tư vấn chế độ dinh dưỡng: Nhận đề xuất thực đơn và chế độ ăn dựa trên trò chuyện với AI  
  * Hướng dẫn chuẩn bị thực đơn: Xem danh sách món ăn, nguyên liệu và quy trình nấu ăn cho các món trong thực đơn  
  * Hướng dẫn lập kế hoạch: Thiết lập chi tiết kế hoạch dinh dưỡng hàng tuần/tháng.  
  * Theo dõi và đánh giá: Ghi nhật ký ăn uống, tập luyện, và xem báo cáo tiến độ.  
  * Chia sẻ: Chia sẻ kế hoạch, công thức hoặc thành tích với cộng đồng hoặc bạn bè.  
  * Quản lý hồ sơ cá nhân: Cập nhật thông tin sinh trắc học, mục tiêu và sở thích ăn uống.  
- Quản trị viên:  
  * Các chức năng người dùng cơ bản  
  * Quản lý thông tin người dùng  
  * Quản lý thông tin tư vấn chế độ dinh dưỡng  
  * Quản lý thông tin lập kế hoạch  
  * Quản lý thông tin theo dõi và đánh giá  
  * Quản lý danh sách món ăn cho việc đề xuất thực đơn và chế độ ăn  
  * Quản lý nguyên liệu và quy trình nấu ăn cho các món ăn

3\. Hoạt động Nghiệp vụ Chi tiết

1. Đăng ký tài khoản mới  
- Người dùng chưa có tài khoản truy cập hệ thống  
- Hệ thống hiển thị giao diện đăng ký hoặc đăng nhập  
- Người dùng chọn đăng ký tài khoản mới  
- Hệ thống hiển thị giao diện đăng ký  
- Người dùng nhập tên thật, địa chỉ email, tên người dùng và mật khẩu cho tài khoản của mình, sau đó xác nhận đăng ký tài khoản. Mật khẩu được nhập vào hai trường trong giao diện đăng ký.  
- Hệ thống kiểm tra yêu cầu đăng ký và hiển thị giao diện chính cho người dùng  
2. Đăng nhập  
- Người dùng đã có tài khoản truy cập hệ thống  
- Hệ thống hiển thị giao diện đăng ký hoặc đăng nhập  
- Người dùng chọn đăng nhập  
- Hệ thống hiển thị giao diện đăng nhập  
- Người dùng nhập tên người dùng và mật khẩu cho tài khoản của mình, sau đó xác nhận đăng nhập  
- Hệ thống kiểm tra thông tin đăng nhập và hiển thị giao diện chính cho người dùng  
3. Tư vấn chế độ dinh dưỡng dựa trên mục tiêu và đặc điểm cá nhân  
- Người dùng đăng nhập vào hệ thống  
- Hệ thống hiển thị giao diện chính  
- Người dùng chọn chức năng tư vấn chế độ dinh dưỡng  
- Hệ thống hiển thị giao diện ngôn ngữ tự nhiên (chatbox)  
- Người dùng cung cấp mục tiêu (ví dụ: "Tôi muốn giảm 5kg trong 2 tháng"), đặc điểm (chiều cao, cân nặng, mức độ vận động, bệnh lý nền, dị ứng) thông qua chatbox.  
- Hệ thống nhận dữ liệu đầu vào và gọi mô hình NLP  
- Mô hình NLP phân tích câu lệnh/dữ liệu đầu vào. Hệ thống AI (Machine Learning) tính toán nhu cầu năng lượng (TDEE, BMR) và phân bổ macro (Protein, Carb, Fat) phù hợp.  
- Hệ thống truy xuất dữ liệu thực phẩm và tạo ra thực đơn mẫu (ví dụ: 7 ngày) kèm theo định lượng, công thức và lý do đề xuất. Người dùng có thể yêu cầu chỉnh sửa tiếp bằng ngôn ngữ tự nhiên (ví dụ: "Thay món gà bằng cá cho bữa tối")  
- Với mỗi yêu cầu chỉnh sửa, hệ thống truy xuất dữ liệu thực phẩm để tìm món mới đáp ứng yêu cầu và có hàm lượng dinh dưỡng tương tự món cũ đễ chỉnh sửa thực đơn.  
4. Hướng dẫn lập kế hoạch và thực hiện quy trình dinh dưỡng  
- Người dùng đăng nhập vào hệ thống  
- Hệ thống hiển thị giao diện chính  
- Người dùng chọn chức năng lập kế hoạch và thực hiện quy trình dinh dưỡng  
- Hệ thống hiển thị giao diện chọn kế hoạch  
- Người dùng chọn Kế hoạch mẫu từ bước 1 hoặc tự tạo mới  
- Hệ thống hiển thị giao diện lập kế hoạch mới nếu người dùng chon tạo kế hoạch mới  
- Người dùng điền cụ thể các bữa ăn, công thức, thời gian ăn trong Kế hoạch.  
- Hệ thống tự động tính toán tổng Calo và Macro của Kế hoạch, so sánh với mục tiêu và cảnh báo nếu có sự chênh lệch lớn  
- Hệ thống cung cấp Checklist (danh sách mua sắm) dựa trên Kế hoạch và các mẹo/lời nhắc thực hiện hàng ngày (ví dụ: "Nhớ uống đủ nước", "Chuẩn bị bữa trưa")  
5. Theo dõi và đánh giá kế hoạch dinh dưỡng  
- Người dùng đăng nhập vào hệ thống  
- Hệ thống hiển thị giao diện chính  
- Người dùng chọn chức năng theo dõi và đánh giá kế hoạch dinh dưỡng  
- Hệ thống hiển thị giao diện theo dõi và đánh giá kế hoạch dinh dưỡng  
- Người dùng nhập (gõ, chụp ảnh thực phẩm, hoặc nói) những gì đã ăn/uống và hoạt động thể chất (calories tiêu hao) hàng ngày  
- AI sử dụng NLP và Vision (nếu có ảnh) để nhận dạng thực phẩm và ước tính dinh dưỡng/calo  
- Hệ thống so sánh dữ liệu thực tế (đã ăn) với dữ liệu Kế hoạch (cần ăn)  
- Tạo Biểu đồ Tiến độ (cân nặng, chỉ số cơ thể, % mục tiêu đạt được), Báo cáo Dinh dưỡng (Macro, Micro nutrient tiêu thụ) và đưa ra các Gợi ý Điều chỉnh (ví dụ: "Tuần này bạn ăn quá nhiều chất béo, nên tăng cường rau xanh")  
6. Chia sẻ kế hoạch và hoạt động dinh dưỡng  
- Người dùng có thể đánh dấu công thức, kế hoạch hoặc bài viết của mình là "Công khai" hoặc "Chia sẻ với Bạn bè"  
- Cho phép người dùng xem, thích, bình luận và lưu lại các nội dung chia sẻ  
- Hệ thống AI có thể gợi ý người dùng khác hoặc nhóm có cùng mục tiêu/sở thích để tăng tính động lực và kết nối.  
7. Các chức năng thêm dành cho quản trị viên  
- Quản trị viên truy cập giao diện quản trị hệ thống  
- Hệ thống hiển thị giao diện quản trị  
- Quản trị viên chọn loại đối tượng quản trị  
- Hệ thống hiển thị giao diện chức năng quản trị loại đối tượng  
- Quản trị viên chọn tùy chọn thêm đối tượng được quản trị  
- Hệ thống hiển thị giao diện thêm đối tượng  
- Quản trị viên nhập thông tin đối tượng để thêm vào và xác nhận thao tác thêm. Thông tin đối tượng phụ thuộc vào loại đối tượng.  
- Hệ thống lưu đối tượng mới và gửi dữ liệu vào hệ thống AI và NLP để tiến hành huấn luyện lại với đối tượng mới  
8. Các chức năng xóa hoặc sửa dành cho quản trị viên  
- Quản trị viên truy cập giao diện quản trị hệ thống  
- Hệ thống hiển thị giao diện quản trị  
- Quản trị viên chọn loại đối tượng quản trị  
- Hệ thống hiển thị giao diện chức năng quản trị loại đối tượng  
- Quản trị viên chọn đối tượng để xóa hoặc sửa  
- Hệ thống hiển thị giao diện xóa hoặc sửa đối tượng  
- Quản trị viên nhập thông tin đối tượng để sửa và xác nhận thao tác lưu những chỉnh sủa. Ngoài ra, nếu muốn xóa đối tượng, quản trị viên có thể chọn thao tác xóa đối tượng. Thông tin đối tượng phụ thuộc vào loại đối tượng.  
- Hệ thống lưu đối tượng đã được sửa hoặc xóa đối tượng và gửi dữ liệu vào hệ thống AI và NLP để tiến hành huấn luyện lại với thay đổi về đối tượng

4\. Các Đối tượng (Entities) và Thuộc tính

Các đối tượng được quản lý/xử lý trong hệ thống (thường tương ứng với các bảng trong cơ sở dữ liệu):

- Người dùng (User): tên đăng nhập, email, mật khẩu, tên thật, ngày sinh, giới tính, chiều cao, cân nặng hiện tại, mức độ vận động, bệnh lý/dị ứng, sở thích/không thích món ăn, vai trò (User/Admin)  
- Mục tiêu (Goal): người dùng, loại mục tiêu, cân nặng mục tiêu, thời gian bắt đầu, thời gian kết thúc, trạng thái, nhu cầu Calo/Macro đề xuất (tính toán bởi AI).  
- Kế hoạch dinh dưỡng (Plan): người dùng, tên kế hoạch, mô tả, ngày bắt đầu, ngày kết thúc, tổng Calo mục tiêu/ngày, Macro mục tiêu/ngày, trạng thái, mức độ công khai.  
- Công thức/Thực phẩm (Recipe/Food Item): tên, mô tả, nguyên liệu, hướng dẫn chế biến, thông tin dinh dưỡng/100g, nguồn (hệ thống/người dùng), trạng thái kiểm duyệt.  
- Nhật ký ăn uống (Log Entry): người dùng, công thức/thực phẩm/mô tả tự nhập, ngày, thời gian, loại bữa ăn, khối lượng/định lượng, calo thực tế, macro thực tế.  
- Hoạt động thể chất (Activity): người dùng, ngày, loại hoạt động, thời gian, cường độ, calo tiêu hao ước tính.

5\. Quan hệ (Số lượng) giữa các Đối tượng

Đây là mô tả quan hệ phổ biến (1-1: Một-một, 1-N: Một-nhiều, N-N: Nhiều-nhiều) giữa các đối tượng:

- Người dùng (User) và mục tiêu (Goal): 1 \- N (Một-nhiều): Một người dùng có thể có nhiều mục tiêu qua thời gian (ví dụ: hoàn thành mục tiêu 1, chuyển sang mục tiêu 2\)  
- Người dùng (User) và kế hoạch dinh dưỡng (Plan): 1 \- N (Một-nhiều): Một người dùng có thể tạo ra nhiều kế hoạch dinh dưỡng  
- Người dùng (User) và nhật ký ăn uống (Log Entry): 1 \- N (Một-nhiều): Một người dùng có nhiều nhật ký ăn uống (mỗi bữa ăn là một bản ghi)  
- Người dùng (User) và hoạt động thể chất (Activity): 1 \- N (Một-nhiều): Một người dùng có nhiều bản ghi hoạt động thể chất  
- Kế hoạch dinh dưỡng (Plan) và công thức/thực phẩm (Recipe/Food Item): N \- N (Nhiều-nhiều): Một kế hoạch chứa nhiều công thức/thực phẩm. Một công thức/thực phẩm có thể được sử dụng trong nhiều kế hoạch 🡺 Cần một bảng chi tiết kế hoạch trung gian liên hệ kế hoạch dinh dưỡng và công thức/thực phẩm và bao gồm ngày, loại bữa ăn, định lượng  
- Nhật ký ăn uống (Log Entry) và công thức/thực phẩm (Recipe/Food Item): N \- N (Nhiều-nhiều): Một nhật ký có thể ghi nhiều thực phẩm (ví dụ: cơm, rau, cá), và một thực phẩm được ghi trong nhiều nhật ký 🡺 Cần một bảng chi tiết nhật ký trung gian bao gồm khối lượng và liên hệ nhật ký ăn uống và công thức/thực phẩm  
- Công thức/thực phẩm (Recipe/Food Item) và người dùng (User): N \- N (Nhiều-nhiều): Một công thức/thực phẩm có thể được chia sẻ/lưu bởi nhiều người dùng. Một người dùng có thể lưu nhiều công thức/thực phẩm 🡺 Cần một bảng công thức đã lưu trung gian liên hệ với người dùng và công thức/thực phẩm
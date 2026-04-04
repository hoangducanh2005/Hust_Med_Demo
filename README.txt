this is a branch for developing features/ media


UI hiển thị:
    track_1
        ↓
User click
        ↓
Hệ thống gửi: "track_1"
        ↓
onAddMediaItems()
        ↓
Bạn xử lý "track_1" → tìm URI


.setIsBrowsable(false): Đánh dấu đây là "lá" (leaf node). Vì đây là một bài hát cụ thể, người dùng không thể nhấn vào để mở ra thư mục con nào nữa.
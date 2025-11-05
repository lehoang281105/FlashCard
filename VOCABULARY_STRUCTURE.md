# FlashLearn Lite - Quản lý từ vựng

## 📚 Cấu trúc chủ đề và từ vựng

### 5 Chủ đề mặc định:

#### 1. Động từ thường gặp (ID: "1")

- 10 động từ cơ bản: go, come, eat, drink, sleep, study, work, play, read, write

#### 2. Danh từ hàng ngày (ID: "2")

- 10 danh từ thường gặp: house, car, phone, book, table, chair, computer, water, food, money

#### 3. Tính từ mô tả (ID: "3")

- 10 tính từ: big, small, good, bad, beautiful, happy, sad, easy, difficult, fast

#### 4. Từ vựng công việc (ID: "4")

- 10 từ về công việc: job, office, meeting, boss, colleague, project, deadline, salary, interview, contract

#### 5. Từ vựng du lịch (ID: "5")

- 10 từ về du lịch: hotel, airport, ticket, passport, luggage, beach, tour, guide, map, souvenir

## 🎯 Tính năng:

### Quản lý Chủ đề:

- ✅ Xem danh sách chủ đề
- ✅ Thêm chủ đề mới (nút +)
- ✅ Sửa chủ đề (nút Edit)
- ✅ Xóa chủ đề (nút Delete - sẽ xóa cả từ vựng bên trong)
- ✅ Click vào chủ đề để xem từ vựng

### Quản lý Từ vựng:

- ✅ Xem từ vựng theo chủ đề
- ✅ Thêm từ mới vào chủ đề
- ✅ Sửa từ vựng
- ✅ Xóa từ vựng
- ✅ Hiển thị: Word, Meaning, Pronunciation, Example

### Dữ liệu mẫu:

- ✅ Tải 50 từ vựng mẫu (10 từ/chủ đề)
- ✅ Dialog hướng dẫn lần đầu

## 📱 Cách sử dụng:

1. **Lần đầu mở app**: Chọn "Tải dữ liệu mẫu" để có 50 từ vựng
2. **Xem từ vựng**: Click vào bất kỳ chủ đề nào
3. **Thêm chủ đề**: Nhấn nút + ở góc phải dưới
4. **Thêm từ**: Vào trong chủ đề, nhấn nút +
5. **Tải thêm dữ liệu**: Nhấn icon download ở toolbar

## 🔧 MockAPI Structure:

```json
{
  "id": "1",
  "word": "hello",
  "meaning": "xin chào",
  "pronunciation": "həˈloʊ",
  "example": "Hello, how are you?",
  "topicId": "1",
  "topicName": "Động từ thường gặp"
}
```

## 🎨 Giao diện:

- Material Design
- CardView với elevation
- Icon emoji cho mỗi chủ đề
- Màu sắc: Purple theme
- Floating Action Button cho thêm mới

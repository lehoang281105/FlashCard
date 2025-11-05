# 🌟 FlashLearn Lite - Ứng dụng học từ vựng tiếng Anh

Ứng dụng học từ vựng tiếng Anh bằng flashcard với 3 tính năng chính:

- 📚 Học từ (Flashcard)
- 📝 Làm Quiz
- ⚙️ Quản lý từ vựng

## 👥 Phân Công Nhóm

### Người 1 - Màn hình "Học Flashcard" 🧠 ✅ HOÀN THÀNH

**Chức năng đã hoàn thành:**

- ✅ Gọi API lấy danh sách từ vựng từ MockAPI
- ✅ Hiển thị từng từ bằng ViewPager2 với CardView
- ✅ Nút "Next" / "Previous" để chuyển từ
- ✅ Hiệu ứng lật thẻ (flip animation) giữa tiếng Anh và tiếng Việt
- ✅ Hiển thị ví dụ ở mặt sau của thẻ
- ✅ Phát âm từ bằng TextToSpeech
- ✅ Chế độ ngẫu nhiên (Shuffle Mode)
- ✅ Hiển thị progress (vị trí hiện tại / tổng số từ)
- ✅ Footer navigation với 3 tab

**Files đã tạo:**

- `fragment/FlashcardFragment.java` - Logic của màn hình flashcard
- `adapter/FlashcardAdapter.java` - Adapter cho ViewPager2
- `layout/fragment_flashcard.xml` - Layout màn hình flashcard
- `layout/item_flashcard.xml` - Layout cho mỗi thẻ flashcard
- `animator/card_flip_in.xml` & `card_flip_out.xml` - Animation lật thẻ

### Người 2 - Màn hình "Quiz" 📝 ⏳ CHỜ PHÁT TRIỂN

**Chức năng cần làm:**

- [ ] Dùng danh sách từ từ API (RetrofitClient.getApiService().getWords())
- [ ] Cho chọn số lượng câu hỏi: 10, 15, 20, 30
- [ ] Random 1 từ làm câu hỏi, và 4 đáp án (1 đúng, 3 sai)
- [ ] Hiển thị điểm và kết quả sau khi làm xong
- [ ] Có nút "Làm lại"
- [ ] Hiển thị kết quả tốt nhất

**Files placeholder:**

- `fragment/QuizFragment.java`
- `layout/fragment_quiz.xml`

### Người 3 - Màn hình "Quản lý Từ Vựng" ⚙️ ⏳ CHỜ PHÁT TRIỂN

**Chức năng cần làm:**

- [ ] Gọi API MockAPI bằng Retrofit
- [ ] Thêm từ mới (POST) - `RetrofitClient.getApiService().addWord(word)`
- [ ] Xóa từ (DELETE) - `RetrofitClient.getApiService().deleteWord(id)`
- [ ] Cập nhật từ (PUT) - `RetrofitClient.getApiService().updateWord(id, word)`
- [ ] Hiển thị danh sách từ trong RecyclerView

**Files placeholder:**

- `fragment/VocabularyFragment.java`
- `layout/fragment_vocabulary.xml`

## 🔗 API MockAPI

**Base URL:** `https://6903097bd0f10a340b2250fa.mockapi.io/`

**Endpoint:** `/words`

**Model Word:**

```java
{
  "id": "1",
  "english": "Hello",
  "vietnamese": "Xin chào",
  "example": "Hello, how are you?",
  "type": "noun"
}
```

**Các phương thức API đã setup:**

- `GET /words` - Lấy tất cả từ
- `GET /words/{id}` - Lấy 1 từ theo ID
- `POST /words` - Thêm từ mới
- `PUT /words/{id}` - Cập nhật từ
- `DELETE /words/{id}` - Xóa từ

## 📁 Cấu Trúc Project

```
app/src/main/java/com/example/flashcardnnn/
├── MainActivity.java              # Quản lý navigation và fragments
├── model/
│   └── Word.java                  # Model cho từ vựng
├── api/
│   ├── ApiService.java            # Interface Retrofit API
│   └── RetrofitClient.java        # Singleton Retrofit client
├── adapter/
│   └── FlashcardAdapter.java      # Adapter cho flashcard ViewPager
└── fragment/
    ├── FlashcardFragment.java     # Fragment học flashcard (Người 1) ✅
    ├── QuizFragment.java          # Fragment quiz (Người 2) ⏳
    └── VocabularyFragment.java    # Fragment quản lý từ (Người 3) ⏳

app/src/main/res/
├── layout/
│   ├── activity_main.xml          # Layout chính với footer navigation
│   ├── fragment_flashcard.xml     # Layout flashcard
│   ├── fragment_quiz.xml          # Layout quiz
│   ├── fragment_vocabulary.xml    # Layout vocabulary
│   └── item_flashcard.xml         # Layout item flashcard
├── animator/
│   ├── card_flip_in.xml           # Animation lật thẻ vào
│   └── card_flip_out.xml          # Animation lật thẻ ra
├── drawable/
│   ├── ic_flashcard.xml           # Icon flashcard
│   ├── ic_quiz.xml                # Icon quiz
│   ├── ic_vocabulary.xml          # Icon vocabulary
│   ├── nav_item_background.xml    # Background cho nav item
│   └── nav_item_color.xml         # Color selector cho nav item
├── values/
│   ├── colors.xml                 # Định nghĩa màu sắc
│   └── strings.xml                # Định nghĩa chuỗi text
```

## 🚀 Hướng Dẫn Sử Dụng

### Cho Người 1 (Đã hoàn thành):

1. ✅ Màn hình flashcard sẽ tự động load khi mở app
2. ✅ Nhấn vào thẻ để lật giữa tiếng Anh và tiếng Việt
3. ✅ Nhấn icon loa để phát âm từ
4. ✅ Dùng nút Previous/Next để chuyển thẻ
5. ✅ Nhấn nút "Ngẫu nhiên" để trộn thẻ

### Cho Người 2 (Quiz):

1. Tạo các file layout cần thiết cho quiz
2. Sử dụng `RetrofitClient.getApiService().getWords()` để lấy danh sách từ
3. Random các từ và tạo câu hỏi với 4 đáp án
4. Lưu điểm cao nhất vào SharedPreferences
5. Test kỹ trước khi merge

### Cho Người 3 (Vocabulary):

1. Tạo RecyclerView để hiển thị danh sách từ
2. Sử dụng các method trong `ApiService`:
   - `addWord(word)` để thêm từ
   - `deleteWord(id)` để xóa từ
   - `updateWord(id, word)` để cập nhật từ
3. Thêm dialog để input từ mới
4. Test API calls kỹ trước khi merge

## 🛠️ Dependencies

```gradle
// Retrofit for API calls
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// Gson for JSON parsing
implementation 'com.google.code.gson:gson:2.10.1'

// ViewPager2 for flashcard swiping
implementation 'androidx.viewpager2:viewpager2:1.0.0'

// CardView for flashcard design
implementation 'androidx.cardview:cardview:1.0.0'

// Fragment
implementation 'androidx.fragment:fragment:1.6.2'
```

## 📱 Tính Năng Nổi Bật

### ✨ Flashcard (Người 1 - Hoàn thành)

- Hiệu ứng lật thẻ mượt mà với 3D animation
- Text-to-Speech phát âm chuẩn
- Chế độ shuffle để học không nhàm chán
- Progress tracking để biết vị trí hiện tại
- UI đẹp với Material Design

### ✨ Footer Navigation

- 3 tab: Flashcard, Quiz, Vocabulary
- Icon đẹp với hiệu ứng selected
- Navigation mượt mà với fade animation
- Back button thông minh (về Flashcard khi ở tab khác)

## 🎨 UI/UX

- **Màu chủ đạo:** Indigo/Purple (#6366F1)
- **Card design:** Modern với shadow và rounded corners
- **Animation:** Smooth flip effect 300ms
- **Typography:** Clear và dễ đọc
- **Footer:** Bottom navigation giống trong ảnh mẫu

## 📝 Notes

- API sử dụng HTTPS nên đã config `usesCleartextTraffic="true"` trong manifest
- TextToSpeech cần device có Google TTS hoặc TTS engine
- ViewPager2 cho phép swipe mượt giữa các thẻ
- Fragment architecture giúp dễ maintain và mở rộng

## 🤝 Hướng Dẫn Merge Code

1. **Người 2 và 3:** Chỉ code trong file fragment của mình
2. **Không sửa:** MainActivity.java, ApiService.java, RetrofitClient.java, Word.java
3. **Có thể tạo thêm:** Layout, drawable, adapter, helper class
4. **Test kỹ** trước khi commit
5. **Giải quyết conflict** trước khi merge

## 📞 Liên Hệ

Nếu có thắc mắc về phần Flashcard (Người 1), vui lòng liên hệ!

---

**Made with ❤️ by Team FlashLearn**

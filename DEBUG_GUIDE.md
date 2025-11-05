# 🔍 HƯỚNG DẪN DEBUG - TÌM NGUYÊN NHÂN KHÔNG PRE-FILL

## Bước 1: Build lại app

```
Build → Clean Project
Build → Rebuild Project
```

## Bước 2: Mở Logcat

1. Trong Android Studio, click **View** → **Tool Windows** → **Logcat**
2. Ở ô **Filter**, gõ: `TopicWords|AddEditWord`

## Bước 3: Test và xem log

1. **Chạy app**
2. **Vào một chủ đề** (VD: Động vật)
3. **Click icon Edit (✏️)** của từ "bird"
4. **Xem Logcat**, bạn sẽ thấy:

### Log mẫu KHI THÀNH CÔNG:

```
D/TopicWords: ===== ON EDIT CLICK =====
D/TopicWords: Word ID: 123abc
D/TopicWords: Word: bird
D/TopicWords: Meaning: con chim
D/TopicWords: Pronunciation: /bɜːrd/
D/TopicWords: Example: Birds fly in the sky
D/TopicWords: Topic ID: animal
D/TopicWords: Topic Name: Động vật
D/TopicWords: ✅ Starting AddEditWordActivity...

D/AddEditWord: ===== INIT VIEWS =====
D/AddEditWord: wordId: 123abc
D/AddEditWord: topicId: animal
D/AddEditWord: topicName: Động vật
D/AddEditWord: ✅ EDIT MODE - sẽ pre-fill data

D/AddEditWord: ===== LOADING WORD DATA FOR EDIT =====
D/AddEditWord: word: bird
D/AddEditWord: meaning: con chim
D/AddEditWord: pronunciation: /bɜːrd/
D/AddEditWord: example: Birds fly in the sky
D/AddEditWord: ✅ Đã set word: bird
D/AddEditWord: ✅ Đã set meaning: con chim
D/AddEditWord: ✅ Đã set pronunciation: /bɜːrd/
D/AddEditWord: ✅ Đã set example: Birds fly in the sky
D/AddEditWord: ✅ HOÀN TẤT pre-fill!
```

### Log mẫu KHI BỊ LỖI (data null):

```
D/TopicWords: ===== ON EDIT CLICK =====
D/TopicWords: Word ID: null          ← ⚠️ LỖI Ở ĐÂY
D/TopicWords: Word: null             ← ⚠️ LỖI Ở ĐÂY
D/TopicWords: Meaning: null          ← ⚠️ LỖI Ở ĐÂY
...

D/AddEditWord: ===== LOADING WORD DATA FOR EDIT =====
D/AddEditWord: word: null
D/AddEditWord: meaning: null
D/AddEditWord: ❌ TẤT CẢ DATA ĐỀU NULL!
```

## Bước 4: Gửi log cho tôi

**Copy toàn bộ log** từ `TopicWords: ===== ON EDIT CLICK` đến `AddEditWord: ✅ HOÀN TẤT pre-fill!`

Gửi cho tôi để tôi phân tích nguyên nhân chính xác!

---

## 🎯 Các trường hợp có thể xảy ra:

### Trường hợp 1: wordId là null
→ **Nguyên nhân**: API không trả về id
→ **Giải pháp**: Kiểm tra response từ API

### Trường hợp 2: word, meaning là null
→ **Nguyên nhân**: Field name không khớp (english vs word)
→ **Giải pháp**: Sửa Word.java

### Trường hợp 3: pronunciation là null
→ **Nguyên nhân**: API không có field này
→ **Giải pháp**: Bỏ qua, không bắt buộc

### Trường hợp 4: Log hiển thị đầy đủ data nhưng form vẫn trống
→ **Nguyên nhân**: EditText không được set đúng
→ **Giải pháp**: Kiểm tra layout XML

---

## ✅ Sau khi có log, tôi sẽ:

1. Xác định chính xác nguyên nhân
2. Fix code đúng chỗ
3. Đảm bảo pre-fill hoạt động 100%

**Chạy test và gửi log cho tôi nhé!** 🚀


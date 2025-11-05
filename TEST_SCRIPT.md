# 🎯 TEST SCRIPT CỤ THỂ - PHẢI LÀM ĐÚNG THỨ TỰ

## BƯỚC 1: Build lại (BẮT BUỘC!)

1. Trong Android Studio, click **Build** → **Clean Project**
2. Đợi xong (khoảng 10 giây)
3. Click **Build** → **Rebuild Project**  
4. Đợi xong (khoảng 30-60 giây)

⚠️ **QUAN TRỌNG**: Phải clean + rebuild nếu không sẽ vẫn chạy code cũ!

---

## BƯỚC 2: Mở Logcat

1. Click **View** → **Tool Windows** → **Logcat** (hoặc nhấn `Alt+6`)
2. Ở thanh filter phía trên, xóa hết
3. Gõ vào: `package:mine tag:TopicWords|AddEditWord level:debug`
4. Hoặc đơn giản hơn: `TopicWords|AddEditWord`

---

## BƯỚC 3: Chạy app và test CỤ THỂ

### Test Case 1: Edit từ có sẵn

1. **Chạy app** (nhấn Shift+F10)
2. **Vào chủ đề "Động vật"** (hoặc bất kỳ chủ đề nào có từ)
3. Tìm từ **"cat"** hoặc **"dog"** hoặc bất kỳ từ nào
4. **Click icon Edit (✏️)** bên cạnh từ đó
5. **Chờ 2 giây** và quan sát:
   - Có Toast "Đang tải dữ liệu..." hiện không?
   - Form có data không?
   - Logcat có log không?

---

## BƯỚC 4: Chụp màn hình và copy log

### A. Chụp màn hình app

Chụp lại màn hình sau khi click Edit, để tôi thấy form có gì

### B. Copy log từ Logcat

1. Trong Logcat, tìm dòng `TopicWords: ===== ON EDIT CLICK =====`
2. Select (bôi đen) từ dòng đó đến dòng `AddEditWord: ✅ HOÀN TẤT pre-fill!`
3. Nhấn `Ctrl+C` để copy
4. Paste vào đây

---

## 🔍 Những log BẠN PHẢI THẤY:

### Log khi click Edit icon:

```
D/TopicWords: ===== ON EDIT CLICK =====
D/TopicWords: Word ID: 123
D/TopicWords: Word: cat
D/TopicWords: Meaning: con mèo
D/TopicWords: Pronunciation: /kæt/
D/TopicWords: Example: The cat is sleeping
D/TopicWords: ✅ Starting AddEditWordActivity...
```

### Log khi mở AddEditWordActivity:

```
D/AddEditWord: ===== INIT VIEWS =====
D/AddEditWord: wordId: 123
D/AddEditWord: ✅ EDIT MODE - sẽ pre-fill data

D/AddEditWord: ===== LOADING WORD DATA FOR EDIT =====
D/AddEditWord: word: cat
D/AddEditWord: meaning: con mèo
D/AddEditWord: ✅ Đã set word: cat
D/AddEditWord:    etWord text sau khi set: cat
D/AddEditWord: ✅ Đã set meaning: con mèo
D/AddEditWord:    etMeaning text sau khi set: con mèo
D/AddEditWord: ✅ HOÀN TẤT pre-fill!
```

### ❌ Nếu thấy log SAI:

```
D/AddEditWord: wordId: null          ← LỖI: wordId null
D/AddEditWord: ⚠️ wordId là NULL!
```

Hoặc:

```
D/AddEditWord: word: null           ← LỖI: data null
D/AddEditWord: ⚠️ word là null
```

---

## 📱 Kết quả mong đợi:

### ✅ Khi THÀNH CÔNG:

1. Khi click Edit, thấy Toast: "Đang tải dữ liệu để chỉnh sửa..."
2. Form hiện ra với **TẤT CẢ trường đã điền sẵn**:
   - Từ tiếng Anh: **"cat"** (KHÔNG phải "Ví dụ: phone")
   - Nghĩa tiếng Việt: **"con mèo"** (KHÔNG phải "Ví dụ: điện thoại")
   - Phát âm: **"/kæt/"**
   - Câu ví dụ: **"The cat is sleeping"**
3. Logcat có đầy đủ log như trên

### ❌ Khi VẪN LỖI:

1. KHÔNG thấy Toast
2. Form vẫn trống, chỉ có placeholder
3. Logcat có log lỗi `wordId: null` hoặc `word: null`

---

## 🚀 Sau khi test:

Gửi cho tôi:
1. **Screenshot** màn hình form sau khi click Edit
2. **Log** từ Logcat (copy toàn bộ từ `TopicWords: =====` đến `AddEditWord: ✅ HOÀN TẤT`)
3. Cho tôi biết:
   - Có thấy Toast "Đang tải dữ liệu..." không?
   - Form có data hay vẫn trống?

Tôi sẽ phân tích chính xác 100% và fix đúng chỗ!


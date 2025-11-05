package com.example.flashcard_manager.utils;

import com.example.flashcard_manager.api.RetrofitClient;
import com.example.flashcard_manager.models.Word;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleDataGenerator {

    public interface OnDataLoadedListener {
        void onSuccess(int count);
        void onFailure(String error);
    }

    public static void generateSampleWords(OnDataLoadedListener listener) {
        List<Word> sampleWords = createSampleWords();

        final int[] successCount = {0};
        final int[] failCount = {0};
        final int totalWords = sampleWords.size();

        for (Word word : sampleWords) {
            RetrofitClient.getWordApiService().createWord(word).enqueue(new Callback<Word>() {
                @Override
                public void onResponse(Call<Word> call, Response<Word> response) {
                    successCount[0]++;
                    checkCompletion();
                }

                @Override
                public void onFailure(Call<Word> call, Throwable t) {
                    failCount[0]++;
                    checkCompletion();
                }

                private void checkCompletion() {
                    if (successCount[0] + failCount[0] == totalWords) {
                        if (successCount[0] > 0) {
                            listener.onSuccess(successCount[0]);
                        } else {
                            listener.onFailure("Không thể thêm từ vựng");
                        }
                    }
                }
            });
        }
    }

    private static List<Word> createSampleWords() {
        List<Word> words = new ArrayList<>();

        // Topic 1: Động từ thường gặp
        words.add(new Word("go", "đi", "ɡoʊ", "I go to school every day.", "1", "Động từ thường gặp"));
        words.add(new Word("come", "đến", "kʌm", "Come here, please.", "1", "Động từ thường gặp"));
        words.add(new Word("eat", "ăn", "iːt", "I eat breakfast at 7 AM.", "1", "Động từ thường gặp"));
        words.add(new Word("drink", "uống", "drɪŋk", "She drinks coffee every morning.", "1", "Động từ thường gặp"));
        words.add(new Word("sleep", "ngủ", "sliːp", "I sleep 8 hours a day.", "1", "Động từ thường gặp"));
        words.add(new Word("study", "học", "ˈstʌdi", "Students study hard for exams.", "1", "Động từ thường gặp"));
        words.add(new Word("work", "làm việc", "wɜːrk", "He works at a bank.", "1", "Động từ thường gặp"));
        words.add(new Word("play", "chơi", "pleɪ", "Children play in the park.", "1", "Động từ thường gặp"));
        words.add(new Word("read", "đọc", "riːd", "I read books every night.", "1", "Động từ thường gặp"));
        words.add(new Word("write", "viết", "raɪt", "She writes a letter.", "1", "Động từ thường gặp"));

        // Topic 2: Danh từ hàng ngày
        words.add(new Word("house", "nhà", "haʊs", "I live in a big house.", "2", "Danh từ hàng ngày"));
        words.add(new Word("car", "xe hơi", "kɑːr", "My father drives a car.", "2", "Danh từ hàng ngày"));
        words.add(new Word("phone", "điện thoại", "foʊn", "Can I use your phone?", "2", "Danh từ hàng ngày"));
        words.add(new Word("book", "sách", "bʊk", "This book is very interesting.", "2", "Danh từ hàng ngày"));
        words.add(new Word("table", "bàn", "ˈteɪbl", "Put the book on the table.", "2", "Danh từ hàng ngày"));
        words.add(new Word("chair", "ghế", "tʃer", "Sit on this chair.", "2", "Danh từ hàng ngày"));
        words.add(new Word("computer", "máy tính", "kəmˈpjuːtər", "I work on my computer.", "2", "Danh từ hàng ngày"));
        words.add(new Word("water", "nước", "ˈwɔːtər", "Drink more water.", "2", "Danh từ hàng ngày"));
        words.add(new Word("food", "thức ăn", "fuːd", "Vietnamese food is delicious.", "2", "Danh từ hàng ngày"));
        words.add(new Word("money", "tiền", "ˈmʌni", "I need some money.", "2", "Danh từ hàng ngày"));

        // Topic 3: Tính từ mô tả
        words.add(new Word("big", "to, lớn", "bɪɡ", "This is a big house.", "3", "Tính từ mô tả"));
        words.add(new Word("small", "nhỏ", "smɔːl", "I have a small dog.", "3", "Tính từ mô tả"));
        words.add(new Word("good", "tốt", "ɡʊd", "That's a good idea.", "3", "Tính từ mô tả"));
        words.add(new Word("bad", "xấu, tệ", "bæd", "Today is a bad day.", "3", "Tính từ mô tả"));
        words.add(new Word("beautiful", "đẹp", "ˈbjuːtɪfl", "She is beautiful.", "3", "Tính từ mô tả"));
        words.add(new Word("happy", "vui vẻ", "ˈhæpi", "I am happy today.", "3", "Tính từ mô tả"));
        words.add(new Word("sad", "buồn", "sæd", "Don't be sad.", "3", "Tính từ mô tả"));
        words.add(new Word("easy", "dễ", "ˈiːzi", "This test is easy.", "3", "Tính từ mô tả"));
        words.add(new Word("difficult", "khó", "ˈdɪfɪkəlt", "Math is difficult.", "3", "Tính từ mô tả"));
        words.add(new Word("fast", "nhanh", "fæst", "He runs fast.", "3", "Tính từ mô tả"));

        // Topic 4: Từ vựng công việc
        words.add(new Word("job", "công việc", "dʒɑːb", "I love my job.", "4", "Từ vựng công việc"));
        words.add(new Word("office", "văn phòng", "ˈɔːfɪs", "I work in an office.", "4", "Từ vựng công việc"));
        words.add(new Word("meeting", "cuộc họp", "ˈmiːtɪŋ", "We have a meeting at 2 PM.", "4", "Từ vựng công việc"));
        words.add(new Word("boss", "sếp", "bɔːs", "My boss is very kind.", "4", "Từ vựng công việc"));
        words.add(new Word("colleague", "đồng nghiệp", "ˈkɑːliːɡ", "My colleagues are friendly.", "4", "Từ vựng công việc"));
        words.add(new Word("project", "dự án", "ˈprɑːdʒekt", "This project is important.", "4", "Từ vựng công việc"));
        words.add(new Word("deadline", "hạn chót", "ˈdedlaɪn", "The deadline is tomorrow.", "4", "Từ vựng công việc"));
        words.add(new Word("salary", "lương", "ˈsæləri", "My salary is good.", "4", "Từ vựng công việc"));
        words.add(new Word("interview", "phỏng vấn", "ˈɪntərvjuː", "I have a job interview.", "4", "Từ vựng công việc"));
        words.add(new Word("contract", "hợp đồng", "ˈkɑːntrækt", "Sign the contract.", "4", "Từ vựng công việc"));

        // Topic 5: Từ vựng du lịch
        words.add(new Word("hotel", "khách sạn", "hoʊˈtel", "We stay at a nice hotel.", "5", "Từ vựng du lịch"));
        words.add(new Word("airport", "sân bay", "ˈerpɔːrt", "The airport is crowded.", "5", "Từ vựng du lịch"));
        words.add(new Word("ticket", "vé", "ˈtɪkɪt", "I need to buy a ticket.", "5", "Từ vựng du lịch"));
        words.add(new Word("passport", "hộ chiếu", "ˈpæspɔːrt", "Don't forget your passport.", "5", "Từ vựng du lịch"));
        words.add(new Word("luggage", "hành lý", "ˈlʌɡɪdʒ", "Where is my luggage?", "5", "Từ vựng du lịch"));
        words.add(new Word("beach", "bãi biển", "biːtʃ", "I love the beach.", "5", "Từ vựng du lịch"));
        words.add(new Word("tour", "chuyến tham quan", "tʊr", "We join a city tour.", "5", "Từ vựng du lịch"));
        words.add(new Word("guide", "hướng dẫn viên", "ɡaɪd", "The guide is helpful.", "5", "Từ vựng du lịch"));
        words.add(new Word("map", "bản đồ", "mæp", "I need a map.", "5", "Từ vựng du lịch"));
        words.add(new Word("souvenir", "quà lưu niệm", "ˌsuːvəˈnɪr", "Buy some souvenirs.", "5", "Từ vựng du lịch"));

        return words;
    }
}

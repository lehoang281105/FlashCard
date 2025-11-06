package com.example.flashcard_quiz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class QuizUtils {

    /**
     * Kiểm tra kết nối internet
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    /**
     * Tính phần trăm điểm số
     */
    public static double calculatePercentage(int score, int total) {
        if (total == 0) return 0;
        return (score * 100.0) / total;
    }

    /**
     * Lấy thông điệp dựa trên phần trăm
     */
    public static String getResultMessage(double percentage) {
        if (percentage == 100) {
            return "🎉 XUẤT SẮC!";
        } else if (percentage >= 80) {
            return "😊 RẤT TỐT!";
        } else if (percentage >= 60) {
            return "👍 KHÁ!";
        } else if (percentage >= 40) {
            return "💪 CÓ TIẾN BỘ!";
        } else {
            return "📚 CẦN CỐ GẮNG!";
        }
    }

    /**
     * Lấy mô tả chi tiết dựa trên phần trăm
     */
    public static String getResultDescription(double percentage) {
        if (percentage == 100) {
            return "Hoàn hảo! Bạn đã trả lời đúng tất cả!";
        } else if (percentage >= 80) {
            return "Tuyệt vời! Bạn làm rất tốt!";
        } else if (percentage >= 60) {
            return "Khá ổn! Hãy cố gắng hơn nữa!";
        } else if (percentage >= 40) {
            return "Không tệ! Tiếp tục luyện tập nhé!";
        } else {
            return "Đừng nản lòng! Hãy học thêm và thử lại!";
        }
    }

    /**
     * Lấy màu cho kết quả (dưới dạng String color code)
     */
    public static String getResultColor(double percentage) {
        if (percentage >= 80) {
            return "#4CAF50"; // Green
        } else if (percentage >= 60) {
            return "#FF9800"; // Orange
        } else if (percentage >= 40) {
            return "#2196F3"; // Blue
        } else {
            return "#F44336"; // Red
        }
    }


    /**
     * Kiểm tra điểm có phá kỷ lục không
     */
    public static boolean isNewRecord(int currentScore, int bestScore) {
        return currentScore > bestScore;
    }

    /**
     * Lấy rank dựa trên phần trăm
     */
    public static String getRank(double percentage) {
        if (percentage == 100) return "S";
        else if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B+";
        else if (percentage >= 60) return "B";
        else if (percentage >= 50) return "C+";
        else if (percentage >= 40) return "C";
        else return "D";
    }
}

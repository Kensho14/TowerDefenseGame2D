package jp.ac.uryukyu.ie.e195723.utils;

public class FormatUtils {
    /**
     * ミリ秒から m:ss といった時間を表す文字列に変換する
     * @param time milliseconds
     * @return フォーマットされた文字列
     */
    public static String getFormattedTimeText(long time){
        long secs = time/1000;
        long s = secs%60;
        long m = (secs/60)%60;
        return String.format("%d:%02d", m, s);
    }
}

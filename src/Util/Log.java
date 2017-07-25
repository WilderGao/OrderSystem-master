package Util;

/**
 * Created by 小吉哥哥 on 2017/7/21.
 */
public class Log {
    public static void d(String tag, Object o) {
        System.out.print("日志： " + tag + "-----------------");
        System.out.println(o);
    }

    public static void d(Object o) {
        System.out.println("日志： " + o);
    }
}

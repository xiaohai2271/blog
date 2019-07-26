package cn.celess.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : xiaohai
 * @date : 2019/03/28 17:22
 */
public class DateFormatUtil {
    public static String get(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getNow() {
        return get(new Date());
    }
}

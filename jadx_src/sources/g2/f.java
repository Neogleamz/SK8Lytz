package g2;

import android.content.Context;
import android.text.format.DateFormat;
import com.daimajia.numberprogressbar.BuildConfig;
import com.zengge.wifi.Common.App;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f {

    /* renamed from: a  reason: collision with root package name */
    private static Calendar f20186a = Calendar.getInstance();

    public static Date a(String str) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        } catch (ParseException unused) {
            date = null;
        }
        if (date == null) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(str);
            } catch (ParseException unused2) {
                return null;
            }
        }
        return date;
    }

    public static Date b(String str) {
        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-0"));
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException unused) {
            date = null;
        }
        if (date == null) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(str);
            } catch (ParseException unused2) {
                return null;
            }
        }
        return date;
    }

    public static String c(Context context, Date date) {
        return (DateFormat.is24HourFormat(context) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") : new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")).format(date);
    }

    public static String d(int i8, int i9, int i10) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i8, i9, i10);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static String e(int i8, int i9) {
        String string = App.o().getString(2131820667);
        String replace = string.replace("{Hour}", i8 + BuildConfig.FLAVOR);
        return replace.replace("{Minute}", i9 + BuildConfig.FLAVOR);
    }

    public static String f(Context context, int i8, int i9) {
        Date date = new Date();
        date.setHours(i8);
        date.setMinutes(i9);
        return (DateFormat.is24HourFormat(context) ? new SimpleDateFormat("HH:mm") : new SimpleDateFormat("a hh:mm")).format(date);
    }
}

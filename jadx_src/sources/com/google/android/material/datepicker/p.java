package com.google.android.material.datepicker;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.icu.text.DateFormat;
import com.daimajia.numberprogressbar.BuildConfig;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class p {

    /* renamed from: a  reason: collision with root package name */
    static AtomicReference<o> f17887a = new AtomicReference<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long a(long j8) {
        Calendar q = q();
        q.setTimeInMillis(j8);
        return f(q).getTimeInMillis();
    }

    private static int b(String str, String str2, int i8, int i9) {
        while (i9 >= 0 && i9 < str.length() && str2.indexOf(str.charAt(i9)) == -1) {
            if (str.charAt(i9) == '\'') {
                do {
                    i9 += i8;
                    if (i9 >= 0 && i9 < str.length()) {
                    }
                } while (str.charAt(i9) != '\'');
            }
            i9 += i8;
        }
        return i9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @TargetApi(24)
    public static DateFormat c(Locale locale) {
        return e("MMMd", locale);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @TargetApi(24)
    public static DateFormat d(Locale locale) {
        return e("MMMEd", locale);
    }

    @TargetApi(24)
    private static DateFormat e(String str, Locale locale) {
        DateFormat instanceForSkeleton = DateFormat.getInstanceForSkeleton(str, locale);
        instanceForSkeleton.setTimeZone(p());
        return instanceForSkeleton;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Calendar f(Calendar calendar) {
        Calendar r4 = r(calendar);
        Calendar q = q();
        q.set(r4.get(1), r4.get(2), r4.get(5));
        return q;
    }

    private static java.text.DateFormat g(int i8, Locale locale) {
        java.text.DateFormat dateInstance = java.text.DateFormat.getDateInstance(i8, locale);
        dateInstance.setTimeZone(n());
        return dateInstance;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static java.text.DateFormat h(Locale locale) {
        return g(0, locale);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static java.text.DateFormat i(Locale locale) {
        return g(2, locale);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static java.text.DateFormat j(Locale locale) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) i(locale);
        simpleDateFormat.applyPattern(u(simpleDateFormat.toPattern()));
        return simpleDateFormat;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SimpleDateFormat k() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(((SimpleDateFormat) java.text.DateFormat.getDateInstance(3, Locale.getDefault())).toLocalizedPattern().replaceAll("\\s+", BuildConfig.FLAVOR), Locale.getDefault());
        simpleDateFormat.setTimeZone(n());
        simpleDateFormat.setLenient(false);
        return simpleDateFormat;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String l(Resources resources, SimpleDateFormat simpleDateFormat) {
        String localizedPattern = simpleDateFormat.toLocalizedPattern();
        return localizedPattern.replaceAll("d", resources.getString(k7.j.C)).replaceAll("M", resources.getString(k7.j.D)).replaceAll("y", resources.getString(k7.j.E));
    }

    static o m() {
        o oVar = f17887a.get();
        return oVar == null ? o.c() : oVar;
    }

    private static TimeZone n() {
        return TimeZone.getTimeZone("UTC");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Calendar o() {
        Calendar a9 = m().a();
        a9.set(11, 0);
        a9.set(12, 0);
        a9.set(13, 0);
        a9.set(14, 0);
        a9.setTimeZone(n());
        return a9;
    }

    @TargetApi(24)
    private static android.icu.util.TimeZone p() {
        return android.icu.util.TimeZone.getTimeZone("UTC");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Calendar q() {
        return r(null);
    }

    static Calendar r(Calendar calendar) {
        Calendar calendar2 = Calendar.getInstance(n());
        if (calendar == null) {
            calendar2.clear();
        } else {
            calendar2.setTimeInMillis(calendar.getTimeInMillis());
        }
        return calendar2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @TargetApi(24)
    public static DateFormat s(Locale locale) {
        return e("yMMMd", locale);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @TargetApi(24)
    public static DateFormat t(Locale locale) {
        return e("yMMMEd", locale);
    }

    private static String u(String str) {
        int b9 = b(str, "yY", 1, 0);
        if (b9 >= str.length()) {
            return str;
        }
        String str2 = "EMd";
        int b10 = b(str, "EMd", 1, b9);
        if (b10 < str.length()) {
            str2 = "EMd,";
        }
        return str.replace(str.substring(b(str, str2, -1, b9) + 1, b10), " ").trim();
    }
}

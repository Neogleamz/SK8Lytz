package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static androidx.core.util.d<String, String> a(Long l8, Long l9) {
        return b(l8, l9, null);
    }

    static androidx.core.util.d<String, String> b(Long l8, Long l9, SimpleDateFormat simpleDateFormat) {
        if (l8 == null && l9 == null) {
            return androidx.core.util.d.a(null, null);
        }
        if (l8 == null) {
            return androidx.core.util.d.a(null, d(l9.longValue(), simpleDateFormat));
        }
        if (l9 == null) {
            return androidx.core.util.d.a(d(l8.longValue(), simpleDateFormat), null);
        }
        Calendar o5 = p.o();
        Calendar q = p.q();
        q.setTimeInMillis(l8.longValue());
        Calendar q8 = p.q();
        q8.setTimeInMillis(l9.longValue());
        if (simpleDateFormat != null) {
            return androidx.core.util.d.a(simpleDateFormat.format(new Date(l8.longValue())), simpleDateFormat.format(new Date(l9.longValue())));
        }
        return q.get(1) == q8.get(1) ? q.get(1) == o5.get(1) ? androidx.core.util.d.a(f(l8.longValue(), Locale.getDefault()), f(l9.longValue(), Locale.getDefault())) : androidx.core.util.d.a(f(l8.longValue(), Locale.getDefault()), k(l9.longValue(), Locale.getDefault())) : androidx.core.util.d.a(k(l8.longValue(), Locale.getDefault()), k(l9.longValue(), Locale.getDefault()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String c(long j8) {
        return d(j8, null);
    }

    static String d(long j8, SimpleDateFormat simpleDateFormat) {
        Calendar o5 = p.o();
        Calendar q = p.q();
        q.setTimeInMillis(j8);
        return simpleDateFormat != null ? simpleDateFormat.format(new Date(j8)) : o5.get(1) == q.get(1) ? e(j8) : j(j8);
    }

    static String e(long j8) {
        return f(j8, Locale.getDefault());
    }

    static String f(long j8, Locale locale) {
        return Build.VERSION.SDK_INT >= 24 ? p.c(locale).format(new Date(j8)) : p.j(locale).format(new Date(j8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String g(long j8) {
        return h(j8, Locale.getDefault());
    }

    static String h(long j8, Locale locale) {
        return Build.VERSION.SDK_INT >= 24 ? p.d(locale).format(new Date(j8)) : p.h(locale).format(new Date(j8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String i(Context context, long j8) {
        return DateUtils.formatDateTime(context, j8 - TimeZone.getDefault().getOffset(j8), 36);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String j(long j8) {
        return k(j8, Locale.getDefault());
    }

    static String k(long j8, Locale locale) {
        return Build.VERSION.SDK_INT >= 24 ? p.s(locale).format(new Date(j8)) : p.i(locale).format(new Date(j8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String l(long j8) {
        return m(j8, Locale.getDefault());
    }

    static String m(long j8, Locale locale) {
        return Build.VERSION.SDK_INT >= 24 ? p.t(locale).format(new Date(j8)) : p.h(locale).format(new Date(j8));
    }
}

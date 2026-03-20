package com.google.android.material.datepicker;

import java.util.Calendar;
import java.util.TimeZone;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class o {

    /* renamed from: c  reason: collision with root package name */
    private static final o f17884c = new o(null, null);

    /* renamed from: a  reason: collision with root package name */
    private final Long f17885a;

    /* renamed from: b  reason: collision with root package name */
    private final TimeZone f17886b;

    private o(Long l8, TimeZone timeZone) {
        this.f17885a = l8;
        this.f17886b = timeZone;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static o c() {
        return f17884c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Calendar a() {
        return b(this.f17886b);
    }

    Calendar b(TimeZone timeZone) {
        Calendar calendar = timeZone == null ? Calendar.getInstance() : Calendar.getInstance(timeZone);
        Long l8 = this.f17885a;
        if (l8 != null) {
            calendar.setTimeInMillis(l8.longValue());
        }
        return calendar;
    }
}

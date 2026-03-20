package com.google.android.material.datepicker;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class c extends com.google.android.material.internal.l {

    /* renamed from: a  reason: collision with root package name */
    private final TextInputLayout f17817a;

    /* renamed from: b  reason: collision with root package name */
    private final DateFormat f17818b;

    /* renamed from: c  reason: collision with root package name */
    private final CalendarConstraints f17819c;

    /* renamed from: d  reason: collision with root package name */
    private final String f17820d;

    /* renamed from: e  reason: collision with root package name */
    private final Runnable f17821e;

    /* renamed from: f  reason: collision with root package name */
    private Runnable f17822f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String f17823a;

        a(String str) {
            this.f17823a = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            TextInputLayout textInputLayout = c.this.f17817a;
            DateFormat dateFormat = c.this.f17818b;
            Context context = textInputLayout.getContext();
            String string = context.getString(k7.j.f21222s);
            String format = String.format(context.getString(k7.j.f21224u), this.f17823a);
            String format2 = String.format(context.getString(k7.j.f21223t), dateFormat.format(new Date(p.o().getTimeInMillis())));
            textInputLayout.setError(string + "\n" + format + "\n" + format2);
            c.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ long f17825a;

        b(long j8) {
            this.f17825a = j8;
        }

        @Override // java.lang.Runnable
        public void run() {
            c.this.f17817a.setError(String.format(c.this.f17820d, d.c(this.f17825a)));
            c.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(String str, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints) {
        this.f17818b = dateFormat;
        this.f17817a = textInputLayout;
        this.f17819c = calendarConstraints;
        this.f17820d = textInputLayout.getContext().getString(k7.j.f21227x);
        this.f17821e = new a(str);
    }

    private Runnable d(long j8) {
        return new b(j8);
    }

    abstract void e();

    abstract void f(Long l8);

    public void g(View view, Runnable runnable) {
        view.postDelayed(runnable, 1000L);
    }

    @Override // com.google.android.material.internal.l, android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        this.f17817a.removeCallbacks(this.f17821e);
        this.f17817a.removeCallbacks(this.f17822f);
        this.f17817a.setError(null);
        f(null);
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        try {
            Date parse = this.f17818b.parse(charSequence.toString());
            this.f17817a.setError(null);
            long time = parse.getTime();
            if (this.f17819c.f().s0(time) && this.f17819c.l(time)) {
                f(Long.valueOf(parse.getTime()));
                return;
            }
            Runnable d8 = d(time);
            this.f17822f = d8;
            g(this.f17817a, d8);
        } catch (ParseException unused) {
            g(this.f17817a, this.f17821e);
        }
    }
}

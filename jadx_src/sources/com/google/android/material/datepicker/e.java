package com.google.android.material.datepicker;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class e extends BaseAdapter {

    /* renamed from: d  reason: collision with root package name */
    private static final int f17827d;

    /* renamed from: a  reason: collision with root package name */
    private final Calendar f17828a;

    /* renamed from: b  reason: collision with root package name */
    private final int f17829b;

    /* renamed from: c  reason: collision with root package name */
    private final int f17830c;

    static {
        f17827d = Build.VERSION.SDK_INT >= 26 ? 4 : 1;
    }

    public e() {
        Calendar q = p.q();
        this.f17828a = q;
        this.f17829b = q.getMaximum(7);
        this.f17830c = q.getFirstDayOfWeek();
    }

    private int b(int i8) {
        int i9 = i8 + this.f17830c;
        int i10 = this.f17829b;
        return i9 > i10 ? i9 - i10 : i9;
    }

    @Override // android.widget.Adapter
    /* renamed from: a */
    public Integer getItem(int i8) {
        if (i8 >= this.f17829b) {
            return null;
        }
        return Integer.valueOf(b(i8));
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.f17829b;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i8) {
        return 0L;
    }

    @Override // android.widget.Adapter
    @SuppressLint({"WrongConstant"})
    public View getView(int i8, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) view;
        if (view == null) {
            textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(k7.h.f21199v, viewGroup, false);
        }
        this.f17828a.set(7, b(i8));
        textView.setText(this.f17828a.getDisplayName(7, f17827d, textView.getResources().getConfiguration().locale));
        textView.setContentDescription(String.format(viewGroup.getContext().getString(k7.j.f21221r), this.f17828a.getDisplayName(7, 2, Locale.getDefault())));
        return textView;
    }
}

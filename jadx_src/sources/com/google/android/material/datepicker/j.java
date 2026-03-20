package com.google.android.material.datepicker;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Collection;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j extends BaseAdapter {

    /* renamed from: f  reason: collision with root package name */
    static final int f17868f = p.q().getMaximum(4);

    /* renamed from: a  reason: collision with root package name */
    final Month f17869a;

    /* renamed from: b  reason: collision with root package name */
    final DateSelector<?> f17870b;

    /* renamed from: c  reason: collision with root package name */
    private Collection<Long> f17871c;

    /* renamed from: d  reason: collision with root package name */
    b f17872d;

    /* renamed from: e  reason: collision with root package name */
    final CalendarConstraints f17873e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(Month month, DateSelector<?> dateSelector, CalendarConstraints calendarConstraints) {
        this.f17869a = month;
        this.f17870b = dateSelector;
        this.f17873e = calendarConstraints;
        this.f17871c = dateSelector.F0();
    }

    private void e(Context context) {
        if (this.f17872d == null) {
            this.f17872d = new b(context);
        }
    }

    private boolean h(long j8) {
        Iterator<Long> it = this.f17870b.F0().iterator();
        while (it.hasNext()) {
            if (p.a(j8) == p.a(it.next().longValue())) {
                return true;
            }
        }
        return false;
    }

    private void k(TextView textView, long j8) {
        a aVar;
        if (textView == null) {
            return;
        }
        if (this.f17873e.f().s0(j8)) {
            textView.setEnabled(true);
            aVar = h(j8) ? this.f17872d.f17810b : p.o().getTimeInMillis() == j8 ? this.f17872d.f17811c : this.f17872d.f17809a;
        } else {
            textView.setEnabled(false);
            aVar = this.f17872d.f17815g;
        }
        aVar.d(textView);
    }

    private void l(MaterialCalendarGridView materialCalendarGridView, long j8) {
        if (Month.h(j8).equals(this.f17869a)) {
            k((TextView) materialCalendarGridView.getChildAt(materialCalendarGridView.getAdapter2().a(this.f17869a.o(j8)) - materialCalendarGridView.getFirstVisiblePosition()), j8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a(int i8) {
        return b() + (i8 - 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f17869a.j();
    }

    @Override // android.widget.Adapter
    /* renamed from: c */
    public Long getItem(int i8) {
        if (i8 < this.f17869a.j() || i8 > i()) {
            return null;
        }
        return Long.valueOf(this.f17869a.k(j(i8)));
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0080 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0081  */
    @Override // android.widget.Adapter
    /* renamed from: d */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.widget.TextView getView(int r6, android.view.View r7, android.view.ViewGroup r8) {
        /*
            r5 = this;
            android.content.Context r0 = r8.getContext()
            r5.e(r0)
            r0 = r7
            android.widget.TextView r0 = (android.widget.TextView) r0
            r1 = 0
            if (r7 != 0) goto L1e
            android.content.Context r7 = r8.getContext()
            android.view.LayoutInflater r7 = android.view.LayoutInflater.from(r7)
            int r0 = k7.h.f21198u
            android.view.View r7 = r7.inflate(r0, r8, r1)
            r0 = r7
            android.widget.TextView r0 = (android.widget.TextView) r0
        L1e:
            int r7 = r5.b()
            int r7 = r6 - r7
            if (r7 < 0) goto L72
            com.google.android.material.datepicker.Month r8 = r5.f17869a
            int r2 = r8.f17783e
            if (r7 < r2) goto L2d
            goto L72
        L2d:
            r2 = 1
            int r7 = r7 + r2
            r0.setTag(r8)
            android.content.res.Resources r8 = r0.getResources()
            android.content.res.Configuration r8 = r8.getConfiguration()
            java.util.Locale r8 = r8.locale
            java.lang.Object[] r3 = new java.lang.Object[r2]
            java.lang.Integer r4 = java.lang.Integer.valueOf(r7)
            r3[r1] = r4
            java.lang.String r4 = "%d"
            java.lang.String r8 = java.lang.String.format(r8, r4, r3)
            r0.setText(r8)
            com.google.android.material.datepicker.Month r8 = r5.f17869a
            long r7 = r8.k(r7)
            com.google.android.material.datepicker.Month r3 = r5.f17869a
            int r3 = r3.f17781c
            com.google.android.material.datepicker.Month r4 = com.google.android.material.datepicker.Month.i()
            int r4 = r4.f17781c
            if (r3 != r4) goto L64
            java.lang.String r7 = com.google.android.material.datepicker.d.g(r7)
            goto L68
        L64:
            java.lang.String r7 = com.google.android.material.datepicker.d.l(r7)
        L68:
            r0.setContentDescription(r7)
            r0.setVisibility(r1)
            r0.setEnabled(r2)
            goto L7a
        L72:
            r7 = 8
            r0.setVisibility(r7)
            r0.setEnabled(r1)
        L7a:
            java.lang.Long r6 = r5.getItem(r6)
            if (r6 != 0) goto L81
            return r0
        L81:
            long r6 = r6.longValue()
            r5.k(r0, r6)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.datepicker.j.getView(int, android.view.View, android.view.ViewGroup):android.widget.TextView");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f(int i8) {
        return i8 % this.f17869a.f17782d == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean g(int i8) {
        return (i8 + 1) % this.f17869a.f17782d == 0;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.f17869a.f17783e + b();
    }

    @Override // android.widget.Adapter
    public long getItemId(int i8) {
        return i8 / this.f17869a.f17782d;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        return (this.f17869a.j() + this.f17869a.f17783e) - 1;
    }

    int j(int i8) {
        return (i8 - this.f17869a.j()) + 1;
    }

    public void m(MaterialCalendarGridView materialCalendarGridView) {
        for (Long l8 : this.f17871c) {
            l(materialCalendarGridView, l8.longValue());
        }
        DateSelector<?> dateSelector = this.f17870b;
        if (dateSelector != null) {
            for (Long l9 : dateSelector.F0()) {
                l(materialCalendarGridView, l9.longValue());
            }
            this.f17871c = this.f17870b.F0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean n(int i8) {
        return i8 >= b() && i8 <= i();
    }
}

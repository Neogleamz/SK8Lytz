package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.core.view.c0;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.datepicker.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class k extends RecyclerView.g<b> {

    /* renamed from: c  reason: collision with root package name */
    private final Context f17874c;

    /* renamed from: d  reason: collision with root package name */
    private final CalendarConstraints f17875d;

    /* renamed from: e  reason: collision with root package name */
    private final DateSelector<?> f17876e;

    /* renamed from: f  reason: collision with root package name */
    private final f.l f17877f;

    /* renamed from: g  reason: collision with root package name */
    private final int f17878g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements AdapterView.OnItemClickListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ MaterialCalendarGridView f17879a;

        a(MaterialCalendarGridView materialCalendarGridView) {
            this.f17879a = materialCalendarGridView;
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i8, long j8) {
            if (this.f17879a.getAdapter2().n(i8)) {
                k.this.f17877f.a(this.f17879a.getAdapter2().getItem(i8).longValue());
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends RecyclerView.b0 {

        /* renamed from: t  reason: collision with root package name */
        final TextView f17881t;

        /* renamed from: u  reason: collision with root package name */
        final MaterialCalendarGridView f17882u;

        b(LinearLayout linearLayout, boolean z4) {
            super(linearLayout);
            TextView textView = (TextView) linearLayout.findViewById(k7.f.f21171u);
            this.f17881t = textView;
            c0.u0(textView, true);
            this.f17882u = (MaterialCalendarGridView) linearLayout.findViewById(k7.f.q);
            if (z4) {
                return;
            }
            textView.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(Context context, DateSelector<?> dateSelector, CalendarConstraints calendarConstraints, f.l lVar) {
        Month j8 = calendarConstraints.j();
        Month g8 = calendarConstraints.g();
        Month i8 = calendarConstraints.i();
        if (j8.compareTo(i8) > 0) {
            throw new IllegalArgumentException("firstPage cannot be after currentPage");
        }
        if (i8.compareTo(g8) > 0) {
            throw new IllegalArgumentException("currentPage cannot be after lastPage");
        }
        int Y1 = j.f17868f * f.Y1(context);
        int Y12 = g.o2(context) ? f.Y1(context) : 0;
        this.f17874c = context;
        this.f17878g = Y1 + Y12;
        this.f17875d = calendarConstraints;
        this.f17876e = dateSelector;
        this.f17877f = lVar;
        A(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month D(int i8) {
        return this.f17875d.j().s(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence E(int i8) {
        return D(i8).q(this.f17874c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int F(Month month) {
        return this.f17875d.j().t(month);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    /* renamed from: G */
    public void r(b bVar, int i8) {
        Month s8 = this.f17875d.j().s(i8);
        bVar.f17881t.setText(s8.q(bVar.f6628a.getContext()));
        MaterialCalendarGridView materialCalendarGridView = (MaterialCalendarGridView) bVar.f17882u.findViewById(k7.f.q);
        if (materialCalendarGridView.getAdapter2() == null || !s8.equals(materialCalendarGridView.getAdapter2().f17869a)) {
            j jVar = new j(s8, this.f17876e, this.f17875d);
            materialCalendarGridView.setNumColumns(s8.f17782d);
            materialCalendarGridView.setAdapter((ListAdapter) jVar);
        } else {
            materialCalendarGridView.invalidate();
            materialCalendarGridView.getAdapter2().m(materialCalendarGridView);
        }
        materialCalendarGridView.setOnItemClickListener(new a(materialCalendarGridView));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    /* renamed from: H */
    public b t(ViewGroup viewGroup, int i8) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(k7.h.f21201x, viewGroup, false);
        if (g.o2(viewGroup.getContext())) {
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, this.f17878g));
            return new b(linearLayout, true);
        }
        return new b(linearLayout, false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    public int c() {
        return this.f17875d.h();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    public long d(int i8) {
        return this.f17875d.j().s(i8).r();
    }
}

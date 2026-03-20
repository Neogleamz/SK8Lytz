package com.google.android.material.datepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.datepicker.f;
import java.util.Calendar;
import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q extends RecyclerView.g<b> {

    /* renamed from: c  reason: collision with root package name */
    private final f<?> f17888c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements View.OnClickListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f17889a;

        a(int i8) {
            this.f17889a = i8;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            q.this.f17888c.c2(q.this.f17888c.U1().e(Month.f(this.f17889a, q.this.f17888c.W1().f17780b)));
            q.this.f17888c.d2(f.k.DAY);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends RecyclerView.b0 {

        /* renamed from: t  reason: collision with root package name */
        final TextView f17891t;

        b(TextView textView) {
            super(textView);
            this.f17891t = textView;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(f<?> fVar) {
        this.f17888c = fVar;
    }

    private View.OnClickListener D(int i8) {
        return new a(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int E(int i8) {
        return i8 - this.f17888c.U1().j().f17781c;
    }

    int F(int i8) {
        return this.f17888c.U1().j().f17781c + i8;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    /* renamed from: G */
    public void r(b bVar, int i8) {
        int F = F(i8);
        String string = bVar.f17891t.getContext().getString(k7.j.f21226w);
        bVar.f17891t.setText(String.format(Locale.getDefault(), "%d", Integer.valueOf(F)));
        bVar.f17891t.setContentDescription(String.format(string, Integer.valueOf(F)));
        com.google.android.material.datepicker.b V1 = this.f17888c.V1();
        Calendar o5 = p.o();
        com.google.android.material.datepicker.a aVar = o5.get(1) == F ? V1.f17814f : V1.f17812d;
        for (Long l8 : this.f17888c.X1().F0()) {
            o5.setTimeInMillis(l8.longValue());
            if (o5.get(1) == F) {
                aVar = V1.f17813e;
            }
        }
        aVar.d(bVar.f17891t);
        bVar.f17891t.setOnClickListener(D(F));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    /* renamed from: H */
    public b t(ViewGroup viewGroup, int i8) {
        return new b((TextView) LayoutInflater.from(viewGroup.getContext()).inflate(k7.h.f21203z, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    public int c() {
        return this.f17888c.U1().k();
    }
}

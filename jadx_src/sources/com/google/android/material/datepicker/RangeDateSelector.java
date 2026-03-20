package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.android.material.internal.s;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class RangeDateSelector implements DateSelector<androidx.core.util.d<Long, Long>> {
    public static final Parcelable.Creator<RangeDateSelector> CREATOR = new c();

    /* renamed from: a  reason: collision with root package name */
    private String f17786a;

    /* renamed from: b  reason: collision with root package name */
    private final String f17787b = " ";

    /* renamed from: c  reason: collision with root package name */
    private Long f17788c = null;

    /* renamed from: d  reason: collision with root package name */
    private Long f17789d = null;

    /* renamed from: e  reason: collision with root package name */
    private Long f17790e = null;

    /* renamed from: f  reason: collision with root package name */
    private Long f17791f = null;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends com.google.android.material.datepicker.c {

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ TextInputLayout f17792g;

        /* renamed from: h  reason: collision with root package name */
        final /* synthetic */ TextInputLayout f17793h;

        /* renamed from: j  reason: collision with root package name */
        final /* synthetic */ l f17794j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(String str, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints, TextInputLayout textInputLayout2, TextInputLayout textInputLayout3, l lVar) {
            super(str, dateFormat, textInputLayout, calendarConstraints);
            this.f17792g = textInputLayout2;
            this.f17793h = textInputLayout3;
            this.f17794j = lVar;
        }

        @Override // com.google.android.material.datepicker.c
        void e() {
            RangeDateSelector.this.f17790e = null;
            RangeDateSelector.this.j(this.f17792g, this.f17793h, this.f17794j);
        }

        @Override // com.google.android.material.datepicker.c
        void f(Long l8) {
            RangeDateSelector.this.f17790e = l8;
            RangeDateSelector.this.j(this.f17792g, this.f17793h, this.f17794j);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends com.google.android.material.datepicker.c {

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ TextInputLayout f17796g;

        /* renamed from: h  reason: collision with root package name */
        final /* synthetic */ TextInputLayout f17797h;

        /* renamed from: j  reason: collision with root package name */
        final /* synthetic */ l f17798j;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(String str, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints, TextInputLayout textInputLayout2, TextInputLayout textInputLayout3, l lVar) {
            super(str, dateFormat, textInputLayout, calendarConstraints);
            this.f17796g = textInputLayout2;
            this.f17797h = textInputLayout3;
            this.f17798j = lVar;
        }

        @Override // com.google.android.material.datepicker.c
        void e() {
            RangeDateSelector.this.f17791f = null;
            RangeDateSelector.this.j(this.f17796g, this.f17797h, this.f17798j);
        }

        @Override // com.google.android.material.datepicker.c
        void f(Long l8) {
            RangeDateSelector.this.f17791f = l8;
            RangeDateSelector.this.j(this.f17796g, this.f17797h, this.f17798j);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c implements Parcelable.Creator<RangeDateSelector> {
        c() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public RangeDateSelector createFromParcel(Parcel parcel) {
            RangeDateSelector rangeDateSelector = new RangeDateSelector();
            rangeDateSelector.f17788c = (Long) parcel.readValue(Long.class.getClassLoader());
            rangeDateSelector.f17789d = (Long) parcel.readValue(Long.class.getClassLoader());
            return rangeDateSelector;
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public RangeDateSelector[] newArray(int i8) {
            return new RangeDateSelector[i8];
        }
    }

    private void f(TextInputLayout textInputLayout, TextInputLayout textInputLayout2) {
        if (textInputLayout.getError() != null && this.f17786a.contentEquals(textInputLayout.getError())) {
            textInputLayout.setError(null);
        }
        if (textInputLayout2.getError() == null || !" ".contentEquals(textInputLayout2.getError())) {
            return;
        }
        textInputLayout2.setError(null);
    }

    private boolean h(long j8, long j9) {
        return j8 <= j9;
    }

    private void i(TextInputLayout textInputLayout, TextInputLayout textInputLayout2) {
        textInputLayout.setError(this.f17786a);
        textInputLayout2.setError(" ");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j(TextInputLayout textInputLayout, TextInputLayout textInputLayout2, l<androidx.core.util.d<Long, Long>> lVar) {
        Long l8 = this.f17790e;
        if (l8 == null || this.f17791f == null) {
            f(textInputLayout, textInputLayout2);
            lVar.a();
        } else if (!h(l8.longValue(), this.f17791f.longValue())) {
            i(textInputLayout, textInputLayout2);
            lVar.a();
        } else {
            this.f17788c = this.f17790e;
            this.f17789d = this.f17791f;
            lVar.b(G0());
        }
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<Long> F0() {
        ArrayList arrayList = new ArrayList();
        Long l8 = this.f17788c;
        if (l8 != null) {
            arrayList.add(l8);
        }
        Long l9 = this.f17789d;
        if (l9 != null) {
            arrayList.add(l9);
        }
        return arrayList;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public String K(Context context) {
        Resources resources = context.getResources();
        Long l8 = this.f17788c;
        if (l8 == null && this.f17789d == null) {
            return resources.getString(k7.j.B);
        }
        Long l9 = this.f17789d;
        if (l9 == null) {
            return resources.getString(k7.j.f21229z, d.c(l8.longValue()));
        }
        if (l8 == null) {
            return resources.getString(k7.j.f21228y, d.c(l9.longValue()));
        }
        androidx.core.util.d<String, String> a9 = d.a(l8, l9);
        return resources.getString(k7.j.A, a9.f4889a, a9.f4890b);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<androidx.core.util.d<Long, Long>> M() {
        if (this.f17788c == null || this.f17789d == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new androidx.core.util.d(this.f17788c, this.f17789d));
        return arrayList;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public void O0(long j8) {
        Long l8 = this.f17788c;
        if (l8 != null) {
            if (this.f17789d == null && h(l8.longValue(), j8)) {
                this.f17789d = Long.valueOf(j8);
                return;
            }
            this.f17789d = null;
        }
        this.f17788c = Long.valueOf(j8);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    /* renamed from: g */
    public androidx.core.util.d<Long, Long> G0() {
        return new androidx.core.util.d<>(this.f17788c, this.f17789d);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public View j0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, CalendarConstraints calendarConstraints, l<androidx.core.util.d<Long, Long>> lVar) {
        View inflate = layoutInflater.inflate(k7.h.G, viewGroup, false);
        TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(k7.f.J);
        TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(k7.f.I);
        EditText editText = textInputLayout.getEditText();
        EditText editText2 = textInputLayout2.getEditText();
        if (com.google.android.material.internal.d.a()) {
            editText.setInputType(17);
            editText2.setInputType(17);
        }
        this.f17786a = inflate.getResources().getString(k7.j.f21225v);
        SimpleDateFormat k8 = p.k();
        Long l8 = this.f17788c;
        if (l8 != null) {
            editText.setText(k8.format(l8));
            this.f17790e = this.f17788c;
        }
        Long l9 = this.f17789d;
        if (l9 != null) {
            editText2.setText(k8.format(l9));
            this.f17791f = this.f17789d;
        }
        String l10 = p.l(inflate.getResources(), k8);
        textInputLayout.setPlaceholderText(l10);
        textInputLayout2.setPlaceholderText(l10);
        editText.addTextChangedListener(new a(l10, k8, textInputLayout, calendarConstraints, textInputLayout, textInputLayout2, lVar));
        editText2.addTextChangedListener(new b(l10, k8, textInputLayout2, calendarConstraints, textInputLayout, textInputLayout2, lVar));
        s.k(editText);
        return inflate;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public int p0(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return u7.b.c(context, Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) > resources.getDimensionPixelSize(k7.d.V) ? k7.b.F : k7.b.D, g.class.getCanonicalName());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeValue(this.f17788c);
        parcel.writeValue(this.f17789d);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public boolean z0() {
        Long l8 = this.f17788c;
        return (l8 == null || this.f17789d == null || !h(l8.longValue(), this.f17789d.longValue())) ? false : true;
    }
}

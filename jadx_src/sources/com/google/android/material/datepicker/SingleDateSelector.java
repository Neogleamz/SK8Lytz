package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
public class SingleDateSelector implements DateSelector<Long> {
    public static final Parcelable.Creator<SingleDateSelector> CREATOR = new b();

    /* renamed from: a  reason: collision with root package name */
    private Long f17800a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends c {

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ l f17801g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(String str, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints, l lVar) {
            super(str, dateFormat, textInputLayout, calendarConstraints);
            this.f17801g = lVar;
        }

        @Override // com.google.android.material.datepicker.c
        void e() {
            this.f17801g.a();
        }

        @Override // com.google.android.material.datepicker.c
        void f(Long l8) {
            if (l8 == null) {
                SingleDateSelector.this.c();
            } else {
                SingleDateSelector.this.O0(l8.longValue());
            }
            this.f17801g.b(SingleDateSelector.this.G0());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b implements Parcelable.Creator<SingleDateSelector> {
        b() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public SingleDateSelector createFromParcel(Parcel parcel) {
            SingleDateSelector singleDateSelector = new SingleDateSelector();
            singleDateSelector.f17800a = (Long) parcel.readValue(Long.class.getClassLoader());
            return singleDateSelector;
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public SingleDateSelector[] newArray(int i8) {
            return new SingleDateSelector[i8];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        this.f17800a = null;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<Long> F0() {
        ArrayList arrayList = new ArrayList();
        Long l8 = this.f17800a;
        if (l8 != null) {
            arrayList.add(l8);
        }
        return arrayList;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public String K(Context context) {
        Resources resources = context.getResources();
        Long l8 = this.f17800a;
        if (l8 == null) {
            return resources.getString(k7.j.q);
        }
        return resources.getString(k7.j.f21220p, d.j(l8.longValue()));
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<androidx.core.util.d<Long, Long>> M() {
        return new ArrayList();
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public void O0(long j8) {
        this.f17800a = Long.valueOf(j8);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    /* renamed from: d */
    public Long G0() {
        return this.f17800a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public View j0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, CalendarConstraints calendarConstraints, l<Long> lVar) {
        View inflate = layoutInflater.inflate(k7.h.F, viewGroup, false);
        TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(k7.f.H);
        EditText editText = textInputLayout.getEditText();
        if (com.google.android.material.internal.d.a()) {
            editText.setInputType(17);
        }
        SimpleDateFormat k8 = p.k();
        String l8 = p.l(inflate.getResources(), k8);
        textInputLayout.setPlaceholderText(l8);
        Long l9 = this.f17800a;
        if (l9 != null) {
            editText.setText(k8.format(l9));
        }
        editText.addTextChangedListener(new a(l8, k8, textInputLayout, calendarConstraints, lVar));
        s.k(editText);
        return inflate;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public int p0(Context context) {
        return u7.b.c(context, k7.b.F, g.class.getCanonicalName());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeValue(this.f17800a);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public boolean z0() {
        return this.f17800a != null;
    }
}

package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.material.datepicker.CalendarConstraints;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CompositeDateValidator implements CalendarConstraints.DateValidator {

    /* renamed from: a  reason: collision with root package name */
    private final d f17772a;

    /* renamed from: b  reason: collision with root package name */
    private final List<CalendarConstraints.DateValidator> f17773b;

    /* renamed from: c  reason: collision with root package name */
    private static final d f17770c = new a();

    /* renamed from: d  reason: collision with root package name */
    private static final d f17771d = new b();
    public static final Parcelable.Creator<CompositeDateValidator> CREATOR = new c();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements d {
        a() {
        }

        @Override // com.google.android.material.datepicker.CompositeDateValidator.d
        public boolean a(List<CalendarConstraints.DateValidator> list, long j8) {
            for (CalendarConstraints.DateValidator dateValidator : list) {
                if (dateValidator != null && dateValidator.s0(j8)) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.google.android.material.datepicker.CompositeDateValidator.d
        public int e() {
            return 1;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b implements d {
        b() {
        }

        @Override // com.google.android.material.datepicker.CompositeDateValidator.d
        public boolean a(List<CalendarConstraints.DateValidator> list, long j8) {
            for (CalendarConstraints.DateValidator dateValidator : list) {
                if (dateValidator != null && !dateValidator.s0(j8)) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.android.material.datepicker.CompositeDateValidator.d
        public int e() {
            return 2;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c implements Parcelable.Creator<CompositeDateValidator> {
        c() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public CompositeDateValidator createFromParcel(Parcel parcel) {
            ArrayList readArrayList = parcel.readArrayList(CalendarConstraints.DateValidator.class.getClassLoader());
            int readInt = parcel.readInt();
            return new CompositeDateValidator((List) androidx.core.util.h.h(readArrayList), (readInt != 2 && readInt == 1) ? CompositeDateValidator.f17770c : CompositeDateValidator.f17771d, null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public CompositeDateValidator[] newArray(int i8) {
            return new CompositeDateValidator[i8];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        boolean a(List<CalendarConstraints.DateValidator> list, long j8);

        int e();
    }

    private CompositeDateValidator(List<CalendarConstraints.DateValidator> list, d dVar) {
        this.f17773b = list;
        this.f17772a = dVar;
    }

    /* synthetic */ CompositeDateValidator(List list, d dVar, a aVar) {
        this(list, dVar);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CompositeDateValidator) {
            CompositeDateValidator compositeDateValidator = (CompositeDateValidator) obj;
            return this.f17773b.equals(compositeDateValidator.f17773b) && this.f17772a.e() == compositeDateValidator.f17772a.e();
        }
        return false;
    }

    public int hashCode() {
        return this.f17773b.hashCode();
    }

    @Override // com.google.android.material.datepicker.CalendarConstraints.DateValidator
    public boolean s0(long j8) {
        return this.f17772a.a(this.f17773b, j8);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeList(this.f17773b);
        parcel.writeInt(this.f17772a.e());
    }
}

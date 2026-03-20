package com.google.android.material.datepicker;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CalendarConstraints implements Parcelable {
    public static final Parcelable.Creator<CalendarConstraints> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private final Month f17758a;

    /* renamed from: b  reason: collision with root package name */
    private final Month f17759b;

    /* renamed from: c  reason: collision with root package name */
    private final DateValidator f17760c;

    /* renamed from: d  reason: collision with root package name */
    private Month f17761d;

    /* renamed from: e  reason: collision with root package name */
    private final int f17762e;

    /* renamed from: f  reason: collision with root package name */
    private final int f17763f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface DateValidator extends Parcelable {
        boolean s0(long j8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Parcelable.Creator<CalendarConstraints> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public CalendarConstraints createFromParcel(Parcel parcel) {
            return new CalendarConstraints((Month) parcel.readParcelable(Month.class.getClassLoader()), (Month) parcel.readParcelable(Month.class.getClassLoader()), (DateValidator) parcel.readParcelable(DateValidator.class.getClassLoader()), (Month) parcel.readParcelable(Month.class.getClassLoader()), null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public CalendarConstraints[] newArray(int i8) {
            return new CalendarConstraints[i8];
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: e  reason: collision with root package name */
        static final long f17764e = p.a(Month.f(1900, 0).f17784f);

        /* renamed from: f  reason: collision with root package name */
        static final long f17765f = p.a(Month.f(2100, 11).f17784f);

        /* renamed from: a  reason: collision with root package name */
        private long f17766a;

        /* renamed from: b  reason: collision with root package name */
        private long f17767b;

        /* renamed from: c  reason: collision with root package name */
        private Long f17768c;

        /* renamed from: d  reason: collision with root package name */
        private DateValidator f17769d;

        /* JADX INFO: Access modifiers changed from: package-private */
        public b(CalendarConstraints calendarConstraints) {
            this.f17766a = f17764e;
            this.f17767b = f17765f;
            this.f17769d = DateValidatorPointForward.a(Long.MIN_VALUE);
            this.f17766a = calendarConstraints.f17758a.f17784f;
            this.f17767b = calendarConstraints.f17759b.f17784f;
            this.f17768c = Long.valueOf(calendarConstraints.f17761d.f17784f);
            this.f17769d = calendarConstraints.f17760c;
        }

        public CalendarConstraints a() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("DEEP_COPY_VALIDATOR_KEY", this.f17769d);
            Month h8 = Month.h(this.f17766a);
            Month h9 = Month.h(this.f17767b);
            DateValidator dateValidator = (DateValidator) bundle.getParcelable("DEEP_COPY_VALIDATOR_KEY");
            Long l8 = this.f17768c;
            return new CalendarConstraints(h8, h9, dateValidator, l8 == null ? null : Month.h(l8.longValue()), null);
        }

        public b b(long j8) {
            this.f17768c = Long.valueOf(j8);
            return this;
        }
    }

    private CalendarConstraints(Month month, Month month2, DateValidator dateValidator, Month month3) {
        this.f17758a = month;
        this.f17759b = month2;
        this.f17761d = month3;
        this.f17760c = dateValidator;
        if (month3 != null && month.compareTo(month3) > 0) {
            throw new IllegalArgumentException("start Month cannot be after current Month");
        }
        if (month3 != null && month3.compareTo(month2) > 0) {
            throw new IllegalArgumentException("current Month cannot be after end Month");
        }
        this.f17763f = month.t(month2) + 1;
        this.f17762e = (month2.f17781c - month.f17781c) + 1;
    }

    /* synthetic */ CalendarConstraints(Month month, Month month2, DateValidator dateValidator, Month month3, a aVar) {
        this(month, month2, dateValidator, month3);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month e(Month month) {
        return month.compareTo(this.f17758a) < 0 ? this.f17758a : month.compareTo(this.f17759b) > 0 ? this.f17759b : month;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CalendarConstraints) {
            CalendarConstraints calendarConstraints = (CalendarConstraints) obj;
            return this.f17758a.equals(calendarConstraints.f17758a) && this.f17759b.equals(calendarConstraints.f17759b) && androidx.core.util.c.a(this.f17761d, calendarConstraints.f17761d) && this.f17760c.equals(calendarConstraints.f17760c);
        }
        return false;
    }

    public DateValidator f() {
        return this.f17760c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month g() {
        return this.f17759b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int h() {
        return this.f17763f;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.f17758a, this.f17759b, this.f17761d, this.f17760c});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month i() {
        return this.f17761d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month j() {
        return this.f17758a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int k() {
        return this.f17762e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l(long j8) {
        if (this.f17758a.k(1) <= j8) {
            Month month = this.f17759b;
            if (j8 <= month.k(month.f17783e)) {
                return true;
            }
        }
        return false;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeParcelable(this.f17758a, 0);
        parcel.writeParcelable(this.f17759b, 0);
        parcel.writeParcelable(this.f17761d, 0);
        parcel.writeParcelable(this.f17760c, 0);
    }
}

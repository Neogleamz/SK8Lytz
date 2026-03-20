package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Month implements Comparable<Month>, Parcelable {
    public static final Parcelable.Creator<Month> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private final Calendar f17779a;

    /* renamed from: b  reason: collision with root package name */
    final int f17780b;

    /* renamed from: c  reason: collision with root package name */
    final int f17781c;

    /* renamed from: d  reason: collision with root package name */
    final int f17782d;

    /* renamed from: e  reason: collision with root package name */
    final int f17783e;

    /* renamed from: f  reason: collision with root package name */
    final long f17784f;

    /* renamed from: g  reason: collision with root package name */
    private String f17785g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Parcelable.Creator<Month> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public Month createFromParcel(Parcel parcel) {
            return Month.f(parcel.readInt(), parcel.readInt());
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public Month[] newArray(int i8) {
            return new Month[i8];
        }
    }

    private Month(Calendar calendar) {
        calendar.set(5, 1);
        Calendar f5 = p.f(calendar);
        this.f17779a = f5;
        this.f17780b = f5.get(2);
        this.f17781c = f5.get(1);
        this.f17782d = f5.getMaximum(7);
        this.f17783e = f5.getActualMaximum(5);
        this.f17784f = f5.getTimeInMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Month f(int i8, int i9) {
        Calendar q = p.q();
        q.set(1, i8);
        q.set(2, i9);
        return new Month(q);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Month h(long j8) {
        Calendar q = p.q();
        q.setTimeInMillis(j8);
        return new Month(q);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Month i() {
        return new Month(p.o());
    }

    @Override // java.lang.Comparable
    /* renamed from: c */
    public int compareTo(Month month) {
        return this.f17779a.compareTo(month.f17779a);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Month) {
            Month month = (Month) obj;
            return this.f17780b == month.f17780b && this.f17781c == month.f17781c;
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.f17780b), Integer.valueOf(this.f17781c)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j() {
        int firstDayOfWeek = this.f17779a.get(7) - this.f17779a.getFirstDayOfWeek();
        return firstDayOfWeek < 0 ? firstDayOfWeek + this.f17782d : firstDayOfWeek;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long k(int i8) {
        Calendar f5 = p.f(this.f17779a);
        f5.set(5, i8);
        return f5.getTimeInMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o(long j8) {
        Calendar f5 = p.f(this.f17779a);
        f5.setTimeInMillis(j8);
        return f5.get(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String q(Context context) {
        if (this.f17785g == null) {
            this.f17785g = d.i(context, this.f17779a.getTimeInMillis());
        }
        return this.f17785g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long r() {
        return this.f17779a.getTimeInMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month s(int i8) {
        Calendar f5 = p.f(this.f17779a);
        f5.add(2, i8);
        return new Month(f5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int t(Month month) {
        if (this.f17779a instanceof GregorianCalendar) {
            return ((month.f17781c - this.f17781c) * 12) + (month.f17780b - this.f17780b);
        }
        throw new IllegalArgumentException("Only Gregorian calendars are supported.");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f17781c);
        parcel.writeInt(this.f17780b);
    }
}

package com.google.android.material.timepicker;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TimeModel implements Parcelable {
    public static final Parcelable.Creator<TimeModel> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private final com.google.android.material.timepicker.a f18725a;

    /* renamed from: b  reason: collision with root package name */
    private final com.google.android.material.timepicker.a f18726b;

    /* renamed from: c  reason: collision with root package name */
    final int f18727c;

    /* renamed from: d  reason: collision with root package name */
    int f18728d;

    /* renamed from: e  reason: collision with root package name */
    int f18729e;

    /* renamed from: f  reason: collision with root package name */
    int f18730f;

    /* renamed from: g  reason: collision with root package name */
    int f18731g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Parcelable.Creator<TimeModel> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public TimeModel createFromParcel(Parcel parcel) {
            return new TimeModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public TimeModel[] newArray(int i8) {
            return new TimeModel[i8];
        }
    }

    public TimeModel() {
        this(0);
    }

    public TimeModel(int i8) {
        this(0, 0, 10, i8);
    }

    public TimeModel(int i8, int i9, int i10, int i11) {
        this.f18728d = i8;
        this.f18729e = i9;
        this.f18730f = i10;
        this.f18727c = i11;
        this.f18731g = c(i8);
        this.f18725a = new com.google.android.material.timepicker.a(59);
        this.f18726b = new com.google.android.material.timepicker.a(i11 == 1 ? 24 : 12);
    }

    protected TimeModel(Parcel parcel) {
        this(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
    }

    public static String a(Resources resources, CharSequence charSequence) {
        return b(resources, charSequence, "%02d");
    }

    public static String b(Resources resources, CharSequence charSequence, String str) {
        return String.format(resources.getConfiguration().locale, str, Integer.valueOf(Integer.parseInt(String.valueOf(charSequence))));
    }

    private static int c(int i8) {
        return i8 >= 12 ? 1 : 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TimeModel) {
            TimeModel timeModel = (TimeModel) obj;
            return this.f18728d == timeModel.f18728d && this.f18729e == timeModel.f18729e && this.f18727c == timeModel.f18727c && this.f18730f == timeModel.f18730f;
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.f18727c), Integer.valueOf(this.f18728d), Integer.valueOf(this.f18729e), Integer.valueOf(this.f18730f)});
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f18728d);
        parcel.writeInt(this.f18729e);
        parcel.writeInt(this.f18730f);
        parcel.writeInt(this.f18727c);
    }
}

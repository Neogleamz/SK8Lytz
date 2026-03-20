package com.google.android.exoplayer2.offline;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class StreamKey implements Comparable<StreamKey>, Parcelable {
    public static final Parcelable.Creator<StreamKey> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final int f10209a;

    /* renamed from: b  reason: collision with root package name */
    public final int f10210b;

    /* renamed from: c  reason: collision with root package name */
    public final int f10211c;
    @Deprecated

    /* renamed from: d  reason: collision with root package name */
    public final int f10212d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<StreamKey> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public StreamKey createFromParcel(Parcel parcel) {
            return new StreamKey(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public StreamKey[] newArray(int i8) {
            return new StreamKey[i8];
        }
    }

    public StreamKey(int i8, int i9, int i10) {
        this.f10209a = i8;
        this.f10210b = i9;
        this.f10211c = i10;
        this.f10212d = i10;
    }

    StreamKey(Parcel parcel) {
        this.f10209a = parcel.readInt();
        this.f10210b = parcel.readInt();
        int readInt = parcel.readInt();
        this.f10211c = readInt;
        this.f10212d = readInt;
    }

    @Override // java.lang.Comparable
    /* renamed from: c */
    public int compareTo(StreamKey streamKey) {
        int i8 = this.f10209a - streamKey.f10209a;
        if (i8 == 0) {
            int i9 = this.f10210b - streamKey.f10210b;
            return i9 == 0 ? this.f10211c - streamKey.f10211c : i9;
        }
        return i8;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || StreamKey.class != obj.getClass()) {
            return false;
        }
        StreamKey streamKey = (StreamKey) obj;
        return this.f10209a == streamKey.f10209a && this.f10210b == streamKey.f10210b && this.f10211c == streamKey.f10211c;
    }

    public int hashCode() {
        return (((this.f10209a * 31) + this.f10210b) * 31) + this.f10211c;
    }

    public String toString() {
        return this.f10209a + "." + this.f10210b + "." + this.f10211c;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f10209a);
        parcel.writeInt(this.f10210b);
        parcel.writeInt(this.f10211c);
    }
}

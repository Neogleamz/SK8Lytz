package com.google.android.exoplayer2.metadata.icy;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.a1;
import com.google.android.exoplayer2.metadata.Metadata;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class IcyInfo implements Metadata.Entry {
    public static final Parcelable.Creator<IcyInfo> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final byte[] f10083a;

    /* renamed from: b  reason: collision with root package name */
    public final String f10084b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10085c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<IcyInfo> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public IcyInfo createFromParcel(Parcel parcel) {
            return new IcyInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public IcyInfo[] newArray(int i8) {
            return new IcyInfo[i8];
        }
    }

    IcyInfo(Parcel parcel) {
        this.f10083a = (byte[]) b6.a.e(parcel.createByteArray());
        this.f10084b = parcel.readString();
        this.f10085c = parcel.readString();
    }

    public IcyInfo(byte[] bArr, String str, String str2) {
        this.f10083a = bArr;
        this.f10084b = str;
        this.f10085c = str2;
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public void C(a1.b bVar) {
        String str = this.f10084b;
        if (str != null) {
            bVar.m0(str);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || IcyInfo.class != obj.getClass()) {
            return false;
        }
        return Arrays.equals(this.f10083a, ((IcyInfo) obj).f10083a);
    }

    public int hashCode() {
        return Arrays.hashCode(this.f10083a);
    }

    public String toString() {
        return String.format("ICY: title=\"%s\", url=\"%s\", rawMetadata.length=\"%s\"", this.f10084b, this.f10085c, Integer.valueOf(this.f10083a.length));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeByteArray(this.f10083a);
        parcel.writeString(this.f10084b);
        parcel.writeString(this.f10085c);
    }
}

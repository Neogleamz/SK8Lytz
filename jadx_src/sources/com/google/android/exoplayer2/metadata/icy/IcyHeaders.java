package com.google.android.exoplayer2.metadata.icy;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import com.google.android.exoplayer2.a1;
import com.google.android.exoplayer2.metadata.Metadata;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class IcyHeaders implements Metadata.Entry {
    public static final Parcelable.Creator<IcyHeaders> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final int f10077a;

    /* renamed from: b  reason: collision with root package name */
    public final String f10078b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10079c;

    /* renamed from: d  reason: collision with root package name */
    public final String f10080d;

    /* renamed from: e  reason: collision with root package name */
    public final boolean f10081e;

    /* renamed from: f  reason: collision with root package name */
    public final int f10082f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<IcyHeaders> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public IcyHeaders createFromParcel(Parcel parcel) {
            return new IcyHeaders(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public IcyHeaders[] newArray(int i8) {
            return new IcyHeaders[i8];
        }
    }

    public IcyHeaders(int i8, String str, String str2, String str3, boolean z4, int i9) {
        b6.a.a(i9 == -1 || i9 > 0);
        this.f10077a = i8;
        this.f10078b = str;
        this.f10079c = str2;
        this.f10080d = str3;
        this.f10081e = z4;
        this.f10082f = i9;
    }

    IcyHeaders(Parcel parcel) {
        this.f10077a = parcel.readInt();
        this.f10078b = parcel.readString();
        this.f10079c = parcel.readString();
        this.f10080d = parcel.readString();
        this.f10081e = l0.M0(parcel);
        this.f10082f = parcel.readInt();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.google.android.exoplayer2.metadata.icy.IcyHeaders a(java.util.Map<java.lang.String, java.util.List<java.lang.String>> r13) {
        /*
            Method dump skipped, instructions count: 247
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.metadata.icy.IcyHeaders.a(java.util.Map):com.google.android.exoplayer2.metadata.icy.IcyHeaders");
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public void C(a1.b bVar) {
        String str = this.f10079c;
        if (str != null) {
            bVar.k0(str);
        }
        String str2 = this.f10078b;
        if (str2 != null) {
            bVar.Z(str2);
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
        if (obj == null || IcyHeaders.class != obj.getClass()) {
            return false;
        }
        IcyHeaders icyHeaders = (IcyHeaders) obj;
        return this.f10077a == icyHeaders.f10077a && l0.c(this.f10078b, icyHeaders.f10078b) && l0.c(this.f10079c, icyHeaders.f10079c) && l0.c(this.f10080d, icyHeaders.f10080d) && this.f10081e == icyHeaders.f10081e && this.f10082f == icyHeaders.f10082f;
    }

    public int hashCode() {
        int i8 = (527 + this.f10077a) * 31;
        String str = this.f10078b;
        int hashCode = (i8 + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.f10079c;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.f10080d;
        return ((((hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31) + (this.f10081e ? 1 : 0)) * 31) + this.f10082f;
    }

    public String toString() {
        return "IcyHeaders: name=\"" + this.f10079c + "\", genre=\"" + this.f10078b + "\", bitrate=" + this.f10077a + ", metadataInterval=" + this.f10082f;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f10077a);
        parcel.writeString(this.f10078b);
        parcel.writeString(this.f10079c);
        parcel.writeString(this.f10080d);
        l0.b1(parcel, this.f10081e);
        parcel.writeInt(this.f10082f);
    }
}

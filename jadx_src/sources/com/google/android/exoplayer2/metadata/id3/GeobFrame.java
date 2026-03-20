package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class GeobFrame extends Id3Frame {
    public static final Parcelable.Creator<GeobFrame> CREATOR = new a();

    /* renamed from: b  reason: collision with root package name */
    public final String f10105b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10106c;

    /* renamed from: d  reason: collision with root package name */
    public final String f10107d;

    /* renamed from: e  reason: collision with root package name */
    public final byte[] f10108e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<GeobFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public GeobFrame createFromParcel(Parcel parcel) {
            return new GeobFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public GeobFrame[] newArray(int i8) {
            return new GeobFrame[i8];
        }
    }

    GeobFrame(Parcel parcel) {
        super("GEOB");
        this.f10105b = (String) l0.j(parcel.readString());
        this.f10106c = (String) l0.j(parcel.readString());
        this.f10107d = (String) l0.j(parcel.readString());
        this.f10108e = (byte[]) l0.j(parcel.createByteArray());
    }

    public GeobFrame(String str, String str2, String str3, byte[] bArr) {
        super("GEOB");
        this.f10105b = str;
        this.f10106c = str2;
        this.f10107d = str3;
        this.f10108e = bArr;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || GeobFrame.class != obj.getClass()) {
            return false;
        }
        GeobFrame geobFrame = (GeobFrame) obj;
        return l0.c(this.f10105b, geobFrame.f10105b) && l0.c(this.f10106c, geobFrame.f10106c) && l0.c(this.f10107d, geobFrame.f10107d) && Arrays.equals(this.f10108e, geobFrame.f10108e);
    }

    public int hashCode() {
        String str = this.f10105b;
        int hashCode = (527 + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.f10106c;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.f10107d;
        return ((hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31) + Arrays.hashCode(this.f10108e);
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame
    public String toString() {
        return this.f10109a + ": mimeType=" + this.f10105b + ", filename=" + this.f10106c + ", description=" + this.f10107d;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10105b);
        parcel.writeString(this.f10106c);
        parcel.writeString(this.f10107d);
        parcel.writeByteArray(this.f10108e);
    }
}

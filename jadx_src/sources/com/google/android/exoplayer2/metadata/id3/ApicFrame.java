package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import com.google.android.exoplayer2.a1;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ApicFrame extends Id3Frame {
    public static final Parcelable.Creator<ApicFrame> CREATOR = new a();

    /* renamed from: b  reason: collision with root package name */
    public final String f10086b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10087c;

    /* renamed from: d  reason: collision with root package name */
    public final int f10088d;

    /* renamed from: e  reason: collision with root package name */
    public final byte[] f10089e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<ApicFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public ApicFrame createFromParcel(Parcel parcel) {
            return new ApicFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public ApicFrame[] newArray(int i8) {
            return new ApicFrame[i8];
        }
    }

    ApicFrame(Parcel parcel) {
        super("APIC");
        this.f10086b = (String) l0.j(parcel.readString());
        this.f10087c = parcel.readString();
        this.f10088d = parcel.readInt();
        this.f10089e = (byte[]) l0.j(parcel.createByteArray());
    }

    public ApicFrame(String str, String str2, int i8, byte[] bArr) {
        super("APIC");
        this.f10086b = str;
        this.f10087c = str2;
        this.f10088d = i8;
        this.f10089e = bArr;
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public void C(a1.b bVar) {
        bVar.I(this.f10089e, this.f10088d);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ApicFrame.class != obj.getClass()) {
            return false;
        }
        ApicFrame apicFrame = (ApicFrame) obj;
        return this.f10088d == apicFrame.f10088d && l0.c(this.f10086b, apicFrame.f10086b) && l0.c(this.f10087c, apicFrame.f10087c) && Arrays.equals(this.f10089e, apicFrame.f10089e);
    }

    public int hashCode() {
        int i8 = (527 + this.f10088d) * 31;
        String str = this.f10086b;
        int hashCode = (i8 + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.f10087c;
        return ((hashCode + (str2 != null ? str2.hashCode() : 0)) * 31) + Arrays.hashCode(this.f10089e);
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame
    public String toString() {
        return this.f10109a + ": mimeType=" + this.f10086b + ", description=" + this.f10087c;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10086b);
        parcel.writeString(this.f10087c);
        parcel.writeInt(this.f10088d);
        parcel.writeByteArray(this.f10089e);
    }
}

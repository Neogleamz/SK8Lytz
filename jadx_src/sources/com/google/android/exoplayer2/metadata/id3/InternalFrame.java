package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class InternalFrame extends Id3Frame {
    public static final Parcelable.Creator<InternalFrame> CREATOR = new a();

    /* renamed from: b  reason: collision with root package name */
    public final String f10110b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10111c;

    /* renamed from: d  reason: collision with root package name */
    public final String f10112d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<InternalFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public InternalFrame createFromParcel(Parcel parcel) {
            return new InternalFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public InternalFrame[] newArray(int i8) {
            return new InternalFrame[i8];
        }
    }

    InternalFrame(Parcel parcel) {
        super("----");
        this.f10110b = (String) l0.j(parcel.readString());
        this.f10111c = (String) l0.j(parcel.readString());
        this.f10112d = (String) l0.j(parcel.readString());
    }

    public InternalFrame(String str, String str2, String str3) {
        super("----");
        this.f10110b = str;
        this.f10111c = str2;
        this.f10112d = str3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || InternalFrame.class != obj.getClass()) {
            return false;
        }
        InternalFrame internalFrame = (InternalFrame) obj;
        return l0.c(this.f10111c, internalFrame.f10111c) && l0.c(this.f10110b, internalFrame.f10110b) && l0.c(this.f10112d, internalFrame.f10112d);
    }

    public int hashCode() {
        String str = this.f10110b;
        int hashCode = (527 + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.f10111c;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.f10112d;
        return hashCode2 + (str3 != null ? str3.hashCode() : 0);
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame
    public String toString() {
        return this.f10109a + ": domain=" + this.f10110b + ", description=" + this.f10111c;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10109a);
        parcel.writeString(this.f10110b);
        parcel.writeString(this.f10112d);
    }
}

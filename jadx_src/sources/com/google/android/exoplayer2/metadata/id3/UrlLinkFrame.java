package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class UrlLinkFrame extends Id3Frame {
    public static final Parcelable.Creator<UrlLinkFrame> CREATOR = new a();

    /* renamed from: b  reason: collision with root package name */
    public final String f10123b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10124c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<UrlLinkFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public UrlLinkFrame createFromParcel(Parcel parcel) {
            return new UrlLinkFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public UrlLinkFrame[] newArray(int i8) {
            return new UrlLinkFrame[i8];
        }
    }

    UrlLinkFrame(Parcel parcel) {
        super((String) l0.j(parcel.readString()));
        this.f10123b = parcel.readString();
        this.f10124c = (String) l0.j(parcel.readString());
    }

    public UrlLinkFrame(String str, String str2, String str3) {
        super(str);
        this.f10123b = str2;
        this.f10124c = str3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || UrlLinkFrame.class != obj.getClass()) {
            return false;
        }
        UrlLinkFrame urlLinkFrame = (UrlLinkFrame) obj;
        return this.f10109a.equals(urlLinkFrame.f10109a) && l0.c(this.f10123b, urlLinkFrame.f10123b) && l0.c(this.f10124c, urlLinkFrame.f10124c);
    }

    public int hashCode() {
        int hashCode = (527 + this.f10109a.hashCode()) * 31;
        String str = this.f10123b;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.f10124c;
        return hashCode2 + (str2 != null ? str2.hashCode() : 0);
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame
    public String toString() {
        return this.f10109a + ": url=" + this.f10124c;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10109a);
        parcel.writeString(this.f10123b);
        parcel.writeString(this.f10124c);
    }
}

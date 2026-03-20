package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CommentFrame extends Id3Frame {
    public static final Parcelable.Creator<CommentFrame> CREATOR = new a();

    /* renamed from: b  reason: collision with root package name */
    public final String f10102b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10103c;

    /* renamed from: d  reason: collision with root package name */
    public final String f10104d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<CommentFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public CommentFrame createFromParcel(Parcel parcel) {
            return new CommentFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public CommentFrame[] newArray(int i8) {
            return new CommentFrame[i8];
        }
    }

    CommentFrame(Parcel parcel) {
        super("COMM");
        this.f10102b = (String) l0.j(parcel.readString());
        this.f10103c = (String) l0.j(parcel.readString());
        this.f10104d = (String) l0.j(parcel.readString());
    }

    public CommentFrame(String str, String str2, String str3) {
        super("COMM");
        this.f10102b = str;
        this.f10103c = str2;
        this.f10104d = str3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || CommentFrame.class != obj.getClass()) {
            return false;
        }
        CommentFrame commentFrame = (CommentFrame) obj;
        return l0.c(this.f10103c, commentFrame.f10103c) && l0.c(this.f10102b, commentFrame.f10102b) && l0.c(this.f10104d, commentFrame.f10104d);
    }

    public int hashCode() {
        String str = this.f10102b;
        int hashCode = (527 + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.f10103c;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.f10104d;
        return hashCode2 + (str3 != null ? str3.hashCode() : 0);
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame
    public String toString() {
        return this.f10109a + ": language=" + this.f10102b + ", description=" + this.f10103c;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10109a);
        parcel.writeString(this.f10102b);
        parcel.writeString(this.f10104d);
    }
}

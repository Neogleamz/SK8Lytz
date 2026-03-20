package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ChapterTocFrame extends Id3Frame {
    public static final Parcelable.Creator<ChapterTocFrame> CREATOR = new a();

    /* renamed from: b  reason: collision with root package name */
    public final String f10097b;

    /* renamed from: c  reason: collision with root package name */
    public final boolean f10098c;

    /* renamed from: d  reason: collision with root package name */
    public final boolean f10099d;

    /* renamed from: e  reason: collision with root package name */
    public final String[] f10100e;

    /* renamed from: f  reason: collision with root package name */
    private final Id3Frame[] f10101f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<ChapterTocFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public ChapterTocFrame createFromParcel(Parcel parcel) {
            return new ChapterTocFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public ChapterTocFrame[] newArray(int i8) {
            return new ChapterTocFrame[i8];
        }
    }

    ChapterTocFrame(Parcel parcel) {
        super("CTOC");
        this.f10097b = (String) l0.j(parcel.readString());
        this.f10098c = parcel.readByte() != 0;
        this.f10099d = parcel.readByte() != 0;
        this.f10100e = (String[]) l0.j(parcel.createStringArray());
        int readInt = parcel.readInt();
        this.f10101f = new Id3Frame[readInt];
        for (int i8 = 0; i8 < readInt; i8++) {
            this.f10101f[i8] = (Id3Frame) parcel.readParcelable(Id3Frame.class.getClassLoader());
        }
    }

    public ChapterTocFrame(String str, boolean z4, boolean z8, String[] strArr, Id3Frame[] id3FrameArr) {
        super("CTOC");
        this.f10097b = str;
        this.f10098c = z4;
        this.f10099d = z8;
        this.f10100e = strArr;
        this.f10101f = id3FrameArr;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ChapterTocFrame.class != obj.getClass()) {
            return false;
        }
        ChapterTocFrame chapterTocFrame = (ChapterTocFrame) obj;
        return this.f10098c == chapterTocFrame.f10098c && this.f10099d == chapterTocFrame.f10099d && l0.c(this.f10097b, chapterTocFrame.f10097b) && Arrays.equals(this.f10100e, chapterTocFrame.f10100e) && Arrays.equals(this.f10101f, chapterTocFrame.f10101f);
    }

    public int hashCode() {
        int i8 = (((527 + (this.f10098c ? 1 : 0)) * 31) + (this.f10099d ? 1 : 0)) * 31;
        String str = this.f10097b;
        return i8 + (str != null ? str.hashCode() : 0);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10097b);
        parcel.writeByte(this.f10098c ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.f10099d ? (byte) 1 : (byte) 0);
        parcel.writeStringArray(this.f10100e);
        parcel.writeInt(this.f10101f.length);
        for (Id3Frame id3Frame : this.f10101f) {
            parcel.writeParcelable(id3Frame, 0);
        }
    }
}

package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ChapterFrame extends Id3Frame {
    public static final Parcelable.Creator<ChapterFrame> CREATOR = new a();

    /* renamed from: b  reason: collision with root package name */
    public final String f10091b;

    /* renamed from: c  reason: collision with root package name */
    public final int f10092c;

    /* renamed from: d  reason: collision with root package name */
    public final int f10093d;

    /* renamed from: e  reason: collision with root package name */
    public final long f10094e;

    /* renamed from: f  reason: collision with root package name */
    public final long f10095f;

    /* renamed from: g  reason: collision with root package name */
    private final Id3Frame[] f10096g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<ChapterFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public ChapterFrame createFromParcel(Parcel parcel) {
            return new ChapterFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public ChapterFrame[] newArray(int i8) {
            return new ChapterFrame[i8];
        }
    }

    ChapterFrame(Parcel parcel) {
        super("CHAP");
        this.f10091b = (String) l0.j(parcel.readString());
        this.f10092c = parcel.readInt();
        this.f10093d = parcel.readInt();
        this.f10094e = parcel.readLong();
        this.f10095f = parcel.readLong();
        int readInt = parcel.readInt();
        this.f10096g = new Id3Frame[readInt];
        for (int i8 = 0; i8 < readInt; i8++) {
            this.f10096g[i8] = (Id3Frame) parcel.readParcelable(Id3Frame.class.getClassLoader());
        }
    }

    public ChapterFrame(String str, int i8, int i9, long j8, long j9, Id3Frame[] id3FrameArr) {
        super("CHAP");
        this.f10091b = str;
        this.f10092c = i8;
        this.f10093d = i9;
        this.f10094e = j8;
        this.f10095f = j9;
        this.f10096g = id3FrameArr;
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ChapterFrame.class != obj.getClass()) {
            return false;
        }
        ChapterFrame chapterFrame = (ChapterFrame) obj;
        return this.f10092c == chapterFrame.f10092c && this.f10093d == chapterFrame.f10093d && this.f10094e == chapterFrame.f10094e && this.f10095f == chapterFrame.f10095f && l0.c(this.f10091b, chapterFrame.f10091b) && Arrays.equals(this.f10096g, chapterFrame.f10096g);
    }

    public int hashCode() {
        int i8 = (((((((527 + this.f10092c) * 31) + this.f10093d) * 31) + ((int) this.f10094e)) * 31) + ((int) this.f10095f)) * 31;
        String str = this.f10091b;
        return i8 + (str != null ? str.hashCode() : 0);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10091b);
        parcel.writeInt(this.f10092c);
        parcel.writeInt(this.f10093d);
        parcel.writeLong(this.f10094e);
        parcel.writeLong(this.f10095f);
        parcel.writeInt(this.f10096g.length);
        for (Id3Frame id3Frame : this.f10096g) {
            parcel.writeParcelable(id3Frame, 0);
        }
    }
}

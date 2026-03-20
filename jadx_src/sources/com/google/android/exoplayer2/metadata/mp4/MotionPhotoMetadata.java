package com.google.android.exoplayer2.metadata.mp4;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.common.primitives.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MotionPhotoMetadata implements Metadata.Entry {
    public static final Parcelable.Creator<MotionPhotoMetadata> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final long f10129a;

    /* renamed from: b  reason: collision with root package name */
    public final long f10130b;

    /* renamed from: c  reason: collision with root package name */
    public final long f10131c;

    /* renamed from: d  reason: collision with root package name */
    public final long f10132d;

    /* renamed from: e  reason: collision with root package name */
    public final long f10133e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<MotionPhotoMetadata> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public MotionPhotoMetadata createFromParcel(Parcel parcel) {
            return new MotionPhotoMetadata(parcel, null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public MotionPhotoMetadata[] newArray(int i8) {
            return new MotionPhotoMetadata[i8];
        }
    }

    public MotionPhotoMetadata(long j8, long j9, long j10, long j11, long j12) {
        this.f10129a = j8;
        this.f10130b = j9;
        this.f10131c = j10;
        this.f10132d = j11;
        this.f10133e = j12;
    }

    private MotionPhotoMetadata(Parcel parcel) {
        this.f10129a = parcel.readLong();
        this.f10130b = parcel.readLong();
        this.f10131c = parcel.readLong();
        this.f10132d = parcel.readLong();
        this.f10133e = parcel.readLong();
    }

    /* synthetic */ MotionPhotoMetadata(Parcel parcel, a aVar) {
        this(parcel);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || MotionPhotoMetadata.class != obj.getClass()) {
            return false;
        }
        MotionPhotoMetadata motionPhotoMetadata = (MotionPhotoMetadata) obj;
        return this.f10129a == motionPhotoMetadata.f10129a && this.f10130b == motionPhotoMetadata.f10130b && this.f10131c == motionPhotoMetadata.f10131c && this.f10132d == motionPhotoMetadata.f10132d && this.f10133e == motionPhotoMetadata.f10133e;
    }

    public int hashCode() {
        return ((((((((527 + i.b(this.f10129a)) * 31) + i.b(this.f10130b)) * 31) + i.b(this.f10131c)) * 31) + i.b(this.f10132d)) * 31) + i.b(this.f10133e);
    }

    public String toString() {
        return "Motion photo metadata: photoStartPosition=" + this.f10129a + ", photoSize=" + this.f10130b + ", photoPresentationTimestampUs=" + this.f10131c + ", videoStartPosition=" + this.f10132d + ", videoSize=" + this.f10133e;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeLong(this.f10129a);
        parcel.writeLong(this.f10130b);
        parcel.writeLong(this.f10131c);
        parcel.writeLong(this.f10132d);
        parcel.writeLong(this.f10133e);
    }
}

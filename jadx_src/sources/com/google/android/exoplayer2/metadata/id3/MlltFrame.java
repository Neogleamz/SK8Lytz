package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MlltFrame extends Id3Frame {
    public static final Parcelable.Creator<MlltFrame> CREATOR = new a();

    /* renamed from: b  reason: collision with root package name */
    public final int f10113b;

    /* renamed from: c  reason: collision with root package name */
    public final int f10114c;

    /* renamed from: d  reason: collision with root package name */
    public final int f10115d;

    /* renamed from: e  reason: collision with root package name */
    public final int[] f10116e;

    /* renamed from: f  reason: collision with root package name */
    public final int[] f10117f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<MlltFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public MlltFrame createFromParcel(Parcel parcel) {
            return new MlltFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public MlltFrame[] newArray(int i8) {
            return new MlltFrame[i8];
        }
    }

    public MlltFrame(int i8, int i9, int i10, int[] iArr, int[] iArr2) {
        super("MLLT");
        this.f10113b = i8;
        this.f10114c = i9;
        this.f10115d = i10;
        this.f10116e = iArr;
        this.f10117f = iArr2;
    }

    MlltFrame(Parcel parcel) {
        super("MLLT");
        this.f10113b = parcel.readInt();
        this.f10114c = parcel.readInt();
        this.f10115d = parcel.readInt();
        this.f10116e = (int[]) l0.j(parcel.createIntArray());
        this.f10117f = (int[]) l0.j(parcel.createIntArray());
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || MlltFrame.class != obj.getClass()) {
            return false;
        }
        MlltFrame mlltFrame = (MlltFrame) obj;
        return this.f10113b == mlltFrame.f10113b && this.f10114c == mlltFrame.f10114c && this.f10115d == mlltFrame.f10115d && Arrays.equals(this.f10116e, mlltFrame.f10116e) && Arrays.equals(this.f10117f, mlltFrame.f10117f);
    }

    public int hashCode() {
        return ((((((((527 + this.f10113b) * 31) + this.f10114c) * 31) + this.f10115d) * 31) + Arrays.hashCode(this.f10116e)) * 31) + Arrays.hashCode(this.f10117f);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f10113b);
        parcel.writeInt(this.f10114c);
        parcel.writeInt(this.f10115d);
        parcel.writeIntArray(this.f10116e);
        parcel.writeIntArray(this.f10117f);
    }
}

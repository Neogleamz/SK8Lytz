package com.google.android.exoplayer2.metadata.flac;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import b6.z;
import com.google.android.exoplayer2.a1;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.common.base.e;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class PictureFrame implements Metadata.Entry {
    public static final Parcelable.Creator<PictureFrame> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final int f10067a;

    /* renamed from: b  reason: collision with root package name */
    public final String f10068b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10069c;

    /* renamed from: d  reason: collision with root package name */
    public final int f10070d;

    /* renamed from: e  reason: collision with root package name */
    public final int f10071e;

    /* renamed from: f  reason: collision with root package name */
    public final int f10072f;

    /* renamed from: g  reason: collision with root package name */
    public final int f10073g;

    /* renamed from: h  reason: collision with root package name */
    public final byte[] f10074h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<PictureFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public PictureFrame createFromParcel(Parcel parcel) {
            return new PictureFrame(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public PictureFrame[] newArray(int i8) {
            return new PictureFrame[i8];
        }
    }

    public PictureFrame(int i8, String str, String str2, int i9, int i10, int i11, int i12, byte[] bArr) {
        this.f10067a = i8;
        this.f10068b = str;
        this.f10069c = str2;
        this.f10070d = i9;
        this.f10071e = i10;
        this.f10072f = i11;
        this.f10073g = i12;
        this.f10074h = bArr;
    }

    PictureFrame(Parcel parcel) {
        this.f10067a = parcel.readInt();
        this.f10068b = (String) l0.j(parcel.readString());
        this.f10069c = (String) l0.j(parcel.readString());
        this.f10070d = parcel.readInt();
        this.f10071e = parcel.readInt();
        this.f10072f = parcel.readInt();
        this.f10073g = parcel.readInt();
        this.f10074h = (byte[]) l0.j(parcel.createByteArray());
    }

    public static PictureFrame a(z zVar) {
        int q = zVar.q();
        String F = zVar.F(zVar.q(), e.f18815a);
        String E = zVar.E(zVar.q());
        int q8 = zVar.q();
        int q9 = zVar.q();
        int q10 = zVar.q();
        int q11 = zVar.q();
        int q12 = zVar.q();
        byte[] bArr = new byte[q12];
        zVar.l(bArr, 0, q12);
        return new PictureFrame(q, F, E, q8, q9, q10, q11, bArr);
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public void C(a1.b bVar) {
        bVar.I(this.f10074h, this.f10067a);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || PictureFrame.class != obj.getClass()) {
            return false;
        }
        PictureFrame pictureFrame = (PictureFrame) obj;
        return this.f10067a == pictureFrame.f10067a && this.f10068b.equals(pictureFrame.f10068b) && this.f10069c.equals(pictureFrame.f10069c) && this.f10070d == pictureFrame.f10070d && this.f10071e == pictureFrame.f10071e && this.f10072f == pictureFrame.f10072f && this.f10073g == pictureFrame.f10073g && Arrays.equals(this.f10074h, pictureFrame.f10074h);
    }

    public int hashCode() {
        return ((((((((((((((527 + this.f10067a) * 31) + this.f10068b.hashCode()) * 31) + this.f10069c.hashCode()) * 31) + this.f10070d) * 31) + this.f10071e) * 31) + this.f10072f) * 31) + this.f10073g) * 31) + Arrays.hashCode(this.f10074h);
    }

    public String toString() {
        return "Picture: mimeType=" + this.f10068b + ", description=" + this.f10069c;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f10067a);
        parcel.writeString(this.f10068b);
        parcel.writeString(this.f10069c);
        parcel.writeInt(this.f10070d);
        parcel.writeInt(this.f10071e);
        parcel.writeInt(this.f10072f);
        parcel.writeInt(this.f10073g);
        parcel.writeByteArray(this.f10074h);
    }
}

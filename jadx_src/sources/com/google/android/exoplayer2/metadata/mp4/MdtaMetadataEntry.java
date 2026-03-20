package com.google.android.exoplayer2.metadata.mp4;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import com.google.android.exoplayer2.metadata.Metadata;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MdtaMetadataEntry implements Metadata.Entry {
    public static final Parcelable.Creator<MdtaMetadataEntry> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final String f10125a;

    /* renamed from: b  reason: collision with root package name */
    public final byte[] f10126b;

    /* renamed from: c  reason: collision with root package name */
    public final int f10127c;

    /* renamed from: d  reason: collision with root package name */
    public final int f10128d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<MdtaMetadataEntry> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public MdtaMetadataEntry createFromParcel(Parcel parcel) {
            return new MdtaMetadataEntry(parcel, null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public MdtaMetadataEntry[] newArray(int i8) {
            return new MdtaMetadataEntry[i8];
        }
    }

    private MdtaMetadataEntry(Parcel parcel) {
        this.f10125a = (String) l0.j(parcel.readString());
        this.f10126b = (byte[]) l0.j(parcel.createByteArray());
        this.f10127c = parcel.readInt();
        this.f10128d = parcel.readInt();
    }

    /* synthetic */ MdtaMetadataEntry(Parcel parcel, a aVar) {
        this(parcel);
    }

    public MdtaMetadataEntry(String str, byte[] bArr, int i8, int i9) {
        this.f10125a = str;
        this.f10126b = bArr;
        this.f10127c = i8;
        this.f10128d = i9;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || MdtaMetadataEntry.class != obj.getClass()) {
            return false;
        }
        MdtaMetadataEntry mdtaMetadataEntry = (MdtaMetadataEntry) obj;
        return this.f10125a.equals(mdtaMetadataEntry.f10125a) && Arrays.equals(this.f10126b, mdtaMetadataEntry.f10126b) && this.f10127c == mdtaMetadataEntry.f10127c && this.f10128d == mdtaMetadataEntry.f10128d;
    }

    public int hashCode() {
        return ((((((527 + this.f10125a.hashCode()) * 31) + Arrays.hashCode(this.f10126b)) * 31) + this.f10127c) * 31) + this.f10128d;
    }

    public String toString() {
        return "mdta: key=" + this.f10125a;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10125a);
        parcel.writeByteArray(this.f10126b);
        parcel.writeInt(this.f10127c);
        parcel.writeInt(this.f10128d);
    }
}

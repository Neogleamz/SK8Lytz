package com.google.android.exoplayer2.metadata.mp4;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.common.primitives.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SmtaMetadataEntry implements Metadata.Entry {
    public static final Parcelable.Creator<SmtaMetadataEntry> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final float f10139a;

    /* renamed from: b  reason: collision with root package name */
    public final int f10140b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<SmtaMetadataEntry> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public SmtaMetadataEntry createFromParcel(Parcel parcel) {
            return new SmtaMetadataEntry(parcel, (a) null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public SmtaMetadataEntry[] newArray(int i8) {
            return new SmtaMetadataEntry[i8];
        }
    }

    public SmtaMetadataEntry(float f5, int i8) {
        this.f10139a = f5;
        this.f10140b = i8;
    }

    private SmtaMetadataEntry(Parcel parcel) {
        this.f10139a = parcel.readFloat();
        this.f10140b = parcel.readInt();
    }

    /* synthetic */ SmtaMetadataEntry(Parcel parcel, a aVar) {
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
        if (obj == null || SmtaMetadataEntry.class != obj.getClass()) {
            return false;
        }
        SmtaMetadataEntry smtaMetadataEntry = (SmtaMetadataEntry) obj;
        return this.f10139a == smtaMetadataEntry.f10139a && this.f10140b == smtaMetadataEntry.f10140b;
    }

    public int hashCode() {
        return ((527 + e.a(this.f10139a)) * 31) + this.f10140b;
    }

    public String toString() {
        return "smta: captureFrameRate=" + this.f10139a + ", svcTemporalLayerCount=" + this.f10140b;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeFloat(this.f10139a);
        parcel.writeInt(this.f10140b);
    }
}

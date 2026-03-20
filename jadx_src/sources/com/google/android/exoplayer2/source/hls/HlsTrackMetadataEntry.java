package com.google.android.exoplayer2.source.hls;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.metadata.Metadata;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HlsTrackMetadataEntry implements Metadata.Entry {
    public static final Parcelable.Creator<HlsTrackMetadataEntry> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final String f10471a;

    /* renamed from: b  reason: collision with root package name */
    public final String f10472b;

    /* renamed from: c  reason: collision with root package name */
    public final List<VariantInfo> f10473c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class VariantInfo implements Parcelable {
        public static final Parcelable.Creator<VariantInfo> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        public final int f10474a;

        /* renamed from: b  reason: collision with root package name */
        public final int f10475b;

        /* renamed from: c  reason: collision with root package name */
        public final String f10476c;

        /* renamed from: d  reason: collision with root package name */
        public final String f10477d;

        /* renamed from: e  reason: collision with root package name */
        public final String f10478e;

        /* renamed from: f  reason: collision with root package name */
        public final String f10479f;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<VariantInfo> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public VariantInfo createFromParcel(Parcel parcel) {
                return new VariantInfo(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public VariantInfo[] newArray(int i8) {
                return new VariantInfo[i8];
            }
        }

        public VariantInfo(int i8, int i9, String str, String str2, String str3, String str4) {
            this.f10474a = i8;
            this.f10475b = i9;
            this.f10476c = str;
            this.f10477d = str2;
            this.f10478e = str3;
            this.f10479f = str4;
        }

        VariantInfo(Parcel parcel) {
            this.f10474a = parcel.readInt();
            this.f10475b = parcel.readInt();
            this.f10476c = parcel.readString();
            this.f10477d = parcel.readString();
            this.f10478e = parcel.readString();
            this.f10479f = parcel.readString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || VariantInfo.class != obj.getClass()) {
                return false;
            }
            VariantInfo variantInfo = (VariantInfo) obj;
            return this.f10474a == variantInfo.f10474a && this.f10475b == variantInfo.f10475b && TextUtils.equals(this.f10476c, variantInfo.f10476c) && TextUtils.equals(this.f10477d, variantInfo.f10477d) && TextUtils.equals(this.f10478e, variantInfo.f10478e) && TextUtils.equals(this.f10479f, variantInfo.f10479f);
        }

        public int hashCode() {
            int i8 = ((this.f10474a * 31) + this.f10475b) * 31;
            String str = this.f10476c;
            int hashCode = (i8 + (str != null ? str.hashCode() : 0)) * 31;
            String str2 = this.f10477d;
            int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
            String str3 = this.f10478e;
            int hashCode3 = (hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31;
            String str4 = this.f10479f;
            return hashCode3 + (str4 != null ? str4.hashCode() : 0);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeInt(this.f10474a);
            parcel.writeInt(this.f10475b);
            parcel.writeString(this.f10476c);
            parcel.writeString(this.f10477d);
            parcel.writeString(this.f10478e);
            parcel.writeString(this.f10479f);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<HlsTrackMetadataEntry> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public HlsTrackMetadataEntry createFromParcel(Parcel parcel) {
            return new HlsTrackMetadataEntry(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public HlsTrackMetadataEntry[] newArray(int i8) {
            return new HlsTrackMetadataEntry[i8];
        }
    }

    HlsTrackMetadataEntry(Parcel parcel) {
        this.f10471a = parcel.readString();
        this.f10472b = parcel.readString();
        int readInt = parcel.readInt();
        ArrayList arrayList = new ArrayList(readInt);
        for (int i8 = 0; i8 < readInt; i8++) {
            arrayList.add((VariantInfo) parcel.readParcelable(VariantInfo.class.getClassLoader()));
        }
        this.f10473c = Collections.unmodifiableList(arrayList);
    }

    public HlsTrackMetadataEntry(String str, String str2, List<VariantInfo> list) {
        this.f10471a = str;
        this.f10472b = str2;
        this.f10473c = Collections.unmodifiableList(new ArrayList(list));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || HlsTrackMetadataEntry.class != obj.getClass()) {
            return false;
        }
        HlsTrackMetadataEntry hlsTrackMetadataEntry = (HlsTrackMetadataEntry) obj;
        return TextUtils.equals(this.f10471a, hlsTrackMetadataEntry.f10471a) && TextUtils.equals(this.f10472b, hlsTrackMetadataEntry.f10472b) && this.f10473c.equals(hlsTrackMetadataEntry.f10473c);
    }

    public int hashCode() {
        String str = this.f10471a;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.f10472b;
        return ((hashCode + (str2 != null ? str2.hashCode() : 0)) * 31) + this.f10473c.hashCode();
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("HlsTrackMetadataEntry");
        if (this.f10471a != null) {
            str = " [" + this.f10471a + ", " + this.f10472b + "]";
        } else {
            str = BuildConfig.FLAVOR;
        }
        sb.append(str);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10471a);
        parcel.writeString(this.f10472b);
        int size = this.f10473c.size();
        parcel.writeInt(size);
        for (int i9 = 0; i9 < size; i9++) {
            parcel.writeParcelable(this.f10473c.get(i9), 0);
        }
    }
}

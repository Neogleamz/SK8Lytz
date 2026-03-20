package com.google.android.exoplayer2.metadata;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.a1;
import com.google.android.exoplayer2.w0;
import com.google.common.primitives.i;
import java.util.Arrays;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Metadata implements Parcelable {
    public static final Parcelable.Creator<Metadata> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private final Entry[] f10049a;

    /* renamed from: b  reason: collision with root package name */
    public final long f10050b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface Entry extends Parcelable {
        default void C(a1.b bVar) {
        }

        default byte[] M0() {
            return null;
        }

        default w0 V() {
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<Metadata> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public Metadata createFromParcel(Parcel parcel) {
            return new Metadata(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public Metadata[] newArray(int i8) {
            return new Metadata[i8];
        }
    }

    public Metadata(long j8, List<? extends Entry> list) {
        this(j8, (Entry[]) list.toArray(new Entry[0]));
    }

    public Metadata(long j8, Entry... entryArr) {
        this.f10050b = j8;
        this.f10049a = entryArr;
    }

    Metadata(Parcel parcel) {
        this.f10049a = new Entry[parcel.readInt()];
        int i8 = 0;
        while (true) {
            Entry[] entryArr = this.f10049a;
            if (i8 >= entryArr.length) {
                this.f10050b = parcel.readLong();
                return;
            } else {
                entryArr[i8] = (Entry) parcel.readParcelable(Entry.class.getClassLoader());
                i8++;
            }
        }
    }

    public Metadata(List<? extends Entry> list) {
        this((Entry[]) list.toArray(new Entry[0]));
    }

    public Metadata(Entry... entryArr) {
        this(-9223372036854775807L, entryArr);
    }

    public Metadata a(Entry... entryArr) {
        return entryArr.length == 0 ? this : new Metadata(this.f10050b, (Entry[]) l0.G0(this.f10049a, entryArr));
    }

    public Metadata b(Metadata metadata) {
        return metadata == null ? this : a(metadata.f10049a);
    }

    public Metadata c(long j8) {
        return this.f10050b == j8 ? this : new Metadata(j8, this.f10049a);
    }

    public Entry d(int i8) {
        return this.f10049a[i8];
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int e() {
        return this.f10049a.length;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || Metadata.class != obj.getClass()) {
            return false;
        }
        Metadata metadata = (Metadata) obj;
        return Arrays.equals(this.f10049a, metadata.f10049a) && this.f10050b == metadata.f10050b;
    }

    public int hashCode() {
        return (Arrays.hashCode(this.f10049a) * 31) + i.b(this.f10050b);
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("entries=");
        sb.append(Arrays.toString(this.f10049a));
        if (this.f10050b == -9223372036854775807L) {
            str = BuildConfig.FLAVOR;
        } else {
            str = ", presentationTimeUs=" + this.f10050b;
        }
        sb.append(str);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f10049a.length);
        for (Entry entry : this.f10049a) {
            parcel.writeParcelable(entry, 0);
        }
        parcel.writeLong(this.f10050b);
    }
}

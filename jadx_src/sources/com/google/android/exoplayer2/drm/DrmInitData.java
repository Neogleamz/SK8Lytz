package com.google.android.exoplayer2.drm;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import b6.l0;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class DrmInitData implements Comparator<SchemeData>, Parcelable {
    public static final Parcelable.Creator<DrmInitData> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private final SchemeData[] f9592a;

    /* renamed from: b  reason: collision with root package name */
    private int f9593b;

    /* renamed from: c  reason: collision with root package name */
    public final String f9594c;

    /* renamed from: d  reason: collision with root package name */
    public final int f9595d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class SchemeData implements Parcelable {
        public static final Parcelable.Creator<SchemeData> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        private int f9596a;

        /* renamed from: b  reason: collision with root package name */
        public final UUID f9597b;

        /* renamed from: c  reason: collision with root package name */
        public final String f9598c;

        /* renamed from: d  reason: collision with root package name */
        public final String f9599d;

        /* renamed from: e  reason: collision with root package name */
        public final byte[] f9600e;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<SchemeData> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SchemeData createFromParcel(Parcel parcel) {
                return new SchemeData(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public SchemeData[] newArray(int i8) {
                return new SchemeData[i8];
            }
        }

        SchemeData(Parcel parcel) {
            this.f9597b = new UUID(parcel.readLong(), parcel.readLong());
            this.f9598c = parcel.readString();
            this.f9599d = (String) l0.j(parcel.readString());
            this.f9600e = parcel.createByteArray();
        }

        public SchemeData(UUID uuid, String str, String str2, byte[] bArr) {
            this.f9597b = (UUID) b6.a.e(uuid);
            this.f9598c = str;
            this.f9599d = (String) b6.a.e(str2);
            this.f9600e = bArr;
        }

        public SchemeData(UUID uuid, String str, byte[] bArr) {
            this(uuid, null, str, bArr);
        }

        public boolean a(SchemeData schemeData) {
            return c() && !schemeData.c() && d(schemeData.f9597b);
        }

        public SchemeData b(byte[] bArr) {
            return new SchemeData(this.f9597b, this.f9598c, this.f9599d, bArr);
        }

        public boolean c() {
            return this.f9600e != null;
        }

        public boolean d(UUID uuid) {
            return i4.b.f20465a.equals(this.f9597b) || uuid.equals(this.f9597b);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean equals(Object obj) {
            if (obj instanceof SchemeData) {
                if (obj == this) {
                    return true;
                }
                SchemeData schemeData = (SchemeData) obj;
                return l0.c(this.f9598c, schemeData.f9598c) && l0.c(this.f9599d, schemeData.f9599d) && l0.c(this.f9597b, schemeData.f9597b) && Arrays.equals(this.f9600e, schemeData.f9600e);
            }
            return false;
        }

        public int hashCode() {
            if (this.f9596a == 0) {
                int hashCode = this.f9597b.hashCode() * 31;
                String str = this.f9598c;
                this.f9596a = ((((hashCode + (str == null ? 0 : str.hashCode())) * 31) + this.f9599d.hashCode()) * 31) + Arrays.hashCode(this.f9600e);
            }
            return this.f9596a;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeLong(this.f9597b.getMostSignificantBits());
            parcel.writeLong(this.f9597b.getLeastSignificantBits());
            parcel.writeString(this.f9598c);
            parcel.writeString(this.f9599d);
            parcel.writeByteArray(this.f9600e);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<DrmInitData> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public DrmInitData createFromParcel(Parcel parcel) {
            return new DrmInitData(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public DrmInitData[] newArray(int i8) {
            return new DrmInitData[i8];
        }
    }

    DrmInitData(Parcel parcel) {
        this.f9594c = parcel.readString();
        SchemeData[] schemeDataArr = (SchemeData[]) l0.j((SchemeData[]) parcel.createTypedArray(SchemeData.CREATOR));
        this.f9592a = schemeDataArr;
        this.f9595d = schemeDataArr.length;
    }

    public DrmInitData(String str, List<SchemeData> list) {
        this(str, false, (SchemeData[]) list.toArray(new SchemeData[0]));
    }

    private DrmInitData(String str, boolean z4, SchemeData... schemeDataArr) {
        this.f9594c = str;
        schemeDataArr = z4 ? (SchemeData[]) schemeDataArr.clone() : schemeDataArr;
        this.f9592a = schemeDataArr;
        this.f9595d = schemeDataArr.length;
        Arrays.sort(schemeDataArr, this);
    }

    public DrmInitData(String str, SchemeData... schemeDataArr) {
        this(str, true, schemeDataArr);
    }

    public DrmInitData(List<SchemeData> list) {
        this(null, false, (SchemeData[]) list.toArray(new SchemeData[0]));
    }

    public DrmInitData(SchemeData... schemeDataArr) {
        this((String) null, schemeDataArr);
    }

    private static boolean b(ArrayList<SchemeData> arrayList, int i8, UUID uuid) {
        for (int i9 = 0; i9 < i8; i9++) {
            if (arrayList.get(i9).f9597b.equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public static DrmInitData d(DrmInitData drmInitData, DrmInitData drmInitData2) {
        String str;
        SchemeData[] schemeDataArr;
        SchemeData[] schemeDataArr2;
        ArrayList arrayList = new ArrayList();
        if (drmInitData != null) {
            str = drmInitData.f9594c;
            for (SchemeData schemeData : drmInitData.f9592a) {
                if (schemeData.c()) {
                    arrayList.add(schemeData);
                }
            }
        } else {
            str = null;
        }
        if (drmInitData2 != null) {
            if (str == null) {
                str = drmInitData2.f9594c;
            }
            int size = arrayList.size();
            for (SchemeData schemeData2 : drmInitData2.f9592a) {
                if (schemeData2.c() && !b(arrayList, size, schemeData2.f9597b)) {
                    arrayList.add(schemeData2);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new DrmInitData(str, arrayList);
    }

    @Override // java.util.Comparator
    /* renamed from: a */
    public int compare(SchemeData schemeData, SchemeData schemeData2) {
        UUID uuid = i4.b.f20465a;
        return uuid.equals(schemeData.f9597b) ? uuid.equals(schemeData2.f9597b) ? 0 : 1 : schemeData.f9597b.compareTo(schemeData2.f9597b);
    }

    public DrmInitData c(String str) {
        return l0.c(this.f9594c, str) ? this : new DrmInitData(str, false, this.f9592a);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public SchemeData e(int i8) {
        return this.f9592a[i8];
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || DrmInitData.class != obj.getClass()) {
            return false;
        }
        DrmInitData drmInitData = (DrmInitData) obj;
        return l0.c(this.f9594c, drmInitData.f9594c) && Arrays.equals(this.f9592a, drmInitData.f9592a);
    }

    public DrmInitData f(DrmInitData drmInitData) {
        String str;
        String str2 = this.f9594c;
        b6.a.f(str2 == null || (str = drmInitData.f9594c) == null || TextUtils.equals(str2, str));
        String str3 = this.f9594c;
        if (str3 == null) {
            str3 = drmInitData.f9594c;
        }
        return new DrmInitData(str3, (SchemeData[]) l0.G0(this.f9592a, drmInitData.f9592a));
    }

    public int hashCode() {
        if (this.f9593b == 0) {
            String str = this.f9594c;
            this.f9593b = ((str == null ? 0 : str.hashCode()) * 31) + Arrays.hashCode(this.f9592a);
        }
        return this.f9593b;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f9594c);
        parcel.writeTypedArray(this.f9592a, 0);
    }
}

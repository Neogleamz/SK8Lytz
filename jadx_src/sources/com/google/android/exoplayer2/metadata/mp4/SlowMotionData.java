package com.google.android.exoplayer2.metadata.mp4;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.common.base.k;
import com.google.common.collect.b0;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SlowMotionData implements Metadata.Entry {
    public static final Parcelable.Creator<SlowMotionData> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final List<Segment> f10134a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Segment implements Parcelable {

        /* renamed from: a  reason: collision with root package name */
        public final long f10136a;

        /* renamed from: b  reason: collision with root package name */
        public final long f10137b;

        /* renamed from: c  reason: collision with root package name */
        public final int f10138c;

        /* renamed from: d  reason: collision with root package name */
        public static final Comparator<Segment> f10135d = f5.a.a;
        public static final Parcelable.Creator<Segment> CREATOR = new a();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<Segment> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public Segment createFromParcel(Parcel parcel) {
                return new Segment(parcel.readLong(), parcel.readLong(), parcel.readInt());
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public Segment[] newArray(int i8) {
                return new Segment[i8];
            }
        }

        public Segment(long j8, long j9, int i8) {
            b6.a.a(j8 < j9);
            this.f10136a = j8;
            this.f10137b = j9;
            this.f10138c = i8;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int b(Segment segment, Segment segment2) {
            return b0.k().e(segment.f10136a, segment2.f10136a).e(segment.f10137b, segment2.f10137b).d(segment.f10138c, segment2.f10138c).j();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || Segment.class != obj.getClass()) {
                return false;
            }
            Segment segment = (Segment) obj;
            return this.f10136a == segment.f10136a && this.f10137b == segment.f10137b && this.f10138c == segment.f10138c;
        }

        public int hashCode() {
            return k.b(Long.valueOf(this.f10136a), Long.valueOf(this.f10137b), Integer.valueOf(this.f10138c));
        }

        public String toString() {
            return l0.C("Segment: startTimeMs=%d, endTimeMs=%d, speedDivisor=%d", Long.valueOf(this.f10136a), Long.valueOf(this.f10137b), Integer.valueOf(this.f10138c));
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeLong(this.f10136a);
            parcel.writeLong(this.f10137b);
            parcel.writeInt(this.f10138c);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<SlowMotionData> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public SlowMotionData createFromParcel(Parcel parcel) {
            ArrayList arrayList = new ArrayList();
            parcel.readList(arrayList, Segment.class.getClassLoader());
            return new SlowMotionData(arrayList);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public SlowMotionData[] newArray(int i8) {
            return new SlowMotionData[i8];
        }
    }

    public SlowMotionData(List<Segment> list) {
        this.f10134a = list;
        b6.a.a(!a(list));
    }

    private static boolean a(List<Segment> list) {
        if (list.isEmpty()) {
            return false;
        }
        long j8 = list.get(0).f10137b;
        for (int i8 = 1; i8 < list.size(); i8++) {
            if (list.get(i8).f10136a < j8) {
                return true;
            }
            j8 = list.get(i8).f10137b;
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || SlowMotionData.class != obj.getClass()) {
            return false;
        }
        return this.f10134a.equals(((SlowMotionData) obj).f10134a);
    }

    public int hashCode() {
        return this.f10134a.hashCode();
    }

    public String toString() {
        return "SlowMotion: segments=" + this.f10134a;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeList(this.f10134a);
    }
}

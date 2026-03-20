package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import b6.z;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SpliceScheduleCommand extends SpliceCommand {
    public static final Parcelable.Creator<SpliceScheduleCommand> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final List<c> f10160a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Parcelable.Creator<SpliceScheduleCommand> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public SpliceScheduleCommand createFromParcel(Parcel parcel) {
            return new SpliceScheduleCommand(parcel, null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public SpliceScheduleCommand[] newArray(int i8) {
            return new SpliceScheduleCommand[i8];
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f10161a;

        /* renamed from: b  reason: collision with root package name */
        public final long f10162b;

        private b(int i8, long j8) {
            this.f10161a = i8;
            this.f10162b = j8;
        }

        /* synthetic */ b(int i8, long j8, a aVar) {
            this(i8, j8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static b c(Parcel parcel) {
            return new b(parcel.readInt(), parcel.readLong());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void d(Parcel parcel) {
            parcel.writeInt(this.f10161a);
            parcel.writeLong(this.f10162b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        public final long f10163a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f10164b;

        /* renamed from: c  reason: collision with root package name */
        public final boolean f10165c;

        /* renamed from: d  reason: collision with root package name */
        public final boolean f10166d;

        /* renamed from: e  reason: collision with root package name */
        public final long f10167e;

        /* renamed from: f  reason: collision with root package name */
        public final List<b> f10168f;

        /* renamed from: g  reason: collision with root package name */
        public final boolean f10169g;

        /* renamed from: h  reason: collision with root package name */
        public final long f10170h;

        /* renamed from: i  reason: collision with root package name */
        public final int f10171i;

        /* renamed from: j  reason: collision with root package name */
        public final int f10172j;

        /* renamed from: k  reason: collision with root package name */
        public final int f10173k;

        private c(long j8, boolean z4, boolean z8, boolean z9, List<b> list, long j9, boolean z10, long j10, int i8, int i9, int i10) {
            this.f10163a = j8;
            this.f10164b = z4;
            this.f10165c = z8;
            this.f10166d = z9;
            this.f10168f = Collections.unmodifiableList(list);
            this.f10167e = j9;
            this.f10169g = z10;
            this.f10170h = j10;
            this.f10171i = i8;
            this.f10172j = i9;
            this.f10173k = i10;
        }

        private c(Parcel parcel) {
            this.f10163a = parcel.readLong();
            this.f10164b = parcel.readByte() == 1;
            this.f10165c = parcel.readByte() == 1;
            this.f10166d = parcel.readByte() == 1;
            int readInt = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt);
            for (int i8 = 0; i8 < readInt; i8++) {
                arrayList.add(b.c(parcel));
            }
            this.f10168f = Collections.unmodifiableList(arrayList);
            this.f10167e = parcel.readLong();
            this.f10169g = parcel.readByte() == 1;
            this.f10170h = parcel.readLong();
            this.f10171i = parcel.readInt();
            this.f10172j = parcel.readInt();
            this.f10173k = parcel.readInt();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static c d(Parcel parcel) {
            return new c(parcel);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static c e(z zVar) {
            ArrayList arrayList;
            boolean z4;
            long j8;
            boolean z8;
            long j9;
            int i8;
            int i9;
            int i10;
            boolean z9;
            boolean z10;
            long j10;
            long J = zVar.J();
            boolean z11 = (zVar.H() & RecognitionOptions.ITF) != 0;
            ArrayList arrayList2 = new ArrayList();
            if (z11) {
                arrayList = arrayList2;
                z4 = false;
                j8 = -9223372036854775807L;
                z8 = false;
                j9 = -9223372036854775807L;
                i8 = 0;
                i9 = 0;
                i10 = 0;
                z9 = false;
            } else {
                int H = zVar.H();
                boolean z12 = (H & RecognitionOptions.ITF) != 0;
                boolean z13 = (H & 64) != 0;
                boolean z14 = (H & 32) != 0;
                long J2 = z13 ? zVar.J() : -9223372036854775807L;
                if (!z13) {
                    int H2 = zVar.H();
                    ArrayList arrayList3 = new ArrayList(H2);
                    for (int i11 = 0; i11 < H2; i11++) {
                        arrayList3.add(new b(zVar.H(), zVar.J(), null));
                    }
                    arrayList2 = arrayList3;
                }
                if (z14) {
                    long H3 = zVar.H();
                    boolean z15 = (128 & H3) != 0;
                    j10 = ((((H3 & 1) << 32) | zVar.J()) * 1000) / 90;
                    z10 = z15;
                } else {
                    z10 = false;
                    j10 = -9223372036854775807L;
                }
                int N = zVar.N();
                int H4 = zVar.H();
                z9 = z13;
                i10 = zVar.H();
                j9 = j10;
                arrayList = arrayList2;
                long j11 = J2;
                i8 = N;
                i9 = H4;
                j8 = j11;
                boolean z16 = z12;
                z8 = z10;
                z4 = z16;
            }
            return new c(J, z11, z4, z9, arrayList, j8, z8, j9, i8, i9, i10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void f(Parcel parcel) {
            parcel.writeLong(this.f10163a);
            parcel.writeByte(this.f10164b ? (byte) 1 : (byte) 0);
            parcel.writeByte(this.f10165c ? (byte) 1 : (byte) 0);
            parcel.writeByte(this.f10166d ? (byte) 1 : (byte) 0);
            int size = this.f10168f.size();
            parcel.writeInt(size);
            for (int i8 = 0; i8 < size; i8++) {
                this.f10168f.get(i8).d(parcel);
            }
            parcel.writeLong(this.f10167e);
            parcel.writeByte(this.f10169g ? (byte) 1 : (byte) 0);
            parcel.writeLong(this.f10170h);
            parcel.writeInt(this.f10171i);
            parcel.writeInt(this.f10172j);
            parcel.writeInt(this.f10173k);
        }
    }

    private SpliceScheduleCommand(Parcel parcel) {
        int readInt = parcel.readInt();
        ArrayList arrayList = new ArrayList(readInt);
        for (int i8 = 0; i8 < readInt; i8++) {
            arrayList.add(c.d(parcel));
        }
        this.f10160a = Collections.unmodifiableList(arrayList);
    }

    /* synthetic */ SpliceScheduleCommand(Parcel parcel, a aVar) {
        this(parcel);
    }

    private SpliceScheduleCommand(List<c> list) {
        this.f10160a = Collections.unmodifiableList(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpliceScheduleCommand a(z zVar) {
        int H = zVar.H();
        ArrayList arrayList = new ArrayList(H);
        for (int i8 = 0; i8 < H; i8++) {
            arrayList.add(c.e(zVar));
        }
        return new SpliceScheduleCommand(arrayList);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int size = this.f10160a.size();
        parcel.writeInt(size);
        for (int i9 = 0; i9 < size; i9++) {
            this.f10160a.get(i9).f(parcel);
        }
    }
}

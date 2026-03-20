package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import b6.h0;
import b6.z;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SpliceInsertCommand extends SpliceCommand {
    public static final Parcelable.Creator<SpliceInsertCommand> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final long f10144a;

    /* renamed from: b  reason: collision with root package name */
    public final boolean f10145b;

    /* renamed from: c  reason: collision with root package name */
    public final boolean f10146c;

    /* renamed from: d  reason: collision with root package name */
    public final boolean f10147d;

    /* renamed from: e  reason: collision with root package name */
    public final boolean f10148e;

    /* renamed from: f  reason: collision with root package name */
    public final long f10149f;

    /* renamed from: g  reason: collision with root package name */
    public final long f10150g;

    /* renamed from: h  reason: collision with root package name */
    public final List<b> f10151h;

    /* renamed from: j  reason: collision with root package name */
    public final boolean f10152j;

    /* renamed from: k  reason: collision with root package name */
    public final long f10153k;

    /* renamed from: l  reason: collision with root package name */
    public final int f10154l;

    /* renamed from: m  reason: collision with root package name */
    public final int f10155m;

    /* renamed from: n  reason: collision with root package name */
    public final int f10156n;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<SpliceInsertCommand> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public SpliceInsertCommand createFromParcel(Parcel parcel) {
            return new SpliceInsertCommand(parcel, null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public SpliceInsertCommand[] newArray(int i8) {
            return new SpliceInsertCommand[i8];
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f10157a;

        /* renamed from: b  reason: collision with root package name */
        public final long f10158b;

        /* renamed from: c  reason: collision with root package name */
        public final long f10159c;

        private b(int i8, long j8, long j9) {
            this.f10157a = i8;
            this.f10158b = j8;
            this.f10159c = j9;
        }

        /* synthetic */ b(int i8, long j8, long j9, a aVar) {
            this(i8, j8, j9);
        }

        public static b a(Parcel parcel) {
            return new b(parcel.readInt(), parcel.readLong(), parcel.readLong());
        }

        public void b(Parcel parcel) {
            parcel.writeInt(this.f10157a);
            parcel.writeLong(this.f10158b);
            parcel.writeLong(this.f10159c);
        }
    }

    private SpliceInsertCommand(long j8, boolean z4, boolean z8, boolean z9, boolean z10, long j9, long j10, List<b> list, boolean z11, long j11, int i8, int i9, int i10) {
        this.f10144a = j8;
        this.f10145b = z4;
        this.f10146c = z8;
        this.f10147d = z9;
        this.f10148e = z10;
        this.f10149f = j9;
        this.f10150g = j10;
        this.f10151h = Collections.unmodifiableList(list);
        this.f10152j = z11;
        this.f10153k = j11;
        this.f10154l = i8;
        this.f10155m = i9;
        this.f10156n = i10;
    }

    private SpliceInsertCommand(Parcel parcel) {
        this.f10144a = parcel.readLong();
        this.f10145b = parcel.readByte() == 1;
        this.f10146c = parcel.readByte() == 1;
        this.f10147d = parcel.readByte() == 1;
        this.f10148e = parcel.readByte() == 1;
        this.f10149f = parcel.readLong();
        this.f10150g = parcel.readLong();
        int readInt = parcel.readInt();
        ArrayList arrayList = new ArrayList(readInt);
        for (int i8 = 0; i8 < readInt; i8++) {
            arrayList.add(b.a(parcel));
        }
        this.f10151h = Collections.unmodifiableList(arrayList);
        this.f10152j = parcel.readByte() == 1;
        this.f10153k = parcel.readLong();
        this.f10154l = parcel.readInt();
        this.f10155m = parcel.readInt();
        this.f10156n = parcel.readInt();
    }

    /* synthetic */ SpliceInsertCommand(Parcel parcel, a aVar) {
        this(parcel);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpliceInsertCommand a(z zVar, long j8, h0 h0Var) {
        List list;
        boolean z4;
        boolean z8;
        long j9;
        boolean z9;
        long j10;
        int i8;
        int i9;
        int i10;
        boolean z10;
        boolean z11;
        long j11;
        long J = zVar.J();
        boolean z12 = (zVar.H() & RecognitionOptions.ITF) != 0;
        List emptyList = Collections.emptyList();
        if (z12) {
            list = emptyList;
            z4 = false;
            z8 = false;
            j9 = -9223372036854775807L;
            z9 = false;
            j10 = -9223372036854775807L;
            i8 = 0;
            i9 = 0;
            i10 = 0;
            z10 = false;
        } else {
            int H = zVar.H();
            boolean z13 = (H & RecognitionOptions.ITF) != 0;
            boolean z14 = (H & 64) != 0;
            boolean z15 = (H & 32) != 0;
            boolean z16 = (H & 16) != 0;
            long b9 = (!z14 || z16) ? -9223372036854775807L : TimeSignalCommand.b(zVar, j8);
            if (!z14) {
                int H2 = zVar.H();
                ArrayList arrayList = new ArrayList(H2);
                for (int i11 = 0; i11 < H2; i11++) {
                    int H3 = zVar.H();
                    long b10 = !z16 ? TimeSignalCommand.b(zVar, j8) : -9223372036854775807L;
                    arrayList.add(new b(H3, b10, h0Var.b(b10), null));
                }
                emptyList = arrayList;
            }
            if (z15) {
                long H4 = zVar.H();
                boolean z17 = (128 & H4) != 0;
                j11 = ((((H4 & 1) << 32) | zVar.J()) * 1000) / 90;
                z11 = z17;
            } else {
                z11 = false;
                j11 = -9223372036854775807L;
            }
            i8 = zVar.N();
            z10 = z14;
            i9 = zVar.H();
            i10 = zVar.H();
            list = emptyList;
            long j12 = b9;
            z9 = z11;
            j10 = j11;
            z8 = z16;
            z4 = z13;
            j9 = j12;
        }
        return new SpliceInsertCommand(J, z12, z4, z10, z8, j9, h0Var.b(j9), list, z9, j10, i8, i9, i10);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeLong(this.f10144a);
        parcel.writeByte(this.f10145b ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.f10146c ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.f10147d ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.f10148e ? (byte) 1 : (byte) 0);
        parcel.writeLong(this.f10149f);
        parcel.writeLong(this.f10150g);
        int size = this.f10151h.size();
        parcel.writeInt(size);
        for (int i9 = 0; i9 < size; i9++) {
            this.f10151h.get(i9).b(parcel);
        }
        parcel.writeByte(this.f10152j ? (byte) 1 : (byte) 0);
        parcel.writeLong(this.f10153k);
        parcel.writeInt(this.f10154l);
        parcel.writeInt(this.f10155m);
        parcel.writeInt(this.f10156n);
    }
}

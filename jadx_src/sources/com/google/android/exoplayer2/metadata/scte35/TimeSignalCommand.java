package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import b6.h0;
import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class TimeSignalCommand extends SpliceCommand {
    public static final Parcelable.Creator<TimeSignalCommand> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final long f10174a;

    /* renamed from: b  reason: collision with root package name */
    public final long f10175b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<TimeSignalCommand> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public TimeSignalCommand createFromParcel(Parcel parcel) {
            return new TimeSignalCommand(parcel.readLong(), parcel.readLong(), null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public TimeSignalCommand[] newArray(int i8) {
            return new TimeSignalCommand[i8];
        }
    }

    private TimeSignalCommand(long j8, long j9) {
        this.f10174a = j8;
        this.f10175b = j9;
    }

    /* synthetic */ TimeSignalCommand(long j8, long j9, a aVar) {
        this(j8, j9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TimeSignalCommand a(z zVar, long j8, h0 h0Var) {
        long b9 = b(zVar, j8);
        return new TimeSignalCommand(b9, h0Var.b(b9));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long b(z zVar, long j8) {
        long H = zVar.H();
        if ((128 & H) != 0) {
            return 8589934591L & ((((H & 1) << 32) | zVar.J()) + j8);
        }
        return -9223372036854775807L;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeLong(this.f10174a);
        parcel.writeLong(this.f10175b);
    }
}

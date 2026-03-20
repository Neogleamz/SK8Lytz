package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class PrivateCommand extends SpliceCommand {
    public static final Parcelable.Creator<PrivateCommand> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final long f10141a;

    /* renamed from: b  reason: collision with root package name */
    public final long f10142b;

    /* renamed from: c  reason: collision with root package name */
    public final byte[] f10143c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<PrivateCommand> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public PrivateCommand createFromParcel(Parcel parcel) {
            return new PrivateCommand(parcel, null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public PrivateCommand[] newArray(int i8) {
            return new PrivateCommand[i8];
        }
    }

    private PrivateCommand(long j8, byte[] bArr, long j9) {
        this.f10141a = j9;
        this.f10142b = j8;
        this.f10143c = bArr;
    }

    private PrivateCommand(Parcel parcel) {
        this.f10141a = parcel.readLong();
        this.f10142b = parcel.readLong();
        this.f10143c = (byte[]) l0.j(parcel.createByteArray());
    }

    /* synthetic */ PrivateCommand(Parcel parcel, a aVar) {
        this(parcel);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PrivateCommand a(z zVar, int i8, long j8) {
        long J = zVar.J();
        int i9 = i8 - 4;
        byte[] bArr = new byte[i9];
        zVar.l(bArr, 0, i9);
        return new PrivateCommand(J, bArr, j8);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeLong(this.f10141a);
        parcel.writeLong(this.f10142b);
        parcel.writeByteArray(this.f10143c);
    }
}

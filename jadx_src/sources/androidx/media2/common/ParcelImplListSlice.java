package androidx.media2.common;

import android.annotation.SuppressLint;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import androidx.versionedparcelable.ParcelImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ParcelImplListSlice implements Parcelable {
    public static final Parcelable.Creator<ParcelImplListSlice> CREATOR = new b();

    /* renamed from: a  reason: collision with root package name */
    final List<ParcelImpl> f6126a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends Binder {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f6127a;

        a(int i8) {
            this.f6127a = i8;
        }

        @Override // android.os.Binder
        protected boolean onTransact(int i8, Parcel parcel, Parcel parcel2, int i9) {
            if (i8 != 1) {
                return super.onTransact(i8, parcel, parcel2, i9);
            }
            int readInt = parcel.readInt();
            while (readInt < this.f6127a && parcel2.dataSize() < 65536) {
                parcel2.writeInt(1);
                parcel2.writeParcelable(ParcelImplListSlice.this.f6126a.get(readInt), i9);
                readInt++;
            }
            if (readInt < this.f6127a) {
                parcel2.writeInt(0);
            }
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Parcelable.Creator<ParcelImplListSlice> {
        b() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public ParcelImplListSlice createFromParcel(Parcel parcel) {
            return new ParcelImplListSlice(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public ParcelImplListSlice[] newArray(int i8) {
            return new ParcelImplListSlice[i8];
        }
    }

    ParcelImplListSlice(Parcel parcel) {
        int readInt = parcel.readInt();
        this.f6126a = new ArrayList(readInt);
        if (readInt <= 0) {
            return;
        }
        int i8 = 0;
        while (i8 < readInt && parcel.readInt() != 0) {
            this.f6126a.add((ParcelImpl) parcel.readParcelable(ParcelImpl.class.getClassLoader()));
            i8++;
        }
        if (i8 >= readInt) {
            return;
        }
        IBinder readStrongBinder = parcel.readStrongBinder();
        while (i8 < readInt) {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInt(i8);
                readStrongBinder.transact(1, obtain, obtain2, 0);
                while (i8 < readInt && obtain2.readInt() != 0) {
                    this.f6126a.add((ParcelImpl) obtain2.readParcelable(ParcelImpl.class.getClassLoader()));
                    i8++;
                }
            } catch (RemoteException e8) {
                Log.w("ParcelImplListSlice", "Failure retrieving array; only received " + i8 + " of " + readInt, e8);
                return;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
    }

    public ParcelImplListSlice(List<ParcelImpl> list) {
        Objects.requireNonNull(list, "list shouldn't be null");
        this.f6126a = list;
    }

    public List<ParcelImpl> a() {
        return this.f6126a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x002d, code lost:
        r7.writeInt(0);
        r7.writeStrongBinder(new androidx.media2.common.ParcelImplListSlice.a(r6, r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0038, code lost:
        return;
     */
    @Override // android.os.Parcelable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void writeToParcel(android.os.Parcel r7, int r8) {
        /*
            r6 = this;
            java.util.List<androidx.versionedparcelable.ParcelImpl> r0 = r6.f6126a
            int r0 = r0.size()
            r7.writeInt(r0)
            if (r0 <= 0) goto L38
            r1 = 0
            r2 = r1
        Ld:
            if (r2 >= r0) goto L2b
            r3 = 1
            if (r2 >= r3) goto L2b
            int r4 = r7.dataSize()
            r5 = 65536(0x10000, float:9.18355E-41)
            if (r4 >= r5) goto L2b
            r7.writeInt(r3)
            java.util.List<androidx.versionedparcelable.ParcelImpl> r3 = r6.f6126a
            java.lang.Object r3 = r3.get(r2)
            androidx.versionedparcelable.ParcelImpl r3 = (androidx.versionedparcelable.ParcelImpl) r3
            r7.writeParcelable(r3, r8)
            int r2 = r2 + 1
            goto Ld
        L2b:
            if (r2 >= r0) goto L38
            r7.writeInt(r1)
            androidx.media2.common.ParcelImplListSlice$a r8 = new androidx.media2.common.ParcelImplListSlice$a
            r8.<init>(r0)
            r7.writeStrongBinder(r8)
        L38:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.media2.common.ParcelImplListSlice.writeToParcel(android.os.Parcel, int):void");
    }
}

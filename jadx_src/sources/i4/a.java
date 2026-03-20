package i4;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import b6.l0;
import com.google.common.collect.ImmutableList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends Binder {

    /* renamed from: a  reason: collision with root package name */
    private static final int f20464a;

    static {
        f20464a = l0.f8063a >= 30 ? IBinder.getSuggestedMaxIpcSizeBytes() : 65536;
    }

    public static ImmutableList<Bundle> a(IBinder iBinder) {
        int readInt;
        ImmutableList.a u8 = ImmutableList.u();
        int i8 = 0;
        int i9 = 1;
        while (i9 != 0) {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInt(i8);
                try {
                    iBinder.transact(1, obtain, obtain2, 0);
                    while (true) {
                        readInt = obtain2.readInt();
                        if (readInt == 1) {
                            u8.a((Bundle) b6.a.e(obtain2.readBundle()));
                            i8++;
                        }
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    i9 = readInt;
                } catch (RemoteException e8) {
                    throw new RuntimeException(e8);
                }
            } catch (Throwable th) {
                obtain2.recycle();
                obtain.recycle();
                throw th;
            }
        }
        return u8.k();
    }
}

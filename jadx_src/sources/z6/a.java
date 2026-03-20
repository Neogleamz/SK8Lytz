package z6;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements IInterface {

    /* renamed from: a  reason: collision with root package name */
    private final IBinder f24768a;

    /* renamed from: b  reason: collision with root package name */
    private final String f24769b = "com.google.android.gms.ads.identifier.internal.IAdvertisingIdService";

    /* JADX INFO: Access modifiers changed from: protected */
    public a(IBinder iBinder, String str) {
        this.f24768a = iBinder;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.f24768a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parcel d() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.f24769b);
        return obtain;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parcel e(int i8, Parcel parcel) {
        Parcel obtain = Parcel.obtain();
        try {
            try {
                this.f24768a.transact(i8, parcel, obtain, 0);
                obtain.readException();
                return obtain;
            } catch (RuntimeException e8) {
                obtain.recycle();
                throw e8;
            }
        } finally {
            parcel.recycle();
        }
    }
}

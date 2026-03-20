package a7;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements IInterface {

    /* renamed from: a  reason: collision with root package name */
    private final IBinder f193a;

    /* renamed from: b  reason: collision with root package name */
    private final String f194b;

    /* JADX INFO: Access modifiers changed from: protected */
    public a(IBinder iBinder, String str) {
        this.f193a = iBinder;
        this.f194b = str;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.f193a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parcel d() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.f194b);
        return obtain;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parcel e(int i8, Parcel parcel) {
        Parcel obtain = Parcel.obtain();
        try {
            try {
                this.f193a.transact(2, parcel, obtain, 0);
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

    /* JADX INFO: Access modifiers changed from: protected */
    public final void f(int i8, Parcel parcel) {
        Parcel obtain = Parcel.obtain();
        try {
            this.f193a.transact(i8, parcel, obtain, 0);
            obtain.readException();
        } finally {
            parcel.recycle();
            obtain.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void g(int i8, Parcel parcel) {
        try {
            this.f193a.transact(1, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }
}

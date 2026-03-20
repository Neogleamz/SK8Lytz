package androidx.room;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.room.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface r extends IInterface {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a extends Binder implements r {

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: androidx.room.r$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class C0077a implements r {

            /* renamed from: b  reason: collision with root package name */
            public static r f7180b;

            /* renamed from: a  reason: collision with root package name */
            private IBinder f7181a;

            C0077a(IBinder iBinder) {
                this.f7181a = iBinder;
            }

            @Override // androidx.room.r
            public void N1(int i8, String[] strArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("androidx.room.IMultiInstanceInvalidationService");
                    obtain.writeInt(i8);
                    obtain.writeStringArray(strArr);
                    if (this.f7181a.transact(3, obtain, null, 1) || a.e() == null) {
                        return;
                    }
                    a.e().N1(i8, strArr);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // androidx.room.r
            public int U(q qVar, String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("androidx.room.IMultiInstanceInvalidationService");
                    obtain.writeStrongBinder(qVar != null ? qVar.asBinder() : null);
                    obtain.writeString(str);
                    if (this.f7181a.transact(1, obtain, obtain2, 0) || a.e() == null) {
                        obtain2.readException();
                        return obtain2.readInt();
                    }
                    return a.e().U(qVar, str);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // androidx.room.r
            public void V1(q qVar, int i8) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("androidx.room.IMultiInstanceInvalidationService");
                    obtain.writeStrongBinder(qVar != null ? qVar.asBinder() : null);
                    obtain.writeInt(i8);
                    if (this.f7181a.transact(2, obtain, obtain2, 0) || a.e() == null) {
                        obtain2.readException();
                    } else {
                        a.e().V1(qVar, i8);
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f7181a;
            }
        }

        public a() {
            attachInterface(this, "androidx.room.IMultiInstanceInvalidationService");
        }

        public static r d(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("androidx.room.IMultiInstanceInvalidationService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof r)) ? new C0077a(iBinder) : (r) queryLocalInterface;
        }

        public static r e() {
            return C0077a.f7180b;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i8, Parcel parcel, Parcel parcel2, int i9) {
            if (i8 == 1) {
                parcel.enforceInterface("androidx.room.IMultiInstanceInvalidationService");
                int U = U(q.a.d(parcel.readStrongBinder()), parcel.readString());
                parcel2.writeNoException();
                parcel2.writeInt(U);
                return true;
            } else if (i8 == 2) {
                parcel.enforceInterface("androidx.room.IMultiInstanceInvalidationService");
                V1(q.a.d(parcel.readStrongBinder()), parcel.readInt());
                parcel2.writeNoException();
                return true;
            } else if (i8 == 3) {
                parcel.enforceInterface("androidx.room.IMultiInstanceInvalidationService");
                N1(parcel.readInt(), parcel.createStringArray());
                return true;
            } else if (i8 != 1598968902) {
                return super.onTransact(i8, parcel, parcel2, i9);
            } else {
                parcel2.writeString("androidx.room.IMultiInstanceInvalidationService");
                return true;
            }
        }
    }

    void N1(int i8, String[] strArr);

    int U(q qVar, String str);

    void V1(q qVar, int i8);
}

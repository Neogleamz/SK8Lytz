package d;

import android.app.Notification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface a extends IInterface {

    /* renamed from: d.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class AbstractBinderC0162a extends Binder implements a {

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: d.a$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class C0163a implements a {

            /* renamed from: b  reason: collision with root package name */
            public static a f19690b;

            /* renamed from: a  reason: collision with root package name */
            private IBinder f19691a;

            C0163a(IBinder iBinder) {
                this.f19691a = iBinder;
            }

            @Override // d.a
            public void M(String str) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                    obtain.writeString(str);
                    if (this.f19691a.transact(3, obtain, null, 1) || AbstractBinderC0162a.e() == null) {
                        return;
                    }
                    AbstractBinderC0162a.e().M(str);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // d.a
            public void R1(String str, int i8, String str2, Notification notification) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                    obtain.writeString(str);
                    obtain.writeInt(i8);
                    obtain.writeString(str2);
                    if (notification != null) {
                        obtain.writeInt(1);
                        notification.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.f19691a.transact(1, obtain, null, 1) || AbstractBinderC0162a.e() == null) {
                        return;
                    }
                    AbstractBinderC0162a.e().R1(str, i8, str2, notification);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f19691a;
            }

            @Override // d.a
            public void b1(String str, int i8, String str2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                    obtain.writeString(str);
                    obtain.writeInt(i8);
                    obtain.writeString(str2);
                    if (this.f19691a.transact(2, obtain, null, 1) || AbstractBinderC0162a.e() == null) {
                        return;
                    }
                    AbstractBinderC0162a.e().b1(str, i8, str2);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public AbstractBinderC0162a() {
            attachInterface(this, "android.support.v4.app.INotificationSideChannel");
        }

        public static a d(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("android.support.v4.app.INotificationSideChannel");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof a)) ? new C0163a(iBinder) : (a) queryLocalInterface;
        }

        public static a e() {
            return C0163a.f19690b;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i8, Parcel parcel, Parcel parcel2, int i9) {
            if (i8 == 1) {
                parcel.enforceInterface("android.support.v4.app.INotificationSideChannel");
                R1(parcel.readString(), parcel.readInt(), parcel.readString(), parcel.readInt() != 0 ? (Notification) Notification.CREATOR.createFromParcel(parcel) : null);
                return true;
            } else if (i8 == 2) {
                parcel.enforceInterface("android.support.v4.app.INotificationSideChannel");
                b1(parcel.readString(), parcel.readInt(), parcel.readString());
                return true;
            } else if (i8 == 3) {
                parcel.enforceInterface("android.support.v4.app.INotificationSideChannel");
                M(parcel.readString());
                return true;
            } else if (i8 != 1598968902) {
                return super.onTransact(i8, parcel, parcel2, i9);
            } else {
                parcel2.writeString("android.support.v4.app.INotificationSideChannel");
                return true;
            }
        }
    }

    void M(String str);

    void R1(String str, int i8, String str2, Notification notification);

    void b1(String str, int i8, String str2);
}

package com.google.android.gms.cloudmessaging;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
interface a extends IInterface {

    /* renamed from: com.google.android.gms.cloudmessaging.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0120a implements a {

        /* renamed from: a  reason: collision with root package name */
        private final IBinder f11472a;

        /* JADX INFO: Access modifiers changed from: package-private */
        public C0120a(IBinder iBinder) {
            this.f11472a = iBinder;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.f11472a;
        }

        @Override // com.google.android.gms.cloudmessaging.a
        public void p(Message message) {
            Parcel obtain = Parcel.obtain();
            obtain.writeInterfaceToken("com.google.android.gms.iid.IMessengerCompat");
            obtain.writeInt(1);
            message.writeToParcel(obtain, 0);
            try {
                this.f11472a.transact(1, obtain, null, 1);
            } finally {
                obtain.recycle();
            }
        }
    }

    void p(Message message);
}

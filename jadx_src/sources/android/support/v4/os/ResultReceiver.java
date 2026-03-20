package android.support.v4.os;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.os.a;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ResultReceiver implements Parcelable {
    public static final Parcelable.Creator<ResultReceiver> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    final boolean f340a = false;

    /* renamed from: b  reason: collision with root package name */
    final Handler f341b = null;

    /* renamed from: c  reason: collision with root package name */
    android.support.v4.os.a f342c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<ResultReceiver> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public ResultReceiver createFromParcel(Parcel parcel) {
            return new ResultReceiver(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public ResultReceiver[] newArray(int i8) {
            return new ResultReceiver[i8];
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends a.AbstractBinderC0007a {
        b() {
        }

        @Override // android.support.v4.os.a
        public void c2(int i8, Bundle bundle) {
            ResultReceiver resultReceiver = ResultReceiver.this;
            Handler handler = resultReceiver.f341b;
            if (handler != null) {
                handler.post(new c(i8, bundle));
            } else {
                resultReceiver.a(i8, bundle);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final int f344a;

        /* renamed from: b  reason: collision with root package name */
        final Bundle f345b;

        c(int i8, Bundle bundle) {
            this.f344a = i8;
            this.f345b = bundle;
        }

        @Override // java.lang.Runnable
        public void run() {
            ResultReceiver.this.a(this.f344a, this.f345b);
        }
    }

    ResultReceiver(Parcel parcel) {
        this.f342c = a.AbstractBinderC0007a.d(parcel.readStrongBinder());
    }

    protected void a(int i8, Bundle bundle) {
    }

    public void b(int i8, Bundle bundle) {
        if (this.f340a) {
            Handler handler = this.f341b;
            if (handler != null) {
                handler.post(new c(i8, bundle));
                return;
            } else {
                a(i8, bundle);
                return;
            }
        }
        android.support.v4.os.a aVar = this.f342c;
        if (aVar != null) {
            try {
                aVar.c2(i8, bundle);
            } catch (RemoteException unused) {
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        synchronized (this) {
            if (this.f342c == null) {
                this.f342c = new b();
            }
            parcel.writeStrongBinder(this.f342c.asBinder());
        }
    }
}

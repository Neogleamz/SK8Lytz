package com.google.android.gms.common.internal;

import android.app.PendingIntent;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.b;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p extends com.google.android.gms.internal.common.j {

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ b f11856b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p(b bVar, Looper looper) {
        super(looper);
        this.f11856b = bVar;
    }

    private static final void a(Message message) {
        q qVar = (q) message.obj;
        qVar.b();
        qVar.e();
    }

    private static final boolean b(Message message) {
        int i8 = message.what;
        return i8 == 2 || i8 == 1 || i8 == 7;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        b.a aVar;
        b.a aVar2;
        ConnectionResult connectionResult;
        ConnectionResult connectionResult2;
        boolean z4;
        if (this.f11856b.L.get() != message.arg1) {
            if (b(message)) {
                a(message);
                return;
            }
            return;
        }
        int i8 = message.what;
        if ((i8 == 1 || i8 == 7 || ((i8 == 4 && !this.f11856b.r()) || message.what == 5)) && !this.f11856b.e()) {
            a(message);
            return;
        }
        int i9 = message.what;
        if (i9 == 4) {
            this.f11856b.G = new ConnectionResult(message.arg2);
            if (b.f0(this.f11856b)) {
                b bVar = this.f11856b;
                z4 = bVar.H;
                if (!z4) {
                    bVar.g0(3, null);
                    return;
                }
            }
            b bVar2 = this.f11856b;
            connectionResult2 = bVar2.G;
            ConnectionResult connectionResult3 = connectionResult2 != null ? bVar2.G : new ConnectionResult(8);
            this.f11856b.f11835t.a(connectionResult3);
            this.f11856b.J(connectionResult3);
        } else if (i9 == 5) {
            b bVar3 = this.f11856b;
            connectionResult = bVar3.G;
            ConnectionResult connectionResult4 = connectionResult != null ? bVar3.G : new ConnectionResult(8);
            this.f11856b.f11835t.a(connectionResult4);
            this.f11856b.J(connectionResult4);
        } else if (i9 == 3) {
            Object obj = message.obj;
            ConnectionResult connectionResult5 = new ConnectionResult(message.arg2, obj instanceof PendingIntent ? (PendingIntent) obj : null);
            this.f11856b.f11835t.a(connectionResult5);
            this.f11856b.J(connectionResult5);
        } else if (i9 == 6) {
            this.f11856b.g0(5, null);
            b bVar4 = this.f11856b;
            aVar = bVar4.A;
            if (aVar != null) {
                aVar2 = bVar4.A;
                aVar2.d(message.arg2);
            }
            this.f11856b.K(message.arg2);
            b.e0(this.f11856b, 5, 1, null);
        } else if (i9 == 2 && !this.f11856b.isConnected()) {
            a(message);
        } else if (b(message)) {
            ((q) message.obj).c();
        } else {
            int i10 = message.what;
            Log.wtf("GmsClient", "Don't know how to handle message: " + i10, new Exception());
        }
    }
}

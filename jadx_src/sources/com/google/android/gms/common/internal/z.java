package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.HashMap;
import n6.k0;
import n6.l0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z implements Handler.Callback {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ a0 f11874a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ z(a0 a0Var, l0 l0Var) {
        this.f11874a = a0Var;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        HashMap hashMap;
        HashMap hashMap2;
        HashMap hashMap3;
        HashMap hashMap4;
        HashMap hashMap5;
        int i8 = message.what;
        if (i8 == 0) {
            hashMap = this.f11874a.f11813f;
            synchronized (hashMap) {
                k0 k0Var = (k0) message.obj;
                hashMap2 = this.f11874a.f11813f;
                y yVar = (y) hashMap2.get(k0Var);
                if (yVar != null && yVar.i()) {
                    if (yVar.j()) {
                        yVar.g("GmsClientSupervisor");
                    }
                    hashMap3 = this.f11874a.f11813f;
                    hashMap3.remove(k0Var);
                }
            }
            return true;
        } else if (i8 != 1) {
            return false;
        } else {
            hashMap4 = this.f11874a.f11813f;
            synchronized (hashMap4) {
                k0 k0Var2 = (k0) message.obj;
                hashMap5 = this.f11874a.f11813f;
                y yVar2 = (y) hashMap5.get(k0Var2);
                if (yVar2 != null && yVar2.a() == 3) {
                    String valueOf = String.valueOf(k0Var2);
                    Log.e("GmsClientSupervisor", "Timeout waiting for ServiceConnection callback " + valueOf, new Exception());
                    ComponentName b9 = yVar2.b();
                    if (b9 == null) {
                        b9 = k0Var2.a();
                    }
                    if (b9 == null) {
                        String c9 = k0Var2.c();
                        n6.j.l(c9);
                        b9 = new ComponentName(c9, "unknown");
                    }
                    yVar2.onServiceDisconnected(b9);
                }
            }
            return true;
        }
    }
}

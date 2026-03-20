package com.google.android.gms.cloudmessaging;

import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class n {

    /* renamed from: a  reason: collision with root package name */
    private final Messenger f11507a;

    /* renamed from: b  reason: collision with root package name */
    private final zze f11508b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(IBinder iBinder) {
        String interfaceDescriptor = iBinder.getInterfaceDescriptor();
        if (i6.f.a(interfaceDescriptor, "android.os.IMessenger")) {
            this.f11507a = new Messenger(iBinder);
            this.f11508b = null;
        } else if (!i6.f.a(interfaceDescriptor, "com.google.android.gms.iid.IMessengerCompat")) {
            Log.w("MessengerIpcClient", "Invalid interface descriptor: ".concat(String.valueOf(interfaceDescriptor)));
            throw new RemoteException();
        } else {
            this.f11508b = new zze(iBinder);
            this.f11507a = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(Message message) {
        Messenger messenger = this.f11507a;
        if (messenger != null) {
            messenger.send(message);
            return;
        }
        zze zzeVar = this.f11508b;
        if (zzeVar == null) {
            throw new IllegalStateException("Both messengers are null");
        }
        zzeVar.b(message);
    }
}

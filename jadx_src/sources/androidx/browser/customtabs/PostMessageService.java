package androidx.browser.customtabs;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import b.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class PostMessageService extends Service {

    /* renamed from: a  reason: collision with root package name */
    private c.a f1667a = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends c.a {
        a() {
        }

        @Override // b.c
        public void L0(b.a aVar, Bundle bundle) {
            aVar.P1(bundle);
        }

        @Override // b.c
        public void x1(b.a aVar, String str, Bundle bundle) {
            aVar.M1(str, bundle);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f1667a;
    }
}

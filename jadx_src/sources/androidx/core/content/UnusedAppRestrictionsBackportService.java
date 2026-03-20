package androidx.core.content;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import r0.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class UnusedAppRestrictionsBackportService extends Service {

    /* renamed from: a  reason: collision with root package name */
    private b.a f4625a = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends b.a {
        a() {
        }

        @Override // r0.b
        public void U1(r0.a aVar) {
            if (aVar == null) {
                return;
            }
            UnusedAppRestrictionsBackportService.this.a(new c(aVar));
        }
    }

    protected abstract void a(c cVar);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f4625a;
    }
}

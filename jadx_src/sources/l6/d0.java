package l6;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d0 {

    /* renamed from: a  reason: collision with root package name */
    private final k0.a f21738a;

    /* renamed from: b  reason: collision with root package name */
    private final k0.a f21739b;

    /* renamed from: c  reason: collision with root package name */
    private final j7.k f21740c;

    /* renamed from: d  reason: collision with root package name */
    private int f21741d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f21742e;

    public final Set a() {
        return this.f21738a.keySet();
    }

    public final void b(b bVar, ConnectionResult connectionResult, String str) {
        this.f21738a.put(bVar, connectionResult);
        this.f21739b.put(bVar, str);
        this.f21741d--;
        if (!connectionResult.E0()) {
            this.f21742e = true;
        }
        if (this.f21741d == 0) {
            if (!this.f21742e) {
                this.f21740c.c(this.f21739b);
                return;
            }
            this.f21740c.b(new AvailabilityException(this.f21738a));
        }
    }
}

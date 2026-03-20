package v6;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.internal.common.j;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements Executor {

    /* renamed from: a  reason: collision with root package name */
    private final Handler f23345a;

    public a(Looper looper) {
        this.f23345a = new j(looper);
    }

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        this.f23345a.post(runnable);
    }
}

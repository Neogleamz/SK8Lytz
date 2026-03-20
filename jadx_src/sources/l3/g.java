package l3;

import android.os.Handler;
import android.os.Looper;
import io.flutter.plugin.common.j;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class g {

    /* renamed from: c  reason: collision with root package name */
    public static final a f21567c = new a(null);

    /* renamed from: d  reason: collision with root package name */
    private static final Handler f21568d = new Handler(Looper.getMainLooper());

    /* renamed from: e  reason: collision with root package name */
    private static final ExecutorService f21569e;

    /* renamed from: a  reason: collision with root package name */
    private j.d f21570a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f21571b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(i iVar) {
            this();
        }

        public final ExecutorService a() {
            return g.f21569e;
        }
    }

    static {
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(8);
        p.d(newFixedThreadPool, "newFixedThreadPool(...)");
        f21569e = newFixedThreadPool;
    }

    public g(j.d dVar) {
        this.f21570a = dVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(j.d dVar, Object obj) {
        if (dVar != null) {
            dVar.success(obj);
        }
    }

    public final void c(Object obj) {
        if (this.f21571b) {
            return;
        }
        this.f21571b = true;
        j.d dVar = this.f21570a;
        this.f21570a = null;
        f21568d.post(new f(dVar, obj));
    }
}

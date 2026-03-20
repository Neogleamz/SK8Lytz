package z;

import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements Executor {

    /* renamed from: a  reason: collision with root package name */
    private static volatile b f24481a;

    b() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Executor a() {
        if (f24481a != null) {
            return f24481a;
        }
        synchronized (b.class) {
            if (f24481a == null) {
                f24481a = new b();
            }
        }
        return f24481a;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        runnable.run();
    }
}

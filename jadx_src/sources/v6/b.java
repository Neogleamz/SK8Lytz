package v6;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b implements ThreadFactory {

    /* renamed from: a  reason: collision with root package name */
    private final String f23346a;

    /* renamed from: b  reason: collision with root package name */
    private final ThreadFactory f23347b = Executors.defaultThreadFactory();

    public b(String str) {
        j.m(str, "Name must not be null");
        this.f23346a = str;
    }

    @Override // java.util.concurrent.ThreadFactory
    public final Thread newThread(Runnable runnable) {
        Thread newThread = this.f23347b.newThread(new c(runnable, 0));
        newThread.setName(this.f23346a);
        return newThread;
    }
}

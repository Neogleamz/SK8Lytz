package b6;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g {

    /* renamed from: a  reason: collision with root package name */
    private final d f8045a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f8046b;

    public g() {
        this(d.f8029a);
    }

    public g(d dVar) {
        this.f8045a = dVar;
    }

    public synchronized void a() {
        while (!this.f8046b) {
            wait();
        }
    }

    public synchronized void b() {
        boolean z4 = false;
        while (!this.f8046b) {
            try {
                wait();
            } catch (InterruptedException unused) {
                z4 = true;
            }
        }
        if (z4) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized boolean c() {
        boolean z4;
        z4 = this.f8046b;
        this.f8046b = false;
        return z4;
    }

    public synchronized boolean d() {
        return this.f8046b;
    }

    public synchronized boolean e() {
        if (this.f8046b) {
            return false;
        }
        this.f8046b = true;
        notifyAll();
        return true;
    }
}

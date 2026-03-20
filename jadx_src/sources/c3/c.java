package c3;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c<T> {

    /* renamed from: a  reason: collision with root package name */
    private T f8296a;

    /* renamed from: b  reason: collision with root package name */
    private Throwable f8297b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f8298c = false;

    private T a() {
        Throwable th = this.f8297b;
        if (th == null) {
            return this.f8296a;
        }
        throw th;
    }

    public T b(int i8) {
        synchronized (this) {
            if (this.f8298c) {
                return a();
            }
            wait(i8);
            if (this.f8298c) {
                return a();
            }
            RuntimeException runtimeException = new RuntimeException("time out");
            c(this.f8297b);
            throw runtimeException;
        }
    }

    public void c(Throwable th) {
        synchronized (this) {
            if (this.f8298c) {
                return;
            }
            this.f8297b = th;
            this.f8298c = true;
            notify();
        }
    }

    public void d(T t8) {
        synchronized (this) {
            if (this.f8298c) {
                return;
            }
            this.f8296a = t8;
            this.f8298c = true;
            notify();
        }
    }
}

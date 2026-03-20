package androidx.recyclerview.widget;

import androidx.recyclerview.widget.h;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c<T> {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f6796a;

    /* renamed from: b  reason: collision with root package name */
    private final Executor f6797b;

    /* renamed from: c  reason: collision with root package name */
    private final h.d<T> f6798c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a<T> {

        /* renamed from: d  reason: collision with root package name */
        private static final Object f6799d = new Object();

        /* renamed from: e  reason: collision with root package name */
        private static Executor f6800e;

        /* renamed from: a  reason: collision with root package name */
        private Executor f6801a;

        /* renamed from: b  reason: collision with root package name */
        private Executor f6802b;

        /* renamed from: c  reason: collision with root package name */
        private final h.d<T> f6803c;

        public a(h.d<T> dVar) {
            this.f6803c = dVar;
        }

        public c<T> a() {
            if (this.f6802b == null) {
                synchronized (f6799d) {
                    if (f6800e == null) {
                        f6800e = Executors.newFixedThreadPool(2);
                    }
                }
                this.f6802b = f6800e;
            }
            return new c<>(this.f6801a, this.f6802b, this.f6803c);
        }
    }

    c(Executor executor, Executor executor2, h.d<T> dVar) {
        this.f6796a = executor;
        this.f6797b = executor2;
        this.f6798c = dVar;
    }

    public Executor a() {
        return this.f6797b;
    }

    public h.d<T> b() {
        return this.f6798c;
    }

    public Executor c() {
        return this.f6796a;
    }
}

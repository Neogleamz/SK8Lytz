package a6;

import a6.d;
import android.os.Handler;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface d {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {

        /* renamed from: a6.d$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class C0002a {

            /* renamed from: a  reason: collision with root package name */
            private final CopyOnWriteArrayList<C0003a> f80a = new CopyOnWriteArrayList<>();

            /* JADX INFO: Access modifiers changed from: private */
            /* renamed from: a6.d$a$a$a  reason: collision with other inner class name */
            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            public static final class C0003a {

                /* renamed from: a  reason: collision with root package name */
                private final Handler f81a;

                /* renamed from: b  reason: collision with root package name */
                private final a f82b;

                /* renamed from: c  reason: collision with root package name */
                private boolean f83c;

                public C0003a(Handler handler, a aVar) {
                    this.f81a = handler;
                    this.f82b = aVar;
                }

                public void d() {
                    this.f83c = true;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static /* synthetic */ void d(C0003a c0003a, int i8, long j8, long j9) {
                c0003a.f82b.P(i8, j8, j9);
            }

            public void b(Handler handler, a aVar) {
                b6.a.e(handler);
                b6.a.e(aVar);
                e(aVar);
                this.f80a.add(new C0003a(handler, aVar));
            }

            public void c(final int i8, final long j8, final long j9) {
                Iterator<C0003a> it = this.f80a.iterator();
                while (it.hasNext()) {
                    final C0003a next = it.next();
                    if (!next.f83c) {
                        next.f81a.post(new Runnable() { // from class: a6.c
                            @Override // java.lang.Runnable
                            public final void run() {
                                d.a.C0002a.d(d.a.C0002a.C0003a.this, i8, j8, j9);
                            }
                        });
                    }
                }
            }

            public void e(a aVar) {
                Iterator<C0003a> it = this.f80a.iterator();
                while (it.hasNext()) {
                    C0003a next = it.next();
                    if (next.f82b == aVar) {
                        next.d();
                        this.f80a.remove(next);
                    }
                }
            }
        }

        void P(int i8, long j8, long j9);
    }

    void a(a aVar);

    default long b() {
        return -9223372036854775807L;
    }

    y d();

    long f();

    void h(Handler handler, a aVar);
}

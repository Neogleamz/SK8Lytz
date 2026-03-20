package androidx.emoji2.text;

import android.content.Context;
import android.os.Build;
import androidx.emoji2.text.EmojiCompatInitializer;
import androidx.emoji2.text.e;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleInitializer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class EmojiCompatInitializer implements v1.a<Boolean> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends e.c {
        protected a(Context context) {
            super(new b(context));
            b(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements e.g {

        /* renamed from: a  reason: collision with root package name */
        private final Context f5214a;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends e.h {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ e.h f5215a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ ThreadPoolExecutor f5216b;

            a(e.h hVar, ThreadPoolExecutor threadPoolExecutor) {
                this.f5215a = hVar;
                this.f5216b = threadPoolExecutor;
            }

            @Override // androidx.emoji2.text.e.h
            public void a(Throwable th) {
                try {
                    this.f5215a.a(th);
                } finally {
                    this.f5216b.shutdown();
                }
            }

            @Override // androidx.emoji2.text.e.h
            public void b(m mVar) {
                try {
                    this.f5215a.b(mVar);
                } finally {
                    this.f5216b.shutdown();
                }
            }
        }

        b(Context context) {
            this.f5214a = context.getApplicationContext();
        }

        @Override // androidx.emoji2.text.e.g
        public void a(final e.h hVar) {
            final ThreadPoolExecutor b9 = androidx.emoji2.text.b.b("EmojiCompatInitializer");
            b9.execute(new Runnable() { // from class: androidx.emoji2.text.f
                @Override // java.lang.Runnable
                public final void run() {
                    EmojiCompatInitializer.b.this.d(hVar, b9);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: c */
        public void d(e.h hVar, ThreadPoolExecutor threadPoolExecutor) {
            try {
                j a9 = androidx.emoji2.text.c.a(this.f5214a);
                if (a9 == null) {
                    throw new RuntimeException("EmojiCompat font provider not available on this device.");
                }
                a9.c(threadPoolExecutor);
                a9.a().a(new a(hVar, threadPoolExecutor));
            } catch (Throwable th) {
                hVar.a(th);
                threadPoolExecutor.shutdown();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                androidx.core.os.o.a("EmojiCompat.EmojiCompatInitializer.run");
                if (e.h()) {
                    e.b().k();
                }
            } finally {
                androidx.core.os.o.b();
            }
        }
    }

    @Override // v1.a
    public List<Class<? extends v1.a<?>>> a() {
        return Collections.singletonList(ProcessLifecycleInitializer.class);
    }

    @Override // v1.a
    /* renamed from: c */
    public Boolean b(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            e.g(new a(context));
            d(context);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    void d(Context context) {
        final Lifecycle lifecycle = ((androidx.lifecycle.j) androidx.startup.a.e(context).f(ProcessLifecycleInitializer.class)).getLifecycle();
        lifecycle.a(new DefaultLifecycleObserver() { // from class: androidx.emoji2.text.EmojiCompatInitializer.1
            @Override // androidx.lifecycle.DefaultLifecycleObserver
            public void onResume(androidx.lifecycle.j jVar) {
                EmojiCompatInitializer.this.e();
                lifecycle.c(this);
            }
        });
    }

    void e() {
        androidx.emoji2.text.b.d().postDelayed(new c(), 500L);
    }
}

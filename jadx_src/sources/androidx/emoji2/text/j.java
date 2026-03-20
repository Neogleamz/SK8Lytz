package androidx.emoji2.text;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.core.provider.g;
import androidx.emoji2.text.e;
import androidx.emoji2.text.j;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j extends e.c {

    /* renamed from: j  reason: collision with root package name */
    private static final a f5279j = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        public Typeface a(Context context, g.b bVar) {
            return androidx.core.provider.g.a(context, null, new g.b[]{bVar});
        }

        public g.a b(Context context, androidx.core.provider.e eVar) {
            return androidx.core.provider.g.b(context, null, eVar);
        }

        public void c(Context context, ContentObserver contentObserver) {
            context.getContentResolver().unregisterContentObserver(contentObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements e.g {

        /* renamed from: a  reason: collision with root package name */
        private final Context f5280a;

        /* renamed from: b  reason: collision with root package name */
        private final androidx.core.provider.e f5281b;

        /* renamed from: c  reason: collision with root package name */
        private final a f5282c;

        /* renamed from: d  reason: collision with root package name */
        private final Object f5283d = new Object();

        /* renamed from: e  reason: collision with root package name */
        private Handler f5284e;

        /* renamed from: f  reason: collision with root package name */
        private Executor f5285f;

        /* renamed from: g  reason: collision with root package name */
        private ThreadPoolExecutor f5286g;

        /* renamed from: h  reason: collision with root package name */
        e.h f5287h;

        /* renamed from: i  reason: collision with root package name */
        private ContentObserver f5288i;

        /* renamed from: j  reason: collision with root package name */
        private Runnable f5289j;

        b(Context context, androidx.core.provider.e eVar, a aVar) {
            androidx.core.util.h.i(context, "Context cannot be null");
            androidx.core.util.h.i(eVar, "FontRequest cannot be null");
            this.f5280a = context.getApplicationContext();
            this.f5281b = eVar;
            this.f5282c = aVar;
        }

        private void b() {
            synchronized (this.f5283d) {
                this.f5287h = null;
                ContentObserver contentObserver = this.f5288i;
                if (contentObserver != null) {
                    this.f5282c.c(this.f5280a, contentObserver);
                    this.f5288i = null;
                }
                Handler handler = this.f5284e;
                if (handler != null) {
                    handler.removeCallbacks(this.f5289j);
                }
                this.f5284e = null;
                ThreadPoolExecutor threadPoolExecutor = this.f5286g;
                if (threadPoolExecutor != null) {
                    threadPoolExecutor.shutdown();
                }
                this.f5285f = null;
                this.f5286g = null;
            }
        }

        private g.b e() {
            try {
                g.a b9 = this.f5282c.b(this.f5280a, this.f5281b);
                if (b9.c() == 0) {
                    g.b[] b10 = b9.b();
                    if (b10 == null || b10.length == 0) {
                        throw new RuntimeException("fetchFonts failed (empty result)");
                    }
                    return b10[0];
                }
                throw new RuntimeException("fetchFonts failed (" + b9.c() + ")");
            } catch (PackageManager.NameNotFoundException e8) {
                throw new RuntimeException("provider not found", e8);
            }
        }

        @Override // androidx.emoji2.text.e.g
        public void a(e.h hVar) {
            androidx.core.util.h.i(hVar, "LoaderCallback cannot be null");
            synchronized (this.f5283d) {
                this.f5287h = hVar;
            }
            d();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void c() {
            synchronized (this.f5283d) {
                if (this.f5287h == null) {
                    return;
                }
                try {
                    g.b e8 = e();
                    int b9 = e8.b();
                    if (b9 == 2) {
                        synchronized (this.f5283d) {
                        }
                    }
                    if (b9 != 0) {
                        throw new RuntimeException("fetchFonts result is not OK. (" + b9 + ")");
                    }
                    androidx.core.os.o.a("EmojiCompat.FontRequestEmojiCompatConfig.buildTypeface");
                    Typeface a9 = this.f5282c.a(this.f5280a, e8);
                    ByteBuffer f5 = androidx.core.graphics.m.f(this.f5280a, null, e8.d());
                    if (f5 == null || a9 == null) {
                        throw new RuntimeException("Unable to open file.");
                    }
                    m b10 = m.b(a9, f5);
                    androidx.core.os.o.b();
                    synchronized (this.f5283d) {
                        e.h hVar = this.f5287h;
                        if (hVar != null) {
                            hVar.b(b10);
                        }
                    }
                    b();
                } catch (Throwable th) {
                    synchronized (this.f5283d) {
                        e.h hVar2 = this.f5287h;
                        if (hVar2 != null) {
                            hVar2.a(th);
                        }
                        b();
                    }
                }
            }
        }

        void d() {
            synchronized (this.f5283d) {
                if (this.f5287h == null) {
                    return;
                }
                if (this.f5285f == null) {
                    ThreadPoolExecutor b9 = androidx.emoji2.text.b.b("emojiCompat");
                    this.f5286g = b9;
                    this.f5285f = b9;
                }
                this.f5285f.execute(new Runnable() { // from class: androidx.emoji2.text.k
                    @Override // java.lang.Runnable
                    public final void run() {
                        j.b.this.c();
                    }
                });
            }
        }

        public void f(Executor executor) {
            synchronized (this.f5283d) {
                this.f5285f = executor;
            }
        }
    }

    public j(Context context, androidx.core.provider.e eVar) {
        super(new b(context, eVar, f5279j));
    }

    public j c(Executor executor) {
        ((b) a()).f(executor);
        return this;
    }
}

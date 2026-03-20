package androidx.core.provider;

import android.graphics.Typeface;
import android.os.Handler;
import androidx.core.provider.f;
import androidx.core.provider.g;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private final g.c f4788a;

    /* renamed from: b  reason: collision with root package name */
    private final Handler f4789b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.core.provider.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class RunnableC0036a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ g.c f4790a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Typeface f4791b;

        RunnableC0036a(g.c cVar, Typeface typeface) {
            this.f4790a = cVar;
            this.f4791b = typeface;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f4790a.b(this.f4791b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ g.c f4793a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f4794b;

        b(g.c cVar, int i8) {
            this.f4793a = cVar;
            this.f4794b = i8;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f4793a.a(this.f4794b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(g.c cVar, Handler handler) {
        this.f4788a = cVar;
        this.f4789b = handler;
    }

    private void a(int i8) {
        this.f4789b.post(new b(this.f4788a, i8));
    }

    private void c(Typeface typeface) {
        this.f4789b.post(new RunnableC0036a(this.f4788a, typeface));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(f.e eVar) {
        if (eVar.a()) {
            c(eVar.f4818a);
        } else {
            a(eVar.f4819b);
        }
    }
}

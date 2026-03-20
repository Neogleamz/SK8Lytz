package z5;

import a6.d;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import i4.f0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a0 {

    /* renamed from: a  reason: collision with root package name */
    private a f24601a;

    /* renamed from: b  reason: collision with root package name */
    private d f24602b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final d a() {
        return (d) b6.a.h(this.f24602b);
    }

    public void b(a aVar, d dVar) {
        this.f24601a = aVar;
        this.f24602b = dVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void c() {
        a aVar = this.f24601a;
        if (aVar != null) {
            aVar.b();
        }
    }

    public boolean d() {
        return false;
    }

    public abstract void e(Object obj);

    public void f() {
        this.f24601a = null;
        this.f24602b = null;
    }

    public abstract b0 g(f0[] f0VarArr, h5.w wVar, k.b bVar, h2 h2Var);

    public void h(com.google.android.exoplayer2.audio.a aVar) {
    }
}

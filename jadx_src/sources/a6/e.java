package a6;

import b6.l0;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e implements h {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f84a;

    /* renamed from: b  reason: collision with root package name */
    private final ArrayList<y> f85b = new ArrayList<>(1);

    /* renamed from: c  reason: collision with root package name */
    private int f86c;

    /* renamed from: d  reason: collision with root package name */
    private com.google.android.exoplayer2.upstream.a f87d;

    /* JADX INFO: Access modifiers changed from: protected */
    public e(boolean z4) {
        this.f84a = z4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void l(int i8) {
        com.google.android.exoplayer2.upstream.a aVar = (com.google.android.exoplayer2.upstream.a) l0.j(this.f87d);
        for (int i9 = 0; i9 < this.f86c; i9++) {
            this.f85b.get(i9).c(this, aVar, this.f84a, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void m() {
        com.google.android.exoplayer2.upstream.a aVar = (com.google.android.exoplayer2.upstream.a) l0.j(this.f87d);
        for (int i8 = 0; i8 < this.f86c; i8++) {
            this.f85b.get(i8).g(this, aVar, this.f84a);
        }
        this.f87d = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void n(com.google.android.exoplayer2.upstream.a aVar) {
        for (int i8 = 0; i8 < this.f86c; i8++) {
            this.f85b.get(i8).e(this, aVar, this.f84a);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void o(com.google.android.exoplayer2.upstream.a aVar) {
        this.f87d = aVar;
        for (int i8 = 0; i8 < this.f86c; i8++) {
            this.f85b.get(i8).i(this, aVar, this.f84a);
        }
    }

    @Override // a6.h
    public final void w(y yVar) {
        b6.a.e(yVar);
        if (this.f85b.contains(yVar)) {
            return;
        }
        this.f85b.add(yVar);
        this.f86c++;
    }
}

package j5;

import com.google.android.exoplayer2.w0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a extends n {

    /* renamed from: k  reason: collision with root package name */
    public final long f20714k;

    /* renamed from: l  reason: collision with root package name */
    public final long f20715l;

    /* renamed from: m  reason: collision with root package name */
    private c f20716m;

    /* renamed from: n  reason: collision with root package name */
    private int[] f20717n;

    public a(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, w0 w0Var, int i8, Object obj, long j8, long j9, long j10, long j11, long j12) {
        super(hVar, aVar, w0Var, i8, obj, j8, j9, j12);
        this.f20714k = j10;
        this.f20715l = j11;
    }

    public final int i(int i8) {
        return ((int[]) b6.a.h(this.f20717n))[i8];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final c j() {
        return (c) b6.a.h(this.f20716m);
    }

    public void k(c cVar) {
        this.f20716m = cVar;
        this.f20717n = cVar.a();
    }
}

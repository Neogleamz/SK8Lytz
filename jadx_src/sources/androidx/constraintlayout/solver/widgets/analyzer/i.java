package androidx.constraintlayout.solver.widgets.analyzer;

import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i {

    /* renamed from: h  reason: collision with root package name */
    public static int f3771h;

    /* renamed from: c  reason: collision with root package name */
    k f3774c;

    /* renamed from: d  reason: collision with root package name */
    k f3775d;

    /* renamed from: f  reason: collision with root package name */
    int f3777f;

    /* renamed from: g  reason: collision with root package name */
    int f3778g;

    /* renamed from: a  reason: collision with root package name */
    public int f3772a = 0;

    /* renamed from: b  reason: collision with root package name */
    public boolean f3773b = false;

    /* renamed from: e  reason: collision with root package name */
    ArrayList<k> f3776e = new ArrayList<>();

    public i(k kVar, int i8) {
        this.f3774c = null;
        this.f3775d = null;
        this.f3777f = 0;
        int i9 = f3771h;
        this.f3777f = i9;
        f3771h = i9 + 1;
        this.f3774c = kVar;
        this.f3775d = kVar;
        this.f3778g = i8;
    }

    private long c(d dVar, long j8) {
        k kVar = dVar.f3750d;
        if (kVar instanceof g) {
            return j8;
        }
        int size = dVar.f3757k.size();
        long j9 = j8;
        for (int i8 = 0; i8 < size; i8++) {
            o0.a aVar = dVar.f3757k.get(i8);
            if (aVar instanceof d) {
                d dVar2 = (d) aVar;
                if (dVar2.f3750d != kVar) {
                    j9 = Math.min(j9, c(dVar2, dVar2.f3752f + j8));
                }
            }
        }
        if (dVar == kVar.f3790i) {
            long j10 = j8 - kVar.j();
            return Math.min(Math.min(j9, c(kVar.f3789h, j10)), j10 - kVar.f3789h.f3752f);
        }
        return j9;
    }

    private long d(d dVar, long j8) {
        k kVar = dVar.f3750d;
        if (kVar instanceof g) {
            return j8;
        }
        int size = dVar.f3757k.size();
        long j9 = j8;
        for (int i8 = 0; i8 < size; i8++) {
            o0.a aVar = dVar.f3757k.get(i8);
            if (aVar instanceof d) {
                d dVar2 = (d) aVar;
                if (dVar2.f3750d != kVar) {
                    j9 = Math.max(j9, d(dVar2, dVar2.f3752f + j8));
                }
            }
        }
        if (dVar == kVar.f3789h) {
            long j10 = j8 + kVar.j();
            return Math.max(Math.max(j9, d(kVar.f3790i, j10)), j10 - kVar.f3790i.f3752f);
        }
        return j9;
    }

    public void a(k kVar) {
        this.f3776e.add(kVar);
        this.f3775d = kVar;
    }

    public long b(androidx.constraintlayout.solver.widgets.d dVar, int i8) {
        long j8;
        k kVar;
        long j9;
        long j10;
        k kVar2 = this.f3774c;
        if (kVar2 instanceof b) {
            if (((b) kVar2).f3787f != i8) {
                return 0L;
            }
        } else if (i8 == 0) {
            if (!(kVar2 instanceof h)) {
                return 0L;
            }
        } else if (!(kVar2 instanceof j)) {
            return 0L;
        }
        d dVar2 = (i8 == 0 ? dVar.f3672e : dVar.f3674f).f3789h;
        d dVar3 = (i8 == 0 ? dVar.f3672e : dVar.f3674f).f3790i;
        boolean contains = kVar2.f3789h.f3758l.contains(dVar2);
        boolean contains2 = this.f3774c.f3790i.f3758l.contains(dVar3);
        long j11 = this.f3774c.j();
        if (!contains || !contains2) {
            if (contains) {
                d dVar4 = this.f3774c.f3789h;
                j10 = d(dVar4, dVar4.f3752f);
                j9 = this.f3774c.f3789h.f3752f + j11;
            } else if (contains2) {
                d dVar5 = this.f3774c.f3790i;
                long c9 = c(dVar5, dVar5.f3752f);
                j9 = (-this.f3774c.f3790i.f3752f) + j11;
                j10 = -c9;
            } else {
                k kVar3 = this.f3774c;
                j8 = kVar3.f3789h.f3752f + kVar3.j();
                kVar = this.f3774c;
            }
            return Math.max(j10, j9);
        }
        long d8 = d(this.f3774c.f3789h, 0L);
        long c10 = c(this.f3774c.f3790i, 0L);
        long j12 = d8 - j11;
        k kVar4 = this.f3774c;
        int i9 = kVar4.f3790i.f3752f;
        if (j12 >= (-i9)) {
            j12 += i9;
        }
        int i10 = kVar4.f3789h.f3752f;
        long j13 = ((-c10) - j11) - i10;
        if (j13 >= i10) {
            j13 -= i10;
        }
        float p8 = kVar4.f3783b.p(i8);
        float f5 = (float) (p8 > 0.0f ? (((float) j13) / p8) + (((float) j12) / (1.0f - p8)) : 0L);
        long j14 = (f5 * p8) + 0.5f + j11 + (f5 * (1.0f - p8)) + 0.5f;
        kVar = this.f3774c;
        j8 = kVar.f3789h.f3752f + j14;
        return j8 - kVar.f3790i.f3752f;
    }
}

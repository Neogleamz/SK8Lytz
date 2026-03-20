package z5;

import j5.n;
import j5.o;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s extends c {

    /* renamed from: h  reason: collision with root package name */
    private final int f24692h;

    /* renamed from: i  reason: collision with root package name */
    private final Object f24693i;

    public s(h5.u uVar, int i8, int i9) {
        this(uVar, i8, i9, 0, null);
    }

    public s(h5.u uVar, int i8, int i9, int i10, Object obj) {
        super(uVar, new int[]{i8}, i9);
        this.f24692h = i10;
        this.f24693i = obj;
    }

    @Override // z5.r
    public void a(long j8, long j9, long j10, List<? extends n> list, o[] oVarArr) {
    }

    @Override // z5.r
    public int c() {
        return 0;
    }

    @Override // z5.r
    public int o() {
        return this.f24692h;
    }

    @Override // z5.r
    public Object r() {
        return this.f24693i;
    }
}

package androidx.room;

import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h0 implements t1.d {

    /* renamed from: a  reason: collision with root package name */
    private List<Object> f7130a = new ArrayList();

    private void b(int i8, Object obj) {
        int i9 = i8 - 1;
        if (i9 >= this.f7130a.size()) {
            for (int size = this.f7130a.size(); size <= i9; size++) {
                this.f7130a.add(null);
            }
        }
        this.f7130a.set(i9, obj);
    }

    @Override // t1.d
    public void I(int i8, String str) {
        b(i8, str);
    }

    @Override // t1.d
    public void Q(int i8, double d8) {
        b(i8, Double.valueOf(d8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Object> a() {
        return this.f7130a;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // t1.d
    public void h0(int i8, long j8) {
        b(i8, Long.valueOf(j8));
    }

    @Override // t1.d
    public void o0(int i8, byte[] bArr) {
        b(i8, bArr);
    }

    @Override // t1.d
    public void o1(int i8) {
        b(i8, null);
    }
}

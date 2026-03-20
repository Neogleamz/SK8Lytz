package s2;

import p2.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a {

    /* renamed from: a  reason: collision with root package name */
    private int f22771a = 1;

    /* renamed from: b  reason: collision with root package name */
    private boolean f22772b = false;

    private void i(d dVar, boolean z4) {
        int c9 = c();
        if (c9 != 0) {
            dVar.O(c9, z4);
        }
    }

    private void j(d dVar, boolean z4) {
        dVar.O(d(), z4);
    }

    private void k(d dVar, boolean z4) {
        dVar.O(f(), z4);
    }

    public void a(d dVar) {
        int i8 = this.f22771a;
        if (i8 == 1) {
            k(dVar, false);
        } else if (i8 != 2) {
            if (i8 == 3) {
                k(dVar, false);
                j(dVar, true);
                i(dVar, false);
            } else if (i8 != 4) {
                return;
            } else {
                k(dVar, false);
                j(dVar, false);
                i(dVar, true);
                return;
            }
        } else {
            k(dVar, true);
        }
        j(dVar, false);
        i(dVar, false);
    }

    public abstract int b();

    protected abstract int c();

    protected abstract int d();

    public int e() {
        return this.f22771a;
    }

    protected abstract int f();

    public final boolean g() {
        if (c() == 0) {
            return true;
        }
        return this.f22772b;
    }

    public void h(int i8) {
        this.f22771a = i8;
    }
}

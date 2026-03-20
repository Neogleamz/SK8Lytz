package r;

import androidx.camera.core.impl.f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import y.n0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends n0<b> {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final List<b> f22640a = new ArrayList();

        a(List<b> list) {
            for (b bVar : list) {
                this.f22640a.add(bVar);
            }
        }

        public void a() {
            for (b bVar : this.f22640a) {
                bVar.a();
            }
        }

        public List<f> b() {
            ArrayList arrayList = new ArrayList();
            for (b bVar : this.f22640a) {
                f b9 = bVar.b();
                if (b9 != null) {
                    arrayList.add(b9);
                }
            }
            return arrayList;
        }

        public List<f> c() {
            ArrayList arrayList = new ArrayList();
            for (b bVar : this.f22640a) {
                f c9 = bVar.c();
                if (c9 != null) {
                    arrayList.add(c9);
                }
            }
            return arrayList;
        }

        public List<f> d() {
            ArrayList arrayList = new ArrayList();
            for (b bVar : this.f22640a) {
                f d8 = bVar.d();
                if (d8 != null) {
                    arrayList.add(d8);
                }
            }
            return arrayList;
        }

        public List<f> e() {
            ArrayList arrayList = new ArrayList();
            for (b bVar : this.f22640a) {
                f e8 = bVar.e();
                if (e8 != null) {
                    arrayList.add(e8);
                }
            }
            return arrayList;
        }
    }

    public c(b... bVarArr) {
        a(Arrays.asList(bVarArr));
    }

    public static c e() {
        return new c(new b[0]);
    }

    @Override // y.n0
    /* renamed from: b */
    public n0<b> clone() {
        c e8 = e();
        e8.a(c());
        return e8;
    }

    public a d() {
        return new a(c());
    }
}

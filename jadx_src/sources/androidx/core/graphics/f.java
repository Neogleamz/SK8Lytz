package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.core.content.res.e;
import androidx.core.content.res.h;
import androidx.core.provider.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f {

    /* renamed from: a  reason: collision with root package name */
    private static final l f4744a;

    /* renamed from: b  reason: collision with root package name */
    private static final k0.e<String, Typeface> f4745b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends g.c {

        /* renamed from: a  reason: collision with root package name */
        private h.f f4746a;

        public a(h.f fVar) {
            this.f4746a = fVar;
        }

        @Override // androidx.core.provider.g.c
        public void a(int i8) {
            h.f fVar = this.f4746a;
            if (fVar != null) {
                fVar.f(i8);
            }
        }

        @Override // androidx.core.provider.g.c
        public void b(Typeface typeface) {
            h.f fVar = this.f4746a;
            if (fVar != null) {
                fVar.g(typeface);
            }
        }
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        f4744a = i8 >= 29 ? new k() : i8 >= 28 ? new j() : i8 >= 26 ? new i() : (i8 < 24 || !h.m()) ? i8 >= 21 ? new g() : new l() : new h();
        f4745b = new k0.e<>(16);
    }

    public static Typeface a(Context context, Typeface typeface, int i8) {
        Typeface g8;
        if (context != null) {
            return (Build.VERSION.SDK_INT >= 21 || (g8 = g(context, typeface, i8)) == null) ? Typeface.create(typeface, i8) : g8;
        }
        throw new IllegalArgumentException("Context cannot be null");
    }

    public static Typeface b(Context context, CancellationSignal cancellationSignal, g.b[] bVarArr, int i8) {
        return f4744a.c(context, cancellationSignal, bVarArr, i8);
    }

    public static Typeface c(Context context, e.b bVar, Resources resources, int i8, String str, int i9, int i10, h.f fVar, Handler handler, boolean z4) {
        Typeface b9;
        if (bVar instanceof e.C0033e) {
            e.C0033e c0033e = (e.C0033e) bVar;
            Typeface h8 = h(c0033e.c());
            if (h8 != null) {
                if (fVar != null) {
                    fVar.d(h8, handler);
                }
                return h8;
            }
            boolean z8 = !z4 ? fVar != null : c0033e.a() != 0;
            int d8 = z4 ? c0033e.d() : -1;
            b9 = androidx.core.provider.g.c(context, c0033e.b(), i10, z8, d8, h.f.e(handler), new a(fVar));
        } else {
            b9 = f4744a.b(context, (e.c) bVar, resources, i10);
            if (fVar != null) {
                if (b9 != null) {
                    fVar.d(b9, handler);
                } else {
                    fVar.c(-3, handler);
                }
            }
        }
        if (b9 != null) {
            f4745b.d(e(resources, i8, str, i9, i10), b9);
        }
        return b9;
    }

    public static Typeface d(Context context, Resources resources, int i8, String str, int i9, int i10) {
        Typeface e8 = f4744a.e(context, resources, i8, str, i10);
        if (e8 != null) {
            f4745b.d(e(resources, i8, str, i9, i10), e8);
        }
        return e8;
    }

    private static String e(Resources resources, int i8, String str, int i9, int i10) {
        return resources.getResourcePackageName(i8) + '-' + str + '-' + i9 + '-' + i8 + '-' + i10;
    }

    public static Typeface f(Resources resources, int i8, String str, int i9, int i10) {
        return f4745b.c(e(resources, i8, str, i9, i10));
    }

    private static Typeface g(Context context, Typeface typeface, int i8) {
        l lVar = f4744a;
        e.c i9 = lVar.i(typeface);
        if (i9 == null) {
            return null;
        }
        return lVar.b(context, i9, context.getResources(), i8);
    }

    private static Typeface h(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        Typeface create = Typeface.create(str, 0);
        Typeface create2 = Typeface.create(Typeface.DEFAULT, 0);
        if (create == null || create.equals(create2)) {
            return null;
        }
        return create;
    }
}

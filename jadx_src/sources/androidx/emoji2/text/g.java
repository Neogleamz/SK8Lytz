package androidx.emoji2.text;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g {

    /* renamed from: d  reason: collision with root package name */
    private static final ThreadLocal<z0.a> f5257d = new ThreadLocal<>();

    /* renamed from: a  reason: collision with root package name */
    private final int f5258a;

    /* renamed from: b  reason: collision with root package name */
    private final m f5259b;

    /* renamed from: c  reason: collision with root package name */
    private volatile int f5260c = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(m mVar, int i8) {
        this.f5259b = mVar;
        this.f5258a = i8;
    }

    private z0.a g() {
        ThreadLocal<z0.a> threadLocal = f5257d;
        z0.a aVar = threadLocal.get();
        if (aVar == null) {
            aVar = new z0.a();
            threadLocal.set(aVar);
        }
        this.f5259b.d().j(aVar, this.f5258a);
        return aVar;
    }

    public void a(Canvas canvas, float f5, float f8, Paint paint) {
        Typeface g8 = this.f5259b.g();
        Typeface typeface = paint.getTypeface();
        paint.setTypeface(g8);
        canvas.drawText(this.f5259b.c(), this.f5258a * 2, 2, f5, f8, paint);
        paint.setTypeface(typeface);
    }

    public int b(int i8) {
        return g().h(i8);
    }

    public int c() {
        return g().i();
    }

    @SuppressLint({"KotlinPropertyAccess"})
    public int d() {
        return this.f5260c;
    }

    public short e() {
        return g().k();
    }

    public int f() {
        return g().l();
    }

    public short h() {
        return g().m();
    }

    public short i() {
        return g().n();
    }

    public boolean j() {
        return g().j();
    }

    @SuppressLint({"KotlinPropertyAccess"})
    public void k(boolean z4) {
        this.f5260c = z4 ? 2 : 1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(", id:");
        sb.append(Integer.toHexString(f()));
        sb.append(", codepoints:");
        int c9 = c();
        for (int i8 = 0; i8 < c9; i8++) {
            sb.append(Integer.toHexString(b(i8)));
            sb.append(" ");
        }
        return sb.toString();
    }
}

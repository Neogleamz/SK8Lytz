package androidx.core.graphics;

import android.graphics.Insets;
import android.graphics.Rect;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: e  reason: collision with root package name */
    public static final c f4707e = new c(0, 0, 0, 0);

    /* renamed from: a  reason: collision with root package name */
    public final int f4708a;

    /* renamed from: b  reason: collision with root package name */
    public final int f4709b;

    /* renamed from: c  reason: collision with root package name */
    public final int f4710c;

    /* renamed from: d  reason: collision with root package name */
    public final int f4711d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static Insets a(int i8, int i9, int i10, int i11) {
            return Insets.of(i8, i9, i10, i11);
        }
    }

    private c(int i8, int i9, int i10, int i11) {
        this.f4708a = i8;
        this.f4709b = i9;
        this.f4710c = i10;
        this.f4711d = i11;
    }

    public static c a(c cVar, c cVar2) {
        return b(Math.max(cVar.f4708a, cVar2.f4708a), Math.max(cVar.f4709b, cVar2.f4709b), Math.max(cVar.f4710c, cVar2.f4710c), Math.max(cVar.f4711d, cVar2.f4711d));
    }

    public static c b(int i8, int i9, int i10, int i11) {
        return (i8 == 0 && i9 == 0 && i10 == 0 && i11 == 0) ? f4707e : new c(i8, i9, i10, i11);
    }

    public static c c(Rect rect) {
        return b(rect.left, rect.top, rect.right, rect.bottom);
    }

    public static c d(Insets insets) {
        return b(insets.left, insets.top, insets.right, insets.bottom);
    }

    public Insets e() {
        return a.a(this.f4708a, this.f4709b, this.f4710c, this.f4711d);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || c.class != obj.getClass()) {
            return false;
        }
        c cVar = (c) obj;
        return this.f4711d == cVar.f4711d && this.f4708a == cVar.f4708a && this.f4710c == cVar.f4710c && this.f4709b == cVar.f4709b;
    }

    public int hashCode() {
        return (((((this.f4708a * 31) + this.f4709b) * 31) + this.f4710c) * 31) + this.f4711d;
    }

    public String toString() {
        return "Insets{left=" + this.f4708a + ", top=" + this.f4709b + ", right=" + this.f4710c + ", bottom=" + this.f4711d + '}';
    }
}

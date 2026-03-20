package c6;

import android.os.Bundle;
import b6.l0;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x implements com.google.android.exoplayer2.g {

    /* renamed from: e  reason: collision with root package name */
    public static final x f8455e = new x(0, 0);

    /* renamed from: f  reason: collision with root package name */
    private static final String f8456f = l0.r0(0);

    /* renamed from: g  reason: collision with root package name */
    private static final String f8457g = l0.r0(1);

    /* renamed from: h  reason: collision with root package name */
    private static final String f8458h = l0.r0(2);

    /* renamed from: j  reason: collision with root package name */
    private static final String f8459j = l0.r0(3);

    /* renamed from: k  reason: collision with root package name */
    public static final g.a<x> f8460k = new g.a() { // from class: c6.w
        @Override // com.google.android.exoplayer2.g.a
        public final com.google.android.exoplayer2.g a(Bundle bundle) {
            x b9;
            b9 = x.b(bundle);
            return b9;
        }
    };

    /* renamed from: a  reason: collision with root package name */
    public final int f8461a;

    /* renamed from: b  reason: collision with root package name */
    public final int f8462b;

    /* renamed from: c  reason: collision with root package name */
    public final int f8463c;

    /* renamed from: d  reason: collision with root package name */
    public final float f8464d;

    public x(int i8, int i9) {
        this(i8, i9, 0, 1.0f);
    }

    public x(int i8, int i9, int i10, float f5) {
        this.f8461a = i8;
        this.f8462b = i9;
        this.f8463c = i10;
        this.f8464d = f5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ x b(Bundle bundle) {
        return new x(bundle.getInt(f8456f, 0), bundle.getInt(f8457g, 0), bundle.getInt(f8458h, 0), bundle.getFloat(f8459j, 1.0f));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof x) {
            x xVar = (x) obj;
            return this.f8461a == xVar.f8461a && this.f8462b == xVar.f8462b && this.f8463c == xVar.f8463c && this.f8464d == xVar.f8464d;
        }
        return false;
    }

    public int hashCode() {
        return ((((((217 + this.f8461a) * 31) + this.f8462b) * 31) + this.f8463c) * 31) + Float.floatToRawIntBits(this.f8464d);
    }
}

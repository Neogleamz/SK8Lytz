package c6;

import android.os.Bundle;
import b6.l0;
import com.google.android.exoplayer2.g;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements com.google.android.exoplayer2.g {

    /* renamed from: f  reason: collision with root package name */
    public static final c f8332f = new c(1, 2, 3, null);

    /* renamed from: g  reason: collision with root package name */
    private static final String f8333g = l0.r0(0);

    /* renamed from: h  reason: collision with root package name */
    private static final String f8334h = l0.r0(1);

    /* renamed from: j  reason: collision with root package name */
    private static final String f8335j = l0.r0(2);

    /* renamed from: k  reason: collision with root package name */
    private static final String f8336k = l0.r0(3);

    /* renamed from: l  reason: collision with root package name */
    public static final g.a<c> f8337l = new g.a() { // from class: c6.b
        @Override // com.google.android.exoplayer2.g.a
        public final com.google.android.exoplayer2.g a(Bundle bundle) {
            c d8;
            d8 = c.d(bundle);
            return d8;
        }
    };

    /* renamed from: a  reason: collision with root package name */
    public final int f8338a;

    /* renamed from: b  reason: collision with root package name */
    public final int f8339b;

    /* renamed from: c  reason: collision with root package name */
    public final int f8340c;

    /* renamed from: d  reason: collision with root package name */
    public final byte[] f8341d;

    /* renamed from: e  reason: collision with root package name */
    private int f8342e;

    public c(int i8, int i9, int i10, byte[] bArr) {
        this.f8338a = i8;
        this.f8339b = i9;
        this.f8340c = i10;
        this.f8341d = bArr;
    }

    public static int b(int i8) {
        if (i8 != 1) {
            if (i8 != 9) {
                return (i8 == 4 || i8 == 5 || i8 == 6 || i8 == 7) ? 2 : -1;
            }
            return 6;
        }
        return 1;
    }

    public static int c(int i8) {
        if (i8 != 1) {
            if (i8 != 16) {
                if (i8 != 18) {
                    return (i8 == 6 || i8 == 7) ? 3 : -1;
                }
                return 7;
            }
            return 6;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ c d(Bundle bundle) {
        return new c(bundle.getInt(f8333g, -1), bundle.getInt(f8334h, -1), bundle.getInt(f8335j, -1), bundle.getByteArray(f8336k));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || c.class != obj.getClass()) {
            return false;
        }
        c cVar = (c) obj;
        return this.f8338a == cVar.f8338a && this.f8339b == cVar.f8339b && this.f8340c == cVar.f8340c && Arrays.equals(this.f8341d, cVar.f8341d);
    }

    public int hashCode() {
        if (this.f8342e == 0) {
            this.f8342e = ((((((527 + this.f8338a) * 31) + this.f8339b) * 31) + this.f8340c) * 31) + Arrays.hashCode(this.f8341d);
        }
        return this.f8342e;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ColorInfo(");
        sb.append(this.f8338a);
        sb.append(", ");
        sb.append(this.f8339b);
        sb.append(", ");
        sb.append(this.f8340c);
        sb.append(", ");
        sb.append(this.f8341d != null);
        sb.append(")");
        return sb.toString();
    }
}

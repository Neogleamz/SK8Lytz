package androidx.emoji2.text;

import android.graphics.Typeface;
import android.util.SparseArray;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m {

    /* renamed from: a  reason: collision with root package name */
    private final z0.b f5294a;

    /* renamed from: b  reason: collision with root package name */
    private final char[] f5295b;

    /* renamed from: c  reason: collision with root package name */
    private final a f5296c = new a(RecognitionOptions.UPC_E);

    /* renamed from: d  reason: collision with root package name */
    private final Typeface f5297d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private final SparseArray<a> f5298a;

        /* renamed from: b  reason: collision with root package name */
        private g f5299b;

        private a() {
            this(1);
        }

        a(int i8) {
            this.f5298a = new SparseArray<>(i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a a(int i8) {
            SparseArray<a> sparseArray = this.f5298a;
            if (sparseArray == null) {
                return null;
            }
            return sparseArray.get(i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final g b() {
            return this.f5299b;
        }

        void c(g gVar, int i8, int i9) {
            a a9 = a(gVar.b(i8));
            if (a9 == null) {
                a9 = new a();
                this.f5298a.put(gVar.b(i8), a9);
            }
            if (i9 > i8) {
                a9.c(gVar, i8 + 1, i9);
            } else {
                a9.f5299b = gVar;
            }
        }
    }

    private m(Typeface typeface, z0.b bVar) {
        this.f5297d = typeface;
        this.f5294a = bVar;
        this.f5295b = new char[bVar.k() * 2];
        a(bVar);
    }

    private void a(z0.b bVar) {
        int k8 = bVar.k();
        for (int i8 = 0; i8 < k8; i8++) {
            g gVar = new g(this, i8);
            Character.toChars(gVar.f(), this.f5295b, i8 * 2);
            h(gVar);
        }
    }

    public static m b(Typeface typeface, ByteBuffer byteBuffer) {
        try {
            androidx.core.os.o.a("EmojiCompat.MetadataRepo.create");
            return new m(typeface, l.b(byteBuffer));
        } finally {
            androidx.core.os.o.b();
        }
    }

    public char[] c() {
        return this.f5295b;
    }

    public z0.b d() {
        return this.f5294a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int e() {
        return this.f5294a.l();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a f() {
        return this.f5296c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Typeface g() {
        return this.f5297d;
    }

    void h(g gVar) {
        androidx.core.util.h.i(gVar, "emoji metadata cannot be null");
        androidx.core.util.h.b(gVar.c() > 0, "invalid metadata codepoint length");
        this.f5296c.c(gVar, 0, gVar.c() - 1);
    }
}

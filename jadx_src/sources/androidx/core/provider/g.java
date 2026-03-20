package androidx.core.provider;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private final int f4820a;

        /* renamed from: b  reason: collision with root package name */
        private final b[] f4821b;

        @Deprecated
        public a(int i8, b[] bVarArr) {
            this.f4820a = i8;
            this.f4821b = bVarArr;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static a a(int i8, b[] bVarArr) {
            return new a(i8, bVarArr);
        }

        public b[] b() {
            return this.f4821b;
        }

        public int c() {
            return this.f4820a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        private final Uri f4822a;

        /* renamed from: b  reason: collision with root package name */
        private final int f4823b;

        /* renamed from: c  reason: collision with root package name */
        private final int f4824c;

        /* renamed from: d  reason: collision with root package name */
        private final boolean f4825d;

        /* renamed from: e  reason: collision with root package name */
        private final int f4826e;

        @Deprecated
        public b(Uri uri, int i8, int i9, boolean z4, int i10) {
            this.f4822a = (Uri) androidx.core.util.h.h(uri);
            this.f4823b = i8;
            this.f4824c = i9;
            this.f4825d = z4;
            this.f4826e = i10;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static b a(Uri uri, int i8, int i9, boolean z4, int i10) {
            return new b(uri, i8, i9, z4, i10);
        }

        public int b() {
            return this.f4826e;
        }

        public int c() {
            return this.f4823b;
        }

        public Uri d() {
            return this.f4822a;
        }

        public int e() {
            return this.f4824c;
        }

        public boolean f() {
            return this.f4825d;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {
        public void a(int i8) {
            throw null;
        }

        public void b(Typeface typeface) {
            throw null;
        }
    }

    public static Typeface a(Context context, CancellationSignal cancellationSignal, b[] bVarArr) {
        return androidx.core.graphics.f.b(context, cancellationSignal, bVarArr, 0);
    }

    public static a b(Context context, CancellationSignal cancellationSignal, e eVar) {
        return d.e(context, eVar, cancellationSignal);
    }

    public static Typeface c(Context context, e eVar, int i8, boolean z4, int i9, Handler handler, c cVar) {
        androidx.core.provider.a aVar = new androidx.core.provider.a(cVar, handler);
        return z4 ? f.e(context, eVar, aVar, i8, i9) : f.d(context, eVar, i8, null, aVar);
    }
}

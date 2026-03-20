package k1;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseBooleanArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: f  reason: collision with root package name */
    static final c f20926f = new a();

    /* renamed from: a  reason: collision with root package name */
    private final List<e> f20927a;

    /* renamed from: b  reason: collision with root package name */
    private final List<k1.c> f20928b;

    /* renamed from: d  reason: collision with root package name */
    private final SparseBooleanArray f20930d = new SparseBooleanArray();

    /* renamed from: c  reason: collision with root package name */
    private final Map<k1.c, e> f20929c = new k0.a();

    /* renamed from: e  reason: collision with root package name */
    private final e f20931e = a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements c {
        a() {
        }

        private boolean b(float[] fArr) {
            return fArr[2] <= 0.05f;
        }

        private boolean c(float[] fArr) {
            return fArr[0] >= 10.0f && fArr[0] <= 37.0f && fArr[1] <= 0.82f;
        }

        private boolean d(float[] fArr) {
            return fArr[2] >= 0.95f;
        }

        @Override // k1.b.c
        public boolean a(int i8, float[] fArr) {
            return (d(fArr) || b(fArr) || c(fArr)) ? false : true;
        }
    }

    /* renamed from: k1.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0180b {

        /* renamed from: a  reason: collision with root package name */
        private final List<e> f20932a;

        /* renamed from: b  reason: collision with root package name */
        private final Bitmap f20933b;

        /* renamed from: c  reason: collision with root package name */
        private final List<k1.c> f20934c;

        /* renamed from: d  reason: collision with root package name */
        private int f20935d;

        /* renamed from: e  reason: collision with root package name */
        private int f20936e;

        /* renamed from: f  reason: collision with root package name */
        private int f20937f;

        /* renamed from: g  reason: collision with root package name */
        private final List<c> f20938g;

        /* renamed from: h  reason: collision with root package name */
        private Rect f20939h;

        /* renamed from: k1.b$b$a */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends AsyncTask<Bitmap, Void, b> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ d f20940a;

            a(d dVar) {
                this.f20940a = dVar;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            /* renamed from: a */
            public b doInBackground(Bitmap... bitmapArr) {
                try {
                    return C0180b.this.b();
                } catch (Exception e8) {
                    Log.e("Palette", "Exception thrown during async generate", e8);
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            /* renamed from: b */
            public void onPostExecute(b bVar) {
                this.f20940a.a(bVar);
            }
        }

        public C0180b(Bitmap bitmap) {
            ArrayList arrayList = new ArrayList();
            this.f20934c = arrayList;
            this.f20935d = 16;
            this.f20936e = 12544;
            this.f20937f = -1;
            ArrayList arrayList2 = new ArrayList();
            this.f20938g = arrayList2;
            if (bitmap == null || bitmap.isRecycled()) {
                throw new IllegalArgumentException("Bitmap is not valid");
            }
            arrayList2.add(b.f20926f);
            this.f20933b = bitmap;
            this.f20932a = null;
            arrayList.add(k1.c.f20951e);
            arrayList.add(k1.c.f20952f);
            arrayList.add(k1.c.f20953g);
            arrayList.add(k1.c.f20954h);
            arrayList.add(k1.c.f20955i);
            arrayList.add(k1.c.f20956j);
        }

        private int[] c(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] iArr = new int[width * height];
            bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
            Rect rect = this.f20939h;
            if (rect == null) {
                return iArr;
            }
            int width2 = rect.width();
            int height2 = this.f20939h.height();
            int[] iArr2 = new int[width2 * height2];
            for (int i8 = 0; i8 < height2; i8++) {
                Rect rect2 = this.f20939h;
                System.arraycopy(iArr, ((rect2.top + i8) * width) + rect2.left, iArr2, i8 * width2, width2);
            }
            return iArr2;
        }

        private Bitmap e(Bitmap bitmap) {
            int max;
            int i8;
            double d8 = -1.0d;
            if (this.f20936e > 0) {
                int width = bitmap.getWidth() * bitmap.getHeight();
                int i9 = this.f20936e;
                if (width > i9) {
                    d8 = Math.sqrt(i9 / width);
                }
            } else if (this.f20937f > 0 && (max = Math.max(bitmap.getWidth(), bitmap.getHeight())) > (i8 = this.f20937f)) {
                d8 = i8 / max;
            }
            return d8 <= 0.0d ? bitmap : Bitmap.createScaledBitmap(bitmap, (int) Math.ceil(bitmap.getWidth() * d8), (int) Math.ceil(bitmap.getHeight() * d8), false);
        }

        public AsyncTask<Bitmap, Void, b> a(d dVar) {
            if (dVar != null) {
                return new a(dVar).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.f20933b);
            }
            throw new IllegalArgumentException("listener can not be null");
        }

        public b b() {
            List<e> list;
            c[] cVarArr;
            Bitmap bitmap = this.f20933b;
            if (bitmap != null) {
                Bitmap e8 = e(bitmap);
                Rect rect = this.f20939h;
                if (e8 != this.f20933b && rect != null) {
                    double width = e8.getWidth() / this.f20933b.getWidth();
                    rect.left = (int) Math.floor(rect.left * width);
                    rect.top = (int) Math.floor(rect.top * width);
                    rect.right = Math.min((int) Math.ceil(rect.right * width), e8.getWidth());
                    rect.bottom = Math.min((int) Math.ceil(rect.bottom * width), e8.getHeight());
                }
                int[] c9 = c(e8);
                int i8 = this.f20935d;
                if (this.f20938g.isEmpty()) {
                    cVarArr = null;
                } else {
                    List<c> list2 = this.f20938g;
                    cVarArr = (c[]) list2.toArray(new c[list2.size()]);
                }
                k1.a aVar = new k1.a(c9, i8, cVarArr);
                if (e8 != this.f20933b) {
                    e8.recycle();
                }
                list = aVar.d();
            } else {
                list = this.f20932a;
                if (list == null) {
                    throw new AssertionError();
                }
            }
            b bVar = new b(list, this.f20934c);
            bVar.c();
            return bVar;
        }

        public C0180b d(int i8) {
            this.f20935d = i8;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        boolean a(int i8, float[] fArr);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void a(b bVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        private final int f20942a;

        /* renamed from: b  reason: collision with root package name */
        private final int f20943b;

        /* renamed from: c  reason: collision with root package name */
        private final int f20944c;

        /* renamed from: d  reason: collision with root package name */
        private final int f20945d;

        /* renamed from: e  reason: collision with root package name */
        private final int f20946e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f20947f;

        /* renamed from: g  reason: collision with root package name */
        private int f20948g;

        /* renamed from: h  reason: collision with root package name */
        private int f20949h;

        /* renamed from: i  reason: collision with root package name */
        private float[] f20950i;

        public e(int i8, int i9) {
            this.f20942a = Color.red(i8);
            this.f20943b = Color.green(i8);
            this.f20944c = Color.blue(i8);
            this.f20945d = i8;
            this.f20946e = i9;
        }

        private void a() {
            int p8;
            if (this.f20947f) {
                return;
            }
            int g8 = androidx.core.graphics.b.g(-1, this.f20945d, 4.5f);
            int g9 = androidx.core.graphics.b.g(-1, this.f20945d, 3.0f);
            if (g8 == -1 || g9 == -1) {
                int g10 = androidx.core.graphics.b.g(-16777216, this.f20945d, 4.5f);
                int g11 = androidx.core.graphics.b.g(-16777216, this.f20945d, 3.0f);
                if (g10 == -1 || g11 == -1) {
                    this.f20949h = g8 != -1 ? androidx.core.graphics.b.p(-1, g8) : androidx.core.graphics.b.p(-16777216, g10);
                    this.f20948g = g9 != -1 ? androidx.core.graphics.b.p(-1, g9) : androidx.core.graphics.b.p(-16777216, g11);
                    this.f20947f = true;
                    return;
                }
                this.f20949h = androidx.core.graphics.b.p(-16777216, g10);
                p8 = androidx.core.graphics.b.p(-16777216, g11);
            } else {
                this.f20949h = androidx.core.graphics.b.p(-1, g8);
                p8 = androidx.core.graphics.b.p(-1, g9);
            }
            this.f20948g = p8;
            this.f20947f = true;
        }

        public int b() {
            a();
            return this.f20949h;
        }

        public float[] c() {
            if (this.f20950i == null) {
                this.f20950i = new float[3];
            }
            androidx.core.graphics.b.b(this.f20942a, this.f20943b, this.f20944c, this.f20950i);
            return this.f20950i;
        }

        public int d() {
            return this.f20946e;
        }

        public int e() {
            return this.f20945d;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || e.class != obj.getClass()) {
                return false;
            }
            e eVar = (e) obj;
            return this.f20946e == eVar.f20946e && this.f20945d == eVar.f20945d;
        }

        public int f() {
            a();
            return this.f20948g;
        }

        public int hashCode() {
            return (this.f20945d * 31) + this.f20946e;
        }

        public String toString() {
            return e.class.getSimpleName() + " [RGB: #" + Integer.toHexString(e()) + "] [HSL: " + Arrays.toString(c()) + "] [Population: " + this.f20946e + "] [Title Text: #" + Integer.toHexString(f()) + "] [Body Text: #" + Integer.toHexString(b()) + ']';
        }
    }

    b(List<e> list, List<k1.c> list2) {
        this.f20927a = list;
        this.f20928b = list2;
    }

    private e a() {
        int size = this.f20927a.size();
        int i8 = Integer.MIN_VALUE;
        e eVar = null;
        for (int i9 = 0; i9 < size; i9++) {
            e eVar2 = this.f20927a.get(i9);
            if (eVar2.d() > i8) {
                i8 = eVar2.d();
                eVar = eVar2;
            }
        }
        return eVar;
    }

    public static C0180b b(Bitmap bitmap) {
        return new C0180b(bitmap);
    }

    private float d(e eVar, k1.c cVar) {
        float[] c9 = eVar.c();
        e eVar2 = this.f20931e;
        int d8 = eVar2 != null ? eVar2.d() : 1;
        return (cVar.g() > 0.0f ? cVar.g() * (1.0f - Math.abs(c9[1] - cVar.i())) : 0.0f) + (cVar.a() > 0.0f ? cVar.a() * (1.0f - Math.abs(c9[2] - cVar.h())) : 0.0f) + (cVar.f() > 0.0f ? cVar.f() * (eVar.d() / d8) : 0.0f);
    }

    private e e(k1.c cVar) {
        e j8 = j(cVar);
        if (j8 != null && cVar.j()) {
            this.f20930d.append(j8.e(), true);
        }
        return j8;
    }

    private e j(k1.c cVar) {
        int size = this.f20927a.size();
        float f5 = 0.0f;
        e eVar = null;
        for (int i8 = 0; i8 < size; i8++) {
            e eVar2 = this.f20927a.get(i8);
            if (n(eVar2, cVar)) {
                float d8 = d(eVar2, cVar);
                if (eVar == null || d8 > f5) {
                    eVar = eVar2;
                    f5 = d8;
                }
            }
        }
        return eVar;
    }

    private boolean n(e eVar, k1.c cVar) {
        float[] c9 = eVar.c();
        return c9[1] >= cVar.e() && c9[1] <= cVar.c() && c9[2] >= cVar.d() && c9[2] <= cVar.b() && !this.f20930d.get(eVar.e());
    }

    void c() {
        int size = this.f20928b.size();
        for (int i8 = 0; i8 < size; i8++) {
            k1.c cVar = this.f20928b.get(i8);
            cVar.k();
            this.f20929c.put(cVar, e(cVar));
        }
        this.f20930d.clear();
    }

    public int f(k1.c cVar, int i8) {
        e l8 = l(cVar);
        return l8 != null ? l8.e() : i8;
    }

    public int g(int i8) {
        e eVar = this.f20931e;
        return eVar != null ? eVar.e() : i8;
    }

    public int h(int i8) {
        return f(k1.c.f20954h, i8);
    }

    public int i(int i8) {
        return f(k1.c.f20951e, i8);
    }

    public int k(int i8) {
        return f(k1.c.f20955i, i8);
    }

    public e l(k1.c cVar) {
        return this.f20929c.get(cVar);
    }

    public int m(int i8) {
        return f(k1.c.f20952f, i8);
    }
}

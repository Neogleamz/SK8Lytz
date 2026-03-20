package k1;

import android.graphics.Color;
import android.util.TimingLogger;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import k1.b;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: g  reason: collision with root package name */
    private static final Comparator<b> f20909g = new C0179a();

    /* renamed from: a  reason: collision with root package name */
    final int[] f20910a;

    /* renamed from: b  reason: collision with root package name */
    final int[] f20911b;

    /* renamed from: c  reason: collision with root package name */
    final List<b.e> f20912c;

    /* renamed from: e  reason: collision with root package name */
    final b.c[] f20914e;

    /* renamed from: f  reason: collision with root package name */
    private final float[] f20915f = new float[3];

    /* renamed from: d  reason: collision with root package name */
    final TimingLogger f20913d = null;

    /* renamed from: k1.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class C0179a implements Comparator<b> {
        C0179a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(b bVar, b bVar2) {
            return bVar2.g() - bVar.g();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b {

        /* renamed from: a  reason: collision with root package name */
        private int f20916a;

        /* renamed from: b  reason: collision with root package name */
        private int f20917b;

        /* renamed from: c  reason: collision with root package name */
        private int f20918c;

        /* renamed from: d  reason: collision with root package name */
        private int f20919d;

        /* renamed from: e  reason: collision with root package name */
        private int f20920e;

        /* renamed from: f  reason: collision with root package name */
        private int f20921f;

        /* renamed from: g  reason: collision with root package name */
        private int f20922g;

        /* renamed from: h  reason: collision with root package name */
        private int f20923h;

        /* renamed from: i  reason: collision with root package name */
        private int f20924i;

        b(int i8, int i9) {
            this.f20916a = i8;
            this.f20917b = i9;
            c();
        }

        final boolean a() {
            return e() > 1;
        }

        final int b() {
            int f5 = f();
            a aVar = a.this;
            int[] iArr = aVar.f20910a;
            int[] iArr2 = aVar.f20911b;
            a.e(iArr, f5, this.f20916a, this.f20917b);
            Arrays.sort(iArr, this.f20916a, this.f20917b + 1);
            a.e(iArr, f5, this.f20916a, this.f20917b);
            int i8 = this.f20918c / 2;
            int i9 = this.f20916a;
            int i10 = 0;
            while (true) {
                int i11 = this.f20917b;
                if (i9 > i11) {
                    return this.f20916a;
                }
                i10 += iArr2[iArr[i9]];
                if (i10 >= i8) {
                    return Math.min(i11 - 1, i9);
                }
                i9++;
            }
        }

        final void c() {
            a aVar = a.this;
            int[] iArr = aVar.f20910a;
            int[] iArr2 = aVar.f20911b;
            int i8 = Integer.MAX_VALUE;
            int i9 = Integer.MIN_VALUE;
            int i10 = Integer.MIN_VALUE;
            int i11 = Integer.MIN_VALUE;
            int i12 = 0;
            int i13 = Integer.MAX_VALUE;
            int i14 = Integer.MAX_VALUE;
            for (int i15 = this.f20916a; i15 <= this.f20917b; i15++) {
                int i16 = iArr[i15];
                i12 += iArr2[i16];
                int k8 = a.k(i16);
                int j8 = a.j(i16);
                int i17 = a.i(i16);
                if (k8 > i9) {
                    i9 = k8;
                }
                if (k8 < i8) {
                    i8 = k8;
                }
                if (j8 > i10) {
                    i10 = j8;
                }
                if (j8 < i13) {
                    i13 = j8;
                }
                if (i17 > i11) {
                    i11 = i17;
                }
                if (i17 < i14) {
                    i14 = i17;
                }
            }
            this.f20919d = i8;
            this.f20920e = i9;
            this.f20921f = i13;
            this.f20922g = i10;
            this.f20923h = i14;
            this.f20924i = i11;
            this.f20918c = i12;
        }

        final b.e d() {
            a aVar = a.this;
            int[] iArr = aVar.f20910a;
            int[] iArr2 = aVar.f20911b;
            int i8 = 0;
            int i9 = 0;
            int i10 = 0;
            int i11 = 0;
            for (int i12 = this.f20916a; i12 <= this.f20917b; i12++) {
                int i13 = iArr[i12];
                int i14 = iArr2[i13];
                i9 += i14;
                i8 += a.k(i13) * i14;
                i10 += a.j(i13) * i14;
                i11 += i14 * a.i(i13);
            }
            float f5 = i9;
            return new b.e(a.b(Math.round(i8 / f5), Math.round(i10 / f5), Math.round(i11 / f5)), i9);
        }

        final int e() {
            return (this.f20917b + 1) - this.f20916a;
        }

        final int f() {
            int i8 = this.f20920e - this.f20919d;
            int i9 = this.f20922g - this.f20921f;
            int i10 = this.f20924i - this.f20923h;
            if (i8 < i9 || i8 < i10) {
                return (i9 < i8 || i9 < i10) ? -1 : -2;
            }
            return -3;
        }

        final int g() {
            return ((this.f20920e - this.f20919d) + 1) * ((this.f20922g - this.f20921f) + 1) * ((this.f20924i - this.f20923h) + 1);
        }

        final b h() {
            if (a()) {
                int b9 = b();
                b bVar = new b(b9 + 1, this.f20917b);
                this.f20917b = b9;
                c();
                return bVar;
            }
            throw new IllegalStateException("Can not split a box with only 1 color");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(int[] iArr, int i8, b.c[] cVarArr) {
        this.f20914e = cVarArr;
        int[] iArr2 = new int[RecognitionOptions.TEZ_CODE];
        this.f20911b = iArr2;
        for (int i9 = 0; i9 < iArr.length; i9++) {
            int g8 = g(iArr[i9]);
            iArr[i9] = g8;
            iArr2[g8] = iArr2[g8] + 1;
        }
        int i10 = 0;
        for (int i11 = 0; i11 < 32768; i11++) {
            if (iArr2[i11] > 0 && l(i11)) {
                iArr2[i11] = 0;
            }
            if (iArr2[i11] > 0) {
                i10++;
            }
        }
        int[] iArr3 = new int[i10];
        this.f20910a = iArr3;
        int i12 = 0;
        for (int i13 = 0; i13 < 32768; i13++) {
            if (iArr2[i13] > 0) {
                iArr3[i12] = i13;
                i12++;
            }
        }
        if (i10 > i8) {
            this.f20912c = h(i8);
            return;
        }
        this.f20912c = new ArrayList();
        for (int i14 = 0; i14 < i10; i14++) {
            int i15 = iArr3[i14];
            this.f20912c.add(new b.e(a(i15), iArr2[i15]));
        }
    }

    private static int a(int i8) {
        return b(k(i8), j(i8), i(i8));
    }

    static int b(int i8, int i9, int i10) {
        return Color.rgb(f(i8, 5, 8), f(i9, 5, 8), f(i10, 5, 8));
    }

    private List<b.e> c(Collection<b> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (b bVar : collection) {
            b.e d8 = bVar.d();
            if (!n(d8)) {
                arrayList.add(d8);
            }
        }
        return arrayList;
    }

    static void e(int[] iArr, int i8, int i9, int i10) {
        if (i8 == -2) {
            while (i9 <= i10) {
                int i11 = iArr[i9];
                iArr[i9] = i(i11) | (j(i11) << 10) | (k(i11) << 5);
                i9++;
            }
        } else if (i8 != -1) {
        } else {
            while (i9 <= i10) {
                int i12 = iArr[i9];
                iArr[i9] = k(i12) | (i(i12) << 10) | (j(i12) << 5);
                i9++;
            }
        }
    }

    private static int f(int i8, int i9, int i10) {
        return (i10 > i9 ? i8 << (i10 - i9) : i8 >> (i9 - i10)) & ((1 << i10) - 1);
    }

    private static int g(int i8) {
        return f(Color.blue(i8), 8, 5) | (f(Color.red(i8), 8, 5) << 10) | (f(Color.green(i8), 8, 5) << 5);
    }

    private List<b.e> h(int i8) {
        PriorityQueue<b> priorityQueue = new PriorityQueue<>(i8, f20909g);
        priorityQueue.offer(new b(0, this.f20910a.length - 1));
        o(priorityQueue, i8);
        return c(priorityQueue);
    }

    static int i(int i8) {
        return i8 & 31;
    }

    static int j(int i8) {
        return (i8 >> 5) & 31;
    }

    static int k(int i8) {
        return (i8 >> 10) & 31;
    }

    private boolean l(int i8) {
        int a9 = a(i8);
        androidx.core.graphics.b.h(a9, this.f20915f);
        return m(a9, this.f20915f);
    }

    private boolean m(int i8, float[] fArr) {
        b.c[] cVarArr = this.f20914e;
        if (cVarArr != null && cVarArr.length > 0) {
            int length = cVarArr.length;
            for (int i9 = 0; i9 < length; i9++) {
                if (!this.f20914e[i9].a(i8, fArr)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean n(b.e eVar) {
        return m(eVar.e(), eVar.c());
    }

    private void o(PriorityQueue<b> priorityQueue, int i8) {
        b poll;
        while (priorityQueue.size() < i8 && (poll = priorityQueue.poll()) != null && poll.a()) {
            priorityQueue.offer(poll.h());
            priorityQueue.offer(poll);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<b.e> d() {
        return this.f20912c;
    }
}

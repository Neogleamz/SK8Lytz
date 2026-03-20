package a6;

import a6.w;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class w {

    /* renamed from: h  reason: collision with root package name */
    private static final Comparator<b> f177h = new Comparator() { // from class: a6.v
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int g8;
            g8 = w.g((w.b) obj, (w.b) obj2);
            return g8;
        }
    };

    /* renamed from: i  reason: collision with root package name */
    private static final Comparator<b> f178i = new Comparator() { // from class: a6.u
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int h8;
            h8 = w.h((w.b) obj, (w.b) obj2);
            return h8;
        }
    };

    /* renamed from: a  reason: collision with root package name */
    private final int f179a;

    /* renamed from: e  reason: collision with root package name */
    private int f183e;

    /* renamed from: f  reason: collision with root package name */
    private int f184f;

    /* renamed from: g  reason: collision with root package name */
    private int f185g;

    /* renamed from: c  reason: collision with root package name */
    private final b[] f181c = new b[5];

    /* renamed from: b  reason: collision with root package name */
    private final ArrayList<b> f180b = new ArrayList<>();

    /* renamed from: d  reason: collision with root package name */
    private int f182d = -1;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        public int f186a;

        /* renamed from: b  reason: collision with root package name */
        public int f187b;

        /* renamed from: c  reason: collision with root package name */
        public float f188c;

        private b() {
        }
    }

    public w(int i8) {
        this.f179a = i8;
    }

    private void d() {
        if (this.f182d != 1) {
            Collections.sort(this.f180b, f177h);
            this.f182d = 1;
        }
    }

    private void e() {
        if (this.f182d != 0) {
            Collections.sort(this.f180b, f178i);
            this.f182d = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int g(b bVar, b bVar2) {
        return bVar.f186a - bVar2.f186a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int h(b bVar, b bVar2) {
        return Float.compare(bVar.f188c, bVar2.f188c);
    }

    public void c(int i8, float f5) {
        b bVar;
        int i9;
        b bVar2;
        int i10;
        d();
        int i11 = this.f185g;
        if (i11 > 0) {
            b[] bVarArr = this.f181c;
            int i12 = i11 - 1;
            this.f185g = i12;
            bVar = bVarArr[i12];
        } else {
            bVar = new b();
        }
        int i13 = this.f183e;
        this.f183e = i13 + 1;
        bVar.f186a = i13;
        bVar.f187b = i8;
        bVar.f188c = f5;
        this.f180b.add(bVar);
        int i14 = this.f184f + i8;
        while (true) {
            this.f184f = i14;
            while (true) {
                int i15 = this.f184f;
                int i16 = this.f179a;
                if (i15 <= i16) {
                    return;
                }
                i9 = i15 - i16;
                bVar2 = this.f180b.get(0);
                i10 = bVar2.f187b;
                if (i10 <= i9) {
                    this.f184f -= i10;
                    this.f180b.remove(0);
                    int i17 = this.f185g;
                    if (i17 < 5) {
                        b[] bVarArr2 = this.f181c;
                        this.f185g = i17 + 1;
                        bVarArr2[i17] = bVar2;
                    }
                }
            }
            bVar2.f187b = i10 - i9;
            i14 = this.f184f - i9;
        }
    }

    public float f(float f5) {
        ArrayList<b> arrayList;
        e();
        float f8 = f5 * this.f184f;
        int i8 = 0;
        for (int i9 = 0; i9 < this.f180b.size(); i9++) {
            b bVar = this.f180b.get(i9);
            i8 += bVar.f187b;
            if (i8 >= f8) {
                return bVar.f188c;
            }
        }
        if (this.f180b.isEmpty()) {
            return Float.NaN;
        }
        return this.f180b.get(arrayList.size() - 1).f188c;
    }

    public void i() {
        this.f180b.clear();
        this.f182d = -1;
        this.f183e = 0;
        this.f184f = 0;
    }
}

package com.google.common.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p {

    /* renamed from: a  reason: collision with root package name */
    private final d f18841a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f18842b;

    /* renamed from: c  reason: collision with root package name */
    private final c f18843c;

    /* renamed from: d  reason: collision with root package name */
    private final int f18844d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements c {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ d f18845a;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.google.common.base.p$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class C0148a extends b {
            C0148a(p pVar, CharSequence charSequence) {
                super(pVar, charSequence);
            }

            @Override // com.google.common.base.p.b
            int e(int i8) {
                return i8 + 1;
            }

            @Override // com.google.common.base.p.b
            int f(int i8) {
                return a.this.f18845a.d(this.f18847c, i8);
            }
        }

        a(d dVar) {
            this.f18845a = dVar;
        }

        @Override // com.google.common.base.p.c
        /* renamed from: b */
        public b a(p pVar, CharSequence charSequence) {
            return new C0148a(pVar, charSequence);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class b extends com.google.common.base.b<String> {

        /* renamed from: c  reason: collision with root package name */
        final CharSequence f18847c;

        /* renamed from: d  reason: collision with root package name */
        final d f18848d;

        /* renamed from: e  reason: collision with root package name */
        final boolean f18849e;

        /* renamed from: f  reason: collision with root package name */
        int f18850f = 0;

        /* renamed from: g  reason: collision with root package name */
        int f18851g;

        protected b(p pVar, CharSequence charSequence) {
            this.f18848d = pVar.f18841a;
            this.f18849e = pVar.f18842b;
            this.f18851g = pVar.f18844d;
            this.f18847c = charSequence;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Code restructure failed: missing block: B:14:0x002f, code lost:
            if (r0 >= r1) goto L47;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x003d, code lost:
            if (r6.f18848d.f(r6.f18847c.charAt(r0)) == false) goto L21;
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x003f, code lost:
            r0 = r0 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x0042, code lost:
            if (r1 <= r0) goto L46;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0052, code lost:
            if (r6.f18848d.f(r6.f18847c.charAt(r1 - 1)) == false) goto L27;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x0054, code lost:
            r1 = r1 - 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0059, code lost:
            if (r6.f18849e == false) goto L45;
         */
        @Override // com.google.common.base.b
        /* renamed from: d */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.String a() {
            /*
                r6 = this;
            L0:
                int r0 = r6.f18850f
            L2:
                int r1 = r6.f18850f
                r2 = -1
                if (r1 == r2) goto L8e
                int r1 = r6.f(r1)
                if (r1 != r2) goto L16
                java.lang.CharSequence r1 = r6.f18847c
                int r1 = r1.length()
                r6.f18850f = r2
                goto L1c
            L16:
                int r3 = r6.e(r1)
                r6.f18850f = r3
            L1c:
                int r3 = r6.f18850f
                if (r3 != r0) goto L2f
                int r3 = r3 + 1
                r6.f18850f = r3
                java.lang.CharSequence r1 = r6.f18847c
                int r1 = r1.length()
                if (r3 <= r1) goto L2
                r6.f18850f = r2
                goto L2
            L2f:
                if (r0 >= r1) goto L42
                com.google.common.base.d r3 = r6.f18848d
                java.lang.CharSequence r4 = r6.f18847c
                char r4 = r4.charAt(r0)
                boolean r3 = r3.f(r4)
                if (r3 == 0) goto L42
                int r0 = r0 + 1
                goto L2f
            L42:
                if (r1 <= r0) goto L57
                com.google.common.base.d r3 = r6.f18848d
                java.lang.CharSequence r4 = r6.f18847c
                int r5 = r1 + (-1)
                char r4 = r4.charAt(r5)
                boolean r3 = r3.f(r4)
                if (r3 == 0) goto L57
                int r1 = r1 + (-1)
                goto L42
            L57:
                boolean r3 = r6.f18849e
                if (r3 == 0) goto L5e
                if (r0 != r1) goto L5e
                goto L0
            L5e:
                int r3 = r6.f18851g
                r4 = 1
                if (r3 != r4) goto L80
                java.lang.CharSequence r1 = r6.f18847c
                int r1 = r1.length()
                r6.f18850f = r2
            L6b:
                if (r1 <= r0) goto L83
                com.google.common.base.d r2 = r6.f18848d
                java.lang.CharSequence r3 = r6.f18847c
                int r4 = r1 + (-1)
                char r3 = r3.charAt(r4)
                boolean r2 = r2.f(r3)
                if (r2 == 0) goto L83
                int r1 = r1 + (-1)
                goto L6b
            L80:
                int r3 = r3 - r4
                r6.f18851g = r3
            L83:
                java.lang.CharSequence r2 = r6.f18847c
                java.lang.CharSequence r0 = r2.subSequence(r0, r1)
                java.lang.String r0 = r0.toString()
                return r0
            L8e:
                java.lang.Object r0 = r6.b()
                java.lang.String r0 = (java.lang.String) r0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.base.p.b.a():java.lang.String");
        }

        abstract int e(int i8);

        abstract int f(int i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        Iterator<String> a(p pVar, CharSequence charSequence);
    }

    private p(c cVar) {
        this(cVar, false, d.g(), Integer.MAX_VALUE);
    }

    private p(c cVar, boolean z4, d dVar, int i8) {
        this.f18843c = cVar;
        this.f18842b = z4;
        this.f18841a = dVar;
        this.f18844d = i8;
    }

    public static p d(char c9) {
        return e(d.e(c9));
    }

    public static p e(d dVar) {
        l.n(dVar);
        return new p(new a(dVar));
    }

    private Iterator<String> g(CharSequence charSequence) {
        return this.f18843c.a(this, charSequence);
    }

    public List<String> f(CharSequence charSequence) {
        l.n(charSequence);
        Iterator<String> g8 = g(charSequence);
        ArrayList arrayList = new ArrayList();
        while (g8.hasNext()) {
            arrayList.add(g8.next());
        }
        return Collections.unmodifiableList(arrayList);
    }
}

package androidx.fragment.app;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.c0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class s {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f5689a = {0, 3, 0, 1, 5, 4, 7, 6, 9, 8, 10};

    /* renamed from: b  reason: collision with root package name */
    static final u f5690b;

    /* renamed from: c  reason: collision with root package name */
    static final u f5691c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ g f5692a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Fragment f5693b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ androidx.core.os.e f5694c;

        a(g gVar, Fragment fragment, androidx.core.os.e eVar) {
            this.f5692a = gVar;
            this.f5693b = fragment;
            this.f5694c = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f5692a.a(this.f5693b, this.f5694c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ArrayList f5695a;

        b(ArrayList arrayList) {
            this.f5695a = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            s.A(this.f5695a, 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ g f5696a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Fragment f5697b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ androidx.core.os.e f5698c;

        c(g gVar, Fragment fragment, androidx.core.os.e eVar) {
            this.f5696a = gVar;
            this.f5697b = fragment;
            this.f5698c = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f5696a.a(this.f5697b, this.f5698c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Object f5699a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ u f5700b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ View f5701c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ Fragment f5702d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ ArrayList f5703e;

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ ArrayList f5704f;

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ ArrayList f5705g;

        /* renamed from: h  reason: collision with root package name */
        final /* synthetic */ Object f5706h;

        d(Object obj, u uVar, View view, Fragment fragment, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, Object obj2) {
            this.f5699a = obj;
            this.f5700b = uVar;
            this.f5701c = view;
            this.f5702d = fragment;
            this.f5703e = arrayList;
            this.f5704f = arrayList2;
            this.f5705g = arrayList3;
            this.f5706h = obj2;
        }

        @Override // java.lang.Runnable
        public void run() {
            Object obj = this.f5699a;
            if (obj != null) {
                this.f5700b.p(obj, this.f5701c);
                this.f5704f.addAll(s.k(this.f5700b, this.f5699a, this.f5702d, this.f5703e, this.f5701c));
            }
            if (this.f5705g != null) {
                if (this.f5706h != null) {
                    ArrayList<View> arrayList = new ArrayList<>();
                    arrayList.add(this.f5701c);
                    this.f5700b.q(this.f5706h, this.f5705g, arrayList);
                }
                this.f5705g.clear();
                this.f5705g.add(this.f5701c);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Fragment f5707a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Fragment f5708b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ boolean f5709c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ k0.a f5710d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ View f5711e;

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ u f5712f;

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ Rect f5713g;

        e(Fragment fragment, Fragment fragment2, boolean z4, k0.a aVar, View view, u uVar, Rect rect) {
            this.f5707a = fragment;
            this.f5708b = fragment2;
            this.f5709c = z4;
            this.f5710d = aVar;
            this.f5711e = view;
            this.f5712f = uVar;
            this.f5713g = rect;
        }

        @Override // java.lang.Runnable
        public void run() {
            s.f(this.f5707a, this.f5708b, this.f5709c, this.f5710d, false);
            View view = this.f5711e;
            if (view != null) {
                this.f5712f.k(view, this.f5713g);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ u f5714a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ k0.a f5715b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Object f5716c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ h f5717d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ ArrayList f5718e;

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ View f5719f;

        /* renamed from: g  reason: collision with root package name */
        final /* synthetic */ Fragment f5720g;

        /* renamed from: h  reason: collision with root package name */
        final /* synthetic */ Fragment f5721h;

        /* renamed from: j  reason: collision with root package name */
        final /* synthetic */ boolean f5722j;

        /* renamed from: k  reason: collision with root package name */
        final /* synthetic */ ArrayList f5723k;

        /* renamed from: l  reason: collision with root package name */
        final /* synthetic */ Object f5724l;

        /* renamed from: m  reason: collision with root package name */
        final /* synthetic */ Rect f5725m;

        f(u uVar, k0.a aVar, Object obj, h hVar, ArrayList arrayList, View view, Fragment fragment, Fragment fragment2, boolean z4, ArrayList arrayList2, Object obj2, Rect rect) {
            this.f5714a = uVar;
            this.f5715b = aVar;
            this.f5716c = obj;
            this.f5717d = hVar;
            this.f5718e = arrayList;
            this.f5719f = view;
            this.f5720g = fragment;
            this.f5721h = fragment2;
            this.f5722j = z4;
            this.f5723k = arrayList2;
            this.f5724l = obj2;
            this.f5725m = rect;
        }

        @Override // java.lang.Runnable
        public void run() {
            k0.a<String, View> h8 = s.h(this.f5714a, this.f5715b, this.f5716c, this.f5717d);
            if (h8 != null) {
                this.f5718e.addAll(h8.values());
                this.f5718e.add(this.f5719f);
            }
            s.f(this.f5720g, this.f5721h, this.f5722j, h8, false);
            Object obj = this.f5716c;
            if (obj != null) {
                this.f5714a.A(obj, this.f5723k, this.f5718e);
                View s8 = s.s(h8, this.f5717d, this.f5724l, this.f5722j);
                if (s8 != null) {
                    this.f5714a.k(s8, this.f5725m);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface g {
        void a(Fragment fragment, androidx.core.os.e eVar);

        void b(Fragment fragment, androidx.core.os.e eVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h {

        /* renamed from: a  reason: collision with root package name */
        public Fragment f5726a;

        /* renamed from: b  reason: collision with root package name */
        public boolean f5727b;

        /* renamed from: c  reason: collision with root package name */
        public androidx.fragment.app.a f5728c;

        /* renamed from: d  reason: collision with root package name */
        public Fragment f5729d;

        /* renamed from: e  reason: collision with root package name */
        public boolean f5730e;

        /* renamed from: f  reason: collision with root package name */
        public androidx.fragment.app.a f5731f;

        h() {
        }
    }

    static {
        f5690b = Build.VERSION.SDK_INT >= 21 ? new t() : null;
        f5691c = w();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void A(ArrayList<View> arrayList, int i8) {
        if (arrayList == null) {
            return;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            arrayList.get(size).setVisibility(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void B(Context context, androidx.fragment.app.e eVar, ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2, int i8, int i9, boolean z4, g gVar) {
        ViewGroup viewGroup;
        SparseArray sparseArray = new SparseArray();
        for (int i10 = i8; i10 < i9; i10++) {
            androidx.fragment.app.a aVar = arrayList.get(i10);
            if (arrayList2.get(i10).booleanValue()) {
                e(aVar, sparseArray, z4);
            } else {
                c(aVar, sparseArray, z4);
            }
        }
        if (sparseArray.size() != 0) {
            View view = new View(context);
            int size = sparseArray.size();
            for (int i11 = 0; i11 < size; i11++) {
                int keyAt = sparseArray.keyAt(i11);
                k0.a<String, String> d8 = d(keyAt, arrayList, arrayList2, i8, i9);
                h hVar = (h) sparseArray.valueAt(i11);
                if (eVar.d() && (viewGroup = (ViewGroup) eVar.c(keyAt)) != null) {
                    if (z4) {
                        o(viewGroup, hVar, view, d8, gVar);
                    } else {
                        n(viewGroup, hVar, view, d8, gVar);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean C() {
        return (f5690b == null && f5691c == null) ? false : true;
    }

    private static void a(ArrayList<View> arrayList, k0.a<String, View> aVar, Collection<String> collection) {
        for (int size = aVar.size() - 1; size >= 0; size--) {
            View o5 = aVar.o(size);
            if (collection.contains(c0.N(o5))) {
                arrayList.add(o5);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0039, code lost:
        if (r0.f5412m != false) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x006e, code lost:
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0088, code lost:
        if (r0.G == false) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x008a, code lost:
        r9 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x00a5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x00c5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x00d7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:96:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void b(androidx.fragment.app.a r8, androidx.fragment.app.r.a r9, android.util.SparseArray<androidx.fragment.app.s.h> r10, boolean r11, boolean r12) {
        /*
            Method dump skipped, instructions count: 226
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.s.b(androidx.fragment.app.a, androidx.fragment.app.r$a, android.util.SparseArray, boolean, boolean):void");
    }

    public static void c(androidx.fragment.app.a aVar, SparseArray<h> sparseArray, boolean z4) {
        int size = aVar.f5665c.size();
        for (int i8 = 0; i8 < size; i8++) {
            b(aVar, aVar.f5665c.get(i8), sparseArray, false, z4);
        }
    }

    private static k0.a<String, String> d(int i8, ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2, int i9, int i10) {
        ArrayList<String> arrayList3;
        ArrayList<String> arrayList4;
        k0.a<String, String> aVar = new k0.a<>();
        for (int i11 = i10 - 1; i11 >= i9; i11--) {
            androidx.fragment.app.a aVar2 = arrayList.get(i11);
            if (aVar2.E(i8)) {
                boolean booleanValue = arrayList2.get(i11).booleanValue();
                ArrayList<String> arrayList5 = aVar2.f5678p;
                if (arrayList5 != null) {
                    int size = arrayList5.size();
                    if (booleanValue) {
                        arrayList3 = aVar2.f5678p;
                        arrayList4 = aVar2.q;
                    } else {
                        ArrayList<String> arrayList6 = aVar2.f5678p;
                        arrayList3 = aVar2.q;
                        arrayList4 = arrayList6;
                    }
                    for (int i12 = 0; i12 < size; i12++) {
                        String str = arrayList4.get(i12);
                        String str2 = arrayList3.get(i12);
                        String remove = aVar.remove(str2);
                        if (remove != null) {
                            aVar.put(str, remove);
                        } else {
                            aVar.put(str, str2);
                        }
                    }
                }
            }
        }
        return aVar;
    }

    public static void e(androidx.fragment.app.a aVar, SparseArray<h> sparseArray, boolean z4) {
        if (aVar.f5545t.o0().d()) {
            for (int size = aVar.f5665c.size() - 1; size >= 0; size--) {
                b(aVar, aVar.f5665c.get(size), sparseArray, true, z4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void f(Fragment fragment, Fragment fragment2, boolean z4, k0.a<String, View> aVar, boolean z8) {
        if (z4) {
            fragment2.t();
        } else {
            fragment.t();
        }
    }

    private static boolean g(u uVar, List<Object> list) {
        int size = list.size();
        for (int i8 = 0; i8 < size; i8++) {
            if (!uVar.e(list.get(i8))) {
                return false;
            }
        }
        return true;
    }

    static k0.a<String, View> h(u uVar, k0.a<String, String> aVar, Object obj, h hVar) {
        ArrayList<String> arrayList;
        Fragment fragment = hVar.f5726a;
        View T = fragment.T();
        if (aVar.isEmpty() || obj == null || T == null) {
            aVar.clear();
            return null;
        }
        k0.a<String, View> aVar2 = new k0.a<>();
        uVar.j(aVar2, T);
        androidx.fragment.app.a aVar3 = hVar.f5728c;
        if (hVar.f5727b) {
            fragment.w();
            arrayList = aVar3.f5678p;
        } else {
            fragment.t();
            arrayList = aVar3.q;
        }
        if (arrayList != null) {
            aVar2.q(arrayList);
            aVar2.q(aVar.values());
        }
        x(aVar, aVar2);
        return aVar2;
    }

    private static k0.a<String, View> i(u uVar, k0.a<String, String> aVar, Object obj, h hVar) {
        ArrayList<String> arrayList;
        if (aVar.isEmpty() || obj == null) {
            aVar.clear();
            return null;
        }
        Fragment fragment = hVar.f5729d;
        k0.a<String, View> aVar2 = new k0.a<>();
        uVar.j(aVar2, fragment.m1());
        androidx.fragment.app.a aVar3 = hVar.f5731f;
        if (hVar.f5730e) {
            fragment.t();
            arrayList = aVar3.q;
        } else {
            fragment.w();
            arrayList = aVar3.f5678p;
        }
        if (arrayList != null) {
            aVar2.q(arrayList);
        }
        aVar.q(aVar2.keySet());
        return aVar2;
    }

    private static u j(Fragment fragment, Fragment fragment2) {
        ArrayList arrayList = new ArrayList();
        if (fragment != null) {
            Object v8 = fragment.v();
            if (v8 != null) {
                arrayList.add(v8);
            }
            Object L = fragment.L();
            if (L != null) {
                arrayList.add(L);
            }
            Object N = fragment.N();
            if (N != null) {
                arrayList.add(N);
            }
        }
        if (fragment2 != null) {
            Object s8 = fragment2.s();
            if (s8 != null) {
                arrayList.add(s8);
            }
            Object J = fragment2.J();
            if (J != null) {
                arrayList.add(J);
            }
            Object M = fragment2.M();
            if (M != null) {
                arrayList.add(M);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        u uVar = f5690b;
        if (uVar == null || !g(uVar, arrayList)) {
            u uVar2 = f5691c;
            if (uVar2 == null || !g(uVar2, arrayList)) {
                if (uVar == null && uVar2 == null) {
                    return null;
                }
                throw new IllegalArgumentException("Invalid Transition types");
            }
            return uVar2;
        }
        return uVar;
    }

    static ArrayList<View> k(u uVar, Object obj, Fragment fragment, ArrayList<View> arrayList, View view) {
        if (obj != null) {
            ArrayList<View> arrayList2 = new ArrayList<>();
            View T = fragment.T();
            if (T != null) {
                uVar.f(arrayList2, T);
            }
            if (arrayList != null) {
                arrayList2.removeAll(arrayList);
            }
            if (arrayList2.isEmpty()) {
                return arrayList2;
            }
            arrayList2.add(view);
            uVar.b(obj, arrayList2);
            return arrayList2;
        }
        return null;
    }

    private static Object l(u uVar, ViewGroup viewGroup, View view, k0.a<String, String> aVar, h hVar, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object obj, Object obj2) {
        Object t8;
        k0.a<String, String> aVar2;
        Object obj3;
        Rect rect;
        Fragment fragment = hVar.f5726a;
        Fragment fragment2 = hVar.f5729d;
        if (fragment == null || fragment2 == null) {
            return null;
        }
        boolean z4 = hVar.f5727b;
        if (aVar.isEmpty()) {
            aVar2 = aVar;
            t8 = null;
        } else {
            t8 = t(uVar, fragment, fragment2, z4);
            aVar2 = aVar;
        }
        k0.a<String, View> i8 = i(uVar, aVar2, t8, hVar);
        if (aVar.isEmpty()) {
            obj3 = null;
        } else {
            arrayList.addAll(i8.values());
            obj3 = t8;
        }
        if (obj == null && obj2 == null && obj3 == null) {
            return null;
        }
        f(fragment, fragment2, z4, i8, true);
        if (obj3 != null) {
            rect = new Rect();
            uVar.z(obj3, view, arrayList);
            z(uVar, obj3, obj2, i8, hVar.f5730e, hVar.f5731f);
            if (obj != null) {
                uVar.u(obj, rect);
            }
        } else {
            rect = null;
        }
        androidx.core.view.y.a(viewGroup, new f(uVar, aVar, obj3, hVar, arrayList2, view, fragment, fragment2, z4, arrayList, obj, rect));
        return obj3;
    }

    private static Object m(u uVar, ViewGroup viewGroup, View view, k0.a<String, String> aVar, h hVar, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object obj, Object obj2) {
        Object obj3;
        View view2;
        Rect rect;
        Fragment fragment = hVar.f5726a;
        Fragment fragment2 = hVar.f5729d;
        if (fragment != null) {
            fragment.m1().setVisibility(0);
        }
        if (fragment == null || fragment2 == null) {
            return null;
        }
        boolean z4 = hVar.f5727b;
        Object t8 = aVar.isEmpty() ? null : t(uVar, fragment, fragment2, z4);
        k0.a<String, View> i8 = i(uVar, aVar, t8, hVar);
        k0.a<String, View> h8 = h(uVar, aVar, t8, hVar);
        if (aVar.isEmpty()) {
            if (i8 != null) {
                i8.clear();
            }
            if (h8 != null) {
                h8.clear();
            }
            obj3 = null;
        } else {
            a(arrayList, i8, aVar.keySet());
            a(arrayList2, h8, aVar.values());
            obj3 = t8;
        }
        if (obj == null && obj2 == null && obj3 == null) {
            return null;
        }
        f(fragment, fragment2, z4, i8, true);
        if (obj3 != null) {
            arrayList2.add(view);
            uVar.z(obj3, view, arrayList);
            z(uVar, obj3, obj2, i8, hVar.f5730e, hVar.f5731f);
            Rect rect2 = new Rect();
            View s8 = s(h8, hVar, obj, z4);
            if (s8 != null) {
                uVar.u(obj, rect2);
            }
            rect = rect2;
            view2 = s8;
        } else {
            view2 = null;
            rect = null;
        }
        androidx.core.view.y.a(viewGroup, new e(fragment, fragment2, z4, h8, view2, uVar, rect));
        return obj3;
    }

    private static void n(ViewGroup viewGroup, h hVar, View view, k0.a<String, String> aVar, g gVar) {
        Object obj;
        Fragment fragment = hVar.f5726a;
        Fragment fragment2 = hVar.f5729d;
        u j8 = j(fragment2, fragment);
        if (j8 == null) {
            return;
        }
        boolean z4 = hVar.f5727b;
        boolean z8 = hVar.f5730e;
        Object q = q(j8, fragment, z4);
        Object r4 = r(j8, fragment2, z8);
        ArrayList arrayList = new ArrayList();
        ArrayList<View> arrayList2 = new ArrayList<>();
        Object l8 = l(j8, viewGroup, view, aVar, hVar, arrayList, arrayList2, q, r4);
        if (q == null && l8 == null) {
            obj = r4;
            if (obj == null) {
                return;
            }
        } else {
            obj = r4;
        }
        ArrayList<View> k8 = k(j8, obj, fragment2, arrayList, view);
        Object obj2 = (k8 == null || k8.isEmpty()) ? null : null;
        j8.a(q, view);
        Object u8 = u(j8, q, obj2, l8, fragment, hVar.f5727b);
        if (fragment2 != null && k8 != null && (k8.size() > 0 || arrayList.size() > 0)) {
            androidx.core.os.e eVar = new androidx.core.os.e();
            gVar.b(fragment2, eVar);
            j8.w(fragment2, u8, eVar, new c(gVar, fragment2, eVar));
        }
        if (u8 != null) {
            ArrayList<View> arrayList3 = new ArrayList<>();
            j8.t(u8, q, arrayList3, obj2, k8, l8, arrayList2);
            y(j8, viewGroup, fragment, view, arrayList2, q, arrayList3, obj2, k8);
            j8.x(viewGroup, arrayList2, aVar);
            j8.c(viewGroup, u8);
            j8.s(viewGroup, arrayList2, aVar);
        }
    }

    private static void o(ViewGroup viewGroup, h hVar, View view, k0.a<String, String> aVar, g gVar) {
        Object obj;
        Fragment fragment = hVar.f5726a;
        Fragment fragment2 = hVar.f5729d;
        u j8 = j(fragment2, fragment);
        if (j8 == null) {
            return;
        }
        boolean z4 = hVar.f5727b;
        boolean z8 = hVar.f5730e;
        ArrayList<View> arrayList = new ArrayList<>();
        ArrayList<View> arrayList2 = new ArrayList<>();
        Object q = q(j8, fragment, z4);
        Object r4 = r(j8, fragment2, z8);
        Object m8 = m(j8, viewGroup, view, aVar, hVar, arrayList2, arrayList, q, r4);
        if (q == null && m8 == null) {
            obj = r4;
            if (obj == null) {
                return;
            }
        } else {
            obj = r4;
        }
        ArrayList<View> k8 = k(j8, obj, fragment2, arrayList2, view);
        ArrayList<View> k9 = k(j8, q, fragment, arrayList, view);
        A(k9, 4);
        Object u8 = u(j8, q, obj, m8, fragment, z4);
        if (fragment2 != null && k8 != null && (k8.size() > 0 || arrayList2.size() > 0)) {
            androidx.core.os.e eVar = new androidx.core.os.e();
            gVar.b(fragment2, eVar);
            j8.w(fragment2, u8, eVar, new a(gVar, fragment2, eVar));
        }
        if (u8 != null) {
            v(j8, obj, fragment2, k8);
            ArrayList<String> o5 = j8.o(arrayList);
            j8.t(u8, q, k9, obj, k8, m8, arrayList);
            j8.c(viewGroup, u8);
            j8.y(viewGroup, arrayList2, arrayList, o5, aVar);
            A(k9, 0);
            j8.A(m8, arrayList2, arrayList);
        }
    }

    private static h p(h hVar, SparseArray<h> sparseArray, int i8) {
        if (hVar == null) {
            h hVar2 = new h();
            sparseArray.put(i8, hVar2);
            return hVar2;
        }
        return hVar;
    }

    private static Object q(u uVar, Fragment fragment, boolean z4) {
        if (fragment == null) {
            return null;
        }
        return uVar.g(z4 ? fragment.J() : fragment.s());
    }

    private static Object r(u uVar, Fragment fragment, boolean z4) {
        if (fragment == null) {
            return null;
        }
        return uVar.g(z4 ? fragment.L() : fragment.v());
    }

    static View s(k0.a<String, View> aVar, h hVar, Object obj, boolean z4) {
        ArrayList<String> arrayList;
        androidx.fragment.app.a aVar2 = hVar.f5728c;
        if (obj == null || aVar == null || (arrayList = aVar2.f5678p) == null || arrayList.isEmpty()) {
            return null;
        }
        return aVar.get((z4 ? aVar2.f5678p : aVar2.q).get(0));
    }

    private static Object t(u uVar, Fragment fragment, Fragment fragment2, boolean z4) {
        if (fragment == null || fragment2 == null) {
            return null;
        }
        return uVar.B(uVar.g(z4 ? fragment2.N() : fragment.M()));
    }

    private static Object u(u uVar, Object obj, Object obj2, Object obj3, Fragment fragment, boolean z4) {
        return (obj == null || obj2 == null || fragment == null) ? true : z4 ? fragment.m() : fragment.l() ? uVar.n(obj2, obj, obj3) : uVar.m(obj2, obj, obj3);
    }

    private static void v(u uVar, Object obj, Fragment fragment, ArrayList<View> arrayList) {
        if (fragment != null && obj != null && fragment.f5412m && fragment.G && fragment.f5392b0) {
            fragment.v1(true);
            uVar.r(obj, fragment.T(), arrayList);
            androidx.core.view.y.a(fragment.R, new b(arrayList));
        }
    }

    private static u w() {
        try {
            return (u) androidx.transition.d.class.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void x(k0.a<String, String> aVar, k0.a<String, View> aVar2) {
        for (int size = aVar.size() - 1; size >= 0; size--) {
            if (!aVar2.containsKey(aVar.o(size))) {
                aVar.m(size);
            }
        }
    }

    private static void y(u uVar, ViewGroup viewGroup, Fragment fragment, View view, ArrayList<View> arrayList, Object obj, ArrayList<View> arrayList2, Object obj2, ArrayList<View> arrayList3) {
        androidx.core.view.y.a(viewGroup, new d(obj, uVar, view, fragment, arrayList, arrayList2, arrayList3, obj2));
    }

    private static void z(u uVar, Object obj, Object obj2, k0.a<String, View> aVar, boolean z4, androidx.fragment.app.a aVar2) {
        ArrayList<String> arrayList = aVar2.f5678p;
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        View view = aVar.get((z4 ? aVar2.q : aVar2.f5678p).get(0));
        uVar.v(obj, view);
        if (obj2 != null) {
            uVar.v(obj2, view);
        }
    }
}

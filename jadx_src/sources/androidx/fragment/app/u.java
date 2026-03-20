package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.c0;
import androidx.core.view.f0;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@SuppressLint({"UnknownNullness"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class u {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f5748a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ArrayList f5749b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ ArrayList f5750c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ ArrayList f5751d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ ArrayList f5752e;

        a(int i8, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4) {
            this.f5748a = i8;
            this.f5749b = arrayList;
            this.f5750c = arrayList2;
            this.f5751d = arrayList3;
            this.f5752e = arrayList4;
        }

        @Override // java.lang.Runnable
        public void run() {
            for (int i8 = 0; i8 < this.f5748a; i8++) {
                c0.O0((View) this.f5749b.get(i8), (String) this.f5750c.get(i8));
                c0.O0((View) this.f5751d.get(i8), (String) this.f5752e.get(i8));
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ArrayList f5754a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Map f5755b;

        b(ArrayList arrayList, Map map) {
            this.f5754a = arrayList;
            this.f5755b = map;
        }

        @Override // java.lang.Runnable
        public void run() {
            int size = this.f5754a.size();
            for (int i8 = 0; i8 < size; i8++) {
                View view = (View) this.f5754a.get(i8);
                String N = c0.N(view);
                if (N != null) {
                    c0.O0(view, u.i(this.f5755b, N));
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ArrayList f5757a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Map f5758b;

        c(ArrayList arrayList, Map map) {
            this.f5757a = arrayList;
            this.f5758b = map;
        }

        @Override // java.lang.Runnable
        public void run() {
            int size = this.f5757a.size();
            for (int i8 = 0; i8 < size; i8++) {
                View view = (View) this.f5757a.get(i8);
                c0.O0(view, (String) this.f5758b.get(c0.N(view)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void d(List<View> list, View view) {
        int size = list.size();
        if (h(list, view, size)) {
            return;
        }
        if (c0.N(view) != null) {
            list.add(view);
        }
        for (int i8 = size; i8 < list.size(); i8++) {
            View view2 = list.get(i8);
            if (view2 instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view2;
                int childCount = viewGroup.getChildCount();
                for (int i9 = 0; i9 < childCount; i9++) {
                    View childAt = viewGroup.getChildAt(i9);
                    if (!h(list, childAt, size) && c0.N(childAt) != null) {
                        list.add(childAt);
                    }
                }
            }
        }
    }

    private static boolean h(List<View> list, View view, int i8) {
        for (int i9 = 0; i9 < i8; i9++) {
            if (list.get(i9) == view) {
                return true;
            }
        }
        return false;
    }

    static String i(Map<String, String> map, String str) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (str.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean l(List list) {
        return list == null || list.isEmpty();
    }

    public abstract void A(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2);

    public abstract Object B(Object obj);

    public abstract void a(Object obj, View view);

    public abstract void b(Object obj, ArrayList<View> arrayList);

    public abstract void c(ViewGroup viewGroup, Object obj);

    public abstract boolean e(Object obj);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(ArrayList<View> arrayList, View view) {
        if (view.getVisibility() == 0) {
            boolean z4 = view instanceof ViewGroup;
            ViewGroup viewGroup = view;
            if (z4) {
                ViewGroup viewGroup2 = (ViewGroup) view;
                boolean a9 = f0.a(viewGroup2);
                viewGroup = viewGroup2;
                if (!a9) {
                    int childCount = viewGroup2.getChildCount();
                    for (int i8 = 0; i8 < childCount; i8++) {
                        f(arrayList, viewGroup2.getChildAt(i8));
                    }
                    return;
                }
            }
            arrayList.add(viewGroup);
        }
    }

    public abstract Object g(Object obj);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(Map<String, View> map, View view) {
        if (view.getVisibility() == 0) {
            String N = c0.N(view);
            if (N != null) {
                map.put(N, view);
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i8 = 0; i8 < childCount; i8++) {
                    j(map, viewGroup.getChildAt(i8));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void k(View view, Rect rect) {
        if (c0.V(view)) {
            RectF rectF = new RectF();
            rectF.set(0.0f, 0.0f, view.getWidth(), view.getHeight());
            view.getMatrix().mapRect(rectF);
            rectF.offset(view.getLeft(), view.getTop());
            ViewParent parent = view.getParent();
            while (parent instanceof View) {
                View view2 = (View) parent;
                rectF.offset(-view2.getScrollX(), -view2.getScrollY());
                view2.getMatrix().mapRect(rectF);
                rectF.offset(view2.getLeft(), view2.getTop());
                parent = view2.getParent();
            }
            int[] iArr = new int[2];
            view.getRootView().getLocationOnScreen(iArr);
            rectF.offset(iArr[0], iArr[1]);
            rect.set(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
        }
    }

    public abstract Object m(Object obj, Object obj2, Object obj3);

    public abstract Object n(Object obj, Object obj2, Object obj3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<String> o(ArrayList<View> arrayList) {
        ArrayList<String> arrayList2 = new ArrayList<>();
        int size = arrayList.size();
        for (int i8 = 0; i8 < size; i8++) {
            View view = arrayList.get(i8);
            arrayList2.add(c0.N(view));
            c0.O0(view, null);
        }
        return arrayList2;
    }

    public abstract void p(Object obj, View view);

    public abstract void q(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2);

    public abstract void r(Object obj, View view, ArrayList<View> arrayList);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(ViewGroup viewGroup, ArrayList<View> arrayList, Map<String, String> map) {
        androidx.core.view.y.a(viewGroup, new c(arrayList, map));
    }

    public abstract void t(Object obj, Object obj2, ArrayList<View> arrayList, Object obj3, ArrayList<View> arrayList2, Object obj4, ArrayList<View> arrayList3);

    public abstract void u(Object obj, Rect rect);

    public abstract void v(Object obj, View view);

    public void w(Fragment fragment, Object obj, androidx.core.os.e eVar, Runnable runnable) {
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(View view, ArrayList<View> arrayList, Map<String, String> map) {
        androidx.core.view.y.a(view, new b(arrayList, map));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(View view, ArrayList<View> arrayList, ArrayList<View> arrayList2, ArrayList<String> arrayList3, Map<String, String> map) {
        int size = arrayList2.size();
        ArrayList arrayList4 = new ArrayList();
        for (int i8 = 0; i8 < size; i8++) {
            View view2 = arrayList.get(i8);
            String N = c0.N(view2);
            arrayList4.add(N);
            if (N != null) {
                c0.O0(view2, null);
                String str = map.get(N);
                int i9 = 0;
                while (true) {
                    if (i9 >= size) {
                        break;
                    } else if (str.equals(arrayList3.get(i9))) {
                        c0.O0(arrayList2.get(i9), N);
                        break;
                    } else {
                        i9++;
                    }
                }
            }
        }
        androidx.core.view.y.a(view, new a(size, arrayList2, arrayList3, arrayList, arrayList4));
    }

    public abstract void z(Object obj, View view, ArrayList<View> arrayList);
}

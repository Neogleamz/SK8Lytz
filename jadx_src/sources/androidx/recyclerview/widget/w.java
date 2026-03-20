package androidx.recyclerview.widget;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.c0;
import java.util.Map;
import java.util.WeakHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class w extends androidx.core.view.a {

    /* renamed from: d  reason: collision with root package name */
    final RecyclerView f7032d;

    /* renamed from: e  reason: collision with root package name */
    private final a f7033e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends androidx.core.view.a {

        /* renamed from: d  reason: collision with root package name */
        final w f7034d;

        /* renamed from: e  reason: collision with root package name */
        private Map<View, androidx.core.view.a> f7035e = new WeakHashMap();

        public a(w wVar) {
            this.f7034d = wVar;
        }

        @Override // androidx.core.view.a
        public boolean a(View view, AccessibilityEvent accessibilityEvent) {
            androidx.core.view.a aVar = this.f7035e.get(view);
            return aVar != null ? aVar.a(view, accessibilityEvent) : super.a(view, accessibilityEvent);
        }

        @Override // androidx.core.view.a
        public androidx.core.view.accessibility.d b(View view) {
            androidx.core.view.a aVar = this.f7035e.get(view);
            return aVar != null ? aVar.b(view) : super.b(view);
        }

        @Override // androidx.core.view.a
        public void f(View view, AccessibilityEvent accessibilityEvent) {
            androidx.core.view.a aVar = this.f7035e.get(view);
            if (aVar != null) {
                aVar.f(view, accessibilityEvent);
            } else {
                super.f(view, accessibilityEvent);
            }
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            if (!this.f7034d.o() && this.f7034d.f7032d.getLayoutManager() != null) {
                this.f7034d.f7032d.getLayoutManager().P0(view, cVar);
                androidx.core.view.a aVar = this.f7035e.get(view);
                if (aVar != null) {
                    aVar.g(view, cVar);
                    return;
                }
            }
            super.g(view, cVar);
        }

        @Override // androidx.core.view.a
        public void h(View view, AccessibilityEvent accessibilityEvent) {
            androidx.core.view.a aVar = this.f7035e.get(view);
            if (aVar != null) {
                aVar.h(view, accessibilityEvent);
            } else {
                super.h(view, accessibilityEvent);
            }
        }

        @Override // androidx.core.view.a
        public boolean i(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            androidx.core.view.a aVar = this.f7035e.get(viewGroup);
            return aVar != null ? aVar.i(viewGroup, view, accessibilityEvent) : super.i(viewGroup, view, accessibilityEvent);
        }

        @Override // androidx.core.view.a
        public boolean j(View view, int i8, Bundle bundle) {
            if (this.f7034d.o() || this.f7034d.f7032d.getLayoutManager() == null) {
                return super.j(view, i8, bundle);
            }
            androidx.core.view.a aVar = this.f7035e.get(view);
            if (aVar != null) {
                if (aVar.j(view, i8, bundle)) {
                    return true;
                }
            } else if (super.j(view, i8, bundle)) {
                return true;
            }
            return this.f7034d.f7032d.getLayoutManager().j1(view, i8, bundle);
        }

        @Override // androidx.core.view.a
        public void l(View view, int i8) {
            androidx.core.view.a aVar = this.f7035e.get(view);
            if (aVar != null) {
                aVar.l(view, i8);
            } else {
                super.l(view, i8);
            }
        }

        @Override // androidx.core.view.a
        public void m(View view, AccessibilityEvent accessibilityEvent) {
            androidx.core.view.a aVar = this.f7035e.get(view);
            if (aVar != null) {
                aVar.m(view, accessibilityEvent);
            } else {
                super.m(view, accessibilityEvent);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public androidx.core.view.a n(View view) {
            return this.f7035e.remove(view);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void o(View view) {
            androidx.core.view.a n8 = c0.n(view);
            if (n8 == null || n8 == this) {
                return;
            }
            this.f7035e.put(view, n8);
        }
    }

    public w(RecyclerView recyclerView) {
        this.f7032d = recyclerView;
        androidx.core.view.a n8 = n();
        this.f7033e = (n8 == null || !(n8 instanceof a)) ? new a(this) : (a) n8;
    }

    @Override // androidx.core.view.a
    public void f(View view, AccessibilityEvent accessibilityEvent) {
        super.f(view, accessibilityEvent);
        if (!(view instanceof RecyclerView) || o()) {
            return;
        }
        RecyclerView recyclerView = (RecyclerView) view;
        if (recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().L0(accessibilityEvent);
        }
    }

    @Override // androidx.core.view.a
    public void g(View view, androidx.core.view.accessibility.c cVar) {
        super.g(view, cVar);
        if (o() || this.f7032d.getLayoutManager() == null) {
            return;
        }
        this.f7032d.getLayoutManager().N0(cVar);
    }

    @Override // androidx.core.view.a
    public boolean j(View view, int i8, Bundle bundle) {
        if (super.j(view, i8, bundle)) {
            return true;
        }
        if (o() || this.f7032d.getLayoutManager() == null) {
            return false;
        }
        return this.f7032d.getLayoutManager().h1(i8, bundle);
    }

    public androidx.core.view.a n() {
        return this.f7033e;
    }

    boolean o() {
        return this.f7032d.p0();
    }
}

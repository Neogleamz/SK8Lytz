package androidx.core.view;

import android.os.Build;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.core.view.accessibility.c;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: c  reason: collision with root package name */
    private static final View.AccessibilityDelegate f4896c = new View.AccessibilityDelegate();

    /* renamed from: a  reason: collision with root package name */
    private final View.AccessibilityDelegate f4897a;

    /* renamed from: b  reason: collision with root package name */
    private final View.AccessibilityDelegate f4898b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.core.view.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0042a extends View.AccessibilityDelegate {

        /* renamed from: a  reason: collision with root package name */
        final a f4899a;

        C0042a(a aVar) {
            this.f4899a = aVar;
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            return this.f4899a.a(view, accessibilityEvent);
        }

        @Override // android.view.View.AccessibilityDelegate
        public AccessibilityNodeProvider getAccessibilityNodeProvider(View view) {
            androidx.core.view.accessibility.d b9 = this.f4899a.b(view);
            if (b9 != null) {
                return (AccessibilityNodeProvider) b9.e();
            }
            return null;
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            this.f4899a.f(view, accessibilityEvent);
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            androidx.core.view.accessibility.c I0 = androidx.core.view.accessibility.c.I0(accessibilityNodeInfo);
            I0.x0(c0.Z(view));
            I0.m0(c0.U(view));
            I0.s0(c0.r(view));
            I0.D0(c0.M(view));
            this.f4899a.g(view, I0);
            I0.f(accessibilityNodeInfo.getText(), view);
            List<c.a> c9 = a.c(view);
            for (int i8 = 0; i8 < c9.size(); i8++) {
                I0.b(c9.get(i8));
            }
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            this.f4899a.h(view, accessibilityEvent);
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            return this.f4899a.i(viewGroup, view, accessibilityEvent);
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean performAccessibilityAction(View view, int i8, Bundle bundle) {
            return this.f4899a.j(view, i8, bundle);
        }

        @Override // android.view.View.AccessibilityDelegate
        public void sendAccessibilityEvent(View view, int i8) {
            this.f4899a.l(view, i8);
        }

        @Override // android.view.View.AccessibilityDelegate
        public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
            this.f4899a.m(view, accessibilityEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static AccessibilityNodeProvider a(View.AccessibilityDelegate accessibilityDelegate, View view) {
            return accessibilityDelegate.getAccessibilityNodeProvider(view);
        }

        static boolean b(View.AccessibilityDelegate accessibilityDelegate, View view, int i8, Bundle bundle) {
            return accessibilityDelegate.performAccessibilityAction(view, i8, bundle);
        }
    }

    public a() {
        this(f4896c);
    }

    public a(View.AccessibilityDelegate accessibilityDelegate) {
        this.f4897a = accessibilityDelegate;
        this.f4898b = new C0042a(this);
    }

    static List<c.a> c(View view) {
        List<c.a> list = (List) view.getTag(q0.e.W);
        return list == null ? Collections.emptyList() : list;
    }

    private boolean e(ClickableSpan clickableSpan, View view) {
        if (clickableSpan != null) {
            ClickableSpan[] q = androidx.core.view.accessibility.c.q(view.createAccessibilityNodeInfo().getText());
            for (int i8 = 0; q != null && i8 < q.length; i8++) {
                if (clickableSpan.equals(q[i8])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean k(int i8, View view) {
        WeakReference weakReference;
        SparseArray sparseArray = (SparseArray) view.getTag(q0.e.X);
        if (sparseArray == null || (weakReference = (WeakReference) sparseArray.get(i8)) == null) {
            return false;
        }
        ClickableSpan clickableSpan = (ClickableSpan) weakReference.get();
        if (e(clickableSpan, view)) {
            clickableSpan.onClick(view);
            return true;
        }
        return false;
    }

    public boolean a(View view, AccessibilityEvent accessibilityEvent) {
        return this.f4897a.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    public androidx.core.view.accessibility.d b(View view) {
        AccessibilityNodeProvider a9;
        if (Build.VERSION.SDK_INT < 16 || (a9 = b.a(this.f4897a, view)) == null) {
            return null;
        }
        return new androidx.core.view.accessibility.d(a9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View.AccessibilityDelegate d() {
        return this.f4898b;
    }

    public void f(View view, AccessibilityEvent accessibilityEvent) {
        this.f4897a.onInitializeAccessibilityEvent(view, accessibilityEvent);
    }

    public void g(View view, androidx.core.view.accessibility.c cVar) {
        this.f4897a.onInitializeAccessibilityNodeInfo(view, cVar.H0());
    }

    public void h(View view, AccessibilityEvent accessibilityEvent) {
        this.f4897a.onPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    public boolean i(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        return this.f4897a.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }

    public boolean j(View view, int i8, Bundle bundle) {
        List<c.a> c9 = c(view);
        boolean z4 = false;
        int i9 = 0;
        while (true) {
            if (i9 >= c9.size()) {
                break;
            }
            c.a aVar = c9.get(i9);
            if (aVar.b() == i8) {
                z4 = aVar.d(view, bundle);
                break;
            }
            i9++;
        }
        if (!z4 && Build.VERSION.SDK_INT >= 16) {
            z4 = b.b(this.f4897a, view, i8, bundle);
        }
        return (z4 || i8 != q0.e.f22456a || bundle == null) ? z4 : k(bundle.getInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", -1), view);
    }

    public void l(View view, int i8) {
        this.f4897a.sendAccessibilityEvent(view, i8);
    }

    public void m(View view, AccessibilityEvent accessibilityEvent) {
        this.f4897a.sendAccessibilityEventUnchecked(view, accessibilityEvent);
    }
}

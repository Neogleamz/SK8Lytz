package androidx.core.view.accessibility;

import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {

    /* renamed from: a  reason: collision with root package name */
    private final Object f4935a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends AccessibilityNodeProvider {

        /* renamed from: a  reason: collision with root package name */
        final d f4936a;

        a(d dVar) {
            this.f4936a = dVar;
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i8) {
            androidx.core.view.accessibility.c b9 = this.f4936a.b(i8);
            if (b9 == null) {
                return null;
            }
            return b9.H0();
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String str, int i8) {
            List<androidx.core.view.accessibility.c> c9 = this.f4936a.c(str, i8);
            if (c9 == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            int size = c9.size();
            for (int i9 = 0; i9 < size; i9++) {
                arrayList.add(c9.get(i9).H0());
            }
            return arrayList;
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public boolean performAction(int i8, int i9, Bundle bundle) {
            return this.f4936a.f(i8, i9, bundle);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends a {
        b(d dVar) {
            super(dVar);
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo findFocus(int i8) {
            androidx.core.view.accessibility.c d8 = this.f4936a.d(i8);
            if (d8 == null) {
                return null;
            }
            return d8.H0();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends b {
        c(d dVar) {
            super(dVar);
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public void addExtraDataToAccessibilityNodeInfo(int i8, AccessibilityNodeInfo accessibilityNodeInfo, String str, Bundle bundle) {
            this.f4936a.a(i8, androidx.core.view.accessibility.c.I0(accessibilityNodeInfo), str, bundle);
        }
    }

    public d() {
        int i8 = Build.VERSION.SDK_INT;
        this.f4935a = i8 >= 26 ? new c(this) : i8 >= 19 ? new b(this) : i8 >= 16 ? new a(this) : null;
    }

    public d(Object obj) {
        this.f4935a = obj;
    }

    public void a(int i8, androidx.core.view.accessibility.c cVar, String str, Bundle bundle) {
    }

    public androidx.core.view.accessibility.c b(int i8) {
        return null;
    }

    public List<androidx.core.view.accessibility.c> c(String str, int i8) {
        return null;
    }

    public androidx.core.view.accessibility.c d(int i8) {
        return null;
    }

    public Object e() {
        return this.f4935a;
    }

    public boolean f(int i8, int i9, Bundle bundle) {
        return false;
    }
}

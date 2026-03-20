package androidx.navigation.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.i;
import androidx.navigation.q;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
@q.b("fragment")
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends q<C0069a> {

    /* renamed from: a  reason: collision with root package name */
    private final Context f6335a;

    /* renamed from: b  reason: collision with root package name */
    private final FragmentManager f6336b;

    /* renamed from: c  reason: collision with root package name */
    private final int f6337c;

    /* renamed from: d  reason: collision with root package name */
    private ArrayDeque<Integer> f6338d = new ArrayDeque<>();

    /* renamed from: androidx.navigation.fragment.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0069a extends i {

        /* renamed from: k  reason: collision with root package name */
        private String f6339k;

        public C0069a(q<? extends C0069a> qVar) {
            super(qVar);
        }

        public final String F() {
            String str = this.f6339k;
            if (str != null) {
                return str;
            }
            throw new IllegalStateException("Fragment class was not set");
        }

        public final C0069a G(String str) {
            this.f6339k = str;
            return this;
        }

        @Override // androidx.navigation.i
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append(" class=");
            String str = this.f6339k;
            if (str == null) {
                str = "null";
            }
            sb.append(str);
            return sb.toString();
        }

        @Override // androidx.navigation.i
        public void x(Context context, AttributeSet attributeSet) {
            super.x(context, attributeSet);
            TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, d.f6355i);
            String string = obtainAttributes.getString(d.f6356j);
            if (string != null) {
                G(string);
            }
            obtainAttributes.recycle();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements q.a {

        /* renamed from: a  reason: collision with root package name */
        private final LinkedHashMap<View, String> f6340a;

        public Map<View, String> a() {
            return Collections.unmodifiableMap(this.f6340a);
        }
    }

    public a(Context context, FragmentManager fragmentManager, int i8) {
        this.f6335a = context;
        this.f6336b = fragmentManager;
        this.f6337c = i8;
    }

    private String g(int i8, int i9) {
        return i8 + "-" + i9;
    }

    @Override // androidx.navigation.q
    public void c(Bundle bundle) {
        int[] intArray;
        if (bundle == null || (intArray = bundle.getIntArray("androidx-nav-fragment:navigator:backStackIds")) == null) {
            return;
        }
        this.f6338d.clear();
        for (int i8 : intArray) {
            this.f6338d.add(Integer.valueOf(i8));
        }
    }

    @Override // androidx.navigation.q
    public Bundle d() {
        Bundle bundle = new Bundle();
        int[] iArr = new int[this.f6338d.size()];
        Iterator<Integer> it = this.f6338d.iterator();
        int i8 = 0;
        while (it.hasNext()) {
            iArr[i8] = it.next().intValue();
            i8++;
        }
        bundle.putIntArray("androidx-nav-fragment:navigator:backStackIds", iArr);
        return bundle;
    }

    @Override // androidx.navigation.q
    public boolean e() {
        if (this.f6338d.isEmpty()) {
            return false;
        }
        if (this.f6336b.K0()) {
            Log.i("FragmentNavigator", "Ignoring popBackStack() call: FragmentManager has already saved its state");
            return false;
        }
        this.f6336b.X0(g(this.f6338d.size(), this.f6338d.peekLast().intValue()), 1);
        this.f6338d.removeLast();
        return true;
    }

    @Override // androidx.navigation.q
    /* renamed from: f */
    public C0069a a() {
        return new C0069a(this);
    }

    @Deprecated
    public Fragment h(Context context, FragmentManager fragmentManager, String str, Bundle bundle) {
        return fragmentManager.q0().a(context.getClassLoader(), str);
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0134 A[RETURN] */
    @Override // androidx.navigation.q
    /* renamed from: i */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public androidx.navigation.i b(androidx.navigation.fragment.a.C0069a r9, android.os.Bundle r10, androidx.navigation.n r11, androidx.navigation.q.a r12) {
        /*
            Method dump skipped, instructions count: 309
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.navigation.fragment.a.b(androidx.navigation.fragment.a$a, android.os.Bundle, androidx.navigation.n, androidx.navigation.q$a):androidx.navigation.i");
    }
}

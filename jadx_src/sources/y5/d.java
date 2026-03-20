package y5;

import android.text.TextUtils;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: f  reason: collision with root package name */
    private int f24433f;

    /* renamed from: h  reason: collision with root package name */
    private int f24435h;

    /* renamed from: o  reason: collision with root package name */
    private float f24442o;

    /* renamed from: a  reason: collision with root package name */
    private String f24428a = BuildConfig.FLAVOR;

    /* renamed from: b  reason: collision with root package name */
    private String f24429b = BuildConfig.FLAVOR;

    /* renamed from: c  reason: collision with root package name */
    private Set<String> f24430c = Collections.emptySet();

    /* renamed from: d  reason: collision with root package name */
    private String f24431d = BuildConfig.FLAVOR;

    /* renamed from: e  reason: collision with root package name */
    private String f24432e = null;

    /* renamed from: g  reason: collision with root package name */
    private boolean f24434g = false;

    /* renamed from: i  reason: collision with root package name */
    private boolean f24436i = false;

    /* renamed from: j  reason: collision with root package name */
    private int f24437j = -1;

    /* renamed from: k  reason: collision with root package name */
    private int f24438k = -1;

    /* renamed from: l  reason: collision with root package name */
    private int f24439l = -1;

    /* renamed from: m  reason: collision with root package name */
    private int f24440m = -1;

    /* renamed from: n  reason: collision with root package name */
    private int f24441n = -1;

    /* renamed from: p  reason: collision with root package name */
    private int f24443p = -1;
    private boolean q = false;

    private static int B(int i8, String str, String str2, int i9) {
        if (str.isEmpty() || i8 == -1) {
            return i8;
        }
        if (str.equals(str2)) {
            return i8 + i9;
        }
        return -1;
    }

    public d A(boolean z4) {
        this.f24438k = z4 ? 1 : 0;
        return this;
    }

    public int a() {
        if (this.f24436i) {
            return this.f24435h;
        }
        throw new IllegalStateException("Background color not defined.");
    }

    public boolean b() {
        return this.q;
    }

    public int c() {
        if (this.f24434g) {
            return this.f24433f;
        }
        throw new IllegalStateException("Font color not defined");
    }

    public String d() {
        return this.f24432e;
    }

    public float e() {
        return this.f24442o;
    }

    public int f() {
        return this.f24441n;
    }

    public int g() {
        return this.f24443p;
    }

    public int h(String str, String str2, Set<String> set, String str3) {
        if (this.f24428a.isEmpty() && this.f24429b.isEmpty() && this.f24430c.isEmpty() && this.f24431d.isEmpty()) {
            return TextUtils.isEmpty(str2) ? 1 : 0;
        }
        int B = B(B(B(0, this.f24428a, str, 1073741824), this.f24429b, str2, 2), this.f24431d, str3, 4);
        if (B == -1 || !set.containsAll(this.f24430c)) {
            return 0;
        }
        return B + (this.f24430c.size() * 4);
    }

    public int i() {
        int i8 = this.f24439l;
        if (i8 == -1 && this.f24440m == -1) {
            return -1;
        }
        return (i8 == 1 ? 1 : 0) | (this.f24440m == 1 ? 2 : 0);
    }

    public boolean j() {
        return this.f24436i;
    }

    public boolean k() {
        return this.f24434g;
    }

    public boolean l() {
        return this.f24437j == 1;
    }

    public boolean m() {
        return this.f24438k == 1;
    }

    public d n(int i8) {
        this.f24435h = i8;
        this.f24436i = true;
        return this;
    }

    public d o(boolean z4) {
        this.f24439l = z4 ? 1 : 0;
        return this;
    }

    public d p(boolean z4) {
        this.q = z4;
        return this;
    }

    public d q(int i8) {
        this.f24433f = i8;
        this.f24434g = true;
        return this;
    }

    public d r(String str) {
        this.f24432e = str == null ? null : com.google.common.base.c.e(str);
        return this;
    }

    public d s(float f5) {
        this.f24442o = f5;
        return this;
    }

    public d t(int i8) {
        this.f24441n = i8;
        return this;
    }

    public d u(boolean z4) {
        this.f24440m = z4 ? 1 : 0;
        return this;
    }

    public d v(int i8) {
        this.f24443p = i8;
        return this;
    }

    public void w(String[] strArr) {
        this.f24430c = new HashSet(Arrays.asList(strArr));
    }

    public void x(String str) {
        this.f24428a = str;
    }

    public void y(String str) {
        this.f24429b = str;
    }

    public void z(String str) {
        this.f24431d = str;
    }
}

package androidx.fragment.app;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.r;
import androidx.lifecycle.Lifecycle;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.PrintWriter;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends r implements FragmentManager.n {

    /* renamed from: t  reason: collision with root package name */
    final FragmentManager f5545t;

    /* renamed from: u  reason: collision with root package name */
    boolean f5546u;

    /* renamed from: v  reason: collision with root package name */
    int f5547v;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(FragmentManager fragmentManager) {
        super(fragmentManager.q0(), fragmentManager.t0() != null ? fragmentManager.t0().f().getClassLoader() : null);
        this.f5547v = -1;
        this.f5545t = fragmentManager;
    }

    private static boolean G(r.a aVar) {
        Fragment fragment = aVar.f5682b;
        return (fragment == null || !fragment.f5412m || fragment.T == null || fragment.H || fragment.G || !fragment.f0()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A() {
        int size = this.f5665c.size();
        for (int i8 = 0; i8 < size; i8++) {
            r.a aVar = this.f5665c.get(i8);
            Fragment fragment = aVar.f5682b;
            if (fragment != null) {
                fragment.z1(false);
                fragment.x1(this.f5670h);
                fragment.B1(this.f5678p, this.q);
            }
            switch (aVar.f5681a) {
                case 1:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.k1(fragment, false);
                    this.f5545t.g(fragment);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + aVar.f5681a);
                case 3:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.d1(fragment);
                    break;
                case 4:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.C0(fragment);
                    break;
                case 5:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.k1(fragment, false);
                    this.f5545t.o1(fragment);
                    break;
                case 6:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.x(fragment);
                    break;
                case 7:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.k1(fragment, false);
                    this.f5545t.k(fragment);
                    break;
                case 8:
                    this.f5545t.m1(fragment);
                    break;
                case 9:
                    this.f5545t.m1(null);
                    break;
                case 10:
                    this.f5545t.l1(fragment, aVar.f5688h);
                    break;
            }
            if (!this.f5679r && aVar.f5681a != 1 && fragment != null && !FragmentManager.P) {
                this.f5545t.P0(fragment);
            }
        }
        if (this.f5679r || FragmentManager.P) {
            return;
        }
        FragmentManager fragmentManager = this.f5545t;
        fragmentManager.Q0(fragmentManager.q, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B(boolean z4) {
        for (int size = this.f5665c.size() - 1; size >= 0; size--) {
            r.a aVar = this.f5665c.get(size);
            Fragment fragment = aVar.f5682b;
            if (fragment != null) {
                fragment.z1(true);
                fragment.x1(FragmentManager.h1(this.f5670h));
                fragment.B1(this.q, this.f5678p);
            }
            switch (aVar.f5681a) {
                case 1:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.k1(fragment, true);
                    this.f5545t.d1(fragment);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + aVar.f5681a);
                case 3:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.g(fragment);
                    break;
                case 4:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.o1(fragment);
                    break;
                case 5:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.k1(fragment, true);
                    this.f5545t.C0(fragment);
                    break;
                case 6:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.k(fragment);
                    break;
                case 7:
                    fragment.r1(aVar.f5683c, aVar.f5684d, aVar.f5685e, aVar.f5686f);
                    this.f5545t.k1(fragment, true);
                    this.f5545t.x(fragment);
                    break;
                case 8:
                    this.f5545t.m1(null);
                    break;
                case 9:
                    this.f5545t.m1(fragment);
                    break;
                case 10:
                    this.f5545t.l1(fragment, aVar.f5687g);
                    break;
            }
            if (!this.f5679r && aVar.f5681a != 3 && fragment != null && !FragmentManager.P) {
                this.f5545t.P0(fragment);
            }
        }
        if (this.f5679r || !z4 || FragmentManager.P) {
            return;
        }
        FragmentManager fragmentManager = this.f5545t;
        fragmentManager.Q0(fragmentManager.q, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment C(ArrayList<Fragment> arrayList, Fragment fragment) {
        Fragment fragment2 = fragment;
        int i8 = 0;
        while (i8 < this.f5665c.size()) {
            r.a aVar = this.f5665c.get(i8);
            int i9 = aVar.f5681a;
            if (i9 != 1) {
                if (i9 == 2) {
                    Fragment fragment3 = aVar.f5682b;
                    int i10 = fragment3.E;
                    boolean z4 = false;
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        Fragment fragment4 = arrayList.get(size);
                        if (fragment4.E == i10) {
                            if (fragment4 == fragment3) {
                                z4 = true;
                            } else {
                                if (fragment4 == fragment2) {
                                    this.f5665c.add(i8, new r.a(9, fragment4));
                                    i8++;
                                    fragment2 = null;
                                }
                                r.a aVar2 = new r.a(3, fragment4);
                                aVar2.f5683c = aVar.f5683c;
                                aVar2.f5685e = aVar.f5685e;
                                aVar2.f5684d = aVar.f5684d;
                                aVar2.f5686f = aVar.f5686f;
                                this.f5665c.add(i8, aVar2);
                                arrayList.remove(fragment4);
                                i8++;
                            }
                        }
                    }
                    if (z4) {
                        this.f5665c.remove(i8);
                        i8--;
                    } else {
                        aVar.f5681a = 1;
                        arrayList.add(fragment3);
                    }
                } else if (i9 == 3 || i9 == 6) {
                    arrayList.remove(aVar.f5682b);
                    Fragment fragment5 = aVar.f5682b;
                    if (fragment5 == fragment2) {
                        this.f5665c.add(i8, new r.a(9, fragment5));
                        i8++;
                        fragment2 = null;
                    }
                } else if (i9 != 7) {
                    if (i9 == 8) {
                        this.f5665c.add(i8, new r.a(9, fragment2));
                        i8++;
                        fragment2 = aVar.f5682b;
                    }
                }
                i8++;
            }
            arrayList.add(aVar.f5682b);
            i8++;
        }
        return fragment2;
    }

    public String D() {
        return this.f5673k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean E(int i8) {
        int size = this.f5665c.size();
        for (int i9 = 0; i9 < size; i9++) {
            Fragment fragment = this.f5665c.get(i9).f5682b;
            int i10 = fragment != null ? fragment.E : 0;
            if (i10 != 0 && i10 == i8) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean F(ArrayList<a> arrayList, int i8, int i9) {
        if (i9 == i8) {
            return false;
        }
        int size = this.f5665c.size();
        int i10 = -1;
        for (int i11 = 0; i11 < size; i11++) {
            Fragment fragment = this.f5665c.get(i11).f5682b;
            int i12 = fragment != null ? fragment.E : 0;
            if (i12 != 0 && i12 != i10) {
                for (int i13 = i8; i13 < i9; i13++) {
                    a aVar = arrayList.get(i13);
                    int size2 = aVar.f5665c.size();
                    for (int i14 = 0; i14 < size2; i14++) {
                        Fragment fragment2 = aVar.f5665c.get(i14).f5682b;
                        if ((fragment2 != null ? fragment2.E : 0) == i12) {
                            return true;
                        }
                    }
                }
                i10 = i12;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean H() {
        for (int i8 = 0; i8 < this.f5665c.size(); i8++) {
            if (G(this.f5665c.get(i8))) {
                return true;
            }
        }
        return false;
    }

    public void I() {
        if (this.f5680s != null) {
            for (int i8 = 0; i8 < this.f5680s.size(); i8++) {
                this.f5680s.get(i8).run();
            }
            this.f5680s = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(Fragment.g gVar) {
        for (int i8 = 0; i8 < this.f5665c.size(); i8++) {
            r.a aVar = this.f5665c.get(i8);
            if (G(aVar)) {
                aVar.f5682b.y1(gVar);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment K(ArrayList<Fragment> arrayList, Fragment fragment) {
        for (int size = this.f5665c.size() - 1; size >= 0; size--) {
            r.a aVar = this.f5665c.get(size);
            int i8 = aVar.f5681a;
            if (i8 != 1) {
                if (i8 != 3) {
                    switch (i8) {
                        case 8:
                            fragment = null;
                            break;
                        case 9:
                            fragment = aVar.f5682b;
                            break;
                        case 10:
                            aVar.f5688h = aVar.f5687g;
                            break;
                    }
                }
                arrayList.add(aVar.f5682b);
            }
            arrayList.remove(aVar.f5682b);
        }
        return fragment;
    }

    @Override // androidx.fragment.app.FragmentManager.n
    public boolean a(ArrayList<a> arrayList, ArrayList<Boolean> arrayList2) {
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "Run: " + this);
        }
        arrayList.add(this);
        arrayList2.add(Boolean.FALSE);
        if (this.f5671i) {
            this.f5545t.e(this);
            return true;
        }
        return true;
    }

    @Override // androidx.fragment.app.r
    public int i() {
        return x(false);
    }

    @Override // androidx.fragment.app.r
    public int j() {
        return x(true);
    }

    @Override // androidx.fragment.app.r
    public void k() {
        n();
        this.f5545t.b0(this, false);
    }

    @Override // androidx.fragment.app.r
    public void l() {
        n();
        this.f5545t.b0(this, true);
    }

    @Override // androidx.fragment.app.r
    public r m(Fragment fragment) {
        FragmentManager fragmentManager = fragment.f5420y;
        if (fragmentManager == null || fragmentManager == this.f5545t) {
            return super.m(fragment);
        }
        throw new IllegalStateException("Cannot detach Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.fragment.app.r
    public void o(int i8, Fragment fragment, String str, int i9) {
        super.o(i8, fragment, str, i9);
        fragment.f5420y = this.f5545t;
    }

    @Override // androidx.fragment.app.r
    public r p(Fragment fragment) {
        FragmentManager fragmentManager = fragment.f5420y;
        if (fragmentManager == null || fragmentManager == this.f5545t) {
            return super.p(fragment);
        }
        throw new IllegalStateException("Cannot remove Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
    }

    @Override // androidx.fragment.app.r
    public r t(Fragment fragment, Lifecycle.State state) {
        if (fragment.f5420y != this.f5545t) {
            throw new IllegalArgumentException("Cannot setMaxLifecycle for Fragment not attached to FragmentManager " + this.f5545t);
        } else if (state == Lifecycle.State.INITIALIZED && fragment.f5389a > -1) {
            throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + state + " after the Fragment has been created");
        } else if (state != Lifecycle.State.DESTROYED) {
            return super.t(fragment, state);
        } else {
            throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + state + ". Use remove() to remove the fragment from the FragmentManager and trigger its destruction.");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder((int) RecognitionOptions.ITF);
        sb.append("BackStackEntry{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.f5547v >= 0) {
            sb.append(" #");
            sb.append(this.f5547v);
        }
        if (this.f5673k != null) {
            sb.append(" ");
            sb.append(this.f5673k);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override // androidx.fragment.app.r
    public r u(Fragment fragment) {
        FragmentManager fragmentManager;
        if (fragment == null || (fragmentManager = fragment.f5420y) == null || fragmentManager == this.f5545t) {
            return super.u(fragment);
        }
        throw new IllegalStateException("Cannot setPrimaryNavigation for Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(int i8) {
        r.a aVar;
        if (this.f5671i) {
            if (FragmentManager.F0(2)) {
                Log.v("FragmentManager", "Bump nesting in " + this + " by " + i8);
            }
            int size = this.f5665c.size();
            for (int i9 = 0; i9 < size; i9++) {
                Fragment fragment = this.f5665c.get(i9).f5682b;
                if (fragment != null) {
                    fragment.f5419x += i8;
                    if (FragmentManager.F0(2)) {
                        Log.v("FragmentManager", "Bump nesting of " + aVar.f5682b + " to " + aVar.f5682b.f5419x);
                    }
                }
            }
        }
    }

    int x(boolean z4) {
        if (this.f5546u) {
            throw new IllegalStateException("commit already called");
        }
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "Commit: " + this);
            PrintWriter printWriter = new PrintWriter(new w("FragmentManager"));
            y("  ", printWriter);
            printWriter.close();
        }
        this.f5546u = true;
        this.f5547v = this.f5671i ? this.f5545t.i() : -1;
        this.f5545t.Y(this, z4);
        return this.f5547v;
    }

    public void y(String str, PrintWriter printWriter) {
        z(str, printWriter, true);
    }

    public void z(String str, PrintWriter printWriter, boolean z4) {
        String str2;
        if (z4) {
            printWriter.print(str);
            printWriter.print("mName=");
            printWriter.print(this.f5673k);
            printWriter.print(" mIndex=");
            printWriter.print(this.f5547v);
            printWriter.print(" mCommitted=");
            printWriter.println(this.f5546u);
            if (this.f5670h != 0) {
                printWriter.print(str);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.f5670h));
            }
            if (this.f5666d != 0 || this.f5667e != 0) {
                printWriter.print(str);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.f5666d));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.f5667e));
            }
            if (this.f5668f != 0 || this.f5669g != 0) {
                printWriter.print(str);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.f5668f));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.f5669g));
            }
            if (this.f5674l != 0 || this.f5675m != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.f5674l));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.f5675m);
            }
            if (this.f5676n != 0 || this.f5677o != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.f5676n));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.f5677o);
            }
        }
        if (this.f5665c.isEmpty()) {
            return;
        }
        printWriter.print(str);
        printWriter.println("Operations:");
        int size = this.f5665c.size();
        for (int i8 = 0; i8 < size; i8++) {
            r.a aVar = this.f5665c.get(i8);
            switch (aVar.f5681a) {
                case 0:
                    str2 = "NULL";
                    break;
                case 1:
                    str2 = "ADD";
                    break;
                case 2:
                    str2 = "REPLACE";
                    break;
                case 3:
                    str2 = "REMOVE";
                    break;
                case 4:
                    str2 = "HIDE";
                    break;
                case 5:
                    str2 = "SHOW";
                    break;
                case 6:
                    str2 = "DETACH";
                    break;
                case 7:
                    str2 = "ATTACH";
                    break;
                case 8:
                    str2 = "SET_PRIMARY_NAV";
                    break;
                case 9:
                    str2 = "UNSET_PRIMARY_NAV";
                    break;
                case 10:
                    str2 = "OP_SET_MAX_LIFECYCLE";
                    break;
                default:
                    str2 = "cmd=" + aVar.f5681a;
                    break;
            }
            printWriter.print(str);
            printWriter.print("  Op #");
            printWriter.print(i8);
            printWriter.print(": ");
            printWriter.print(str2);
            printWriter.print(" ");
            printWriter.println(aVar.f5682b);
            if (z4) {
                if (aVar.f5683c != 0 || aVar.f5684d != 0) {
                    printWriter.print(str);
                    printWriter.print("enterAnim=#");
                    printWriter.print(Integer.toHexString(aVar.f5683c));
                    printWriter.print(" exitAnim=#");
                    printWriter.println(Integer.toHexString(aVar.f5684d));
                }
                if (aVar.f5685e != 0 || aVar.f5686f != 0) {
                    printWriter.print(str);
                    printWriter.print("popEnterAnim=#");
                    printWriter.print(Integer.toHexString(aVar.f5685e));
                    printWriter.print(" popExitAnim=#");
                    printWriter.println(Integer.toHexString(aVar.f5686f));
                }
            }
        }
    }
}

package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q {

    /* renamed from: a  reason: collision with root package name */
    private final ArrayList<Fragment> f5660a = new ArrayList<>();

    /* renamed from: b  reason: collision with root package name */
    private final HashMap<String, p> f5661b = new HashMap<>();

    /* renamed from: c  reason: collision with root package name */
    private l f5662c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Fragment fragment) {
        if (this.f5660a.contains(fragment)) {
            throw new IllegalStateException("Fragment already added: " + fragment);
        }
        synchronized (this.f5660a) {
            this.f5660a.add(fragment);
        }
        fragment.f5412m = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        this.f5661b.values().removeAll(Collections.singleton(null));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c(String str) {
        return this.f5661b.get(str) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(int i8) {
        for (p pVar : this.f5661b.values()) {
            if (pVar != null) {
                pVar.t(i8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str2 = str + "    ";
        if (!this.f5661b.isEmpty()) {
            printWriter.print(str);
            printWriter.println("Active Fragments:");
            for (p pVar : this.f5661b.values()) {
                printWriter.print(str);
                if (pVar != null) {
                    Fragment k8 = pVar.k();
                    printWriter.println(k8);
                    k8.g(str2, fileDescriptor, printWriter, strArr);
                } else {
                    printWriter.println("null");
                }
            }
        }
        int size = this.f5660a.size();
        if (size > 0) {
            printWriter.print(str);
            printWriter.println("Added Fragments:");
            for (int i8 = 0; i8 < size; i8++) {
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i8);
                printWriter.print(": ");
                printWriter.println(this.f5660a.get(i8).toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment f(String str) {
        p pVar = this.f5661b.get(str);
        if (pVar != null) {
            return pVar.k();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment g(int i8) {
        for (int size = this.f5660a.size() - 1; size >= 0; size--) {
            Fragment fragment = this.f5660a.get(size);
            if (fragment != null && fragment.C == i8) {
                return fragment;
            }
        }
        for (p pVar : this.f5661b.values()) {
            if (pVar != null) {
                Fragment k8 = pVar.k();
                if (k8.C == i8) {
                    return k8;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment h(String str) {
        if (str != null) {
            for (int size = this.f5660a.size() - 1; size >= 0; size--) {
                Fragment fragment = this.f5660a.get(size);
                if (fragment != null && str.equals(fragment.F)) {
                    return fragment;
                }
            }
        }
        if (str != null) {
            for (p pVar : this.f5661b.values()) {
                if (pVar != null) {
                    Fragment k8 = pVar.k();
                    if (str.equals(k8.F)) {
                        return k8;
                    }
                }
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment i(String str) {
        Fragment j8;
        for (p pVar : this.f5661b.values()) {
            if (pVar != null && (j8 = pVar.k().j(str)) != null) {
                return j8;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j(Fragment fragment) {
        View view;
        View view2;
        ViewGroup viewGroup = fragment.R;
        if (viewGroup == null) {
            return -1;
        }
        int indexOf = this.f5660a.indexOf(fragment);
        for (int i8 = indexOf - 1; i8 >= 0; i8--) {
            Fragment fragment2 = this.f5660a.get(i8);
            if (fragment2.R == viewGroup && (view2 = fragment2.T) != null) {
                return viewGroup.indexOfChild(view2) + 1;
            }
        }
        while (true) {
            indexOf++;
            if (indexOf >= this.f5660a.size()) {
                return -1;
            }
            Fragment fragment3 = this.f5660a.get(indexOf);
            if (fragment3.R == viewGroup && (view = fragment3.T) != null) {
                return viewGroup.indexOfChild(view);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<p> k() {
        ArrayList arrayList = new ArrayList();
        for (p pVar : this.f5661b.values()) {
            if (pVar != null) {
                arrayList.add(pVar);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Fragment> l() {
        ArrayList arrayList = new ArrayList();
        Iterator<p> it = this.f5661b.values().iterator();
        while (it.hasNext()) {
            p next = it.next();
            arrayList.add(next != null ? next.k() : null);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p m(String str) {
        return this.f5661b.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Fragment> n() {
        ArrayList arrayList;
        if (this.f5660a.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (this.f5660a) {
            arrayList = new ArrayList(this.f5660a);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public l o() {
        return this.f5662c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(p pVar) {
        Fragment k8 = pVar.k();
        if (c(k8.f5399f)) {
            return;
        }
        this.f5661b.put(k8.f5399f, pVar);
        if (k8.L) {
            if (k8.K) {
                this.f5662c.f(k8);
            } else {
                this.f5662c.n(k8);
            }
            k8.L = false;
        }
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "Added fragment to active set " + k8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(p pVar) {
        Fragment k8 = pVar.k();
        if (k8.K) {
            this.f5662c.n(k8);
        }
        if (this.f5661b.put(k8.f5399f, null) != null && FragmentManager.F0(2)) {
            Log.v("FragmentManager", "Removed fragment from active set " + k8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        Iterator<Fragment> it = this.f5660a.iterator();
        while (it.hasNext()) {
            p pVar = this.f5661b.get(it.next().f5399f);
            if (pVar != null) {
                pVar.m();
            }
        }
        for (p pVar2 : this.f5661b.values()) {
            if (pVar2 != null) {
                pVar2.m();
                Fragment k8 = pVar2.k();
                if (k8.f5414n && !k8.d0()) {
                    q(pVar2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(Fragment fragment) {
        synchronized (this.f5660a) {
            this.f5660a.remove(fragment);
        }
        fragment.f5412m = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t() {
        this.f5661b.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(List<String> list) {
        this.f5660a.clear();
        if (list != null) {
            for (String str : list) {
                Fragment f5 = f(str);
                if (f5 == null) {
                    throw new IllegalStateException("No instantiated fragment for (" + str + ")");
                }
                if (FragmentManager.F0(2)) {
                    Log.v("FragmentManager", "restoreSaveState: added (" + str + "): " + f5);
                }
                a(f5);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<FragmentState> v() {
        ArrayList<FragmentState> arrayList = new ArrayList<>(this.f5661b.size());
        for (p pVar : this.f5661b.values()) {
            if (pVar != null) {
                Fragment k8 = pVar.k();
                FragmentState r4 = pVar.r();
                arrayList.add(r4);
                if (FragmentManager.F0(2)) {
                    Log.v("FragmentManager", "Saved state of " + k8 + ": " + r4.f5532n);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<String> w() {
        synchronized (this.f5660a) {
            if (this.f5660a.isEmpty()) {
                return null;
            }
            ArrayList<String> arrayList = new ArrayList<>(this.f5660a.size());
            Iterator<Fragment> it = this.f5660a.iterator();
            while (it.hasNext()) {
                Fragment next = it.next();
                arrayList.add(next.f5399f);
                if (FragmentManager.F0(2)) {
                    Log.v("FragmentManager", "saveAllState: adding fragment (" + next.f5399f + "): " + next);
                }
            }
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(l lVar) {
        this.f5662c = lVar;
    }
}

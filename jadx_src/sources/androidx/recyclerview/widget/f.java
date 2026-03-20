package androidx.recyclerview.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f {

    /* renamed from: a  reason: collision with root package name */
    final b f6826a;

    /* renamed from: b  reason: collision with root package name */
    final a f6827b = new a();

    /* renamed from: c  reason: collision with root package name */
    final List<View> f6828c = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        long f6829a = 0;

        /* renamed from: b  reason: collision with root package name */
        a f6830b;

        a() {
        }

        private void c() {
            if (this.f6830b == null) {
                this.f6830b = new a();
            }
        }

        void a(int i8) {
            if (i8 < 64) {
                this.f6829a &= ~(1 << i8);
                return;
            }
            a aVar = this.f6830b;
            if (aVar != null) {
                aVar.a(i8 - 64);
            }
        }

        int b(int i8) {
            a aVar = this.f6830b;
            return aVar == null ? i8 >= 64 ? Long.bitCount(this.f6829a) : Long.bitCount(this.f6829a & ((1 << i8) - 1)) : i8 < 64 ? Long.bitCount(this.f6829a & ((1 << i8) - 1)) : aVar.b(i8 - 64) + Long.bitCount(this.f6829a);
        }

        boolean d(int i8) {
            if (i8 < 64) {
                return (this.f6829a & (1 << i8)) != 0;
            }
            c();
            return this.f6830b.d(i8 - 64);
        }

        void e(int i8, boolean z4) {
            if (i8 >= 64) {
                c();
                this.f6830b.e(i8 - 64, z4);
                return;
            }
            long j8 = this.f6829a;
            boolean z8 = (Long.MIN_VALUE & j8) != 0;
            long j9 = (1 << i8) - 1;
            this.f6829a = ((j8 & (~j9)) << 1) | (j8 & j9);
            if (z4) {
                h(i8);
            } else {
                a(i8);
            }
            if (z8 || this.f6830b != null) {
                c();
                this.f6830b.e(0, z8);
            }
        }

        boolean f(int i8) {
            if (i8 >= 64) {
                c();
                return this.f6830b.f(i8 - 64);
            }
            long j8 = 1 << i8;
            long j9 = this.f6829a;
            boolean z4 = (j9 & j8) != 0;
            long j10 = j9 & (~j8);
            this.f6829a = j10;
            long j11 = j8 - 1;
            this.f6829a = (j10 & j11) | Long.rotateRight((~j11) & j10, 1);
            a aVar = this.f6830b;
            if (aVar != null) {
                if (aVar.d(0)) {
                    h(63);
                }
                this.f6830b.f(0);
            }
            return z4;
        }

        void g() {
            this.f6829a = 0L;
            a aVar = this.f6830b;
            if (aVar != null) {
                aVar.g();
            }
        }

        void h(int i8) {
            if (i8 < 64) {
                this.f6829a |= 1 << i8;
                return;
            }
            c();
            this.f6830b.h(i8 - 64);
        }

        public String toString() {
            if (this.f6830b == null) {
                return Long.toBinaryString(this.f6829a);
            }
            return this.f6830b.toString() + "xx" + Long.toBinaryString(this.f6829a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        View a(int i8);

        void b(View view);

        int c();

        void d();

        int e(View view);

        RecyclerView.b0 f(View view);

        void g(int i8);

        void h(View view);

        void i(View view, int i8);

        void j(int i8);

        void k(View view, int i8, ViewGroup.LayoutParams layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(b bVar) {
        this.f6826a = bVar;
    }

    private int h(int i8) {
        if (i8 < 0) {
            return -1;
        }
        int c9 = this.f6826a.c();
        int i9 = i8;
        while (i9 < c9) {
            int b9 = i8 - (i9 - this.f6827b.b(i9));
            if (b9 == 0) {
                while (this.f6827b.d(i9)) {
                    i9++;
                }
                return i9;
            }
            i9 += b9;
        }
        return -1;
    }

    private void l(View view) {
        this.f6828c.add(view);
        this.f6826a.b(view);
    }

    private boolean t(View view) {
        if (this.f6828c.remove(view)) {
            this.f6826a.h(view);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(View view, int i8, boolean z4) {
        int c9 = i8 < 0 ? this.f6826a.c() : h(i8);
        this.f6827b.e(c9, z4);
        if (z4) {
            l(view);
        }
        this.f6826a.i(view, c9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(View view, boolean z4) {
        a(view, -1, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(View view, int i8, ViewGroup.LayoutParams layoutParams, boolean z4) {
        int c9 = i8 < 0 ? this.f6826a.c() : h(i8);
        this.f6827b.e(c9, z4);
        if (z4) {
            l(view);
        }
        this.f6826a.k(view, c9, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(int i8) {
        int h8 = h(i8);
        this.f6827b.f(h8);
        this.f6826a.g(h8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View e(int i8) {
        int size = this.f6828c.size();
        for (int i9 = 0; i9 < size; i9++) {
            View view = this.f6828c.get(i9);
            RecyclerView.b0 f5 = this.f6826a.f(view);
            if (f5.m() == i8 && !f5.t() && !f5.v()) {
                return view;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View f(int i8) {
        return this.f6826a.a(h(i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g() {
        return this.f6826a.c() - this.f6828c.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View i(int i8) {
        return this.f6826a.a(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j() {
        return this.f6826a.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(View view) {
        int e8 = this.f6826a.e(view);
        if (e8 >= 0) {
            this.f6827b.h(e8);
            l(view);
            return;
        }
        throw new IllegalArgumentException("view is not a child, cannot hide " + view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int m(View view) {
        int e8 = this.f6826a.e(view);
        if (e8 == -1 || this.f6827b.d(e8)) {
            return -1;
        }
        return e8 - this.f6827b.b(e8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean n(View view) {
        return this.f6828c.contains(view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o() {
        this.f6827b.g();
        for (int size = this.f6828c.size() - 1; size >= 0; size--) {
            this.f6826a.h(this.f6828c.get(size));
            this.f6828c.remove(size);
        }
        this.f6826a.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(View view) {
        int e8 = this.f6826a.e(view);
        if (e8 < 0) {
            return;
        }
        if (this.f6827b.f(e8)) {
            t(view);
        }
        this.f6826a.j(e8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(int i8) {
        int h8 = h(i8);
        View a9 = this.f6826a.a(h8);
        if (a9 == null) {
            return;
        }
        if (this.f6827b.f(h8)) {
            t(a9);
        }
        this.f6826a.j(h8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean r(View view) {
        int e8 = this.f6826a.e(view);
        if (e8 == -1) {
            t(view);
            return true;
        } else if (this.f6827b.d(e8)) {
            this.f6827b.f(e8);
            t(view);
            this.f6826a.j(e8);
            return true;
        } else {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(View view) {
        int e8 = this.f6826a.e(view);
        if (e8 < 0) {
            throw new IllegalArgumentException("view is not a child, cannot hide " + view);
        } else if (this.f6827b.d(e8)) {
            this.f6827b.a(e8);
            t(view);
        } else {
            throw new RuntimeException("trying to unhide a view that was not hidden" + view);
        }
    }

    public String toString() {
        return this.f6827b.toString() + ", hidden list:" + this.f6828c.size();
    }
}

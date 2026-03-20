package androidx.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.navigation.i;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j extends i implements Iterable<i> {

    /* renamed from: k  reason: collision with root package name */
    final k0.h<i> f6401k;

    /* renamed from: l  reason: collision with root package name */
    private int f6402l;

    /* renamed from: m  reason: collision with root package name */
    private String f6403m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Iterator<i> {

        /* renamed from: a  reason: collision with root package name */
        private int f6404a = -1;

        /* renamed from: b  reason: collision with root package name */
        private boolean f6405b = false;

        a() {
        }

        @Override // java.util.Iterator
        /* renamed from: a */
        public i next() {
            if (hasNext()) {
                this.f6405b = true;
                k0.h<i> hVar = j.this.f6401k;
                int i8 = this.f6404a + 1;
                this.f6404a = i8;
                return hVar.q(i8);
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f6404a + 1 < j.this.f6401k.o();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.f6405b) {
                throw new IllegalStateException("You must call next() before you can remove an element");
            }
            j.this.f6401k.q(this.f6404a).D(null);
            j.this.f6401k.m(this.f6404a);
            this.f6404a--;
            this.f6405b = false;
        }
    }

    public j(q<? extends j> qVar) {
        super(qVar);
        this.f6401k = new k0.h<>();
    }

    public final void F(i iVar) {
        int q = iVar.q();
        if (q == 0) {
            throw new IllegalArgumentException("Destinations must have an id. Call setId() or include an android:id in your navigation XML.");
        }
        if (q == q()) {
            throw new IllegalArgumentException("Destination " + iVar + " cannot have the same id as graph " + this);
        }
        i f5 = this.f6401k.f(q);
        if (f5 == iVar) {
            return;
        }
        if (iVar.u() != null) {
            throw new IllegalStateException("Destination already has a parent set. Call NavGraph.remove() to remove the previous parent.");
        }
        if (f5 != null) {
            f5.D(null);
        }
        iVar.D(this);
        this.f6401k.l(iVar.q(), iVar);
    }

    public final i G(int i8) {
        return H(i8, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final i H(int i8, boolean z4) {
        i f5 = this.f6401k.f(i8);
        if (f5 != null) {
            return f5;
        }
        if (!z4 || u() == null) {
            return null;
        }
        return u().G(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String I() {
        if (this.f6403m == null) {
            this.f6403m = Integer.toString(this.f6402l);
        }
        return this.f6403m;
    }

    public final int K() {
        return this.f6402l;
    }

    public final void L(int i8) {
        if (i8 != q()) {
            this.f6402l = i8;
            this.f6403m = null;
            return;
        }
        throw new IllegalArgumentException("Start destination " + i8 + " cannot use the same id as the graph " + this);
    }

    @Override // java.lang.Iterable
    public final Iterator<i> iterator() {
        return new a();
    }

    @Override // androidx.navigation.i
    public String n() {
        return q() != 0 ? super.n() : "the root navigation";
    }

    @Override // androidx.navigation.i
    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" startDestination=");
        i G = G(K());
        if (G == null) {
            str = this.f6403m;
            if (str == null) {
                sb.append("0x");
                str = Integer.toHexString(this.f6402l);
            }
        } else {
            sb.append("{");
            sb.append(G.toString());
            str = "}";
        }
        sb.append(str);
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.navigation.i
    public i.a v(h hVar) {
        i.a v8 = super.v(hVar);
        Iterator<i> it = iterator();
        while (it.hasNext()) {
            i.a v9 = it.next().v(hVar);
            if (v9 != null && (v8 == null || v9.compareTo(v8) > 0)) {
                v8 = v9;
            }
        }
        return v8;
    }

    @Override // androidx.navigation.i
    public void x(Context context, AttributeSet attributeSet) {
        super.x(context, attributeSet);
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, j1.a.f20631y);
        L(obtainAttributes.getResourceId(j1.a.f20632z, 0));
        this.f6403m = i.p(context, this.f6402l);
        obtainAttributes.recycle();
    }
}

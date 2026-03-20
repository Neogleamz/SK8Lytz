package com.google.common.collect;

import com.google.common.collect.r1;
import com.google.common.collect.s1;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class TreeMultiset<E> extends m<E> implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: e  reason: collision with root package name */
    private final transient g<f<E>> f19124e;

    /* renamed from: f  reason: collision with root package name */
    private final transient t0<E> f19125f;

    /* renamed from: g  reason: collision with root package name */
    private final transient f<E> f19126g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends s1.b<E> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ f f19127a;

        a(f fVar) {
            this.f19127a = fVar;
        }

        @Override // com.google.common.collect.r1.a
        public E a() {
            return (E) this.f19127a.x();
        }

        @Override // com.google.common.collect.r1.a
        public int getCount() {
            int w8 = this.f19127a.w();
            return w8 == 0 ? TreeMultiset.this.m0(a()) : w8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Iterator<r1.a<E>> {

        /* renamed from: a  reason: collision with root package name */
        f<E> f19129a;

        /* renamed from: b  reason: collision with root package name */
        r1.a<E> f19130b;

        b() {
            this.f19129a = TreeMultiset.this.I();
        }

        @Override // java.util.Iterator
        /* renamed from: a */
        public r1.a<E> next() {
            if (hasNext()) {
                TreeMultiset treeMultiset = TreeMultiset.this;
                f<E> fVar = this.f19129a;
                Objects.requireNonNull(fVar);
                r1.a<E> N = treeMultiset.N(fVar);
                this.f19130b = N;
                this.f19129a = this.f19129a.L() == TreeMultiset.this.f19126g ? null : this.f19129a.L();
                return N;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f19129a == null) {
                return false;
            }
            if (TreeMultiset.this.f19125f.l(this.f19129a.x())) {
                this.f19129a = null;
                return false;
            }
            return true;
        }

        @Override // java.util.Iterator
        public void remove() {
            com.google.common.base.l.t(this.f19130b != null, "no calls to next() since the last call to remove()");
            TreeMultiset.this.U(this.f19130b.a(), 0);
            this.f19130b = null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements Iterator<r1.a<E>> {

        /* renamed from: a  reason: collision with root package name */
        f<E> f19132a;

        /* renamed from: b  reason: collision with root package name */
        r1.a<E> f19133b = null;

        c() {
            this.f19132a = TreeMultiset.this.K();
        }

        @Override // java.util.Iterator
        /* renamed from: a */
        public r1.a<E> next() {
            if (hasNext()) {
                Objects.requireNonNull(this.f19132a);
                r1.a<E> N = TreeMultiset.this.N(this.f19132a);
                this.f19133b = N;
                this.f19132a = this.f19132a.z() == TreeMultiset.this.f19126g ? null : this.f19132a.z();
                return N;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f19132a == null) {
                return false;
            }
            if (TreeMultiset.this.f19125f.m(this.f19132a.x())) {
                this.f19132a = null;
                return false;
            }
            return true;
        }

        @Override // java.util.Iterator
        public void remove() {
            com.google.common.base.l.t(this.f19133b != null, "no calls to next() since the last call to remove()");
            TreeMultiset.this.U(this.f19133b.a(), 0);
            this.f19133b = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class d {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f19135a;

        static {
            int[] iArr = new int[BoundType.values().length];
            f19135a = iArr;
            try {
                iArr[BoundType.OPEN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f19135a[BoundType.CLOSED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class e {

        /* renamed from: a  reason: collision with root package name */
        public static final e f19136a = new a("SIZE", 0);

        /* renamed from: b  reason: collision with root package name */
        public static final e f19137b = new b("DISTINCT", 1);

        /* renamed from: c  reason: collision with root package name */
        private static final /* synthetic */ e[] f19138c = c();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum a extends e {
            a(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.collect.TreeMultiset.e
            int f(f<?> fVar) {
                return ((f) fVar).f19140b;
            }

            @Override // com.google.common.collect.TreeMultiset.e
            long h(f<?> fVar) {
                if (fVar == null) {
                    return 0L;
                }
                return ((f) fVar).f19142d;
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum b extends e {
            b(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.collect.TreeMultiset.e
            int f(f<?> fVar) {
                return 1;
            }

            @Override // com.google.common.collect.TreeMultiset.e
            long h(f<?> fVar) {
                if (fVar == null) {
                    return 0L;
                }
                return ((f) fVar).f19141c;
            }
        }

        private e(String str, int i8) {
        }

        /* synthetic */ e(String str, int i8, a aVar) {
            this(str, i8);
        }

        private static /* synthetic */ e[] c() {
            return new e[]{f19136a, f19137b};
        }

        public static e valueOf(String str) {
            return (e) Enum.valueOf(e.class, str);
        }

        public static e[] values() {
            return (e[]) f19138c.clone();
        }

        abstract int f(f<?> fVar);

        abstract long h(f<?> fVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f<E> {

        /* renamed from: a  reason: collision with root package name */
        private final E f19139a;

        /* renamed from: b  reason: collision with root package name */
        private int f19140b;

        /* renamed from: c  reason: collision with root package name */
        private int f19141c;

        /* renamed from: d  reason: collision with root package name */
        private long f19142d;

        /* renamed from: e  reason: collision with root package name */
        private int f19143e;

        /* renamed from: f  reason: collision with root package name */
        private f<E> f19144f;

        /* renamed from: g  reason: collision with root package name */
        private f<E> f19145g;

        /* renamed from: h  reason: collision with root package name */
        private f<E> f19146h;

        /* renamed from: i  reason: collision with root package name */
        private f<E> f19147i;

        f() {
            this.f19139a = null;
            this.f19140b = 1;
        }

        f(E e8, int i8) {
            com.google.common.base.l.d(i8 > 0);
            this.f19139a = e8;
            this.f19140b = i8;
            this.f19142d = i8;
            this.f19141c = 1;
            this.f19143e = 1;
            this.f19144f = null;
            this.f19145g = null;
        }

        private f<E> A() {
            int r4 = r();
            if (r4 == -2) {
                Objects.requireNonNull(this.f19145g);
                if (this.f19145g.r() > 0) {
                    this.f19145g = this.f19145g.I();
                }
                return H();
            } else if (r4 != 2) {
                C();
                return this;
            } else {
                Objects.requireNonNull(this.f19144f);
                if (this.f19144f.r() < 0) {
                    this.f19144f = this.f19144f.H();
                }
                return I();
            }
        }

        private void B() {
            D();
            C();
        }

        private void C() {
            this.f19143e = Math.max(y(this.f19144f), y(this.f19145g)) + 1;
        }

        private void D() {
            this.f19141c = TreeMultiset.H(this.f19144f) + 1 + TreeMultiset.H(this.f19145g);
            this.f19142d = this.f19140b + M(this.f19144f) + M(this.f19145g);
        }

        private f<E> F(f<E> fVar) {
            f<E> fVar2 = this.f19145g;
            if (fVar2 == null) {
                return this.f19144f;
            }
            this.f19145g = fVar2.F(fVar);
            this.f19141c--;
            this.f19142d -= fVar.f19140b;
            return A();
        }

        private f<E> G(f<E> fVar) {
            f<E> fVar2 = this.f19144f;
            if (fVar2 == null) {
                return this.f19145g;
            }
            this.f19144f = fVar2.G(fVar);
            this.f19141c--;
            this.f19142d -= fVar.f19140b;
            return A();
        }

        private f<E> H() {
            com.google.common.base.l.s(this.f19145g != null);
            f<E> fVar = this.f19145g;
            this.f19145g = fVar.f19144f;
            fVar.f19144f = this;
            fVar.f19142d = this.f19142d;
            fVar.f19141c = this.f19141c;
            B();
            fVar.C();
            return fVar;
        }

        private f<E> I() {
            com.google.common.base.l.s(this.f19144f != null);
            f<E> fVar = this.f19144f;
            this.f19144f = fVar.f19145g;
            fVar.f19145g = this;
            fVar.f19142d = this.f19142d;
            fVar.f19141c = this.f19141c;
            B();
            fVar.C();
            return fVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public f<E> L() {
            f<E> fVar = this.f19147i;
            Objects.requireNonNull(fVar);
            return fVar;
        }

        private static long M(f<?> fVar) {
            if (fVar == null) {
                return 0L;
            }
            return ((f) fVar).f19142d;
        }

        private f<E> p(E e8, int i8) {
            this.f19144f = new f<>(e8, i8);
            TreeMultiset.M(z(), this.f19144f, this);
            this.f19143e = Math.max(2, this.f19143e);
            this.f19141c++;
            this.f19142d += i8;
            return this;
        }

        private f<E> q(E e8, int i8) {
            f<E> fVar = new f<>(e8, i8);
            this.f19145g = fVar;
            TreeMultiset.M(this, fVar, L());
            this.f19143e = Math.max(2, this.f19143e);
            this.f19141c++;
            this.f19142d += i8;
            return this;
        }

        private int r() {
            return y(this.f19144f) - y(this.f19145g);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public f<E> s(Comparator<? super E> comparator, E e8) {
            int compare = comparator.compare(e8, x());
            if (compare < 0) {
                f<E> fVar = this.f19144f;
                return fVar == null ? this : (f) com.google.common.base.i.a(fVar.s(comparator, e8), this);
            } else if (compare == 0) {
                return this;
            } else {
                f<E> fVar2 = this.f19145g;
                if (fVar2 == null) {
                    return null;
                }
                return fVar2.s(comparator, e8);
            }
        }

        private f<E> u() {
            f<E> L;
            int i8 = this.f19140b;
            this.f19140b = 0;
            TreeMultiset.L(z(), L());
            f<E> fVar = this.f19144f;
            if (fVar == null) {
                return this.f19145g;
            }
            f<E> fVar2 = this.f19145g;
            if (fVar2 == null) {
                return fVar;
            }
            if (fVar.f19143e >= fVar2.f19143e) {
                L = z();
                L.f19144f = this.f19144f.F(L);
                L.f19145g = this.f19145g;
            } else {
                L = L();
                L.f19145g = this.f19145g.G(L);
                L.f19144f = this.f19144f;
            }
            L.f19141c = this.f19141c - 1;
            L.f19142d = this.f19142d - i8;
            return L.A();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public f<E> v(Comparator<? super E> comparator, E e8) {
            int compare = comparator.compare(e8, x());
            if (compare > 0) {
                f<E> fVar = this.f19145g;
                return fVar == null ? this : (f) com.google.common.base.i.a(fVar.v(comparator, e8), this);
            } else if (compare == 0) {
                return this;
            } else {
                f<E> fVar2 = this.f19144f;
                if (fVar2 == null) {
                    return null;
                }
                return fVar2.v(comparator, e8);
            }
        }

        private static int y(f<?> fVar) {
            if (fVar == null) {
                return 0;
            }
            return ((f) fVar).f19143e;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public f<E> z() {
            f<E> fVar = this.f19146h;
            Objects.requireNonNull(fVar);
            return fVar;
        }

        /* JADX WARN: Multi-variable type inference failed */
        f<E> E(Comparator<? super E> comparator, E e8, int i8, int[] iArr) {
            long j8;
            long j9;
            int compare = comparator.compare(e8, x());
            if (compare < 0) {
                f<E> fVar = this.f19144f;
                if (fVar == null) {
                    iArr[0] = 0;
                    return this;
                }
                this.f19144f = fVar.E(comparator, e8, i8, iArr);
                if (iArr[0] > 0) {
                    if (i8 >= iArr[0]) {
                        this.f19141c--;
                        j9 = this.f19142d;
                        i8 = iArr[0];
                    } else {
                        j9 = this.f19142d;
                    }
                    this.f19142d = j9 - i8;
                }
                return iArr[0] == 0 ? this : A();
            } else if (compare <= 0) {
                int i9 = this.f19140b;
                iArr[0] = i9;
                if (i8 >= i9) {
                    return u();
                }
                this.f19140b = i9 - i8;
                this.f19142d -= i8;
                return this;
            } else {
                f<E> fVar2 = this.f19145g;
                if (fVar2 == null) {
                    iArr[0] = 0;
                    return this;
                }
                this.f19145g = fVar2.E(comparator, e8, i8, iArr);
                if (iArr[0] > 0) {
                    if (i8 >= iArr[0]) {
                        this.f19141c--;
                        j8 = this.f19142d;
                        i8 = iArr[0];
                    } else {
                        j8 = this.f19142d;
                    }
                    this.f19142d = j8 - i8;
                }
                return A();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        f<E> J(Comparator<? super E> comparator, E e8, int i8, int i9, int[] iArr) {
            int i10;
            int i11;
            int compare = comparator.compare(e8, x());
            if (compare < 0) {
                f<E> fVar = this.f19144f;
                if (fVar == null) {
                    iArr[0] = 0;
                    return (i8 != 0 || i9 <= 0) ? this : p(e8, i9);
                }
                this.f19144f = fVar.J(comparator, e8, i8, i9, iArr);
                if (iArr[0] == i8) {
                    if (i9 != 0 || iArr[0] == 0) {
                        if (i9 > 0 && iArr[0] == 0) {
                            i11 = this.f19141c + 1;
                        }
                        this.f19142d += i9 - iArr[0];
                    } else {
                        i11 = this.f19141c - 1;
                    }
                    this.f19141c = i11;
                    this.f19142d += i9 - iArr[0];
                }
                return A();
            } else if (compare <= 0) {
                int i12 = this.f19140b;
                iArr[0] = i12;
                if (i8 == i12) {
                    if (i9 == 0) {
                        return u();
                    }
                    this.f19142d += i9 - i12;
                    this.f19140b = i9;
                }
                return this;
            } else {
                f<E> fVar2 = this.f19145g;
                if (fVar2 == null) {
                    iArr[0] = 0;
                    return (i8 != 0 || i9 <= 0) ? this : q(e8, i9);
                }
                this.f19145g = fVar2.J(comparator, e8, i8, i9, iArr);
                if (iArr[0] == i8) {
                    if (i9 != 0 || iArr[0] == 0) {
                        if (i9 > 0 && iArr[0] == 0) {
                            i10 = this.f19141c + 1;
                        }
                        this.f19142d += i9 - iArr[0];
                    } else {
                        i10 = this.f19141c - 1;
                    }
                    this.f19141c = i10;
                    this.f19142d += i9 - iArr[0];
                }
                return A();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        f<E> K(Comparator<? super E> comparator, E e8, int i8, int[] iArr) {
            int i9;
            int i10;
            long j8;
            int i11;
            int i12;
            int compare = comparator.compare(e8, x());
            if (compare < 0) {
                f<E> fVar = this.f19144f;
                if (fVar == null) {
                    iArr[0] = 0;
                    return i8 > 0 ? p(e8, i8) : this;
                }
                this.f19144f = fVar.K(comparator, e8, i8, iArr);
                if (i8 != 0 || iArr[0] == 0) {
                    if (i8 > 0 && iArr[0] == 0) {
                        i12 = this.f19141c + 1;
                    }
                    j8 = this.f19142d;
                    i11 = iArr[0];
                } else {
                    i12 = this.f19141c - 1;
                }
                this.f19141c = i12;
                j8 = this.f19142d;
                i11 = iArr[0];
            } else if (compare <= 0) {
                iArr[0] = this.f19140b;
                if (i8 == 0) {
                    return u();
                }
                this.f19142d += i8 - i9;
                this.f19140b = i8;
                return this;
            } else {
                f<E> fVar2 = this.f19145g;
                if (fVar2 == null) {
                    iArr[0] = 0;
                    return i8 > 0 ? q(e8, i8) : this;
                }
                this.f19145g = fVar2.K(comparator, e8, i8, iArr);
                if (i8 != 0 || iArr[0] == 0) {
                    if (i8 > 0 && iArr[0] == 0) {
                        i10 = this.f19141c + 1;
                    }
                    j8 = this.f19142d;
                    i11 = iArr[0];
                } else {
                    i10 = this.f19141c - 1;
                }
                this.f19141c = i10;
                j8 = this.f19142d;
                i11 = iArr[0];
            }
            this.f19142d = j8 + (i8 - i11);
            return A();
        }

        /* JADX WARN: Multi-variable type inference failed */
        f<E> o(Comparator<? super E> comparator, E e8, int i8, int[] iArr) {
            int compare = comparator.compare(e8, x());
            if (compare < 0) {
                f<E> fVar = this.f19144f;
                if (fVar == null) {
                    iArr[0] = 0;
                    return p(e8, i8);
                }
                int i9 = fVar.f19143e;
                f<E> o5 = fVar.o(comparator, e8, i8, iArr);
                this.f19144f = o5;
                if (iArr[0] == 0) {
                    this.f19141c++;
                }
                this.f19142d += i8;
                return o5.f19143e == i9 ? this : A();
            } else if (compare <= 0) {
                int i10 = this.f19140b;
                iArr[0] = i10;
                long j8 = i8;
                com.google.common.base.l.d(((long) i10) + j8 <= 2147483647L);
                this.f19140b += i8;
                this.f19142d += j8;
                return this;
            } else {
                f<E> fVar2 = this.f19145g;
                if (fVar2 == null) {
                    iArr[0] = 0;
                    return q(e8, i8);
                }
                int i11 = fVar2.f19143e;
                f<E> o8 = fVar2.o(comparator, e8, i8, iArr);
                this.f19145g = o8;
                if (iArr[0] == 0) {
                    this.f19141c++;
                }
                this.f19142d += i8;
                return o8.f19143e == i11 ? this : A();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        int t(Comparator<? super E> comparator, E e8) {
            int compare = comparator.compare(e8, x());
            if (compare < 0) {
                f<E> fVar = this.f19144f;
                if (fVar == null) {
                    return 0;
                }
                return fVar.t(comparator, e8);
            } else if (compare > 0) {
                f<E> fVar2 = this.f19145g;
                if (fVar2 == null) {
                    return 0;
                }
                return fVar2.t(comparator, e8);
            } else {
                return this.f19140b;
            }
        }

        public String toString() {
            return s1.g(x(), w()).toString();
        }

        int w() {
            return this.f19140b;
        }

        E x() {
            return (E) u1.a(this.f19139a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g<T> {

        /* renamed from: a  reason: collision with root package name */
        private T f19148a;

        private g() {
        }

        /* synthetic */ g(a aVar) {
            this();
        }

        public void a(T t8, T t9) {
            if (this.f19148a != t8) {
                throw new ConcurrentModificationException();
            }
            this.f19148a = t9;
        }

        void b() {
            this.f19148a = null;
        }

        public T c() {
            return this.f19148a;
        }
    }

    TreeMultiset(g<f<E>> gVar, t0<E> t0Var, f<E> fVar) {
        super(t0Var.b());
        this.f19124e = gVar;
        this.f19125f = t0Var;
        this.f19126g = fVar;
    }

    private long E(e eVar, f<E> fVar) {
        long h8;
        long E;
        if (fVar == null) {
            return 0L;
        }
        int compare = comparator().compare(u1.a(this.f19125f.h()), fVar.x());
        if (compare > 0) {
            return E(eVar, ((f) fVar).f19145g);
        }
        if (compare == 0) {
            int i8 = d.f19135a[this.f19125f.g().ordinal()];
            if (i8 != 1) {
                if (i8 == 2) {
                    return eVar.h(((f) fVar).f19145g);
                }
                throw new AssertionError();
            }
            h8 = eVar.f(fVar);
            E = eVar.h(((f) fVar).f19145g);
        } else {
            h8 = eVar.h(((f) fVar).f19145g) + eVar.f(fVar);
            E = E(eVar, ((f) fVar).f19144f);
        }
        return h8 + E;
    }

    private long F(e eVar, f<E> fVar) {
        long h8;
        long F;
        if (fVar == null) {
            return 0L;
        }
        int compare = comparator().compare(u1.a(this.f19125f.f()), fVar.x());
        if (compare < 0) {
            return F(eVar, ((f) fVar).f19144f);
        }
        if (compare == 0) {
            int i8 = d.f19135a[this.f19125f.e().ordinal()];
            if (i8 != 1) {
                if (i8 == 2) {
                    return eVar.h(((f) fVar).f19144f);
                }
                throw new AssertionError();
            }
            h8 = eVar.f(fVar);
            F = eVar.h(((f) fVar).f19144f);
        } else {
            h8 = eVar.h(((f) fVar).f19144f) + eVar.f(fVar);
            F = F(eVar, ((f) fVar).f19145g);
        }
        return h8 + F;
    }

    private long G(e eVar) {
        f<E> c9 = this.f19124e.c();
        long h8 = eVar.h(c9);
        if (this.f19125f.i()) {
            h8 -= F(eVar, c9);
        }
        return this.f19125f.j() ? h8 - E(eVar, c9) : h8;
    }

    static int H(f<?> fVar) {
        if (fVar == null) {
            return 0;
        }
        return ((f) fVar).f19141c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003f, code lost:
        if (comparator().compare(r2, r0.x()) == 0) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.google.common.collect.TreeMultiset.f<E> I() {
        /*
            r5 = this;
            com.google.common.collect.TreeMultiset$g<com.google.common.collect.TreeMultiset$f<E>> r0 = r5.f19124e
            java.lang.Object r0 = r0.c()
            com.google.common.collect.TreeMultiset$f r0 = (com.google.common.collect.TreeMultiset.f) r0
            r1 = 0
            if (r0 != 0) goto Lc
            return r1
        Lc:
            com.google.common.collect.t0<E> r2 = r5.f19125f
            boolean r2 = r2.i()
            if (r2 == 0) goto L42
            com.google.common.collect.t0<E> r2 = r5.f19125f
            java.lang.Object r2 = r2.f()
            java.lang.Object r2 = com.google.common.collect.u1.a(r2)
            java.util.Comparator r3 = r5.comparator()
            com.google.common.collect.TreeMultiset$f r0 = com.google.common.collect.TreeMultiset.f.a(r0, r3, r2)
            if (r0 != 0) goto L29
            return r1
        L29:
            com.google.common.collect.t0<E> r3 = r5.f19125f
            com.google.common.collect.BoundType r3 = r3.e()
            com.google.common.collect.BoundType r4 = com.google.common.collect.BoundType.OPEN
            if (r3 != r4) goto L48
            java.util.Comparator r3 = r5.comparator()
            java.lang.Object r4 = r0.x()
            int r2 = r3.compare(r2, r4)
            if (r2 != 0) goto L48
            goto L44
        L42:
            com.google.common.collect.TreeMultiset$f<E> r0 = r5.f19126g
        L44:
            com.google.common.collect.TreeMultiset$f r0 = com.google.common.collect.TreeMultiset.f.l(r0)
        L48:
            com.google.common.collect.TreeMultiset$f<E> r2 = r5.f19126g
            if (r0 == r2) goto L5a
            com.google.common.collect.t0<E> r2 = r5.f19125f
            java.lang.Object r3 = r0.x()
            boolean r2 = r2.c(r3)
            if (r2 != 0) goto L59
            goto L5a
        L59:
            r1 = r0
        L5a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.TreeMultiset.I():com.google.common.collect.TreeMultiset$f");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003f, code lost:
        if (comparator().compare(r2, r0.x()) == 0) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.google.common.collect.TreeMultiset.f<E> K() {
        /*
            r5 = this;
            com.google.common.collect.TreeMultiset$g<com.google.common.collect.TreeMultiset$f<E>> r0 = r5.f19124e
            java.lang.Object r0 = r0.c()
            com.google.common.collect.TreeMultiset$f r0 = (com.google.common.collect.TreeMultiset.f) r0
            r1 = 0
            if (r0 != 0) goto Lc
            return r1
        Lc:
            com.google.common.collect.t0<E> r2 = r5.f19125f
            boolean r2 = r2.j()
            if (r2 == 0) goto L42
            com.google.common.collect.t0<E> r2 = r5.f19125f
            java.lang.Object r2 = r2.h()
            java.lang.Object r2 = com.google.common.collect.u1.a(r2)
            java.util.Comparator r3 = r5.comparator()
            com.google.common.collect.TreeMultiset$f r0 = com.google.common.collect.TreeMultiset.f.b(r0, r3, r2)
            if (r0 != 0) goto L29
            return r1
        L29:
            com.google.common.collect.t0<E> r3 = r5.f19125f
            com.google.common.collect.BoundType r3 = r3.g()
            com.google.common.collect.BoundType r4 = com.google.common.collect.BoundType.OPEN
            if (r3 != r4) goto L48
            java.util.Comparator r3 = r5.comparator()
            java.lang.Object r4 = r0.x()
            int r2 = r3.compare(r2, r4)
            if (r2 != 0) goto L48
            goto L44
        L42:
            com.google.common.collect.TreeMultiset$f<E> r0 = r5.f19126g
        L44:
            com.google.common.collect.TreeMultiset$f r0 = com.google.common.collect.TreeMultiset.f.c(r0)
        L48:
            com.google.common.collect.TreeMultiset$f<E> r2 = r5.f19126g
            if (r0 == r2) goto L5a
            com.google.common.collect.t0<E> r2 = r5.f19125f
            java.lang.Object r3 = r0.x()
            boolean r2 = r2.c(r3)
            if (r2 != 0) goto L59
            goto L5a
        L59:
            r1 = r0
        L5a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.TreeMultiset.K():com.google.common.collect.TreeMultiset$f");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> void L(f<T> fVar, f<T> fVar2) {
        ((f) fVar).f19147i = fVar2;
        ((f) fVar2).f19146h = fVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> void M(f<T> fVar, f<T> fVar2, f<T> fVar3) {
        L(fVar, fVar2);
        L(fVar2, fVar3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public r1.a<E> N(f<E> fVar) {
        return new a(fVar);
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        Comparator comparator = (Comparator) objectInputStream.readObject();
        m2.a(m.class, "comparator").b(this, comparator);
        m2.a(TreeMultiset.class, "range").b(this, t0.a(comparator));
        m2.a(TreeMultiset.class, "rootReference").b(this, new g(null));
        f fVar = new f();
        m2.a(TreeMultiset.class, "header").b(this, fVar);
        L(fVar, fVar);
        m2.f(this, objectInputStream);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(l().comparator());
        m2.k(this, objectOutputStream);
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public int B(Object obj, int i8) {
        t.b(i8, "occurrences");
        if (i8 == 0) {
            return m0(obj);
        }
        f<E> c9 = this.f19124e.c();
        int[] iArr = new int[1];
        try {
            if (this.f19125f.c(obj) && c9 != null) {
                this.f19124e.a(c9, c9.E(comparator(), obj, i8, iArr));
                return iArr[0];
            }
        } catch (ClassCastException | NullPointerException unused) {
        }
        return 0;
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public int J(E e8, int i8) {
        t.b(i8, "occurrences");
        if (i8 == 0) {
            return m0(e8);
        }
        com.google.common.base.l.d(this.f19125f.c(e8));
        f<E> c9 = this.f19124e.c();
        if (c9 != null) {
            int[] iArr = new int[1];
            this.f19124e.a(c9, c9.o(comparator(), e8, i8, iArr));
            return iArr[0];
        }
        comparator().compare(e8, e8);
        f<E> fVar = new f<>(e8, i8);
        f<E> fVar2 = this.f19126g;
        M(fVar2, fVar, fVar2);
        this.f19124e.a(c9, fVar);
        return 0;
    }

    @Override // com.google.common.collect.m, com.google.common.collect.u2
    public /* bridge */ /* synthetic */ u2 P() {
        return super.P();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.m, com.google.common.collect.u2
    public /* bridge */ /* synthetic */ u2 P0(Object obj, BoundType boundType, Object obj2, BoundType boundType2) {
        return super.P0(obj, boundType, obj2, boundType2);
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public int U(E e8, int i8) {
        t.b(i8, "count");
        if (!this.f19125f.c(e8)) {
            com.google.common.base.l.d(i8 == 0);
            return 0;
        }
        f<E> c9 = this.f19124e.c();
        if (c9 == null) {
            if (i8 > 0) {
                J(e8, i8);
            }
            return 0;
        }
        int[] iArr = new int[1];
        this.f19124e.a(c9, c9.K(comparator(), e8, i8, iArr));
        return iArr[0];
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public boolean Y(E e8, int i8, int i9) {
        t.b(i9, "newCount");
        t.b(i8, "oldCount");
        com.google.common.base.l.d(this.f19125f.c(e8));
        f<E> c9 = this.f19124e.c();
        if (c9 != null) {
            int[] iArr = new int[1];
            this.f19124e.a(c9, c9.J(comparator(), e8, i8, i9, iArr));
            return iArr[0] == i8;
        } else if (i8 == 0) {
            if (i9 > 0) {
                J(e8, i9);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        if (this.f19125f.i() || this.f19125f.j()) {
            g1.e(k());
            return;
        }
        f<E> L = this.f19126g.L();
        while (true) {
            f<E> fVar = this.f19126g;
            if (L == fVar) {
                L(fVar, fVar);
                this.f19124e.b();
                return;
            }
            f<E> L2 = L.L();
            ((f) L).f19140b = 0;
            ((f) L).f19144f = null;
            ((f) L).f19145g = null;
            ((f) L).f19146h = null;
            ((f) L).f19147i = null;
            L = L2;
        }
    }

    @Override // com.google.common.collect.m, com.google.common.collect.u2, com.google.common.collect.s2
    public /* bridge */ /* synthetic */ Comparator comparator() {
        return super.comparator();
    }

    @Override // com.google.common.collect.i, java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ boolean contains(Object obj) {
        return super.contains(obj);
    }

    @Override // com.google.common.collect.i, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.m, com.google.common.collect.u2
    public /* bridge */ /* synthetic */ r1.a firstEntry() {
        return super.firstEntry();
    }

    @Override // com.google.common.collect.u2
    public u2<E> g0(E e8, BoundType boundType) {
        return new TreeMultiset(this.f19124e, this.f19125f.k(t0.n(comparator(), e8, boundType)), this.f19126g);
    }

    @Override // com.google.common.collect.i
    int h() {
        return com.google.common.primitives.g.k(G(e.f19137b));
    }

    @Override // com.google.common.collect.i
    Iterator<E> i() {
        return s1.e(k());
    }

    @Override // com.google.common.collect.i, java.util.AbstractCollection, java.util.Collection
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return s1.h(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.i
    public Iterator<r1.a<E>> k() {
        return new b();
    }

    @Override // com.google.common.collect.m, com.google.common.collect.i, com.google.common.collect.r1
    public /* bridge */ /* synthetic */ NavigableSet l() {
        return super.l();
    }

    @Override // com.google.common.collect.m, com.google.common.collect.u2
    public /* bridge */ /* synthetic */ r1.a lastEntry() {
        return super.lastEntry();
    }

    @Override // com.google.common.collect.r1
    public int m0(Object obj) {
        try {
            f<E> c9 = this.f19124e.c();
            if (this.f19125f.c(obj) && c9 != null) {
                return c9.t(comparator(), obj);
            }
        } catch (ClassCastException | NullPointerException unused) {
        }
        return 0;
    }

    @Override // com.google.common.collect.u2
    public u2<E> n0(E e8, BoundType boundType) {
        return new TreeMultiset(this.f19124e, this.f19125f.k(t0.d(comparator(), e8, boundType)), this.f19126g);
    }

    @Override // com.google.common.collect.m, com.google.common.collect.u2
    public /* bridge */ /* synthetic */ r1.a pollFirstEntry() {
        return super.pollFirstEntry();
    }

    @Override // com.google.common.collect.m, com.google.common.collect.u2
    public /* bridge */ /* synthetic */ r1.a pollLastEntry() {
        return super.pollLastEntry();
    }

    @Override // com.google.common.collect.m
    Iterator<r1.a<E>> q() {
        return new c();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public int size() {
        return com.google.common.primitives.g.k(G(e.f19136a));
    }
}

package com.google.android.gms.internal.mlkit_vision_barcode;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x0 extends AbstractMap implements Serializable {

    /* renamed from: k  reason: collision with root package name */
    private static final Object f14186k = new Object();

    /* renamed from: a  reason: collision with root package name */
    private transient Object f14187a;

    /* renamed from: b  reason: collision with root package name */
    transient int[] f14188b;

    /* renamed from: c  reason: collision with root package name */
    transient Object[] f14189c;

    /* renamed from: d  reason: collision with root package name */
    transient Object[] f14190d;

    /* renamed from: e  reason: collision with root package name */
    private transient int f14191e;

    /* renamed from: f  reason: collision with root package name */
    private transient int f14192f;

    /* renamed from: g  reason: collision with root package name */
    private transient Set f14193g;

    /* renamed from: h  reason: collision with root package name */
    private transient Set f14194h;

    /* renamed from: j  reason: collision with root package name */
    private transient Collection f14195j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x0(int i8) {
        o(12);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object j(x0 x0Var) {
        Object obj = x0Var.f14187a;
        obj.getClass();
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int r() {
        return (1 << (this.f14191e & 31)) - 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int s(Object obj) {
        if (q()) {
            return -1;
        }
        int a9 = z0.a(obj);
        int r4 = r();
        Object obj2 = this.f14187a;
        obj2.getClass();
        int c9 = y0.c(obj2, a9 & r4);
        if (c9 != 0) {
            int i8 = ~r4;
            int i9 = a9 & i8;
            do {
                int i10 = c9 - 1;
                int[] iArr = this.f14188b;
                iArr.getClass();
                int i11 = iArr[i10];
                if ((i11 & i8) == i9) {
                    Object[] objArr = this.f14189c;
                    objArr.getClass();
                    if (p.a(obj, objArr[i10])) {
                        return i10;
                    }
                }
                c9 = i11 & r4;
            } while (c9 != 0);
            return -1;
        }
        return -1;
    }

    private final int t(int i8, int i9, int i10, int i11) {
        int i12 = i9 - 1;
        Object d8 = y0.d(i9);
        if (i11 != 0) {
            y0.e(d8, i10 & i12, i11 + 1);
        }
        Object obj = this.f14187a;
        obj.getClass();
        int[] iArr = this.f14188b;
        iArr.getClass();
        for (int i13 = 0; i13 <= i8; i13++) {
            int c9 = y0.c(obj, i13);
            while (c9 != 0) {
                int i14 = c9 - 1;
                int i15 = iArr[i14];
                int i16 = ((~i8) & i15) | i13;
                int i17 = i16 & i12;
                int c10 = y0.c(d8, i17);
                y0.e(d8, i17, c9);
                iArr[i14] = ((~i12) & i16) | (c10 & i12);
                c9 = i15 & i8;
            }
        }
        this.f14187a = d8;
        v(i12);
        return i12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object u(Object obj) {
        if (q()) {
            return f14186k;
        }
        int r4 = r();
        Object obj2 = this.f14187a;
        obj2.getClass();
        int[] iArr = this.f14188b;
        iArr.getClass();
        Object[] objArr = this.f14189c;
        objArr.getClass();
        int b9 = y0.b(obj, null, r4, obj2, iArr, objArr, null);
        if (b9 == -1) {
            return f14186k;
        }
        Object[] objArr2 = this.f14190d;
        objArr2.getClass();
        Object obj3 = objArr2[b9];
        p(b9, r4);
        this.f14192f--;
        n();
        return obj3;
    }

    private final void v(int i8) {
        this.f14191e = ((32 - Integer.numberOfLeadingZeros(i8)) & 31) | (this.f14191e & (-32));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final void clear() {
        if (q()) {
            return;
        }
        n();
        Map l8 = l();
        if (l8 == null) {
            Object[] objArr = this.f14189c;
            objArr.getClass();
            Arrays.fill(objArr, 0, this.f14192f, (Object) null);
            Object[] objArr2 = this.f14190d;
            objArr2.getClass();
            Arrays.fill(objArr2, 0, this.f14192f, (Object) null);
            Object obj = this.f14187a;
            obj.getClass();
            if (obj instanceof byte[]) {
                Arrays.fill((byte[]) obj, (byte) 0);
            } else if (obj instanceof short[]) {
                Arrays.fill((short[]) obj, (short) 0);
            } else {
                Arrays.fill((int[]) obj, 0);
            }
            int[] iArr = this.f14188b;
            iArr.getClass();
            Arrays.fill(iArr, 0, this.f14192f, 0);
        } else {
            this.f14191e = d2.a(size(), 3, 1073741823);
            l8.clear();
            this.f14187a = null;
        }
        this.f14192f = 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsKey(Object obj) {
        Map l8 = l();
        return l8 != null ? l8.containsKey(obj) : s(obj) != -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsValue(Object obj) {
        Map l8 = l();
        if (l8 == null) {
            for (int i8 = 0; i8 < this.f14192f; i8++) {
                Object[] objArr = this.f14190d;
                objArr.getClass();
                if (p.a(obj, objArr[i8])) {
                    return true;
                }
            }
            return false;
        }
        return l8.containsValue(obj);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set entrySet() {
        Set set = this.f14194h;
        if (set == null) {
            r0 r0Var = new r0(this);
            this.f14194h = r0Var;
            return r0Var;
        }
        return set;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int f() {
        return isEmpty() ? -1 : 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Object get(Object obj) {
        Map l8 = l();
        if (l8 != null) {
            return l8.get(obj);
        }
        int s8 = s(obj);
        if (s8 == -1) {
            return null;
        }
        Object[] objArr = this.f14190d;
        objArr.getClass();
        return objArr[s8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int h(int i8) {
        int i9 = i8 + 1;
        if (i9 < this.f14192f) {
            return i9;
        }
        return -1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set keySet() {
        Set set = this.f14193g;
        if (set == null) {
            u0 u0Var = new u0(this);
            this.f14193g = u0Var;
            return u0Var;
        }
        return set;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map l() {
        Object obj = this.f14187a;
        if (obj instanceof Map) {
            return (Map) obj;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void n() {
        this.f14191e += 32;
    }

    final void o(int i8) {
        this.f14191e = d2.a(12, 1, 1073741823);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void p(int i8, int i9) {
        Object obj = this.f14187a;
        obj.getClass();
        int[] iArr = this.f14188b;
        iArr.getClass();
        Object[] objArr = this.f14189c;
        objArr.getClass();
        Object[] objArr2 = this.f14190d;
        objArr2.getClass();
        int size = size() - 1;
        if (i8 >= size) {
            objArr[i8] = null;
            objArr2[i8] = null;
            iArr[i8] = 0;
            return;
        }
        Object obj2 = objArr[size];
        objArr[i8] = obj2;
        objArr2[i8] = objArr2[size];
        objArr[size] = null;
        objArr2[size] = null;
        iArr[i8] = iArr[size];
        iArr[size] = 0;
        int a9 = z0.a(obj2) & i9;
        int c9 = y0.c(obj, a9);
        int i10 = size + 1;
        if (c9 == i10) {
            y0.e(obj, a9, i8 + 1);
            return;
        }
        while (true) {
            int i11 = c9 - 1;
            int i12 = iArr[i11];
            int i13 = i12 & i9;
            if (i13 == i10) {
                iArr[i11] = ((i8 + 1) & i9) | (i12 & (~i9));
                return;
            }
            c9 = i13;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Object put(Object obj, Object obj2) {
        int length;
        int min;
        if (q()) {
            u.e(q(), "Arrays already allocated");
            int i8 = this.f14191e;
            int max = Math.max(i8 + 1, 2);
            int highestOneBit = Integer.highestOneBit(max);
            if (max > highestOneBit && (highestOneBit = highestOneBit + highestOneBit) <= 0) {
                highestOneBit = 1073741824;
            }
            int max2 = Math.max(4, highestOneBit);
            this.f14187a = y0.d(max2);
            v(max2 - 1);
            this.f14188b = new int[i8];
            this.f14189c = new Object[i8];
            this.f14190d = new Object[i8];
        }
        Map l8 = l();
        if (l8 != null) {
            return l8.put(obj, obj2);
        }
        int[] iArr = this.f14188b;
        iArr.getClass();
        Object[] objArr = this.f14189c;
        objArr.getClass();
        Object[] objArr2 = this.f14190d;
        objArr2.getClass();
        int i9 = this.f14192f;
        int i10 = i9 + 1;
        int a9 = z0.a(obj);
        int r4 = r();
        int i11 = a9 & r4;
        Object obj3 = this.f14187a;
        obj3.getClass();
        int c9 = y0.c(obj3, i11);
        if (c9 == 0) {
            if (i10 <= r4) {
                Object obj4 = this.f14187a;
                obj4.getClass();
                y0.e(obj4, i11, i10);
                int[] iArr2 = this.f14188b;
                iArr2.getClass();
                length = iArr2.length;
                if (i10 > length && (min = Math.min(1073741823, (Math.max(1, length >>> 1) + length) | 1)) != length) {
                    int[] iArr3 = this.f14188b;
                    iArr3.getClass();
                    this.f14188b = Arrays.copyOf(iArr3, min);
                    Object[] objArr3 = this.f14189c;
                    objArr3.getClass();
                    this.f14189c = Arrays.copyOf(objArr3, min);
                    Object[] objArr4 = this.f14190d;
                    objArr4.getClass();
                    this.f14190d = Arrays.copyOf(objArr4, min);
                }
                int[] iArr4 = this.f14188b;
                iArr4.getClass();
                iArr4[i9] = (~r4) & a9;
                Object[] objArr5 = this.f14189c;
                objArr5.getClass();
                objArr5[i9] = obj;
                Object[] objArr6 = this.f14190d;
                objArr6.getClass();
                objArr6[i9] = obj2;
                this.f14192f = i10;
                n();
                return null;
            }
            r4 = t(r4, y0.a(r4), a9, i9);
            int[] iArr22 = this.f14188b;
            iArr22.getClass();
            length = iArr22.length;
            if (i10 > length) {
                int[] iArr32 = this.f14188b;
                iArr32.getClass();
                this.f14188b = Arrays.copyOf(iArr32, min);
                Object[] objArr32 = this.f14189c;
                objArr32.getClass();
                this.f14189c = Arrays.copyOf(objArr32, min);
                Object[] objArr42 = this.f14190d;
                objArr42.getClass();
                this.f14190d = Arrays.copyOf(objArr42, min);
            }
            int[] iArr42 = this.f14188b;
            iArr42.getClass();
            iArr42[i9] = (~r4) & a9;
            Object[] objArr52 = this.f14189c;
            objArr52.getClass();
            objArr52[i9] = obj;
            Object[] objArr62 = this.f14190d;
            objArr62.getClass();
            objArr62[i9] = obj2;
            this.f14192f = i10;
            n();
            return null;
        }
        int i12 = ~r4;
        int i13 = a9 & i12;
        int i14 = 0;
        while (true) {
            int i15 = c9 - 1;
            int i16 = iArr[i15];
            int i17 = i16 & i12;
            if (i17 == i13 && p.a(obj, objArr[i15])) {
                Object obj5 = objArr2[i15];
                objArr2[i15] = obj2;
                return obj5;
            }
            int i18 = i16 & r4;
            i14++;
            if (i18 != 0) {
                c9 = i18;
            } else if (i14 >= 9) {
                LinkedHashMap linkedHashMap = new LinkedHashMap(r() + 1, 1.0f);
                int f5 = f();
                while (f5 >= 0) {
                    Object[] objArr7 = this.f14189c;
                    objArr7.getClass();
                    Object obj6 = objArr7[f5];
                    Object[] objArr8 = this.f14190d;
                    objArr8.getClass();
                    linkedHashMap.put(obj6, objArr8[f5]);
                    f5 = h(f5);
                }
                this.f14187a = linkedHashMap;
                this.f14188b = null;
                this.f14189c = null;
                this.f14190d = null;
                n();
                return linkedHashMap.put(obj, obj2);
            } else if (i10 <= r4) {
                iArr[i15] = (i10 & r4) | i17;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean q() {
        return this.f14187a == null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Object remove(Object obj) {
        Map l8 = l();
        if (l8 != null) {
            return l8.remove(obj);
        }
        Object u8 = u(obj);
        if (u8 == f14186k) {
            return null;
        }
        return u8;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int size() {
        Map l8 = l();
        return l8 != null ? l8.size() : this.f14192f;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Collection values() {
        Collection collection = this.f14195j;
        if (collection == null) {
            w0 w0Var = new w0(this);
            this.f14195j = w0Var;
            return w0Var;
        }
        return collection;
    }
}

package com.google.common.base;

import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class b<T> implements Iterator<T> {

    /* renamed from: a  reason: collision with root package name */
    private EnumC0145b f18802a = EnumC0145b.NOT_READY;

    /* renamed from: b  reason: collision with root package name */
    private T f18803b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f18804a;

        static {
            int[] iArr = new int[EnumC0145b.values().length];
            f18804a = iArr;
            try {
                iArr[EnumC0145b.DONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f18804a[EnumC0145b.READY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: com.google.common.base.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum EnumC0145b {
        READY,
        NOT_READY,
        DONE,
        FAILED
    }

    private boolean c() {
        this.f18802a = EnumC0145b.FAILED;
        this.f18803b = a();
        if (this.f18802a != EnumC0145b.DONE) {
            this.f18802a = EnumC0145b.READY;
            return true;
        }
        return false;
    }

    protected abstract T a();

    /* JADX INFO: Access modifiers changed from: protected */
    public final T b() {
        this.f18802a = EnumC0145b.DONE;
        return null;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        l.s(this.f18802a != EnumC0145b.FAILED);
        int i8 = a.f18804a[this.f18802a.ordinal()];
        if (i8 != 1) {
            if (i8 != 2) {
                return c();
            }
            return true;
        }
        return false;
    }

    @Override // java.util.Iterator
    public final T next() {
        if (hasNext()) {
            this.f18802a = EnumC0145b.NOT_READY;
            T t8 = (T) j.a(this.f18803b);
            this.f18803b = null;
            return t8;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}

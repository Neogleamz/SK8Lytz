package com.google.common.base;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: a  reason: collision with root package name */
    private final String f18821a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends h {

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ String f18822b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(h hVar, String str) {
            super(hVar, null);
            this.f18822b = str;
        }

        @Override // com.google.common.base.h
        CharSequence h(Object obj) {
            return obj == null ? this.f18822b : h.this.h(obj);
        }

        @Override // com.google.common.base.h
        public h i(String str) {
            throw new UnsupportedOperationException("already specified useForNull");
        }
    }

    private h(h hVar) {
        this.f18821a = hVar.f18821a;
    }

    /* synthetic */ h(h hVar, a aVar) {
        this(hVar);
    }

    private h(String str) {
        this.f18821a = (String) l.n(str);
    }

    public static h f(char c9) {
        return new h(String.valueOf(c9));
    }

    public static h g(String str) {
        return new h(str);
    }

    public <A extends Appendable> A a(A a9, Iterator<? extends Object> it) {
        l.n(a9);
        if (it.hasNext()) {
            while (true) {
                a9.append(h(it.next()));
                if (!it.hasNext()) {
                    break;
                }
                a9.append(this.f18821a);
            }
        }
        return a9;
    }

    public final StringBuilder b(StringBuilder sb, Iterable<? extends Object> iterable) {
        return c(sb, iterable.iterator());
    }

    public final StringBuilder c(StringBuilder sb, Iterator<? extends Object> it) {
        try {
            a(sb, it);
            return sb;
        } catch (IOException e8) {
            throw new AssertionError(e8);
        }
    }

    public final String d(Iterable<? extends Object> iterable) {
        return e(iterable.iterator());
    }

    public final String e(Iterator<? extends Object> it) {
        return c(new StringBuilder(), it).toString();
    }

    CharSequence h(Object obj) {
        Objects.requireNonNull(obj);
        return obj instanceof CharSequence ? (CharSequence) obj : obj.toString();
    }

    public h i(String str) {
        l.n(str);
        return new a(this, str);
    }
}

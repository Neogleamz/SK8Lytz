package com.google.common.base;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a<T> extends Optional<T> {

    /* renamed from: a  reason: collision with root package name */
    static final a<Object> f18801a = new a<>();
    private static final long serialVersionUID = 0;

    private a() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Optional<T> f() {
        return f18801a;
    }

    private Object readResolve() {
        return f18801a;
    }

    @Override // com.google.common.base.Optional
    public T b() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    @Override // com.google.common.base.Optional
    public boolean c() {
        return false;
    }

    @Override // com.google.common.base.Optional
    public T e(T t8) {
        return (T) l.o(t8, "use Optional.orNull() instead of Optional.or(null)");
    }

    public boolean equals(Object obj) {
        return obj == this;
    }

    public int hashCode() {
        return 2040732332;
    }

    public String toString() {
        return "Optional.absent()";
    }
}

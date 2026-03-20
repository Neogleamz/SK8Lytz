package defpackage;

import java.util.List;
import kotlin.collections.q;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
/* renamed from: c  reason: default package */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: b  reason: collision with root package name */
    public static final a f8205b = new a(null);

    /* renamed from: a  reason: collision with root package name */
    private final Boolean f8206a;

    /* renamed from: c$a */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(i iVar) {
            this();
        }

        public final c a(List<? extends Object> list) {
            p.e(list, "list");
            return new c((Boolean) list.get(0));
        }
    }

    public c() {
        this(null, 1, null);
    }

    public c(Boolean bool) {
        this.f8206a = bool;
    }

    public /* synthetic */ c(Boolean bool, int i8, i iVar) {
        this((i8 & 1) != 0 ? null : bool);
    }

    public final Boolean a() {
        return this.f8206a;
    }

    public final List<Object> b() {
        return q.d(this.f8206a);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof c) && p.a(this.f8206a, ((c) obj).f8206a);
    }

    public int hashCode() {
        Boolean bool = this.f8206a;
        if (bool == null) {
            return 0;
        }
        return bool.hashCode();
    }

    public String toString() {
        return "ToggleMessage(enable=" + this.f8206a + ')';
    }
}

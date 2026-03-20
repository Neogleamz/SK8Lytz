package n6;

import android.accounts.Account;
import android.view.View;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
@VisibleForTesting
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: a  reason: collision with root package name */
    private final Account f22162a;

    /* renamed from: b  reason: collision with root package name */
    private final Set f22163b;

    /* renamed from: c  reason: collision with root package name */
    private final Set f22164c;

    /* renamed from: d  reason: collision with root package name */
    private final Map f22165d;

    /* renamed from: e  reason: collision with root package name */
    private final int f22166e;

    /* renamed from: f  reason: collision with root package name */
    private final View f22167f;

    /* renamed from: g  reason: collision with root package name */
    private final String f22168g;

    /* renamed from: h  reason: collision with root package name */
    private final String f22169h;

    /* renamed from: i  reason: collision with root package name */
    private final g7.a f22170i;

    /* renamed from: j  reason: collision with root package name */
    private Integer f22171j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private Account f22172a;

        /* renamed from: b  reason: collision with root package name */
        private k0.b f22173b;

        /* renamed from: c  reason: collision with root package name */
        private String f22174c;

        /* renamed from: d  reason: collision with root package name */
        private String f22175d;

        /* renamed from: e  reason: collision with root package name */
        private g7.a f22176e = g7.a.f20204k;

        public c a() {
            return new c(this.f22172a, this.f22173b, null, 0, null, this.f22174c, this.f22175d, this.f22176e, false);
        }

        public a b(String str) {
            this.f22174c = str;
            return this;
        }

        public final a c(Collection collection) {
            if (this.f22173b == null) {
                this.f22173b = new k0.b();
            }
            this.f22173b.addAll(collection);
            return this;
        }

        public final a d(Account account) {
            this.f22172a = account;
            return this;
        }

        public final a e(String str) {
            this.f22175d = str;
            return this;
        }
    }

    public c(Account account, Set set, Map map, int i8, View view, String str, String str2, g7.a aVar, boolean z4) {
        this.f22162a = account;
        Set emptySet = set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
        this.f22163b = emptySet;
        map = map == null ? Collections.emptyMap() : map;
        this.f22165d = map;
        this.f22167f = view;
        this.f22166e = i8;
        this.f22168g = str;
        this.f22169h = str2;
        this.f22170i = aVar == null ? g7.a.f20204k : aVar;
        HashSet hashSet = new HashSet(emptySet);
        for (t tVar : map.values()) {
            hashSet.addAll(tVar.f22202a);
        }
        this.f22164c = Collections.unmodifiableSet(hashSet);
    }

    public Account a() {
        return this.f22162a;
    }

    public Account b() {
        Account account = this.f22162a;
        return account != null ? account : new Account("<<default account>>", "com.google");
    }

    public Set<Scope> c() {
        return this.f22164c;
    }

    public String d() {
        return this.f22168g;
    }

    public Set<Scope> e() {
        return this.f22163b;
    }

    public final g7.a f() {
        return this.f22170i;
    }

    public final Integer g() {
        return this.f22171j;
    }

    public final String h() {
        return this.f22169h;
    }

    public final void i(Integer num) {
        this.f22171j = num;
    }
}

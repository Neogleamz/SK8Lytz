package androidx.core.app;

import android.app.Person;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o {

    /* renamed from: a  reason: collision with root package name */
    CharSequence f4588a;

    /* renamed from: b  reason: collision with root package name */
    IconCompat f4589b;

    /* renamed from: c  reason: collision with root package name */
    String f4590c;

    /* renamed from: d  reason: collision with root package name */
    String f4591d;

    /* renamed from: e  reason: collision with root package name */
    boolean f4592e;

    /* renamed from: f  reason: collision with root package name */
    boolean f4593f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static o a(Person person) {
            return new b().f(person.getName()).c(person.getIcon() != null ? IconCompat.g(person.getIcon()) : null).g(person.getUri()).e(person.getKey()).b(person.isBot()).d(person.isImportant()).a();
        }

        static Person b(o oVar) {
            return new Person.Builder().setName(oVar.e()).setIcon(oVar.c() != null ? oVar.c().A() : null).setUri(oVar.f()).setKey(oVar.d()).setBot(oVar.g()).setImportant(oVar.h()).build();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        CharSequence f4594a;

        /* renamed from: b  reason: collision with root package name */
        IconCompat f4595b;

        /* renamed from: c  reason: collision with root package name */
        String f4596c;

        /* renamed from: d  reason: collision with root package name */
        String f4597d;

        /* renamed from: e  reason: collision with root package name */
        boolean f4598e;

        /* renamed from: f  reason: collision with root package name */
        boolean f4599f;

        public o a() {
            return new o(this);
        }

        public b b(boolean z4) {
            this.f4598e = z4;
            return this;
        }

        public b c(IconCompat iconCompat) {
            this.f4595b = iconCompat;
            return this;
        }

        public b d(boolean z4) {
            this.f4599f = z4;
            return this;
        }

        public b e(String str) {
            this.f4597d = str;
            return this;
        }

        public b f(CharSequence charSequence) {
            this.f4594a = charSequence;
            return this;
        }

        public b g(String str) {
            this.f4596c = str;
            return this;
        }
    }

    o(b bVar) {
        this.f4588a = bVar.f4594a;
        this.f4589b = bVar.f4595b;
        this.f4590c = bVar.f4596c;
        this.f4591d = bVar.f4597d;
        this.f4592e = bVar.f4598e;
        this.f4593f = bVar.f4599f;
    }

    public static o a(Person person) {
        return a.a(person);
    }

    public static o b(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle("icon");
        return new b().f(bundle.getCharSequence("name")).c(bundle2 != null ? IconCompat.f(bundle2) : null).g(bundle.getString("uri")).e(bundle.getString("key")).b(bundle.getBoolean("isBot")).d(bundle.getBoolean("isImportant")).a();
    }

    public IconCompat c() {
        return this.f4589b;
    }

    public String d() {
        return this.f4591d;
    }

    public CharSequence e() {
        return this.f4588a;
    }

    public String f() {
        return this.f4590c;
    }

    public boolean g() {
        return this.f4592e;
    }

    public boolean h() {
        return this.f4593f;
    }

    public String i() {
        String str = this.f4590c;
        if (str != null) {
            return str;
        }
        if (this.f4588a != null) {
            return "name:" + ((Object) this.f4588a);
        }
        return BuildConfig.FLAVOR;
    }

    public Person j() {
        return a.b(this);
    }

    public Bundle k() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("name", this.f4588a);
        IconCompat iconCompat = this.f4589b;
        bundle.putBundle("icon", iconCompat != null ? iconCompat.z() : null);
        bundle.putString("uri", this.f4590c);
        bundle.putString("key", this.f4591d);
        bundle.putBoolean("isBot", this.f4592e);
        bundle.putBoolean("isImportant", this.f4593f);
        return bundle;
    }
}

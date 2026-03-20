package androidx.core.view;

import android.content.ClipData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContentInfo;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: a  reason: collision with root package name */
    private final f f4942a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final InterfaceC0046c f4943a;

        public a(ClipData clipData, int i8) {
            this.f4943a = Build.VERSION.SDK_INT >= 31 ? new b(clipData, i8) : new d(clipData, i8);
        }

        public c a() {
            return this.f4943a.a();
        }

        public a b(Bundle bundle) {
            this.f4943a.setExtras(bundle);
            return this;
        }

        public a c(int i8) {
            this.f4943a.b(i8);
            return this;
        }

        public a d(Uri uri) {
            this.f4943a.c(uri);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b implements InterfaceC0046c {

        /* renamed from: a  reason: collision with root package name */
        private final ContentInfo.Builder f4944a;

        b(ClipData clipData, int i8) {
            this.f4944a = new ContentInfo.Builder(clipData, i8);
        }

        @Override // androidx.core.view.c.InterfaceC0046c
        public c a() {
            return new c(new e(this.f4944a.build()));
        }

        @Override // androidx.core.view.c.InterfaceC0046c
        public void b(int i8) {
            this.f4944a.setFlags(i8);
        }

        @Override // androidx.core.view.c.InterfaceC0046c
        public void c(Uri uri) {
            this.f4944a.setLinkUri(uri);
        }

        @Override // androidx.core.view.c.InterfaceC0046c
        public void setExtras(Bundle bundle) {
            this.f4944a.setExtras(bundle);
        }
    }

    /* renamed from: androidx.core.view.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private interface InterfaceC0046c {
        c a();

        void b(int i8);

        void c(Uri uri);

        void setExtras(Bundle bundle);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class d implements InterfaceC0046c {

        /* renamed from: a  reason: collision with root package name */
        ClipData f4945a;

        /* renamed from: b  reason: collision with root package name */
        int f4946b;

        /* renamed from: c  reason: collision with root package name */
        int f4947c;

        /* renamed from: d  reason: collision with root package name */
        Uri f4948d;

        /* renamed from: e  reason: collision with root package name */
        Bundle f4949e;

        d(ClipData clipData, int i8) {
            this.f4945a = clipData;
            this.f4946b = i8;
        }

        @Override // androidx.core.view.c.InterfaceC0046c
        public c a() {
            return new c(new g(this));
        }

        @Override // androidx.core.view.c.InterfaceC0046c
        public void b(int i8) {
            this.f4947c = i8;
        }

        @Override // androidx.core.view.c.InterfaceC0046c
        public void c(Uri uri) {
            this.f4948d = uri;
        }

        @Override // androidx.core.view.c.InterfaceC0046c
        public void setExtras(Bundle bundle) {
            this.f4949e = bundle;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class e implements f {

        /* renamed from: a  reason: collision with root package name */
        private final ContentInfo f4950a;

        e(ContentInfo contentInfo) {
            this.f4950a = (ContentInfo) androidx.core.util.h.h(contentInfo);
        }

        @Override // androidx.core.view.c.f
        public ClipData a() {
            return this.f4950a.getClip();
        }

        @Override // androidx.core.view.c.f
        public int b() {
            return this.f4950a.getFlags();
        }

        @Override // androidx.core.view.c.f
        public ContentInfo c() {
            return this.f4950a;
        }

        @Override // androidx.core.view.c.f
        public int d() {
            return this.f4950a.getSource();
        }

        public String toString() {
            return "ContentInfoCompat{" + this.f4950a + "}";
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private interface f {
        ClipData a();

        int b();

        ContentInfo c();

        int d();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class g implements f {

        /* renamed from: a  reason: collision with root package name */
        private final ClipData f4951a;

        /* renamed from: b  reason: collision with root package name */
        private final int f4952b;

        /* renamed from: c  reason: collision with root package name */
        private final int f4953c;

        /* renamed from: d  reason: collision with root package name */
        private final Uri f4954d;

        /* renamed from: e  reason: collision with root package name */
        private final Bundle f4955e;

        g(d dVar) {
            this.f4951a = (ClipData) androidx.core.util.h.h(dVar.f4945a);
            this.f4952b = androidx.core.util.h.d(dVar.f4946b, 0, 5, "source");
            this.f4953c = androidx.core.util.h.g(dVar.f4947c, 1);
            this.f4954d = dVar.f4948d;
            this.f4955e = dVar.f4949e;
        }

        @Override // androidx.core.view.c.f
        public ClipData a() {
            return this.f4951a;
        }

        @Override // androidx.core.view.c.f
        public int b() {
            return this.f4953c;
        }

        @Override // androidx.core.view.c.f
        public ContentInfo c() {
            return null;
        }

        @Override // androidx.core.view.c.f
        public int d() {
            return this.f4952b;
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("ContentInfoCompat{clip=");
            sb.append(this.f4951a.getDescription());
            sb.append(", source=");
            sb.append(c.e(this.f4952b));
            sb.append(", flags=");
            sb.append(c.a(this.f4953c));
            Uri uri = this.f4954d;
            String str2 = BuildConfig.FLAVOR;
            if (uri == null) {
                str = BuildConfig.FLAVOR;
            } else {
                str = ", hasLinkUri(" + this.f4954d.toString().length() + ")";
            }
            sb.append(str);
            if (this.f4955e != null) {
                str2 = ", hasExtras";
            }
            sb.append(str2);
            sb.append("}");
            return sb.toString();
        }
    }

    c(f fVar) {
        this.f4942a = fVar;
    }

    static String a(int i8) {
        return (i8 & 1) != 0 ? "FLAG_CONVERT_TO_PLAIN_TEXT" : String.valueOf(i8);
    }

    static String e(int i8) {
        return i8 != 0 ? i8 != 1 ? i8 != 2 ? i8 != 3 ? i8 != 4 ? i8 != 5 ? String.valueOf(i8) : "SOURCE_PROCESS_TEXT" : "SOURCE_AUTOFILL" : "SOURCE_DRAG_AND_DROP" : "SOURCE_INPUT_METHOD" : "SOURCE_CLIPBOARD" : "SOURCE_APP";
    }

    public static c g(ContentInfo contentInfo) {
        return new c(new e(contentInfo));
    }

    public ClipData b() {
        return this.f4942a.a();
    }

    public int c() {
        return this.f4942a.b();
    }

    public int d() {
        return this.f4942a.d();
    }

    public ContentInfo f() {
        ContentInfo c9 = this.f4942a.c();
        Objects.requireNonNull(c9);
        return c9;
    }

    public String toString() {
        return this.f4942a.toString();
    }
}

package u0;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.view.inputmethod.InputContentInfo;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    private final c f22958a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a implements c {

        /* renamed from: a  reason: collision with root package name */
        final InputContentInfo f22959a;

        a(Uri uri, ClipDescription clipDescription, Uri uri2) {
            this.f22959a = new InputContentInfo(uri, clipDescription, uri2);
        }

        a(Object obj) {
            this.f22959a = (InputContentInfo) obj;
        }

        @Override // u0.d.c
        public Uri a() {
            return this.f22959a.getContentUri();
        }

        @Override // u0.d.c
        public void b() {
            this.f22959a.requestPermission();
        }

        @Override // u0.d.c
        public Uri c() {
            return this.f22959a.getLinkUri();
        }

        @Override // u0.d.c
        public Object d() {
            return this.f22959a;
        }

        @Override // u0.d.c
        public ClipDescription getDescription() {
            return this.f22959a.getDescription();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b implements c {

        /* renamed from: a  reason: collision with root package name */
        private final Uri f22960a;

        /* renamed from: b  reason: collision with root package name */
        private final ClipDescription f22961b;

        /* renamed from: c  reason: collision with root package name */
        private final Uri f22962c;

        b(Uri uri, ClipDescription clipDescription, Uri uri2) {
            this.f22960a = uri;
            this.f22961b = clipDescription;
            this.f22962c = uri2;
        }

        @Override // u0.d.c
        public Uri a() {
            return this.f22960a;
        }

        @Override // u0.d.c
        public void b() {
        }

        @Override // u0.d.c
        public Uri c() {
            return this.f22962c;
        }

        @Override // u0.d.c
        public Object d() {
            return null;
        }

        @Override // u0.d.c
        public ClipDescription getDescription() {
            return this.f22961b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private interface c {
        Uri a();

        void b();

        Uri c();

        Object d();

        ClipDescription getDescription();
    }

    public d(Uri uri, ClipDescription clipDescription, Uri uri2) {
        this.f22958a = Build.VERSION.SDK_INT >= 25 ? new a(uri, clipDescription, uri2) : new b(uri, clipDescription, uri2);
    }

    private d(c cVar) {
        this.f22958a = cVar;
    }

    public static d f(Object obj) {
        if (obj != null && Build.VERSION.SDK_INT >= 25) {
            return new d(new a(obj));
        }
        return null;
    }

    public Uri a() {
        return this.f22958a.a();
    }

    public ClipDescription b() {
        return this.f22958a.getDescription();
    }

    public Uri c() {
        return this.f22958a.c();
    }

    public void d() {
        this.f22958a.b();
    }

    public Object e() {
        return this.f22958a.d();
    }
}

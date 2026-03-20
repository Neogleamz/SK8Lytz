package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.RemoteViews;
import androidx.core.app.o;
import androidx.core.graphics.drawable.IconCompat;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        final Bundle f4479a;

        /* renamed from: b  reason: collision with root package name */
        private IconCompat f4480b;

        /* renamed from: c  reason: collision with root package name */
        private final q[] f4481c;

        /* renamed from: d  reason: collision with root package name */
        private final q[] f4482d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f4483e;

        /* renamed from: f  reason: collision with root package name */
        boolean f4484f;

        /* renamed from: g  reason: collision with root package name */
        private final int f4485g;

        /* renamed from: h  reason: collision with root package name */
        private final boolean f4486h;
        @Deprecated

        /* renamed from: i  reason: collision with root package name */
        public int f4487i;

        /* renamed from: j  reason: collision with root package name */
        public CharSequence f4488j;

        /* renamed from: k  reason: collision with root package name */
        public PendingIntent f4489k;

        /* renamed from: l  reason: collision with root package name */
        private boolean f4490l;

        /* renamed from: androidx.core.app.k$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class C0030a {

            /* renamed from: a  reason: collision with root package name */
            private final IconCompat f4491a;

            /* renamed from: b  reason: collision with root package name */
            private final CharSequence f4492b;

            /* renamed from: c  reason: collision with root package name */
            private final PendingIntent f4493c;

            /* renamed from: d  reason: collision with root package name */
            private boolean f4494d;

            /* renamed from: e  reason: collision with root package name */
            private final Bundle f4495e;

            /* renamed from: f  reason: collision with root package name */
            private ArrayList<q> f4496f;

            /* renamed from: g  reason: collision with root package name */
            private int f4497g;

            /* renamed from: h  reason: collision with root package name */
            private boolean f4498h;

            /* renamed from: i  reason: collision with root package name */
            private boolean f4499i;

            /* renamed from: j  reason: collision with root package name */
            private boolean f4500j;

            public C0030a(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent) {
                this(iconCompat, charSequence, pendingIntent, new Bundle(), null, true, 0, true, false, false);
            }

            private C0030a(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, q[] qVarArr, boolean z4, int i8, boolean z8, boolean z9, boolean z10) {
                this.f4494d = true;
                this.f4498h = true;
                this.f4491a = iconCompat;
                this.f4492b = e.k(charSequence);
                this.f4493c = pendingIntent;
                this.f4495e = bundle;
                this.f4496f = qVarArr == null ? null : new ArrayList<>(Arrays.asList(qVarArr));
                this.f4494d = z4;
                this.f4497g = i8;
                this.f4498h = z8;
                this.f4499i = z9;
                this.f4500j = z10;
            }

            private void c() {
                if (this.f4499i) {
                    Objects.requireNonNull(this.f4493c, "Contextual Actions must contain a valid PendingIntent");
                }
            }

            public C0030a a(q qVar) {
                if (this.f4496f == null) {
                    this.f4496f = new ArrayList<>();
                }
                if (qVar != null) {
                    this.f4496f.add(qVar);
                }
                return this;
            }

            public a b() {
                c();
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                ArrayList<q> arrayList3 = this.f4496f;
                if (arrayList3 != null) {
                    Iterator<q> it = arrayList3.iterator();
                    while (it.hasNext()) {
                        q next = it.next();
                        if (next.l()) {
                            arrayList.add(next);
                        } else {
                            arrayList2.add(next);
                        }
                    }
                }
                q[] qVarArr = arrayList.isEmpty() ? null : (q[]) arrayList.toArray(new q[arrayList.size()]);
                return new a(this.f4491a, this.f4492b, this.f4493c, this.f4495e, arrayList2.isEmpty() ? null : (q[]) arrayList2.toArray(new q[arrayList2.size()]), qVarArr, this.f4494d, this.f4497g, this.f4498h, this.f4499i, this.f4500j);
            }

            public C0030a d(boolean z4) {
                this.f4494d = z4;
                return this;
            }

            public C0030a e(boolean z4) {
                this.f4499i = z4;
                return this;
            }

            public C0030a f(boolean z4) {
                this.f4498h = z4;
                return this;
            }
        }

        public a(int i8, CharSequence charSequence, PendingIntent pendingIntent) {
            this(i8 != 0 ? IconCompat.p(null, BuildConfig.FLAVOR, i8) : null, charSequence, pendingIntent);
        }

        public a(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent) {
            this(iconCompat, charSequence, pendingIntent, new Bundle(), null, null, true, 0, true, false, false);
        }

        a(IconCompat iconCompat, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, q[] qVarArr, q[] qVarArr2, boolean z4, int i8, boolean z8, boolean z9, boolean z10) {
            this.f4484f = true;
            this.f4480b = iconCompat;
            if (iconCompat != null && iconCompat.u() == 2) {
                this.f4487i = iconCompat.r();
            }
            this.f4488j = e.k(charSequence);
            this.f4489k = pendingIntent;
            this.f4479a = bundle == null ? new Bundle() : bundle;
            this.f4481c = qVarArr;
            this.f4482d = qVarArr2;
            this.f4483e = z4;
            this.f4485g = i8;
            this.f4484f = z8;
            this.f4486h = z9;
            this.f4490l = z10;
        }

        public PendingIntent a() {
            return this.f4489k;
        }

        public boolean b() {
            return this.f4483e;
        }

        public q[] c() {
            return this.f4482d;
        }

        public Bundle d() {
            return this.f4479a;
        }

        @Deprecated
        public int e() {
            return this.f4487i;
        }

        public IconCompat f() {
            int i8;
            if (this.f4480b == null && (i8 = this.f4487i) != 0) {
                this.f4480b = IconCompat.p(null, BuildConfig.FLAVOR, i8);
            }
            return this.f4480b;
        }

        public q[] g() {
            return this.f4481c;
        }

        public int h() {
            return this.f4485g;
        }

        public boolean i() {
            return this.f4484f;
        }

        public CharSequence j() {
            return this.f4488j;
        }

        public boolean k() {
            return this.f4490l;
        }

        public boolean l() {
            return this.f4486h;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends i {

        /* renamed from: e  reason: collision with root package name */
        private Bitmap f4501e;

        /* renamed from: f  reason: collision with root package name */
        private IconCompat f4502f;

        /* renamed from: g  reason: collision with root package name */
        private boolean f4503g;

        /* renamed from: h  reason: collision with root package name */
        private CharSequence f4504h;

        /* renamed from: i  reason: collision with root package name */
        private boolean f4505i;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class a {
            static void a(Notification.BigPictureStyle bigPictureStyle, Bitmap bitmap) {
                bigPictureStyle.bigLargeIcon(bitmap);
            }

            static void b(Notification.BigPictureStyle bigPictureStyle, CharSequence charSequence) {
                bigPictureStyle.setSummaryText(charSequence);
            }
        }

        /* renamed from: androidx.core.app.k$b$b  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class C0031b {
            static void a(Notification.BigPictureStyle bigPictureStyle, Icon icon) {
                bigPictureStyle.bigLargeIcon(icon);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class c {
            static void a(Notification.BigPictureStyle bigPictureStyle, CharSequence charSequence) {
                bigPictureStyle.setContentDescription(charSequence);
            }

            static void b(Notification.BigPictureStyle bigPictureStyle, boolean z4) {
                bigPictureStyle.showBigPictureWhenCollapsed(z4);
            }
        }

        private static IconCompat x(Parcelable parcelable) {
            if (parcelable != null) {
                if (Build.VERSION.SDK_INT < 23 || !(parcelable instanceof Icon)) {
                    if (parcelable instanceof Bitmap) {
                        return IconCompat.k((Bitmap) parcelable);
                    }
                    return null;
                }
                return IconCompat.g((Icon) parcelable);
            }
            return null;
        }

        public b A(CharSequence charSequence) {
            this.f4545b = e.k(charSequence);
            return this;
        }

        public b B(CharSequence charSequence) {
            this.f4546c = e.k(charSequence);
            this.f4547d = true;
            return this;
        }

        @Override // androidx.core.app.k.i
        public void b(j jVar) {
            int i8 = Build.VERSION.SDK_INT;
            if (i8 >= 16) {
                Notification.BigPictureStyle bigPicture = new Notification.BigPictureStyle(jVar.a()).setBigContentTitle(this.f4545b).bigPicture(this.f4501e);
                if (this.f4503g) {
                    IconCompat iconCompat = this.f4502f;
                    if (iconCompat != null) {
                        if (i8 >= 23) {
                            C0031b.a(bigPicture, this.f4502f.B(jVar instanceof l ? ((l) jVar).f() : null));
                        } else if (iconCompat.u() == 1) {
                            a.a(bigPicture, this.f4502f.q());
                        }
                    }
                    a.a(bigPicture, null);
                }
                if (this.f4547d) {
                    a.b(bigPicture, this.f4546c);
                }
                if (i8 >= 31) {
                    c.b(bigPicture, this.f4505i);
                    c.a(bigPicture, this.f4504h);
                }
            }
        }

        @Override // androidx.core.app.k.i
        protected String q() {
            return "androidx.core.app.NotificationCompat$BigPictureStyle";
        }

        @Override // androidx.core.app.k.i
        protected void v(Bundle bundle) {
            super.v(bundle);
            if (bundle.containsKey("android.largeIcon.big")) {
                this.f4502f = x(bundle.getParcelable("android.largeIcon.big"));
                this.f4503g = true;
            }
            this.f4501e = (Bitmap) bundle.getParcelable("android.picture");
            this.f4505i = bundle.getBoolean("android.showBigPictureWhenCollapsed");
        }

        public b y(Bitmap bitmap) {
            this.f4502f = bitmap == null ? null : IconCompat.k(bitmap);
            this.f4503g = true;
            return this;
        }

        public b z(Bitmap bitmap) {
            this.f4501e = bitmap;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends i {

        /* renamed from: e  reason: collision with root package name */
        private CharSequence f4506e;

        @Override // androidx.core.app.k.i
        public void a(Bundle bundle) {
            super.a(bundle);
            if (Build.VERSION.SDK_INT < 21) {
                bundle.putCharSequence("android.bigText", this.f4506e);
            }
        }

        @Override // androidx.core.app.k.i
        public void b(j jVar) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.BigTextStyle bigText = new Notification.BigTextStyle(jVar.a()).setBigContentTitle(this.f4545b).bigText(this.f4506e);
                if (this.f4547d) {
                    bigText.setSummaryText(this.f4546c);
                }
            }
        }

        @Override // androidx.core.app.k.i
        protected String q() {
            return "androidx.core.app.NotificationCompat$BigTextStyle";
        }

        @Override // androidx.core.app.k.i
        protected void v(Bundle bundle) {
            super.v(bundle);
            this.f4506e = bundle.getCharSequence("android.bigText");
        }

        public c x(CharSequence charSequence) {
            this.f4506e = e.k(charSequence);
            return this;
        }

        public c y(CharSequence charSequence) {
            this.f4545b = e.k(charSequence);
            return this;
        }

        public c z(CharSequence charSequence) {
            this.f4546c = e.k(charSequence);
            this.f4547d = true;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {
        public static Notification.BubbleMetadata a(d dVar) {
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {
        boolean A;
        boolean B;
        boolean C;
        String D;
        Bundle E;
        int F;
        int G;
        Notification H;
        RemoteViews I;
        RemoteViews J;
        RemoteViews K;
        String L;
        int M;
        String N;
        long O;
        int P;
        int Q;
        boolean R;
        d S;
        Notification T;
        boolean U;
        Icon V;
        @Deprecated
        public ArrayList<String> W;

        /* renamed from: a  reason: collision with root package name */
        public Context f4507a;

        /* renamed from: b  reason: collision with root package name */
        public ArrayList<a> f4508b;

        /* renamed from: c  reason: collision with root package name */
        public ArrayList<o> f4509c;

        /* renamed from: d  reason: collision with root package name */
        ArrayList<a> f4510d;

        /* renamed from: e  reason: collision with root package name */
        CharSequence f4511e;

        /* renamed from: f  reason: collision with root package name */
        CharSequence f4512f;

        /* renamed from: g  reason: collision with root package name */
        PendingIntent f4513g;

        /* renamed from: h  reason: collision with root package name */
        PendingIntent f4514h;

        /* renamed from: i  reason: collision with root package name */
        RemoteViews f4515i;

        /* renamed from: j  reason: collision with root package name */
        Bitmap f4516j;

        /* renamed from: k  reason: collision with root package name */
        CharSequence f4517k;

        /* renamed from: l  reason: collision with root package name */
        int f4518l;

        /* renamed from: m  reason: collision with root package name */
        int f4519m;

        /* renamed from: n  reason: collision with root package name */
        boolean f4520n;

        /* renamed from: o  reason: collision with root package name */
        boolean f4521o;

        /* renamed from: p  reason: collision with root package name */
        boolean f4522p;
        i q;

        /* renamed from: r  reason: collision with root package name */
        CharSequence f4523r;

        /* renamed from: s  reason: collision with root package name */
        CharSequence f4524s;

        /* renamed from: t  reason: collision with root package name */
        CharSequence[] f4525t;

        /* renamed from: u  reason: collision with root package name */
        int f4526u;

        /* renamed from: v  reason: collision with root package name */
        int f4527v;

        /* renamed from: w  reason: collision with root package name */
        boolean f4528w;

        /* renamed from: x  reason: collision with root package name */
        String f4529x;

        /* renamed from: y  reason: collision with root package name */
        boolean f4530y;

        /* renamed from: z  reason: collision with root package name */
        String f4531z;

        @Deprecated
        public e(Context context) {
            this(context, null);
        }

        public e(Context context, String str) {
            this.f4508b = new ArrayList<>();
            this.f4509c = new ArrayList<>();
            this.f4510d = new ArrayList<>();
            this.f4520n = true;
            this.A = false;
            this.F = 0;
            this.G = 0;
            this.M = 0;
            this.P = 0;
            this.Q = 0;
            Notification notification = new Notification();
            this.T = notification;
            this.f4507a = context;
            this.L = str;
            notification.when = System.currentTimeMillis();
            this.T.audioStreamType = -1;
            this.f4519m = 0;
            this.W = new ArrayList<>();
            this.R = true;
        }

        protected static CharSequence k(CharSequence charSequence) {
            return (charSequence != null && charSequence.length() > 5120) ? charSequence.subSequence(0, 5120) : charSequence;
        }

        private Bitmap l(Bitmap bitmap) {
            if (bitmap == null || Build.VERSION.SDK_INT >= 27) {
                return bitmap;
            }
            Resources resources = this.f4507a.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(q0.c.f22442b);
            int dimensionPixelSize2 = resources.getDimensionPixelSize(q0.c.f22441a);
            if (bitmap.getWidth() > dimensionPixelSize || bitmap.getHeight() > dimensionPixelSize2) {
                double min = Math.min(dimensionPixelSize / Math.max(1, bitmap.getWidth()), dimensionPixelSize2 / Math.max(1, bitmap.getHeight()));
                return Bitmap.createScaledBitmap(bitmap, (int) Math.ceil(bitmap.getWidth() * min), (int) Math.ceil(bitmap.getHeight() * min), true);
            }
            return bitmap;
        }

        private void x(int i8, boolean z4) {
            Notification notification;
            int i9;
            if (z4) {
                notification = this.T;
                i9 = i8 | notification.flags;
            } else {
                notification = this.T;
                i9 = (~i8) & notification.flags;
            }
            notification.flags = i9;
        }

        public e A(int i8) {
            this.P = i8;
            return this;
        }

        public e B(boolean z4) {
            this.f4530y = z4;
            return this;
        }

        public e C(Bitmap bitmap) {
            this.f4516j = l(bitmap);
            return this;
        }

        public e D(int i8, int i9, int i10) {
            Notification notification = this.T;
            notification.ledARGB = i8;
            notification.ledOnMS = i9;
            notification.ledOffMS = i10;
            notification.flags = ((i9 == 0 || i10 == 0) ? 0 : 1) | (notification.flags & (-2));
            return this;
        }

        public e E(boolean z4) {
            this.A = z4;
            return this;
        }

        public e F(int i8) {
            this.f4518l = i8;
            return this;
        }

        public e G(boolean z4) {
            x(2, z4);
            return this;
        }

        public e H(boolean z4) {
            x(8, z4);
            return this;
        }

        public e I(int i8) {
            this.f4519m = i8;
            return this;
        }

        public e J(int i8, int i9, boolean z4) {
            this.f4526u = i8;
            this.f4527v = i9;
            this.f4528w = z4;
            return this;
        }

        public e K(String str) {
            this.N = str;
            return this;
        }

        public e L(boolean z4) {
            this.f4520n = z4;
            return this;
        }

        public e M(boolean z4) {
            this.U = z4;
            return this;
        }

        public e N(int i8) {
            this.T.icon = i8;
            return this;
        }

        public e O(Uri uri) {
            Notification notification = this.T;
            notification.sound = uri;
            notification.audioStreamType = -1;
            if (Build.VERSION.SDK_INT >= 21) {
                notification.audioAttributes = new AudioAttributes.Builder().setContentType(4).setUsage(5).build();
            }
            return this;
        }

        public e P(i iVar) {
            if (this.q != iVar) {
                this.q = iVar;
                if (iVar != null) {
                    iVar.w(this);
                }
            }
            return this;
        }

        public e Q(CharSequence charSequence) {
            this.f4523r = k(charSequence);
            return this;
        }

        public e R(CharSequence charSequence) {
            this.T.tickerText = k(charSequence);
            return this;
        }

        public e S(long j8) {
            this.O = j8;
            return this;
        }

        public e T(boolean z4) {
            this.f4521o = z4;
            return this;
        }

        public e U(long[] jArr) {
            this.T.vibrate = jArr;
            return this;
        }

        public e V(int i8) {
            this.G = i8;
            return this;
        }

        public e W(long j8) {
            this.T.when = j8;
            return this;
        }

        public e a(int i8, CharSequence charSequence, PendingIntent pendingIntent) {
            this.f4508b.add(new a(i8, charSequence, pendingIntent));
            return this;
        }

        public e b(a aVar) {
            if (aVar != null) {
                this.f4508b.add(aVar);
            }
            return this;
        }

        public Notification c() {
            return new l(this).c();
        }

        public RemoteViews d() {
            return this.J;
        }

        public int e() {
            return this.F;
        }

        public RemoteViews f() {
            return this.I;
        }

        public Bundle g() {
            if (this.E == null) {
                this.E = new Bundle();
            }
            return this.E;
        }

        public RemoteViews h() {
            return this.K;
        }

        public int i() {
            return this.f4519m;
        }

        public long j() {
            if (this.f4520n) {
                return this.T.when;
            }
            return 0L;
        }

        public e m(boolean z4) {
            x(16, z4);
            return this;
        }

        public e n(String str) {
            this.D = str;
            return this;
        }

        public e o(String str) {
            this.L = str;
            return this;
        }

        public e p(boolean z4) {
            this.f4522p = z4;
            g().putBoolean("android.chronometerCountDown", z4);
            return this;
        }

        public e q(int i8) {
            this.F = i8;
            return this;
        }

        public e r(boolean z4) {
            this.B = z4;
            this.C = true;
            return this;
        }

        public e s(PendingIntent pendingIntent) {
            this.f4513g = pendingIntent;
            return this;
        }

        public e t(CharSequence charSequence) {
            this.f4512f = k(charSequence);
            return this;
        }

        public e u(CharSequence charSequence) {
            this.f4511e = k(charSequence);
            return this;
        }

        public e v(int i8) {
            Notification notification = this.T;
            notification.defaults = i8;
            if ((i8 & 4) != 0) {
                notification.flags |= 1;
            }
            return this;
        }

        public e w(PendingIntent pendingIntent) {
            this.T.deleteIntent = pendingIntent;
            return this;
        }

        public e y(PendingIntent pendingIntent, boolean z4) {
            this.f4514h = pendingIntent;
            x(RecognitionOptions.ITF, z4);
            return this;
        }

        public e z(String str) {
            this.f4529x = str;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f extends i {
        private RemoteViews x(RemoteViews remoteViews, boolean z4) {
            int min;
            boolean z8 = true;
            RemoteViews c9 = c(true, q0.g.f22497c, false);
            c9.removeAllViews(q0.e.L);
            List<a> z9 = z(this.f4544a.f4508b);
            if (!z4 || z9 == null || (min = Math.min(z9.size(), 3)) <= 0) {
                z8 = false;
            } else {
                for (int i8 = 0; i8 < min; i8++) {
                    c9.addView(q0.e.L, y(z9.get(i8)));
                }
            }
            int i9 = z8 ? 0 : 8;
            c9.setViewVisibility(q0.e.L, i9);
            c9.setViewVisibility(q0.e.I, i9);
            d(c9, remoteViews);
            return c9;
        }

        private RemoteViews y(a aVar) {
            boolean z4 = aVar.f4489k == null;
            RemoteViews remoteViews = new RemoteViews(this.f4544a.f4507a.getPackageName(), z4 ? q0.g.f22496b : q0.g.f22495a);
            IconCompat f5 = aVar.f();
            if (f5 != null) {
                remoteViews.setImageViewBitmap(q0.e.J, m(f5, this.f4544a.f4507a.getResources().getColor(q0.b.f22440a)));
            }
            remoteViews.setTextViewText(q0.e.K, aVar.f4488j);
            if (!z4) {
                remoteViews.setOnClickPendingIntent(q0.e.H, aVar.f4489k);
            }
            if (Build.VERSION.SDK_INT >= 15) {
                remoteViews.setContentDescription(q0.e.H, aVar.f4488j);
            }
            return remoteViews;
        }

        private static List<a> z(List<a> list) {
            if (list == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (a aVar : list) {
                if (!aVar.l()) {
                    arrayList.add(aVar);
                }
            }
            return arrayList;
        }

        @Override // androidx.core.app.k.i
        public void b(j jVar) {
            if (Build.VERSION.SDK_INT >= 24) {
                jVar.a().setStyle(new Notification.DecoratedCustomViewStyle());
            }
        }

        @Override // androidx.core.app.k.i
        protected String q() {
            return "androidx.core.app.NotificationCompat$DecoratedCustomViewStyle";
        }

        @Override // androidx.core.app.k.i
        public RemoteViews s(j jVar) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            RemoteViews d8 = this.f4544a.d();
            if (d8 == null) {
                d8 = this.f4544a.f();
            }
            if (d8 == null) {
                return null;
            }
            return x(d8, true);
        }

        @Override // androidx.core.app.k.i
        public RemoteViews t(j jVar) {
            if (Build.VERSION.SDK_INT < 24 && this.f4544a.f() != null) {
                return x(this.f4544a.f(), false);
            }
            return null;
        }

        @Override // androidx.core.app.k.i
        public RemoteViews u(j jVar) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            RemoteViews h8 = this.f4544a.h();
            RemoteViews f5 = h8 != null ? h8 : this.f4544a.f();
            if (h8 == null) {
                return null;
            }
            return x(f5, true);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g extends i {

        /* renamed from: e  reason: collision with root package name */
        private ArrayList<CharSequence> f4532e = new ArrayList<>();

        @Override // androidx.core.app.k.i
        public void b(j jVar) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.InboxStyle bigContentTitle = new Notification.InboxStyle(jVar.a()).setBigContentTitle(this.f4545b);
                if (this.f4547d) {
                    bigContentTitle.setSummaryText(this.f4546c);
                }
                Iterator<CharSequence> it = this.f4532e.iterator();
                while (it.hasNext()) {
                    bigContentTitle.addLine(it.next());
                }
            }
        }

        @Override // androidx.core.app.k.i
        protected String q() {
            return "androidx.core.app.NotificationCompat$InboxStyle";
        }

        @Override // androidx.core.app.k.i
        protected void v(Bundle bundle) {
            super.v(bundle);
            this.f4532e.clear();
            if (bundle.containsKey("android.textLines")) {
                Collections.addAll(this.f4532e, bundle.getCharSequenceArray("android.textLines"));
            }
        }

        public g x(CharSequence charSequence) {
            if (charSequence != null) {
                this.f4532e.add(e.k(charSequence));
            }
            return this;
        }

        public g y(CharSequence charSequence) {
            this.f4545b = e.k(charSequence);
            return this;
        }

        public g z(CharSequence charSequence) {
            this.f4546c = e.k(charSequence);
            this.f4547d = true;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h extends i {

        /* renamed from: e  reason: collision with root package name */
        private final List<a> f4533e = new ArrayList();

        /* renamed from: f  reason: collision with root package name */
        private final List<a> f4534f = new ArrayList();

        /* renamed from: g  reason: collision with root package name */
        private o f4535g;

        /* renamed from: h  reason: collision with root package name */
        private CharSequence f4536h;

        /* renamed from: i  reason: collision with root package name */
        private Boolean f4537i;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            private final CharSequence f4538a;

            /* renamed from: b  reason: collision with root package name */
            private final long f4539b;

            /* renamed from: c  reason: collision with root package name */
            private final o f4540c;

            /* renamed from: d  reason: collision with root package name */
            private Bundle f4541d = new Bundle();

            /* renamed from: e  reason: collision with root package name */
            private String f4542e;

            /* renamed from: f  reason: collision with root package name */
            private Uri f4543f;

            public a(CharSequence charSequence, long j8, o oVar) {
                this.f4538a = charSequence;
                this.f4539b = j8;
                this.f4540c = oVar;
            }

            static Bundle[] a(List<a> list) {
                Bundle[] bundleArr = new Bundle[list.size()];
                int size = list.size();
                for (int i8 = 0; i8 < size; i8++) {
                    bundleArr[i8] = list.get(i8).l();
                }
                return bundleArr;
            }

            static a e(Bundle bundle) {
                try {
                    if (bundle.containsKey("text") && bundle.containsKey("time")) {
                        a aVar = new a(bundle.getCharSequence("text"), bundle.getLong("time"), bundle.containsKey("person") ? o.b(bundle.getBundle("person")) : (!bundle.containsKey("sender_person") || Build.VERSION.SDK_INT < 28) ? bundle.containsKey("sender") ? new o.b().f(bundle.getCharSequence("sender")).a() : null : o.a((Person) bundle.getParcelable("sender_person")));
                        if (bundle.containsKey("type") && bundle.containsKey("uri")) {
                            aVar.j(bundle.getString("type"), (Uri) bundle.getParcelable("uri"));
                        }
                        if (bundle.containsKey("extras")) {
                            aVar.d().putAll(bundle.getBundle("extras"));
                        }
                        return aVar;
                    }
                } catch (ClassCastException unused) {
                }
                return null;
            }

            static List<a> f(Parcelable[] parcelableArr) {
                a e8;
                ArrayList arrayList = new ArrayList(parcelableArr.length);
                for (int i8 = 0; i8 < parcelableArr.length; i8++) {
                    if ((parcelableArr[i8] instanceof Bundle) && (e8 = e((Bundle) parcelableArr[i8])) != null) {
                        arrayList.add(e8);
                    }
                }
                return arrayList;
            }

            private Bundle l() {
                Bundle bundle = new Bundle();
                CharSequence charSequence = this.f4538a;
                if (charSequence != null) {
                    bundle.putCharSequence("text", charSequence);
                }
                bundle.putLong("time", this.f4539b);
                o oVar = this.f4540c;
                if (oVar != null) {
                    bundle.putCharSequence("sender", oVar.e());
                    if (Build.VERSION.SDK_INT >= 28) {
                        bundle.putParcelable("sender_person", this.f4540c.j());
                    } else {
                        bundle.putBundle("person", this.f4540c.k());
                    }
                }
                String str = this.f4542e;
                if (str != null) {
                    bundle.putString("type", str);
                }
                Uri uri = this.f4543f;
                if (uri != null) {
                    bundle.putParcelable("uri", uri);
                }
                Bundle bundle2 = this.f4541d;
                if (bundle2 != null) {
                    bundle.putBundle("extras", bundle2);
                }
                return bundle;
            }

            public String b() {
                return this.f4542e;
            }

            public Uri c() {
                return this.f4543f;
            }

            public Bundle d() {
                return this.f4541d;
            }

            public o g() {
                return this.f4540c;
            }

            public CharSequence h() {
                return this.f4538a;
            }

            public long i() {
                return this.f4539b;
            }

            public a j(String str, Uri uri) {
                this.f4542e = str;
                this.f4543f = uri;
                return this;
            }

            Notification.MessagingStyle.Message k() {
                Notification.MessagingStyle.Message message;
                o g8 = g();
                if (Build.VERSION.SDK_INT >= 28) {
                    message = new Notification.MessagingStyle.Message(h(), i(), g8 != null ? g8.j() : null);
                } else {
                    message = new Notification.MessagingStyle.Message(h(), i(), g8 != null ? g8.e() : null);
                }
                if (b() != null) {
                    message.setData(b(), c());
                }
                return message;
            }
        }

        h() {
        }

        public h(o oVar) {
            if (TextUtils.isEmpty(oVar.e())) {
                throw new IllegalArgumentException("User's name must not be empty.");
            }
            this.f4535g = oVar;
        }

        private boolean D() {
            for (int size = this.f4533e.size() - 1; size >= 0; size--) {
                a aVar = this.f4533e.get(size);
                if (aVar.g() != null && aVar.g().e() == null) {
                    return true;
                }
            }
            return false;
        }

        private TextAppearanceSpan F(int i8) {
            return new TextAppearanceSpan(null, 0, 0, ColorStateList.valueOf(i8), null);
        }

        private CharSequence G(a aVar) {
            androidx.core.text.a c9 = androidx.core.text.a.c();
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            boolean z4 = Build.VERSION.SDK_INT >= 21;
            int i8 = z4 ? -16777216 : -1;
            o g8 = aVar.g();
            CharSequence charSequence = BuildConfig.FLAVOR;
            CharSequence e8 = g8 == null ? BuildConfig.FLAVOR : aVar.g().e();
            if (TextUtils.isEmpty(e8)) {
                e8 = this.f4535g.e();
                if (z4 && this.f4544a.e() != 0) {
                    i8 = this.f4544a.e();
                }
            }
            CharSequence h8 = c9.h(e8);
            spannableStringBuilder.append(h8);
            spannableStringBuilder.setSpan(F(i8), spannableStringBuilder.length() - h8.length(), spannableStringBuilder.length(), 33);
            if (aVar.h() != null) {
                charSequence = aVar.h();
            }
            spannableStringBuilder.append((CharSequence) "  ").append(c9.h(charSequence));
            return spannableStringBuilder;
        }

        public static h y(Notification notification) {
            i p8 = i.p(notification);
            if (p8 instanceof h) {
                return (h) p8;
            }
            return null;
        }

        private a z() {
            List<a> list;
            for (int size = this.f4533e.size() - 1; size >= 0; size--) {
                a aVar = this.f4533e.get(size);
                if (aVar.g() != null && !TextUtils.isEmpty(aVar.g().e())) {
                    return aVar;
                }
            }
            if (this.f4533e.isEmpty()) {
                return null;
            }
            return this.f4533e.get(list.size() - 1);
        }

        public CharSequence A() {
            return this.f4536h;
        }

        public List<a> B() {
            return this.f4533e;
        }

        public o C() {
            return this.f4535g;
        }

        public boolean E() {
            e eVar = this.f4544a;
            if (eVar != null && eVar.f4507a.getApplicationInfo().targetSdkVersion < 28 && this.f4537i == null) {
                return this.f4536h != null;
            }
            Boolean bool = this.f4537i;
            if (bool != null) {
                return bool.booleanValue();
            }
            return false;
        }

        public h H(CharSequence charSequence) {
            this.f4536h = charSequence;
            return this;
        }

        public h I(boolean z4) {
            this.f4537i = Boolean.valueOf(z4);
            return this;
        }

        @Override // androidx.core.app.k.i
        public void a(Bundle bundle) {
            super.a(bundle);
            bundle.putCharSequence("android.selfDisplayName", this.f4535g.e());
            bundle.putBundle("android.messagingStyleUser", this.f4535g.k());
            bundle.putCharSequence("android.hiddenConversationTitle", this.f4536h);
            if (this.f4536h != null && this.f4537i.booleanValue()) {
                bundle.putCharSequence("android.conversationTitle", this.f4536h);
            }
            if (!this.f4533e.isEmpty()) {
                bundle.putParcelableArray("android.messages", a.a(this.f4533e));
            }
            if (!this.f4534f.isEmpty()) {
                bundle.putParcelableArray("android.messages.historic", a.a(this.f4534f));
            }
            Boolean bool = this.f4537i;
            if (bool != null) {
                bundle.putBoolean("android.isGroupConversation", bool.booleanValue());
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:38:0x00c3  */
        /* JADX WARN: Removed duplicated region for block: B:45:0x00db  */
        /* JADX WARN: Removed duplicated region for block: B:70:? A[RETURN, SYNTHETIC] */
        @Override // androidx.core.app.k.i
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void b(androidx.core.app.j r8) {
            /*
                Method dump skipped, instructions count: 306
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.k.h.b(androidx.core.app.j):void");
        }

        @Override // androidx.core.app.k.i
        protected String q() {
            return "androidx.core.app.NotificationCompat$MessagingStyle";
        }

        @Override // androidx.core.app.k.i
        protected void v(Bundle bundle) {
            super.v(bundle);
            this.f4533e.clear();
            this.f4535g = bundle.containsKey("android.messagingStyleUser") ? o.b(bundle.getBundle("android.messagingStyleUser")) : new o.b().f(bundle.getString("android.selfDisplayName")).a();
            CharSequence charSequence = bundle.getCharSequence("android.conversationTitle");
            this.f4536h = charSequence;
            if (charSequence == null) {
                this.f4536h = bundle.getCharSequence("android.hiddenConversationTitle");
            }
            Parcelable[] parcelableArray = bundle.getParcelableArray("android.messages");
            if (parcelableArray != null) {
                this.f4533e.addAll(a.f(parcelableArray));
            }
            Parcelable[] parcelableArray2 = bundle.getParcelableArray("android.messages.historic");
            if (parcelableArray2 != null) {
                this.f4534f.addAll(a.f(parcelableArray2));
            }
            if (bundle.containsKey("android.isGroupConversation")) {
                this.f4537i = Boolean.valueOf(bundle.getBoolean("android.isGroupConversation"));
            }
        }

        public h x(a aVar) {
            if (aVar != null) {
                this.f4533e.add(aVar);
                if (this.f4533e.size() > 25) {
                    this.f4533e.remove(0);
                }
            }
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class i {

        /* renamed from: a  reason: collision with root package name */
        protected e f4544a;

        /* renamed from: b  reason: collision with root package name */
        CharSequence f4545b;

        /* renamed from: c  reason: collision with root package name */
        CharSequence f4546c;

        /* renamed from: d  reason: collision with root package name */
        boolean f4547d = false;

        private int e() {
            Resources resources = this.f4544a.f4507a.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(q0.c.f22449i);
            int dimensionPixelSize2 = resources.getDimensionPixelSize(q0.c.f22450j);
            float f5 = (f(resources.getConfiguration().fontScale, 1.0f, 1.3f) - 1.0f) / 0.29999995f;
            return Math.round(((1.0f - f5) * dimensionPixelSize) + (f5 * dimensionPixelSize2));
        }

        private static float f(float f5, float f8, float f9) {
            return f5 < f8 ? f8 : f5 > f9 ? f9 : f5;
        }

        static i g(String str) {
            if (str != null) {
                char c9 = 65535;
                switch (str.hashCode()) {
                    case -716705180:
                        if (str.equals("androidx.core.app.NotificationCompat$DecoratedCustomViewStyle")) {
                            c9 = 0;
                            break;
                        }
                        break;
                    case -171946061:
                        if (str.equals("androidx.core.app.NotificationCompat$BigPictureStyle")) {
                            c9 = 1;
                            break;
                        }
                        break;
                    case 912942987:
                        if (str.equals("androidx.core.app.NotificationCompat$InboxStyle")) {
                            c9 = 2;
                            break;
                        }
                        break;
                    case 919595044:
                        if (str.equals("androidx.core.app.NotificationCompat$BigTextStyle")) {
                            c9 = 3;
                            break;
                        }
                        break;
                    case 2090799565:
                        if (str.equals("androidx.core.app.NotificationCompat$MessagingStyle")) {
                            c9 = 4;
                            break;
                        }
                        break;
                }
                switch (c9) {
                    case 0:
                        return new f();
                    case 1:
                        return new b();
                    case 2:
                        return new g();
                    case 3:
                        return new c();
                    case 4:
                        return new h();
                    default:
                        return null;
                }
            }
            return null;
        }

        private static i h(String str) {
            int i8;
            if (str != null && (i8 = Build.VERSION.SDK_INT) >= 16) {
                if (str.equals(Notification.BigPictureStyle.class.getName())) {
                    return new b();
                }
                if (str.equals(Notification.BigTextStyle.class.getName())) {
                    return new c();
                }
                if (str.equals(Notification.InboxStyle.class.getName())) {
                    return new g();
                }
                if (i8 >= 24) {
                    if (str.equals(Notification.MessagingStyle.class.getName())) {
                        return new h();
                    }
                    if (str.equals(Notification.DecoratedCustomViewStyle.class.getName())) {
                        return new f();
                    }
                }
            }
            return null;
        }

        static i i(Bundle bundle) {
            i g8 = g(bundle.getString("androidx.core.app.extra.COMPAT_TEMPLATE"));
            return g8 != null ? g8 : (bundle.containsKey("android.selfDisplayName") || bundle.containsKey("android.messagingStyleUser")) ? new h() : bundle.containsKey("android.picture") ? new b() : bundle.containsKey("android.bigText") ? new c() : bundle.containsKey("android.textLines") ? new g() : h(bundle.getString("android.template"));
        }

        static i j(Bundle bundle) {
            i i8 = i(bundle);
            if (i8 == null) {
                return null;
            }
            try {
                i8.v(bundle);
                return i8;
            } catch (ClassCastException unused) {
                return null;
            }
        }

        private Bitmap l(int i8, int i9, int i10) {
            return n(IconCompat.o(this.f4544a.f4507a, i8), i9, i10);
        }

        private Bitmap n(IconCompat iconCompat, int i8, int i9) {
            Drawable x8 = iconCompat.x(this.f4544a.f4507a);
            int intrinsicWidth = i9 == 0 ? x8.getIntrinsicWidth() : i9;
            if (i9 == 0) {
                i9 = x8.getIntrinsicHeight();
            }
            Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, i9, Bitmap.Config.ARGB_8888);
            x8.setBounds(0, 0, intrinsicWidth, i9);
            if (i8 != 0) {
                x8.mutate().setColorFilter(new PorterDuffColorFilter(i8, PorterDuff.Mode.SRC_IN));
            }
            x8.draw(new Canvas(createBitmap));
            return createBitmap;
        }

        private Bitmap o(int i8, int i9, int i10, int i11) {
            int i12 = q0.d.f22453c;
            if (i11 == 0) {
                i11 = 0;
            }
            Bitmap l8 = l(i12, i11, i9);
            Canvas canvas = new Canvas(l8);
            Drawable mutate = this.f4544a.f4507a.getResources().getDrawable(i8).mutate();
            mutate.setFilterBitmap(true);
            int i13 = (i9 - i10) / 2;
            int i14 = i10 + i13;
            mutate.setBounds(i13, i13, i14, i14);
            mutate.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_ATOP));
            mutate.draw(canvas);
            return l8;
        }

        public static i p(Notification notification) {
            Bundle a9 = k.a(notification);
            if (a9 == null) {
                return null;
            }
            return j(a9);
        }

        private void r(RemoteViews remoteViews) {
            remoteViews.setViewVisibility(q0.e.f22481m0, 8);
            remoteViews.setViewVisibility(q0.e.f22477k0, 8);
            remoteViews.setViewVisibility(q0.e.f22475j0, 8);
        }

        public void a(Bundle bundle) {
            if (this.f4547d) {
                bundle.putCharSequence("android.summaryText", this.f4546c);
            }
            CharSequence charSequence = this.f4545b;
            if (charSequence != null) {
                bundle.putCharSequence("android.title.big", charSequence);
            }
            String q = q();
            if (q != null) {
                bundle.putString("androidx.core.app.extra.COMPAT_TEMPLATE", q);
            }
        }

        public abstract void b(j jVar);

        /* JADX WARN: Removed duplicated region for block: B:65:0x016c  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x0176  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x017e A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0182  */
        /* JADX WARN: Removed duplicated region for block: B:75:0x01a4  */
        /* JADX WARN: Removed duplicated region for block: B:84:0x01ea  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x01ef  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x01f1  */
        /* JADX WARN: Removed duplicated region for block: B:92:0x01fa  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public android.widget.RemoteViews c(boolean r17, int r18, boolean r19) {
            /*
                Method dump skipped, instructions count: 511
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.k.i.c(boolean, int, boolean):android.widget.RemoteViews");
        }

        public void d(RemoteViews remoteViews, RemoteViews remoteViews2) {
            r(remoteViews);
            int i8 = q0.e.S;
            remoteViews.removeAllViews(i8);
            remoteViews.addView(i8, remoteViews2.clone());
            remoteViews.setViewVisibility(i8, 0);
            if (Build.VERSION.SDK_INT >= 21) {
                remoteViews.setViewPadding(q0.e.T, 0, e(), 0, 0);
            }
        }

        public Bitmap k(int i8, int i9) {
            return l(i8, i9, 0);
        }

        Bitmap m(IconCompat iconCompat, int i8) {
            return n(iconCompat, i8, 0);
        }

        protected String q() {
            return null;
        }

        public RemoteViews s(j jVar) {
            return null;
        }

        public RemoteViews t(j jVar) {
            return null;
        }

        public RemoteViews u(j jVar) {
            return null;
        }

        protected void v(Bundle bundle) {
            if (bundle.containsKey("android.summaryText")) {
                this.f4546c = bundle.getCharSequence("android.summaryText");
                this.f4547d = true;
            }
            this.f4545b = bundle.getCharSequence("android.title.big");
        }

        public void w(e eVar) {
            if (this.f4544a != eVar) {
                this.f4544a = eVar;
                if (eVar != null) {
                    eVar.P(this);
                }
            }
        }
    }

    public static Bundle a(Notification notification) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 19) {
            return notification.extras;
        }
        if (i8 >= 16) {
            return m.c(notification);
        }
        return null;
    }
}

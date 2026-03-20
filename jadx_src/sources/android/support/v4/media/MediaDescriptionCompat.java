package android.support.v4.media;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MediaDescriptionCompat implements Parcelable {
    public static final Parcelable.Creator<MediaDescriptionCompat> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private final String f262a;

    /* renamed from: b  reason: collision with root package name */
    private final CharSequence f263b;

    /* renamed from: c  reason: collision with root package name */
    private final CharSequence f264c;

    /* renamed from: d  reason: collision with root package name */
    private final CharSequence f265d;

    /* renamed from: e  reason: collision with root package name */
    private final Bitmap f266e;

    /* renamed from: f  reason: collision with root package name */
    private final Uri f267f;

    /* renamed from: g  reason: collision with root package name */
    private final Bundle f268g;

    /* renamed from: h  reason: collision with root package name */
    private final Uri f269h;

    /* renamed from: j  reason: collision with root package name */
    private MediaDescription f270j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<MediaDescriptionCompat> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public MediaDescriptionCompat createFromParcel(Parcel parcel) {
            return Build.VERSION.SDK_INT < 21 ? new MediaDescriptionCompat(parcel) : MediaDescriptionCompat.a(MediaDescription.CREATOR.createFromParcel(parcel));
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public MediaDescriptionCompat[] newArray(int i8) {
            return new MediaDescriptionCompat[i8];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static MediaDescription a(MediaDescription.Builder builder) {
            return builder.build();
        }

        static MediaDescription.Builder b() {
            return new MediaDescription.Builder();
        }

        static CharSequence c(MediaDescription mediaDescription) {
            return mediaDescription.getDescription();
        }

        static Bundle d(MediaDescription mediaDescription) {
            return mediaDescription.getExtras();
        }

        static Bitmap e(MediaDescription mediaDescription) {
            return mediaDescription.getIconBitmap();
        }

        static Uri f(MediaDescription mediaDescription) {
            return mediaDescription.getIconUri();
        }

        static String g(MediaDescription mediaDescription) {
            return mediaDescription.getMediaId();
        }

        static CharSequence h(MediaDescription mediaDescription) {
            return mediaDescription.getSubtitle();
        }

        static CharSequence i(MediaDescription mediaDescription) {
            return mediaDescription.getTitle();
        }

        static void j(MediaDescription.Builder builder, CharSequence charSequence) {
            builder.setDescription(charSequence);
        }

        static void k(MediaDescription.Builder builder, Bundle bundle) {
            builder.setExtras(bundle);
        }

        static void l(MediaDescription.Builder builder, Bitmap bitmap) {
            builder.setIconBitmap(bitmap);
        }

        static void m(MediaDescription.Builder builder, Uri uri) {
            builder.setIconUri(uri);
        }

        static void n(MediaDescription.Builder builder, String str) {
            builder.setMediaId(str);
        }

        static void o(MediaDescription.Builder builder, CharSequence charSequence) {
            builder.setSubtitle(charSequence);
        }

        static void p(MediaDescription.Builder builder, CharSequence charSequence) {
            builder.setTitle(charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {
        static Uri a(MediaDescription mediaDescription) {
            return mediaDescription.getMediaUri();
        }

        static void b(MediaDescription.Builder builder, Uri uri) {
            builder.setMediaUri(uri);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        private String f271a;

        /* renamed from: b  reason: collision with root package name */
        private CharSequence f272b;

        /* renamed from: c  reason: collision with root package name */
        private CharSequence f273c;

        /* renamed from: d  reason: collision with root package name */
        private CharSequence f274d;

        /* renamed from: e  reason: collision with root package name */
        private Bitmap f275e;

        /* renamed from: f  reason: collision with root package name */
        private Uri f276f;

        /* renamed from: g  reason: collision with root package name */
        private Bundle f277g;

        /* renamed from: h  reason: collision with root package name */
        private Uri f278h;

        public MediaDescriptionCompat a() {
            return new MediaDescriptionCompat(this.f271a, this.f272b, this.f273c, this.f274d, this.f275e, this.f276f, this.f277g, this.f278h);
        }

        public d b(CharSequence charSequence) {
            this.f274d = charSequence;
            return this;
        }

        public d c(Bundle bundle) {
            this.f277g = bundle;
            return this;
        }

        public d d(Bitmap bitmap) {
            this.f275e = bitmap;
            return this;
        }

        public d e(Uri uri) {
            this.f276f = uri;
            return this;
        }

        public d f(String str) {
            this.f271a = str;
            return this;
        }

        public d g(Uri uri) {
            this.f278h = uri;
            return this;
        }

        public d h(CharSequence charSequence) {
            this.f273c = charSequence;
            return this;
        }

        public d i(CharSequence charSequence) {
            this.f272b = charSequence;
            return this;
        }
    }

    MediaDescriptionCompat(Parcel parcel) {
        this.f262a = parcel.readString();
        this.f263b = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.f264c = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.f265d = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        ClassLoader classLoader = MediaDescriptionCompat.class.getClassLoader();
        this.f266e = (Bitmap) parcel.readParcelable(classLoader);
        this.f267f = (Uri) parcel.readParcelable(classLoader);
        this.f268g = parcel.readBundle(classLoader);
        this.f269h = (Uri) parcel.readParcelable(classLoader);
    }

    MediaDescriptionCompat(String str, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, Bitmap bitmap, Uri uri, Bundle bundle, Uri uri2) {
        this.f262a = str;
        this.f263b = charSequence;
        this.f264c = charSequence2;
        this.f265d = charSequence3;
        this.f266e = bitmap;
        this.f267f = uri;
        this.f268g = bundle;
        this.f269h = uri2;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static android.support.v4.media.MediaDescriptionCompat a(java.lang.Object r9) {
        /*
            r0 = 0
            if (r9 == 0) goto L83
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 21
            if (r1 < r2) goto L83
            android.support.v4.media.MediaDescriptionCompat$d r2 = new android.support.v4.media.MediaDescriptionCompat$d
            r2.<init>()
            android.media.MediaDescription r9 = (android.media.MediaDescription) r9
            java.lang.String r3 = android.support.v4.media.MediaDescriptionCompat.b.g(r9)
            r2.f(r3)
            java.lang.CharSequence r3 = android.support.v4.media.MediaDescriptionCompat.b.i(r9)
            r2.i(r3)
            java.lang.CharSequence r3 = android.support.v4.media.MediaDescriptionCompat.b.h(r9)
            r2.h(r3)
            java.lang.CharSequence r3 = android.support.v4.media.MediaDescriptionCompat.b.c(r9)
            r2.b(r3)
            android.graphics.Bitmap r3 = android.support.v4.media.MediaDescriptionCompat.b.e(r9)
            r2.d(r3)
            android.net.Uri r3 = android.support.v4.media.MediaDescriptionCompat.b.f(r9)
            r2.e(r3)
            android.os.Bundle r3 = android.support.v4.media.MediaDescriptionCompat.b.d(r9)
            if (r3 == 0) goto L44
            android.os.Bundle r3 = android.support.v4.media.session.MediaSessionCompat.c(r3)
        L44:
            java.lang.String r4 = "android.support.v4.media.description.MEDIA_URI"
            if (r3 == 0) goto L4f
            android.os.Parcelable r5 = r3.getParcelable(r4)
            android.net.Uri r5 = (android.net.Uri) r5
            goto L50
        L4f:
            r5 = r0
        L50:
            if (r5 == 0) goto L68
            java.lang.String r6 = "android.support.v4.media.description.NULL_BUNDLE_FLAG"
            boolean r7 = r3.containsKey(r6)
            if (r7 == 0) goto L62
            int r7 = r3.size()
            r8 = 2
            if (r7 != r8) goto L62
            goto L69
        L62:
            r3.remove(r4)
            r3.remove(r6)
        L68:
            r0 = r3
        L69:
            r2.c(r0)
            if (r5 == 0) goto L72
            r2.g(r5)
            goto L7d
        L72:
            r0 = 23
            if (r1 < r0) goto L7d
            android.net.Uri r0 = android.support.v4.media.MediaDescriptionCompat.c.a(r9)
            r2.g(r0)
        L7d:
            android.support.v4.media.MediaDescriptionCompat r0 = r2.a()
            r0.f270j = r9
        L83:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.media.MediaDescriptionCompat.a(java.lang.Object):android.support.v4.media.MediaDescriptionCompat");
    }

    public Object b() {
        int i8;
        MediaDescription mediaDescription = this.f270j;
        if (mediaDescription != null || (i8 = Build.VERSION.SDK_INT) < 21) {
            return mediaDescription;
        }
        MediaDescription.Builder b9 = b.b();
        b.n(b9, this.f262a);
        b.p(b9, this.f263b);
        b.o(b9, this.f264c);
        b.j(b9, this.f265d);
        b.l(b9, this.f266e);
        b.m(b9, this.f267f);
        Bundle bundle = this.f268g;
        if (i8 < 23 && this.f269h != null) {
            if (bundle == null) {
                bundle = new Bundle();
                bundle.putBoolean("android.support.v4.media.description.NULL_BUNDLE_FLAG", true);
            }
            bundle.putParcelable("android.support.v4.media.description.MEDIA_URI", this.f269h);
        }
        b.k(b9, bundle);
        if (i8 >= 23) {
            c.b(b9, this.f269h);
        }
        MediaDescription a9 = b.a(b9);
        this.f270j = a9;
        return a9;
    }

    public String c() {
        return this.f262a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return ((Object) this.f263b) + ", " + ((Object) this.f264c) + ", " + ((Object) this.f265d);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        if (Build.VERSION.SDK_INT >= 21) {
            ((MediaDescription) b()).writeToParcel(parcel, i8);
            return;
        }
        parcel.writeString(this.f262a);
        TextUtils.writeToParcel(this.f263b, parcel, i8);
        TextUtils.writeToParcel(this.f264c, parcel, i8);
        TextUtils.writeToParcel(this.f265d, parcel, i8);
        parcel.writeParcelable(this.f266e, i8);
        parcel.writeParcelable(this.f267f, i8);
        parcel.writeBundle(this.f268g);
        parcel.writeParcelable(this.f269h, i8);
    }
}

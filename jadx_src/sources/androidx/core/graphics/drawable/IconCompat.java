package androidx.core.graphics.drawable;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.util.h;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class IconCompat extends CustomVersionedParcelable {

    /* renamed from: k  reason: collision with root package name */
    static final PorterDuff.Mode f4713k = PorterDuff.Mode.SRC_IN;

    /* renamed from: a  reason: collision with root package name */
    public int f4714a;

    /* renamed from: b  reason: collision with root package name */
    Object f4715b;

    /* renamed from: c  reason: collision with root package name */
    public byte[] f4716c;

    /* renamed from: d  reason: collision with root package name */
    public Parcelable f4717d;

    /* renamed from: e  reason: collision with root package name */
    public int f4718e;

    /* renamed from: f  reason: collision with root package name */
    public int f4719f;

    /* renamed from: g  reason: collision with root package name */
    public ColorStateList f4720g;

    /* renamed from: h  reason: collision with root package name */
    PorterDuff.Mode f4721h;

    /* renamed from: i  reason: collision with root package name */
    public String f4722i;

    /* renamed from: j  reason: collision with root package name */
    public String f4723j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static IconCompat a(Object obj) {
            h.h(obj);
            int d8 = d(obj);
            if (d8 != 2) {
                if (d8 != 4) {
                    if (d8 != 6) {
                        IconCompat iconCompat = new IconCompat(-1);
                        iconCompat.f4715b = obj;
                        return iconCompat;
                    }
                    return IconCompat.i(e(obj));
                }
                return IconCompat.l(e(obj));
            }
            return IconCompat.p(null, c(obj), b(obj));
        }

        static int b(Object obj) {
            if (Build.VERSION.SDK_INT >= 28) {
                return c.a(obj);
            }
            try {
                return ((Integer) obj.getClass().getMethod("getResId", new Class[0]).invoke(obj, new Object[0])).intValue();
            } catch (IllegalAccessException e8) {
                Log.e("IconCompat", "Unable to get icon resource", e8);
                return 0;
            } catch (NoSuchMethodException e9) {
                Log.e("IconCompat", "Unable to get icon resource", e9);
                return 0;
            } catch (InvocationTargetException e10) {
                Log.e("IconCompat", "Unable to get icon resource", e10);
                return 0;
            }
        }

        static String c(Object obj) {
            if (Build.VERSION.SDK_INT >= 28) {
                return c.b(obj);
            }
            try {
                return (String) obj.getClass().getMethod("getResPackage", new Class[0]).invoke(obj, new Object[0]);
            } catch (IllegalAccessException e8) {
                Log.e("IconCompat", "Unable to get icon package", e8);
                return null;
            } catch (NoSuchMethodException e9) {
                Log.e("IconCompat", "Unable to get icon package", e9);
                return null;
            } catch (InvocationTargetException e10) {
                Log.e("IconCompat", "Unable to get icon package", e10);
                return null;
            }
        }

        static int d(Object obj) {
            StringBuilder sb;
            if (Build.VERSION.SDK_INT >= 28) {
                return c.c(obj);
            }
            try {
                return ((Integer) obj.getClass().getMethod("getType", new Class[0]).invoke(obj, new Object[0])).intValue();
            } catch (IllegalAccessException e8) {
                e = e8;
                sb = new StringBuilder();
                sb.append("Unable to get icon type ");
                sb.append(obj);
                Log.e("IconCompat", sb.toString(), e);
                return -1;
            } catch (NoSuchMethodException e9) {
                e = e9;
                sb = new StringBuilder();
                sb.append("Unable to get icon type ");
                sb.append(obj);
                Log.e("IconCompat", sb.toString(), e);
                return -1;
            } catch (InvocationTargetException e10) {
                e = e10;
                sb = new StringBuilder();
                sb.append("Unable to get icon type ");
                sb.append(obj);
                Log.e("IconCompat", sb.toString(), e);
                return -1;
            }
        }

        static Uri e(Object obj) {
            if (Build.VERSION.SDK_INT >= 28) {
                return c.d(obj);
            }
            try {
                return (Uri) obj.getClass().getMethod("getUri", new Class[0]).invoke(obj, new Object[0]);
            } catch (IllegalAccessException e8) {
                Log.e("IconCompat", "Unable to get icon uri", e8);
                return null;
            } catch (NoSuchMethodException e9) {
                Log.e("IconCompat", "Unable to get icon uri", e9);
                return null;
            } catch (InvocationTargetException e10) {
                Log.e("IconCompat", "Unable to get icon uri", e10);
                return null;
            }
        }

        static Drawable f(Icon icon, Context context) {
            return icon.loadDrawable(context);
        }

        /* JADX WARN: Code restructure failed: missing block: B:13:0x002c, code lost:
            if (r0 >= 26) goto L23;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        static android.graphics.drawable.Icon g(androidx.core.graphics.drawable.IconCompat r4, android.content.Context r5) {
            /*
                int r0 = r4.f4714a
                r1 = 0
                r2 = 26
                switch(r0) {
                    case -1: goto Lb5;
                    case 0: goto L8;
                    case 1: goto L9c;
                    case 2: goto L91;
                    case 3: goto L84;
                    case 4: goto L7b;
                    case 5: goto L65;
                    case 6: goto L10;
                    default: goto L8;
                }
            L8:
                java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
                java.lang.String r5 = "Unknown type"
                r4.<init>(r5)
                throw r4
            L10:
                int r0 = android.os.Build.VERSION.SDK_INT
                r3 = 30
                if (r0 < r3) goto L20
                android.net.Uri r5 = r4.v()
                android.graphics.drawable.Icon r5 = androidx.core.graphics.drawable.IconCompat.d.a(r5)
                goto La4
            L20:
                if (r5 == 0) goto L4a
                java.io.InputStream r5 = r4.w(r5)
                if (r5 == 0) goto L2f
                android.graphics.Bitmap r5 = android.graphics.BitmapFactory.decodeStream(r5)
                if (r0 < r2) goto L76
                goto L6d
            L2f:
                java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "Cannot load adaptive icon from uri: "
                r0.append(r1)
                android.net.Uri r4 = r4.v()
                r0.append(r4)
                java.lang.String r4 = r0.toString()
                r5.<init>(r4)
                throw r5
            L4a:
                java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "Context is required to resolve the file uri of the icon: "
                r0.append(r1)
                android.net.Uri r4 = r4.v()
                r0.append(r4)
                java.lang.String r4 = r0.toString()
                r5.<init>(r4)
                throw r5
            L65:
                int r5 = android.os.Build.VERSION.SDK_INT
                if (r5 < r2) goto L72
                java.lang.Object r5 = r4.f4715b
                android.graphics.Bitmap r5 = (android.graphics.Bitmap) r5
            L6d:
                android.graphics.drawable.Icon r5 = androidx.core.graphics.drawable.IconCompat.b.b(r5)
                goto La4
            L72:
                java.lang.Object r5 = r4.f4715b
                android.graphics.Bitmap r5 = (android.graphics.Bitmap) r5
            L76:
                android.graphics.Bitmap r5 = androidx.core.graphics.drawable.IconCompat.h(r5, r1)
                goto La0
            L7b:
                java.lang.Object r5 = r4.f4715b
                java.lang.String r5 = (java.lang.String) r5
                android.graphics.drawable.Icon r5 = android.graphics.drawable.Icon.createWithContentUri(r5)
                goto La4
            L84:
                java.lang.Object r5 = r4.f4715b
                byte[] r5 = (byte[]) r5
                int r0 = r4.f4718e
                int r1 = r4.f4719f
                android.graphics.drawable.Icon r5 = android.graphics.drawable.Icon.createWithData(r5, r0, r1)
                goto La4
            L91:
                java.lang.String r5 = r4.s()
                int r0 = r4.f4718e
                android.graphics.drawable.Icon r5 = android.graphics.drawable.Icon.createWithResource(r5, r0)
                goto La4
            L9c:
                java.lang.Object r5 = r4.f4715b
                android.graphics.Bitmap r5 = (android.graphics.Bitmap) r5
            La0:
                android.graphics.drawable.Icon r5 = android.graphics.drawable.Icon.createWithBitmap(r5)
            La4:
                android.content.res.ColorStateList r0 = r4.f4720g
                if (r0 == 0) goto Lab
                r5.setTintList(r0)
            Lab:
                android.graphics.PorterDuff$Mode r4 = r4.f4721h
                android.graphics.PorterDuff$Mode r0 = androidx.core.graphics.drawable.IconCompat.f4713k
                if (r4 == r0) goto Lb4
                r5.setTintMode(r4)
            Lb4:
                return r5
            Lb5:
                java.lang.Object r4 = r4.f4715b
                android.graphics.drawable.Icon r4 = (android.graphics.drawable.Icon) r4
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.drawable.IconCompat.a.g(androidx.core.graphics.drawable.IconCompat, android.content.Context):android.graphics.drawable.Icon");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static Drawable a(Drawable drawable, Drawable drawable2) {
            return new AdaptiveIconDrawable(drawable, drawable2);
        }

        static Icon b(Bitmap bitmap) {
            return Icon.createWithAdaptiveBitmap(bitmap);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {
        static int a(Object obj) {
            return ((Icon) obj).getResId();
        }

        static String b(Object obj) {
            return ((Icon) obj).getResPackage();
        }

        static int c(Object obj) {
            return ((Icon) obj).getType();
        }

        static Uri d(Object obj) {
            return ((Icon) obj).getUri();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {
        static Icon a(Uri uri) {
            return Icon.createWithAdaptiveBitmapContentUri(uri);
        }
    }

    public IconCompat() {
        this.f4714a = -1;
        this.f4716c = null;
        this.f4717d = null;
        this.f4718e = 0;
        this.f4719f = 0;
        this.f4720g = null;
        this.f4721h = f4713k;
        this.f4722i = null;
    }

    IconCompat(int i8) {
        this.f4714a = -1;
        this.f4716c = null;
        this.f4717d = null;
        this.f4718e = 0;
        this.f4719f = 0;
        this.f4720g = null;
        this.f4721h = f4713k;
        this.f4722i = null;
        this.f4714a = i8;
    }

    private static String C(int i8) {
        switch (i8) {
            case 1:
                return "BITMAP";
            case 2:
                return "RESOURCE";
            case 3:
                return "DATA";
            case 4:
                return "URI";
            case 5:
                return "BITMAP_MASKABLE";
            case 6:
                return "URI_MASKABLE";
            default:
                return "UNKNOWN";
        }
    }

    public static IconCompat f(Bundle bundle) {
        Object parcelable;
        int i8 = bundle.getInt("type");
        IconCompat iconCompat = new IconCompat(i8);
        iconCompat.f4718e = bundle.getInt("int1");
        iconCompat.f4719f = bundle.getInt("int2");
        iconCompat.f4723j = bundle.getString("string1");
        if (bundle.containsKey("tint_list")) {
            iconCompat.f4720g = (ColorStateList) bundle.getParcelable("tint_list");
        }
        if (bundle.containsKey("tint_mode")) {
            iconCompat.f4721h = PorterDuff.Mode.valueOf(bundle.getString("tint_mode"));
        }
        switch (i8) {
            case -1:
            case 1:
            case 5:
                parcelable = bundle.getParcelable("obj");
                iconCompat.f4715b = parcelable;
                break;
            case 0:
            default:
                Log.w("IconCompat", "Unknown type " + i8);
                return null;
            case 2:
            case 4:
            case 6:
                parcelable = bundle.getString("obj");
                iconCompat.f4715b = parcelable;
                break;
            case 3:
                iconCompat.f4715b = bundle.getByteArray("obj");
                break;
        }
        return iconCompat;
    }

    public static IconCompat g(Icon icon) {
        return a.a(icon);
    }

    static Bitmap h(Bitmap bitmap, boolean z4) {
        int min = (int) (Math.min(bitmap.getWidth(), bitmap.getHeight()) * 0.6666667f);
        Bitmap createBitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(3);
        float f5 = min;
        float f8 = 0.5f * f5;
        float f9 = 0.9166667f * f8;
        if (z4) {
            float f10 = 0.010416667f * f5;
            paint.setColor(0);
            paint.setShadowLayer(f10, 0.0f, f5 * 0.020833334f, 1023410176);
            canvas.drawCircle(f8, f8, f9, paint);
            paint.setShadowLayer(f10, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(f8, f8, f9, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(-16777216);
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        Matrix matrix = new Matrix();
        matrix.setTranslate((-(bitmap.getWidth() - min)) / 2.0f, (-(bitmap.getHeight() - min)) / 2.0f);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(f8, f8, f9, paint);
        canvas.setBitmap(null);
        return createBitmap;
    }

    public static IconCompat i(Uri uri) {
        androidx.core.util.c.d(uri);
        return j(uri.toString());
    }

    public static IconCompat j(String str) {
        androidx.core.util.c.d(str);
        IconCompat iconCompat = new IconCompat(6);
        iconCompat.f4715b = str;
        return iconCompat;
    }

    public static IconCompat k(Bitmap bitmap) {
        androidx.core.util.c.d(bitmap);
        IconCompat iconCompat = new IconCompat(1);
        iconCompat.f4715b = bitmap;
        return iconCompat;
    }

    public static IconCompat l(Uri uri) {
        androidx.core.util.c.d(uri);
        return m(uri.toString());
    }

    public static IconCompat m(String str) {
        androidx.core.util.c.d(str);
        IconCompat iconCompat = new IconCompat(4);
        iconCompat.f4715b = str;
        return iconCompat;
    }

    public static IconCompat n(byte[] bArr, int i8, int i9) {
        androidx.core.util.c.d(bArr);
        IconCompat iconCompat = new IconCompat(3);
        iconCompat.f4715b = bArr;
        iconCompat.f4718e = i8;
        iconCompat.f4719f = i9;
        return iconCompat;
    }

    public static IconCompat o(Context context, int i8) {
        androidx.core.util.c.d(context);
        return p(context.getResources(), context.getPackageName(), i8);
    }

    public static IconCompat p(Resources resources, String str, int i8) {
        androidx.core.util.c.d(str);
        if (i8 != 0) {
            IconCompat iconCompat = new IconCompat(2);
            iconCompat.f4718e = i8;
            if (resources != null) {
                try {
                    iconCompat.f4715b = resources.getResourceName(i8);
                } catch (Resources.NotFoundException unused) {
                    throw new IllegalArgumentException("Icon resource cannot be found");
                }
            } else {
                iconCompat.f4715b = str;
            }
            iconCompat.f4723j = str;
            return iconCompat;
        }
        throw new IllegalArgumentException("Drawable resource ID must not be 0");
    }

    static Resources t(Context context, String str) {
        if ("android".equals(str)) {
            return Resources.getSystem();
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 8192);
            if (applicationInfo != null) {
                return packageManager.getResourcesForApplication(applicationInfo);
            }
            return null;
        } catch (PackageManager.NameNotFoundException e8) {
            Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", str), e8);
            return null;
        }
    }

    private Drawable y(Context context) {
        switch (this.f4714a) {
            case 1:
                return new BitmapDrawable(context.getResources(), (Bitmap) this.f4715b);
            case 2:
                String s8 = s();
                if (TextUtils.isEmpty(s8)) {
                    s8 = context.getPackageName();
                }
                try {
                    return androidx.core.content.res.h.e(t(context, s8), this.f4718e, context.getTheme());
                } catch (RuntimeException e8) {
                    Log.e("IconCompat", String.format("Unable to load resource 0x%08x from pkg=%s", Integer.valueOf(this.f4718e), this.f4715b), e8);
                    break;
                }
            case 3:
                return new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray((byte[]) this.f4715b, this.f4718e, this.f4719f));
            case 4:
                InputStream w8 = w(context);
                if (w8 != null) {
                    return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(w8));
                }
                break;
            case 5:
                return new BitmapDrawable(context.getResources(), h((Bitmap) this.f4715b, false));
            case 6:
                InputStream w9 = w(context);
                if (w9 != null) {
                    return Build.VERSION.SDK_INT >= 26 ? b.a(null, new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(w9))) : new BitmapDrawable(context.getResources(), h(BitmapFactory.decodeStream(w9), false));
                }
                break;
        }
        return null;
    }

    @Deprecated
    public Icon A() {
        return B(null);
    }

    public Icon B(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return a.g(this, context);
        }
        throw new UnsupportedOperationException("This method is only supported on API level 23+");
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void c() {
        Parcelable parcelable;
        this.f4721h = PorterDuff.Mode.valueOf(this.f4722i);
        switch (this.f4714a) {
            case -1:
                parcelable = this.f4717d;
                if (parcelable == null) {
                    throw new IllegalArgumentException("Invalid icon");
                }
                break;
            case 0:
            default:
                return;
            case 1:
            case 5:
                parcelable = this.f4717d;
                if (parcelable == null) {
                    byte[] bArr = this.f4716c;
                    this.f4715b = bArr;
                    this.f4714a = 3;
                    this.f4718e = 0;
                    this.f4719f = bArr.length;
                    return;
                }
                break;
            case 2:
            case 4:
            case 6:
                String str = new String(this.f4716c, Charset.forName("UTF-16"));
                this.f4715b = str;
                if (this.f4714a == 2 && this.f4723j == null) {
                    this.f4723j = str.split(":", -1)[0];
                    return;
                }
                return;
            case 3:
                this.f4715b = this.f4716c;
                return;
        }
        this.f4715b = parcelable;
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void d(boolean z4) {
        this.f4722i = this.f4721h.name();
        switch (this.f4714a) {
            case -1:
                if (z4) {
                    throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
                }
                break;
            case 0:
            default:
                return;
            case 1:
            case 5:
                if (z4) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ((Bitmap) this.f4715b).compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
                    this.f4716c = byteArrayOutputStream.toByteArray();
                    return;
                }
                break;
            case 2:
                this.f4716c = ((String) this.f4715b).getBytes(Charset.forName("UTF-16"));
                return;
            case 3:
                this.f4716c = (byte[]) this.f4715b;
                return;
            case 4:
            case 6:
                this.f4716c = this.f4715b.toString().getBytes(Charset.forName("UTF-16"));
                return;
        }
        this.f4717d = (Parcelable) this.f4715b;
    }

    public void e(Context context) {
        Object obj;
        if (this.f4714a != 2 || (obj = this.f4715b) == null) {
            return;
        }
        String str = (String) obj;
        if (str.contains(":")) {
            String str2 = str.split(":", -1)[1];
            String str3 = str2.split("/", -1)[0];
            String str4 = str2.split("/", -1)[1];
            String str5 = str.split(":", -1)[0];
            if ("0_resource_name_obfuscated".equals(str4)) {
                Log.i("IconCompat", "Found obfuscated resource, not trying to update resource id for it");
                return;
            }
            String s8 = s();
            int identifier = t(context, s8).getIdentifier(str4, str3, str5);
            if (this.f4718e != identifier) {
                Log.i("IconCompat", "Id has changed for " + s8 + " " + str);
                this.f4718e = identifier;
            }
        }
    }

    public Bitmap q() {
        int i8 = this.f4714a;
        if (i8 == -1 && Build.VERSION.SDK_INT >= 23) {
            Object obj = this.f4715b;
            if (obj instanceof Bitmap) {
                return (Bitmap) obj;
            }
            return null;
        } else if (i8 == 1) {
            return (Bitmap) this.f4715b;
        } else {
            if (i8 == 5) {
                return h((Bitmap) this.f4715b, true);
            }
            throw new IllegalStateException("called getBitmap() on " + this);
        }
    }

    public int r() {
        int i8 = this.f4714a;
        if (i8 != -1 || Build.VERSION.SDK_INT < 23) {
            if (i8 == 2) {
                return this.f4718e;
            }
            throw new IllegalStateException("called getResId() on " + this);
        }
        return a.b(this.f4715b);
    }

    public String s() {
        int i8 = this.f4714a;
        if (i8 != -1 || Build.VERSION.SDK_INT < 23) {
            if (i8 == 2) {
                String str = this.f4723j;
                return (str == null || TextUtils.isEmpty(str)) ? ((String) this.f4715b).split(":", -1)[0] : this.f4723j;
            }
            throw new IllegalStateException("called getResPackage() on " + this);
        }
        return a.c(this.f4715b);
    }

    public String toString() {
        int height;
        if (this.f4714a == -1) {
            return String.valueOf(this.f4715b);
        }
        StringBuilder sb = new StringBuilder("Icon(typ=");
        sb.append(C(this.f4714a));
        switch (this.f4714a) {
            case 1:
            case 5:
                sb.append(" size=");
                sb.append(((Bitmap) this.f4715b).getWidth());
                sb.append("x");
                height = ((Bitmap) this.f4715b).getHeight();
                sb.append(height);
                break;
            case 2:
                sb.append(" pkg=");
                sb.append(this.f4723j);
                sb.append(" id=");
                sb.append(String.format("0x%08x", Integer.valueOf(r())));
                break;
            case 3:
                sb.append(" len=");
                sb.append(this.f4718e);
                if (this.f4719f != 0) {
                    sb.append(" off=");
                    height = this.f4719f;
                    sb.append(height);
                    break;
                }
                break;
            case 4:
            case 6:
                sb.append(" uri=");
                sb.append(this.f4715b);
                break;
        }
        if (this.f4720g != null) {
            sb.append(" tint=");
            sb.append(this.f4720g);
        }
        if (this.f4721h != f4713k) {
            sb.append(" mode=");
            sb.append(this.f4721h);
        }
        sb.append(")");
        return sb.toString();
    }

    public int u() {
        int i8 = this.f4714a;
        return (i8 != -1 || Build.VERSION.SDK_INT < 23) ? i8 : a.d(this.f4715b);
    }

    public Uri v() {
        int i8 = this.f4714a;
        if (i8 != -1 || Build.VERSION.SDK_INT < 23) {
            if (i8 == 4 || i8 == 6) {
                return Uri.parse((String) this.f4715b);
            }
            throw new IllegalStateException("called getUri() on " + this);
        }
        return a.e(this.f4715b);
    }

    public InputStream w(Context context) {
        StringBuilder sb;
        String str;
        Uri v8 = v();
        String scheme = v8.getScheme();
        if ("content".equals(scheme) || "file".equals(scheme)) {
            try {
                return context.getContentResolver().openInputStream(v8);
            } catch (Exception e8) {
                e = e8;
                sb = new StringBuilder();
                str = "Unable to load image from URI: ";
            }
        } else {
            try {
                return new FileInputStream(new File((String) this.f4715b));
            } catch (FileNotFoundException e9) {
                e = e9;
                sb = new StringBuilder();
                str = "Unable to load image from path: ";
            }
        }
        sb.append(str);
        sb.append(v8);
        Log.w("IconCompat", sb.toString(), e);
        return null;
    }

    public Drawable x(Context context) {
        e(context);
        if (Build.VERSION.SDK_INT >= 23) {
            return a.f(B(context), context);
        }
        Drawable y8 = y(context);
        if (y8 != null && (this.f4720g != null || this.f4721h != f4713k)) {
            y8.mutate();
            androidx.core.graphics.drawable.a.o(y8, this.f4720g);
            androidx.core.graphics.drawable.a.p(y8, this.f4721h);
        }
        return y8;
    }

    public Bundle z() {
        Parcelable parcelable;
        Bundle bundle = new Bundle();
        switch (this.f4714a) {
            case -1:
                parcelable = (Parcelable) this.f4715b;
                bundle.putParcelable("obj", parcelable);
                break;
            case 0:
            default:
                throw new IllegalArgumentException("Invalid icon");
            case 1:
            case 5:
                parcelable = (Bitmap) this.f4715b;
                bundle.putParcelable("obj", parcelable);
                break;
            case 2:
            case 4:
            case 6:
                bundle.putString("obj", (String) this.f4715b);
                break;
            case 3:
                bundle.putByteArray("obj", (byte[]) this.f4715b);
                break;
        }
        bundle.putInt("type", this.f4714a);
        bundle.putInt("int1", this.f4718e);
        bundle.putInt("int2", this.f4719f);
        bundle.putString("string1", this.f4723j);
        ColorStateList colorStateList = this.f4720g;
        if (colorStateList != null) {
            bundle.putParcelable("tint_list", colorStateList);
        }
        PorterDuff.Mode mode = this.f4721h;
        if (mode != f4713k) {
            bundle.putString("tint_mode", mode.name());
        }
        return bundle;
    }
}

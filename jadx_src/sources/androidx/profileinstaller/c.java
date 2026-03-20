package androidx.profileinstaller;

import android.content.res.AssetManager;
import android.os.Build;
import androidx.profileinstaller.i;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: a  reason: collision with root package name */
    private final AssetManager f6468a;

    /* renamed from: b  reason: collision with root package name */
    private final Executor f6469b;

    /* renamed from: c  reason: collision with root package name */
    private final i.c f6470c;

    /* renamed from: e  reason: collision with root package name */
    private final File f6472e;

    /* renamed from: f  reason: collision with root package name */
    private final String f6473f;

    /* renamed from: g  reason: collision with root package name */
    private final String f6474g;

    /* renamed from: h  reason: collision with root package name */
    private final String f6475h;

    /* renamed from: j  reason: collision with root package name */
    private d[] f6477j;

    /* renamed from: k  reason: collision with root package name */
    private byte[] f6478k;

    /* renamed from: i  reason: collision with root package name */
    private boolean f6476i = false;

    /* renamed from: d  reason: collision with root package name */
    private final byte[] f6471d = d();

    public c(AssetManager assetManager, Executor executor, i.c cVar, String str, String str2, String str3, File file) {
        this.f6468a = assetManager;
        this.f6469b = executor;
        this.f6470c = cVar;
        this.f6473f = str;
        this.f6474g = str2;
        this.f6475h = str3;
        this.f6472e = file;
    }

    private c b(d[] dVarArr, byte[] bArr) {
        i.c cVar;
        int i8;
        InputStream h8;
        try {
            h8 = h(this.f6468a, this.f6475h);
        } catch (FileNotFoundException e8) {
            e = e8;
            cVar = this.f6470c;
            i8 = 9;
            cVar.b(i8, e);
            return null;
        } catch (IOException e9) {
            e = e9;
            cVar = this.f6470c;
            i8 = 7;
            cVar.b(i8, e);
            return null;
        } catch (IllegalStateException e10) {
            e = e10;
            this.f6477j = null;
            cVar = this.f6470c;
            i8 = 8;
            cVar.b(i8, e);
            return null;
        }
        if (h8 != null) {
            this.f6477j = n.q(h8, n.o(h8, n.f6507b), bArr, dVarArr);
            h8.close();
            return this;
        }
        if (h8 != null) {
            h8.close();
        }
        return null;
    }

    private void c() {
        if (!this.f6476i) {
            throw new IllegalStateException("This device doesn't support aot. Did you call deviceSupportsAotProfile()?");
        }
    }

    private static byte[] d() {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 24 || i8 > 33) {
            return null;
        }
        switch (i8) {
            case 24:
            case 25:
                return p.f6522e;
            case 26:
                return p.f6521d;
            case 27:
                return p.f6520c;
            case 28:
            case 29:
            case 30:
                return p.f6519b;
            case 31:
            case 32:
            case 33:
                return p.f6518a;
            default:
                return null;
        }
    }

    private InputStream f(AssetManager assetManager) {
        i.c cVar;
        int i8;
        try {
            return h(assetManager, this.f6474g);
        } catch (FileNotFoundException e8) {
            e = e8;
            cVar = this.f6470c;
            i8 = 6;
            cVar.b(i8, e);
            return null;
        } catch (IOException e9) {
            e = e9;
            cVar = this.f6470c;
            i8 = 7;
            cVar.b(i8, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g(int i8, Object obj) {
        this.f6470c.b(i8, obj);
    }

    private InputStream h(AssetManager assetManager, String str) {
        try {
            return assetManager.openFd(str).createInputStream();
        } catch (FileNotFoundException e8) {
            String message = e8.getMessage();
            if (message == null || !message.contains("compressed")) {
                return null;
            }
            this.f6470c.a(5, null);
            return null;
        }
    }

    private d[] j(InputStream inputStream) {
        try {
        } catch (IOException e8) {
            this.f6470c.b(7, e8);
        }
        try {
            try {
                d[] w8 = n.w(inputStream, n.o(inputStream, n.f6506a), this.f6473f);
                try {
                    inputStream.close();
                    return w8;
                } catch (IOException e9) {
                    this.f6470c.b(7, e9);
                    return w8;
                }
            } catch (Throwable th) {
                try {
                    inputStream.close();
                } catch (IOException e10) {
                    this.f6470c.b(7, e10);
                }
                throw th;
            }
        } catch (IOException e11) {
            this.f6470c.b(7, e11);
            inputStream.close();
            return null;
        } catch (IllegalStateException e12) {
            this.f6470c.b(8, e12);
            inputStream.close();
            return null;
        }
    }

    private static boolean k() {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 24 || i8 > 33) {
            return false;
        }
        if (i8 != 24 && i8 != 25) {
            switch (i8) {
                case 31:
                case 32:
                case 33:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private void l(final int i8, final Object obj) {
        this.f6469b.execute(new Runnable() { // from class: androidx.profileinstaller.b
            @Override // java.lang.Runnable
            public final void run() {
                c.this.g(i8, obj);
            }
        });
    }

    public boolean e() {
        int i8;
        Integer num;
        if (this.f6471d == null) {
            i8 = 3;
            num = Integer.valueOf(Build.VERSION.SDK_INT);
        } else if (this.f6472e.canWrite()) {
            this.f6476i = true;
            return true;
        } else {
            i8 = 4;
            num = null;
        }
        l(i8, num);
        return false;
    }

    public c i() {
        c b9;
        c();
        if (this.f6471d == null) {
            return this;
        }
        InputStream f5 = f(this.f6468a);
        if (f5 != null) {
            this.f6477j = j(f5);
        }
        d[] dVarArr = this.f6477j;
        return (dVarArr == null || !k() || (b9 = b(dVarArr, this.f6471d)) == null) ? this : b9;
    }

    public c m() {
        i.c cVar;
        int i8;
        ByteArrayOutputStream byteArrayOutputStream;
        d[] dVarArr = this.f6477j;
        byte[] bArr = this.f6471d;
        if (dVarArr != null && bArr != null) {
            c();
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    n.E(byteArrayOutputStream, bArr);
                } catch (Throwable th) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException e8) {
                e = e8;
                cVar = this.f6470c;
                i8 = 7;
                cVar.b(i8, e);
                this.f6477j = null;
                return this;
            } catch (IllegalStateException e9) {
                e = e9;
                cVar = this.f6470c;
                i8 = 8;
                cVar.b(i8, e);
                this.f6477j = null;
                return this;
            }
            if (!n.B(byteArrayOutputStream, bArr, dVarArr)) {
                this.f6470c.b(5, null);
                this.f6477j = null;
                byteArrayOutputStream.close();
                return this;
            }
            this.f6478k = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            this.f6477j = null;
        }
        return this;
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [androidx.profileinstaller.d[], byte[]] */
    public boolean n() {
        byte[] bArr = this.f6478k;
        if (bArr == null) {
            return false;
        }
        c();
        try {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(this.f6472e);
                    e.l(byteArrayInputStream, fileOutputStream);
                    l(1, null);
                    fileOutputStream.close();
                    byteArrayInputStream.close();
                    return true;
                } catch (Throwable th) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (FileNotFoundException e8) {
                l(6, e8);
                return false;
            } catch (IOException e9) {
                l(7, e9);
                return false;
            }
        } finally {
            this.f6478k = null;
            this.f6477j = null;
        }
    }
}

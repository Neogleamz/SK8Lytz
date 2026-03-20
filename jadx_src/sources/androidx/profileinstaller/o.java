package androidx.profileinstaller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o {

    /* renamed from: a  reason: collision with root package name */
    private static final androidx.concurrent.futures.d<c> f6508a = androidx.concurrent.futures.d.C();

    /* renamed from: b  reason: collision with root package name */
    private static final Object f6509b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private static c f6510c = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static PackageInfo a(PackageManager packageManager, Context context) {
            return packageManager.getPackageInfo(context.getPackageName(), PackageManager.PackageInfoFlags.of(0L));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {

        /* renamed from: a  reason: collision with root package name */
        final int f6511a;

        /* renamed from: b  reason: collision with root package name */
        final int f6512b;

        /* renamed from: c  reason: collision with root package name */
        final long f6513c;

        /* renamed from: d  reason: collision with root package name */
        final long f6514d;

        b(int i8, int i9, long j8, long j9) {
            this.f6511a = i8;
            this.f6512b = i9;
            this.f6513c = j8;
            this.f6514d = j9;
        }

        static b a(File file) {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
            try {
                b bVar = new b(dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readLong(), dataInputStream.readLong());
                dataInputStream.close();
                return bVar;
            } catch (Throwable th) {
                try {
                    dataInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        void b(File file) {
            file.delete();
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
            try {
                dataOutputStream.writeInt(this.f6511a);
                dataOutputStream.writeInt(this.f6512b);
                dataOutputStream.writeLong(this.f6513c);
                dataOutputStream.writeLong(this.f6514d);
                dataOutputStream.close();
            } catch (Throwable th) {
                try {
                    dataOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof b)) {
                return false;
            }
            b bVar = (b) obj;
            return this.f6512b == bVar.f6512b && this.f6513c == bVar.f6513c && this.f6511a == bVar.f6511a && this.f6514d == bVar.f6514d;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.f6512b), Long.valueOf(this.f6513c), Integer.valueOf(this.f6511a), Long.valueOf(this.f6514d));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        final int f6515a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f6516b;

        /* renamed from: c  reason: collision with root package name */
        private final boolean f6517c;

        c(int i8, boolean z4, boolean z8) {
            this.f6515a = i8;
            this.f6517c = z8;
            this.f6516b = z4;
        }
    }

    private static long a(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        return (Build.VERSION.SDK_INT >= 33 ? a.a(packageManager, context) : packageManager.getPackageInfo(context.getPackageName(), 0)).lastUpdateTime;
    }

    private static c b(int i8, boolean z4, boolean z8) {
        c cVar = new c(i8, z4, z8);
        f6510c = cVar;
        f6508a.y(cVar);
        return f6510c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't wrap try/catch for region: R(20:14|(1:77)(1:18)|19|(1:76)(1:23)|24|25|26|(2:62|63)|28|(8:35|(1:39)|(1:46)|47|(2:54|55)|51|52|53)|(1:61)|(1:39)|(3:41|44|46)|47|(1:49)|54|55|51|52|53) */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x009d, code lost:
        r3 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00cc, code lost:
        r3 = 196608;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static androidx.profileinstaller.o.c c(android.content.Context r18, boolean r19) {
        /*
            Method dump skipped, instructions count: 231
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.profileinstaller.o.c(android.content.Context, boolean):androidx.profileinstaller.o$c");
    }
}

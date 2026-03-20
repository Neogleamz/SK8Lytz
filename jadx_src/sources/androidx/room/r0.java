package androidx.room;

import android.content.Context;
import android.util.Log;
import androidx.room.RoomDatabase;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class r0 implements t1.c, n {

    /* renamed from: a  reason: collision with root package name */
    private final Context f7182a;

    /* renamed from: b  reason: collision with root package name */
    private final String f7183b;

    /* renamed from: c  reason: collision with root package name */
    private final File f7184c;

    /* renamed from: d  reason: collision with root package name */
    private final Callable<InputStream> f7185d;

    /* renamed from: e  reason: collision with root package name */
    private final int f7186e;

    /* renamed from: f  reason: collision with root package name */
    private final t1.c f7187f;

    /* renamed from: g  reason: collision with root package name */
    private m f7188g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f7189h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r0(Context context, String str, File file, Callable<InputStream> callable, int i8, t1.c cVar) {
        this.f7182a = context;
        this.f7183b = str;
        this.f7184c = file;
        this.f7185d = callable;
        this.f7186e = i8;
        this.f7187f = cVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0084 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0085  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void b(java.io.File r5, boolean r6) {
        /*
            r4 = this;
            java.lang.String r0 = r4.f7183b
            if (r0 == 0) goto L15
            android.content.Context r0 = r4.f7182a
            android.content.res.AssetManager r0 = r0.getAssets()
            java.lang.String r1 = r4.f7183b
            java.io.InputStream r0 = r0.open(r1)
        L10:
            java.nio.channels.ReadableByteChannel r0 = java.nio.channels.Channels.newChannel(r0)
            goto L30
        L15:
            java.io.File r0 = r4.f7184c
            if (r0 == 0) goto L25
            java.io.FileInputStream r0 = new java.io.FileInputStream
            java.io.File r1 = r4.f7184c
            r0.<init>(r1)
            java.nio.channels.FileChannel r0 = r0.getChannel()
            goto L30
        L25:
            java.util.concurrent.Callable<java.io.InputStream> r0 = r4.f7185d
            if (r0 == 0) goto Lba
            java.lang.Object r0 = r0.call()     // Catch: java.lang.Exception -> Lb1
            java.io.InputStream r0 = (java.io.InputStream) r0     // Catch: java.lang.Exception -> Lb1
            goto L10
        L30:
            android.content.Context r1 = r4.f7182a
            java.io.File r1 = r1.getCacheDir()
            java.lang.String r2 = "room-copy-helper"
            java.lang.String r3 = ".tmp"
            java.io.File r1 = java.io.File.createTempFile(r2, r3, r1)
            r1.deleteOnExit()
            java.io.FileOutputStream r2 = new java.io.FileOutputStream
            r2.<init>(r1)
            java.nio.channels.FileChannel r2 = r2.getChannel()
            r1.d.a(r0, r2)
            java.io.File r0 = r5.getParentFile()
            if (r0 == 0) goto L7b
            boolean r2 = r0.exists()
            if (r2 != 0) goto L7b
            boolean r0 = r0.mkdirs()
            if (r0 == 0) goto L60
            goto L7b
        L60:
            java.io.IOException r6 = new java.io.IOException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Failed to create directories for "
            r0.append(r1)
            java.lang.String r5 = r5.getAbsolutePath()
            r0.append(r5)
            java.lang.String r5 = r0.toString()
            r6.<init>(r5)
            throw r6
        L7b:
            r4.c(r1, r6)
            boolean r6 = r1.renameTo(r5)
            if (r6 == 0) goto L85
            return
        L85:
            java.io.IOException r6 = new java.io.IOException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "Failed to move intermediate file ("
            r0.append(r2)
            java.lang.String r1 = r1.getAbsolutePath()
            r0.append(r1)
            java.lang.String r1 = ") to destination ("
            r0.append(r1)
            java.lang.String r5 = r5.getAbsolutePath()
            r0.append(r5)
            java.lang.String r5 = ")."
            r0.append(r5)
            java.lang.String r5 = r0.toString()
            r6.<init>(r5)
            throw r6
        Lb1:
            r5 = move-exception
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r0 = "inputStreamCallable exception on call"
            r6.<init>(r0, r5)
            throw r6
        Lba:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "copyFromAssetPath, copyFromFile and copyFromInputStream are all null!"
            r5.<init>(r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.room.r0.b(java.io.File, boolean):void");
    }

    private void c(File file, boolean z4) {
        m mVar = this.f7188g;
        if (mVar != null) {
            RoomDatabase.d dVar = mVar.f7149f;
        }
    }

    private void f(boolean z4) {
        String databaseName = getDatabaseName();
        File databasePath = this.f7182a.getDatabasePath(databaseName);
        m mVar = this.f7188g;
        r1.a aVar = new r1.a(databaseName, this.f7182a.getFilesDir(), mVar == null || mVar.f7155l);
        try {
            aVar.b();
            if (!databasePath.exists()) {
                try {
                    b(databasePath, z4);
                    aVar.c();
                    return;
                } catch (IOException e8) {
                    throw new RuntimeException("Unable to copy database file.", e8);
                }
            } else if (this.f7188g == null) {
                aVar.c();
                return;
            } else {
                try {
                    int c9 = r1.c.c(databasePath);
                    int i8 = this.f7186e;
                    if (c9 == i8) {
                        aVar.c();
                        return;
                    } else if (this.f7188g.a(c9, i8)) {
                        aVar.c();
                        return;
                    } else {
                        if (this.f7182a.deleteDatabase(databaseName)) {
                            try {
                                b(databasePath, z4);
                            } catch (IOException e9) {
                                Log.w("ROOM", "Unable to copy database file.", e9);
                            }
                        } else {
                            Log.w("ROOM", "Failed to delete database file (" + databaseName + ") for a copy destructive migration.");
                        }
                        aVar.c();
                        return;
                    }
                } catch (IOException e10) {
                    Log.w("ROOM", "Unable to read database version.", e10);
                    aVar.c();
                    return;
                }
            }
        } catch (Throwable th) {
            aVar.c();
            throw th;
        }
        aVar.c();
        throw th;
    }

    @Override // androidx.room.n
    public t1.c a() {
        return this.f7187f;
    }

    @Override // t1.c, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        this.f7187f.close();
        this.f7189h = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(m mVar) {
        this.f7188g = mVar;
    }

    @Override // t1.c
    public String getDatabaseName() {
        return this.f7187f.getDatabaseName();
    }

    @Override // t1.c
    public void setWriteAheadLoggingEnabled(boolean z4) {
        this.f7187f.setWriteAheadLoggingEnabled(z4);
    }

    @Override // t1.c
    public synchronized t1.b v0() {
        if (!this.f7189h) {
            f(true);
            this.f7189h = true;
        }
        return this.f7187f.v0();
    }
}

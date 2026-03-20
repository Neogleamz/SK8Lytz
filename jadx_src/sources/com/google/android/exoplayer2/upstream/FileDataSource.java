package com.google.android.exoplayer2.upstream;

import a6.e;
import android.net.Uri;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.text.TextUtils;
import b6.l0;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class FileDataSource extends e {

    /* renamed from: e  reason: collision with root package name */
    private RandomAccessFile f10895e;

    /* renamed from: f  reason: collision with root package name */
    private Uri f10896f;

    /* renamed from: g  reason: collision with root package name */
    private long f10897g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f10898h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class FileDataSourceException extends DataSourceException {
        public FileDataSourceException(String str, Throwable th, int i8) {
            super(str, th, i8);
        }

        public FileDataSourceException(Throwable th, int i8) {
            super(th, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        /* JADX INFO: Access modifiers changed from: private */
        public static boolean b(Throwable th) {
            return (th instanceof ErrnoException) && ((ErrnoException) th).errno == OsConstants.EACCES;
        }
    }

    public FileDataSource() {
        super(false);
    }

    private static RandomAccessFile p(Uri uri) {
        int i8 = 2006;
        try {
            return new RandomAccessFile((String) b6.a.e(uri.getPath()), "r");
        } catch (FileNotFoundException e8) {
            if (TextUtils.isEmpty(uri.getQuery()) && TextUtils.isEmpty(uri.getFragment())) {
                throw new FileDataSourceException(e8, (l0.f8063a < 21 || !a.b(e8.getCause())) ? 2005 : 2005);
            }
            throw new FileDataSourceException(String.format("uri has query and/or fragment, which are not supported. Did you call Uri.parse() on a string containing '?' or '#'? Use Uri.fromFile(new File(path)) to avoid this. path=%s,query=%s,fragment=%s", uri.getPath(), uri.getQuery(), uri.getFragment()), e8, 1004);
        } catch (SecurityException e9) {
            throw new FileDataSourceException(e9, 2006);
        } catch (RuntimeException e10) {
            throw new FileDataSourceException(e10, 2000);
        }
    }

    @Override // a6.h
    public void close() {
        this.f10896f = null;
        try {
            try {
                RandomAccessFile randomAccessFile = this.f10895e;
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e8) {
                throw new FileDataSourceException(e8, 2000);
            }
        } finally {
            this.f10895e = null;
            if (this.f10898h) {
                this.f10898h = false;
                m();
            }
        }
    }

    @Override // a6.f
    public int read(byte[] bArr, int i8, int i9) {
        if (i9 == 0) {
            return 0;
        }
        if (this.f10897g == 0) {
            return -1;
        }
        try {
            int read = ((RandomAccessFile) l0.j(this.f10895e)).read(bArr, i8, (int) Math.min(this.f10897g, i9));
            if (read > 0) {
                this.f10897g -= read;
                l(read);
            }
            return read;
        } catch (IOException e8) {
            throw new FileDataSourceException(e8, 2000);
        }
    }

    @Override // a6.h
    public Uri v() {
        return this.f10896f;
    }

    @Override // a6.h
    public long x(com.google.android.exoplayer2.upstream.a aVar) {
        Uri uri = aVar.f10942a;
        this.f10896f = uri;
        n(aVar);
        RandomAccessFile p8 = p(uri);
        this.f10895e = p8;
        try {
            p8.seek(aVar.f10948g);
            long j8 = aVar.f10949h;
            if (j8 == -1) {
                j8 = this.f10895e.length() - aVar.f10948g;
            }
            this.f10897g = j8;
            if (j8 >= 0) {
                this.f10898h = true;
                o(aVar);
                return this.f10897g;
            }
            throw new FileDataSourceException(null, null, 2008);
        } catch (IOException e8) {
            throw new FileDataSourceException(e8, 2000);
        }
    }
}

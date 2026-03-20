package com.google.android.exoplayer2.upstream;

import a6.e;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import b6.l0;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class AssetDataSource extends e {

    /* renamed from: e  reason: collision with root package name */
    private final AssetManager f10883e;

    /* renamed from: f  reason: collision with root package name */
    private Uri f10884f;

    /* renamed from: g  reason: collision with root package name */
    private InputStream f10885g;

    /* renamed from: h  reason: collision with root package name */
    private long f10886h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f10887i;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class AssetDataSourceException extends DataSourceException {
        public AssetDataSourceException(Throwable th, int i8) {
            super(th, i8);
        }
    }

    public AssetDataSource(Context context) {
        super(false);
        this.f10883e = context.getAssets();
    }

    @Override // a6.h
    public void close() {
        this.f10884f = null;
        try {
            try {
                InputStream inputStream = this.f10885g;
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e8) {
                throw new AssetDataSourceException(e8, 2000);
            }
        } finally {
            this.f10885g = null;
            if (this.f10887i) {
                this.f10887i = false;
                m();
            }
        }
    }

    @Override // a6.f
    public int read(byte[] bArr, int i8, int i9) {
        if (i9 == 0) {
            return 0;
        }
        long j8 = this.f10886h;
        if (j8 == 0) {
            return -1;
        }
        if (j8 != -1) {
            try {
                i9 = (int) Math.min(j8, i9);
            } catch (IOException e8) {
                throw new AssetDataSourceException(e8, 2000);
            }
        }
        int read = ((InputStream) l0.j(this.f10885g)).read(bArr, i8, i9);
        if (read == -1) {
            return -1;
        }
        long j9 = this.f10886h;
        if (j9 != -1) {
            this.f10886h = j9 - read;
        }
        l(read);
        return read;
    }

    @Override // a6.h
    public Uri v() {
        return this.f10884f;
    }

    @Override // a6.h
    public long x(a aVar) {
        try {
            Uri uri = aVar.f10942a;
            this.f10884f = uri;
            String str = (String) b6.a.e(uri.getPath());
            if (str.startsWith("/android_asset/")) {
                str = str.substring(15);
            } else if (str.startsWith("/")) {
                str = str.substring(1);
            }
            n(aVar);
            InputStream open = this.f10883e.open(str, 1);
            this.f10885g = open;
            if (open.skip(aVar.f10948g) >= aVar.f10948g) {
                long j8 = aVar.f10949h;
                if (j8 != -1) {
                    this.f10886h = j8;
                } else {
                    long available = this.f10885g.available();
                    this.f10886h = available;
                    if (available == 2147483647L) {
                        this.f10886h = -1L;
                    }
                }
                this.f10887i = true;
                o(aVar);
                return this.f10886h;
            }
            throw new AssetDataSourceException(null, 2008);
        } catch (AssetDataSourceException e8) {
            throw e8;
        } catch (IOException e9) {
            throw new AssetDataSourceException(e9, e9 instanceof FileNotFoundException ? 2005 : 2000);
        }
    }
}

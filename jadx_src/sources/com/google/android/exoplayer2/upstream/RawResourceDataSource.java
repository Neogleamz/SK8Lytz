package com.google.android.exoplayer2.upstream;

import a6.e;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import b6.l0;
import com.daimajia.numberprogressbar.BuildConfig;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class RawResourceDataSource extends e {

    /* renamed from: e  reason: collision with root package name */
    private final Resources f10926e;

    /* renamed from: f  reason: collision with root package name */
    private final String f10927f;

    /* renamed from: g  reason: collision with root package name */
    private Uri f10928g;

    /* renamed from: h  reason: collision with root package name */
    private AssetFileDescriptor f10929h;

    /* renamed from: i  reason: collision with root package name */
    private InputStream f10930i;

    /* renamed from: j  reason: collision with root package name */
    private long f10931j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f10932k;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class RawResourceDataSourceException extends DataSourceException {
        public RawResourceDataSourceException(String str, Throwable th, int i8) {
            super(str, th, i8);
        }
    }

    public RawResourceDataSource(Context context) {
        super(false);
        this.f10926e = context.getResources();
        this.f10927f = context.getPackageName();
    }

    public static Uri buildRawResourceUri(int i8) {
        return Uri.parse("rawresource:///" + i8);
    }

    @Override // a6.h
    public void close() {
        this.f10928g = null;
        try {
            try {
                InputStream inputStream = this.f10930i;
                if (inputStream != null) {
                    inputStream.close();
                }
                this.f10930i = null;
                try {
                    try {
                        AssetFileDescriptor assetFileDescriptor = this.f10929h;
                        if (assetFileDescriptor != null) {
                            assetFileDescriptor.close();
                        }
                    } finally {
                        this.f10929h = null;
                        if (this.f10932k) {
                            this.f10932k = false;
                            m();
                        }
                    }
                } catch (IOException e8) {
                    throw new RawResourceDataSourceException(null, e8, 2000);
                }
            } catch (IOException e9) {
                throw new RawResourceDataSourceException(null, e9, 2000);
            }
        } catch (Throwable th) {
            this.f10930i = null;
            try {
                try {
                    AssetFileDescriptor assetFileDescriptor2 = this.f10929h;
                    if (assetFileDescriptor2 != null) {
                        assetFileDescriptor2.close();
                    }
                    this.f10929h = null;
                    if (this.f10932k) {
                        this.f10932k = false;
                        m();
                    }
                    throw th;
                } catch (IOException e10) {
                    throw new RawResourceDataSourceException(null, e10, 2000);
                }
            } finally {
                this.f10929h = null;
                if (this.f10932k) {
                    this.f10932k = false;
                    m();
                }
            }
        }
    }

    @Override // a6.f
    public int read(byte[] bArr, int i8, int i9) {
        if (i9 == 0) {
            return 0;
        }
        long j8 = this.f10931j;
        if (j8 == 0) {
            return -1;
        }
        if (j8 != -1) {
            try {
                i9 = (int) Math.min(j8, i9);
            } catch (IOException e8) {
                throw new RawResourceDataSourceException(null, e8, 2000);
            }
        }
        int read = ((InputStream) l0.j(this.f10930i)).read(bArr, i8, i9);
        if (read == -1) {
            if (this.f10931j == -1) {
                return -1;
            }
            throw new RawResourceDataSourceException("End of stream reached having not read sufficient data.", new EOFException(), 2000);
        }
        long j9 = this.f10931j;
        if (j9 != -1) {
            this.f10931j = j9 - read;
        }
        l(read);
        return read;
    }

    @Override // a6.h
    public Uri v() {
        return this.f10928g;
    }

    @Override // a6.h
    public long x(a aVar) {
        int parseInt;
        String str;
        Uri uri = aVar.f10942a;
        this.f10928g = uri;
        if (TextUtils.equals("rawresource", uri.getScheme()) || (TextUtils.equals("android.resource", uri.getScheme()) && uri.getPathSegments().size() == 1 && ((String) b6.a.e(uri.getLastPathSegment())).matches("\\d+"))) {
            try {
                parseInt = Integer.parseInt((String) b6.a.e(uri.getLastPathSegment()));
            } catch (NumberFormatException unused) {
                throw new RawResourceDataSourceException("Resource identifier must be an integer.", null, 1004);
            }
        } else if (!TextUtils.equals("android.resource", uri.getScheme())) {
            throw new RawResourceDataSourceException("URI must either use scheme rawresource or android.resource", null, 1004);
        } else {
            String str2 = (String) b6.a.e(uri.getPath());
            if (str2.startsWith("/")) {
                str2 = str2.substring(1);
            }
            String host = uri.getHost();
            StringBuilder sb = new StringBuilder();
            if (TextUtils.isEmpty(host)) {
                str = BuildConfig.FLAVOR;
            } else {
                str = host + ":";
            }
            sb.append(str);
            sb.append(str2);
            parseInt = this.f10926e.getIdentifier(sb.toString(), "raw", this.f10927f);
            if (parseInt == 0) {
                throw new RawResourceDataSourceException("Resource not found.", null, 2005);
            }
        }
        n(aVar);
        try {
            AssetFileDescriptor openRawResourceFd = this.f10926e.openRawResourceFd(parseInt);
            this.f10929h = openRawResourceFd;
            if (openRawResourceFd == null) {
                throw new RawResourceDataSourceException("Resource is compressed: " + uri, null, 2000);
            }
            long length = openRawResourceFd.getLength();
            FileInputStream fileInputStream = new FileInputStream(openRawResourceFd.getFileDescriptor());
            this.f10930i = fileInputStream;
            int i8 = (length > (-1L) ? 1 : (length == (-1L) ? 0 : -1));
            if (i8 != 0) {
                try {
                    if (aVar.f10948g > length) {
                        throw new RawResourceDataSourceException(null, null, 2008);
                    }
                } catch (RawResourceDataSourceException e8) {
                    throw e8;
                } catch (IOException e9) {
                    throw new RawResourceDataSourceException(null, e9, 2000);
                }
            }
            long startOffset = openRawResourceFd.getStartOffset();
            long skip = fileInputStream.skip(aVar.f10948g + startOffset) - startOffset;
            if (skip == aVar.f10948g) {
                if (i8 == 0) {
                    FileChannel channel = fileInputStream.getChannel();
                    if (channel.size() == 0) {
                        this.f10931j = -1L;
                    } else {
                        long size = channel.size() - channel.position();
                        this.f10931j = size;
                        if (size < 0) {
                            throw new RawResourceDataSourceException(null, null, 2008);
                        }
                    }
                } else {
                    long j8 = length - skip;
                    this.f10931j = j8;
                    if (j8 < 0) {
                        throw new DataSourceException(2008);
                    }
                }
                long j9 = aVar.f10949h;
                if (j9 != -1) {
                    long j10 = this.f10931j;
                    if (j10 != -1) {
                        j9 = Math.min(j10, j9);
                    }
                    this.f10931j = j9;
                }
                this.f10932k = true;
                o(aVar);
                long j11 = aVar.f10949h;
                return j11 != -1 ? j11 : this.f10931j;
            }
            throw new RawResourceDataSourceException(null, null, 2008);
        } catch (Resources.NotFoundException e10) {
            throw new RawResourceDataSourceException(null, e10, 2005);
        }
    }
}

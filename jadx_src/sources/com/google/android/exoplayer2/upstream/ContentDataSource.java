package com.google.android.exoplayer2.upstream;

import a6.e;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import b6.l0;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ContentDataSource extends e {

    /* renamed from: e  reason: collision with root package name */
    private final ContentResolver f10888e;

    /* renamed from: f  reason: collision with root package name */
    private Uri f10889f;

    /* renamed from: g  reason: collision with root package name */
    private AssetFileDescriptor f10890g;

    /* renamed from: h  reason: collision with root package name */
    private FileInputStream f10891h;

    /* renamed from: i  reason: collision with root package name */
    private long f10892i;

    /* renamed from: j  reason: collision with root package name */
    private boolean f10893j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class ContentDataSourceException extends DataSourceException {
        public ContentDataSourceException(IOException iOException, int i8) {
            super(iOException, i8);
        }
    }

    public ContentDataSource(Context context) {
        super(false);
        this.f10888e = context.getContentResolver();
    }

    @Override // a6.h
    public void close() {
        this.f10889f = null;
        try {
            try {
                FileInputStream fileInputStream = this.f10891h;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                this.f10891h = null;
                try {
                    try {
                        AssetFileDescriptor assetFileDescriptor = this.f10890g;
                        if (assetFileDescriptor != null) {
                            assetFileDescriptor.close();
                        }
                    } finally {
                        this.f10890g = null;
                        if (this.f10893j) {
                            this.f10893j = false;
                            m();
                        }
                    }
                } catch (IOException e8) {
                    throw new ContentDataSourceException(e8, 2000);
                }
            } catch (IOException e9) {
                throw new ContentDataSourceException(e9, 2000);
            }
        } catch (Throwable th) {
            this.f10891h = null;
            try {
                try {
                    AssetFileDescriptor assetFileDescriptor2 = this.f10890g;
                    if (assetFileDescriptor2 != null) {
                        assetFileDescriptor2.close();
                    }
                    this.f10890g = null;
                    if (this.f10893j) {
                        this.f10893j = false;
                        m();
                    }
                    throw th;
                } catch (IOException e10) {
                    throw new ContentDataSourceException(e10, 2000);
                }
            } finally {
                this.f10890g = null;
                if (this.f10893j) {
                    this.f10893j = false;
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
        long j8 = this.f10892i;
        if (j8 == 0) {
            return -1;
        }
        if (j8 != -1) {
            try {
                i9 = (int) Math.min(j8, i9);
            } catch (IOException e8) {
                throw new ContentDataSourceException(e8, 2000);
            }
        }
        int read = ((FileInputStream) l0.j(this.f10891h)).read(bArr, i8, i9);
        if (read == -1) {
            return -1;
        }
        long j9 = this.f10892i;
        if (j9 != -1) {
            this.f10892i = j9 - read;
        }
        l(read);
        return read;
    }

    @Override // a6.h
    public Uri v() {
        return this.f10889f;
    }

    @Override // a6.h
    public long x(a aVar) {
        AssetFileDescriptor openAssetFileDescriptor;
        try {
            Uri uri = aVar.f10942a;
            this.f10889f = uri;
            n(aVar);
            if ("content".equals(aVar.f10942a.getScheme())) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("android.provider.extra.ACCEPT_ORIGINAL_MEDIA_FORMAT", true);
                openAssetFileDescriptor = this.f10888e.openTypedAssetFileDescriptor(uri, "*/*", bundle);
            } else {
                openAssetFileDescriptor = this.f10888e.openAssetFileDescriptor(uri, "r");
            }
            this.f10890g = openAssetFileDescriptor;
            if (openAssetFileDescriptor == null) {
                throw new ContentDataSourceException(new IOException("Could not open file descriptor for: " + uri), 2000);
            }
            long length = openAssetFileDescriptor.getLength();
            FileInputStream fileInputStream = new FileInputStream(openAssetFileDescriptor.getFileDescriptor());
            this.f10891h = fileInputStream;
            int i8 = (length > (-1L) ? 1 : (length == (-1L) ? 0 : -1));
            if (i8 != 0 && aVar.f10948g > length) {
                throw new ContentDataSourceException(null, 2008);
            }
            long startOffset = openAssetFileDescriptor.getStartOffset();
            long skip = fileInputStream.skip(aVar.f10948g + startOffset) - startOffset;
            if (skip == aVar.f10948g) {
                if (i8 == 0) {
                    FileChannel channel = fileInputStream.getChannel();
                    long size = channel.size();
                    if (size == 0) {
                        this.f10892i = -1L;
                    } else {
                        long position = size - channel.position();
                        this.f10892i = position;
                        if (position < 0) {
                            throw new ContentDataSourceException(null, 2008);
                        }
                    }
                } else {
                    long j8 = length - skip;
                    this.f10892i = j8;
                    if (j8 < 0) {
                        throw new ContentDataSourceException(null, 2008);
                    }
                }
                long j9 = aVar.f10949h;
                if (j9 != -1) {
                    long j10 = this.f10892i;
                    if (j10 != -1) {
                        j9 = Math.min(j10, j9);
                    }
                    this.f10892i = j9;
                }
                this.f10893j = true;
                o(aVar);
                long j11 = aVar.f10949h;
                return j11 != -1 ? j11 : this.f10892i;
            }
            throw new ContentDataSourceException(null, 2008);
        } catch (ContentDataSourceException e8) {
            throw e8;
        } catch (IOException e9) {
            throw new ContentDataSourceException(e9, e9 instanceof FileNotFoundException ? 2005 : 2000);
        }
    }
}

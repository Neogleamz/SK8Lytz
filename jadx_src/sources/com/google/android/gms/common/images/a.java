package com.google.android.gms.common.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final Uri f11763a;

    /* renamed from: b  reason: collision with root package name */
    private final ParcelFileDescriptor f11764b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ ImageManager f11765c;

    public a(ImageManager imageManager, Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
        this.f11763a = uri;
        this.f11764b = parcelFileDescriptor;
    }

    @Override // java.lang.Runnable
    public final void run() {
        n6.b.b("LoadBitmapFromDiskRunnable can't be executed in the main thread");
        ParcelFileDescriptor parcelFileDescriptor = this.f11764b;
        Bitmap bitmap = null;
        boolean z4 = false;
        if (parcelFileDescriptor != null) {
            try {
                bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
            } catch (OutOfMemoryError e8) {
                Log.e("ImageManager", "OOM while loading bitmap for uri: ".concat(String.valueOf(this.f11763a)), e8);
                z4 = true;
            }
            try {
                this.f11764b.close();
            } catch (IOException e9) {
                Log.e("ImageManager", "closed failed", e9);
            }
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ImageManager imageManager = this.f11765c;
        ImageManager.b(imageManager).post(new b(imageManager, this.f11763a, bitmap, z4, countDownLatch));
        try {
            countDownLatch.await();
        } catch (InterruptedException unused) {
            Log.w("ImageManager", "Latch interrupted while posting ".concat(String.valueOf(this.f11763a)));
        }
    }
}

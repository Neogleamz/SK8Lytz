package com.google.android.gms.common.images;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import com.google.android.gms.common.images.ImageManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final Uri f11766a;

    /* renamed from: b  reason: collision with root package name */
    private final Bitmap f11767b;

    /* renamed from: c  reason: collision with root package name */
    private final CountDownLatch f11768c;

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ ImageManager f11769d;

    public b(ImageManager imageManager, Uri uri, Bitmap bitmap, boolean z4, CountDownLatch countDownLatch) {
        this.f11766a = uri;
        this.f11767b = bitmap;
        this.f11768c = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        HashSet hashSet;
        ArrayList arrayList;
        n6.b.a("OnBitmapLoadedRunnable must be executed in the main thread");
        Bitmap bitmap = this.f11767b;
        ImageManager.ImageReceiver imageReceiver = (ImageManager.ImageReceiver) ImageManager.h(this.f11769d).remove(this.f11766a);
        if (imageReceiver != null) {
            arrayList = imageReceiver.f11757b;
            int size = arrayList.size();
            for (int i8 = 0; i8 < size; i8++) {
                c cVar = (c) arrayList.get(i8);
                Bitmap bitmap2 = this.f11767b;
                if (bitmap2 == null || bitmap == null) {
                    ImageManager.f(this.f11769d).put(this.f11766a, Long.valueOf(SystemClock.elapsedRealtime()));
                    ImageManager imageManager = this.f11769d;
                    cVar.b(ImageManager.a(imageManager), ImageManager.c(imageManager), false);
                } else {
                    cVar.c(ImageManager.a(this.f11769d), bitmap2, false);
                }
                ImageManager.g(this.f11769d).remove(cVar);
            }
        }
        this.f11768c.countDown();
        obj = ImageManager.f11754a;
        synchronized (obj) {
            hashSet = ImageManager.f11755b;
            hashSet.remove(this.f11766a);
        }
    }
}

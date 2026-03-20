package com.google.android.gms.common.images;

import a7.g;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import com.google.android.gms.common.annotation.KeepName;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ImageManager {

    /* renamed from: a  reason: collision with root package name */
    private static final Object f11754a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private static HashSet f11755b = new HashSet();

    /* JADX INFO: Access modifiers changed from: private */
    @KeepName
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class ImageReceiver extends ResultReceiver {

        /* renamed from: a  reason: collision with root package name */
        private final Uri f11756a;

        /* renamed from: b  reason: collision with root package name */
        private final ArrayList f11757b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ ImageManager f11758c;

        @Override // android.os.ResultReceiver
        public final void onReceiveResult(int i8, Bundle bundle) {
            ImageManager imageManager = this.f11758c;
            ImageManager.i(imageManager).execute(new a(imageManager, this.f11756a, (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ Context a(ImageManager imageManager) {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ Handler b(ImageManager imageManager) {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ g c(ImageManager imageManager) {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ Map f(ImageManager imageManager) {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ Map g(ImageManager imageManager) {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* bridge */ /* synthetic */ Map h(ImageManager imageManager) {
        throw null;
    }

    static /* bridge */ /* synthetic */ ExecutorService i(ImageManager imageManager) {
        throw null;
    }
}

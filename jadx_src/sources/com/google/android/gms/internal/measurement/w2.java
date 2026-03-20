package com.google.android.gms.internal.measurement;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w2 implements ThreadFactory {

    /* renamed from: a  reason: collision with root package name */
    private ThreadFactory f12632a = Executors.defaultThreadFactory();

    /* JADX INFO: Access modifiers changed from: package-private */
    public w2(p2 p2Var) {
    }

    @Override // java.util.concurrent.ThreadFactory
    public final Thread newThread(Runnable runnable) {
        Thread newThread = this.f12632a.newThread(runnable);
        newThread.setName("ScionFrontendApi");
        return newThread;
    }
}

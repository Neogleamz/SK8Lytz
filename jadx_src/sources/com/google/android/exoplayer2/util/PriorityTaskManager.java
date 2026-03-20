package com.google.android.exoplayer2.util;

import b6.l0;
import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class PriorityTaskManager {

    /* renamed from: a  reason: collision with root package name */
    private final Object f10983a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private final PriorityQueue<Integer> f10984b = new PriorityQueue<>(10, Collections.reverseOrder());

    /* renamed from: c  reason: collision with root package name */
    private int f10985c = Integer.MIN_VALUE;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class PriorityTooLowException extends IOException {
    }

    public void a(int i8) {
        synchronized (this.f10983a) {
            this.f10984b.add(Integer.valueOf(i8));
            this.f10985c = Math.max(this.f10985c, i8);
        }
    }

    public void b(int i8) {
        synchronized (this.f10983a) {
            this.f10984b.remove(Integer.valueOf(i8));
            this.f10985c = this.f10984b.isEmpty() ? Integer.MIN_VALUE : ((Integer) l0.j(this.f10984b.peek())).intValue();
            this.f10983a.notifyAll();
        }
    }
}

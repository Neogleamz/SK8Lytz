package com.arthenica.ffmpegkit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum Signal {
    SIGINT(2),
    SIGQUIT(3),
    SIGPIPE(13),
    SIGTERM(15),
    SIGXCPU(24);
    

    /* renamed from: a  reason: collision with root package name */
    private final int f8785a;

    Signal(int i8) {
        this.f8785a = i8;
    }

    public int f() {
        return this.f8785a;
    }
}

package com.google.android.datatransport.cct.internal;

import com.google.android.datatransport.cct.internal.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ClientInfo {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum ClientType {
        UNKNOWN(0),
        ANDROID_FIREBASE(23);
        

        /* renamed from: a  reason: collision with root package name */
        private final int f8944a;

        ClientType(int i8) {
            this.f8944a = i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        public abstract ClientInfo a();

        public abstract a b(com.google.android.datatransport.cct.internal.a aVar);

        public abstract a c(ClientType clientType);
    }

    public static a a() {
        return new e.b();
    }

    public abstract com.google.android.datatransport.cct.internal.a b();

    public abstract ClientType c();
}

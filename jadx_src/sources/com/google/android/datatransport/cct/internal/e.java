package com.google.android.datatransport.cct.internal;

import com.google.android.datatransport.cct.internal.ClientInfo;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e extends ClientInfo {

    /* renamed from: a  reason: collision with root package name */
    private final ClientInfo.ClientType f9055a;

    /* renamed from: b  reason: collision with root package name */
    private final com.google.android.datatransport.cct.internal.a f9056b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends ClientInfo.a {

        /* renamed from: a  reason: collision with root package name */
        private ClientInfo.ClientType f9057a;

        /* renamed from: b  reason: collision with root package name */
        private com.google.android.datatransport.cct.internal.a f9058b;

        @Override // com.google.android.datatransport.cct.internal.ClientInfo.a
        public ClientInfo a() {
            return new e(this.f9057a, this.f9058b);
        }

        @Override // com.google.android.datatransport.cct.internal.ClientInfo.a
        public ClientInfo.a b(com.google.android.datatransport.cct.internal.a aVar) {
            this.f9058b = aVar;
            return this;
        }

        @Override // com.google.android.datatransport.cct.internal.ClientInfo.a
        public ClientInfo.a c(ClientInfo.ClientType clientType) {
            this.f9057a = clientType;
            return this;
        }
    }

    private e(ClientInfo.ClientType clientType, com.google.android.datatransport.cct.internal.a aVar) {
        this.f9055a = clientType;
        this.f9056b = aVar;
    }

    @Override // com.google.android.datatransport.cct.internal.ClientInfo
    public com.google.android.datatransport.cct.internal.a b() {
        return this.f9056b;
    }

    @Override // com.google.android.datatransport.cct.internal.ClientInfo
    public ClientInfo.ClientType c() {
        return this.f9055a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ClientInfo) {
            ClientInfo clientInfo = (ClientInfo) obj;
            ClientInfo.ClientType clientType = this.f9055a;
            if (clientType != null ? clientType.equals(clientInfo.c()) : clientInfo.c() == null) {
                com.google.android.datatransport.cct.internal.a aVar = this.f9056b;
                com.google.android.datatransport.cct.internal.a b9 = clientInfo.b();
                if (aVar == null) {
                    if (b9 == null) {
                        return true;
                    }
                } else if (aVar.equals(b9)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        ClientInfo.ClientType clientType = this.f9055a;
        int hashCode = ((clientType == null ? 0 : clientType.hashCode()) ^ 1000003) * 1000003;
        com.google.android.datatransport.cct.internal.a aVar = this.f9056b;
        return hashCode ^ (aVar != null ? aVar.hashCode() : 0);
    }

    public String toString() {
        return "ClientInfo{clientType=" + this.f9055a + ", androidClientInfo=" + this.f9056b + "}";
    }
}

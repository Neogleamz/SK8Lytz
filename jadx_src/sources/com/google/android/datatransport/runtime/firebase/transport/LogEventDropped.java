package com.google.android.datatransport.runtime.firebase.transport;

import com.google.firebase.encoders.proto.Protobuf;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class LogEventDropped {

    /* renamed from: c  reason: collision with root package name */
    private static final LogEventDropped f9098c = new a().a();

    /* renamed from: a  reason: collision with root package name */
    private final long f9099a;

    /* renamed from: b  reason: collision with root package name */
    private final Reason f9100b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum Reason implements m8.a {
        REASON_UNKNOWN(0),
        MESSAGE_TOO_OLD(1),
        CACHE_FULL(2),
        PAYLOAD_TOO_BIG(3),
        MAX_RETRIES_REACHED(4),
        INVALID_PAYLOD(5),
        SERVER_ERROR(6);
        

        /* renamed from: a  reason: collision with root package name */
        private final int f9109a;

        Reason(int i8) {
            this.f9109a = i8;
        }

        public int c() {
            return this.f9109a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private long f9110a = 0;

        /* renamed from: b  reason: collision with root package name */
        private Reason f9111b = Reason.REASON_UNKNOWN;

        a() {
        }

        public LogEventDropped a() {
            return new LogEventDropped(this.f9110a, this.f9111b);
        }

        public a b(long j8) {
            this.f9110a = j8;
            return this;
        }

        public a c(Reason reason) {
            this.f9111b = reason;
            return this;
        }
    }

    LogEventDropped(long j8, Reason reason) {
        this.f9099a = j8;
        this.f9100b = reason;
    }

    public static a c() {
        return new a();
    }

    @Protobuf(tag = 1)
    public long a() {
        return this.f9099a;
    }

    @Protobuf(tag = 3)
    public Reason b() {
        return this.f9100b;
    }
}

package com.google.android.exoplayer2.metadata.scte35;

import a5.c;
import a5.e;
import b6.h0;
import b6.y;
import b6.z;
import com.google.android.exoplayer2.metadata.Metadata;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends e {

    /* renamed from: a  reason: collision with root package name */
    private final z f10176a = new z();

    /* renamed from: b  reason: collision with root package name */
    private final y f10177b = new y();

    /* renamed from: c  reason: collision with root package name */
    private h0 f10178c;

    @Override // a5.e
    protected Metadata b(c cVar, ByteBuffer byteBuffer) {
        h0 h0Var = this.f10178c;
        if (h0Var == null || cVar.f73j != h0Var.e()) {
            h0 h0Var2 = new h0(cVar.f9514e);
            this.f10178c = h0Var2;
            h0Var2.a(cVar.f9514e - cVar.f73j);
        }
        byte[] array = byteBuffer.array();
        int limit = byteBuffer.limit();
        this.f10176a.S(array, limit);
        this.f10177b.o(array, limit);
        this.f10177b.r(39);
        long h8 = (this.f10177b.h(1) << 32) | this.f10177b.h(32);
        this.f10177b.r(20);
        int h9 = this.f10177b.h(12);
        int h10 = this.f10177b.h(8);
        Metadata.Entry entry = null;
        this.f10176a.V(14);
        if (h10 == 0) {
            entry = new SpliceNullCommand();
        } else if (h10 == 255) {
            entry = PrivateCommand.a(this.f10176a, h9, h8);
        } else if (h10 == 4) {
            entry = SpliceScheduleCommand.a(this.f10176a);
        } else if (h10 == 5) {
            entry = SpliceInsertCommand.a(this.f10176a, h8, this.f10178c);
        } else if (h10 == 6) {
            entry = TimeSignalCommand.a(this.f10176a, h8, this.f10178c);
        }
        return entry == null ? new Metadata(new Metadata.Entry[0]) : new Metadata(entry);
    }
}

package com.google.android.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.media.metrics.LogSessionId;
import android.text.TextUtils;
import b6.l0;
import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.m;
import j4.t1;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n implements m {

    /* renamed from: d  reason: collision with root package name */
    public static final m.c f9632d = m4.n.a;

    /* renamed from: a  reason: collision with root package name */
    private final UUID f9633a;

    /* renamed from: b  reason: collision with root package name */
    private final MediaDrm f9634b;

    /* renamed from: c  reason: collision with root package name */
    private int f9635c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {
        public static boolean a(MediaDrm mediaDrm, String str) {
            return mediaDrm.requiresSecureDecoder(str);
        }

        public static void b(MediaDrm mediaDrm, byte[] bArr, t1 t1Var) {
            LogSessionId a9 = t1Var.a();
            if (a9.equals(LogSessionId.LOG_SESSION_ID_NONE)) {
                return;
            }
            ((MediaDrm.PlaybackComponent) b6.a.e(mediaDrm.getPlaybackComponent(bArr))).setLogSessionId(a9);
        }
    }

    private n(UUID uuid) {
        b6.a.e(uuid);
        b6.a.b(!i4.b.f20466b.equals(uuid), "Use C.CLEARKEY_UUID instead");
        this.f9633a = uuid;
        MediaDrm mediaDrm = new MediaDrm(u(uuid));
        this.f9634b = mediaDrm;
        this.f9635c = 1;
        if (i4.b.f20468d.equals(uuid) && B()) {
            w(mediaDrm);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ m A(UUID uuid) {
        try {
            return C(uuid);
        } catch (UnsupportedDrmException unused) {
            b6.p.c("FrameworkMediaDrm", "Failed to instantiate a FrameworkMediaDrm for uuid: " + uuid + ".");
            return new k();
        }
    }

    private static boolean B() {
        return "ASUS_Z00AD".equals(l0.f8066d);
    }

    public static n C(UUID uuid) {
        try {
            return new n(uuid);
        } catch (UnsupportedSchemeException e8) {
            throw new UnsupportedDrmException(1, e8);
        } catch (Exception e9) {
            throw new UnsupportedDrmException(2, e9);
        }
    }

    private static byte[] p(byte[] bArr) {
        int indexOf;
        z zVar = new z(bArr);
        int u8 = zVar.u();
        short w8 = zVar.w();
        short w9 = zVar.w();
        if (w8 != 1 || w9 != 1) {
            b6.p.f("FrameworkMediaDrm", "Unexpected record count or type. Skipping LA_URL workaround.");
            return bArr;
        }
        short w10 = zVar.w();
        Charset charset = com.google.common.base.e.f18819e;
        String F = zVar.F(w10, charset);
        if (F.contains("<LA_URL>")) {
            return bArr;
        }
        if (F.indexOf("</DATA>") == -1) {
            b6.p.i("FrameworkMediaDrm", "Could not find the </DATA> tag. Skipping LA_URL workaround.");
        }
        String str = F.substring(0, indexOf) + "<LA_URL>https://x</LA_URL>" + F.substring(indexOf);
        int i8 = u8 + 52;
        ByteBuffer allocate = ByteBuffer.allocate(i8);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        allocate.putInt(i8);
        allocate.putShort(w8);
        allocate.putShort(w9);
        allocate.putShort((short) (str.length() * 2));
        allocate.put(str.getBytes(charset));
        return allocate.array();
    }

    private static String q(String str) {
        return "<LA_URL>https://x</LA_URL>".equals(str) ? BuildConfig.FLAVOR : (l0.f8063a == 33 && "https://default.url".equals(str)) ? BuildConfig.FLAVOR : str;
    }

    private static byte[] r(UUID uuid, byte[] bArr) {
        return i4.b.f20467c.equals(uuid) ? com.google.android.exoplayer2.drm.a.a(bArr) : bArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0056, code lost:
        if ("AFTT".equals(r0) == false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static byte[] s(java.util.UUID r3, byte[] r4) {
        /*
            java.util.UUID r0 = i4.b.f20469e
            boolean r1 = r0.equals(r3)
            if (r1 == 0) goto L18
            byte[] r1 = v4.l.e(r4, r3)
            if (r1 != 0) goto Lf
            goto L10
        Lf:
            r4 = r1
        L10:
            byte[] r4 = p(r4)
            byte[] r4 = v4.l.a(r0, r4)
        L18:
            int r1 = b6.l0.f8063a
            r2 = 23
            if (r1 >= r2) goto L26
            java.util.UUID r1 = i4.b.f20468d
            boolean r1 = r1.equals(r3)
            if (r1 != 0) goto L58
        L26:
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L5f
            java.lang.String r0 = b6.l0.f8065c
            java.lang.String r1 = "Amazon"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L5f
            java.lang.String r0 = b6.l0.f8066d
            java.lang.String r1 = "AFTB"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L58
            java.lang.String r1 = "AFTS"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L58
            java.lang.String r1 = "AFTM"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L58
            java.lang.String r1 = "AFTT"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L5f
        L58:
            byte[] r3 = v4.l.e(r4, r3)
            if (r3 == 0) goto L5f
            return r3
        L5f:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.drm.n.s(java.util.UUID, byte[]):byte[]");
    }

    private static String t(UUID uuid, String str) {
        return (l0.f8063a < 26 && i4.b.f20467c.equals(uuid) && ("video/mp4".equals(str) || "audio/mp4".equals(str))) ? "cenc" : str;
    }

    private static UUID u(UUID uuid) {
        return (l0.f8063a >= 27 || !i4.b.f20467c.equals(uuid)) ? uuid : i4.b.f20466b;
    }

    private static void w(MediaDrm mediaDrm) {
        mediaDrm.setPropertyString("securityLevel", "L3");
    }

    private static DrmInitData.SchemeData y(UUID uuid, List<DrmInitData.SchemeData> list) {
        boolean z4;
        if (i4.b.f20468d.equals(uuid)) {
            if (l0.f8063a >= 28 && list.size() > 1) {
                DrmInitData.SchemeData schemeData = list.get(0);
                int i8 = 0;
                for (int i9 = 0; i9 < list.size(); i9++) {
                    DrmInitData.SchemeData schemeData2 = list.get(i9);
                    byte[] bArr = (byte[]) b6.a.e(schemeData2.f9600e);
                    if (!l0.c(schemeData2.f9599d, schemeData.f9599d) || !l0.c(schemeData2.f9598c, schemeData.f9598c) || !v4.l.c(bArr)) {
                        z4 = false;
                        break;
                    }
                    i8 += bArr.length;
                }
                z4 = true;
                if (z4) {
                    byte[] bArr2 = new byte[i8];
                    int i10 = 0;
                    for (int i11 = 0; i11 < list.size(); i11++) {
                        byte[] bArr3 = (byte[]) b6.a.e(list.get(i11).f9600e);
                        int length = bArr3.length;
                        System.arraycopy(bArr3, 0, bArr2, i10, length);
                        i10 += length;
                    }
                    return schemeData.b(bArr2);
                }
            }
            for (int i12 = 0; i12 < list.size(); i12++) {
                DrmInitData.SchemeData schemeData3 = list.get(i12);
                int g8 = v4.l.g((byte[]) b6.a.e(schemeData3.f9600e));
                int i13 = l0.f8063a;
                if (i13 < 23 && g8 == 0) {
                    return schemeData3;
                }
                if (i13 >= 23 && g8 == 1) {
                    return schemeData3;
                }
            }
        }
        return list.get(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void z(m.b bVar, MediaDrm mediaDrm, byte[] bArr, int i8, int i9, byte[] bArr2) {
        bVar.a(this, bArr, i8, i9, bArr2);
    }

    @Override // com.google.android.exoplayer2.drm.m
    public Map<String, String> a(byte[] bArr) {
        return this.f9634b.queryKeyStatus(bArr);
    }

    @Override // com.google.android.exoplayer2.drm.m
    public m.d b() {
        MediaDrm.ProvisionRequest provisionRequest = this.f9634b.getProvisionRequest();
        return new m.d(provisionRequest.getData(), provisionRequest.getDefaultUrl());
    }

    @Override // com.google.android.exoplayer2.drm.m
    public byte[] d() {
        return this.f9634b.openSession();
    }

    @Override // com.google.android.exoplayer2.drm.m
    public boolean e(byte[] bArr, String str) {
        if (l0.f8063a >= 31) {
            return a.a(this.f9634b, str);
        }
        try {
            MediaCrypto mediaCrypto = new MediaCrypto(this.f9633a, bArr);
            try {
                return mediaCrypto.requiresSecureDecoderComponent(str);
            } finally {
                mediaCrypto.release();
            }
        } catch (MediaCryptoException unused) {
            return true;
        }
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void f(byte[] bArr, byte[] bArr2) {
        this.f9634b.restoreKeys(bArr, bArr2);
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void g(byte[] bArr) {
        this.f9634b.closeSession(bArr);
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void h(m.b bVar) {
        this.f9634b.setOnEventListener(bVar == null ? null : new m4.m(this, bVar));
    }

    @Override // com.google.android.exoplayer2.drm.m
    public byte[] i(byte[] bArr, byte[] bArr2) {
        if (i4.b.f20467c.equals(this.f9633a)) {
            bArr2 = com.google.android.exoplayer2.drm.a.b(bArr2);
        }
        return this.f9634b.provideKeyResponse(bArr, bArr2);
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void j(byte[] bArr) {
        this.f9634b.provideProvisionResponse(bArr);
    }

    @Override // com.google.android.exoplayer2.drm.m
    @SuppressLint({"WrongConstant"})
    public m.a k(byte[] bArr, List<DrmInitData.SchemeData> list, int i8, HashMap<String, String> hashMap) {
        byte[] bArr2;
        String str;
        DrmInitData.SchemeData schemeData = null;
        if (list != null) {
            schemeData = y(this.f9633a, list);
            bArr2 = s(this.f9633a, (byte[]) b6.a.e(schemeData.f9600e));
            str = t(this.f9633a, schemeData.f9599d);
        } else {
            bArr2 = null;
            str = null;
        }
        MediaDrm.KeyRequest keyRequest = this.f9634b.getKeyRequest(bArr, bArr2, str, i8, hashMap);
        byte[] r4 = r(this.f9633a, keyRequest.getData());
        String q = q(keyRequest.getDefaultUrl());
        if (TextUtils.isEmpty(q) && schemeData != null && !TextUtils.isEmpty(schemeData.f9598c)) {
            q = schemeData.f9598c;
        }
        return new m.a(r4, q, l0.f8063a >= 23 ? keyRequest.getRequestType() : Integer.MIN_VALUE);
    }

    @Override // com.google.android.exoplayer2.drm.m
    public int l() {
        return 2;
    }

    @Override // com.google.android.exoplayer2.drm.m
    public void m(byte[] bArr, t1 t1Var) {
        if (l0.f8063a >= 31) {
            try {
                a.b(this.f9634b, bArr, t1Var);
            } catch (UnsupportedOperationException unused) {
                b6.p.i("FrameworkMediaDrm", "setLogSessionId failed.");
            }
        }
    }

    @Override // com.google.android.exoplayer2.drm.m
    public synchronized void release() {
        int i8 = this.f9635c - 1;
        this.f9635c = i8;
        if (i8 == 0) {
            this.f9634b.release();
        }
    }

    @Override // com.google.android.exoplayer2.drm.m
    /* renamed from: v */
    public m4.l c(byte[] bArr) {
        return new m4.l(u(this.f9633a), bArr, l0.f8063a < 21 && i4.b.f20468d.equals(this.f9633a) && "L3".equals(x("securityLevel")));
    }

    public String x(String str) {
        return this.f9634b.getPropertyString(str);
    }
}

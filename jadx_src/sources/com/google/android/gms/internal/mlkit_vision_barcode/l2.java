package com.google.android.gms.internal.mlkit_vision_barcode;

import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.firebase.encoders.EncodingException;
import j8.b;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class l2 implements j8.d {

    /* renamed from: f  reason: collision with root package name */
    private static final Charset f13652f = Charset.forName("UTF-8");

    /* renamed from: g  reason: collision with root package name */
    private static final j8.b f13653g;

    /* renamed from: h  reason: collision with root package name */
    private static final j8.b f13654h;

    /* renamed from: i  reason: collision with root package name */
    private static final j8.c f13655i;

    /* renamed from: a  reason: collision with root package name */
    private OutputStream f13656a;

    /* renamed from: b  reason: collision with root package name */
    private final Map f13657b;

    /* renamed from: c  reason: collision with root package name */
    private final Map f13658c;

    /* renamed from: d  reason: collision with root package name */
    private final j8.c f13659d;

    /* renamed from: e  reason: collision with root package name */
    private final p2 f13660e = new p2(this);

    static {
        b.b a9 = j8.b.a("key");
        g2 g2Var = new g2();
        g2Var.a(1);
        f13653g = a9.b(g2Var.b()).a();
        b.b a10 = j8.b.a("value");
        g2 g2Var2 = new g2();
        g2Var2.a(2);
        f13654h = a10.b(g2Var2.b()).a();
        f13655i = new j8.c() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.k2
            public final void a(Object obj, Object obj2) {
                l2.j((Map.Entry) obj, (j8.d) obj2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public l2(OutputStream outputStream, Map map, Map map2, j8.c cVar) {
        this.f13656a = outputStream;
        this.f13657b = map;
        this.f13658c = map2;
        this.f13659d = cVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void j(Map.Entry entry, j8.d dVar) {
        dVar.c(f13653g, entry.getKey());
        dVar.c(f13654h, entry.getValue());
    }

    private static int k(j8.b bVar) {
        j2 j2Var = (j2) bVar.c(j2.class);
        if (j2Var != null) {
            return j2Var.zza();
        }
        throw new EncodingException("Field has no @Protobuf config");
    }

    private final long l(j8.c cVar, Object obj) {
        h2 h2Var = new h2();
        try {
            OutputStream outputStream = this.f13656a;
            this.f13656a = h2Var;
            cVar.a(obj, this);
            this.f13656a = outputStream;
            long a9 = h2Var.a();
            h2Var.close();
            return a9;
        } catch (Throwable th) {
            try {
                h2Var.close();
            } catch (Throwable th2) {
                try {
                    Throwable.class.getDeclaredMethod("addSuppressed", Throwable.class).invoke(th, th2);
                } catch (Exception unused) {
                }
            }
            throw th;
        }
    }

    private static j2 m(j8.b bVar) {
        j2 j2Var = (j2) bVar.c(j2.class);
        if (j2Var != null) {
            return j2Var;
        }
        throw new EncodingException("Field has no @Protobuf config");
    }

    private final l2 n(j8.c cVar, j8.b bVar, Object obj, boolean z4) {
        long l8 = l(cVar, obj);
        if (z4 && l8 == 0) {
            return this;
        }
        q((k(bVar) << 3) | 2);
        r(l8);
        cVar.a(obj, this);
        return this;
    }

    private final l2 o(j8.e eVar, j8.b bVar, Object obj, boolean z4) {
        this.f13660e.a(bVar, z4);
        eVar.a(obj, this.f13660e);
        return this;
    }

    private static ByteBuffer p(int i8) {
        return ByteBuffer.allocate(i8).order(ByteOrder.LITTLE_ENDIAN);
    }

    private final void q(int i8) {
        while ((i8 & (-128)) != 0) {
            this.f13656a.write((i8 & 127) | RecognitionOptions.ITF);
            i8 >>>= 7;
        }
        this.f13656a.write(i8 & 127);
    }

    private final void r(long j8) {
        while (((-128) & j8) != 0) {
            this.f13656a.write((((int) j8) & 127) | RecognitionOptions.ITF);
            j8 >>>= 7;
        }
        this.f13656a.write(((int) j8) & 127);
    }

    public final /* synthetic */ j8.d a(j8.b bVar, long j8) {
        h(bVar, j8, true);
        return this;
    }

    public final /* synthetic */ j8.d b(j8.b bVar, int i8) {
        g(bVar, i8, true);
        return this;
    }

    public final j8.d c(j8.b bVar, Object obj) {
        f(bVar, obj, true);
        return this;
    }

    final j8.d d(j8.b bVar, double d8, boolean z4) {
        if (z4 && d8 == 0.0d) {
            return this;
        }
        q((k(bVar) << 3) | 1);
        this.f13656a.write(p(8).putDouble(d8).array());
        return this;
    }

    final j8.d e(j8.b bVar, float f5, boolean z4) {
        if (z4 && f5 == 0.0f) {
            return this;
        }
        q((k(bVar) << 3) | 5);
        this.f13656a.write(p(4).putFloat(f5).array());
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final j8.d f(j8.b bVar, Object obj, boolean z4) {
        if (obj == null) {
            return this;
        }
        if (obj instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) obj;
            if (z4 && charSequence.length() == 0) {
                return this;
            }
            q((k(bVar) << 3) | 2);
            byte[] bytes = charSequence.toString().getBytes(f13652f);
            q(bytes.length);
            this.f13656a.write(bytes);
            return this;
        } else if (obj instanceof Collection) {
            for (Object obj2 : (Collection) obj) {
                f(bVar, obj2, false);
            }
            return this;
        } else if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                n(f13655i, bVar, entry, false);
            }
            return this;
        } else if (obj instanceof Double) {
            d(bVar, ((Double) obj).doubleValue(), z4);
            return this;
        } else if (obj instanceof Float) {
            e(bVar, ((Float) obj).floatValue(), z4);
            return this;
        } else if (obj instanceof Number) {
            h(bVar, ((Number) obj).longValue(), z4);
            return this;
        } else if (obj instanceof Boolean) {
            g(bVar, ((Boolean) obj).booleanValue() ? 1 : 0, z4);
            return this;
        } else if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            if (z4 && bArr.length == 0) {
                return this;
            }
            q((k(bVar) << 3) | 2);
            q(bArr.length);
            this.f13656a.write(bArr);
            return this;
        } else {
            j8.c cVar = (j8.c) this.f13657b.get(obj.getClass());
            if (cVar != null) {
                n(cVar, bVar, obj, z4);
                return this;
            }
            j8.e eVar = (j8.e) this.f13658c.get(obj.getClass());
            if (eVar != null) {
                o(eVar, bVar, obj, z4);
                return this;
            } else if (obj instanceof i2) {
                g(bVar, ((i2) obj).zza(), true);
                return this;
            } else if (obj instanceof Enum) {
                g(bVar, ((Enum) obj).ordinal(), true);
                return this;
            } else {
                n(this.f13659d, bVar, obj, z4);
                return this;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final l2 g(j8.b bVar, int i8, boolean z4) {
        if (z4 && i8 == 0) {
            return this;
        }
        j2 m8 = m(bVar);
        zzff zzffVar = zzff.DEFAULT;
        int ordinal = m8.zzb().ordinal();
        if (ordinal == 0) {
            q(m8.zza() << 3);
            q(i8);
        } else if (ordinal == 1) {
            q(m8.zza() << 3);
            q((i8 + i8) ^ (i8 >> 31));
        } else if (ordinal == 2) {
            q((m8.zza() << 3) | 5);
            this.f13656a.write(p(4).putInt(i8).array());
        }
        return this;
    }

    final l2 h(j8.b bVar, long j8, boolean z4) {
        if (z4 && j8 == 0) {
            return this;
        }
        j2 m8 = m(bVar);
        zzff zzffVar = zzff.DEFAULT;
        int ordinal = m8.zzb().ordinal();
        if (ordinal == 0) {
            q(m8.zza() << 3);
            r(j8);
        } else if (ordinal == 1) {
            q(m8.zza() << 3);
            r((j8 >> 63) ^ (j8 + j8));
        } else if (ordinal == 2) {
            q((m8.zza() << 3) | 1);
            this.f13656a.write(p(8).putLong(j8).array());
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final l2 i(Object obj) {
        if (obj == null) {
            return this;
        }
        j8.c cVar = (j8.c) this.f13657b.get(obj.getClass());
        if (cVar != null) {
            cVar.a(obj, this);
            return this;
        }
        throw new EncodingException("No encoder for ".concat(String.valueOf(obj.getClass())));
    }
}

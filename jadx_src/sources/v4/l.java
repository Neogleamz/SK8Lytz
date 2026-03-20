package v4;

import b6.z;
import java.nio.ByteBuffer;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private final UUID f23287a;

        /* renamed from: b  reason: collision with root package name */
        private final int f23288b;

        /* renamed from: c  reason: collision with root package name */
        private final byte[] f23289c;

        public a(UUID uuid, int i8, byte[] bArr) {
            this.f23287a = uuid;
            this.f23288b = i8;
            this.f23289c = bArr;
        }
    }

    public static byte[] a(UUID uuid, byte[] bArr) {
        return b(uuid, null, bArr);
    }

    public static byte[] b(UUID uuid, UUID[] uuidArr, byte[] bArr) {
        int length = (bArr != null ? bArr.length : 0) + 32;
        if (uuidArr != null) {
            length += (uuidArr.length * 16) + 4;
        }
        ByteBuffer allocate = ByteBuffer.allocate(length);
        allocate.putInt(length);
        allocate.putInt(1886614376);
        allocate.putInt(uuidArr != null ? 16777216 : 0);
        allocate.putLong(uuid.getMostSignificantBits());
        allocate.putLong(uuid.getLeastSignificantBits());
        if (uuidArr != null) {
            allocate.putInt(uuidArr.length);
            for (UUID uuid2 : uuidArr) {
                allocate.putLong(uuid2.getMostSignificantBits());
                allocate.putLong(uuid2.getLeastSignificantBits());
            }
        }
        if (bArr != null && bArr.length != 0) {
            allocate.putInt(bArr.length);
            allocate.put(bArr);
        }
        return allocate.array();
    }

    public static boolean c(byte[] bArr) {
        return d(bArr) != null;
    }

    private static a d(byte[] bArr) {
        z zVar = new z(bArr);
        if (zVar.g() < 32) {
            return null;
        }
        zVar.U(0);
        if (zVar.q() == zVar.a() + 4 && zVar.q() == 1886614376) {
            int c9 = v4.a.c(zVar.q());
            if (c9 > 1) {
                b6.p.i("PsshAtomUtil", "Unsupported pssh version: " + c9);
                return null;
            }
            UUID uuid = new UUID(zVar.A(), zVar.A());
            if (c9 == 1) {
                zVar.V(zVar.L() * 16);
            }
            int L = zVar.L();
            if (L != zVar.a()) {
                return null;
            }
            byte[] bArr2 = new byte[L];
            zVar.l(bArr2, 0, L);
            return new a(uuid, c9, bArr2);
        }
        return null;
    }

    public static byte[] e(byte[] bArr, UUID uuid) {
        a d8 = d(bArr);
        if (d8 == null) {
            return null;
        }
        if (uuid.equals(d8.f23287a)) {
            return d8.f23289c;
        }
        b6.p.i("PsshAtomUtil", "UUID mismatch. Expected: " + uuid + ", got: " + d8.f23287a + ".");
        return null;
    }

    public static UUID f(byte[] bArr) {
        a d8 = d(bArr);
        if (d8 == null) {
            return null;
        }
        return d8.f23287a;
    }

    public static int g(byte[] bArr) {
        a d8 = d(bArr);
        if (d8 == null) {
            return -1;
        }
        return d8.f23288b;
    }
}

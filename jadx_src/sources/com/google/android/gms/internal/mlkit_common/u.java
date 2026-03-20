package com.google.android.gms.internal.mlkit_common;

import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u extends zzau {

    /* renamed from: g  reason: collision with root package name */
    static final zzau f13017g = new u(null, new Object[0], 0);

    /* renamed from: d  reason: collision with root package name */
    private final transient Object f13018d;

    /* renamed from: e  reason: collision with root package name */
    final transient Object[] f13019e;

    /* renamed from: f  reason: collision with root package name */
    private final transient int f13020f;

    private u(Object obj, Object[] objArr, int i8) {
        this.f13018d = obj;
        this.f13019e = objArr;
        this.f13020f = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r15v0 */
    /* JADX WARN: Type inference failed for: r5v12, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v2, types: [int[]] */
    /* JADX WARN: Type inference failed for: r5v5 */
    /* JADX WARN: Type inference failed for: r6v5, types: [java.lang.Object[]] */
    public static u i(int i8, Object[] objArr, n nVar) {
        short[] sArr;
        char c9;
        char c10;
        byte[] bArr;
        byte[] bArr2;
        int i9 = i8;
        Object[] objArr2 = objArr;
        if (i9 == 0) {
            return (u) f13017g;
        }
        Object obj = null;
        int i10 = 1;
        if (i9 == 1) {
            Object obj2 = objArr2[0];
            obj2.getClass();
            Object obj3 = objArr2[1];
            obj3.getClass();
            g.a(obj2, obj3);
            return new u(null, objArr2, 1);
        }
        d.b(i9, objArr2.length >> 1, "index");
        int max = Math.max(i9, 2);
        int i11 = 1073741824;
        if (max < 751619276) {
            int highestOneBit = Integer.highestOneBit(max - 1);
            i11 = highestOneBit + highestOneBit;
            while (i11 * 0.7d < max) {
                i11 += i11;
            }
        } else if (max >= 1073741824) {
            throw new IllegalArgumentException("collection too large");
        }
        if (i9 == 1) {
            Object obj4 = objArr2[0];
            obj4.getClass();
            Object obj5 = objArr2[1];
            obj5.getClass();
            g.a(obj4, obj5);
            c9 = 1;
            c10 = 2;
        } else {
            int i12 = i11 - 1;
            char c11 = 65535;
            if (i11 <= 128) {
                byte[] bArr3 = new byte[i11];
                Arrays.fill(bArr3, (byte) -1);
                int i13 = 0;
                int i14 = 0;
                while (i13 < i9) {
                    int i15 = i14 + i14;
                    int i16 = i13 + i13;
                    Object obj6 = objArr2[i16];
                    obj6.getClass();
                    Object obj7 = objArr2[i16 ^ i10];
                    obj7.getClass();
                    g.a(obj6, obj7);
                    int a9 = i.a(obj6.hashCode());
                    while (true) {
                        int i17 = a9 & i12;
                        int i18 = bArr3[i17] & 255;
                        if (i18 == 255) {
                            bArr3[i17] = (byte) i15;
                            if (i14 < i13) {
                                objArr2[i15] = obj6;
                                objArr2[i15 ^ 1] = obj7;
                            }
                            i14++;
                        } else if (obj6.equals(objArr2[i18])) {
                            int i19 = i18 ^ 1;
                            Object obj8 = objArr2[i19];
                            obj8.getClass();
                            m mVar = new m(obj6, obj7, obj8);
                            objArr2[i19] = obj7;
                            obj = mVar;
                            break;
                        } else {
                            a9 = i17 + 1;
                        }
                    }
                    i13++;
                    i10 = 1;
                }
                if (i14 == i9) {
                    bArr = bArr3;
                } else {
                    bArr2 = new Object[]{bArr3, Integer.valueOf(i14), obj};
                    c10 = 2;
                    c9 = 1;
                    obj = bArr2;
                }
            } else if (i11 <= 32768) {
                sArr = new short[i11];
                Arrays.fill(sArr, (short) -1);
                int i20 = 0;
                for (int i21 = 0; i21 < i9; i21++) {
                    int i22 = i20 + i20;
                    int i23 = i21 + i21;
                    Object obj9 = objArr2[i23];
                    obj9.getClass();
                    Object obj10 = objArr2[i23 ^ 1];
                    obj10.getClass();
                    g.a(obj9, obj10);
                    int a10 = i.a(obj9.hashCode());
                    while (true) {
                        int i24 = a10 & i12;
                        char c12 = (char) sArr[i24];
                        if (c12 == 65535) {
                            sArr[i24] = (short) i22;
                            if (i20 < i21) {
                                objArr2[i22] = obj9;
                                objArr2[i22 ^ 1] = obj10;
                            }
                            i20++;
                        } else if (obj9.equals(objArr2[c12])) {
                            int i25 = c12 ^ 1;
                            Object obj11 = objArr2[i25];
                            obj11.getClass();
                            m mVar2 = new m(obj9, obj10, obj11);
                            objArr2[i25] = obj10;
                            obj = mVar2;
                            break;
                        } else {
                            a10 = i24 + 1;
                        }
                    }
                }
                if (i20 != i9) {
                    c10 = 2;
                    obj = new Object[]{sArr, Integer.valueOf(i20), obj};
                    c9 = 1;
                }
                bArr = sArr;
            } else {
                int i26 = 1;
                sArr = new int[i11];
                Arrays.fill((int[]) sArr, -1);
                int i27 = 0;
                int i28 = 0;
                while (i27 < i9) {
                    int i29 = i28 + i28;
                    int i30 = i27 + i27;
                    Object obj12 = objArr2[i30];
                    obj12.getClass();
                    Object obj13 = objArr2[i30 ^ i26];
                    obj13.getClass();
                    g.a(obj12, obj13);
                    int a11 = i.a(obj12.hashCode());
                    while (true) {
                        int i31 = a11 & i12;
                        ?? r15 = sArr[i31];
                        if (r15 == c11) {
                            sArr[i31] = i29;
                            if (i28 < i27) {
                                objArr2[i29] = obj12;
                                objArr2[i29 ^ 1] = obj13;
                            }
                            i28++;
                        } else if (obj12.equals(objArr2[r15])) {
                            int i32 = r15 ^ 1;
                            Object obj14 = objArr2[i32];
                            obj14.getClass();
                            m mVar3 = new m(obj12, obj13, obj14);
                            objArr2[i32] = obj13;
                            obj = mVar3;
                            break;
                        } else {
                            a11 = i31 + 1;
                            c11 = 65535;
                        }
                    }
                    i27++;
                    i26 = 1;
                    c11 = 65535;
                }
                if (i28 != i9) {
                    c9 = 1;
                    c10 = 2;
                    obj = new Object[]{sArr, Integer.valueOf(i28), obj};
                }
                bArr = sArr;
            }
            c10 = 2;
            bArr2 = bArr;
            c9 = 1;
            obj = bArr2;
        }
        if (obj instanceof Object[]) {
            Object[] objArr3 = (Object[]) obj;
            m mVar4 = (m) objArr3[c10];
            if (nVar == null) {
                throw mVar4.a();
            }
            nVar.f12997c = mVar4;
            Object obj15 = objArr3[0];
            int intValue = ((Integer) objArr3[c9]).intValue();
            objArr2 = Arrays.copyOf(objArr2, intValue + intValue);
            obj = obj15;
            i9 = intValue;
        }
        return new u(obj, objArr2, i9);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzau
    final zzan a() {
        return new t(this.f13019e, 1, this.f13020f);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzau
    final zzav d() {
        return new r(this, this.f13019e, 0, this.f13020f);
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzau
    final zzav f() {
        return new s(this, new t(this.f13019e, 0, this.f13020f));
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x009e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x009f A[RETURN] */
    @Override // com.google.android.gms.internal.mlkit_common.zzau, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object get(java.lang.Object r10) {
        /*
            r9 = this;
            java.lang.Object r0 = r9.f13018d
            java.lang.Object[] r1 = r9.f13019e
            int r2 = r9.f13020f
            r3 = 0
            if (r10 != 0) goto Lc
        L9:
            r10 = r3
            goto L9c
        Lc:
            r4 = 1
            if (r2 != r4) goto L22
            r0 = 0
            r0 = r1[r0]
            r0.getClass()
            boolean r10 = r0.equals(r10)
            if (r10 == 0) goto L9
            r10 = r1[r4]
            r10.getClass()
            goto L9c
        L22:
            if (r0 != 0) goto L25
            goto L9
        L25:
            boolean r2 = r0 instanceof byte[]
            r5 = -1
            if (r2 == 0) goto L51
            r2 = r0
            byte[] r2 = (byte[]) r2
            int r0 = r2.length
            int r6 = r0 + (-1)
            int r0 = r10.hashCode()
            int r0 = com.google.android.gms.internal.mlkit_common.i.a(r0)
        L38:
            r0 = r0 & r6
            r5 = r2[r0]
            r7 = 255(0xff, float:3.57E-43)
            r5 = r5 & r7
            if (r5 != r7) goto L41
            goto L9
        L41:
            r7 = r1[r5]
            boolean r7 = r10.equals(r7)
            if (r7 == 0) goto L4e
            r10 = r5 ^ 1
            r10 = r1[r10]
            goto L9c
        L4e:
            int r0 = r0 + 1
            goto L38
        L51:
            boolean r2 = r0 instanceof short[]
            if (r2 == 0) goto L7d
            r2 = r0
            short[] r2 = (short[]) r2
            int r0 = r2.length
            int r6 = r0 + (-1)
            int r0 = r10.hashCode()
            int r0 = com.google.android.gms.internal.mlkit_common.i.a(r0)
        L63:
            r0 = r0 & r6
            short r5 = r2[r0]
            char r5 = (char) r5
            r7 = 65535(0xffff, float:9.1834E-41)
            if (r5 != r7) goto L6d
            goto L9
        L6d:
            r7 = r1[r5]
            boolean r7 = r10.equals(r7)
            if (r7 == 0) goto L7a
            r10 = r5 ^ 1
            r10 = r1[r10]
            goto L9c
        L7a:
            int r0 = r0 + 1
            goto L63
        L7d:
            int[] r0 = (int[]) r0
            int r2 = r0.length
            int r2 = r2 + r5
            int r6 = r10.hashCode()
            int r6 = com.google.android.gms.internal.mlkit_common.i.a(r6)
        L89:
            r6 = r6 & r2
            r7 = r0[r6]
            if (r7 != r5) goto L90
            goto L9
        L90:
            r8 = r1[r7]
            boolean r8 = r10.equals(r8)
            if (r8 == 0) goto La0
            r10 = r7 ^ 1
            r10 = r1[r10]
        L9c:
            if (r10 != 0) goto L9f
            return r3
        L9f:
            return r10
        La0:
            int r6 = r6 + 1
            goto L89
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.mlkit_common.u.get(java.lang.Object):java.lang.Object");
    }

    @Override // java.util.Map
    public final int size() {
        return this.f13020f;
    }
}

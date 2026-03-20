package com.google.common.util.concurrent;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.j1;
import com.google.common.collect.k1;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CycleDetectingLockFactory {

    /* renamed from: a  reason: collision with root package name */
    private static final ConcurrentMap<Class<? extends Enum<?>>, Map<? extends Enum<?>, c>> f19658a = new k1().l().i();

    /* renamed from: b  reason: collision with root package name */
    private static final Logger f19659b = Logger.getLogger(CycleDetectingLockFactory.class.getName());

    /* renamed from: c  reason: collision with root package name */
    private static final ThreadLocal<ArrayList<c>> f19660c = new a();

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class Policies {

        /* renamed from: a  reason: collision with root package name */
        public static final Policies f19661a = new a("THROW", 0);

        /* renamed from: b  reason: collision with root package name */
        public static final Policies f19662b = new b("WARN", 1);

        /* renamed from: c  reason: collision with root package name */
        public static final Policies f19663c = new c("DISABLED", 2);

        /* renamed from: d  reason: collision with root package name */
        private static final /* synthetic */ Policies[] f19664d = c();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum a extends Policies {
            a(String str, int i8) {
                super(str, i8, null);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum b extends Policies {
            b(String str, int i8) {
                super(str, i8, null);
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum c extends Policies {
            c(String str, int i8) {
                super(str, i8, null);
            }
        }

        private Policies(String str, int i8) {
        }

        /* synthetic */ Policies(String str, int i8, a aVar) {
            this(str, i8);
        }

        private static /* synthetic */ Policies[] c() {
            return new Policies[]{f19661a, f19662b, f19663c};
        }

        public static Policies valueOf(String str) {
            return (Policies) Enum.valueOf(Policies.class, str);
        }

        public static Policies[] values() {
            return (Policies[]) f19664d.clone();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class PotentialDeadlockException extends b {

        /* renamed from: c  reason: collision with root package name */
        private final b f19665c;

        @Override // java.lang.Throwable
        public String getMessage() {
            String message = super.getMessage();
            Objects.requireNonNull(message);
            StringBuilder sb = new StringBuilder(message);
            for (Throwable th = this.f19665c; th != null; th = th.getCause()) {
                sb.append(", ");
                sb.append(th.getMessage());
            }
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends ThreadLocal<ArrayList<c>> {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* renamed from: a */
        public ArrayList<c> initialValue() {
            return j1.k(3);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b extends IllegalStateException {

        /* renamed from: a  reason: collision with root package name */
        static final StackTraceElement[] f19666a = new StackTraceElement[0];

        /* renamed from: b  reason: collision with root package name */
        static final ImmutableSet<String> f19667b = ImmutableSet.L(CycleDetectingLockFactory.class.getName(), b.class.getName(), c.class.getName());
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c {
    }
}

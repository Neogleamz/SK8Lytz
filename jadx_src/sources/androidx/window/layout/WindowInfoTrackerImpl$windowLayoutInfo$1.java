package androidx.window.layout;

import android.app.Activity;
import bk.c;
import cj.a0;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.coroutines.jvm.internal.d;
import mj.p;
@d(c = "androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1", f = "WindowInfoTrackerImpl.kt", l = {54, 55}, m = "invokeSuspend")
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class WindowInfoTrackerImpl$windowLayoutInfo$1 extends SuspendLambda implements p<c<? super WindowLayoutInfo>, fj.c<? super a0>, Object> {
    final /* synthetic */ Activity $activity;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;
    final /* synthetic */ WindowInfoTrackerImpl this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WindowInfoTrackerImpl$windowLayoutInfo$1(WindowInfoTrackerImpl windowInfoTrackerImpl, Activity activity, fj.c<? super WindowInfoTrackerImpl$windowLayoutInfo$1> cVar) {
        super(2, cVar);
        this.this$0 = windowInfoTrackerImpl;
        this.$activity = activity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: invokeSuspend$lambda-0  reason: not valid java name */
    public static final void m8invokeSuspend$lambda0(zj.c cVar, WindowLayoutInfo windowLayoutInfo) {
        kotlin.jvm.internal.p.d(windowLayoutInfo, "info");
        cVar.e(windowLayoutInfo);
    }

    public final fj.c<a0> create(Object obj, fj.c<?> cVar) {
        WindowInfoTrackerImpl$windowLayoutInfo$1 windowInfoTrackerImpl$windowLayoutInfo$1 = new WindowInfoTrackerImpl$windowLayoutInfo$1(this.this$0, this.$activity, cVar);
        windowInfoTrackerImpl$windowLayoutInfo$1.L$0 = obj;
        return windowInfoTrackerImpl$windowLayoutInfo$1;
    }

    public final Object invoke(c<? super WindowLayoutInfo> cVar, fj.c<? super a0> cVar2) {
        return create(cVar, cVar2).invokeSuspend(a0.a);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x006f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x007b A[Catch: all -> 0x009e, TRY_LEAVE, TryCatch #1 {all -> 0x009e, blocks: (B:17:0x0061, B:21:0x0073, B:23:0x007b), top: B:36:0x0061 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0092  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x0090 -> B:36:0x0061). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r10) {
        /*
            r9 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.a.e()
            int r1 = r9.label
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L39
            if (r1 == r3) goto L27
            if (r1 != r2) goto L1f
            java.lang.Object r1 = r9.L$2
            zj.d r1 = (zj.d) r1
            java.lang.Object r4 = r9.L$1
            androidx.core.util.a r4 = (androidx.core.util.a) r4
            java.lang.Object r5 = r9.L$0
            bk.c r5 = (bk.c) r5
            cj.n.b(r10)     // Catch: java.lang.Throwable -> La0
            r10 = r5
            goto L60
        L1f:
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r10.<init>(r0)
            throw r10
        L27:
            java.lang.Object r1 = r9.L$2
            zj.d r1 = (zj.d) r1
            java.lang.Object r4 = r9.L$1
            androidx.core.util.a r4 = (androidx.core.util.a) r4
            java.lang.Object r5 = r9.L$0
            bk.c r5 = (bk.c) r5
            cj.n.b(r10)     // Catch: java.lang.Throwable -> La0
            r6 = r5
            r5 = r9
            goto L73
        L39:
            cj.n.b(r10)
            java.lang.Object r10 = r9.L$0
            bk.c r10 = (bk.c) r10
            r1 = 10
            kotlinx.coroutines.channels.BufferOverflow r4 = kotlinx.coroutines.channels.BufferOverflow.b
            r5 = 4
            r6 = 0
            zj.c r1 = zj.e.b(r1, r4, r6, r5, r6)
            androidx.window.layout.b r4 = new androidx.window.layout.b
            r4.<init>()
            androidx.window.layout.WindowInfoTrackerImpl r5 = r9.this$0
            androidx.window.layout.WindowBackend r5 = androidx.window.layout.WindowInfoTrackerImpl.access$getWindowBackend$p(r5)
            android.app.Activity r6 = r9.$activity
            androidx.profileinstaller.g r7 = androidx.profileinstaller.g.f6495a
            r5.registerLayoutChangeCallback(r6, r7, r4)
            zj.d r1 = r1.iterator()     // Catch: java.lang.Throwable -> La0
        L60:
            r5 = r9
        L61:
            r5.L$0 = r10     // Catch: java.lang.Throwable -> L9e
            r5.L$1 = r4     // Catch: java.lang.Throwable -> L9e
            r5.L$2 = r1     // Catch: java.lang.Throwable -> L9e
            r5.label = r3     // Catch: java.lang.Throwable -> L9e
            java.lang.Object r6 = r1.a(r5)     // Catch: java.lang.Throwable -> L9e
            if (r6 != r0) goto L70
            return r0
        L70:
            r8 = r6
            r6 = r10
            r10 = r8
        L73:
            java.lang.Boolean r10 = (java.lang.Boolean) r10     // Catch: java.lang.Throwable -> L9e
            boolean r10 = r10.booleanValue()     // Catch: java.lang.Throwable -> L9e
            if (r10 == 0) goto L92
            java.lang.Object r10 = r1.next()     // Catch: java.lang.Throwable -> L9e
            androidx.window.layout.WindowLayoutInfo r10 = (androidx.window.layout.WindowLayoutInfo) r10     // Catch: java.lang.Throwable -> L9e
            r5.L$0 = r6     // Catch: java.lang.Throwable -> L9e
            r5.L$1 = r4     // Catch: java.lang.Throwable -> L9e
            r5.L$2 = r1     // Catch: java.lang.Throwable -> L9e
            r5.label = r2     // Catch: java.lang.Throwable -> L9e
            java.lang.Object r10 = r6.emit(r10, r5)     // Catch: java.lang.Throwable -> L9e
            if (r10 != r0) goto L90
            return r0
        L90:
            r10 = r6
            goto L61
        L92:
            androidx.window.layout.WindowInfoTrackerImpl r10 = r5.this$0
            androidx.window.layout.WindowBackend r10 = androidx.window.layout.WindowInfoTrackerImpl.access$getWindowBackend$p(r10)
            r10.unregisterLayoutChangeCallback(r4)
            cj.a0 r10 = cj.a0.a
            return r10
        L9e:
            r10 = move-exception
            goto La2
        La0:
            r10 = move-exception
            r5 = r9
        La2:
            androidx.window.layout.WindowInfoTrackerImpl r0 = r5.this$0
            androidx.window.layout.WindowBackend r0 = androidx.window.layout.WindowInfoTrackerImpl.access$getWindowBackend$p(r0)
            r0.unregisterLayoutChangeCallback(r4)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}

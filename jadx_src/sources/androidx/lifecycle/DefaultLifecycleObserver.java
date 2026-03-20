package androidx.lifecycle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface DefaultLifecycleObserver extends i {
    default void onCreate(j jVar) {
        kotlin.jvm.internal.p.e(jVar, "owner");
    }

    default void onDestroy(j jVar) {
        kotlin.jvm.internal.p.e(jVar, "owner");
    }

    default void onPause(j jVar) {
        kotlin.jvm.internal.p.e(jVar, "owner");
    }

    default void onResume(j jVar) {
        kotlin.jvm.internal.p.e(jVar, "owner");
    }

    default void onStart(j jVar) {
        kotlin.jvm.internal.p.e(jVar, "owner");
    }

    default void onStop(j jVar) {
        kotlin.jvm.internal.p.e(jVar, "owner");
    }
}

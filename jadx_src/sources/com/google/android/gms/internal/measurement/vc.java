package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class vc implements sc {
    private static final n6<String> A;
    private static final n6<String> B;
    private static final n6<Long> C;
    private static final n6<String> D;
    private static final n6<String> E;
    private static final n6<String> F;
    private static final n6<String> G;
    private static final n6<Long> H;
    private static final n6<Long> I;
    private static final n6<Long> J;
    private static final n6<Long> K;
    private static final n6<Long> L;
    private static final n6<Long> M;
    private static final n6<Long> N;
    private static final n6<Long> O;
    private static final n6<Long> P;
    private static final n6<Long> Q;
    private static final n6<Long> R;
    private static final n6<Long> S;
    private static final n6<Long> T;
    private static final n6<Long> U;
    private static final n6<Long> V;
    private static final n6<Long> W;
    private static final n6<Long> X;
    private static final n6<String> Y;
    private static final n6<Long> Z;

    /* renamed from: a  reason: collision with root package name */
    private static final n6<Long> f12596a;

    /* renamed from: a0  reason: collision with root package name */
    private static final n6<String> f12597a0;

    /* renamed from: b  reason: collision with root package name */
    private static final n6<Long> f12598b;

    /* renamed from: c  reason: collision with root package name */
    private static final n6<Long> f12599c;

    /* renamed from: d  reason: collision with root package name */
    private static final n6<Long> f12600d;

    /* renamed from: e  reason: collision with root package name */
    private static final n6<String> f12601e;

    /* renamed from: f  reason: collision with root package name */
    private static final n6<String> f12602f;

    /* renamed from: g  reason: collision with root package name */
    private static final n6<String> f12603g;

    /* renamed from: h  reason: collision with root package name */
    private static final n6<Long> f12604h;

    /* renamed from: i  reason: collision with root package name */
    private static final n6<String> f12605i;

    /* renamed from: j  reason: collision with root package name */
    private static final n6<Long> f12606j;

    /* renamed from: k  reason: collision with root package name */
    private static final n6<Long> f12607k;

    /* renamed from: l  reason: collision with root package name */
    private static final n6<Long> f12608l;

    /* renamed from: m  reason: collision with root package name */
    private static final n6<Long> f12609m;

    /* renamed from: n  reason: collision with root package name */
    private static final n6<Long> f12610n;

    /* renamed from: o  reason: collision with root package name */
    private static final n6<Long> f12611o;

    /* renamed from: p  reason: collision with root package name */
    private static final n6<Long> f12612p;
    private static final n6<Long> q;

    /* renamed from: r  reason: collision with root package name */
    private static final n6<Long> f12613r;

    /* renamed from: s  reason: collision with root package name */
    private static final n6<Long> f12614s;

    /* renamed from: t  reason: collision with root package name */
    private static final n6<Long> f12615t;

    /* renamed from: u  reason: collision with root package name */
    private static final n6<Long> f12616u;

    /* renamed from: v  reason: collision with root package name */
    private static final n6<String> f12617v;

    /* renamed from: w  reason: collision with root package name */
    private static final n6<Long> f12618w;

    /* renamed from: x  reason: collision with root package name */
    private static final n6<Long> f12619x;

    /* renamed from: y  reason: collision with root package name */
    private static final n6<Long> f12620y;

    /* renamed from: z  reason: collision with root package name */
    private static final n6<Long> f12621z;

    static {
        v6 e8 = new v6(o6.a("com.google.android.gms.measurement")).f().e();
        f12596a = e8.b("measurement.ad_id_cache_time", 10000L);
        f12598b = e8.b("measurement.app_uninstalled_additional_ad_id_cache_time", 3600000L);
        f12599c = e8.b("measurement.max_bundles_per_iteration", 100L);
        f12600d = e8.b("measurement.config.cache_time", 86400000L);
        f12601e = e8.c("measurement.log_tag", "FA");
        f12602f = e8.c("measurement.config.url_authority", "app-measurement.com");
        f12603g = e8.c("measurement.config.url_scheme", "https");
        f12604h = e8.b("measurement.upload.debug_upload_interval", 1000L);
        f12605i = e8.c("measurement.rb.attribution.event_params", "value|currency");
        f12606j = e8.b("measurement.id.rb.attribution.app_allowlist", 0L);
        f12607k = e8.b("measurement.lifetimevalue.max_currency_tracked", 4L);
        f12608l = e8.b("measurement.dma_consent.max_daily_dcu_realtime_events", 1L);
        f12609m = e8.b("measurement.upload.max_event_parameter_value_length", 100L);
        f12610n = e8.b("measurement.store.max_stored_events_per_app", 100000L);
        f12611o = e8.b("measurement.experiment.max_ids", 50L);
        f12612p = e8.b("measurement.audience.filter_result_max_count", 200L);
        q = e8.b("measurement.upload.max_item_scoped_custom_parameters", 27L);
        f12613r = e8.b("measurement.rb.attribution.client.min_ad_services_version", 7L);
        f12614s = e8.b("measurement.alarm_manager.minimum_interval", 60000L);
        f12615t = e8.b("measurement.upload.minimum_delay", 500L);
        f12616u = e8.b("measurement.monitoring.sample_period_millis", 86400000L);
        f12617v = e8.c("measurement.rb.attribution.app_allowlist", "com.labpixies.flood,com.sofascore.results,games.spearmint.triplecrush,com.block.juggle,io.supercent.linkedcubic,com.cdtg.gunsound,com.corestudios.storemanagementidle,com.cdgames.fidget3d,io.supercent.burgeridle,io.supercent.pizzaidle,jp.ne.ibis.ibispaintx.app,com.dencreak.dlcalculator,com.ebay.kleinanzeigen,de.wetteronline.wetterapp,com.game.shape.shift,com.champion.cubes,bubbleshooter.orig,com.wolt.android,com.master.hotelmaster,com.games.bus.arrival,com.playstrom.dop2,com.huuuge.casino.slots,com.ig.spider.fighting,com.jura.coloring.page,com.rikkogame.ragdoll2,com.ludo.king,com.sigma.prank.sound.haircut,com.crazy.block.robo.monster.cliffs.craft,com.fugo.wow,com.maps.locator.gps.gpstracker.phone,com.gamovation.tileclub,com.pronetis.ironball2,com.meesho.supply,pdf.pdfreader.viewer.editor.free,com.dino.race.master,com.ig.moto.racing,com.callapp.contacts,ai.photo.enhancer.photoclear,com.duolingo,com.candle.magic_piano,com.free.vpn.super.hotspot.open,sg.bigo.live,com.cdg.tictactoe,com.zhiliaoapp.musically.go,com.wildspike.wormszone,com.mast.status.video.edit,com.vyroai.photoeditorone,com.pujiagames.deeeersimulator,com.superbinogo.jungleboyadventure,com.trustedapp.pdfreaderpdfviewer,com.artimind.aiart.artgenerator.artavatar,de.cellular.ottohybrid");
        f12618w = e8.b("measurement.upload.realtime_upload_interval", 10000L);
        f12619x = e8.b("measurement.upload.refresh_blacklisted_config_interval", 604800000L);
        f12620y = e8.b("measurement.config.cache_time.service", 3600000L);
        f12621z = e8.b("measurement.service_client.idle_disconnect_millis", 5000L);
        A = e8.c("measurement.log_tag.service", "FA-SVC");
        B = e8.c("measurement.sgtm.app_allowlist", BuildConfig.FLAVOR);
        C = e8.b("measurement.upload.stale_data_deletion_interval", 86400000L);
        D = e8.c("measurement.rb.attribution.uri_authority", "google-analytics.com");
        E = e8.c("measurement.rb.attribution.uri_path", "privacy-sandbox/register-app-conversion");
        F = e8.c("measurement.rb.attribution.query_parameters_to_remove", BuildConfig.FLAVOR);
        G = e8.c("measurement.rb.attribution.uri_scheme", "https");
        H = e8.b("measurement.sdk.attribution.cache.ttl", 604800000L);
        I = e8.b("measurement.redaction.app_instance_id.ttl", 7200000L);
        J = e8.b("measurement.upload.backoff_period", 43200000L);
        K = e8.b("measurement.upload.initial_upload_delay_time", 15000L);
        L = e8.b("measurement.upload.interval", 3600000L);
        M = e8.b("measurement.upload.max_bundle_size", 65536L);
        N = e8.b("measurement.upload.max_bundles", 100L);
        O = e8.b("measurement.upload.max_conversions_per_day", 500L);
        P = e8.b("measurement.upload.max_error_events_per_day", 1000L);
        Q = e8.b("measurement.upload.max_events_per_bundle", 1000L);
        R = e8.b("measurement.upload.max_events_per_day", 100000L);
        S = e8.b("measurement.upload.max_public_events_per_day", 50000L);
        T = e8.b("measurement.upload.max_queue_time", 2419200000L);
        U = e8.b("measurement.upload.max_realtime_events_per_day", 10L);
        V = e8.b("measurement.upload.max_batch_size", 65536L);
        W = e8.b("measurement.upload.retry_count", 6L);
        X = e8.b("measurement.upload.retry_time", 1800000L);
        Y = e8.c("measurement.upload.url", "https://app-measurement.com/a");
        Z = e8.b("measurement.upload.window_interval", 3600000L);
        f12597a0 = e8.c("measurement.rb.attribution.user_properties", "_npa,npa");
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long A() {
        return f12618w.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long B() {
        return M.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String C() {
        return B.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long D() {
        return f12619x.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long E() {
        return f12614s.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String F() {
        return f12603g.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long G() {
        return R.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String H() {
        return Y.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long I() {
        return f12615t.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String J() {
        return f12605i.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long K() {
        return K.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long L() {
        return S.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String M() {
        return f12597a0.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long N() {
        return Z.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String O() {
        return F.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long P() {
        return H.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String Q() {
        return f12602f.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long R() {
        return f12613r.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long S() {
        return Q.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String T() {
        return G.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long U() {
        return N.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String V() {
        return D.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long W() {
        return f12621z.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long X() {
        return X.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String Y() {
        return E.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long Z() {
        return C.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long a() {
        return f12599c.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long b() {
        return f12600d.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long c() {
        return W.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long d() {
        return f12604h.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long e() {
        return f12607k.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long f() {
        return f12609m.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long g() {
        return f12608l.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long i() {
        return f12611o.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long j() {
        return f12610n.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long k() {
        return J.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long l() {
        return q.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long m() {
        return P.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long n() {
        return U.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long o() {
        return I.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long t() {
        return V.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long u() {
        return f12612p.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long v() {
        return O.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long w() {
        return f12616u.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final String x() {
        return f12617v.f();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long y() {
        return L.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long z() {
        return T.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long zza() {
        return f12596a.f().longValue();
    }

    @Override // com.google.android.gms.internal.measurement.sc
    public final long zzb() {
        return f12598b.f().longValue();
    }
}

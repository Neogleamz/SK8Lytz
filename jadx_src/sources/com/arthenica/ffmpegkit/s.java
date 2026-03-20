package com.arthenica.ffmpegkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class s {

    /* renamed from: a  reason: collision with root package name */
    private static final List<String> f8822a;

    static {
        ArrayList arrayList = new ArrayList();
        f8822a = arrayList;
        arrayList.add("dav1d");
        arrayList.add("fontconfig");
        arrayList.add("freetype");
        arrayList.add("fribidi");
        arrayList.add("gmp");
        arrayList.add("gnutls");
        arrayList.add("kvazaar");
        arrayList.add("mp3lame");
        arrayList.add("libass");
        arrayList.add("iconv");
        arrayList.add("libilbc");
        arrayList.add("libtheora");
        arrayList.add("libvidstab");
        arrayList.add("libvorbis");
        arrayList.add("libvpx");
        arrayList.add("libwebp");
        arrayList.add("libxml2");
        arrayList.add("opencore-amr");
        arrayList.add("openh264");
        arrayList.add("openssl");
        arrayList.add("opus");
        arrayList.add("rubberband");
        arrayList.add("sdl2");
        arrayList.add("shine");
        arrayList.add("snappy");
        arrayList.add("soxr");
        arrayList.add("speex");
        arrayList.add("srt");
        arrayList.add("tesseract");
        arrayList.add("twolame");
        arrayList.add("x264");
        arrayList.add("x265");
        arrayList.add("xvid");
        arrayList.add("zimg");
    }

    public static List<String> a() {
        String nativeBuildConf = AbiDetect.getNativeBuildConf();
        ArrayList arrayList = new ArrayList();
        for (String str : f8822a) {
            if (!nativeBuildConf.contains("enable-" + str)) {
                if (nativeBuildConf.contains("enable-lib" + str)) {
                }
            }
            arrayList.add(str);
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public static String b() {
        boolean z4;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        List<String> a9 = a();
        boolean contains = a9.contains("speex");
        boolean contains2 = a9.contains("fribidi");
        boolean contains3 = a9.contains("gnutls");
        boolean contains4 = a9.contains("xvid");
        boolean z12 = true;
        boolean z13 = false;
        if (contains && contains2) {
            z4 = false;
            z8 = false;
            z10 = false;
            if (contains4) {
                z9 = false;
                z11 = false;
            } else {
                z9 = false;
                z11 = false;
                z13 = true;
                z12 = false;
            }
        } else {
            if (contains) {
                z8 = true;
                z4 = false;
                z10 = false;
            } else {
                if (contains2) {
                    z4 = true;
                    z8 = false;
                } else if (contains4) {
                    if (contains3) {
                        z10 = true;
                        z4 = false;
                        z8 = false;
                        z9 = false;
                        z12 = z9;
                        z11 = z12;
                    } else {
                        z11 = true;
                        z4 = false;
                        z8 = false;
                        z10 = false;
                        z9 = false;
                        z12 = false;
                    }
                } else if (contains3) {
                    z9 = true;
                    z4 = false;
                    z8 = false;
                    z10 = false;
                    z12 = false;
                    z11 = z12;
                } else {
                    z4 = false;
                    z8 = false;
                }
                z10 = z8;
            }
            z9 = z10;
            z12 = z9;
            z11 = z12;
        }
        return z12 ? (a9.contains("dav1d") && a9.contains("fontconfig") && a9.contains("freetype") && a9.contains("fribidi") && a9.contains("gmp") && a9.contains("gnutls") && a9.contains("kvazaar") && a9.contains("mp3lame") && a9.contains("libass") && a9.contains("iconv") && a9.contains("libilbc") && a9.contains("libtheora") && a9.contains("libvidstab") && a9.contains("libvorbis") && a9.contains("libvpx") && a9.contains("libwebp") && a9.contains("libxml2") && a9.contains("opencore-amr") && a9.contains("opus") && a9.contains("shine") && a9.contains("snappy") && a9.contains("soxr") && a9.contains("speex") && a9.contains("twolame") && a9.contains("x264") && a9.contains("x265") && a9.contains("xvid") && a9.contains("zimg")) ? "full-gpl" : "custom" : z13 ? (a9.contains("dav1d") && a9.contains("fontconfig") && a9.contains("freetype") && a9.contains("fribidi") && a9.contains("gmp") && a9.contains("gnutls") && a9.contains("kvazaar") && a9.contains("mp3lame") && a9.contains("libass") && a9.contains("iconv") && a9.contains("libilbc") && a9.contains("libtheora") && a9.contains("libvorbis") && a9.contains("libvpx") && a9.contains("libwebp") && a9.contains("libxml2") && a9.contains("opencore-amr") && a9.contains("opus") && a9.contains("shine") && a9.contains("snappy") && a9.contains("soxr") && a9.contains("speex") && a9.contains("twolame") && a9.contains("zimg")) ? "full" : "custom" : z4 ? (a9.contains("dav1d") && a9.contains("fontconfig") && a9.contains("freetype") && a9.contains("fribidi") && a9.contains("kvazaar") && a9.contains("libass") && a9.contains("iconv") && a9.contains("libtheora") && a9.contains("libvpx") && a9.contains("libwebp") && a9.contains("snappy") && a9.contains("zimg")) ? "video" : "custom" : z8 ? (a9.contains("mp3lame") && a9.contains("libilbc") && a9.contains("libvorbis") && a9.contains("opencore-amr") && a9.contains("opus") && a9.contains("shine") && a9.contains("soxr") && a9.contains("speex") && a9.contains("twolame")) ? "audio" : "custom" : z10 ? (a9.contains("gmp") && a9.contains("gnutls") && a9.contains("libvidstab") && a9.contains("x264") && a9.contains("x265") && a9.contains("xvid")) ? "https-gpl" : "custom" : z9 ? (a9.contains("gmp") && a9.contains("gnutls")) ? "https" : "custom" : z11 ? (a9.contains("libvidstab") && a9.contains("x264") && a9.contains("x265") && a9.contains("xvid")) ? "min-gpl" : "custom" : a9.size() == 0 ? "min" : "custom";
    }
}

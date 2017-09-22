/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.funjava.functional.applicative;

import xyz.funjava.functional.Function10;
import xyz.funjava.functional.Function11;
import xyz.funjava.functional.Function12;
import xyz.funjava.functional.Function13;
import xyz.funjava.functional.Function14;
import xyz.funjava.functional.Function15;
import xyz.funjava.functional.Function16;
import xyz.funjava.functional.Function17;
import xyz.funjava.functional.Function18;
import xyz.funjava.functional.Function19;
import xyz.funjava.functional.Function2;
import xyz.funjava.functional.Function20;
import xyz.funjava.functional.Function21;
import xyz.funjava.functional.Function22;
import xyz.funjava.functional.Function23;
import xyz.funjava.functional.Function24;
import xyz.funjava.functional.Function25;
import xyz.funjava.functional.Function26;
import xyz.funjava.functional.Function3;
import xyz.funjava.functional.Function4;
import xyz.funjava.functional.Function5;
import xyz.funjava.functional.Function6;
import xyz.funjava.functional.Function7;
import xyz.funjava.functional.Function8;
import xyz.funjava.functional.Function9;

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface Applicative<AP extends Applicative, T> {

    <S> Applicative<AP, S> applyTo(Applicative<AP, Function<T, S>> ap);

    <S> Applicative<AP, S> pure(S t);

    public static <AP extends Applicative<AP, ?>, A, B, RESULT> Applicative<AP, RESULT> apply(Function2<A, B, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b) {
        return b.applyTo(a.applyTo(a.pure((A aa) -> fun.curried().apply(aa))));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, RESULT> Applicative<AP, RESULT> apply(Function3<A, B, C, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c) {
        return c.applyTo(Applicative.apply((A aa, B bb) -> fun.curried().apply(aa).apply(bb), a, b));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, RESULT> Applicative<AP, RESULT> apply(Function4<A, B, C, D, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d) {
        return d.applyTo(Applicative.apply((A aa, B bb, C cc) -> fun.curried().apply(aa).apply(bb).apply(cc), a, b, c));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, RESULT> Applicative<AP, RESULT> apply(Function5<A, B, C, D, E, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e) {
        return e.applyTo(Applicative.apply((A aa, B bb, C cc, D dd) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd), a, b, c, d));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, RESULT> Applicative<AP, RESULT> apply(Function6<A, B, C, D, E, F, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f) {
        return f.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee), a, b, c, d, e));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, RESULT> Applicative<AP, RESULT> apply(Function7<A, B, C, D, E, F, G, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g) {
        return g.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff), a, b, c, d, e, f));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, RESULT> Applicative<AP, RESULT> apply(Function8<A, B, C, D, E, F, G, H, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h) {
        return h.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg), a, b, c, d, e, f, g));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, RESULT> Applicative<AP, RESULT> apply(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i) {
        return i.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh), a, b, c, d, e, f, g, h));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, RESULT> Applicative<AP, RESULT> apply(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j) {
        return j.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii), a, b, c, d, e, f, g, h, i));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, RESULT> Applicative<AP, RESULT> apply(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k) {
        return k.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj), a, b, c, d, e, f, g, h, i, j));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, RESULT> Applicative<AP, RESULT> apply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l) {
        return l.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk), a, b, c, d, e, f, g, h, i, j, k));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> Applicative<AP, RESULT> apply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m) {
        return m.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll), a, b, c, d, e, f, g, h, i, j, k, l));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> Applicative<AP, RESULT> apply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n) {
        return n.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm), a, b, c, d, e, f, g, h, i, j, k, l, m));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> Applicative<AP, RESULT> apply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o) {
        return o.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn), a, b, c, d, e, f, g, h, i, j, k, l, m, n));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> Applicative<AP, RESULT> apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p) {
        return p.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> Applicative<AP, RESULT> apply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q) {
        return q.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> Applicative<AP, RESULT> apply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r) {
        return r.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> Applicative<AP, RESULT> apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s) {
        return s.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> Applicative<AP, RESULT> apply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t) {
        return t.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> Applicative<AP, RESULT> apply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u) {
        return u.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> Applicative<AP, RESULT> apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v) {
        return v.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> Applicative<AP, RESULT> apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v, Applicative<AP, W> w) {
        return w.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> Applicative<AP, RESULT> apply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v, Applicative<AP, W> w, Applicative<AP, X> x) {
        return x.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> Applicative<AP, RESULT> apply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v, Applicative<AP, W> w, Applicative<AP, X> x, Applicative<AP, Y> y) {
        return y.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x));
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> Applicative<AP, RESULT> apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v, Applicative<AP, W> w, Applicative<AP, X> x, Applicative<AP, Y> y, Applicative<AP, Z> z) {
        return z.applyTo(Applicative.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx, Y yy) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx).apply(yy), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y));
    }

}

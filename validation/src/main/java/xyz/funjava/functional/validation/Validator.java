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

package xyz.funjava.functional.validation;

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
import xyz.funjava.functional.higherkinded.applicative.Applicative;
import xyz.funjava.functional.higherkinded.monoid.Monoid;
import xyz.funjava.functional.validation.Validation.Failure;
import xyz.funjava.functional.validation.Validation.Success;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Carlos Sierra Andrés
 */
public interface Validator<T, R, F extends Monoid<F>>
	extends Function<T, Validation<R, F>>, Applicative<Validator, R> {

	Validation<R, F> validate(T input);

	@Override
	default <S> Applicative<Validator, S> pure(S t) {
		return Validator.just(() -> t);
	}

	default Validator<T, R, F> and(Validator<T, R, F> other) {
		return input ->
			Validation.apply(
				(x, y) -> x, validate(input), other.validate(input));
	}

	@Override
	default Validation<R, F> apply(T t) {
		return validate(t);
	}

	default <S> Validator<T, S, F> map(Function<R, S> fun) {

		return input -> {
			Validation<R, F> result = validate(input);

			return (Validation<S, F>)result.map(fun);
		};
	}

	static <T, F extends Monoid<F>> Validator<T, T, F> partial(
		Validator<T, ?, F> validator) {

		return input -> validator.validate(input).map(ign -> input);
	}

	@SafeVarargs
	static <T, F extends Monoid<F>> Validator<T, T, F> partials(
		Validator<T, ?, F> ... validators) {

		return Arrays.stream(validators).map(Validator::partial).reduce(
			(Validation::just), Validator::and);
	}

	@Override
	default <S> Validator<T, S, F> applyTo(
		Applicative<Validator, Function<R, S>> ap) {

		return input -> validate(input).applyTo(
            ((Validator<T, Function<R, S>, F>) ap).validate(input));
	}

	default public <S> Validator<T, S, F> flatMap(
		Function<R, Validator<T, S, F>> fun) {

		return input -> validate(input).flatMap(
			fun.andThen(v -> v.validate(input)));
	}

	default <S> Validator<T, S, F> compose(Validator<R, S, F> validator) {
		return input -> (Validation<S, F>) validate(input).flatMap(
			validator::validate);
	}

	default <C, F2 extends Monoid<F2>> Validator<C, R, F2> adapt(
		Function<C, T> val, Function<F, F2> errors) {

		return input -> validate(val.apply(input)).mapFailures(errors);
	}

	default <C, F2 extends Monoid<F2>> Validator<C, R, F2> flatAdapt(
		Function<C, Validation<T, F>> val, Function<F, F2> errors) {

		return input -> val.apply(input).flatMap(this::validate).mapFailures(
			errors);
	}

	static <T, F extends Monoid<F>> Validator<T, T, F> predicate(
		Predicate<T> predicate, Function<T, F> error) {

		return input -> {
			if (predicate.test(input)) {
				return new Success<>(input);
			}
			else {
				return new Failure<>(error.apply(input));
			}
		};
	}
	
	public static <T, R, F extends Monoid<F>> Validator<T, R, F> just(
		Supplier<R> r) {

		return __ -> Validation.just(r.get());
	}

	public static <A, B, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function2<A, B, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b) {
		return b.applyTo(a.applyTo(just(() -> (A aa) -> fun.curried().apply(aa))));
	}

	public static <A, B, C, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function3<A, B, C, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c) {
		return c.applyTo(Validator.apply((A aa, B bb) -> fun.curried().apply(aa).apply(bb), a, b));
	}

	public static <A, B, C, D, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function4<A, B, C, D, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d) {
		return d.applyTo(Validator.apply((A aa, B bb, C cc) -> fun.curried().apply(aa).apply(bb).apply(cc), a, b, c));
	}

	public static <A, B, C, D, E, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function5<A, B, C, D, E, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e) {
		return e.applyTo(Validator.apply((A aa, B bb, C cc, D dd) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd), a, b, c, d));
	}

	public static <A, B, C, D, E, F, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function6<A, B, C, D, E, F, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f) {
		return f.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee), a, b, c, d, e));
	}

	public static <A, B, C, D, E, F, G, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function7<A, B, C, D, E, F, G, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g) {
		return g.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff), a, b, c, d, e, f));
	}

	public static <A, B, C, D, E, F, G, H, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function8<A, B, C, D, E, F, G, H, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h) {
		return h.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg), a, b, c, d, e, f, g));
	}

	public static <A, B, C, D, E, F, G, H, I, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i) {
		return i.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh), a, b, c, d, e, f, g, h));
	}

	public static <A, B, C, D, E, F, G, H, I, J, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j) {
		return j.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii), a, b, c, d, e, f, g, h, i));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k) {
		return k.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj), a, b, c, d, e, f, g, h, i, j));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l) {
		return l.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk), a, b, c, d, e, f, g, h, i, j, k));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m) {
		return m.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll), a, b, c, d, e, f, g, h, i, j, k, l));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n) {
		return n.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm), a, b, c, d, e, f, g, h, i, j, k, l, m));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o) {
		return o.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn), a, b, c, d, e, f, g, h, i, j, k, l, m, n));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p) {
		return p.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q) {
		return q.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r) {
		return r.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s) {
		return s.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t) {
		return t.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u) {
		return u.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v) {
		return v.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v, Validator<VAL, W, FAILURE> w) {
		return w.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v, Validator<VAL, W, FAILURE> w, Validator<VAL, X, FAILURE> x) {
		return x.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v, Validator<VAL, W, FAILURE> w, Validator<VAL, X, FAILURE> x, Validator<VAL, Y, FAILURE> y) {
		return y.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, VAL, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v, Validator<VAL, W, FAILURE> w, Validator<VAL, X, FAILURE> x, Validator<VAL, Y, FAILURE> y, Validator<VAL, Z, FAILURE> z) {
		return z.applyTo(Validator.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx, Y yy) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx).apply(yy), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y));
	}

//	static Validator<String, String, FAILURE> hasLength(int length) {
//		return predicate(
//			input -> input.length() == length,
//			input -> input + " must have " + length + " letters");
//	}
//
//	static Validator<String, String, FAILURE> longerThan(int length) {
//		return predicate(
//			input -> input.length() > length,
//			input -> input + " must have " + length + " letters");
//	}
//
//	Validator<String, String, FAILURE> isANumber = predicate(
//		input -> {
//			try {
//				Integer.parseInt(input);
//
//				return true;
//			}
//			catch (Exception e) {
//				return false;
//			}
//		}, input -> input + " is not a number");
//
//	static Validator<Integer, Integer, FAILURE> greaterThan(int min) {
//		return predicate(
//			input -> input > min,
//			input -> input + " must be greater than" + min
//		);
//	}
//
//	static Validator<Integer, Integer, FAILURE> lowerThan(int max) {
//		return predicate(
//			input -> input < max,
//			input -> input + " should be lower than " + max
//		);
//	}
//
//	static Validator<String, String, FAILURE> startsWith(String prefix) {
//		return predicate(
//			input -> input.startsWith(prefix),
//			input -> input + " should start with " + prefix
//		);
//	}
//
//	static Validator<String, String, FAILURE> endsWith(String suffix) {
//		return predicate(
//			input -> input.endsWith(suffix),
//			input -> input + " should start with " + suffix
//		);
//	}
//
//	static <T> Validator<Optional<T>, Optional<T>> ifPresent(
//		Validator<T, T, FAILURE> validator) {
//
//		return (Optional<T> input) -> {
//			if (!input.isPresent()) {
//				return new Success<>(input);
//			}
//			else {
//				return (Validation<Optional<T>>)
//					validator.validate(input.get()).map(Optional::of);
//			}
//		};
//	}
//
//	Validator<String, Integer, FAILURE> safeInt = isANumber.map(Integer::parseInt);
//
//	static <T> Validator<Optional<T>, T> notEmpty() {
//		return input -> {
//			if (input.isPresent()) {
//				return new Success<>(input.get());
//			}
//			else {
//				return new Failure<>(
//					Collections.singleton("Input is empty"));
//			}
//		};
//	}

}

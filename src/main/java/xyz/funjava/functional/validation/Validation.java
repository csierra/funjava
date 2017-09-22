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

import xyz.funjava.functional.CheckedFunction10;
import xyz.funjava.functional.CheckedFunction11;
import xyz.funjava.functional.CheckedFunction12;
import xyz.funjava.functional.CheckedFunction13;
import xyz.funjava.functional.CheckedFunction14;
import xyz.funjava.functional.CheckedFunction15;
import xyz.funjava.functional.CheckedFunction16;
import xyz.funjava.functional.CheckedFunction17;
import xyz.funjava.functional.CheckedFunction18;
import xyz.funjava.functional.CheckedFunction19;
import xyz.funjava.functional.CheckedFunction2;
import xyz.funjava.functional.CheckedFunction20;
import xyz.funjava.functional.CheckedFunction21;
import xyz.funjava.functional.CheckedFunction22;
import xyz.funjava.functional.CheckedFunction23;
import xyz.funjava.functional.CheckedFunction24;
import xyz.funjava.functional.CheckedFunction25;
import xyz.funjava.functional.CheckedFunction26;
import xyz.funjava.functional.CheckedFunction3;
import xyz.funjava.functional.CheckedFunction4;
import xyz.funjava.functional.CheckedFunction5;
import xyz.funjava.functional.CheckedFunction6;
import xyz.funjava.functional.CheckedFunction7;
import xyz.funjava.functional.CheckedFunction8;
import xyz.funjava.functional.CheckedFunction9;
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
import xyz.funjava.functional.applicative.Applicative;
import xyz.funjava.functional.monoid.Monoid;

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface Validation<T, F extends Monoid<F>>
	extends Applicative<Validation, T> {

	public boolean isSuccess();

	public T get();

	public F failures();

	public <S> Validation<S, F> map(Function<T, S> fun);

	public <F2 extends Monoid<F2>> Validation<T, F2> mapFailures(
		Function<F, F2> fun);

	public <S> Validation<S, F> flatMap(Function<T, Validation<S, F>> fun);

	@Override
	public <S> Validation<S, F> applyTo(
		Applicative<Validation, Function<T, S>> fun);

	default <S> Validation<S, F> pure(S s) {
		return new Success<>(s);
	}

	class Success<T, F extends Monoid<F>> implements Validation<T, F> {

		private final T _t;

		public Success(T t) {
			_t = t;
		}

		public <S> Validation<S, F> map(Function<T, S> fun) {
			return new Success<>(fun.apply(_t));
		}

		@Override
		public <F2 extends Monoid<F2>> Validation<T, F2> mapFailures(
			Function<F, F2> fun) {

			return (Validation<T, F2>)this;
		}

		@Override
		public <S> Validation<S, F> applyTo(
			Applicative<Validation, Function<T, S>> fun) {

			return ((Validation<Function<T, S>, F>)fun).map(f -> f.apply(_t));
		}

		@Override
		public String toString() {
			return "Success {" + _t + '}';
		}

		public <S> Validation<S, F> flatMap(Function<T, Validation<S, F>> fun) {
			return fun.apply(_t);
		}

		@Override
		public boolean isSuccess() {
			return true;
		}

		@Override
		public T get() {
			return _t;
		}

		@Override
		public F failures() {
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Success<?, ?> success = (Success<?, ?>) o;

			return _t.equals(success._t);
		}

		@Override
		public int hashCode() {
			return _t.hashCode();
		}

	}

	class Failure<T, F extends Monoid<F>> implements Validation<T, F> {

		private F _failure;

		public Failure(F failure) {
			_failure = failure;
		}

		@Override
		public <S> Validation<S, F> map(
			Function<T, S> fun) {

			return (Validation)this;
		}

		@Override
		public <F2 extends Monoid<F2>> Validation<T, F2> mapFailures(
			Function<F, F2> fun) {

			return new Failure<>(fun.apply(_failure));
		}

		@Override
		public <S> Validation<S, F> applyTo(
			Applicative<Validation, Function<T, S>> ap) {

			if (ap instanceof Failure) {
				return new Failure<>((F)_failure.mappend(((Failure) ap)._failure));
			}

			else {
				return (Validation)this;
			}
		}

		@Override
		public String toString() {
			return "Failure {" + "Reasons: " + _failure.toString() + '}';
		}

		@Override
		public <S> Validation<S, F> flatMap(
			Function<T, Validation<S, F>> fun) {

			return (Validation<S, F>)this;
		}

		@Override
		public boolean isSuccess() {
			return false;
		}

		@Override
		public T get() {
			throw new IllegalStateException();
		}

		@Override
		public F failures() {
			return _failure;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Failure<?, ?> failure = (Failure<?, ?>) o;

			return _failure.equals(failure._failure);
		}

		@Override
		public int hashCode() {
			return _failure.hashCode();
		}

	}

	static <T, F extends Monoid<F>> Validation<T, F> just(T t) {
		return new Success<>(t);
	}

	public static <A, B, RESULT, FAILURE extends Monoid<FAILURE>> Function2<A, B, Validation<RESULT, FAILURE>> wrap(CheckedFunction2<A, B, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b) -> {
			try {
				return new Success<>(fun.apply(a, b));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, RESULT> Function2<A, B, RESULT> ignore(CheckedFunction2<A, B, RESULT> fun) {
		return (a, b) -> {
			try {
				return fun.apply(a, b);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function2<A, B, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b);
	}

	public static <A, B, C, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function3<A, B, C, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c);
	}

	public static <A, B, C, D, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function4<A, B, C, D, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d);
	}

	public static <A, B, C, D, E, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function5<A, B, C, D, E, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e);
	}

	public static <A, B, C, D, E, F, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function6<A, B, C, D, E, F, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f);
	}

	public static <A, B, C, D, E, F, G, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function7<A, B, C, D, E, F, G, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g);
	}

	public static <A, B, C, D, E, F, G, H, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function8<A, B, C, D, E, F, G, H, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h);
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i);
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y, Validation<Z, FAILURE> z) {
		return (Validation<RESULT, FAILURE>)Applicative.apply(fun, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
	}

	public static <A, B, C, RESULT, FAILURE extends Monoid<FAILURE>> Function3<A, B, C, Validation<RESULT, FAILURE>> wrap(CheckedFunction3<A, B, C, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c) -> {
			try {
				return new Success<>(fun.apply(a, b, c));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, RESULT> Function3<A, B, C, RESULT> ignore(CheckedFunction3<A, B, C, RESULT> fun) {
		return (a, b, c) -> {
			try {
				return fun.apply(a, b, c);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, RESULT, FAILURE extends Monoid<FAILURE>> Function4<A, B, C, D, Validation<RESULT, FAILURE>> wrap(CheckedFunction4<A, B, C, D, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, RESULT> Function4<A, B, C, D, RESULT> ignore(CheckedFunction4<A, B, C, D, RESULT> fun) {
		return (a, b, c, d) -> {
			try {
				return fun.apply(a, b, c, d);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, RESULT, FAILURE extends Monoid<FAILURE>> Function5<A, B, C, D, E, Validation<RESULT, FAILURE>> wrap(CheckedFunction5<A, B, C, D, E, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, RESULT> Function5<A, B, C, D, E, RESULT> ignore(CheckedFunction5<A, B, C, D, E, RESULT> fun) {
		return (a, b, c, d, e) -> {
			try {
				return fun.apply(a, b, c, d, e);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, RESULT, FAILURE extends Monoid<FAILURE>> Function6<A, B, C, D, E, F, Validation<RESULT, FAILURE>> wrap(CheckedFunction6<A, B, C, D, E, F, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, RESULT> Function6<A, B, C, D, E, F, RESULT> ignore(CheckedFunction6<A, B, C, D, E, F, RESULT> fun) {
		return (a, b, c, d, e, f) -> {
			try {
				return fun.apply(a, b, c, d, e, f);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, RESULT, FAILURE extends Monoid<FAILURE>> Function7<A, B, C, D, E, F, G, Validation<RESULT, FAILURE>> wrap(CheckedFunction7<A, B, C, D, E, F, G, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, RESULT> Function7<A, B, C, D, E, F, G, RESULT> ignore(CheckedFunction7<A, B, C, D, E, F, G, RESULT> fun) {
		return (a, b, c, d, e, f, g) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, RESULT, FAILURE extends Monoid<FAILURE>> Function8<A, B, C, D, E, F, G, H, Validation<RESULT, FAILURE>> wrap(CheckedFunction8<A, B, C, D, E, F, G, H, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, RESULT> Function8<A, B, C, D, E, F, G, H, RESULT> ignore(CheckedFunction8<A, B, C, D, E, F, G, H, RESULT> fun) {
		return (a, b, c, d, e, f, g, h) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT, FAILURE extends Monoid<FAILURE>> Function9<A, B, C, D, E, F, G, H, I, Validation<RESULT, FAILURE>> wrap(CheckedFunction9<A, B, C, D, E, F, G, H, I, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT> Function9<A, B, C, D, E, F, G, H, I, RESULT> ignore(CheckedFunction9<A, B, C, D, E, F, G, H, I, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT, FAILURE extends Monoid<FAILURE>> Function10<A, B, C, D, E, F, G, H, I, J, Validation<RESULT, FAILURE>> wrap(CheckedFunction10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT> Function10<A, B, C, D, E, F, G, H, I, J, RESULT> ignore(CheckedFunction10<A, B, C, D, E, F, G, H, I, J, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT, FAILURE extends Monoid<FAILURE>> Function11<A, B, C, D, E, F, G, H, I, J, K, Validation<RESULT, FAILURE>> wrap(CheckedFunction11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT> Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> ignore(CheckedFunction11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT, FAILURE extends Monoid<FAILURE>> Function12<A, B, C, D, E, F, G, H, I, J, K, L, Validation<RESULT, FAILURE>> wrap(CheckedFunction12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT> Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> ignore(CheckedFunction12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT, FAILURE extends Monoid<FAILURE>> Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, Validation<RESULT, FAILURE>> wrap(CheckedFunction13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> ignore(CheckedFunction13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT, FAILURE extends Monoid<FAILURE>> Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, Validation<RESULT, FAILURE>> wrap(CheckedFunction14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> ignore(CheckedFunction14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT, FAILURE extends Monoid<FAILURE>> Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Validation<RESULT, FAILURE>> wrap(CheckedFunction15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> ignore(CheckedFunction15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT, FAILURE extends Monoid<FAILURE>> Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Validation<RESULT, FAILURE>> wrap(CheckedFunction16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> ignore(CheckedFunction16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT, FAILURE extends Monoid<FAILURE>> Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Validation<RESULT, FAILURE>> wrap(CheckedFunction17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> ignore(CheckedFunction17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT, FAILURE extends Monoid<FAILURE>> Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Validation<RESULT, FAILURE>> wrap(CheckedFunction18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> ignore(CheckedFunction18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT, FAILURE extends Monoid<FAILURE>> Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Validation<RESULT, FAILURE>> wrap(CheckedFunction19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> ignore(CheckedFunction19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT, FAILURE extends Monoid<FAILURE>> Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Validation<RESULT, FAILURE>> wrap(CheckedFunction20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> ignore(CheckedFunction20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT, FAILURE extends Monoid<FAILURE>> Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Validation<RESULT, FAILURE>> wrap(CheckedFunction21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> ignore(CheckedFunction21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT, FAILURE extends Monoid<FAILURE>> Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Validation<RESULT, FAILURE>> wrap(CheckedFunction22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> ignore(CheckedFunction22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT, FAILURE extends Monoid<FAILURE>> Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, Validation<RESULT, FAILURE>> wrap(CheckedFunction23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> ignore(CheckedFunction23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT, FAILURE extends Monoid<FAILURE>> Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Validation<RESULT, FAILURE>> wrap(CheckedFunction24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> ignore(CheckedFunction24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT, FAILURE extends Monoid<FAILURE>> Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Validation<RESULT, FAILURE>> wrap(CheckedFunction25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> ignore(CheckedFunction25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT, FAILURE extends Monoid<FAILURE>> Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, Validation<RESULT, FAILURE>> wrap(CheckedFunction26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> ignore(CheckedFunction26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

}

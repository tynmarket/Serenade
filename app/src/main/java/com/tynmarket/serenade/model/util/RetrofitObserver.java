package com.tynmarket.serenade.model.util;

import com.tynmarket.serenade.core.Callback;
import com.tynmarket.serenade.core.Result;
import com.tynmarket.serenade.core.TwitterException;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;

/**
 * Created by tynmarket on 2018/03/21.
 */

public class RetrofitObserver<T> {
    private final Call<T> call;

    public RetrofitObserver(Call<T> call) {
        this.call = call;
    }

    public static <T> RetrofitObserver<T> create(Call<T> call) {
        return new RetrofitObserver(call);
    }

    public void subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        call.enqueue(new Callback<T>() {
            @Override
            public void success(Result<T> result) {
                try {
                    onNext.accept(result.data);
                } catch (Exception ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }

            @Override
            public void failure(TwitterException e) {
                try {
                    onError.accept(e);
                } catch (Exception ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(new CompositeException(e, ex));
                }
            }
        });
    }
}

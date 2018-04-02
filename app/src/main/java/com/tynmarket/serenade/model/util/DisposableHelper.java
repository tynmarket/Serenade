package com.tynmarket.serenade.model.util;

import android.util.SparseArray;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by tynmarket on 2018/04/02.
 */
public class DisposableHelper {
    private static SparseArray<CompositeDisposable> disposables = new SparseArray<>();

    public static void add(Disposable disposable, int sectionNumber) {
        CompositeDisposable compositeDisposable = disposables.get(sectionNumber);

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            disposables.put(sectionNumber, compositeDisposable);
        }

        compositeDisposable.add(disposable);
    }

    public static void clear(int sectionNumber) {
        CompositeDisposable compositeDisposable = disposables.get(sectionNumber);

        if (compositeDisposable == null) {
            return;
        }
        compositeDisposable.clear();
    }
}

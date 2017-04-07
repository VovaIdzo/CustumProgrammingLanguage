package com.idzodev.callback;

/**
 * Created by vladimir on 23.05.16.
 */
public interface DataCallback<T> {
    void execute(T item);
}

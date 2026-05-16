package com.mycompany.advanced_project.service;

public interface AsyncCallBack<T> {
    void onSuccess(T result);
    void onFailure(Throwable e);
}

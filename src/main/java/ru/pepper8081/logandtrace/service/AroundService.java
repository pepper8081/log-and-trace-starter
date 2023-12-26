package ru.pepper8081.logandtrace.service;

public interface AroundService<R> {

    R doBefore(R r);

    R doAfter(R r);

    R doOnException(R r);

    R doFinally(R r);
}

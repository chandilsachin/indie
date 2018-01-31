package com.sachinchandil.indie.util.generalisedClasses;

public interface ReturnableCallback<R,P> {

    R callback(P value);
}

package com.naosim.rtm.lib;

import java.util.Optional;

interface Either<L, R> {
    default Optional<L> left() { return Optional.empty(); }
    default Optional<R> right() { return Optional.empty(); }
    static <L, R> Either<L, R> left(L left) {
        Optional<L> leftValue = Optional.ofNullable(left);
        if(!leftValue.isPresent()) throw new NullPointerException("left value is null");
        return new Either<L, R>() {
            @Override
            public Optional<L> left() {
                return leftValue;
            }
        };
    }

    static <L, R> Either<L, R> right(R right) {
        Optional<R> rightValue = Optional.ofNullable(right);
        if(!rightValue.isPresent()) throw new NullPointerException("right value is null");
        return new Either<L, R>() {
            @Override
            public Optional<R> right() {
                return rightValue;
            }
        };
    }
}
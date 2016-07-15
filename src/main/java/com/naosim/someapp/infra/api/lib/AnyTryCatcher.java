package com.naosim.someapp.infra.api.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public final class AnyTryCatcher {
    private final List<Exception> exceptionList = new ArrayList<>();
    public void run(Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            exceptionList.add(e);
        }
    }

    public <T> Optional<T> get(Supplier<T> getter) {
        try {
            return Optional.of(getter.get());
        } catch (Exception e) {
            exceptionList.add(e);
            return Optional.empty();
        }
    }

    public List<Exception> getExceptionList() {
        return exceptionList;
    }
}

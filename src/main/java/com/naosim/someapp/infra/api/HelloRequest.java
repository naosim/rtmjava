package com.naosim.someapp.infra.api;

import com.naosim.someapp.domain.タスク名;
import com.naosim.someapp.domain.タスク消化予定日;
import com.naosim.someapp.domain.タスク消化予定日NotExist;
import com.naosim.someapp.domain.タスク消化予定日Optional;
import com.naosim.someapp.infra.api.lib.*;

import java.time.LocalDate;
import java.util.Optional;

import static com.naosim.someapp.infra.api.lib.RequestParamRegexEnum.date;

public class HelloRequest extends ApiRequestParams<HelloRequest> {
    public final RequiredParam<タスク名> name = new RequiredParam<>("name", タスク名::new);
    public final RequestParam<タスク消化予定日Optional> enddate = new OptionParam<タスク消化予定日Optional>(
            "enddate",
            date,
            s -> Optional.of(s).map(LocalDate::parse).map(タスク消化予定日::new).get(),
            タスク消化予定日NotExist::new
    );
}
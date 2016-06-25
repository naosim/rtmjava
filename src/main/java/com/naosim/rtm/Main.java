package com.naosim.rtm;

import com.naosim.rtm.domain.model.*;
import com.naosim.rtm.domain.model.auth.CheckedToken;
import com.naosim.rtm.domain.model.auth.Token;
import com.naosim.rtm.infra.datasource.AuthRepository;
import com.naosim.rtm.infra.datasource.RtmRepositoryNet;
import com.naosim.rtm.infra.datasource.RtmRepositoryNetFactory;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String... args) throws InterruptedException {
        RtmRepositoryNet rtmRepository = new RtmRepositoryNetFactory().create();
        AuthRepository authRepository = new AuthRepository();
//
//        FrobUnAuthed frob = rtmRepository.getFrob();
//        String authUrl = rtmRepository.getAuthUrl(frob);
//        System.out.print("Please access: ");
//        System.out.println(authUrl);
//        Thread.sleep(20 * 1000);
//
//        System.out.println("restart");
//
//        FrobAuthed frobAuthed = new FrobAuthed(frob.getRtmParamValue());
//        GetTokenResponse getTokenResponse = rtmRepository.getToken(frobAuthed);
//        System.out.print(getTokenResponse.getCheckedToken().getRtmParamValue());

        Token token = authRepository.getStoredTokenByUserId(new UserId("naosim")).get();
        Optional<CheckedToken> chekcedToken = rtmRepository.checkToken(token);
        CheckedToken checkedToken = chekcedToken.orElseThrow(() -> new RuntimeException("tokenが無効です"));
        System.out.println(chekcedToken);

        List<TaskSeriesListEntity> taskSeriesListEntityList = rtmRepository.getTaskList(token, new Filter("(status:incomplete)or(completedAfter:25/06/2016)"));
        taskSeriesListEntityList.forEach(l -> {
            System.out.println("list: " + l.getTaskSeriesListName().map(v -> v.getValue()).orElse(""));
            l.getTaskSeriesEntityList().forEach(s -> {
                System.out.println(" " + s.getTaskSeriesName().getValue() + " " + s.getTaskEntity().getTaskDateTimes().getTaskCompletedDateTimes().map(v -> v.getDateTime().toString()).orElse(""));
            });
        });

    }

}

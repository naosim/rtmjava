package com.naosim.someapp;

import com.naosim.rtm.domain.model.auth.CheckedToken;
import com.naosim.rtm.domain.model.auth.Token;
import com.naosim.rtm.domain.model.auth.UserId;
import com.naosim.rtm.domain.model.task.Parse;
import com.naosim.rtm.domain.model.task.TaskSeriesEntity;
import com.naosim.rtm.domain.model.task.TaskSeriesName;
import com.naosim.rtm.domain.model.timeline.TimelineId;
import com.naosim.rtm.domain.model.timeline.TransactionalResponse;
import com.naosim.someapp.infra.datasource.AuthRepository;
import com.naosim.rtm.infra.datasource.RtmRepositoryNet;
import com.naosim.rtm.infra.datasource.RtmRepositoryNetFactory;

import java.util.Optional;

public class Main {
    public static void main(String... args) throws InterruptedException {
        RtmRepositoryNet rtmRepository = new RtmRepositoryNetFactory().create(new RtmApiConfigImpl());
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

        TimelineId timelineId = rtmRepository.createTimeline(token);
        TransactionalResponse<TaskSeriesEntity> res = rtmRepository.addTask(token, timelineId, new TaskSeriesName("barbar"), Optional.empty());

        System.out.println(res.getTransaction().getTransactionId().getRtmParamValue());
        System.out.println(res.getResponse().getTaskSeriesName().getRtmParamValue());

//        List<TaskSeriesListEntity> taskSeriesListEntityList = rtmRepository.getTaskList(token, new Filter("(status:incomplete)or(completedAfter:25/06/2016)"));
//        taskSeriesListEntityList.forEach(l -> {
//            System.out.println("list: " + l.getTaskSeriesListName().map(v -> v.getValue()).orElse(""));
//            l.getTaskSeriesEntityList().forEach(s -> {
//                System.out.println(" " + s.getTaskSeriesName().getValue() + " " + s.getTaskEntity().getTaskDateTimes().getTaskCompletedDateTimes().map(v -> v.getDateTime().toString()).orElse(""));
//            });
//        });

    }

}

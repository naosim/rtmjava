package com.naosim.someapp;

import com.naosim.rtm.domain.model.Filter;
import com.naosim.rtm.domain.model.auth.*;
import com.naosim.rtm.domain.model.task.*;
import com.naosim.rtm.domain.model.timeline.TimelineId;
import com.naosim.rtm.domain.model.timeline.TransactionalResponse;
import com.naosim.rtm.domain.repository.RtmRepository;
import com.naosim.rtm.infra.datasource.RtmRepositoryNet;
import com.naosim.rtm.infra.datasource.RtmRepositoryNetFactory;
import com.naosim.someapp.infra.datasource.AuthRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String... args) throws InterruptedException {
        RtmRepositoryNet rtmRepository = new RtmRepositoryNetFactory().create(new RtmApiConfigImpl());
        AuthRepository authRepository = new AuthRepository();

//        login(rtmRepository);

        Token token = getTokenAtLocal(authRepository, rtmRepository);

        addTask(rtmRepository, token);
//        showTasks(rtmRepository, token);
//        start(rtmRepository, token);

    }

    public static Token login(RtmRepositoryNet rtmRepository) throws InterruptedException {
        FrobUnAuthed frob = rtmRepository.getFrob();
        String authUrl = rtmRepository.getAuthUrl(frob);
        System.out.print("Please access: ");
        System.out.println(authUrl);
        Thread.sleep(20 * 1000);

        System.out.println("restart");

        FrobAuthed frobAuthed = new FrobAuthed(frob.getRtmParamValue());
        GetTokenResponse getTokenResponse = rtmRepository.getToken(frobAuthed);
        System.out.print(getTokenResponse.getCheckedToken().getRtmParamValue());
        return getTokenResponse.getCheckedToken();
    }

    public static Token getTokenAtLocal(AuthRepository authRepository, RtmRepositoryNet rtmRepository) {
        Token token = authRepository.getStoredTokenByUserId(new UserId("naosim")).get();
        Optional<CheckedToken> chekcedToken = rtmRepository.checkToken(token);
        CheckedToken checkedToken = chekcedToken.orElseThrow(() -> new RuntimeException("tokenが無効です"));
        System.out.println(chekcedToken);
        return checkedToken;
    }

    public static void addTask(RtmRepositoryNet rtmRepository, Token token) {
        TimelineId timelineId = rtmRepository.createTimeline(token);
        TransactionalResponse<TaskSeriesEntity> res = rtmRepository.addTask(token, timelineId, new TaskSeriesName("barbar4"));
        res = rtmRepository.updateStartDateTime(token, timelineId, res.getResponse().getTaskIdSet(), Optional.of(new TaskStartDateTime(LocalDateTime.of(2016, 7, 5, 12, 0))));

        System.out.println(res.getTransaction().getTransactionId().getRtmParamValue());
        System.out.println(res.getResponse().getTaskSeriesName().getRtmParamValue());
        System.out.println(res.getResponse().getTaskEntity().getTaskDateTimes().getTaskStartDateTime().get().getRtmParamValue());
    }

    public static void showTasks(RtmRepository rtmRepository, Token token) {
        List<TaskSeriesListEntity> taskSeriesListEntityList = rtmRepository.getTaskList(token, new Filter("(status:incomplete)or(completedAfter:25/06/2016)"));
        taskSeriesListEntityList.forEach(l -> {
            System.out.println("list: " + l.getTaskSeriesListName().map(v -> v.getValue()).orElse(""));
            l.getTaskSeriesEntityList().forEach(Main::printTaskSeriesEntity);
        });
    }

    public static void printTaskSeriesEntity(TaskSeriesEntity taskSeriesEntity) {
        if(taskSeriesEntity.getTaskEntity().getTaskDateTimes().getTaskCompletedDateTime().isPresent()) {
            System.out.println("      " + taskSeriesEntity.getTaskSeriesName().getRtmParamValue() + " " + taskSeriesEntity.getTaskEntity().getTaskDateTimes().getTaskCompletedDateTime().map(v -> v.getDateTime().toString()).orElse(""));
        } else {
            System.out.println(
                    String.format(
                            "%s %s %s %s",
                            taskSeriesEntity.getTaskSeriesName().getRtmParamValue(),
                            taskSeriesEntity.getTaskIdSet().getListId().getValue(),
                            taskSeriesEntity.getTaskIdSet().getTaskSeriesId().getValue(),
                            taskSeriesEntity.getTaskIdSet().getTaskId().getValue()
                    )
            );
        }

    }

    public static void complete(RtmRepositoryNet rtmRepository, Token token) {
        //9905184 294849864 505043442
        TimelineId timelineId = rtmRepository.createTimeline(token);
        TransactionalResponse<TaskSeriesEntity> res = rtmRepository.completeTask(token, timelineId, new TaskIdSet(
                new TaskSeriesListId("9905184"),
                new TaskSeriesId("294849864"),
                new TaskId("505043442")
        ));

        System.out.println(res.getResponse().getTaskSeriesName().getRtmParamValue());

    }

    public static void start(RtmRepositoryNet rtmRepository, Token token) {
        //9905184 294850724 505046081
        TimelineId timelineId = rtmRepository.createTimeline(token);
        TransactionalResponse<TaskSeriesEntity> res = rtmRepository.updateStartDateTime(token, timelineId, new TaskIdSet(
                new TaskSeriesListId("9905184"),
                new TaskSeriesId("294850724"),
                new TaskId("505046081")
        ),
                Optional.of(new TaskStartDateTime(LocalDateTime.now()))
        );

        System.out.println(res.getResponse().getTaskEntity().getTaskDateTimes().getTaskStartDateTime().map(TaskStartDateTime::getDateTime));

    }
}

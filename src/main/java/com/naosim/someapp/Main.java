package com.naosim.someapp;

import com.google.gson.Gson;
import com.naosim.rtm.domain.model.Filter;
import com.naosim.rtm.domain.model.auth.*;
import com.naosim.rtm.domain.model.task.*;
import com.naosim.rtm.domain.model.timeline.TimelineId;
import com.naosim.rtm.domain.model.timeline.TransactionalResponse;
import com.naosim.rtm.domain.repository.RtmRepository;
import com.naosim.rtm.infra.datasource.RtmRepositoryNet;
import com.naosim.rtm.infra.datasource.RtmRepositoryNetFactory;
import com.naosim.someapp.domain.タスク名;
import com.naosim.someapp.domain.タスク消化予定日;
import com.naosim.someapp.infra.MapConverter;
import com.naosim.someapp.infra.RepositoryFactory;
import com.naosim.someapp.infra.api.HelloApi;
import com.naosim.someapp.infra.api.auth.checktoken.AuthCheckTokenApi;
import com.naosim.someapp.infra.api.auth.getauthurl.AuthGetAuthUrlApi;
import com.naosim.someapp.infra.api.auth.gettoken.AuthGetTokenApi;
import com.naosim.someapp.infra.api.task.add.TaskAddApi;
import com.naosim.someapp.infra.api.lib.Api;
import com.naosim.someapp.infra.api.task.complete.TaskCompleteApi;
import com.naosim.someapp.infra.api.task.complete.TaskDeleteApi;
import com.naosim.someapp.infra.api.task.list.TaskListApi;
import com.naosim.someapp.infra.datasource.AuthRepository;
import com.naosim.someapp.infra.datasource.タスクRepositoryWithRTM;
import spark.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static spark.Spark.*;
import static spark.utils.StringUtils.isEmpty;

public class Main {
    public static void main(String[] args) {
        RepositoryFactory repositoryFactory = new RepositoryFactory();
        Gson gson = new Gson();
        Api[] apiList = {
                new HelloApi(),
                new TaskAddApi(repositoryFactory),
                new TaskListApi(repositoryFactory),
                new TaskCompleteApi(repositoryFactory),
                new TaskDeleteApi(repositoryFactory),
                
                new AuthCheckTokenApi(repositoryFactory),
                new AuthGetAuthUrlApi(repositoryFactory),
                new AuthGetTokenApi(repositoryFactory),

        };
        Stream.of(apiList).forEach(api -> get("/api/v1/json" + api.getPath(), api::router, gson::toJson));
        Stream.of(apiList).forEach(api -> get("/api/v1/jsonp" + api.getPath(), (req, res) -> {
            String callbackFunction = req.queryParams("callback");
            if(isEmpty(callbackFunction)) {
                throw new RuntimeException("callback not found");
            }
            try {
                Object obj = api.router(req, res);
                return "" + callbackFunction + "(" + gson.toJson(obj) + ");";
            } catch(RuntimeException e) {
                res.status(500);
                return "" + callbackFunction + "(" + gson.toJson(new MapConverter().apiErrorResult(400, e.getMessage())) + ");";
            }
        }));


        exception(RuntimeException.class, (exception, request, response) -> {
            // Handle the exception here
            response.status(500);
            response.body(gson.toJson(new MapConverter().apiErrorResult(400, exception.getMessage())));
        });
    }

    public static void main2(String... args) throws InterruptedException {
        RtmRepositoryNet rtmRepository = new RtmRepositoryNetFactory().create(new RtmApiConfigImpl());
        AuthRepository authRepository = new AuthRepository();

//        login(rtmRepository);

        Token token = getTokenAtLocal(authRepository, rtmRepository);
//        addTask(rtmRepository, token);
        タスク追加(token, rtmRepository);
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
//        res = rtmRepository.updateStartDateTime(token, timelineId, res.getResponse().getTaskIdSet(), Optional.of(new TaskStartDateTime(LocalDateTime.of(2016, 7, 8, 12, 0))));
        res = rtmRepository.updateDueDateTime(token, timelineId, res.getResponse().getTaskIdSet(), Optional.of(new TaskDueDateTime(LocalDateTime.of(2016, 7, 8, 12, 0))));

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

    public static void タスク追加(Token token, RtmRepositoryNet rtmRepository) {
        タスクRepositoryWithRTM タスクRepositoryWithRTM = new タスクRepositoryWithRTM(token, rtmRepository);
        タスクRepositoryWithRTM.追加(new タスク名("hoge"), new タスク消化予定日(LocalDate.of(2016,7,7)));
    }

    public static <T> T getRequiredValue(String key, Function<String, T> convert, QueryParamsMap queryParamsMap) {
        if(isEmpty(queryParamsMap.value(key))) {
            throw new RuntimeException("required: " + key);
        }
        return convert.apply(queryParamsMap.value(key));
    }

    //--いかUC
    /*
    checktoken(token)
    getloginurl(): flob, url
    gettolen(token)
    addtask(token, name, startdate)
    donetask(token, taskid)
    setstartdate(token, startdate)


     */



    public static class JsonpApiWrapper implements Route {

        @Override
        public Object handle(Request request, Response response) throws Exception {
            return null;
        }
    }
}

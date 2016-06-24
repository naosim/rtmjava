package com.naosim.rtm;

import com.naosim.rtm.domain.model.FrobAuthed;
import com.naosim.rtm.domain.model.FrobUnAuthed;
import com.naosim.rtm.domain.model.GetTokenResponse;
import com.naosim.rtm.domain.model.Token;
import com.naosim.rtm.infra.datasource.RtmRepositoryNet;

public class Main {
    public static void main(String... args) throws InterruptedException {
        RtmRepositoryNet rtmRepository = new RtmRepositoryNet();

        FrobUnAuthed frob = rtmRepository.getFrob();
        String authUrl = rtmRepository.getAuthUrl(frob);
        System.out.print("Please access: ");
        System.out.println(authUrl);
        Thread.sleep(20 * 1000);

        System.out.println("restart");

        FrobAuthed frobAuthed = new FrobAuthed(frob.getRtmParamValue());
        GetTokenResponse getTokenResponse = rtmRepository.getToken(frobAuthed);
        System.out.print(getTokenResponse.getToken().getRtmParamValue());
    }

}

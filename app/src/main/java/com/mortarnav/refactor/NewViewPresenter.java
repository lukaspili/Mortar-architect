//package com.mortarnav.refactor;
//
//import com.mortarnav.deps.RestClient;
//
//import architect.robot.NavigationParam;
//import architect.robot.NavigationResult;
//import architect.robot.ScreenData;
//import mortar.ViewPresenter;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class NewViewPresenter extends ViewPresenter<NewView> {
//
//    private final RestClient restClient;
//
//    @ScreenData
//    private final State state;
//
//    @NavigationParam
//    private String username;
//
//    @NavigationParam
//    private int userId;
//
//    @NavigationResult
//    private final String result;
//
//    public NewViewPresenter(RestClient restClient, String username, State state, String result) {
//        this.restClient = restClient;
//        this.username = username;
//        this.state = state;
//        this.result = result;
//    }
//
//    public NewViewPresenter(RestClient restClient, int userId, State state, String result) {
//        this.restClient = restClient;
//        this.userId = userId;
//        this.state = state;
//        this.result = result;
//    }
//
//    public static class State {
//
//
//    }
//}

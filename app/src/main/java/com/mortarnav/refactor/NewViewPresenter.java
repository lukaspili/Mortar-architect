package com.mortarnav.refactor;

import com.mortarnav.deps.RestClient;

import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NewViewPresenter extends ViewPresenter<NewView> {

    private final RestClient restClient;
    private final String username;
    private final State state;
    private final String result;

    public NewViewPresenter(RestClient restClient, String username, State state, String result) {
        this.restClient = restClient;
        this.username = username;
        this.state = state;
        this.result = result;
    }

    public static class State {

    }
}

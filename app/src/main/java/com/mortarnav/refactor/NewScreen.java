package com.mortarnav.refactor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mortarnav.DaggerScope;
import com.mortarnav.deps.RestClient;

import architect.ScreenPath;
import architect.screen.ReceivesNavigationResult;
import dagger.Provides;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NewScreen implements ScreenPath, ReceivesNavigationResult<String> {

    private String result;
    private String username;
    private NewViewPresenter.State presenterState;

    public NewScreen(String username) {
        this.username = username;
        this.presenterState = new NewViewPresenter.State();
    }

    @Override
    public View createView(Context context, ViewGroup parent) {
        return null;
    }

    @Override
    public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {

    }

    @dagger.Module
    public class Module {

        @Provides
        @DaggerScope(NewViewPresenter.class)
        public NewViewPresenter providesPresenter(RestClient restClient) {
            return new NewViewPresenter(restClient, username, presenterState, result);
        }
    }


    // Navigation result

    @Override
    public void onReceiveNavigationResult(String result) {
        this.result = result;
    }
}

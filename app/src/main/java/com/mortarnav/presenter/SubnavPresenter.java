package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivity2;
import com.mortarnav.presenter.scope.path.SubnavPagePath;
import com.mortarnav.view.SubnavView;

import javax.inject.Inject;

import architect.Navigator;
import architect.TransitionsMapping;
import architect.autopath.AutoPath;
import architect.autostack.AutoStack;
import architect.transition.LateralViewTransition;
import autodagger.AutoComponent;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStack(
        component = @AutoComponent(dependencies = MainActivity2.class),
        path = @AutoPath(withView = SubnavView.class)
)
@DaggerScope(SubnavPresenter.class)
public class SubnavPresenter extends ViewPresenter<SubnavView> {

    private Navigator navigator;

    @Inject
    public SubnavPresenter() {
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        navigator = Navigator.find(getView().getContext());
        if (navigator == null) {
            Timber.d("create navigator");
            navigator = Navigator.create(MortarScope.getScope(getView().getContext()));
            navigator.transitions().register(new TransitionsMapping().byDefault(new LateralViewTransition()));
        }

        navigator.delegate().onCreate(null, savedInstanceState, getView().navigatorView, new SubnavPagePath("INITIAL"));
        navigator.delegate().onStart();
    }

    @Override
    protected void onSave(Bundle outState) {
        navigator.delegate().onSaveInstanceState(outState);
    }

    @Override
    public void dropView(SubnavView view) {
        navigator.delegate().onStop();
        navigator.delegate().onDestroy();
        navigator = null;

        super.dropView(view);
    }

    public boolean backPressed() {
        return navigator.delegate().onBackPressed();
    }
}

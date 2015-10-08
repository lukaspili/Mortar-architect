package com.mortarnav.mvp.slides;

import com.mortarnav.DaggerScope;
import com.mortarnav.StandardAutoComponent;
import com.mortarnav.mvp.banner.BannerScreen;

import architect.robot.AutoScreen;
import architect.robot.ContainsSubscreen;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
//@AutoScreen(
//        component = @AutoComponent(includes = StandardAutoComponent.class),
//        pathView = SlidesView.class,
//        subScreens = {
//                @ContainsSubscreen(type = BannerScreen.class, name = "banner1")
//        }
//)
//@DaggerScope(SlidesPresenter.class)
public class SlidesPresenter extends ViewPresenter {


}

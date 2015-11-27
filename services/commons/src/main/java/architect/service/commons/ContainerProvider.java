package architect.service.commons;

import android.content.Context;
import android.view.View;

import architect.History;
import architect.Processing;

public interface ContainerProvider {

    Context getContext(View container, History.Entry entry, Processing processing);
}
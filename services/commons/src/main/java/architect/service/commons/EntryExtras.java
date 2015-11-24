package architect.service.commons;

import android.os.Bundle;

import architect.History;

/**
 * Created by lukasz on 23/11/15.
 */
public class EntryExtras {

    final static String TRANSITION = "transition";

    public static EntryExtras.Builder builder() {
        return new EntryExtras.Builder();
    }

    public static EntryExtras from(History.Entry entry) {
        if (entry.extras == null) {
            return new EntryExtras(null);
        }

        String transition = entry.extras.getString(TRANSITION, null);
        return new EntryExtras(transition);
    }

    public final String transition;

    private EntryExtras(String transition) {
        this.transition = transition;
    }

    public static class Builder {
        public String transition;

        public Builder transition(String transition) {
            this.transition = transition;
            return this;
        }

        public EntryExtras toExtras() {
            return new EntryExtras(transition);
        }

        public Bundle toBundle() {
            if (transition == null) {
                return null;
            }

            Bundle bundle = new Bundle();
            bundle.putString(TRANSITION, transition);
            return bundle;
        }
    }
}

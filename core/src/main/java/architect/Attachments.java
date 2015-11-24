package architect;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukasz on 23/11/15.
 */
public class Attachments {

    private final Services services;
    private final List<Attachment> attached = new ArrayList<>();

    public Attachments() {
        services = null;
    }

    Attachments(Services services) {
        this.services = services;
    }

    public Attachments attach(String service, Object object) {
        attached.add(new Attachment(service, new WeakReference<>(object)));
        return this;
    }

    void take(Attachments attachments) {
        Preconditions.checkArgument(attached.isEmpty(), "Attachments must be empty");
        attached.addAll(attachments.attached);

        for (Attachments.Attachment a : attached) {
            services.get(a.service).getPresenter().takeContainer(a.container.get());
        }
    }

    void drop() {
        for (Attachments.Attachment a : attached) {
            services.get(a.service).getPresenter().dropContainer(a.container.get());
        }

        attached.clear();
    }

    public static class Attachment {
        final String service;
        final WeakReference<Object> container;

        public Attachment(String service, WeakReference<Object> container) {
            this.service = service;
            this.container = container;
        }
    }
}

package architect.mortar;

import architect.History;
import mortar.MortarScope;

class ScopedEntry {
    final History.Entry entry;
    final MortarScope scope;

    public ScopedEntry(History.Entry entry, MortarScope scope) {
        Preconditions.checkNotNull(entry, "Entry null");
        Preconditions.checkNotNull(scope, "Scope null");
        this.entry = entry;
        this.scope = scope;
    }
}
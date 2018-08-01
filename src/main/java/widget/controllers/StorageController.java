package widget.controllers;

import widget.Widget.Parentable;

public class StorageController implements Parentable {

    private RootController parent;

    @Override
    public void setParent(RootController parent) {
        this.parent = parent;
    }
}


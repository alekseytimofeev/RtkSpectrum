package widget.controllers;

import widget.Parentable;

public class ProcessingController implements Parentable {
    private RootController parent;

    @Override
    public void setParent(RootController parent) {
        this.parent = parent;
    }
}


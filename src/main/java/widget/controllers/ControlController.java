package widget.controllers;

import widget.Widget.Parentable;

public class ControlController implements Parentable
{
    private RootController parent;

    @Override
    public void setParent(RootController parent) {
        this.parent = parent;
    }
}

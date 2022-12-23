module irie.composer.components.tabpanel;

import irie.composer.common;

struct TabComponent {
    const(char)* title;
    Texture* icon;
    Component component;
}

class TabPanel : Component {

    Texture logo;
    public TabComponent[] components;

    public this() {
        logo = Resource.LoadIcon("tray/whiteicon.png");
        this.background = Theme.GetColor("tabpanel-background");
        this.foreground = Theme.GetColor("tabpanel-empty-logo");
    }

    public override void Draw() {
        DrawRectangle(dimension.x, dimension.y, dimension.width, dimension.height, this.background);
        if (components.length == 0) {
            if (dimension.width < logo.width)
                return;
            DrawTexture(
                logo,
                dimension.x + (dimension.width / 2 - logo.width / 2),
                dimension.y + (dimension.height / 2 - logo.height / 2),
                this.foreground
            );
        } else {

        }
    }

    public void AddTab(cstring title, Texture* icon, Component c) {
        components ~= TabComponent(title, icon, c);
    }
}
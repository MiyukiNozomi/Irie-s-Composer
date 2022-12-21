module irie.composer.traycomponents.base;

import irie.composer.common;

public class TrayComponent : Component {

    const(char*) title;

    Dimension childDimension;

    public this(const(char*) title) {
        this.title = title;
        this.background = Theme.GetColor("tray-title-bar-background");
        this.foreground = Theme.GetColor("tray-title-bar-foreground");
    }

    override void Draw() {
        float titleHeight = dimension.height / 16;
        DrawRectangle(dimension.x, dimension.y, dimension.width, cast(int)titleHeight, this.background);
        childDimension = Dimension(dimension.x, cast(int)(dimension.y + titleHeight),
                                   dimension.width, cast(int)(dimension.height - titleHeight));

        float fontSize = titleHeight / 1.7;
        float xText = dimension.x + dimension.width / 12;
        float yText = dimension.y + titleHeight / 3.4;

        DrawTextEx(Resource.YaheiUI, title, Vector2(xText, yText), fontSize, 1, this.foreground);
    }
}
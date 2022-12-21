module irie.composer.traycomponents.settings;

import irie.composer.common;
import irie.composer.traycomponents.base;

public class Settings : TrayComponent {

    public Color settingsBackground;

    public this() {
        super(SettingsTitle);
        settingsBackground = Theme.GetColor("traycomp-background");
    }

    override void Update() {
        super.Update();
    }

    override void Draw() {
        super.Draw();
        DrawRectangle(childDimension.x, childDimension.y, childDimension.width, childDimension.height, settingsBackground);
    }
}
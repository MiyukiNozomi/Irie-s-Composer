module irie.composer.traycomponents.modulelist;

import irie.composer.common;
import irie.composer.traycomponents.base;

public class ModuleLister : TrayComponent {

    public Color listBackground;

    public this() {
        super(ModuleListTitle);
        listBackground = Theme.GetColor("traycomp-background");
    }

    override void Update() {
        super.Update();
    }

    override void Draw() {
        super.Draw();
        DrawRectangle(childDimension.x, childDimension.y, childDimension.width, childDimension.height, listBackground);
    }
}
module irie.composer.components.tray;

import irie.composer.common;
import irie.composer.traycomponents.base;

public class Tray : Component {

    TrayIcon* selectedIcon;
    TrayIcon[] icons;
    Color hoveringForeground;
    Color selectedForeground;

    this() {
        this.background = Theme.GetColor("tray-background");
        this.foreground = Theme.GetColor("tray-foreground");
        this.hoveringForeground = Theme.GetColor("tray-hovering-foreground");
        this.selectedForeground = Theme.GetColor("tray-selected-foreground");

        icons ~= TrayIcon(null, Resource.LoadIcon("tray/explorer.png"), IrieComposer.Get.explorer);
        icons ~= TrayIcon(null, Resource.LoadIcon("tray/module.png"), IrieComposer.Get.moduleList);
        icons ~= TrayIcon(null, Resource.LoadIcon("tray/whiteicon.png"), IrieComposer.Get.settings);

        Select(&icons[0]);
    } 

    override void Update() {
        this.dimension.x = 0;
        this.dimension.y = IrieComposer.Get.menuBar.GetHeight();
        this.dimension.width = Math.Clamp!int(38, 50, GetScreenWidth() / 13);
        this.dimension.height =  GetScreenHeight() - this.dimension.y;
        
        Vector2 mousePosition = GetMousePosition();
        // first, check if its inside this component
        if (!PointInsideDimension(mousePosition.x, mousePosition.y, this.dimension)) {
            for (int d = 0; d < icons.length; d++) icons[d].hovering = false;
            return;
        }

        int paddingStep = this.dimension.width / 4;
        int paddingY = this.dimension.y + paddingStep;
        int xOffset = paddingStep / 2;

        float maxImageSize = this.dimension.width - paddingStep;
        for (int i = 0; i < icons.length; i++) {
            bool last = i + 1 >= icons.length;
            TrayIcon* icon = &icons[i];

            Dimension dimensionToCheck;
            if (last) {
                dimensionToCheck = Dimension(0, (this.dimension.height - this.dimension.width) + this.dimension.y,
                        icon.icon.width, icon.icon.width);
            } else {
                dimensionToCheck = Dimension(xOffset, paddingY, cast(int)maxImageSize, cast(int)maxImageSize);
                paddingY += this.dimension.width + paddingStep;
            }

            // then, check for hovering
            if (PointInsideDimension(mousePosition.x, mousePosition.y, dimensionToCheck)) {
                icon.hovering = true;
                if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
                    if (selectedIcon)
                        selectedIcon.selected = false;
                    Select(icon);
                }
            } else {
                icon.hovering = false;
            }
        }
    }

    override void Draw() {
        super.Draw();

        int paddingStep = this.dimension.width / 4;
        int paddingY = this.dimension.y + paddingStep;
        int xOffset = paddingStep / 2;

        float maxImageSize = this.dimension.width - paddingStep;
        for (int i = 0; i < icons.length; i++) {
            bool last = i + 1 >= icons.length;
            TrayIcon* icon = &icons[i];
            Color foreground;
            if (icon.selected) {
                foreground = selectedForeground;
            }else foreground = icon.hovering ? this.hoveringForeground : this.foreground;

            if (last) {
                float lastYPos = (this.dimension.height - this.dimension.width) + this.dimension.y;
                float imageSize = cast(float)this.dimension.width / cast(float)icon.icon.width;
                DrawTextureEx(icon.icon, Vector2(0, lastYPos),
                        0, imageSize, foreground);
            } else {
                float imageSize = maxImageSize / icon.icon.width;
                DrawTextureEx(icon.icon, Vector2(xOffset, paddingY), 0, imageSize, foreground);
                paddingY += this.dimension.width + paddingStep;
            }
        }
    }

    private void Select(TrayIcon* icon) {
        icon.selected = true;
        selectedIcon = icon;

        if (icon.callback !is null) {
            icon.callback();
        }
        if (icon.targetComponent !is null) {
            IrieComposer.Get.mainSplit.left = icon.targetComponent;
        }
    }
}

public struct TrayIcon {
    TrayActionFunction callback;
    Texture icon;
    TrayComponent targetComponent;
    bool selected, hovering;
}

alias TrayActionFunction = void function();
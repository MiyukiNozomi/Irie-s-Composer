module irie.composer.components.tabpanel;

import irie.composer.common;

struct TabComponent {
    const(char)* title;
    Texture icon;
    Component component;

    float iconSize;
}

class TabPanel : Component {

    Texture logo;
    public int selectedIndex;
    public TabComponent* selectedTab;
    public TabComponent[] components;

    const int fontSize = cast(int)(38 / 1.7);
    const int padding = 8;
    const int tabHeight = 36;
    const int tabminWidth = 128;
    const int textYOffset = 10;

    const int iconSize = 20;

    Color tabForeground, tabSelectedForeground;
    Color tabBackground, tabBorder, tabSelectedBackground;

    public this() {
        logo = Resource.LoadIcon("tray/whiteicon.png");
        this.background = Theme.GetColor("tabpanel-background");
        this.foreground = Theme.GetColor("tabpanel-empty-logo");

        this.tabSelectedForeground = Theme.GetColor("tab-selected-foreground");
        this.tabForeground = Theme.GetColor("tab-foreground");

        this.tabSelectedBackground = Theme.GetColor("tab-selected-background");
        this.tabBackground = Theme.GetColor("tab-background");
        this.tabBorder = Theme.GetColor("tab-border");
    }

    public override void Update() {
        if (selectedTab)
            selectedTab.component.dimension = Dimension(
                GetX(),
                GetY() + tabHeight,
                GetWidth(),
                GetHeight() - tabHeight
            );

            Vector2 p = GetMousePosition();
            if (!PointInsideDimension(p.x, p.y, this.dimension) || !IsMouseButtonPressed(MOUSE_BUTTON_LEFT))
                return;
        
            int offsetX = dimension.x;
            for (int i = 0; i < this.components.length; i++) {
                TabComponent* comp = &this.components[i];

                float textWidth = MeasureTextEx(Resource.YaheiUI, comp.title, fontSize, 1).x;
                int width = iconSize + padding + padding + cast(int)Math.Max!float(textWidth, tabminWidth);
                Dimension tabDimension = Dimension(offsetX, dimension.y, width, tabHeight);

                if (PointInsideDimension(p.x, p.y, tabDimension)) {
                    selectedIndex = i;
                    selectedTab = comp;
                    break;
                }
                offsetX += width;
            }
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
            int offsetX = dimension.x;
            for (int i = 0; i < this.components.length; i++) {
                TabComponent* comp = &this.components[i];

                float textWidth = MeasureTextEx(Resource.YaheiUI, comp.title, fontSize, 1).x;
                int width = iconSize + padding + padding + cast(int)Math.Max!float(textWidth, tabminWidth);

                DrawRectangle(offsetX, dimension.y, width, tabHeight,
                    i == selectedIndex ?  tabSelectedBackground : tabBackground);

                auto foreground = i == selectedIndex ? tabSelectedForeground: tabForeground;
                DrawTextEx(Resource.YaheiUI, comp.title,
                        Vector2(padding + offsetX + iconSize, dimension.y + textYOffset), fontSize, 1,
                        foreground);
                
                DrawTextureEx(comp.icon, Vector2(offsetX + padding, dimension.y + textYOffset),
                    0, comp.iconSize, foreground);
                offsetX += width;
                
                if (i != selectedIndex && i + 1 < components.length &&
                    i + 1 != selectedIndex) {
                    DrawRectangle(offsetX-1, dimension.y, 2, tabHeight,background);
                }
            }
            DrawRectangle(dimension.x, dimension.y + tabHeight - 3, dimension.width, 3, tabBorder);

            selectedTab.component.Draw();
        }
    }

    public void AddTab(cstring title, Texture icon, Component c) {
        components ~= TabComponent(title, icon, c, cast(float)iconSize / cast(float)icon.width);
        selectedIndex = cast(int)components.length - 1;
        selectedTab = &components[selectedIndex];
    }
}
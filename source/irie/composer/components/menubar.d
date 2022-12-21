module irie.composer.components.menubar;

import std.string;
import bindbc.raylib;

import irie.composer.common;
import irie.composer.components.menubar_callbacks;

class MenuBar : Component {

    Color borderColor;
    Color selectedColor;
    Menu[] menus;

    public this() {
        background = Theme.GetColor("menu-background"); 
        foreground = Theme.GetColor("menu-foreground"); 
        selectedColor   = Theme.GetColor("menu-selected");
        borderColor     = Theme.GetColor("menu-border");

        Menu fileMenu = new Menu(MenuItemFile, null);
        fileMenu.children ~= new Menu(MenuOpenFile, &OpenFileMenu_Callback);
        fileMenu.children ~= new Menu(MenuOpenFolder, &OpenFolderMenu_Callback);
        menus ~= fileMenu;
        menus ~= new Menu(MenuItemEdit, null);
    }

    override void Update() {
        dimension = Dimension(0, 0, GetScreenWidth(), Math.Max(GetScreenHeight() / 23, 16));
        Vector2 mousePos = GetMousePosition();

        for (int i = 0; i < menus.length; i++) {
            Menu* menu = &menus[i];
            bool menuExpanded = menu.expanded;
            CheckHover(menu, mousePos);

            if (menuExpanded) {
                for (int h = 0; h < menu.children.length; h++) {
                    CheckHover(&menu.children[h], mousePos);
                }
            }
        }
    }

    void CheckHover(Menu* menu, Vector2 mousePos) {
        if (PointInsideDimension(mousePos.x, mousePos.y, menu.menuDimension)) {
             menu.hovering = true;
        } else {
            menu.hovering = false;
        }
        if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
            if (menu.hovering) {
                menu.expanded = !menu.expanded;
                if (menu.children.length == 0) {
                    Sys.Printfln("MenuBar", "Clicked on '%s'", menu.label.fromStringz());
                    if (menu.listener !is null)
                        menu.listener(menu);
                }
            } else 
                menu.expanded = false;
        }
    }

    override void Draw() {
        super.Draw();
        DrawLine(0, this.dimension.height, this.dimension.width, this.dimension.height, borderColor);

        int paddingStep = this.dimension.height / 2;
        int fontSize = cast(int)(this.dimension.height / 1.3);
    
        // avoid stuff getting too small
        paddingStep = Math.Max!int(paddingStep, 9);
        fontSize = Math.Max!int(fontSize, 16);
        //padding between menu items.
        int padding = paddingStep;
        int paddingY = cast(int) (fontSize / 4.2);
        int menuWidthPadding = + (paddingStep * 2);

        bool first = true;
        foreach(m ; menus) {
            if (m.hovering || m.expanded) {
                DrawRectangle(m.menuDimension.x,
                              m.menuDimension.y,
                              m.menuDimension.width,
                              m.menuDimension.height, selectedColor);
            }

            DrawTextEx(Resource.YaheiUI, m.label, Vector2(padding + (first ? 0 : paddingStep), paddingY),
                            fontSize, 1, foreground);
            m.menuDimension.x = padding - paddingStep + (first ? 0 : paddingStep);
            m.menuDimension.y = 0;
            m.menuDimension.height = dimension.height;
            padding += cast(int) (paddingStep + MeasureTextEx(Resource.YaheiUI, m.label, fontSize, 1).x);
            m.menuDimension.width = padding - m.menuDimension.x + (first ? 0 : paddingStep);
            if (first)
                first = false;

            if (m.expanded && m.children.length > 0) {
                int y = m.menuDimension.y + m.menuDimension.height;
                int height = m.menuDimension.height * cast(int)m.children.length;
                int width = m.menuDimension.width;
                foreach(Menu child ; m.children) {
                    float textwidth = MeasureTextEx(Resource.YaheiUI, child.label, fontSize, 1).x + menuWidthPadding;
                    if (width < textwidth)
                        width = cast(int)textwidth;
                }
                DrawRectangle(m.menuDimension.x, y, width, height, background);
                DrawRectangleLines(m.menuDimension.x - 1, y - 1, width + 2, height + 2, borderColor);

                int heightPerChild = height / cast(int)m.children.length;

                int i = 0;
                foreach(Menu child ; m.children) {  
                    int currHeightOffset = i * heightPerChild;
                    int yPos = y + currHeightOffset;
                    if (child.hovering)  
                        DrawRectangle(m.menuDimension.x, yPos, width,heightPerChild, selectedColor);
                    DrawTextEx(Resource.YaheiUI, child.label, Vector2(paddingStep + m.menuDimension.x,yPos),
                        fontSize, 1, foreground);
                    child.menuDimension = Dimension(
                        m.menuDimension.x,
                        yPos,
                        width,
                        heightPerChild
                    );
                    i++;
                }
            }
        }
    }

}

class Menu {
    Dimension menuDimension;
    const(char)* label;
    MenuActionListener listener;
    Menu[] children;

    bool hovering;
    bool expanded;

    this(const(char)* label, MenuActionListener listener) {
        this.label = label;
        this.listener = listener;
    }
}

alias MenuActionListener = void function(Menu* menu);
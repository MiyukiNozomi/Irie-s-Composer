module irie.composer.traycomponents.explorer;

import std.path;
import std.file;
import std.string;
import std.algorithm : sort;
import std.algorithm.mutation :SwapStrategy;

import irie.composer.common;
import irie.composer.components.test;
import irie.composer.traycomponents.base;

import irie.platform.messagebox;

version(Windows) {
    const string DirSeparator = "\\";
} else {
    const string DirSeparator = "/";
}

auto Explorer_GetFileName(string path) {
    return path[(path.lastIndexOf(DirSeparator)+1) .. path.length];
}

auto DirToInt(bool dir) {
    return dir ? 1 : 0;
}

public struct TreeNode {
    bool isDirectory;
    string path;
    const(char)* name;
    TreeNode[] children;
    bool hovering, selected, expanded;

    void OnExpand() {
        expanded = true;
        if (!isDirectory)
            return;
        DirIterator names = dirEntries(path, SpanMode.shallow);
        foreach(string n ; names) {
            bool isDirec = isDir(n);
            children ~= TreeNode(isDirec, n,Explorer_GetFileName(n).toStringz());
        }
        sort!((a, b) => DirToInt(a.isDirectory) > DirToInt(b.isDirectory))(children);
    }

    void OnRetract() {
        expanded = false;
        if (children.length > 0)
            children = null;
    }
}

public class Explorer : TrayComponent {

    public Color explorerBackground, explorerForeground, selectedColor, hoverColor;
    public TreeNode rootNode;
    public TreeNode* selected;

    public static Texture folderIcon, folderOpenedIcon;
    public static Texture fileIcon;

    double lastClickTime;

    static void LoadAssets() {
        folderIcon = Resource.LoadIcon("explorer/folder.png");
        folderOpenedIcon = Resource.LoadIcon("explorer/folderopened.png");
        fileIcon = Resource.LoadIcon("explorer/file.png");
    }

    public this() {
        super(ExplorerTitle);
        explorerBackground = Theme.GetColor("traycomp-background");
        explorerForeground = Theme.GetColor("traycomp-foreground");
        selectedColor = Theme.GetColor("explorer-selected");
        hoverColor = Theme.GetColor("explorer-hovering");

        iconSize = 19;
        fontSize = iconSize;
        iconPadding = iconSize / 4;
        percentIconSize = cast(float)iconSize / cast(float)fileIcon.width;
    }

    Vector2 mousePos;
    float wheelOffset = 0;
    override void Update() {
        super.Update();
        mousePos = GetMousePosition();
        if (!PointInsideDimension(mousePos.x, mousePos.y, childDimension))
            return;

        if (rootNode.path == "")
            return;

        int defaultOffsetX = cast(int)(childDimension.x + childDimension.width * 0.02);
        int defaultOffsetY = cast(int)(childDimension.y + childDimension.height * 0.02 + wheelOffset);
        childrenYOffset = 0;
        
        float wheel = GetMouseWheelMove();
        if (wheel > 0) {
            wheelOffset += wheel * (iconSize + iconPadding);
        } else if (wheel < 0) {
            wheelOffset -= -wheel * (iconSize + iconPadding);
        }

        CheckNode(&rootNode, defaultOffsetX, defaultOffsetY);
    }

    void CheckNode(TreeNode* nd, int offsetX, int offsetY) {
        offsetY += childrenYOffset;
        if (offsetY < childDimension.height && offsetY > childDimension.y) {
            Dimension collisionBox = Dimension(
                                    offsetX,offsetY,
                                    iconPadding + iconSize + cast(int)MeasureTextEx(Resource.YaheiUI, nd.name, fontSize, 1).x,
                                    iconSize);

            if (PointInsideDimension(mousePos.x, mousePos.y, collisionBox)) {
                if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
                    if (nd == selected) {
                        double tt = GetTime() - lastClickTime;
                        if (tt > 0 && tt < 0.300) {
                            OnClick();
                        }
                        lastClickTime = GetTime();
                    }
                    if (selected !is null)
                        selected.selected = false;
                    selected = nd;
                    selected.selected = true;

                    if (selected.expanded)
                        selected.OnRetract();
                    else
                        selected.OnExpand();
                } else
                nd.hovering = true;
            } else {
                nd.hovering = false;
            }
        }

        if (nd.expanded) {
            offsetX += iconSize;
            if (childrenYOffset > 0)
                offsetY -= childrenYOffset;
            for (int i = 0; i < nd.children.length; i++) {
                TreeNode* child = &nd.children[i];
                offsetY += iconSize + iconPadding;
                CheckNode(child, offsetX, offsetY);
            }
            int childrenLength = cast(int)nd.children.length;
            childrenYOffset += (childrenLength * iconSize) + (childrenLength * iconPadding);
        }
    }

    override void Draw() {

        super.Draw();
        DrawRectangle(childDimension.x, childDimension.y, childDimension.width, childDimension.height, explorerBackground);

        if (rootNode.path == "")
            return;

        int defaultOffsetX = cast(int)(childDimension.x + childDimension.width * 0.02);
        int defaultOffsetY = cast(int)(childDimension.y + childDimension.height * 0.02 + wheelOffset);

        childrenYOffset = 0;
        DrawNode(&rootNode, defaultOffsetX, defaultOffsetY);
    }

    int childrenYOffset;
    int fontSize;
    int iconSize;
    int iconPadding;
    float percentIconSize;

    void DrawNode(TreeNode* nd, int offsetX, int offsetY) {
        offsetY += childrenYOffset;
        if (offsetY < childDimension.height && offsetY > childDimension.y) {
            Color foreColor = nd.selected ? selectedColor : (nd.hovering ? hoverColor : explorerForeground);
            if (nd.isDirectory)
                DrawTextureEx(nd.children.length > 0 ? folderOpenedIcon : folderIcon,
                            Vector2(offsetX, offsetY), 0, percentIconSize, foreColor);
            else
                DrawTextureEx(fileIcon,
                            Vector2(offsetX, offsetY), 0, percentIconSize, foreColor);

            Vector2 texPos = Vector2(offsetX + iconSize + iconPadding, offsetY);
            DrawTextEx(Resource.YaheiUI, nd.name, texPos, fontSize, 1, foreColor);
        }
        if (nd.expanded) {
            offsetX += iconSize;
            if (childrenYOffset > 0)
                offsetY -= childrenYOffset;
            for (int i = 0; i < nd.children.length; i++) {
                TreeNode* child = &nd.children[i];
                offsetY += iconSize + iconPadding;
                DrawNode(child, offsetX, offsetY);
            }
            int childrenLength = cast(int)nd.children.length;
            childrenYOffset += (childrenLength * iconSize) + (childrenLength * iconPadding);
        }
    }

    public void OpenDirectory(string dir) {
        if (!exists(dir) || !isDir(dir)) {
            ShowMessage(ExplorerErrorTitle, ExplorerErrorMessage);
            return;
        }

        rootNode = TreeNode(true, dir, Explorer_GetFileName(dir).toStringz());
        rootNode.OnExpand();
    }

    public void OnClick() {
        if (selected.isDirectory)
            return;
      //  Sys.Printfln("Explorer", "Opening: %s", selected.name.fromStringz());
        
        IrieComposer.Get.editorSplit.top.AddTab(selected.name, fileIcon, new TestComponent(DARKGRAY));
    }
}
module irie.composer.core;

import bindbc.raylib;

import irie.composer.sys;
import irie.composer.theme;
import irie.composer.events;
import irie.composer.launcher;
import irie.composer.util.resource;
import irie.composer.components.test;
import irie.composer.components.tray;
import irie.composer.components.menubar;
import irie.composer.components.mainsplit;
import irie.composer.components.editorsplit;
import irie.composer.components.component;
import irie.composer.traycomponents.explorer;
import irie.composer.traycomponents.settings;
import irie.composer.traycomponents.modulelist;

class IrieComposer {

    static IrieComposer Get;

    Tray tray;
    MenuBar menuBar;
    MainSplit mainSplit;
    EditorSplit editorSplit;
    Explorer explorer;
    ModuleLister moduleList;
    Settings settings;

    public this() {
        Launcher launcher = new Launcher();
        launcher.Start();
        ClearWindowState(FLAG_WINDOW_UNDECORATED);
        SetWindowState(FLAG_WINDOW_RESIZABLE);
        SetWindowTitle("Irie's Composer");

        SetTargetFPS(30);
    }

    public void MakeUI() {
        settings = new Settings();
        editorSplit = new EditorSplit();
        mainSplit = new MainSplit(new TestComponent(GREEN),
                                  0.25,
                                  editorSplit);
        menuBar = new MenuBar();
        explorer = new Explorer();
        moduleList = new ModuleLister();
        tray = new Tray();
    }

    public void Run() {
        while (!WindowShouldClose()) {
            BeginDrawing();
            ClearBackground(Theme.GetColor("background"));

            mainSplit.Update();
            menuBar.Update();
            tray.Update();
            editorSplit.Update();

            mainSplit.Draw();
            tray.Draw();
            menuBar.Draw();

            debug {
                version(Japanese) {
                  DrawTextEx(Resource.YaheiUI, "Debug Build of Irie's Composer, JA_JP", Vector2(GetScreenWidth() / 4,
                    GetScreenHeight() - 45),
                    30, 1, BLUE);
                } else
                    DrawTextEx(Resource.YaheiUI, "Debug Build of Irie's Composer, EN_US", Vector2(GetScreenWidth() / 4,
                    GetScreenHeight() - 45),
                    30, 1, BLUE);
            }
            EndDrawing();
        }
        Resource.Release();
        CloseWindow();
    }
}
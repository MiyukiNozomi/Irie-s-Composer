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
import irie.composer.components.component;
import irie.composer.traycomponents.explorer;
import irie.composer.traycomponents.settings;
import irie.composer.traycomponents.modulelist;

class IrieComposer {

    static IrieComposer Get;

    Tray tray;
    MenuBar menuBar;
    MainSplit mainSplit;
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
        mainSplit = new MainSplit(new TestComponent(GREEN),
                                  0.25,
                                  new TestComponent(RED));
        menuBar = new MenuBar();
        explorer = new Explorer();
        moduleList = new ModuleLister();
        tray = new Tray();
    }

    public void Run() {
        while (!WindowShouldClose()) {
            BeginDrawing();
            ClearBackground(Theme.GetColor("background"));
            //DrawTextEx(Resource.YaheiUI, "Test", Vector2(120, 120), 40, 1, LIGHTGRAY);

            mainSplit.Update();
            menuBar.Update();
            tray.Update();

            mainSplit.Draw();
            tray.Draw();
            menuBar.Draw();

            EndDrawing();
        }
        Resource.Release();
        CloseWindow();
    }
}
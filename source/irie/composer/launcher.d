module irie.composer.launcher;

import core.stdc.stdlib:exit;
import std.string;
import bindbc.raylib;

import irie.composer.sys;
import irie.composer.theme;
import irie.composer.language;
import irie.composer.util.resource;

import irie.composer.traycomponents.explorer;

import core.thread;

class Launcher {
    public void InitialLoad() {
        Sys.Printfln("Launcher", "Initial Loading Stage");
        Resource.Load();
        Theme.current = Theme.LoadTheme("lavander");
    }

    public void PosInit() {
        Sys.Printfln("Launcher", "Post Loading Stage");

        Explorer.LoadAssets();
    }

    public void Start() {
        Resource.FindComposerDir();
        Sys.allowCapture = true;
        SetConfigFlags(FLAG_WINDOW_UNDECORATED);
        InitWindow(430, 300, LauncherTitle);
        SetWindowIcon(LoadImage(Resource.MakePath("images/icon.png")));

        Texture2D background = LoadTexture(Resource.MakePath("images/launcher/background.png"));
        Texture2D errorBackground = LoadTexture(Resource.MakePath("images/launcher/error-background.png"));

        SetTargetFPS(20);

        int loadStage = 0;

        while (!WindowShouldClose()) {
            if (loadStage > 1)
                break;

            try {
                if (loadStage == 0) {
                    InitialLoad();
                    loadStage++;
                } else if (loadStage == 1) {
                    PosInit();
                    loadStage++;
                }
            } catch(Error e) {
                Sys.Printfln("Launcher", "Something threw an error!");
                Sys.WriteCrash(e);
                loadStage = -1;
                UnloadTexture(background);
                background = errorBackground;
            } catch(Exception e) {
                Sys.Printfln("Launcher", "Something threw an Exception");
                Sys.WriteCrash(e);
                loadStage = -1;
                UnloadTexture(background);
                background = errorBackground;
            }

            BeginDrawing();
            ClearBackground(BLACK);

            DrawTexture(background, 0, 0, WHITE);

            if (loadStage != -1) {
                DrawTextEx(Resource.YaheiUI, LauncherTitle, Vector2(16,16), 26, 1, WHITE);

                DrawTextEx(Resource.Consolas, Sys.buffer_c, Vector2(16, 48), 8, 1, LIGHTGRAY);
            } else {
                DrawTextEx(Resource.Consolas, LauncherErrorMessage, Vector2(40, 105), 13, 1, WHITE);
            }
  
            EndDrawing();
        }
        
        if (loadStage != -1) {
            UnloadTexture(background);
        } else {
            UnloadTexture(errorBackground);
            exit(-1);
        }
        UnloadTexture(errorBackground);

        Sys.allowCapture = false;
        Sys.ClearCapture();
    }
}
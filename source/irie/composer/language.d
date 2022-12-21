module irie.composer.language;

version(Japanese) {
    const(char)* MenuItemFile = "ファイル";
    const(char)* MenuItemEdit = "編集";
    const(char)* MenuOpenFile = "ファイルを開く";
    const(char)* MenuOpenFolder = "フォルダオープン";
    const(char)* LauncherTitle = "入江Composer初期化。。。";

    const(char)* LauncherErrorMessage = "[ComposerDir]/crash.txt分析する";

    const(char)* ExplorerTitle = "探検人";
    const(char)* ModuleListTitle = "モジュール";
    const(char)* SettingsTitle = "設定";

    string ExplorerErrorTitle = "<unsure>";
    string ExplorerErrorMessage = "<unsure>";
} else {
    const(char)* MenuItemFile = "File";
    const(char)* MenuItemEdit = "Edit";
    const(char)* MenuOpenFile = "Open File";
    const(char)* MenuOpenFolder = "Open Folder";
    const(char)* LauncherTitle = "入江Composer Loading...";

    const(char)* LauncherErrorMessage = "Check [ComposerDir]/crash.txt";

    const(char)* ExplorerTitle = "Explorer";
    const(char)* ModuleListTitle = "Modules";
    const(char)* SettingsTitle = "Settings";

    string ExplorerErrorTitle = "Unable to open directory!";
    string ExplorerErrorMessage = "Please select your directory again in File > Open Folder";
}
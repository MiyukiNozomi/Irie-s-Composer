module irie.composer.components.menubar_callbacks;

import irie.platform.filechooser;

import irie.composer.common;
import irie.composer.components.menubar;

void OpenFileMenu_Callback(Menu* menu) {
    string filepath;
    if (Platform_OpenFileDialog(false, filepath)) {
        Sys.Printfln("OpenFileMenu_Callback", "Open File: %s", filepath);
    } else {
        Sys.Printfln("OpenFileMenu_Callback", "Dialog Closed!");
        return;
    }
}

void OpenFolderMenu_Callback(Menu* menu) {
    string folderpath;
    if (Platform_OpenFileDialog(true, folderpath)) {
        Sys.Printfln("OpenFolderMenu_Callback", "Open Folder: %s", folderpath);
    } else {
        Sys.Printfln("OpenFolderMenu_Callback", "Dialog Closed!");
        return;
    }

    IrieComposer.Get.explorer.OpenDirectory(folderpath);
}   
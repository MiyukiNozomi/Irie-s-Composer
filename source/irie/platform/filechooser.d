/**
    Platform Specific - File Chooser
*/

module irie.platform.filechooser;

import std.string;
import core.memory:GC;
import irie.composer.sys;

version(Windows) {
    pragma(lib, "ole32.lib");
    // platform.lib is the resulting compilation of the C code in irie/platform.
    // in this case, windows-specific code.
    pragma(lib, "platform.lib");
    pragma(lib, "user32.lib");
    pragma(lib, "comdlg32.lib");

    import std.conv : to;

    import core.sys.windows.shlobj;
    import core.sys.windows.com;
    import core.sys.windows.windows;
    import core.sys.windows.commdlg;

    extern(C) LPWSTR Platform_OpenFileDialog(bool folders);
    extern(C) void FreeLPWSTR(LPWSTR str);

    bool Platform_OpenFileDialog(bool foldersOnly, out string filepath) {
        LPWSTR cpath = Platform_OpenFileDialog(foldersOnly);
        if (cpath is null)
            return false;

        string stuff = cpath.to!string;
        Sys.Printfln("platform-win32", "Stuff length: %u, Data: %s", stuff.length, cpath);
        FreeLPWSTR(cpath);
        filepath = stuff;
        return true;
    }
}
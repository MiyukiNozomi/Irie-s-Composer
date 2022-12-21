module irie.platform.messagebox;

version(Windows) {
    import core.sys.windows.windows;

    import std.utf : toUTF16z;

    void ShowMessage(string title, string message) {
        MessageBox(
                NULL,
                message.toUTF16z(),
                title.toUTF16z(),
                MB_ICONERROR | MB_OK
            );
    }
}
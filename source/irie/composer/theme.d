module irie.composer.theme;

import bindbc.raylib;

import std.json;
import irie.composer.sys;
import std.file : readText;

class Theme {
    static Theme current;

    Color[string] colors;
    Color missing = Color(255,0,255);

    static Color GetColor(string key) {
        if((key in current.colors) is null)
            return current.missing;
        else
            return current.colors[key];
    }

    static Theme LoadTheme(string name) {
        Sys.Printfln("ThemeLoader","Loading Theme: %s", name);

        Theme theme = new Theme();
        
        JSONValue file = parseJSON(readText("resources/themes/" ~ name ~ ".json"));

        JSONValue generalKeys = file["general"];
        auto map = generalKeys.object();
       
        foreach(key ; map.byKey()) {
            theme.colors[key] = HEXtoColor(map[key].str());
        }

        Sys.Printfln("ThemeLoader","Color Array Size: %d", theme.colors.length * Color.sizeof);

        return theme;
    }

    private static Color HEXtoColor(string raw) {
        import std.conv : to;
        ulong color = raw.to!ulong(16);
        return Color(
            ((color >> 16) & 0xFF),  // Extract the RR byte
            ((color >> 8) & 0xFF),   // Extract the GG byte
            ((color) & 0xFF),        // Extract the BB byte
            255
        );
    }
}
module irie.composer.util.resource;

import std.string;
import bindbc.raylib;
import core.memory : GC;
import core.stdc.string :memcpy;

import irie.composer.sys;
import irie.composer.language;

class Resource {
    static Font YaheiUI;
    static Font Consolas;
    static string ComposerDir;

    static string Version = "PreRelease_0";

    static void FindComposerDir() {
        import std.file : thisExePath;
        ComposerDir = thisExePath();
        
        // get a range off the string from 0 to the last "/"
        // because WindowsNT based systems use "\" to represent paths
        // this section is somewhat OS-dependant 
        version(Windows) {
            ComposerDir = ComposerDir[0 .. ComposerDir.lastIndexOf("\\")];
        } else {
            ComposerDir = ComposerDir[0 .. ComposerDir.lastIndexOf("/")];
        }

        Sys.Printfln("Resource", "Composer Directory: %s", ComposerDir);
    }

    static const(char)* MakePath(string internalPath) {
        return (Resource.ComposerDir~"/resources/"~internalPath).toStringz();
    }

    static void Load() {
        version(Japanese) {
            auto text = MenuItemEdit ~ MenuItemFile ~ LauncherTitle ~"　";
    
            // Get codepoints from text
            int codepointsNoDupsCount = 0;
            int *codepointsNoDups = LoadCodepoints(text.toStringz(), &codepointsNoDupsCount);
        
            // Removed duplicate codepoints to generate smaller font atlas
         //   int codepointsNoDupsCount = 0;
         //   int *codepointsNoDups = CodepointRemoveDuplicates(codepoints, codepointCount, &codepointsNoDupsCount);
           // UnloadCodepoints(codepoints);  
            YaheiUI = LoadFontEx("resources/fonts/yaheiui.ttf", 124, codepointsNoDups, codepointsNoDupsCount);
        } else {
            YaheiUI = LoadFont("resources/fonts/yaheiui.ttf");
        }
        SetTextureFilter(YaheiUI.texture, TEXTURE_FILTER_BILINEAR);
        Consolas = LoadFont("resources/fonts/consolas.ttf");
        SetTextureFilter(Consolas.texture, TEXTURE_FILTER_BILINEAR);
    }

    static Texture[string] textures;

    static Texture LoadIcon(string what) {
        if ((what in textures) !is null)
            return textures[what];
        Sys.Printfln("Resource", "Loading Icon: %s", what);
        Texture t = LoadTexture(Resource.MakePath("images/" ~what));
        if (t.width == 0)
            throw new ResourceFailure("Unable to load Texture: " ~ what);
        SetTextureFilter(t, TEXTURE_FILTER_POINT);
        textures[what] = t;
        return t;
    }

    static void Release() {
        Sys.Printfln("Resource","Releasing %u Textures", textures.length);
        foreach(Texture t ; textures.byValue()) {
            UnloadTexture(t);
        }
    }
}

// Remove codepoint duplicates if requested
// WARNING: This process could be a bit slow if there text to process is very long
static int *CodepointRemoveDuplicates(int *codepoints, int codepointCount, int *codepointsResultCount)
{
    int codepointsNoDupsCount = codepointCount;
    int* codepointsNoDups = cast(int *)GC.calloc(codepointCount, int.sizeof);
    memcpy(codepointsNoDups, codepoints, codepointCount*int.sizeof);

    // Remove duplicates
    for (int i = 0; i < codepointsNoDupsCount; i++)
    {
        for (int j = i + 1; j < codepointsNoDupsCount; j++)
        {
            if (codepointsNoDups[i] == codepointsNoDups[j])
            {
                for (int k = j; k < codepointsNoDupsCount; k++) codepointsNoDups[k] = codepointsNoDups[k + 1];

                codepointsNoDupsCount--;
                j--;
            }
        }
    }

    // NOTE: The size of codepointsNoDups is the same as original array but
    // only required positions are filled (codepointsNoDupsCount)

    *codepointsResultCount = codepointsNoDupsCount;
    return codepointsNoDups;
}

class ResourceFailure : Exception {
    this(string msg, string file = __FILE__, size_t line = __LINE__, Throwable nextInChain = null) pure nothrow @nogc @safe
    {
        super(msg, file, line, nextInChain);
    }
}

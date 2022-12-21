module irie.composer.sys;

import core.stdc.stdio;

import core.memory : GC;

import std.format : format;
import std.string : toStringz;

import irie.composer.util.resource;

class Sys {
    static bool allowCapture = true;
    static string buffer;
    static char* buffer_c;

    static void Init() {
        import bindbc.raylib;

        RaylibSupport sup = loadRaylib();

        if (sup != raylibSupport) {
            Printfln("System","Failed to load RayLib");
            if (sup == RaylibSupport.badLibrary) {
                Printfln("System","Bad Raylib Library");
            } else if (sup == RaylibSupport.noLibrary) {
                Printfln("System","missing raylib.");
            }
            return;
        }
        Sys.Printfln("System","Successfuly Initiated Raylib");

        debug {
            SetTraceLogLevel(LOG_WARNING);
        } else {
            SetTraceLogLevel(LOG_ERROR);
        }
    }

    static void Print(string msg) {
        if (allowCapture) {
            buffer ~= msg;
            if (buffer_c)
                GC.free(buffer_c);
            buffer_c = cast(char*)toStringz(buffer);
        }
        printf(msg.toStringz());
    }

    static void ClearCapture() {
        if (buffer_c)
            GC.free(buffer_c);
        buffer = "";
    }

    static void Printfln(string, Char, Args...)(string source, in Char[] fmt, Args args)  {
        Print("[THREAD/INFO] "~source~" > " ~format(fmt, args)~"\n");
    }

    static void Printf(Char, Args...)(in Char[] fmt, Args args) {
        Print(format(fmt, args));
    }

    static void WriteCrash(Error e) {
        import std.file : write;

        string build = Resource.Version ~ " ";
        version(Japanese) {
            build ~= "JP";
        } else {
            build ~= "EN";
        }

string thing =format(
"Composer [build %s] has suffered an Error.
Please remove any modules and themes you might be using.
Source: %s line: %u
Exception Message: %s\n
StackTrace:
%s", build, e.file, e.line, e.msg, e.info.toString());
        write(Resource.ComposerDir ~"/crash.txt", thing);
    }
    static void WriteCrash(Exception e) {
        import std.file : write;

        string build = Resource.Version ~ " ";
        version(Japanese) {
            build ~= "JP";
        } else {
            build ~= "EN";
        }

string thing =format(
"Composer [build %s] has suffered an Exception.
Please remove any modules and themes you might be using.
Source: %s line: %u
Exception Message: %s\n
StackTrace:
%s", build, e.file, e.line, e.msg, e.info.toString());
        write(Resource.ComposerDir ~ "/crash.txt", thing);
    }
}

import std.stdio;

import core.runtime;

import irie.composer.sys;
import irie.composer.core;

void main()
{
    Sys.Init();

    IrieComposer.Get = new IrieComposer();
    IrieComposer.Get.MakeUI();
    IrieComposer.Get.Run();
}

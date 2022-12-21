@echo off
echo ///////////////// Building irie.platform's C code ///////////////// 
cl source\irie\platform\folderselect.cpp /c /Foplatform.obj
lib platform.obj
del platform.obj
echo /////////////////     Building the D project      ///////////////// 
dub build --force
echo /////////////////         Build Finished          ///////////////// 

# Irie's Composer: Next Generation BadApple Studio
Unlike bad apple, this is an actual light weight editor!
it uses RayLib to render stuff to the screen, which helps mainting
a very low resource usage.

# Building
First, you'll need a D compiler and dub. (p.s if you're on windows you'll need MSVC, run your buildfile within a 64 bit instance of VS's cmd.)

Irie's Composer has 2 build versions: English or 日本語
unlike BadApple i decided to keep languages hardcoded as constants in language.d 
since its a lighter way to do such, 
feel free to correct me with my grammar mistakes in language.d in both of these versions.
Anyway, to switch between these versions, on `dub.json` edit from "English" to "Japanese" if you 
want the japanese version, or write something else. it will just fallback to english
if Japanese isn't specified as a version.

now just run build.bat or build.sh and you should be good to go.
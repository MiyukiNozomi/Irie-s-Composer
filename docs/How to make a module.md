# BadApple Tutorials: Custom Modules

## Setting up your project
Bad Apple's Module Handler will be refered as HMODULE throughout this tutorial.

before you start setting up your project, we need to first look at HMODULE's requirements when loading up a jar file for it to be considerated valid:

1) The Module is Required to have a class marked with the `@Character` annotation, with a name (example: `@Character(name = "MyModule")`)
2) For security reasons, if your module contains any packages named as `com.miyuki.badapple` HMODULE will NOT even attempt to load your file. 
make sure that when compiling your project you're specifically only packaging your classes into your module's JAR file and not BadApple.jar's internal classes.
3) Your Main class has to extend BadAppleModule otherwise it will be unaccessible.
4) No Constructors on your main class, use onAwake() instead.
5) DO NOT use `com.miyuki.badapple.Resource`, HMODULE won't really care about this, but you shouldn't really use that class since its for Internal Usage only.

Now that you know the good practices when developing modules, lets get started!
open up https://github.com/MiyukiNozomi/BadApple/releases and get a stable release, tho, current as of October 22, 2022 BadApple's releases are  currently on a pre-release state, grab the one that seems the most stable, however, if somehow i decided to maintain this project grab the release with the tag "stable".

Add that to your project's build path (just add the jar as a dependency)
and you should be good to go!

## Actually Coding the Project

Here's how your main file will look like
```java
package com.miyuki.examplemod;

import com.miyuki.baddapple.modules.BadAppleModule;
import com.miyuki.baddapple.modules.Character;

@Character(name = "MyModule", description="My first module!")
public class ExampleMod extends BadAppleModule {

	// Use this for initialization
	@Override
	public void onAwake() {
		System.out.println("Yeeey its initializing!");
	}
	
	// Use this for cleaning up your mess :D
	@Override
	public void onQuit() {
		System.out.println("Oh no, its closing up!");
	}
}
```

okay, lets break this down a little bit further
`@Character` tells HMODULE more about this module, 
while onAwake and onQuit provides us with our initialization and release functions.

Now, compile your project and you should see on the Debug Console a nice little message:
```
11:29:03 [INFO] com.miyuki.examplemod.ExampleMod > Yeeey its initializing!
```
Also, if you open up the Module tab on the tray bar, you should see your custom module right there.
Now, if you close up bad apple you should see your onQuit message! 
```
11:29:45 [INFO] com.miyuki.examplemod.ExampleMod > Oh no, its closing up!
```

## I don't like the default icon, is there a way to change?
Yes!, of course. first, your icon should have its size to be 64x64, since its the resolution that is going to be shown on the Modules tab.
now, add your icon to your source folder,

all you need to do to get your custom icon showing up is to simply add `iconPath = "<your icon name>"` to your `@Character`!
by the way, custom loader paths don't really work with iconPath, so, you may need to specify the full path like /assets/mymodule/icon.png

And there you go! any questions open an issue on the issues tab and i will politly reply to you!
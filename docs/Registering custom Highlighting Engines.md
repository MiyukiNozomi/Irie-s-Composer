# Bad Apple Tutorials: Registering Custom Highlighting Engines

## How does BadApple handle Support for Multiple Languages?
The Answer to that is somewhat simple, most of that is handled by the Registry class `com.miyuki.baddapple.Registry`

As the name suggests,it deals with registery of Highlighting engines and other stuff.for now we'll only care about the Highlighting part.

The thing that actually interests us here is `Registry#RegisterEngine(String, HighlightEngine)`,
the first parameter is the extension of the source file your HighlightEngine is designed to support; so, for example, if you were targeting HAXE source files you would pass in tha parameter `"hx"`. no need to pass a dot in the begining.

Now, the second parameter is of course your custom HighlightEngine. now, create a new class and make it extend `com.miyuki.baddapple.editor.highlighting.HighlightEngine`

your highlight engine class should look something like this:
```java
package com.miyuki.haxe;

import com.miyuki.baddapple.editor.highlighting.HighlightEngine;

public class HAXEHighlight extends HighlightEngine {
	private static final long serialVersionUID = 1L;
	
}
```

now, back in your main file your should register your Highlighting Engines on your OnAwake() method.

```java
	// Use this for initialization
	@Override
	public void onAwake() {
		System.out.println("Yeeey its initializing!");	
		
		Registry.RegisterEngine("hx", new HAXEHighlight());
	}
```

if you compile your module and run BadApple you should now see a something like: 
```log
01:27:30 [INFO] com.miyuki.baddapple.Registry > Engine for ext 'hx' registered
```

Well, if you now open a file with the extension you wanted there won't really be any difference than the default editor, so, let's make our extension slightly more interesting:

### Updating and Highlighting Tokens
Honestly, there are several ways to code this part.
however, lets start "Simple": RegexHighlightEngine

the RegexHighlightEngine is a class that extends HighlightEngine
and it allows you to provide multiple regexes that will be compiled and executed when a new token is inserted into the editor.

## Coloring Types

tho, you should first understand one thing about coloring tokens: because bad apple supports multiple themes and a modular editor, you will be limited to the following coloring types: `
```
	"keyword"
	"access-modifier"
	"datatype"
	"symbols"
	"block-symbols"
	"import-path"
	"numbers"
	"quotes"
	"comments"
	"function"
```
Honestly, i won't tell you which you should use for each case,
but keep in mind that when defining a coloring type you should
use the target language's extension with the actual coloring type you want; so, as an example: `hx.keyword`

in a future Theme documentation i'll explain the actual reason of why that is important. 

so, switch from extending HighlightEngine to extending `com.miyuki.baddapple.editor.highlighting.RegexHighlightEngine`

now, pass the regexes you want to use to the super's constructor, like this:
```java
	public HAXEHighlight() {
		super(
				new HighlightRegex("\\/\\/.*", "hx.comments"),
				new HighlightRegex("\"([^\"]*)\"","hx.quotes"),
				new HighlightRegex("\'([^\"]*)\'","hx.quotes"),
				new HighlightRegex("\\/\\*.*?\\*\\/", Pattern.DOTALL, "hx.comments"));
	}
```
Okay, lets go over what HighlightRegex actually is:
its basically a """struct""" that stores a regex and its coloring type. 
it has 2 constructors:
```java
// simple regexes:
HighlightRegex(String pattern, String attribute);

// regexes with custom flags: 
HighlightRegex(String pattern, int flags, String attribute);
```

and that's basically it! if you now compile your module and restart badapple, you should now see your regexes working! woo!

[highlight-preview](res/regex-highlight1.png)

Okay but, that's clearly not enough, so, we could.. try something different.

now, lets Tokenize our contents to mark the keywords we want.

so, override HighlightEngine#UpdateHighlight
```java
	@Override
	public void UpdateHighlight(boolean textAdded, int ofset, String str, int removedLength) {
		// TODO Auto-generated method stub
		super.UpdateHighlight(textAdded, ofset, str, removedLength);
	}
```

okay so, what is is this function? and when is it called?

this function is called every time a token is added or removed.

if you're still using RegexHighlightEngine, you should keep `super.UpdateHighlight()` call there,
since RegexHighlightEngine executes your regexes there; it also clears out previous marked words to avoid problems.

luckly for you, HighlightEngine provides a GetLexer() function that returns a Lexer for you to tokenize stuff:

```java
Lexer lexer = GetLexer();
```

what do we care about with this lexer object is its NextToken() function, as an example i'll just show you a way to look through every token:
```java
for (Token t = lexer.NextToken(); t.type != TokenType.EndOfFile; t = lexer.NextToken()) {
	Debug.Info(t.data + " " + t.position);
}
```

Now, if you execute badapple and recompile your module, you'll see it printing out every token whitin the editor.

and here's where its fully up to you, to set an attribute to a token you could simply use 
`SetColoring(t.position, t.data.length(), "default.keyword");`

the first parameter is the position of the area to start coloring,
and the second parameter the length of the area.
the third parameter is the color attribute you want to use. 
refer to the "Coloring Types" area of this page of the third argument.

and that's it!, this is how the example module utilized the concepts written in this documentation:
```java
public class HAXEHighlight extends RegexHighlightEngine {
	private static final long serialVersionUID = 1L;

	String keywords = "public|private|static|dynamic|inline|macro|extern|override|function";
	String classTypes = "abstract|class|enum|interface|typedef";
	
	public HAXEHighlight() {
		super(
				new HighlightRegex("\\/\\/.*", "haxe.comments"),
				new HighlightRegex("\\/\\*.*?\\*\\/", Pattern.DOTALL, "haxe.comments"),
				new HighlightRegex("\"([^\"]*)\"","haxe.quotes"),
				new HighlightRegex("\'([^\"]*)\'","haxe.quotes"));
	}
	
	@Override
	public void UpdateHighlight(boolean textAdded, int offset, String str, int removedLength) {
		super.UpdateHighlight(textAdded, offset, str, removedLength);
		
		Lexer lexer = GetLexer();

		for (Token t = lexer.NextToken(); t.type != TokenType.EndOfFile; t = lexer.NextToken()) {
			if (t.type == TokenType.Identifier) {
				if (t.data.matches(keywords))
					SetColoring(t.position, t.data.length(), "haxe.keyword");
				if (t.data.matches(classTypes))
					SetColoring(t.position, t.data.length(), "haxe.datatype");
			} else if (t.type == TokenType.Number) {
				SetColoring(t.position, t.data.length(), "haxe.numbers");
			}
		}
	}
}
```
Of course, this is far from being the best way to do it, this is why i said this part is fully up to you.

have fun writing your own HighlightingEngines, just keep in mind that the UpdateHighlight method should be fast to execute.
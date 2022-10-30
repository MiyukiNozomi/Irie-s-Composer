# Bad Apple Tutorials: Custom Auto Completion Suggestions

if you have ever pressed Control+SPACE, if you were typing anything a small window pops up showing a few options that you have written previously (in the case of the default editor and a few others);

that little window is called AutoCompletion, it provides you options to make your every-day programming life easier.

Bad Apple allows modules to also make their own completion suggestions,
just override HighlightEngine#RequestCompletion(Editor e) and boom, you'll now have a callback to do just that!

it would look something like this:
```java
	@Override
	public void RequestCompletion(Editor e) {
		// TODO Auto-generated method stub
		super.RequestCompletion(e);
	}
```

if you want the default word suggestion, you can just call the superclass's RequestCompletion.

now, you'll receive a handle to the editor in which that little completion window was requested.
## How to add words to the AutoCompletion window

First, lets take a look at the default RequestCompletion: 
```java
	public void RequestCompletion(Editor e) {
		// regex to only grab words
		String regex = "([^a-zA-Z']+)'*\\1*";
		// split that thing by the regex
		String[] split = getText().split(regex);
		
		// turn it into a list, remove duplicates
		List<String> words = Arrays.asList(split);
		List<String> withoutDuplicates = words.stream().distinct().collect(Collectors.toList());
		
		// now turn it into Completion Suggestions
		List<CompletionSuggestion> suggestions = new ArrayList<>();

		for (String s : withoutDuplicates) {
			suggestions.add(new CompletionSuggestion(CompletionType.Word, s, s));
		}

		// send it to the auto complete window.
		e.autoComplete.words = suggestions;
	}
```
Looks complicated, doesn't it?
Well, all it does is grab every word and send it to the auto completion window, nothing too special,

what actually interests us is the CompletionSuggestion class;
that class holds a completion type, a string representing its name, and another string of what it actually is:
```java
public CompletionSuggestion(CompletionType type, String title, String content)
```
for Example, lets say you want your nice little extension to provide a suggestion called "private_method", that when selected gives a sample private method.

that would look something like:
```java
	@Override
	public void RequestCompletion(Editor e) {
		// you don't need to do this if you're calling super.RequestCompletion
		e.autoComplete.words.clear();
		
		e.autoComplete.words.add(new CompletionSuggestion(CompletionType.Template, "private_method", "private function hello() {}"));
	}
```

and that's it, you could use this function to invoke a parser, 
something like that, personally i wouldn't recommend running a parser here, but since i haven't implemented support for error checking, feel free to do it.

now, if you compile your module and run bad apple,
open up a file, type in priva and then press Control + Space
you should see your suggestion there.

### warning: don't forget to reset the auto completion's words list, otherwise you could end up with several duplicates.

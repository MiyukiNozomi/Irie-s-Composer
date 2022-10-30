package com.miyuki.baddapple;

import java.io.File;
import java.util.Random;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;


public class DiscordPresence {

	private static DiscordRPC rpc;
	private static DiscordEventHandlers handlers;
	private static DiscordRichPresence presence;
	private static boolean failed;

	@SuppressWarnings("static-access")
	public static final boolean Init() {
		try {
			rpc = new DiscordRPC();
		} catch (Exception e) {
			e.printStackTrace();
			failed = true;
			return false;
		}
		String id = "1030632369629167646";
		handlers = new DiscordEventHandlers();
		rpc.discordInitialize(id, handlers, true);

		presence = new DiscordRichPresence();
		presence.smallImageText = "https://miyukinozomi.github.io/wiki";
		presence.largeImageKey = "icon";
		presence.startTimestamp = System.currentTimeMillis() / 1000;
		
		Reset();

		failed = false;
		return true;
	}
	
	@SuppressWarnings("static-access")
	public static final void Reset() {
		if (presence == null)
			return;
		String state = "Huh, Discord RPC doesn't supports Hiragana lol"; //

		switch (new Random().nextInt(10)) {
			case 1:
				state = "Created by MiyukiNozomi#6838";
				break;
			case 2:
				state = "Screw electron based editors";
				break;
			case 3:
				state = "Are ya coding son? no.";
				break;
			case 4:
				state = "Oh look, a weeb's editor!";
				break;
			case 5:
				state = "Haskell is fun, you should try it!";
				break;
			case 6:
				state = "D is great!, you should try it...";
				break;
			case 7:
				state = "https://wiki.dlang.org < best website";
				break;
			case 8:
				state = "'VSCode' was An Impostor.";
				break;
			case 9:
				state = "sus chromium";
				break;
			default:
				state = "analysis";
				break;
		}

		presence.state = state;
		rpc.discordUpdatePresence(presence);
	}

	@SuppressWarnings("static-access")
	public static final void SetCurrentFile(File editor, String activity) {
		if (failed)
			return;
		presence.state = activity + editor.getName();
		rpc.discordUpdatePresence(presence);
	}
}
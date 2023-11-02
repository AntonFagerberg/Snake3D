package com.antonfagerberg.snake3d;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.antonfagerberg.snake3d.Snake3D;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(800, 800);
		config.useVsync(true);
		config.setTitle("Snake3D");
		new Lwjgl3Application(new Snake3D(), config);
	}
}

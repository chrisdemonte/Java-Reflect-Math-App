package app;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Settings {
	
	public static double width;
	public static double height;
	public static int fontSize;
	public static Font mainFont;
	public static Color bgColor;
	public static Color borderColor;
	public static Color canvasColor;

	public static void init(double w, double h, int s) {
		width = w;
		height = h;
		fontSize = s;
		mainFont = new Font(fontSize);
		bgColor = Color.CORNFLOWERBLUE;
		borderColor = Color.SNOW;
		canvasColor = Color.LIGHTBLUE;
	}
}
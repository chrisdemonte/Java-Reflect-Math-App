package app;

import java.lang.reflect.Method;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	
	public static CalculatorWindow gui;
	public static Method[] methods;

	@Override
	public void start(Stage mainStage) throws Exception {
		Settings.init(600, 400, 16);
		methods = Math.class.getDeclaredMethods();	
		for (Method m : methods) m.setAccessible(true);
		gui = new CalculatorWindow();	
		mainStage.setScene(new Scene(gui, Settings.width, Settings.height));
		mainStage.setTitle("Java Reflect Math App");
		mainStage.show();
	}
	public static void main(String[] args) {
		Application.launch(args);
	}
}
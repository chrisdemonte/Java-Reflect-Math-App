package app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class CalculatorWindow extends Pane{
	
	public Method selectedMethod;
	public Object[] params;
	public String result;	
	
	public Pane canvas;
	public ComboBox<Method> dropDownMenu;
	public Label methodDisplay;
	public TextField[] varInputs;
	public Button calculateButton;
	public TextField outputDisplay;
	
	public CalculatorWindow () {
		
		this.guiForm(this, Settings.width, Settings.height, 0, 0);
		this.setBackground(new Background(new BackgroundFill(Settings.bgColor, new CornerRadii(0), new Insets(0,0,0,0))));
		this.setBorder(new Border(new BorderStroke(Settings.borderColor, BorderStrokeStyle.SOLID,new CornerRadii(0),new BorderWidths(2), new Insets(0,0,0,0))));
		
		this.canvas = new Pane();
		this.guiForm(canvas, Settings.width * .9, Settings.height * .9, Settings.width * .05, Settings.height * .05);
		this.canvas.setBackground(new Background(new BackgroundFill(Settings.canvasColor, new CornerRadii(10), new Insets(0,0,0,0))));
		this.canvas.setBorder(new Border(new BorderStroke(Settings.borderColor, BorderStrokeStyle.SOLID,new CornerRadii(10),new BorderWidths(2), new Insets(0,0,0,0))));
		
		this.selectedMethod = null;
		this.params = new Object[2];
		this.result = "Result";
		
		this.dropDownMenu = new ComboBox<Method>(FXCollections.observableArrayList(App.methods));
		this.guiForm(this.dropDownMenu, Settings.width * .85, Settings.height * .1, Settings.width * .075, Settings.height * .1);	
		this.dropDownMenu.setOnAction(e->{
			this.selectedMethod = this.dropDownMenu.getValue();
			this.methodDisplay.setText("Method : " + this.selectedMethod.toString());
			this.refreshInputFields();
		});
		
		this.methodDisplay = new Label();
		this.guiForm(this.methodDisplay, Settings.width * .85, Settings.height * .1, Settings.width * .075, Settings.height * .2);		
		this.methodDisplay.setFont(Settings.mainFont);
		
		this.varInputs = new TextField[2];
		this.varInputs[0] = new TextField();
		this.guiTextForm(this.varInputs[0], Settings.width * .85, Settings.height * .1, Settings.width * .075, Settings.height * .315, false, true);	
		
		this.varInputs[1] = new TextField();
		this.guiTextForm(this.varInputs[1], Settings.width * .85, Settings.height * .1, Settings.width * .075, Settings.height * .465, false, true);		
	
		this.calculateButton = new Button("Calculate");
		this.guiForm(this.calculateButton, Settings.width * .85, Settings.height * .1, Settings.width * .075, Settings.height * .615);	
		this.calculateButton.setOnAction(e->{
			this.runCalculation();
			});
		this.calculateButton.setFont(Settings.mainFont);
		
		this.outputDisplay = new TextField(result);
		this.guiTextForm(this.outputDisplay, Settings.width * .85, Settings.height * .1, Settings.width * .075, Settings.height * .765, true, false);		
		
		this.getChildren().addAll( this.canvas, this.dropDownMenu, this.varInputs[0], this.varInputs[1], this.calculateButton, this.methodDisplay, this.outputDisplay);
	}
	private void guiTextForm(TextField node, double width, double height, double x, double y, boolean vis, boolean edit) {
		this.guiForm(node, width, height, x, y);
		node.setVisible(vis);
		node.setEditable(edit);
		node.setFont(Settings.mainFont);
		node.setAlignment(Pos.BASELINE_RIGHT);
	}
	private void guiForm(Region node , double width, double height, double x, double y) {
		node.setMinSize(width, height);
		node.setPrefSize(width, height);
		node.setLayoutX(x);
		node.setLayoutY(y);
	}
	private void refreshInputFields() {
		for (int i = 0; i < 2; i++) {
			if (this.selectedMethod.getParameterCount() > i) {
				this.varInputs[i].setVisible(true);
			}
			else {
				this.varInputs[i].setVisible(false);
				this.varInputs[i].setText("");
			}
		}
	}
	private void runCalculation() {
		if (this.selectedMethod != null) {
			try {
				this.setParams();
				if (this.selectedMethod.getParameterCount() == 2) {
					this.result = "" + this.selectedMethod.invoke(Math.class, params[0], params[1]);
				} else if (this.selectedMethod.getParameterCount() == 1) {
					this.result = "" + this.selectedMethod.invoke(Math.class, params[0]);
				} else {
					this.result = "" + this.selectedMethod.invoke(Math.class);
				}
			} catch (IllegalAccessException e) {
				this.result = this.selectedMethod.getName() + " is set to private.";
			} catch (IllegalArgumentException e) {
				this.result = "Invalid Entry: " + e.getMessage();
			} catch (InvocationTargetException e) {
				if(e.getCause() instanceof ArithmeticException) {
					this.result = "Arithmetic Error: " + e.getCause().getMessage();
				}
			} 
			this.outputDisplay.setText(result);
		}	
	}
	private void setParams() {			
		for (int i = 0; i < 2; i++) {
			params[i] = null;
			if (this.selectedMethod.getParameterCount() > i) {
				String type = selectedMethod.getParameterTypes()[i].getName();
				if (type.equals("float")) {
					params[i] = Float.parseFloat(this.varInputs[i].getText());
				}
				else if (type.equals("double")) {
					params[i] =Double.parseDouble(this.varInputs[i].getText());
				}
				else if (type.equals("int")) {
					params[i] =Integer.parseInt(this.varInputs[i].getText());
				}
				else if (type.equals("long")) {
					params[i] =Long.parseLong(this.varInputs[i].getText());
				}
			}
		}
	}
}
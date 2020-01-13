package dungeonshooter;

import java.util.List;

import dungeonshooter.animator.Animator;
import dungeonshooter.entity.property.Drawable;
import dungeonshooter.entity.Entity;
import dungeonshooter.entity.property.HitBox;
import dungeonshooter.entity.Player;
import dungeonshooter.entity.PlayerInput;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utility.InputAdapter;

public class DungeonShooter extends Application{

	/**
	 * size of the scene
	 */
	private double width = 700, height = 700;

	private BorderPane root;

	private CanvasMap board;

	private PlayerInput input;

	private String title = "Dungeon Shooter";

	private Color background = Color.DARKCYAN;
	
	private Canvas canvas;
	private InputAdapter adapter;

	public void init() {
		canvas = new Canvas();
		adapter = new InputAdapter(canvas);
		input = new PlayerInput(adapter);
		board = new CanvasMap();

		Player player = new Player(width / 2, height / 2, 70, 46);
		player.setInput(input);
		player.setMap(board);
		Animator animator = new Animator();
		animator.setCanvas(board);
		board.setDrawingCanvas(canvas);
		board.setAnimator(animator);
		board.addSampleShapes();
		List<Entity> players = board.players();

		players.add(player);
		ToolBar statusBar = createStatusBar();
		ToolBar optionsBar = createOptionsBar();

		root = new BorderPane();

		root.setTop(optionsBar);
		// call setCenter on it and pass to it board.getCanvas()
		root.setCenter(board.getCanvas());
		// call setBottom on it and pass to it the status bar
		root.setBottom(statusBar);

		canvas.heightProperty()
				.bind(root.heightProperty().subtract(statusBar.heightProperty().subtract(optionsBar.heightProperty())));

		canvas.widthProperty().bind(root.widthProperty());

	}

	public void start(Stage primaryStage) throws Exception {
		// scene holds all JavaFX components that need to be displayed in Stage
		Scene scene = new Scene(root, width, height, background);
		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
		// when escape key is pressed close the application
		primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (KeyCode.ESCAPE == event.getCode()) {
				primaryStage.hide();
			}
		});
		// display the JavaFX application
		primaryStage.show();
		board.start();
	}

	public void stop() throws Exception {
		board.stop();
	}

	public ToolBar createOptionsBar() {
		// use the createButton method and create a start button with lambda that calls
		// board.start()

		Button startButton = createButton("Start", (event) -> {
			board.start();
		});

		// use the createButton method and create a start button with lambda that calls
		// board.stop()
		Button stopButton = createButton("Stop", (event) -> {
			board.stop();
		});
		// create 2 Pane object called filler1 and filler2
		Pane filler1 = new Pane();
		Pane filler2 = new Pane();

		// Pane class is a super class of all layout mangers. by itself it has no rules.
		// call the static method setHgrow from Hbox with Filler1, Priority.ALWAYS
		HBox.setHgrow(filler1, Priority.ALWAYS);
		// call the static method setHgrow from Hbox with Filler2, Priority.ALWAYS
		HBox.setHgrow(filler2, Priority.ALWAYS);
		// this will allow the filler to expand and fill the space between nodes

		// create a Spinner object called rayCount with generic type of Integer
		// in the constructor pass to it 1 as min, Integer.MAX_VALUE as max and 360*3 as
		// current
		Spinner<Integer> rayCount = new Spinner<>(1, Integer.MAX_VALUE, 360 * 3);
		// call setEditable on it and set to true so the counter can be changed by
		// typing in it.
		rayCount.setEditable(true);
		// call setMaxWidth on it and set 100, as default size it too big
		rayCount.setMaxWidth(100);

		MenuButton options = new MenuButton("Options", null, createCheckBox("FPS", true, board.drawFPSProperty()),
				createCheckBox("Bounds", false, board.drawBoundsProperty()));

		ToolBar toolBar = new ToolBar(startButton, stopButton, filler1, rayCount, options);

		return toolBar;
	}

	public ToolBar createStatusBar() {

		// create two Label objects and with value of "(0,0)".
		Label mouseCoordLabel = new Label("(0,0)");
		Label dragCoordLabel = new Label("(0,0)");

		ToolBar statusBar = new ToolBar(new Label("Mouse: "), mouseCoordLabel, new Label("Drag: "), dragCoordLabel);

		return statusBar;

	}

	public CheckMenuItem createCheckBox(String text, boolean isSelected, BooleanProperty binding) {
		// create a new CheckMenuItem object with given text.
		CheckMenuItem check = new CheckMenuItem(text);
		// call bind on binding and pass to it check.selectedProperty(),
		binding.bind(check.selectedProperty());
		// this will notify the binding object every time check.selectedProperty() is
		// changed.
		// call setSelected and pass to it isSelected.
		check.setSelected(isSelected);
		// return the created CheckMenuItem.
		return check;
	}

	public Button createButton(String text, EventHandler<ActionEvent> action) {

		// create a new Button object with given text
		Button button = new Button(text);
		// call setOnAction on created button and give it action
		button.setOnAction(action);
		// return the created button
		return button;

	}

	public static void main(String[] args) {
		// launch( args); method starts the javaFX application.
		// some IDEs are capable of starting JavaFX without this method.
		launch(args);
	}
}

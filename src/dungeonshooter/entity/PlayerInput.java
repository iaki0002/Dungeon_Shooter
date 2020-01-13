package dungeonshooter.entity;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import utility.InputAdapter;

public class PlayerInput {
	private double x;
	private double y;
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private boolean leftClick = false;
	private boolean rightClick = false;
	private boolean middleClick = false;
	private boolean space = false;
	private boolean shift = false;
	private InputAdapter adapter;

	public PlayerInput(InputAdapter adapter) {
		adapter.forceFocusWhenMouseEnters();
		adapter.registerMouseMovment(this::moved, this::dragged);
		adapter.registerMouseClick(this::mousePressed, this::mouseRelease);
		adapter.registerKey(this::keyPressed, this::keyRelease);
	}

	public boolean hasMoved() {
		return left || right || up || down;
	}

	public int leftOrRight() {
		if(left == false && right == false) {
			return 0;
		}
		if(right == true) {
			return 1;
		} else {
			return -1;
		}
	}

	public int upOrDown() {
		if(up == false && down == false) {
			return 0;
		}
		if(down == true) {
			return 1;
		} else {
			return -1;
		}
	}

	public boolean leftClicked() {
		return leftClick;
	}

	public boolean rightClicked() {
		return rightClick;
	}

	public boolean middleClicked() {
		return middleClick;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	protected void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		leftClick = e.isPrimaryButtonDown();
		rightClick = e.isSecondaryButtonDown();
		middleClick = e.isMiddleButtonDown();
	}

	protected void mouseRelease(MouseEvent e) {
		leftClick = false;
		rightClick = false;
		middleClick = false;
	}

	public void changeKeyStatus(KeyCode key, boolean isPressed) {
		switch (key) {
		case W:
			up = isPressed;
			break;
		case A:
			left = isPressed;
			break;
		case S:
			down = isPressed;
			break;
		case D:
			right = isPressed;
			break;
		case SHIFT:
			shift = isPressed;
			break;
		case SPACE:
			space = isPressed;
			break;
		default:
			break;
		}
	}

	protected void keyPressed(KeyEvent key) {
		changeKeyStatus(key.getCode(), true);
	}

	protected void keyRelease(KeyEvent key) {
		changeKeyStatus(key.getCode(), false);
	}

	public boolean isSpace() {
		return space;
	}

	public boolean isShift() {
		return shift;
	}

	protected void moved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	protected void dragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonshooter.entity;

import dungeonshooter.entity.property.HitBox;
import dungeonshooter.entity.property.Sprite;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import utility.Point;
import dungeonshooter.CanvasMap;

/**
 *
 * @author Dimitri
 */

public class Player implements Entity {

	private Rotate rotationPlayer;
	private double angle;
	private double playerFrame = 0;
	private double muzzleFrame = 0;
	private Point pos;
	private Point dimension;
	private Point prev;
	private Sprite sprite;
	private HitBox hitbox;

	private PlayerInput input;
	private CanvasMap map;

	public Player(double x, double y, double w, double h) {
		rotationPlayer = new Rotate();
		pos = new Point(x - w / 2, y - h / 2);
		prev = new Point(pos);
		dimension = new Point(w, h);

		sprite = new Sprite() {
			private final Image[] PLAYER = new Image[20];
			private final Image[] MUZZLE = new Image[16];
			{
				for (int i = 0; i < PLAYER.length; i++) {
					PLAYER[i] = new Image("file:assets/rifle/idle/survivor-idle_rifle_" + i + ".png");
				}
				for (int i = 0; i < MUZZLE.length; i++) {
					MUZZLE[i] = new Image("file:assets/muzzle_flashs/m_" + i + ".png");
				}
			}
			
			@Override
			public void draw(GraphicsContext gc) {
				gc.save();
				gc.setTransform(rotationPlayer.getMxx(), rotationPlayer.getMyx(), rotationPlayer.getMxy(),
						rotationPlayer.getMyy(), rotationPlayer.getTx(), rotationPlayer.getTy());
				if (input.leftClicked()) {
					gc.drawImage(MUZZLE[(int) muzzleFrame], getRifleMuzzleX() - 8, getRifleMuzzleY() - 25, 50, 50);
					muzzleFrame += 0.5;
				}

				gc.drawImage(PLAYER[(int) playerFrame], pos.x(), pos.y(), dimension.x(), dimension.y());
				gc.restore();

				playerFrame += 0.25;

				if (playerFrame >= PLAYER.length) {
					playerFrame = 0;
				}
				if (muzzleFrame >= MUZZLE.length || !input.leftClicked()) {
					muzzleFrame = 0;
				}
			}

		};
		
		double size = h * .74;
		hitbox = new HitBox().setBounds(pos.x() + dimension.x() * 0.303 - size / 2, pos.y() + dimension.y() * 0.58 - size / 2, size, size);
	}

	
	public Player setMap(CanvasMap map) {
		this.map = map;
		return this;
	}

	public double getPlayerCenterX() {
		return pos.x() + dimension.x() * 0.303;
	}

	public double getPlayerCenterY() {
		return pos.y() + dimension.y() * 0.58;
	}

	public double getRifleMuzzleX() {
		return pos.x() + dimension.x() * 0.93;
	}

	public double getRifleMuzzleY() {
		return pos.y() + dimension.y() * 0.73;
	}

	public void calculateAngles() {
		angle = Math.toDegrees( Math.atan2( input.y() - getPlayerCenterY(), input.x() - getPlayerCenterX())); 
		rotationPlayer.setAngle(this.angle);
		rotationPlayer.setPivotX(getPlayerCenterX());
		rotationPlayer.setPivotY(getPlayerCenterY());
	}

	public void stepBack() {
		hitbox.undoTranslate();
		pos.move(prev);
	}

    @Override
	public void update() {
		calculateAngles();
		
		if (input.leftClicked()) {
			Point2D muzzle = rotationPlayer.transform(getRifleMuzzleX(), getRifleMuzzleY());
			map.fireBullet(new Bullet(this.angle, muzzle.getX(), muzzle.getY()));
		}
		if (input.hasMoved()) {
			prev.move(pos);
			pos.translate(input.leftOrRight(), input.upOrDown());
			hitbox.translate(input.leftOrRight(), input.upOrDown());
		}
		
	}

    @Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public boolean hasHitBox() {
		// TODO Auto-generated method stub
		return true;
	}

    @Override
	public HitBox getHitBox() {
		return hitbox;
	}

    @Override
	public Sprite getDrawable() {
		return sprite;

	}

	public PlayerInput getInput() {
		return input;
	}

	public void setInput(PlayerInput input) {
		this.input = input;
	}

}


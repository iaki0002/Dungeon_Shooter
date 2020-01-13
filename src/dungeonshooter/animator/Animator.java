/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonshooter.animator;

import java.util.Iterator;
import java.util.List;

import dungeonshooter.CanvasMap;
import dungeonshooter.entity.Bullet;
import dungeonshooter.entity.Entity;
import dungeonshooter.entity.Player;
import dungeonshooter.entity.PolyShape;
import dungeonshooter.entity.property.HitBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Animator extends AbstractAnimator {
	private Color background = Color.ANTIQUEWHITE;
	
	public void updateEntities() {
		
		//Questions about bullets and the flow of sequence diagram
		List<Entity> bullets = map.projectiles();
		List<PolyShape> staticShapes = map.staticShapes();
		List<Entity> players = map.players();
		
		for(Entity e: bullets) {
			e.update();
		}
		
		for(Entity e: players) {
			e.update();
		}
		
		for(Entity e: staticShapes) {
			e.update();
		}
		
		if(map.getDrawBounds()) {
			for(Entity e: bullets) {
				e.getHitBox().getDrawable().setStroke(Color.RED);
			
			}
			
			for(Entity e: players) {
				e.getHitBox().getDrawable().setStroke(Color.RED);
			
			}
		}
		
		for(Entity e: staticShapes) {
			this.proccessEntityList(bullets.iterator(), e.getHitBox());
			this.proccessEntityList(players.iterator(), e.getHitBox());
		}

		
	}
	
	
	//what is bounds?
	public void proccessEntityList(Iterator<Entity> iterator, HitBox shapeHitBox) {
		
		//checking if the iterator hit the hitbox
		
		
		//what is bounds? in the first if condtion
		
		while(iterator.hasNext()) {
			
			//iterator.next() is a getter so we needd to store it in an Entity object
			Entity e = iterator.next();
			HitBox bounds = e.getHitBox();
			
			if(!map.inMap(bounds)) {
				if(e instanceof Player) {
					((Player) e).stepBack();
				} else if (e instanceof Bullet) {
					iterator.remove();
				}
				//Question what is bounds in sequence diagram?
			} else if (shapeHitBox.intersectBounds(bounds)) {
				
				if(map.getDrawBounds()) {
					e.getHitBox().getDrawable().setStroke(Color.BLUEVIOLET);
				}
				
				if(shapeHitBox.intersectFull(bounds)) {
					if(e instanceof Player) {
						((Player) e).stepBack();
					} else if(e instanceof Bullet) {
						iterator.remove();
					}
				}
				
			}
			
			
		}
	}
	
	@Override
	protected void handle(long now, GraphicsContext gc) {
		updateEntities();
		clearAndFill(gc, background);
		drawEntities(gc);
	}
}

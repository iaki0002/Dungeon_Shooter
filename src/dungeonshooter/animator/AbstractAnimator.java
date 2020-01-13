package dungeonshooter.animator;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import dungeonshooter.CanvasMap;
import dungeonshooter.entity.Entity;
import dungeonshooter.entity.FpsCounter;
import dungeonshooter.entity.PolyShape;
import java.util.function.Consumer;
import utility.Point;

/**
 * this class must extend {@link AnimationTimer}. job of this class is to hold common functionality among animators.
 * 
 * @author Shahriar (Shawn) Emami
 * @version Jan 13, 2019
 */
public abstract class AbstractAnimator extends AnimationTimer{
	
	/**
	 * create a private fps counter object to keep track of fps
	 */
	private FpsCounter fps;

	/**
	 * create a protected class variable of type {@link CanvasMap} and name it map.
	 */
	protected CanvasMap map;
	
	/**
	 * create a protected class variable of type {@link Point} and name it mouse.
	 */
	protected Point mouse;
	
	/**
	 * create a protected constructor and initialize the {@link AbstractAnimator#mouse} variable
	 */
	public AbstractAnimator(){
		mouse = new Point();
		fps = new FpsCounter( 10, 25);
		fps.getDrawable().setFill( Color.BLACK).setStroke( Color.WHITE).setWidth( 1);
	}
	
	/**
	 * create a setter called setCanvas to inject (set) the {@link CanvasMap}
	 * @param map - {@link CanvasMap} object
	 */
	public void setCanvas( CanvasMap c){
		this.map = c;
	}

	/**
	 * create a method called mouseDragged that is called every time the position of mouse changes.
	 * @param e - {@link MouseEvent} object that hold the details of the mouse. use {@link MouseEvent#getX} and {@link MouseEvent#getY}
	 */
	public void mouseDragged( MouseEvent e){
		mouse.x( e.getX());
		mouse.y( e.getY());
	}

	/**
	 * create a method called mouseMoved that is called every time the position of mouse changes.
	 * @param e - {@link MouseEvent} object that hold the details of the mouse. use {@link MouseEvent#getX} and {@link MouseEvent#getY}
	 */
	public void mouseMoved( MouseEvent e){
		mouse.x( e.getX());
		mouse.y( e.getY());
	}

	public void clearAndFill( GraphicsContext gc, Color background){
		gc.setFill( background);
		gc.clearRect( 0, 0, map.w(), map.h());
		gc.fillRect( 0, 0, map.w(), map.h());
	}
	
	/**
	 * <p>create a method called handle that is inherited from {@link AnimationTimer#handle(long)}.
	 * this method is called by JavaFX application, it should not be called directly.</p>
	 * <p>inside of this method call the abstract handle method {@link AbstractAnimator#handle(GraphicsContext, long)}.
	 * {@link GraphicsContext} can be retrieved from {@link CanvasMap#gc()}</p>
	 * @param now - current time in nanoseconds, represents the time that this function is called.
	 */
	@Override
	public void handle( long now){
		GraphicsContext gc = map.gc();

		if(map.getDrawFPS()) {
			this.fps.calculateFPS(now);
		}
		handle(now, gc);

		if(map.getDrawBounds()) {
			for (int i =0; i < map.staticShapes().size(); i++) {
                map.staticShapes().get(i).getHitBox().getDrawable().draw(gc);
			}
        }
		
		if(map.getDrawFPS()) {
			fps.getDrawable().draw(gc);
		}
	}

	/**
	 * create a protected abstract method called handle, this method to be overridden by subclasses.
	 * @param gc - {@link GraphicsContext} object.
	 * @param now - current time in nanoseconds, represents the time that this function is called.
	 */
	abstract void handle( long now, GraphicsContext gc);
    
    public void drawEntities(GraphicsContext gc) {
		map.updateProjectilesList();
		Consumer<Entity> draw = e ->{
			if (e.isDrawable()) {
				e.getDrawable().draw(gc);
				if(map.getDrawBounds()) {
					e.getHitBox().getDrawable().draw(gc);
				}
			}
		};
        draw.accept(map.getMapShape());

		for (PolyShape e : map.staticShapes()) {
			draw.accept(e);
		}
		
		for (Entity e : map.projectiles()) {
			draw.accept(e);
		}
		
		for (Entity e : map.players()) {
			draw.accept(e);
		}
    }
}

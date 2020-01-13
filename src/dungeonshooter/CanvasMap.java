package dungeonshooter;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import dungeonshooter.animator.AbstractAnimator;
import dungeonshooter.animator.Animator;
import dungeonshooter.entity.Bullet;
import dungeonshooter.entity.Entity;
import dungeonshooter.entity.PolyShape;
import dungeonshooter.entity.property.HitBox;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * this class represents the drawing area. it is backed by {@link Canvas} class.
 * this class itself does not handle any of the drawing. this task is accomplished
 * by the {@link AnimationTimer}.
 * 
 * @author Shahriar (Shawn) Emami
 * @version Jan 13, 2019
 */
public class CanvasMap {
    private Canvas map;
    private Animator animator;
    private BooleanProperty drawBounds;
    private BooleanProperty drawFPS;
    private PolyShape border;
    private List<Entity> players;
    private List<Entity> projectiles;
    private List<Entity> buffer;
    private List<PolyShape> staticShapes;
    /**
     * create a constructor and initialize all class variables.
     */
    public CanvasMap(){
        drawBounds = new SimpleBooleanProperty();
        drawFPS = new SimpleBooleanProperty();
        animator = new Animator();
        border = new PolyShape();
        projectiles = new ArrayList<>(500);
        buffer = new ArrayList<>(500);
        staticShapes = new ArrayList<>(50);
        players = new ArrayList<>(1);
        border.setPoints(0,0,700,0,700,700,0,700);
        border.getDrawable().setFill(new ImagePattern(new Image( "file:assets/floor/pavin.png"), 0, 0, 256, 256, false));
    }
    
    /**
     * create the property class variables functions here
     */
    public BooleanProperty drawFPSProperty(){
        return drawFPS;
    }

    public boolean getDrawFPS(){
        return drawFPS.get();
    }

    public BooleanProperty drawBoundsProperty(){
        return drawBounds;
    }

    public boolean getDrawBounds(){
        return drawBounds.get();
    }

    /**
     * create a method called setAnimator.
     * set an {@link AbstractAnimator}. if an animator exists {@link CanvasMap#stop()} it and 
     * call {@link CanvasMap#removeMouseEvents()}. then set the new animator and
     * call {@link CanvasMap#start()} and {@link CanvasMap#registerMouseEvents()}.
     * @param newAnimator - new {@link AbstractAnimator} object 
     * @return the current instance of this object
     */
    public CanvasMap setAnimator(Animator newAnimator){
        if( animator != null){
            stop();
        }
        animator = newAnimator;
        return this;
    }
    
    public CanvasMap setDrawingCanvas(Canvas map) {
		this.map = map;
        this.map.widthProperty().addListener((data, oldvalue, newvalue) -> border.setPoints(0,0,w(),0,w(),h(),0,h())); 
        this.map.heightProperty().addListener((data, oldvalue, newvalue) -> border.setPoints(0,0,w(),0,w(),h(),0,h()));
		return this;
	}
    
    /**
     * <p>create a method called registerMouseEvents.
     * register the mouse events for when the mouse is 
     * {@link MouseEvent#MOUSE_MOVED} or {@link MouseEvent#MOUSE_DRAGGED}.<br>
     * call {@link CanvasMap#addEventHandler} twice and pass to it
     * {@link MouseEvent#MOUSE_DRAGGED}, {@link animator#mouseDragged} and
     * {@link MouseEvent#MOUSE_MOVED}, {@link animator#mouseMoved}.</p>
     * <p>a method can be passed directly as an argument if the method signature matches
     * the functional interface. in this example you will pass the animator method using
     * object::method syntax.</p>
     */
    public void registerMouseEvents(){
        addEventHandler(MouseEvent.MOUSE_DRAGGED, animator::mouseDragged);
        addEventHandler(MouseEvent.MOUSE_MOVED, animator::mouseMoved);
    }

    /**
     * <p>create a method called removeMouseEvents.
     * remove the mouse events for when the mouse is 
     * {@link MouseEvent#MOUSE_MOVED} or {@link MouseEvent#MOUSE_DRAGGED}.<br>
     * call {@link CanvasMap#addEventHandler} twice and pass to it
     * {@link MouseEvent#MOUSE_DRAGGED}, {@link animator#mouseDragged} and
     * {@link MouseEvent#MOUSE_MOVED}, {@link animator#mouseMoved}.</p>
     * <p>a method can be passed directly as an argument if the method signature matches
     * the functional interface. in this example you will pass the animator method using
     * object::method syntax.</p>
     */
    public void removeMouseEvents(){
        removeEventHandler( MouseEvent.MOUSE_DRAGGED, animator::mouseDragged);
        removeEventHandler( MouseEvent.MOUSE_MOVED, animator::mouseMoved);
    }

    /**
     * <p>
     * register the given {@link EventType} and {@link EventHandler}
     * </p>
     * @param event - an event such as {@link MouseEvent#MOUSE_MOVED}.
     * @param handler - a lambda to be used when registered event is triggered.
     */
    public <E extends Event> void addEventHandler(EventType< E> event, EventHandler< E> handler){
        map.addEventHandler(event, handler);
    }

    /**
     * <p>
     * remove the given {@link EventType} registered with {@link EventHandler}
     * </p>
     * @param event - an event such as {@link MouseEvent#MOUSE_MOVED}.
     * @param handler - a lambda to be used when registered event is triggered.
     */
    public <E extends Event> void removeEventHandler(EventType< E> event, EventHandler< E> handler){
        map.removeEventHandler(event, handler);
    }

    /**
     * create a method called start.
     * start the animator. {@link AnimationTimer#start()}
     */
    public void start(){
        animator.start();
    }

    /**
     * create a method called stop.
     * stop the animator. {@link AnimationTimer#stop()}
     */
    public void stop(){
        animator.stop();
    }

    /**
     * create a method called getCanvas.
     * get the JavaFX {@link Canvas} node 
     * @return {@link Canvas} node 
     */
    public Canvas getCanvas(){
        return map;
    }

    /**
     * create a method called gc.
     * get the {@link GraphicsContext} of {@link Canvas} that allows direct drawing.
     * @return {@link GraphicsContext} of {@link Canvas}
     */
    public GraphicsContext gc(){
        return map.getGraphicsContext2D();
    }

    /**
     * create a method called w.
     * get the height of the map, {@link Canvas#getHeight()}
     * @return height of canvas
     */
    public double h(){
        return map.getHeight();
    }

    /**
     * create a method called w.
     * get the width of the map, {@link Canvas#getWidth()}
     * @return width of canvas
     */
    public double w(){
        return map.getWidth();
    }

    /**
     * get the list of all shapes.
     * @return list of shapes
     */
    public List< PolyShape> staticShapes(){
        return staticShapes;
    }
    
    public List<Entity> players() {
        return players;
    }
    
    public List<Entity> projectiles() {
        return projectiles;
    }
    
	public void fireBullet(Bullet bullet) {
		buffer.add(bullet);
	}
	
	public void updateProjectilesList() {
		projectiles.addAll(buffer);
		buffer.clear();
	}
	
	public PolyShape getMapShape() {
		return border;
	}
	
	public boolean inMap(HitBox hitbox) {
		return border.getHitBox().containsBounds(hitbox);
		
	}

    /**
     * load a set of sample {@link PolyShapes}
     */
    public void addSampleShapes(){
        PolyShape s = new PolyShape().setPoints( 100, 100, 200, 100, 200, 200, 100, 200);
        s.getDrawable().setStroke(Color.BLACK).setWidth( 1.5);
        staticShapes.add(s);
        s = new PolyShape().setPoints( 100, 100, 200, 100, 200, 200, 100, 200);
        s.getDrawable().setStroke(Color.BLACK).setWidth( 1.5);
        staticShapes.add(s);
        s = new PolyShape().randomize( 350, 150, 100, 3, 10);
        s.getDrawable().setStroke(Color.BLACK).setWidth( 1.5);
        staticShapes.add(s);
        s = new PolyShape().setPoints( 600, 100, 500, 100, 450, 200, 550, 200);
        s.getDrawable().setStroke(Color.BLACK).setWidth( 1.5);
        staticShapes.add(s);
        s = new PolyShape().randomize( 550, 350, 100, 3, 10);
        s.getDrawable().setStroke(Color.BLACK).setWidth( 1.5);
        staticShapes.add(s);
        s = new PolyShape().setPoints( 600, 600, 500, 600, 550, 500);
        s.getDrawable().setStroke(Color.BLACK).setWidth( 1.5);
        staticShapes.add(s);
        s = new PolyShape().randomize( 350, 550, 100, 3, 10);
        s.getDrawable().setStroke(Color.BLACK).setWidth( 1.5);
        staticShapes.add(s);
        s = new PolyShape().setPoints( 100, 600, 200, 600, 250, 500, 150, 500);
        s.getDrawable().setStroke(Color.BLACK).setWidth( 1.5);
        staticShapes.add(s);
        s = new PolyShape().randomize( 150, 350, 100, 3, 10);
        s.getDrawable().setStroke(Color.BLACK).setWidth( 1.5);
        staticShapes.add(s);
    }
}

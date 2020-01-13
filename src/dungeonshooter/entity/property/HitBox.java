/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonshooter.entity.property;

import dungeonshooter.entity.Entity;
import dungeonshooter.entity.geometry.RectangleBounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utility.IntersectUtil;
import utility.Point;

/**
 *
 * @author Dimitri
 */
public class HitBox implements Entity {
    private Point prev;
    private RectangleBounds bounds;
    private Sprite sprite;
    private double[][] points;
    private double[] result;
    
    public HitBox() {
        prev = new Point();
        points = new double[2][4];
        result = new double[4];
        sprite = new Sprite() {
            @Override
            public void draw(GraphicsContext gc) {
                gc.setStroke(getStroke());
                gc.setLineWidth(getWidth());
                gc.strokeRect(bounds.x(), bounds.y(), bounds.w(), bounds.h());
            }
        };
        sprite.setStroke(Color.RED).setWidth( 3);
    }
    
    public HitBox setBounds(RectangleBounds bounds) {
        this.bounds = bounds;
        return this;
    }
    
    public HitBox setBounds(double x, double y, double width, double height) {
        return this.setBounds(new RectangleBounds(x, y, width, height));
    }
    
    public HitBox translate(double dx, double dy) {
        prev.move(bounds.startPos());
        this.bounds.translate(dx, dy);
        return this;
    }
    
    public HitBox undoTranslate() {
        this.bounds.move(prev);
        return this;
    }
    
    public boolean containsBounds(HitBox box) {
        return this.bounds.contains(box.bounds);
    }
    
    public boolean intersectBounds(HitBox box) {
        return this.bounds.intersects(box.bounds);
    }
    
    public boolean intersectFull(HitBox box) {
        return this.intersectBounds(box) &&
            this.intersectFull(box.getPoints());
    }
    
    protected boolean intersectFull(double[][] otherPoints) {
        points = getPoints();
		
		for (int i = 0, j = points[0].length-1; i<points[0].length; i++, j = i-1) {
			for (int k = 0, h = otherPoints[0].length - 1; k < otherPoints[0].length; k++, h = k - 1) {
				boolean doesIntersect = IntersectUtil.getIntersection(result, points[0][i], points[1][i], points[0][j], points[1][j],otherPoints[0][k],otherPoints[1][k],otherPoints[0][h], otherPoints[1][h]);
				if(doesIntersect && result[2] <= 1) {
					return true;
				}
			}
		}
		
		return false;
    }
    
    protected boolean hasIntersectFull() {
        return false;
    }
    
    protected double[][] getPoints() {
        return bounds.toArray(points);
    }
    
    public void update() {}
    
    public boolean hasHitbox() {
        return true;
    }
    
    public HitBox getHitBox() {
        return this;
    }
    
    public RectangleBounds getBound() {
        return this.bounds;
    }
    
    public Point getPrev() {
        return this.prev;
    }

    @Override
    public boolean isDrawable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Drawable<?> getDrawable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasHitBox() {
        return false;
    }
    
    @Override
    public String toString() {
        return "HitBox";
    }
    
}

package dungeonshooter.entity;

import java.util.ArrayList;
import java.util.List;

import dungeonshooter.entity.property.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import dungeonshooter.entity.Entity;
import dungeonshooter.entity.geometry.RectangleBounds;
import dungeonshooter.entity.property.HitBox;
import utility.Point;
import utility.RandUtil;

/**
 * this polyshape class can create a shape based on points given to it,
 * or it can create a polyshape based on random points. the sides randomly 
 * generated polyshape never cross each other.
 * 
 * @author Shahriar (Shawn) Emami
 * @version Jan 12, 2019
 */
public class PolyShape implements Entity{

	/**
	 * total number of points in this shape
	 */
	private int pointCount;
	/**
	 * all the points in this shape, each point is in a column, x values are in row 1 and y in row 2
	 */
	private double[][] points;
	/**
	 * min and max values used to determine the rectangle around the points in the shape.
	 */
	private double minX, minY, maxX, maxY;
	/**
	 * Rectangle covering all the points in this shape
	 */
	private HitBox hitbox;
	private Sprite sprite;

	/**
	 * create an empty instance of this shape.
	 */
	public PolyShape() {
        hitbox = new HitBox() {
			protected boolean hasIntersectFull() {
				return true;
			}
			protected double[][] getPoints() {
				return points;
			}
		};
		sprite = new Sprite() {{
                setFill(Color.DARKCYAN);
                setStroke(Color.GREEN);
            }

			@Override
			public void draw(GraphicsContext gc){
				gc.setLineWidth(getWidth());
				if(getStroke() != null){
					gc.setStroke(getStroke());
					gc.strokePolygon( points[0], points[1], pointCount);
				}
				if(getFill() != null) {
					gc.setFill(getFill());
					gc.fillPolygon(points[0], points[1], pointCount);
				}
			}
		};
	}

	/**
	 * create a random shape at a given (x,y) with a range of sides and max size.
	 * @param centerX - initial center in x direction which random point are generated around
	 * @param centerY - initial center in y direction which random point are generated around
	 * @param size - max size (radius) which points can generate from center
	 * @param minPoints - inclusive, min number of sides in this shape
	 * @param maxPoints - exclusive, max number of points in this shape
	 * @return current instance of this object
	 */
	public PolyShape randomize( double centerX, double centerY, double size, int minPoints, int maxPoints){
		pointCount = RandUtil.getInt( minPoints, maxPoints);
		points = new double[2][pointCount];
		final Point center = new Point( centerX, centerY);
		List< Point> unsortedPoints = new ArrayList<>( pointCount);
		centerX = 0;
		centerY = 0;
		//create random points using the Point class
		for( int i = 0; i < pointCount; i++){
			Point randP = center.randomPoint( size);
			unsortedPoints.add( randP);
			//keep the total of all x and y values to calculate the new center
			centerX += randP.x();
			centerY += randP.y();
		}
		//set the new center
		center.set( centerX / pointCount, centerY / pointCount);
		//sort all the points based on their angular relationship to center
		//this prevents the lines from crossing each other.
		unsortedPoints.sort( ( p1, p2) -> center.angle( p1) > center.angle( p2) ? 1 : -1);
		int i = 0;
		//set the min and max of x to first index of unsortedPoints
		//set the min and max of y to first index of unsortedPoints
		minX = maxX = unsortedPoints.get( 0).x();
		minY = maxY = unsortedPoints.get( 0).y();
		//store all points in the points array
		for( Point p : unsortedPoints){
			//check each point for potential min and max
			updateMinMax( p.x(), p.y());
			points[0][i] = p.x();
			points[1][i] = p.y();
			i++;
		}
		hitbox.setBounds(minX, minY, maxX-minX, maxY-minY);
		return this;
	}

	/**
	 * set the current points with the given points
	 * @param nums - points in format of x1,y1,x2,y2,x3,y3,...
	 * @return current instance of this object
	 */
	public PolyShape setPoints( double...nums){
		//set the min and max of x to first index of nums
		//set the min and max of y to second index of nums
		minX = maxX = nums[0];
		minY = maxY = nums[1];
		//number of points is half the size of nums.length
		pointCount = nums.length / 2;
		points = new double[2][pointCount];
		for( int numsIndex = 0, pointsIndex = 0; numsIndex < nums.length && pointsIndex < points[0].length; numsIndex += 2, pointsIndex++){
			//check each point for potential min and max
			updateMinMax( nums[numsIndex], nums[numsIndex + 1]);
			//store the points
			points[0][pointsIndex] = nums[numsIndex];
			points[1][pointsIndex] = nums[numsIndex + 1];
		}
		hitbox.setBounds(minX, minY, maxX-minX, maxY-minY);
		return this;
	}
	
	/**
	 * update the min and max values using the given x and y.
	 * used to find the smallest and largest points in this shape to create the {@link RectangleBounds}. 
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	private void updateMinMax(double x, double y){
		if(x<minX)
			minX=x;
		else if(x>maxX)
			maxX=x;
		if(y<minY)
			minY=y;
		else if(y>maxY)
			maxY=y;
	}

	/**
	 * get number of points in this shape
	 * @return number of points
	 */
	public int pointCount(){
		return pointCount;
	}
	
	/**
	 * get the boundary of this shape in a rectangle form
	 * @return boundary of this shape
	 */
	public HitBox getHitBox() {
		return hitbox;
	}

	/**
	 * x values are in row zero and y values are in row 1
	 * @return array of all points in this shape, each column is one point
	 */
	public double[][] points(){
		return points;
	}

	/**
	 * return the x point of given index
	 * @param index - index of the joint
	 * @return value of x point at given index
	 */
	public double pX( int index){
		return points[0][index];
	}

	/**
	 * return the y point of given index
	 * @param index - index of the joint
	 * @return value of y point at given index
	 */
	public double pY( int index){
        return points[1][index];
	}
    
    @Override
    public boolean hasHitBox() {
        return true;
    }

	@Override
	public boolean isDrawable(){
        return true;
	}

	@Override
	public Sprite getDrawable(){
        return sprite;
	}
    
    @Override
    public void update() {}

	/**
	 * draw circles and number on the joints of this shape
	 * @param gc - {@link GraphicsContext} object
	 */
	public void drawCorners( GraphicsContext gc){
		gc.setFill( Color.DARKCYAN);
		for( int i = 0; i < pointCount; i++){
            gc.fillText( Integer.toString( i), points[0][i] - 5, points[1][i] - 5);
            gc.fillOval( points[0][i] - 5, points[1][i] - 5, 10, 10);
		}
	}
}

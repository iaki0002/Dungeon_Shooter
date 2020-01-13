package dungeonshooter.entity;

import dungeonshooter.entity.property.Drawable;
import dungeonshooter.entity.property.HitBox;

public interface Entity{
    
    public void update();
    
    public boolean hasHitBox();
    
    public HitBox getHitBox();

	boolean isDrawable();

	Drawable<?> getDrawable();
}

/** Fixed substeps prevent jumping through narrow obstacles at low frame rates. */
public final class Player {
    public double x=930, y=853;
    public final double radius=4;
    public void reset() {x=930;y=853;}
    public void move(CampusMap map,double dx,double dy,double seconds) {
        double length=Math.hypot(dx,dy);
        if(length==0) return;
        double distance=125*Math.min(seconds,0.05);
        int steps=Math.max(1,(int)Math.ceil(distance/2));
        double sx=dx/length*distance/steps, sy=dy/length*distance/steps;
        for(int i=0;i<steps;i++) {
            if(map.canStand(x+sx,y,radius)) x+=sx;
            if(map.canStand(x,y+sy,radius)) y+=sy;
        }
    }
}

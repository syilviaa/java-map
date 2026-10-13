import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/** Both renderers consume the same vector geometry; no background image is loaded. */
public final class MapRenderer {
    private MapRenderer() { }
    public static void draw(CampusMap map, Player player, String status) {
        StdDraw.clear(Color.WHITE);
        for(CampusMap.Mark m:map.marks) drawShape(m);
        for(CampusMap.Label l:map.labels) {
            StdDraw.setPenColor(CampusMap.INK);
            StdDraw.setFont(new Font("SansSerif",Font.BOLD,Math.max(9,(int)Math.round(l.size*1200.0/CampusMap.WIDTH))));
            String[] lines=l.text.split("\n");
            for(int i=0;i<lines.length;i++)
                StdDraw.text(l.x,CampusMap.HEIGHT-(l.y+(i-(lines.length-1)/2.0)*l.size*1.15),lines[i]);
        }
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.filledCircle(player.x,CampusMap.HEIGHT-player.y,player.radius+2);
        StdDraw.setPenColor(new Color(25,90,225));
        StdDraw.filledCircle(player.x,CampusMap.HEIGHT-player.y,player.radius);
        if(status!=null && !status.isEmpty()) {
            StdDraw.setPenColor(CampusMap.INK);
            StdDraw.setFont(new Font("SansSerif",Font.BOLD,16));
            StdDraw.text(768,14,status);
        }
    }
    private static void drawShape(CampusMap.Mark mark) {
        PathIterator iterator=mark.shape.getPathIterator(null,0.6);
        ArrayList<Double> xs=new ArrayList<>(), ys=new ArrayList<>();
        double[] coord=new double[6];
        while(!iterator.isDone()) {
            int type=iterator.currentSegment(coord);
            if(type==PathIterator.SEG_MOVETO) {
                paint(xs,ys,mark); xs.clear(); ys.clear();
            }
            if(type==PathIterator.SEG_MOVETO || type==PathIterator.SEG_LINETO) {
                xs.add(coord[0]); ys.add(CampusMap.HEIGHT-coord[1]);
            } else if(type==PathIterator.SEG_CLOSE) {
                paint(xs,ys,mark); xs.clear(); ys.clear();
            }
            iterator.next();
        }
        paint(xs,ys,mark);
    }
    private static void paint(ArrayList<Double> xs,ArrayList<Double> ys,CampusMap.Mark m) {
        if(xs.size()<3) return;
        double[] x=xs.stream().mapToDouble(Double::doubleValue).toArray();
        double[] y=ys.stream().mapToDouble(Double::doubleValue).toArray();
        if(m.fill!=null) { StdDraw.setPenColor(m.fill); StdDraw.filledPolygon(x,y); }
        if(m.outline!=null) { StdDraw.setPenColor(m.outline); StdDraw.setPenRadius(0.001); StdDraw.polygon(x,y); }
    }
    public static void export(CampusMap map,File file) throws IOException {
        BufferedImage image=new BufferedImage(CampusMap.WIDTH,CampusMap.HEIGHT,BufferedImage.TYPE_INT_RGB);
        Graphics2D g=image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE); g.fillRect(0,0,image.getWidth(),image.getHeight());
        for(CampusMap.Mark m:map.marks) {
            if(m.fill!=null) {g.setColor(m.fill);g.fill(m.shape);}
            if(m.outline!=null) {g.setColor(m.outline);g.setStroke(new BasicStroke(1.5f));g.draw(m.shape);}
        }
        g.setColor(CampusMap.INK);
        for(CampusMap.Label l:map.labels) {
            g.setFont(new Font("SansSerif",Font.BOLD,l.size));
            FontMetrics fm=g.getFontMetrics(); String[] lines=l.text.split("\n");
            for(int i=0;i<lines.length;i++) {
                float y=(float)(l.y+(i-(lines.length-1)/2.0)*l.size*1.15+(fm.getAscent()-fm.getDescent())/2.0);
                g.drawString(lines[i],(float)(l.x-fm.stringWidth(lines[i])/2.0),y);
            }
        }
        g.dispose(); ImageIO.write(image,"png",file);
    }
}

import java.awt.event.KeyEvent;
import java.io.File;

public final class Main {
    private static boolean key(int a,int b) {return StdDraw.isKeyPressed(a)||StdDraw.isKeyPressed(b);}
    public static void main(String[] args) throws Exception {
        CampusMap map=new CampusMap();
        if(args.length>0 && args[0].equals("--export")) {
            File file=new File(args.length>1?args[1]:"campus-java.png");
            MapRenderer.export(map,file); System.out.println(file.getAbsolutePath()); return;
        }
        if(args.length>0 && args[0].equals("--check")) {MapChecks.run(map);return;}
        StdDraw.setCanvasSize(1200,800);
        StdDraw.setXscale(0,CampusMap.WIDTH); StdDraw.setYscale(0,CampusMap.HEIGHT);
        StdDraw.enableDoubleBuffering();
        Player player=new Player(); long previous=System.nanoTime();
        while(!StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            long now=System.nanoTime(); double dt=(now-previous)/1e9; previous=now;
            int dx=(key(KeyEvent.VK_D,KeyEvent.VK_RIGHT)?1:0)-(key(KeyEvent.VK_A,KeyEvent.VK_LEFT)?1:0);
            int dy=(key(KeyEvent.VK_S,KeyEvent.VK_DOWN)?1:0)-(key(KeyEvent.VK_W,KeyEvent.VK_UP)?1:0);
            player.move(map,dx,dy,dt);
            if(StdDraw.isKeyPressed(KeyEvent.VK_R)) player.reset();
            String nearby=map.nearbyEntrance(player.x,player.y);
            String status=nearby==null?"Explore the campus | Buildings and water block movement":
                    (StdDraw.isKeyPressed(KeyEvent.VK_E)?nearby+" | Exterior map; interiors not included":"Near "+nearby+" | Hold E for entrance info");
            MapRenderer.draw(map,player,status); StdDraw.show(); StdDraw.pause(16);
            if(args.length>0 && args[0].equals("--snapshot")) {
                StdDraw.save(args.length>1?args[1]:"campus-stddraw.png"); break;
            }
        }
        System.exit(0);
    }
}

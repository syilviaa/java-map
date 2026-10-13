/** Behavioral checks of shared geometry and movement, run without a display. */
public final class MapChecks {
    private static int count;
    private static void check(boolean condition,String description) {
        if(!condition) throw new AssertionError(description);
        count++;
    }
    public static void run(CampusMap map) {
        check(map.canStand(930,853,4),"South Gate spawn must be free");
        check(!map.canStand(455,540,4),"Lake must block movement");
        check(map.canStand(380,420,4),"East-west lake bridge must be passable");
        check(map.canStand(416,310,4),"Northern lake path must be passable");
        check(map.canStand(416,350,4),"Lake platform must be passable");
        check(map.canStand(420,462,4),"Water Pavilion must be passable");
        check(map.canStand(90,445,4),"Western river road crossing must be passable");
        check(map.canStand(1063,478,4),"Skybridge underpass must be passable");
        check(!map.canStand(600,400,4),"Academic Building must block movement");
        check(!map.canStand(855,740,4),"Relocated research building must block movement");
        check(!map.canStand(600,730,4),"Innovation extension must block movement");
        for(String name:new String[]{"15A","15B","15C","15D","15E","15F","15G","15H"})
            check(map.buildings.get(name).entrances.size()==2,name+" needs north and south entries");
        check("Library".equals(map.nearbyEntrance(1110,628)),"Library entrance lookup");
        Player p=new Player(); p.x=661;p.y=605;
        for(int i=0;i<100;i++) p.move(map,0,-1,0.05);
        check(p.y>=584,"Player cannot tunnel through building at south entrance");
        p.x=350;p.y=420;
        for(int i=0;i<10;i++) p.move(map,1,0,0.02);
        check(p.x>370,"Player can traverse lake bridge");
        check(!map.canStand(20,100,4),"Map bounds enforced");
        System.out.println("Passed "+count+" geometry / movement checks.");
    }
}

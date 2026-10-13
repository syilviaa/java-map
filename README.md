# DKU Campus Map — Java / StdDraw

这是根据 `Campus_map.png` 的布局用 Java 图形重绘的室外校园地图。运行时不读取该图片，全部建筑、道路、湖水、入口及文字由 Java 坐标和图形定义。

## 运行

需要 JDK 17 或更新版本。macOS 双击 `run.command`；Windows 双击 `run.bat`。也可以在本目录终端运行：

```sh
mkdir -p out
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

窗口默认 1200 × 800。点击地图窗口获取键盘焦点。

- WASD / 方向键：移动蓝色圆点；可斜向移动，斜向速度不会变快。
- R：回到南门附近。
- 靠近入口时按住 E：显示建筑名称（目前只实现室外地图，没有室内场景）。
- Esc：退出。

草坪、空地、道路可通行；实体建筑和湖水不可通行。湖面步道、平台和湖心亭可通行；社区中心空中连廊不阻挡下方地面道路。体育场地可通行。深色短线表示入口，入口目前是信息交互点，不会穿过建筑墙体。

## 修改地图

设计坐标为 1536 × 1024，原点在左上角，向右为 x 正方向，向下为 y 正方向，与参考图一致。MapRenderer 绘制时转换为 StdDraw 的左下原点。

- `src/CampusMap.java`：地图数据、建筑形状、入口、道路、水域及碰撞区域。
- `src/MapRenderer.java`：将相同图形数据绘制到 StdDraw 窗口，或导出 PNG。
- `src/Player.java`：位置、速度、分步碰撞检测。
- `src/Main.java`：键盘输入、刷新循环、启动参数。
- `src/MapChecks.java`：移动规则与关键区域的行为检查。
- `src/StdDraw.java`：Princeton 官方图形库源码，保留原版权和许可说明。

示例：`rectangle(x, y, width, height)` 使用左上角及完整宽高；`polygon(x1,y1,x2,y2,...)` 使用顶点坐标。`add(...)` 添加建筑和文字；`door(...)` 添加入口及室外连接步道。建筑碰撞区域由同一份建筑形状构建，因此改变建筑轮廓会同步改变碰撞范围。

`ArrayList` 保存绘制顺序及入口；`LinkedHashMap<String, Building>` 用建筑名查找建筑，同时保持插入顺序。Java AWT 的 `Area` 用于碰撞几何运算，不用于替代这些集合的作用。

## 验证与导出

```sh
java -Djava.awt.headless=true -cp out Main --check
java -Djava.awt.headless=true -cp out Main --export campus-java.png
java -cp out Main --snapshot campus-stddraw.png
```

`--check` 检查出生点、湖水阻挡、桥与平台通行、连廊下通行、建筑阻挡、八栋宿舍双入口、入口查询、防穿墙和边界。`--export` 使用相同矢量数据在 Java2D 中导出图片；`--snapshot` 打开真实 StdDraw 窗口，保存一帧后退出。

## 当前范围

这是简化示意地图，不保证实测比例。员工中心内院暂时没有新增入口；没有寻路、室内场景或存档。尚未进行跨平台人工键盘体验测试。

## 来源与课程说明

StdDraw 来源：https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java

本项目中的定制代码由 ChatGPT/Codex 辅助生成。课程说明要求有限使用 AI，团队需理解所有提交代码，并在报告及展示中声明工具、用途和代表性交互。此版本适合作为理解和修改的开发原型；请根据课程要求审查并如实记录使用情况。

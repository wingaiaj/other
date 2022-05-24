import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName Gobang
 * @Description TODO
 * @Author xpower
 * @Date 2022/5/13 19:47
 * @Version 1.0
 */
public class GoBang {
    //主窗体
  private JFrame frame = new JFrame("五子棋");
    //棋盘的宽高
    private final int boardWidth =535;
    private final int boardHeight =536;
    //每行每列可以下的棋子数
    private final int bNum = 15;
    //棋盘的偏移量
    private final int offsetX = 5;
    private final int offsetY = 6;
    //棋盘比率 boardWidth/bNum
    private final int rate = boardWidth/bNum;
    //棋盘数组 大小为bNum
    private final int [][]boardArr =new int[bNum][bNum];
    //位图 棋盘 白子 黑子 光标
   private BufferedImage black;
   private BufferedImage board;
   private BufferedImage selected;
   private BufferedImage white;
   //游戏结束标识
    boolean flagW = false;
    boolean flagB = false;

    //画布类
    private class drawSome extends JPanel{
        @Override
        public void paint(Graphics g) {
            //绘制
            //绘制棋盘
            g.drawImage(board,0,0,null);
            //绘制棋子
            for (int i = 0; i <bNum ; i++) {
                for (int j = 0; j < bNum; j++) {

                    if (boardArr[i][j] == 1) {
                        //绘制白棋
                        if (!flagW && !flagB) {
                            g.drawImage(white, i * rate + offsetX, j * rate + offsetY, null);
                        }
                    }
                    if (boardArr[i][j] == 2) {
                        //绘制黑棋
                        if (!flagW && !flagB) {
                            g.drawImage(black, i * rate + offsetX, j * rate + offsetY, null);
                        }
                    }
                }
            }
            //判断棋子是否满足胜利条件
            for (int i = 0; i < boardArr.length; i++) {
                for (int j = 0; j < boardArr[i].length; j++) {
                  //白棋
                    if(i<=10&&boardArr[i][j]==1&&boardArr[i][j]==boardArr[i+1][j]&&boardArr[i][j]==boardArr[i+2][j]&&boardArr[i][j]==boardArr[i+3][j]&&boardArr[i][j]==boardArr[i+4][j]){
                        flagW = true;
                    }
                    if(j<=10&&boardArr[i][j]==1&&boardArr[i][j]==boardArr[i][j+1]&&boardArr[i][j]==boardArr[i][j+2]&&boardArr[i][j]==boardArr[i][j+3]&&boardArr[i][j]==boardArr[i][j+4]){
                        flagW = true;
                    }
                    if(i<=10&&j<=10&&boardArr[i][j]==1&&boardArr[i][j]==boardArr[i+1][j+1]&&boardArr[i][j]==boardArr[i+2][j+2]&&boardArr[i][j]==boardArr[i+3][j+3]&&boardArr[i][j]==boardArr[i+4][j+4]){
                        flagW = true;
                    }
                   if(boardArr[i][j]==1&&boardArr[i][j]==boardArr[i-1][j+1]&&boardArr[i][j]==boardArr[i-2][j+2]&&boardArr[i][j]==boardArr[i-3][j+3]&&boardArr[i][j]==boardArr[i-4][j+4]){
                       flagW = true;
                   }
                   //黑棋
                   if(i<=10&&boardArr[i][j]==2&&boardArr[i][j]==boardArr[i+1][j]&&boardArr[i][j]==boardArr[i+2][j]&&boardArr[i][j]==boardArr[i+3][j]&&boardArr[i][j]==boardArr[i+4][j]){
                        flagB = true;
                    }
                    if(j<=10&&boardArr[i][j]==2&&boardArr[i][j]==boardArr[i][j+1]&&boardArr[i][j]==boardArr[i][j+2]&&boardArr[i][j]==boardArr[i][j+3]&&boardArr[i][j]==boardArr[i][j+4]){
                        flagB = true;
                    }
                    if(i<=10&&j<=10&&boardArr[i][j]==2&&boardArr[i][j]==boardArr[i+1][j+1]&&boardArr[i][j]==boardArr[i+2][j+2]&&boardArr[i][j]==boardArr[i+3][j+3]&&boardArr[i][j]==boardArr[i+4][j+4]){
                        flagB = true;
                    }
                   if(boardArr[i][j]==2&&boardArr[i][j]==boardArr[i-1][j+1]&&boardArr[i][j]==boardArr[i-2][j+2]&&boardArr[i][j]==boardArr[i-3][j+3]&&boardArr[i][j]==boardArr[i-4][j+4]){
                       flagB = true;
                   }

                }
            }
            //绘制光标
            if(preX > 0&& preY > 0) {
                g.drawImage(selected, preX * rate + offsetX, preY * rate + offsetY, null);
            }
            //绘制游戏结束
            if(flagW) {
                g.setFont(new Font("w",Font.BOLD,30));
                g.drawString("游戏结束！，白获胜", 230, 230);
            }
            if(flagB){
                g.setFont(new Font("w2",Font.BOLD,30));
                g.drawString("游戏结束！,黑获胜", 230, 230);
            }
        }

    }
    //画布创建
   private drawSome drawSm = new drawSome();
    //按钮创建
   private Button whiteBt = new Button("白棋");
   private Button blackBt = new Button("黑棋");
   private Button delBt = new Button("删除");
    //按钮容器创建
   private Panel panel = new Panel();
    //棋子类型 变量  1 白棋 2 黑棋 0 空
    private  int pieceType = 1;
    //光标位置变量
    private int preX = -1;
    private int preY = -1;
    //颜色
   private Color greenColor = Color.green;
    private Color g = Color.GRAY;
    //选中按钮改变按钮颜色方法
    private void setButtonColor(Color w,Color b,Color d ){
        whiteBt.setBackground(w);
        blackBt.setBackground(b);
        delBt.setBackground(d);
    }
    public void init(){//组装容器 和代码逻辑
//按钮监听
        whiteBt.addActionListener(e->{
            String actionCommand = e.getActionCommand();

                //棋子类型变量更改
                pieceType = 1;
                setButtonColor(greenColor,g,g);

        });

        blackBt.addActionListener(e->{
            String actionCommand = e.getActionCommand();

                //棋子类型变量更改
                pieceType = 2;
                setButtonColor(g,greenColor,g);


        });
        delBt.addActionListener(e -> {
            String actionCommand = e.getActionCommand();

                //棋子类型变量更改
                pieceType = 0;
                setButtonColor(g,g,greenColor);

        });
//添加容器 添加到主窗体 位置南
        panel.add(whiteBt);
        panel.add(blackBt);
        panel.add(delBt);

        frame.add(panel, BorderLayout.SOUTH);
// 画布添加鼠标点击监听
        drawSm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
             int x = (e.getX()-offsetX)/rate;
             int y = (e.getY()-offsetY)/rate;
                boardArr[x][y] =pieceType ;
                drawSm.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                preX = -1;
                preY = -1;
        drawSm.repaint();
            }
        });
//鼠标移动监听
        drawSm.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                preX = (e.getX()-offsetX)/rate;
                preY = (e.getY()-offsetY)/rate;
                drawSm.repaint();
            }

        });
//位图的赋路径
        try {
            black = ImageIO.read(new File("C:\\Users\\xpower\\Desktop\\AwtAndSwing\\image\\black.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            board = ImageIO.read(new File("C:\\Users\\xpower\\Desktop\\AwtAndSwing\\image\\board.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            selected = ImageIO.read(new File("C:\\Users\\xpower\\Desktop\\AwtAndSwing\\image\\selected.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            white = ImageIO.read(new File("C:\\Users\\xpower\\Desktop\\AwtAndSwing\\image\\white.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //数组初始化棋盘上所有位置为0
        for (int i = 0; i < boardArr.length; i++) {
            for (int j = 0; j < boardArr[i].length; j++) {
                boardArr[i][j] = 0;
            }
        }

//画布大小
        drawSm.setPreferredSize(new Dimension(boardWidth,boardHeight));
//添加画布
        frame.add(drawSm);
        //最佳大小
        frame.pack();
//设置窗口可见
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        //创建对象调用方法
        new GoBang().init();
    }
}

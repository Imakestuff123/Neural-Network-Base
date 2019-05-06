import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class Draw extends JPanel
{
    Driver Base;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public Draw(JFrame gameFrame) {
        CreateWindow(gameFrame);
        Base = new Driver();
        Base.StartNeuralNetwork();
    }
    private void CreateWindow(JFrame gameFrame) {
        gameFrame.setSize(screenSize.height, screenSize.width);
        gameFrame.setUndecorated(true);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.add(this);
        gameFrame.setVisible(true);
        gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    }
    public static void main() {
        System.out.print('\u000C');
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame gameFrame = new JFrame("Game Name");
                final Draw gamePanel = new Draw(gameFrame);
                Thread loop = new Thread() {
                    public void run() {
                        gamePanel.gameLoop();
                    }
                };
                loop.start();
            }
        });
        
    }
    public void gameLoop() {
        while (true) {
            repaint();
            Base.StepUpdate();
            try {
                //17
                Thread.sleep(Base.FrameSpeed);
            } catch (Exception e) {
                
            }
        }
        
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        Base.GraphicsUpdate(g, g2);
    }
}

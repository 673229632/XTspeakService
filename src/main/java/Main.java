import socket.AppSocketThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *
 * @author zhangrui.i
 * @version V1.0
 * @Description: 主界面入口
 * @date 2019年7月26日13:39:39
 */
public class Main {

    static JTextArea textArea = new JTextArea();

    /**
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    private static void createAndShowGUI() {

        // 创建及设置窗口
        final JFrame frame = new JFrame("叫号服务");

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                int result = JOptionPane.showConfirmDialog(frame,
                        "确定退出叫号服务?", "提示",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        URL imgUrl = Main.class.getClassLoader().getResource("logo.png");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(imgUrl));

        frame.setAlwaysOnTop(true);

        frame.setSize(600, 300);
        //为Frame窗口设置布局为BorderLayout
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);

        //窗体居中显示
        frame.setLocationRelativeTo(null);

        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setFont(new Font("宋体", Font.BOLD, 18));
        textArea.setText("叫号信息:");
        textArea.setLineWrap(true);
        textArea.setBackground(new Color(232, 232, 232));
        textArea.setEditable(false);

        //将文本域放入滚动窗口
        JScrollPane jScrollPane = new JScrollPane(textArea);
        //获得文本域的首选大小
        Dimension size = textArea.getPreferredSize();
        jScrollPane.setBounds(0, 0, size.width, size.height);
        frame.getContentPane().add(jScrollPane, BorderLayout.CENTER);


        JPanel topPanel = new JPanel();

        JButton endButton = new JButton("关闭");
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        topPanel.add(endButton);
        // frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        // 显示窗口
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // 显示应用 GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
                new AppSocketThread(textArea).start();
            }
        });
    }

}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import java.util.Date;

/**
 * @author fzkuji
 * @version 1.0.1
 * @ClassName UI.java
 * @Description D级人员UI界面类
 * @Param
 * @createTime 2019年10月09日 03:56:14
 */

public class UI extends JFrame{
    private Desktop desktop = Desktop.getDesktop();
    private JPanel contentPane;
    private JFileChooser filechooser;
    TextArea records;
    ArrayList<File> FILE=new ArrayList<File>();
    ArrayList<String> Str=new ArrayList<String>();
    public static void main(int role_id) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UI frame = new UI(role_id);
                    frame.setVisible(true);
                    frame.setResizable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public UI(int role_id) throws IOException {
        super.setTitle("欢迎d级人员");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 200, 600, 800);
        contentPane = new JPanel();
        //contentPane.setForeground(Color.black);
        setContentPane(contentPane);
        filechooser=new JFileChooser();
        records=new TextArea(20,60);
        records.setEditable(false);
        Color co=new Color(15790320);
        records.setBackground(co);

        JButton Open = new JButton("打开");
        JButton Dele = new JButton("删除");
        Open.setFont(new Font("幼圆", Font.PLAIN, 14));
        Dele.setFont(new Font("幼圆", Font.PLAIN, 14));
        Open.setContentAreaFilled(false);
        Dele.setContentAreaFilled(false);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu_1 = new JMenu("文件");
        menuBar.add(menu_1);

        JMenuItem menuItem_1 = new JMenuItem("导出历史记录");
        JMenuItem menuItem_2 = new JMenuItem("清空历史记录");
        JMenuItem menuItem_5 = new JMenuItem("退出");
        menu_1.add(menuItem_1);
        menu_1.add(menuItem_2);
        menu_1.add(menuItem_5);

        Open.setBounds(11, 10, 93, 23);
        Dele.setBounds(11, 10, 93, 23);
        contentPane.add(Open);
        contentPane.add(Dele);
        contentPane.add(filechooser,BorderLayout.WEST);
        contentPane.add(records,BorderLayout.WEST);

        records.setFont(new Font("幼圆", Font.BOLD, 14));
        records.setText("历史记录：");
        //records.setFont(new   java.awt.Font("Dialog",1,15));
        filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
        Open.addActionListener(new ActionListener() {
            //打开操作
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = filechooser.getSelectedFile();
                if(file!=null)
                    try {
                        Calendar c = Calendar.getInstance();
                        desktop.open(file);

                        records.append("\r\n\r\n"+c.getTime()+"\r\n打开文件  "+file.getPath());

                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
            }
        });
        Dele.addActionListener(new ActionListener() {
            //删除操作
            @Override
            public void actionPerformed(ActionEvent e) {
                if (role_id > 3){
                    JOptionPane.showMessageDialog(null, "您根本没有权限！");
                }
                else {
                    File file = filechooser.getSelectedFile();
                    Calendar c = Calendar.getInstance();
                    records.append("\r\n\r\n"+c.getTime()+"\r\n删除文件(夹)  "+file.getPath());
                    deleteFile(file);
                }
            }
        });
        menuItem_1.addActionListener(new ActionListener(){
            //工具栏导出历史记录
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String name=null;
                SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date();
                name=f.format(date);
                File f1 = new File("src/res/"+name+".txt");
                try (FileOutputStream fop = new FileOutputStream(f1)) {
                    if (!f1.exists()) {
                        f1.createNewFile();
                    }
                    byte[] contentInBytes = records.getText().getBytes();
                    System.lineSeparator();
                    fop.write(contentInBytes);
                    fop.flush();
                    fop.close();

                    //System.out.println(name);

                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                records.append("\r\n\r\n"+c.getTime()+"\r\n"+"导出历史记录  "+"."+f1.getPath());
            }
        });
        menuItem_2.addActionListener(new ActionListener(){
            //工具栏清空历史记录
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (role_id > 3){
                    JOptionPane.showMessageDialog(null, "您压根就没有权限！");
                }
                else {
                    records.setText("历史记录：");
                }
            }

        });
        menuItem_5.addActionListener(new ActionListener(){
            //工具栏退出按钮
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.exit(0);
            }
        });

    }

    public void deleteFile(File file) {
        if (file!=null)
            if (file.exists()) {//判断文件是否存在
                if (file.isFile()) {//判断是否是文件
                    file.delete();//删除文件
                } else if (file.isDirectory()) {//否则如果它是一个目录
                    File[] files = file.listFiles();//声明目录下所有的文件 files[];
                    for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
                        this.deleteFile(files[i]);//把每个文件用这个方法进行迭代
                    }
                    file.delete();//删除文件夹
                }
            } else {
                System.out.println("所删除的文件不存在");
            }
    }

    public static void main(String[] args, int role_id) {

        UI.main(role_id);
    }
}
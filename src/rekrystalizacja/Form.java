package rekrystalizacja;

import java.awt.Color;
import java.util.Timer;
import java.awt.Image;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Form extends javax.swing.JFrame {

    final int wid = 74, hei = 50;
    Komorka[][] currentMove = new Komorka[hei][wid];
    Komorka[][] nextMove = new Komorka[hei][wid];
    List<Color> listColor = new ArrayList<Color>();
    List<Color> listColorsRecristal = new ArrayList<Color>();
    Color homoColor = new Color(0, 43, 255);
    Color heteroColor = new Color(137, 255, 0);
    Color recColor = new Color(255, 0, 0);
    Color colorChosen;
    int rand = 0;
    Timer time = new Timer();
    TimerTask timerTask;

    boolean play;
    boolean onGB = true;
    Image offScrImg;
    Graphics offScrGraph;
    Image offScrImg2;
    Graphics offScrGraph2;
    Random random = new Random();
    Punkt punkt;
    static int ID;
    static int IDrec;
    static String sasiedztwo = "Moore";
    static String nucleons = "100 nucleons";
    boolean isPeriod = true;
    double q = 0;
    double qSr = 0;
    Runnable t2, t3;
    Runnable t4, t5, t6;
    Thread rec;
    Thread rekrystalizacja, monte;
    Thread task;
    int monteInit = 0;
    int counter = 0;
    int homo = 5;
    int hetero = 7;

    public Form() {

        // nextMove=currentMove.clone();
        initComponents();
        setTitle("Rozrost Ziaren - Marcin");
        offScrImg = createImage(jPanel1.getWidth(), jPanel1.getHeight());
        offScrGraph = offScrImg.getGraphics();
        System.out.println(jPanel2.getWidth() + ", " + jPanel2.getHeight());
        offScrImg2 = createImage(jPanel2.getWidth(), jPanel2.getHeight());
        offScrGraph2 = offScrImg2.getGraphics();
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                currentMove[i][j] = new Komorka();
            }
        }
    }

    private int checkNeighborNeumann(int i, int j) {
        int seed = -1;

        int[] xtemp = new int[2];
        int[] ytemp = new int[2];

        if (isPeriod) {
            xtemp[0] = (i == 0 ? hei - 1 : i - 1);
            xtemp[1] = (i == hei - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? wid - 1 : j - 1);
            ytemp[1] = (j == wid - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = (i == hei - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = (j == wid - 1 ? -1 : j + 1);
        }

        int[] neighboursId = new int[ID + 1];
        for (int k = 0; k <= ID; k++) {
            neighboursId[k] = 0;
        }

        for (int k = 0; k < 2; k++) {
            if (xtemp[k] != -1) {
                int seedId = currentMove[xtemp[k]][j].getId();
                if (seedId != -1) {
                    neighboursId[seedId]++;
                }

            }
            if (ytemp[k] != -1) {
                int seedId = currentMove[i][ytemp[k]].getId();
                if (seedId != -1) {
                    neighboursId[seedId]++;
                }
            }
        }

        int max = -1;
        for (int k = 0; k <= ID; k++) {
            if (neighboursId[k] > max && neighboursId[k] > 0) {
                seed = k;
            } else if (neighboursId[k] == max) {
                seed = random.nextInt(2) == 0 ? k : seed;
            }
        }

        return seed;
    }

    private int checkNeighborMoore(int i, int j) {
        int seed = -1;

        int[] xtemp = new int[3];
        int[] ytemp = new int[3];

        if (isPeriod) {
            xtemp[0] = (i == 0 ? hei - 1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == hei - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? wid - 1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == wid - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == hei - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == wid - 1 ? -1 : j + 1);
        }

        int[] neighboursId = new int[ID + 1];
        for (int k = 0; k <= ID; k++) {
            neighboursId[k] = 0;
        }

        for (int k = 0; k < 3; k++) {
            {
                for (int l = 0; l < 3; l++) {
                    if (xtemp[k] != -1 && ytemp[l] != -1) {
                        int seedId = currentMove[xtemp[k]][ytemp[l]].getId();
                        if (seedId != -1 && seedId != -2) {
                            neighboursId[seedId]++;
                        }

                    }
                }
            }
        }

        int max = -1;
        for (int k = 0; k <= ID; k++) {
            if (neighboursId[k] > max && neighboursId[k] > 0) {
                seed = k;
            } else if (neighboursId[k] == max) {
                seed = random.nextInt(2) == 0 ? k : seed;
            }
        }
        return seed;
    }

    private boolean checkNeighborMooreRec(int i, int j) {
        int seed = -1;

        int[] xtemp = new int[3];
        int[] ytemp = new int[3];

        if (isPeriod) {
            xtemp[0] = (i == 0 ? hei - 1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == hei - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? wid - 1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == wid - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == hei - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == wid - 1 ? -1 : j + 1);
        }

        for (int k = 0; k < 3; k++) {
            {
                for (int l = 0; l < 3; l++) {
                    if (xtemp[k] != -1 && ytemp[l] != -1) {
                        boolean isRec = currentMove[xtemp[k]][ytemp[l]].isRec();
                        int seedId = currentMove[xtemp[k]][ytemp[l]].getId();
                        if (isRec) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;

    }

    private void repain() {
        offScrGraph.setColor(jPanel1.getBackground());
        offScrGraph.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                if (currentMove[i][j].getId() == -2) {
                    offScrGraph.setColor(colorChosen);
                    int x = j * jPanel1.getWidth() / wid;
                    int y = i * jPanel1.getHeight() / hei;
                    offScrGraph.fillRect(x, y, jPanel1.getHeight() / hei + 1, jPanel1.getWidth() / wid);
                } else {
                    if (currentMove[i][j].getId() != -1) {
                        offScrGraph.setColor(listColor.get(currentMove[i][j].getId()));
                        int x = j * jPanel1.getWidth() / wid;
                        int y = i * jPanel1.getHeight() / hei;
                        offScrGraph.fillRect(x, y, jPanel1.getHeight() / hei + 1, jPanel1.getWidth() / wid);
                    }
                }

            }
        }

        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);
    }

    private void repainEnergy() {
        offScrGraph2.setColor(jPanel2.getBackground());
        offScrGraph2.fillRect(0, 0, jPanel2.getWidth(), jPanel2.getHeight());
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                if (currentMove[i][j].getH() == homo) {
                    offScrGraph2.setColor(homoColor);
                    int x = j * jPanel2.getWidth() / wid;
                    int y = i * jPanel2.getHeight() / hei;
                    offScrGraph2.fillRect(x, y, jPanel2.getHeight() / hei + 1, jPanel2.getWidth() / wid);
                } else {
                    if (currentMove[i][j].getH() == hetero) {
                        offScrGraph2.setColor(heteroColor);
                        int x = j * jPanel2.getWidth() / wid;
                        int y = i * jPanel2.getHeight() / hei;
                        offScrGraph2.fillRect(x, y, jPanel2.getHeight() / hei + 1, jPanel2.getWidth() / wid);
                    }
                    if (currentMove[i][j].getH() == 0) {
                        offScrGraph2.setColor(recColor);
                        int x = j * jPanel2.getWidth() / wid;
                        int y = i * jPanel2.getHeight() / hei;
                        offScrGraph2.fillRect(x, y, jPanel2.getHeight() / hei + 1, jPanel2.getWidth() / wid);
                    }
                }

            }
        }
        jPanel2.getGraphics().drawImage(offScrImg2, 0, 0, jPanel2);
    }

    public boolean hasNeighbor(Komorka tab[], Komorka active) {

        for (int i = 0; i < tab.length; i++) {
            if (tab[i].getId() != active.getId() && tab[i].getId() != 0) {
                return true;
            }
        }

        return false;
    }
    private boolean isStarted = false;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jCheckBox2 = new javax.swing.JCheckBox();
        jComboBox2 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentResized(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel2ComponentResized(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        jButton2.setText("Random");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Moore", "von Nuemann" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Non-periodical");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton4.setText("Clear");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setText("Monte Carlo ");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTextField2.setText("5");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton8.setText("monte init");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jTextField3.setText("6");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton9.setText("1 str");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("MC");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jTextField4.setText("0");

        jButton11.setText("CA");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("CA start");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("CA 1 str");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton5.setText("homo");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton14.setText("hetero");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton16.setText("RECRYSTALIZATION");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jTextField1.setText("5");

        jTextField5.setText("7");

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("onGB");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "100 nucleons", "Constant", "Increasing" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton1.setText("Add");
        jButton1.setToolTipText("");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(17, 17, 17)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBox1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton13)
                                .addGap(69, 69, 69)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton16)
                                .addGap(67, 67, 67)
                                .addComponent(jCheckBox2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton7)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton10)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton11)
                        .addComponent(jButton12)
                        .addComponent(jButton13)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCheckBox1)
                        .addComponent(jButton5)
                        .addComponent(jButton14)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton16))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox2)
                        .addComponent(jButton4)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
        listColor.add(col);
        int x = wid * evt.getX() / jPanel1.getWidth();
        int y = hei * evt.getY() / jPanel1.getHeight();
        currentMove[y][x].setId(ID);
        ID++;
        repain();
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized

    }//GEN-LAST:event_jPanel1ComponentResized

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ID = 0;
        for (int k = 0; k < 30; k++) {
            Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
            listColor.add(col);
            int x = random.nextInt(hei);
            int y = random.nextInt(wid);
            currentMove[x][y].setId(ID);
            ID++;
        }
        repain();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        sasiedztwo = jComboBox1.getSelectedItem().toString();

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            isPeriod = false;
        } else {
            isPeriod = true;
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                currentMove[i][j].setId(-1);

            }
        }
        repain();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        counter = Integer.parseInt(jTextField3.getText());
        if (monte != null) {
            monte.stop();
            monte = null;
        }
        for (int i = 0; i < monteInit; i++) {
            Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
            listColor.add(col);
        }
        t3 = new Runnable() {
            public void run() {
                int licznik = 0;
                while (licznik < counter) {
                    List<Integer> chosen = new ArrayList<Integer>();
                    for (int i = 0; i < hei; i++) {
                        for (int j = 0; j < wid; j++) {
                            int[] xtemp = new int[3];
                            int[] ytemp = new int[3];
                            if (isPeriod) {
                                xtemp[0] = (i == 0 ? hei - 1 : i - 1);
                                xtemp[1] = i;
                                xtemp[2] = (i == hei - 1 ? 0 : i + 1);

                                ytemp[0] = (j == 0 ? wid - 1 : j - 1);
                                ytemp[1] = j;
                                ytemp[2] = (j == wid - 1 ? 0 : j + 1);
                            } else {
                                xtemp[0] = (i == 0 ? -1 : i - 1);
                                xtemp[1] = i;
                                xtemp[2] = (i == hei - 1 ? -1 : i + 1);

                                ytemp[0] = (j == 0 ? -1 : j - 1);
                                ytemp[1] = j;
                                ytemp[2] = (j == wid - 1 ? -1 : j + 1);
                            }

                            int E1 = 0;
                            int E2 = 0;
                            for (int k = 0; k < 3; k++) {
                                for (int l = 0; l < 3; l++) {
                                    if (xtemp[k] == -1 || ytemp[l] == -1 || (k == 1 && l == 1)) {
                                        continue;
                                    }
                                    if (currentMove[xtemp[k]][ytemp[l]].getId() != currentMove[i][j].getId()) {
                                        E1++;

                                    }
                                }
                            }

                            chosen.clear();
                            int randomId = -1;
                            do {
                                do {
                                    randomId = random.nextInt(ID);
                                } while (chosen.contains(randomId));
                                chosen.add(randomId);
                                E2 = 0;
                                for (int k = 0; k < 3; k++) {
                                    for (int l = 0; l < 3; l++) {
                                        if (xtemp[k] == -1 || ytemp[l] == -1 || (k == 1 && l == 1)) {
                                            continue;
                                        }
                                        if (currentMove[xtemp[k]][ytemp[l]].getId() != randomId) {
                                            E2++;
                                        }
                                    }
                                }
                            } while (E2 >= E1 && chosen.size() < ID);

                            if (E2 <= E1) {
                                currentMove[i][j].setId(randomId);
                            }
                        }
                    }
                    licznik++;
                    try {
                        repain();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        monte = new Thread(t3);
        monte.start();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        monteInit = Integer.parseInt(jTextField2.getText());
        ID = monteInit;
        for (int i = 0; i < monteInit; i++) {
            Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
            listColor.add(col);
        }
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                currentMove[i][j] = new Komorka();
                currentMove[i][j].setId(random.nextInt(monteInit));
            }
        }

        repain();

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed

    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        rand = random.nextInt(monteInit);
        colorChosen = listColor.get(rand);
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                if (currentMove[i][j].getId() != rand) {
                    currentMove[i][j].setId(-1);
                } else {
                    currentMove[i][j].setId(-2);
                }
            }
        }
        repain();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (timerTask != null) {
            timerTask.cancel();
        }
        int grain = Integer.parseInt(jTextField4.getText());
        ID = 0;
        listColor.clear();
        ID = grain;
        for (int i = 0; i < grain; i++) {
            Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
            listColor.add(col);
        }
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                if (currentMove[i][j].getId() != -2) {
                    currentMove[i][j] = new Komorka();
                    currentMove[i][j].setId(random.nextInt(grain));
                }
            }
        }
        repain();

        if (monte != null) {
            monte.stop();
            monte = null;
        }
        t3 = new Runnable() {
            public void run() {
                int licznik = 0;
                while (licznik < counter) {
                    List<Integer> chosen = new ArrayList<Integer>();
                    for (int i = 0; i < hei; i++) {
                        for (int j = 0; j < wid; j++) {
                            int[] xtemp = new int[3];
                            int[] ytemp = new int[3];
                            if (isPeriod) {
                                xtemp[0] = (i == 0 ? hei - 1 : i - 1);
                                xtemp[1] = i;
                                xtemp[2] = (i == hei - 1 ? 0 : i + 1);

                                ytemp[0] = (j == 0 ? wid - 1 : j - 1);
                                ytemp[1] = j;
                                ytemp[2] = (j == wid - 1 ? 0 : j + 1);
                            } else {
                                xtemp[0] = (i == 0 ? -1 : i - 1);
                                xtemp[1] = i;
                                xtemp[2] = (i == hei - 1 ? -1 : i + 1);

                                ytemp[0] = (j == 0 ? -1 : j - 1);
                                ytemp[1] = j;
                                ytemp[2] = (j == wid - 1 ? -1 : j + 1);
                            }

                            int E1 = 0;
                            int E2 = 0;
                            for (int k = 0; k < 3; k++) {
                                for (int l = 0; l < 3; l++) {
                                    if (xtemp[k] == -1 || ytemp[l] == -1 || (k == 1 && l == 1)) {
                                        continue;
                                    }
                                    if (currentMove[xtemp[k]][ytemp[l]].getId() != currentMove[i][j].getId() && currentMove[xtemp[k]][ytemp[l]].getId() != -2) {
                                        E1++;

                                    }
                                }
                            }

                            chosen.clear();
                            int randomId = -1;
                            do {
                                do {
                                    randomId = random.nextInt(ID);
                                } while (chosen.contains(randomId));
                                chosen.add(randomId);
                                E2 = 0;
                                for (int k = 0; k < 3; k++) {
                                    for (int l = 0; l < 3; l++) {
                                        if (xtemp[k] == -1 || ytemp[l] == -1 || (k == 1 && l == 1)) {
                                            continue;
                                        }
                                        if (currentMove[xtemp[k]][ytemp[l]].getId() != randomId && currentMove[xtemp[k]][ytemp[l]].getId() != -2) {
                                            E2++;
                                        }
                                    }
                                }
                            } while (E2 >= E1 && chosen.size() < ID);

                            if (E2 <= E1 && currentMove[i][j].getId() != -2) {
                                currentMove[i][j].setId(randomId);
                            }
                        }
                    }
                    licznik++;
                    try {
                        repain();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        monte = new Thread(t3);
        monte.start();

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        listColor.clear();
        ID = 0;
        for (int k = 0; k < 40; k++) {
            Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
            listColor.add(col);

            int x, y;

            do {
                OptionalInt randomNumber = random.ints(0, hei).findFirst();
                OptionalInt randomNumber2 = random.ints(0, wid).findFirst();
                x = randomNumber.getAsInt();
                y = randomNumber2.getAsInt();
            } while (currentMove[x][y].getId() == -2);
            currentMove[x][y].setId(ID);
            ID++;
        }
        repain();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        celularAutomata();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        if (timerTask != null) {
            timerTask.cancel();
        }
        Set<Integer> setId = new LinkedHashSet<>();
        colorChosen = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
        while (setId.size() != 4) {
            setId.add(random.ints(0, ID).findAny().getAsInt());
        }
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                if (setId.contains(currentMove[i][j].getId())) {
                    currentMove[i][j].setId(-2);
                } else {
                    currentMove[i][j].setId(-1);
                }
            }
        }
        repain();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jPanel2ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel2ComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2ComponentResized

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        homo = Integer.parseInt(jTextField1.getText());
        if (timerTask != null) {
            timerTask.cancel();
        }
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                currentMove[i][j].setH(homo);
            }
        }
        repainEnergy();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        if (timerTask != null) {
            timerTask.cancel();
        }
        homo = Integer.parseInt(jTextField1.getText());
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                currentMove[i][j].setH(homo);
            }
        }
        hetero = Integer.parseInt(jTextField5.getText());
        for (int i = 0; i < hei; i++) {
            for (int j = 0; j < wid; j++) {
                int a = currentMove[i][j].getId();
                int b = checkNeighborMoore(i, j);
                if (a == b) {
                    continue;
                } else {
                    for (int k = i; k <= i; k++) {
                        for (int l = j; l <= j; l++) {
                            try {
                                currentMove[k][l].setH(hetero);
                            } catch (Exception e) {

                            }
                        }
                    }
                }

            }
        }
        repainEnergy();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void addConstantNucleons()
    {
        for (int i = 0; i < 10; i++) {
            listColor.add(new Color(random.ints(0, 255).findFirst().getAsInt(), 0, 0));
            int x = 0;
            int y = 0;
            if (onGB) {
                do {
                    OptionalInt randomNumber = random.ints(0, hei).findAny();
                    OptionalInt randomNumber2 = random.ints(0, wid).findAny();
                    x = randomNumber.getAsInt();
                    y = randomNumber2.getAsInt();
                } while (currentMove[x][y].getId() == checkNeighborMoore(x, y));
            } else {
                OptionalInt randomNumber = random.ints(0, hei).findAny();
                OptionalInt randomNumber2 = random.ints(0, wid).findAny();
                x = randomNumber.getAsInt();
                y = randomNumber2.getAsInt();
            }
            currentMove[x][y].setH(0);
            currentMove[x][y].setId(ID++);
            currentMove[x][y].setRec(true);
        }
        repain();
        repainEnergy();
    }
    
    private void add100nucleons()
    {
        for (int i = 0; i < 200; i++) {
            listColor.add(new Color(random.ints(0, 255).findFirst().getAsInt(), 0, 0));
            int x = 0;
            int y = 0;
            if (onGB) {
                do {
                    OptionalInt randomNumber = random.ints(0, hei).findAny();
                    OptionalInt randomNumber2 = random.ints(0, wid).findAny();
                    x = randomNumber.getAsInt();
                    y = randomNumber2.getAsInt();
                } while (currentMove[x][y].getId() == checkNeighborMoore(x, y));
            } else {
                OptionalInt randomNumber = random.ints(0, hei).findAny();
                OptionalInt randomNumber2 = random.ints(0, wid).findAny();
                x = randomNumber.getAsInt();
                y = randomNumber2.getAsInt();
            }
            currentMove[x][y].setH(0);
            currentMove[x][y].setId(ID++);
            currentMove[x][y].setRec(true);
        }
        repain();
        repainEnergy();
    }
    
    private void addInreasingNucleons(int amount)
    {
        for (int i = 0; i < amount; i++) {
            listColor.add(new Color(random.ints(0, 255).findFirst().getAsInt(), 0, 0));
            int x = 0;
            int y = 0;
            if (onGB) {
                do {
                    OptionalInt randomNumber = random.ints(0, hei).findAny();
                    OptionalInt randomNumber2 = random.ints(0, wid).findAny();
                    x = randomNumber.getAsInt();
                    y = randomNumber2.getAsInt();
                } while (currentMove[x][y].getId() == checkNeighborMoore(x, y));
            } else {
                OptionalInt randomNumber = random.ints(0, hei).findAny();
                OptionalInt randomNumber2 = random.ints(0, wid).findAny();
                x = randomNumber.getAsInt();
                y = randomNumber2.getAsInt();
            }
            currentMove[x][y].setH(0);
            currentMove[x][y].setId(ID++);
            currentMove[x][y].setRec(true);
        }
        repain();
        repainEnergy();
    }
    void rec(int i, int j) {
        List<Integer> chosen = new ArrayList<Integer>();
        int[] xtemp = new int[3];
        int[] ytemp = new int[3];
        if (isPeriod) {
            xtemp[0] = (i == 0 ? hei - 1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == hei - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? wid - 1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == wid - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == hei - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == wid - 1 ? -1 : j + 1);
        }

        int E1 = 0;
        int E2 = 0;
        boolean isRecry = false;
        int hOfRecID = 0;
        int idRec = 0;
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                if (currentMove[xtemp[k]][ytemp[l]].isRec()) {
                    System.out.println("jest");
                    isRecry = true;
                    hOfRecID = currentMove[xtemp[k]][ytemp[l]].getH();
                    idRec = currentMove[xtemp[k]][ytemp[l]].getId();
                }
            }
        }

        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                if (xtemp[k] == -1 || ytemp[l] == -1 || (k == 1 && l == 1)) {
                    continue;
                }
                if (currentMove[xtemp[k]][ytemp[l]].getId() != currentMove[i][j].getId()) {
                    E1++;
                }
            }
        }

        E1 += currentMove[i][j].getH();
        chosen.clear();
        E2 = 0;
        if (isRecry) {
            for (int k = 0; k < 3; k++) {
                for (int l = 0; l < 3; l++) {
                    if (xtemp[k] == -1 || ytemp[l] == -1 || (k == 1 && l == 1)) {
                        continue;
                    }
                    if (currentMove[xtemp[k]][ytemp[l]].getId() != idRec) {
                        E2++;
                    }
                }
            }
            if (E2 <= E1) {
                currentMove[i][j].setId(idRec);
                currentMove[i][j].setH(0);
                currentMove[i][j].setRec(true);
                System.out.println(i + " " + j);
            }
        }
    }

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
       t4 = new Runnable() {
            public void run() {
                while (true) {
                    OptionalInt randomNumber = random.ints(0, hei).findAny();
                    OptionalInt randomNumber2 = random.ints(0, wid).findAny();
                    int x = randomNumber.getAsInt();
                    int y = randomNumber2.getAsInt();
                    rec(x, y);

                    repain();
                    repainEnergy();

                }
            }
        };
        t5 = new Runnable() {
            public void run() {
                int counter = 0;
                int amount = 0;
                while (true) {
                    if(counter == 10 && amount <=150)
                    {
                        addConstantNucleons();
                        counter = 0;
                    }
                    OptionalInt randomNumber = random.ints(0, hei).findAny();
                    OptionalInt randomNumber2 = random.ints(0, wid).findAny();
                    int x = randomNumber.getAsInt();
                    int y = randomNumber2.getAsInt();
                    rec(x, y);

                    repain();
                    repainEnergy();
                    counter ++;
                    amount ++;
                }
            }
        };
        t6 = new Runnable() {
            public void run() {
                int counter = 0;
                int amount = 0;
                while (true) {
                    if(counter == 10 && amount <=150)
                    {
                        addInreasingNucleons(amount);
                        counter =0;
                    }
                    OptionalInt randomNumber = random.ints(0, hei).findAny();
                    OptionalInt randomNumber2 = random.ints(0, wid).findAny();
                    int x = randomNumber.getAsInt();
                    int y = randomNumber2.getAsInt();
                    rec(x, y);

                    repain();
                    repainEnergy();
                    counter ++;
                    amount ++;
                }
            }
        };
        switch(nucleons)
       {
           case "100 nucleons":
                   rec = new Thread(t4);
                   rec.start();
                   break;
           case "Constant":
                   rec = new Thread(t5);
                   rec.start();
                   break;
           case "Increasing":
                   rec = new Thread(t5);
                   rec.start();
                   break;   
       }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        if (jCheckBox2.isSelected()) {
            onGB = true;
        } else {
            onGB = false;
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        nucleons = jComboBox2.getSelectedItem().toString();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        switch (nucleons) {
            case "100 nucleons":
                add100nucleons();
                break;
            case "Constant":
                addConstantNucleons();
                break;
            case "Increasing":
                addInreasingNucleons(10);
                break;
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void celularAutomata() {
        timerTask = new TimerTask() {
            public void run() {
                Komorka[][] next = new Komorka[hei][wid];
                for (int i = 0; i < hei; i++) {
                    for (int j = 0; j < wid; j++) {
                        next[i][j] = new Komorka();
                        if (currentMove[i][j].getId() == -1) {

                            if (sasiedztwo.equals("Moore")) {
                                if (currentMove[i][j].getId() == -2) {

                                } else {
                                    int id = checkNeighborMoore(i, j);
                                    next[i][j].setId(id);
                                }
                            }
                        } else {
                            next[i][j] = currentMove[i][j];
                        }
                    }
                }
                currentMove = next;

                repain();

                try {
                    Thread.sleep(50);

                } catch (InterruptedException ex) {
                    Logger.getLogger(Form.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        time.scheduleAtFixedRate(timerTask, 0, 30);

    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Form().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}

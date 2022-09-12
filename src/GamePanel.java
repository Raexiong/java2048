import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private static final int ROWS = 4;
    private static final int COLS = 4;
    private JFrame frame = null;
    private GamePanel panel = null;
    private Card[][] cards = new Card[ROWS][COLS];
    private String gameFlag = "start";

    public GamePanel(JFrame frame){
        this.setLayout(null);
        this.setOpaque(false);
        this.frame = frame;
        this.panel = this;

        createMenu();// create Menu

        // create card
        initCard();

        // created a new card randomly
        createRandomNum();

        //create keyboard listener
        createKeyListener();

    }

    private void createKeyListener() {
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(!"start".equals(gameFlag)){
                    return;
                }
                int key = e.getKeyCode();
                switch (key){
                    // move up
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        moveCard(1);
                        break;
                    // move right
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        moveCard(2);
                        break;
                    // move down
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        moveCard(3);
                        break;
                    // move left
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        moveCard(4);
                        break;
                }

            }
        };
        frame.addKeyListener(keyAdapter);
    }

    // move the card to different directions
    private void moveCard(int dir) {
        // clear card's merge label
        clearCard();

        if(dir == 1){
            moveCardTop(true);
        }else if(dir == 2){
            moveCardRight(true);
        }else if(dir == 3){
            moveCardBottom(true);
        }else if(dir == 4){
            moveCardLeft(true);
        }

        // create new card
        createRandomNum();

        // repaint after each move
        repaint();

        // check if game is over
        gameOverOrNot();

    }

    private void gameOverOrNot(){

        if(isWin()){
            gameWin();
        }else if(cardIsFull()){
            if(moveCardTop(false) || moveCardRight(false) || moveCardBottom(false) || moveCardLeft(false)){
                return;
            }else {
                gameOver();
            }
        }
    }

    private void gameOver() {

        gameFlag = "end";
        // pop out "end" notice
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Times New Roman", Font.ITALIC, 18)));
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Times New Roman", Font.ITALIC, 18)));
        JOptionPane.showMessageDialog(frame, "Oops, you lose. Try again!");
    }

    private void gameWin() {

        gameFlag = "end";
        // pop out "end" notice
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Times New Roman", Font.ITALIC, 18)));
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Times New Roman", Font.ITALIC, 18)));
        JOptionPane.showMessageDialog(frame, "Great Job! You win!");
    }

    private boolean isWin() {
        Card card;
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                card = cards[i][j];

                if(card.getNum() == 2048){
                    return true;
                }
            }
        }
        return false;
    }

    private void clearCard() {
        Card card;
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                card = cards[i][j];
                card.setMerge(false);
            }
        }
    }

    private boolean moveCardLeft(boolean b) {
        Card card;
        boolean res = false;
        for(int i = 0; i < ROWS; i++) {
            for (int j = 1; j < COLS; j++) {
                card = cards[i][j];
                if (card.getNum() != 0) {// move card if card is not blank
                    if(card.moveLeft(cards, b)){
                        res = true;
                    }
                }
            }
        }
        return res;
    }

    private boolean moveCardBottom(boolean b) {
        Card card;
        boolean res = false;
        for(int i = ROWS - 2; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                card = cards[i][j];
                if (card.getNum() != 0) {// move card if card is not blank
                    if(card.moveBottom(cards, b)){
                        res = true;
                    }
                }

            }
        }
        return res;
    }

    private boolean moveCardRight(boolean b) {
        Card card;
        boolean res = false;
        for(int i = 0; i < ROWS; i++) {
            for (int j = COLS - 2; j >= 0; j--) {
                card = cards[i][j];

                if (card.getNum() != 0) {// move card if card is not blank
                    if(card.moveRight(cards, b)){
                        res = true;
                    }
                }
            }
        }
        return res;
    }

    private boolean moveCardTop(boolean b) {

        Card card;
        boolean res = false;
         for(int i = 1; i < ROWS; i++) {
             for (int j = 0; j < COLS; j++) {
                 card = cards[i][j];
                 if (card.getNum() != 0) {// move card if card is not blank
                     if(card.moveTop(cards, b)){
                         res = true;
                     }
                 }

             }
         }
        return res;
    }

    private void createRandomNum() {
        // generate 2 or 4 randomly while frequency of 2 to 4 is 1:4
        int num = 0;
        Random random = new Random();
        int n = random.nextInt(5) + 1; // generate 1-5 randomly
        if(n == 1){
            num = 4;
        }else{
            num = 2;
        }

        // if the board is full, then don't need to get card
        if(cardIsFull()){
            return;
        }

        // get the card
        Card card = getRandomCard(random);

        // set the card
        if(card != null){
            card.setNum(num);
        }

    }

    private boolean cardIsFull() {
        Card card;
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                card = cards[i][j];
                if(card.getNum() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private Card getRandomCard(Random random) {
        int i = random.nextInt(ROWS);
        int j = random.nextInt(COLS);
        Card card = cards[i][j];

        if(card.getNum() == 0){ // if it is a blank card, then return
            return card;
        }
        // if we didn't find any card, then use recursion to keep searching
        return getRandomCard(random);

    }

    private void initCard() {
        Card card;
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                card = new Card(i, j);
                cards[i][j] = card;
            }
        }
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        // draw the cards
        drawCard(g);
    }

    // draw card
    private void drawCard(Graphics g) {
        Card card;
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLS; j++){
                card = cards[i][j];
                card.draw(g);
            }
        }
    }


    private Font createFont(){
        return new Font("Serif", Font.BOLD, 12);
    }
    private void createMenu() {
        Font tFont = createFont();
        //create jmenu bar
        JMenuBar jmb = new JMenuBar();

        // JMenu 1
        JMenu jMenu1 = new JMenu("Game");
        jMenu1.setFont(tFont);

        JMenuItem jmi1 = new JMenuItem("New Game");
        jmi1.setFont(tFont);
        JMenuItem jmi2 = new JMenuItem("Exit");
        jmi2.setFont(tFont);

        jMenu1.add(jmi1);
        jMenu1.add(jmi2);

        // JMenu2
        JMenu jMenu2 = new JMenu("Help");
        jMenu2.setFont(tFont);

        JMenuItem jmi3 = new JMenuItem("Operation Help");
        jmi3.setFont(tFont);
        JMenuItem jmi4 = new JMenuItem("Win Requirements");
        jmi4.setFont(tFont);

        jMenu2.add(jmi3);
        jMenu2.add(jmi4);


        //JMenu3
//        JMenu jMenu3 = new JMenu("Game");
//        jMenu3.setFont(tFont);




        jmb.add(jMenu1);
        jmb.add(jMenu2);
//        jmb.add(jMenu3);

        frame.setJMenuBar(jmb);

        // add action listener
        jmi1.addActionListener(this);
        jmi2.addActionListener(this);
        jmi3.addActionListener(this);
        jmi4.addActionListener(this);

        // set instructions
        jmi1.setActionCommand("restart");
        jmi2.setActionCommand("exit");
        jmi3.setActionCommand("help");
        jmi4.setActionCommand("win");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if("restart".equals(command)){
            System.out.println("New Game");
            restart();
        }else if("exit".equals(command)){
            System.out.println("Exit");
            Object[] options = {"Yes", "Cancel"};
            int res = JOptionPane.showOptionDialog(this, "Are you sure to exit?", "",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if(res == 0){ // make sure to exit
                System.exit(0);
            }
        }else if("help".equals(command)){
            System.out.println("Help");
            JOptionPane.showMessageDialog(null, "press direction keys on your keyboard to move the board, same numbers will merge",
                    "Notice!", JOptionPane.INFORMATION_MESSAGE);
        }else if("win".equals(command)){
            System.out.println("Win");
            JOptionPane.showMessageDialog(null, "You win if you get number 2048, lose when there's no empty space",
                    "Notice!", JOptionPane.INFORMATION_MESSAGE);
        }


    }

    private void restart() {
    }



}











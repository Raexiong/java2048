import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;

import java.awt.*;

public class Card {

    private int x = 0; // x coordinate
    private int y = 0; // y coordinate
    private int w = 80; // width
    private int h = 80; // height
    private int i = 0; // index i
    private int j = 0; // index j

    private int start = 10; // offset
    private int num = 0; // numbers
    private boolean merge = false; // merged or not, if merged already, then can't be merged anymore

    public Card(int i, int j){
        this.i = i;
        this.j = j;

        cal();
    }

    // calculate the coordinate
    private void cal(){
        this.x = start + j*w + (j+1)*5;
        this.y = start + i*h + (i+1)*5;
    }
    // draw one single card
    public void draw(Graphics g) {
        // set the color according to the number
        Color color = getColor();

        Color oldColor= g.getColor();
        // set new color
        g.setColor(color);
        g.fillRoundRect(x, y, w, h, 4, 4);
        // draw number
        if (num != 0){
            g.setColor(new Color(125, 78, 51));
            Font font = new Font("Times New Roman", Font.BOLD, 30);
            g.setFont(font);
            String text = num + "";
            int textLen = getWordWidth(font, text, g);
            int tx = x + (w - textLen) / 2;
            int ty = y + 50;
            g.drawString(text, tx, ty);
        }

        // restore the old color
        g.setColor(oldColor);

    }

    // get the length of the number string
    public static int getWordWidth(Font font, String content, Graphics g){

        FontMetrics metrics = g.getFontMetrics(font);
        // FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int width = 0;
        for (int i = 0; i < content.length(); i++){
            width += metrics.charWidth(content.charAt(i));
        }
        return width;

    }


    // get color, color is based on num
    private Color getColor(){
        Color color = null;
        switch (num){
            case 2:
                color = new Color(238, 244, 234);
                break;
            case 4:
                color = new Color(222, 236, 200);
                break;
            case 8:
                color = new Color(174, 213, 130);
                break;
            case 16:
                color = new Color(142, 201, 75);
                break;
            case 32:
                color = new Color(111, 148, 48);
                break;
            case 64:
                color = new Color(76, 174, 124);
                break;
            case 128:
                color = new Color(60, 180, 144);
                break;
            case 256:
                color = new Color(45, 130, 120);
                break;
            case 512:
                color = new Color(9, 97, 26);
                break;
            case 1024:
                color = new Color(242, 177, 121);
                break;
            case 2048:
                color = new Color(223, 185, 0);
                break;

            default: // default color
                color = new Color(92, 151, 117);
                break;

        }
        return color;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }

    // move up
    public boolean moveTop(Card[][] cards, boolean b) {
        // base case of recursion
        if(i==0){
            return false;
        }
        // last card
        Card prev = cards[i-1][j];

        if(prev.getNum()==0){
            if(b){
                prev.num = this.num;
                this.num = 0;

                prev.moveTop(cards, b);
            }
            return true;
        }else if(prev.getNum() == num && !prev.merge){// merge if two cards have the same number
            if(b) {

                prev.merge = true;
                prev.num = this.num * 2;
                this.num = 0;
            }
            return true;
        }else{
            return false;
        }

    }

    public void setMerge(boolean b) {
        this.merge = b;
    }

    public boolean moveRight(Card[][] cards, boolean b) {

        // base case of recursion
        if(j==3){
            return false;
        }
        // last card
        Card prev = cards[i][j+1];

        if(prev.getNum()==0){
            if(b) {

                prev.num = this.num;
                this.num = 0;


                prev.moveRight(cards, b);
            }
            return true;
        }else if(prev.getNum() == num && !prev.merge){// merge if two cards have the same number
            if(b) {
                prev.merge = true;
                prev.num = this.num * 2;
                this.num = 0;
            }
            return true;
        }else{
            return false;
        }
    }

    public boolean moveBottom(Card[][] cards, boolean b) {
        // base case of recursion
        if(i==3){
            return false;
        }
        // last card
        Card prev = cards[i+1][j];

        if(prev.getNum()==0){
            if(b) {

                prev.num = this.num;
                this.num = 0;


                prev.moveBottom(cards, b);
            }
            return true;
        }else if(prev.getNum() == num && !prev.merge){// merge if two cards have the same number
            if(b) {
                prev.merge = true;
                prev.num = this.num * 2;
                this.num = 0;
            }
            return true;
        }else{
            return false;
        }

    }


    public boolean moveLeft(Card[][] cards, boolean b) {
        // base case of recursion
        if(j==0){
            return false;
        }
        // last card
        Card prev = cards[i][j-1];

        if(prev.getNum()==0){
            if(b){
                prev.num = this.num;
                this.num = 0;

                prev.moveLeft(cards, b);
            }
            return true;
        }else if(prev.getNum() == num && !prev.merge) {// merge if two cards have the same number
            if (b) {
                prev.merge = true;
                prev.num = this.num * 2;
                this.num = 0;
            }
            return true;
        }else{
                return false;
            }
    }

}





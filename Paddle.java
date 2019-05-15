// Daniel Reader
package ping;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author fatla
 */
public class Paddle {

    public int paddleNumber;
    public int x, y, width = 50, height = 250;
    public int score;

    public Paddle(Ping ping, int paddleNumber) {
        this.paddleNumber = paddleNumber;

        if (paddleNumber == 1) {
            this.x = 0;
        }
        if (paddleNumber == 2) {
            this.x = ping.width - width;
        }
        this.y = (ping.height / 2) - (this.height / 2);
    }

    void render(Graphics g, int theme) {
        if (theme == 0) {// change colour based on theme choice from main class
            g.setColor(Color.WHITE);
        } else if (theme == 1) {
            g.setColor(Color.RED);
        } else if (theme == 2) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }
        g.fillRect(x, y, width, height);
    }

    void move(boolean up) {
        int speed = 10;
        if (up) {
            if (y - speed > 0) {
                y -= speed;
            } else {
                y = 0;
            }
        } else {
            if (y + height + speed < Ping.ping.height) {
                y += speed;
            } else {
                y = Ping.ping.height - height;
            }
        }
    }

}

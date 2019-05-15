// Daniel Reader
package ping;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author fatla
 */
public class Ball {

    public int x, y, width = 25, height = 25;
    public static int motionx, motiony;
    public Random random;

    private Ping ping;

    public int amountOfHits;

    public Ball(Ping ping) {
        this.ping = ping;

        this.random = new Random();

        spawn();

    }

    public void update(Paddle paddle1, Paddle paddle2) {
        int speed = 5;
        this.x += motionx * speed;
        this.y += motiony * speed;

        if (this.y + height - motiony > ping.height || this.y + motiony < 0) {
            if (this.motiony < 0) {
                this.y = 0;
                this.motiony = random.nextInt(4);

                if (motiony == 0) {
                    motiony = 1;
                }

            } else {
                this.motiony = -random.nextInt(4);
                this.y = ping.height - height;

                if (motiony == 0) {
                    motiony = -1;
                }
            }

        }

        if (checkCollision(paddle1) == 1) {
            this.motionx = 1 + (amountOfHits / 5);
            this.motiony = -2 + random.nextInt(4);
            if (motiony == 0) {
                motiony = 1;
            }
            amountOfHits++;
        } else if (checkCollision(paddle2) == 1) {
            this.motionx = -1 - (amountOfHits / 5);
            this.motiony = -2 + random.nextInt(4);

            if (motiony == 0) {
                motiony = 1;
            }
            amountOfHits++;
        }

        if (checkCollision(paddle1) == 2) {
            paddle2.score++;
            spawn();
        } else if (checkCollision(paddle2) == 2) {
            paddle1.score++;
            spawn();
        }

    }

    public void spawn() {
        this.amountOfHits = 0;

        this.x = ping.width / 2 - this.width / 2;
        this.y = ping.height / 2 - this.height / 2;
        this.motiony = -2 + random.nextInt(4);

        if (motiony == 0) {
            motiony = 1;
        }

        if (random.nextBoolean()) {
            motionx = 1;
        } else {
            motionx = -1;
        }
    }

    public int checkCollision(Paddle paddle) {
        if (this.x < paddle.x + paddle.width && this.x + width > paddle.x && this.y < paddle.y + paddle.height && this.y + height > paddle.y) {
            return 1;//bounce
        } else if ((paddle.x > x && paddle.paddleNumber == 1) || (paddle.x < x - width && paddle.paddleNumber == 2)) {
            return 2;//score
        }
        return 0;//nothing
    }

    /**
     *
     * @param g
     * @param theme
     */
    public void render(Graphics g, int theme) {
        if (theme == 0) { // set colour based on selected theme
            g.setColor(Color.WHITE);
        } else if (theme == 1 || theme == 3) {
            g.setColor(Color.BLACK);
        } else if (theme == 2) {
            g.setColor(Color.ORANGE);
        }
        g.fillOval(x, y, width, height);
    }
}

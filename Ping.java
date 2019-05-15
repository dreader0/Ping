// Daniel Reader
package ping;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public abstract class Ping implements ActionListener, KeyListener {

    public static Ping ping;

    public JFrame jframe;

    public int width = 700, height = 700;

    public Renderer renderer;

    public Paddle player1;
    public Paddle player2;
    public Ball ball;

    public boolean bot = false, selectingDifficulty;
    public boolean w, s, up, down;
    public boolean changeTheme, controls = false; //Choice to change colour of ball and paddles, and boolean for controls screen

    public Random random;

    public int gameStatus = 0; //0 = Menu, 1 = Paused, 2 = Playing, 3 = Over
    public int botDifficulty, botMoves, botCooldown = 0;
    public int scoreLimit = 7, playerWon;

    public int theme; // used for different colour choices 

    public Ping() {
        Timer timer = new Timer(20, this);
        random = new Random();
        jframe = new JFrame("PING");

        renderer = new Renderer();

        jframe.setSize(width + 18, height + 45);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderer);
        jframe.addKeyListener(this);

        timer.start();
    }

    public void start() {
        gameStatus = 2;
        player1 = new Paddle(this, 1);
        player2 = new Paddle(this, 2);
        ball = new Ball(this);
    }

    public void update() {

        if (player1.score == scoreLimit) {
            playerWon = 1;
            gameStatus = 3;
        }
        if (player2.score == scoreLimit) {
            playerWon = 2;
            gameStatus = 3;
        }
        if (w) {
            player1.move(true);
        }
        if (s) {
            player1.move(false);
        }
        if (!bot) {
            if (up) {
                player2.move(true);
            }
            if (down) {
                player2.move(false);
            }
        } else {
            if (botCooldown > 0) {
                botCooldown--;
                if (botCooldown == 0) {
                    botMoves = 0;
                }
            }
            if (botMoves < 10) {
                if (player2.y + player2.height > ball.y) {
                    player2.move(true);
                    botMoves++;
                }
                if (player2.y + player2.height / 2 < ball.y) {
                    player2.move(false);
                    botMoves++;
                }
                if (botDifficulty == 0) {
                    botCooldown = 20;
                }
                if (botDifficulty == 1) {
                    botCooldown = 15;
                }
                if (botDifficulty == 2) {
                    botCooldown = 5;
                }

            }

        }
        ball.update(player1, player2);
    }

    public void render(Graphics2D g) {

        if (theme == 1 || theme == 3) { //set the colour depending on the theme choice
            g.setColor(Color.WHITE);
        } else if (theme == 2) {
            g.setColor(Color.CYAN);
        } else {
            g.setColor(Color.BLACK);
        }
        g.fillRect(0, 0, width, height);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (gameStatus == 0) {
            if (theme == 1 || theme == 3) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }
            g.setFont(new Font("Arial", 1, 50));

            g.drawString("PING", width / 2 - 60, 50);

            if (!selectingDifficulty && !changeTheme && !controls) {//only show this when no other menu is chosen

                g.setFont(new Font("Arial", 1, 30));

                g.drawString("Press Space to Play", width / 2 - 150, height / 2 - 75);
                g.drawString("Press Shift to Play with Bot", width / 2 - 200, height / 2 - 25);
                g.drawString("<< Score Limit: " + scoreLimit + " >>", width / 2 - 150, height / 2 + 25);
                g.drawString("Press CTRL to Change Theme", width / 2 - 220, height / 2 + 75);//theme menu prompt
                g.drawString("  Press C to View Controls", width / 2 - 200, height / 2 + 125);//controls screen prompt
            }
        }
        if (selectingDifficulty) {
            String string = botDifficulty == 0 ? "Easy" : (botDifficulty == 1 ? "Medium" : "Hard");
            g.setFont(new Font("Arial", 1, 30));

            g.drawString("<< Bot Difficulty: " + string + " >>", width / 2 - 175, height / 2 - 25);
            g.drawString("Press Space to Play", width / 2 - 150, height / 2 + 25);
            g.drawString("Press ESC for Menu", width / 2 - 150, height / 2 + 75);//exit to menu prompt
        }
        if (changeTheme) {
            String string = theme == 0 ? "Classic" : theme == 1 ? "Hockey" : theme == 2 ? "   Sky   " : "Inverse"; // similar to difficulty selection but with 4 possible themes
            g.setFont(new Font("Arial", 1, 30));

            g.drawString("<< Theme: " + string + " >>", width / 2 - 160, height / 2 - 25);
            g.drawString("Press Space to Play", width / 2 - 150, height / 2 + 25);
            g.drawString("Press ESC for Menu", width / 2 - 150, height / 2 + 75);//exit to menu prompt
        }
        if (controls) {//check if controls = true, if it is, display controls
            g.setFont(new Font("Arial", 1, 30));

            g.drawString("   W and S - Left Paddle", width / 2 - 160, height / 2 - 50);
            g.drawString("Arrow Keys - Right Paddle", width / 2 - 187, height / 2);
            g.drawString("Spacebar - Play/Pause", width / 2 - 155, height / 2 + 50);
            g.drawString("Press ESC for Menu", width / 2 - 140, height / 2 + 100);

        }

        if (gameStatus == 1) {
            if (theme == 1 || theme == 3) {// set colour based on theme choice
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("PAUSED", width / 2 - 103, height / 2 - 25);

            g.setFont(new Font("Arial", 1, 30));
            g.drawString("Press ESC for Menu", width / 2 - 150, height / 2 + 175);

        }

        if (gameStatus == 1 || gameStatus == 2) {
            if (theme == 1) { // set colour based on theme 
                g.setColor(Color.RED);
            } else if (theme == 3) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }

            g.setStroke(new BasicStroke(3f));

            g.drawLine(width / 2, 0, width / 2, height);

            g.setFont(new Font("Arial", 1, 50));
            if (theme == 1 || theme == 3) {
                g.setColor(Color.BLACK);
            }
            g.drawString(String.valueOf(player1.score), width / 2 - 90, 50);
            g.drawString(String.valueOf(player2.score), width / 2 + 65, 50);

            if (theme == 1) {
                g.setColor(Color.BLUE);
            }
            g.drawOval(width / 2 - 150, height / 2 - 150, 300, 300);

            player1.render(g, theme);
            player2.render(g, theme);
            ball.render(g, theme);
        }

        if (gameStatus == 3) {
            if (theme == 1 || theme == 3) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }
            g.setFont(new Font("Arial", 1, 50));

            g.drawString("PING", width / 2 - 60, 50);
            if (bot && player2.score == scoreLimit) {
                g.drawString("The Bot Wins!", width / 2 - 175, 200);
            } else {
                g.drawString("Player " + playerWon + " Wins!", width / 2 - 165, 200);
            }
            g.setFont(new Font("Arial", 1, 30));

            g.drawString("Press Space to Play Again", width / 2 - 185, height / 2 - 25);
            g.drawString("Press ESC for Menu", width / 2 - 140, height / 2 + 25);

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStatus == 2) {
            update();
        }
        renderer.repaint();

    }

    public static void main(String[] args) {
        ping = new Ping() {
        };
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = true;
        } else if (id == KeyEvent.VK_S) {
            s = true;
        } else if (id == KeyEvent.VK_UP) {
            up = true;
        } else if (id == KeyEvent.VK_DOWN) {
            down = true;
        } else if (id == KeyEvent.VK_RIGHT) {
            if (selectingDifficulty) {
                if (botDifficulty < 2) {
                    botDifficulty++;
                } else {
                    botDifficulty = 0;
                }
            }
            if (changeTheme && gameStatus == 0) {
                if (theme < 3) {
                    theme++;
                } else {
                    theme = 0;
                }
            } else if (gameStatus == 0 && (!selectingDifficulty) && (!changeTheme) && (!controls)) {
                scoreLimit++;
            }
        } else if (id == KeyEvent.VK_LEFT) {
            if (selectingDifficulty) {
                if (botDifficulty > 0) {
                    botDifficulty--;
                } else {
                    botDifficulty = 2;
                }
            }
            if (changeTheme && gameStatus == 0) { //read key presses to change theme 
                if (theme > 0) {
                    theme--;
                } else {
                    theme = 3;
                }
            } else if (gameStatus == 0 && scoreLimit > 1 && (!selectingDifficulty) && (!changeTheme) && (!controls)) { //score limit only works on the main menu, not if any other one has been selected
                scoreLimit--;
            }
        } else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 3 || gameStatus == 1 || (changeTheme) || (selectingDifficulty) || (controls))) { //escape exits to main menu from any other menu

            gameStatus = 0;
            controls = false;
            changeTheme = false;
            selectingDifficulty = false;

        } else if (id == KeyEvent.VK_SHIFT && gameStatus == 0 && !changeTheme && !controls) {
            bot = true;
            selectingDifficulty = true;

        } else if (id == KeyEvent.VK_SPACE) {
            if ((gameStatus == 0 || gameStatus == 3) && (!controls)) {

                if (!selectingDifficulty) {
                    bot = false;
                } else {
                    selectingDifficulty = false;
                }
                if (bot) {
                    selectingDifficulty = true;
                }
                start();

            } else if (gameStatus == 1) {
                gameStatus = 2;
            } else if (gameStatus == 2) {
                gameStatus = 1;
            }
        } else if (id == KeyEvent.VK_CONTROL) { // ADDING MY COLOUR OPTIONS
            changeTheme = true;
        } else if (id == KeyEvent.VK_C) { // added a new key just for the controls. I tried ALT but that messed things up for some reason, C works just fine though
            controls = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int id = ke.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = false;
        } else if (id == KeyEvent.VK_S) {
            s = false;
        } else if (id == KeyEvent.VK_UP) {
            up = false;
        } else if (id == KeyEvent.VK_DOWN) {
            down = false;
        }

    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

}

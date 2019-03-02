import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Random;

class Coin extends Sprite
{
  static BufferedImage coin = null;

  static Random rand = new Random();

  //coin constructor
  Coin(Model m, int _x, int _y)
  {
    super(m);
    x = _x + 15;
    y = _y - 25;
    w = 60;
    h = 60;
    horiz_vel = rand.nextDouble() * 16 - 8;
    vert_vel = -20.0;

    if(coin == null)
    {
      try
      {
      	coin = ImageIO.read(new File("./images/coin.png"));
      }
      catch(Exception e)
      {
      	e.printStackTrace(System.err);
      	System.exit(1);
      }
    }
  }

  //coin copy constructor
  Coin(Coin that, Model newmodel)
  {
    super(that, newmodel);
    this.vert_vel = that.vert_vel;
    this.horiz_vel = that.horiz_vel;
  }

  Coin clone_this_sprite(Model newmodel)
  {
    return new Coin(this, newmodel);
  }

  boolean update()
  {
    //model.scrollPos = x - 250;

    x += horiz_vel;

		vert_vel += 3.5;
		y += vert_vel;

    if(y > 560)
      return false;

    return true;
  }

  void draw(Graphics g)
  {
    g.drawImage(coin, x - model.scrollPos, y, w, h, null);
  }

  Json marshall()
  {
    return null;
  }


}

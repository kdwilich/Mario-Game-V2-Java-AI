import java.awt.Graphics;

abstract class Sprite
{
  int x;
  int y;
  int w;
  int h;
  double num_hits;
  String type;
  double vert_vel;
  double horiz_vel;
  Model model;

  Sprite(Model m)
  {
    model = m;
  }

  Sprite(Sprite that, Model newmodel)
  {
    this.x = that.x;
    this.y = that.y;
    this.w = that.w;
    this.h = that.h;
    this.model = newmodel;
  }

  abstract Sprite clone_this_sprite(Model newmodel);

  // abstract classes exist to be extended, they can not be instantiated
  // this method must be implemented by any class that extends game object
  abstract boolean update();

  // this method must be implemented by any class that extends game object
  abstract void draw(Graphics g);

  abstract Json marshall();

  boolean am_i_a_brick()
  {
    return false;
  }

  boolean am_i_a_coin_block()
  {
    return false;
  }

  boolean am_i_mario()
  {
    return false;
  }


}

import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;

class Mario extends Sprite
{
	int prev_x;
	int prev_y;
	double gravity = 4.20;
	boolean collides_on_bottom;
	boolean grounded;
	static Image[] mario_images = null;

	//mario constructor
	Mario(Model m)
	{
		super(m);
		x = 250;
		y = 0;
		w = 65;
		h = 90;
		collides_on_bottom = false;
		grounded = false;


		// lazy loading
		if(mario_images == null)
		{
			mario_images = new Image[5];
			try
			{
				mario_images[0] = ImageIO.read(new File("./images/mario1.png"));
				mario_images[1] = ImageIO.read(new File("./images/mario2.png"));
				mario_images[2] = ImageIO.read(new File("./images/mario3.png"));
				mario_images[3] = ImageIO.read(new File("./images/mario4.png"));
				mario_images[4] = ImageIO.read(new File("./images/mario5.png"));
			}
			catch(Exception e)
			{
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}

	}

	//mario json constructor
	Mario(Json ob, Model m)
	{
		super(m);
		type = ob.getString("type");
		x = (int)ob.getLong("x");
		y = (int)ob.getLong("y");
		w = (int)ob.getLong("w");
		h = (int)ob.getLong("h");
		vert_vel = (double)ob.getDouble("vertvel");

		// lazy loading
		if(mario_images == null)
		{
			mario_images = new Image[5];
			try
			{
				mario_images[0] = ImageIO.read(new File("./images/mario1.png"));
				mario_images[1] = ImageIO.read(new File("./images/mario2.png"));
				mario_images[2] = ImageIO.read(new File("./images/mario3.png"));
				mario_images[3] = ImageIO.read(new File("./images/mario4.png"));
				mario_images[4] = ImageIO.read(new File("./images/mario5.png"));
			}
			catch(Exception e)
			{
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}

	//mario copy constructor
	Mario(Mario that, Model newmodel)
	{
		super(that, newmodel);
		this.vert_vel = that.vert_vel;
		this.prev_x = that.prev_x;
		this.prev_y = that.prev_y;
		this.collides_on_bottom = that.collides_on_bottom;
		this.grounded = that.grounded;
	}

	Mario clone_this_sprite(Model newmodel)
	{
		return new Mario(this, newmodel);
	}

	void rememberPrevStep()
	{
		prev_x = x;
		prev_y = y;
	}

	boolean doesCollide(Sprite spr)
	{
		if(x + w < spr.x) // mario right side enters sprite left side
			return false;
		else if(x > spr.x + spr.w) // mario left side enter sprite right side
			return false;
		else if(y + h < spr.y) { // mario bottom side enters sprite top side
			return false;
		}
		else if(y > spr.y + spr.h) { // mario top side enters sprite bottom side
			return false;
		}
		else
			return true;

	}

	void collidesBottom(Sprite spr)
	{
		if(prev_x + w > spr.x && prev_x < spr.x + spr.w &&
				y < spr.y + spr.h && y > spr.y) {
			vert_vel = 0;
			collides_on_bottom = true;
		}
		else
			collides_on_bottom = false;
	}

	//System.out.print("in doesCollide on bottom. ");
	//System.out.println("collides is: " + collides_on_bottom);
	//collides_on_bottom = true;

	void getOut(Sprite spr)
	{
		if(x + w > spr.x && prev_x + w <= spr.x) // collides with left
		{
			x = spr.x - w;
		}
	 	else if(x < spr.x + spr.w && prev_x >= spr.w + spr.x) // collides with right
		{
			x = spr.x + spr.w;
		}
		else if(y + h > spr.y && prev_y + h <= spr.y) // collides with top
		{
			y = spr.y - h;
			vert_vel = 0;
			grounded = true;
		}
		else if(y < spr.y + spr.h && prev_y > spr.y + spr.h) // collides with bottom
		{
			//System.out.println("in get out");
			y = spr.y + spr.h;
			//collides_on_bottom = true;
			vert_vel = 0;
		}
	}

	boolean update()
	{
		if(x > 250)
			model.scrollPos = x - 250;
		if(x <= 0)
			x = 0;

		vert_vel += gravity;
		y += vert_vel;

		if(y >= 500)
		{
			y = 500; // snap back to reality
			vert_vel = 0.0;
			grounded = true;
		}
		else
			grounded = false;


		for(int i = 0; i < model.sprites.size(); i++)
		{
			Sprite s = model.sprites.get(i);
			if(s.am_i_a_brick())
			{
				if(doesCollide(s))
				{
					getOut(s);
				}
			}
			if(s.am_i_a_coin_block())
			{
				if(doesCollide(s))
				{
					collidesBottom(s);
					getOut(s);

					if(collides_on_bottom)
					{
						s.num_hits++;
						if(s.num_hits <= 5) {
							model.addCoin(s.x, s.y);
							model.coin_total++;
						}
						collides_on_bottom = false;
					}
				}
			}
		}

		return true;
	}

	void draw(Graphics g)
	{
		// draw Mario
		int marioFrame = (Math.abs(x) / 20) % 5; //20 = rate of running, 5 = frames of mario
		g.drawImage(mario_images[marioFrame], x - model.scrollPos, y, null);

	}

	Json marshall()
	{
		Json ob = Json.newObject();
		ob.add("type", "Mario");
		ob.add("x", x);
		ob.add("y", y);
		ob.add("w", w);
		ob.add("h",h);
		ob.add("vertvel", vert_vel);

		return ob;
	}

	boolean am_i_mario()
	{
		return true;
	}

}

import java.util.ArrayList;
import java.util.Random;


class Model
{
	ArrayList<Sprite> sprites;
	int scrollPos;
	int jump_count;
	int coin_total;




	//redundant mario reference for controller
	Mario mario;

	Model()
	{
		scrollPos = 0;
		jump_count = 0;
		coin_total = 0;

		mario = new Mario(this);
		sprites = new ArrayList<Sprite>();

	}

	Model(Model other)
	{
		scrollPos = other.scrollPos;
		jump_count = other.jump_count;
		coin_total = other.coin_total;

		sprites = new ArrayList<Sprite>();
		for(int i = 0; i < other.sprites.size(); i++)
		{
			Sprite s = other.sprites.get(i);
			Sprite clone = s.clone_this_sprite(this);
			this.sprites.add(clone);
			if(clone.am_i_mario())
			{
				Mario m = (Mario)clone;
				mario = m;
			}
		}
	}

	void doAction(Action i)
	{
		rememberPrevStep();
		if(i == Action.run)
		{
			mario.x += 10;

		}
		else if(i == Action.jump)
		{
			if(mario.grounded) {
				mario.vert_vel = -40;
				jump_count++;
				mario.grounded = false;
			}
		}
		else if(i == Action.jump_and_run)
		{
			mario.x += 10;

			if(mario.grounded) {
				mario.vert_vel = -40;
				jump_count++;
				mario.grounded = false;
			}
		}
	}

	int k = 13;
	int d = 25;
	double evaluateAction(Action action, int depth)
	{
		// Evaluate the state
		if(depth >= d){
			return mario.x + 5000 * coin_total - 2 * jump_count;
		}

		// Simulate the action
		Model copy = new Model(this); // uses the copy constructor
		copy.doAction(action); // like what Controller.update did before
		copy.update(); // advance simulated time

		// Recurse
		if(depth % k != 0)
			 return copy.evaluateAction(action, depth + 1);
		else
		{
			 double best = copy.evaluateAction(Action.run, depth + 1);
			 best = Math.max(best, copy.evaluateAction(Action.jump, depth + 1));
			 best = Math.max(best, copy.evaluateAction(Action.jump_and_run, depth + 1));
			 return best;
		}
	}





	void rememberPrevStep()
	{
		mario.rememberPrevStep();
	}

	public void update()
	{
		for(int i = 0; i < sprites.size(); i++)
		{
			Sprite s = sprites.get(i);
			boolean keep = s.update();
			if(!keep)
			{
				sprites.remove(i);
				i--;
			}
		}
	}

	//called from model in order to pass from controller to sprites
	public void addBrick(int x, int y, int w, int h)
  {
    Sprite b = new Brick(this, x, y, w, h);
    sprites.add(b);
  }

	//called from model in order to pass from controller to sprites
	public void addCoinBlock(int x, int y)
	{
		Sprite cb = new CoinBlock(this, x, y);
		sprites.add(cb);
	}

	public void addCoin(int x, int y)
	{
		Sprite coin = new Coin(this, x, y);
		sprites.add(coin);
	}

	void unmarshall(Json ob)
  {
    sprites.clear();
    Json json_sprites = ob.get("sprites");
    for(int i = 0; i < json_sprites.size(); i++)
    {
    	Json j = json_sprites.get(i);
		String spr = j.getString("type");
		Sprite s;

		if(spr.equals("Brick"))
		{
			s = new Brick(j, this);
		  sprites.add(s);
		}
		else if(spr.equals("CoinBlock"))
		{
			s = new CoinBlock(j, this);
			sprites.add(s);
		}
		else //(spr.equals("Brick"))
		{
			mario = new Mario(j, this);
			sprites.add(mario);
		}

  	  //System.out.println("Sprite being added: " + sprites.add(s));
    }
  }

  Json marshall()
  {
    Json ob = Json.newObject();
    Json json_sprites = Json.newList();
    ob.add("sprites", json_sprites);
    for(int i = 0; i < sprites.size(); i++)
    {
      Sprite s = sprites.get(i);
      Json j = s.marshall();
      json_sprites.add(j);
    }
    return ob;
  }

  void saveMap(String filename)
  {
    Json ob = marshall();
    ob.save(filename);
  }

  void loadMap(String filename)
  {
    Json ob = Json.load(filename);
		unmarshall(ob);
  }
}

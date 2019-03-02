import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Controller implements MouseListener, KeyListener
{
	View view;
	Model model;
	boolean keyLeft;
	boolean keyRight;
	boolean keyUp;
	boolean keyDown;
	boolean keySpace;
	boolean keyR;
	int mouseDownX;
	int mouseDownY;
	boolean AItoggle = true;

	Controller(Model m)
	{
		model = m;
	}

	void setView(View v)
	{
		view = v;
	}

	public void mousePressed(MouseEvent e)
	{
	    mouseDownX = e.getX();
	    mouseDownY = e.getY();
	}

	public void mouseReleased(MouseEvent e)
	{
		int x1 = mouseDownX;
	    int x2 = e.getX();
	    int y1 = mouseDownY;
	    int y2 = e.getY();
	    int left = Math.min(x1, x2);
	    int right = Math.max(x1, x2);
	    int top = Math.min(y1, y2);
	    int bottom = Math.max(y1, y2);

		model.addBrick(left + model.scrollPos, top, right - left, bottom - top);




	}

	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }
	public void mouseClicked(MouseEvent e) {    }

	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
			{
				case KeyEvent.VK_RIGHT: keyRight = true; break;
				case KeyEvent.VK_LEFT: keyLeft = true; break;
				case KeyEvent.VK_UP: keyUp = true; break;
				case KeyEvent.VK_DOWN: keyDown = true; break;
				case KeyEvent.VK_SPACE: keySpace = true; break;
				case KeyEvent.VK_S: model.saveMap("map.json"); break;
				case KeyEvent.VK_T: model.loadMap("map.json"); break;
				case KeyEvent.VK_R: keyR = true; model.loadMap("default.json"); break;
				case KeyEvent.VK_E: model.addCoinBlock(model.mario.x, model.mario.y); break;
				case KeyEvent.VK_W: model.addCoinBlock(mouseDownX + model.scrollPos, mouseDownY); break;
				case KeyEvent.VK_B: AItoggle = true; break;
		}
	}

		public void keyReleased(KeyEvent e)
		{
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_RIGHT: keyRight = false; break;
				case KeyEvent.VK_LEFT: keyLeft = false; break;
				case KeyEvent.VK_UP: keyUp = false; break;
				case KeyEvent.VK_DOWN: keyDown = false; break;
				case KeyEvent.VK_SPACE: keySpace = false; break;
				case KeyEvent.VK_N: AItoggle = false; break;
			}
		}

		public void keyTyped(KeyEvent e)
		{
		}

		void update()
		{
			model.rememberPrevStep();


	    if(keyRight)
			{
				model.mario.x += 12.5;
					//model.scrollPos++;
			}
			if(keyLeft)
			{
				model.mario.x -= 12.5;
					//model.scrollPos--;
			}
			if(keySpace)
			{
				if(model.mario.grounded) {
					//model.jump_count++;
					model.mario.vert_vel = -40.1;
					//System.out.println(model.jump_count);
					model.mario.grounded = false;
				}
			}

		}



		void AIupdate()
		{
			// Evaluate each possible action
			double score_run = model.evaluateAction(Action.run, 0);
			double score_jump = model.evaluateAction(Action.jump, 0);
			double score_jump_and_run = model.evaluateAction(Action.jump_and_run, 0);

			/*pr("score_run\t\t", score_run);
			pr("score_jump\t\t", score_jump);
			pr("score_jump_and_run\t", score_jump_and_run);

			System.out.println(model.jump_count);*/

			//score_jump_and_run = 0;

			// Do the best one
			if(score_jump_and_run > score_jump && score_jump_and_run > score_run){
				//pr("jump run:	", score_jump_and_run);
				model.doAction(Action.jump_and_run);
			}
			else if(score_jump > score_run) {
				//pr("jump: 		", score_jump);
				model.doAction(Action.jump);
			}
			else
				//pr("run		", score_run);
				model.doAction(Action.run);
		}

		void pr(String str, double num)
		{
			System.out.println(str + num);
		}


}

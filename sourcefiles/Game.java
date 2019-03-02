import javax.swing.JFrame;
import java.awt.Toolkit;

public class Game extends JFrame
{
	Model model;
	View view;
	Controller controller;

	public Game()
	{
		model = new Model();
		controller = new Controller(model);
		view = new View(controller, model);
		controller.setView(view);
		view.addMouseListener(controller);
		this.addKeyListener(controller);
		this.setTitle("Mario");
		this.setSize(900, 700);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void run()
	{

		//model.loadMap("default.json");
		model.loadMap("map.json");
		while(true)
		{
			controller.update();
			if(controller.AItoggle){
				controller.AIupdate();
			}
			model.update();
			view.repaint(); // Indirectly calls View.paintComponent
			//Toolkit.getDefaultToolkit().sync(); // Updates screen



			// Go to sleep for 50 miliseconds
			try
			{
				Thread.sleep(50);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}



	public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
	}


}

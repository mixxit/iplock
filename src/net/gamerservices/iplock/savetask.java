package net.gamerservices.iplock;

import java.util.TimerTask;

public class savetask extends TimerTask {

	private iplock parent;
	public savetask(iplock parent)
	{
		this.parent = parent;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (this.parent != null)
		{
			// System.out.print("DEBUG iplock : saving");
			this.parent.saveIPLockUsers();
		}
	}

}

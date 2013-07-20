package com.maogu.chess;

public class TimeThread extends Thread {
	private boolean flag = true;
	private GameView gameView;

	public TimeThread(GameView gameView) {
		this.gameView = gameView;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public void run() {
		while (flag) {
			if (gameView.caiPan == false) {			// ��ǰΪ�ڷ����塢˼��
				gameView.blackTime++;
			} else if (gameView.caiPan == true) {	// ��ǰΪ�췽���塢˼��
				gameView.redTime++;
			}
			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

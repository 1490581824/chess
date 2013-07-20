package com.maogu.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private MainActivity activity;
	private TutorialThread tutorialThread;
	private TimeThread timeThread;
	
	private Bitmap chessboardBmp;	// ����
	private Bitmap chessmanBackgroundBmp;
	private Bitmap winBmp;
	private Bitmap lostBmp;
	private Bitmap okBmp;
	private Bitmap vsBmp;			// �ڷ��췽VS��ͼƬ
	private Bitmap rightPointerBmp;
	private Bitmap leftPointerBmp;
	private Bitmap currentBmp;		// ����ǰ������
	private Bitmap exit2Bmp;
	private Bitmap sound2Bmp;
	private Bitmap sound3Bmp;
	private Bitmap timeBmp;
	private Bitmap redtimeBmp;
	
	private Bitmap[] blackChessman = new Bitmap[7];	// ���ӵ�ͼƬ����
	private Bitmap[] redChessman = new Bitmap[7];	// ���ӵ�ͼƬ����
	private Bitmap[] number = new Bitmap[10];		// ���ֵ�ͼƬ���飬������ʾʱ��
	private Bitmap[] redNumber = new Bitmap[10];	// ��ɫ���ֵ�ͼƬ��������ʾʱ��
	
	private MediaPlayer go;			// ��������
	private Paint paint;
	
	boolean caiPan = true;			// �Ƿ�Ϊ�������
	boolean focus = true;			// ��ǰ�Ƿ���ѡ�е�����
	int selectChessman = 0;			// ��Ȼѡ�е�����
	
	int currentChessmanStartPositionX, currentChessmanStartPositionY; 
	int currentChessmanEndPositionX, currentCHessmanEndPositionY;
	
	int status = 1;					// ��Ϸ״̬��0��Ϸ�У�1ʤ��, 2ʧ��
	int blackTime = 0;				// �ڷ��ܹ�˼��ʱ��
	int redTime = 0;				// �췽�ܹ�˼��ʱ��
	
	private int width;
	private int height;
	
	int[][] chessman = new int[][] { 
		{ 2, 3, 6, 5, 1, 5, 6, 3, 2 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		{ 0, 4, 0, 0, 0, 0, 0, 4, 0 },
		{ 7, 0, 7, 0, 7, 0, 7, 0, 7 }, 
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },

		{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
		{ 14, 0, 14, 0, 14, 0, 14, 0, 14 },
		{ 0, 11, 0, 0, 0, 0, 0, 11, 0 }, 
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 9, 10, 13, 12, 8, 12, 13, 10, 9 }, 
	};
	
	public GameView(Context context) {
		super(context);
	}
	
	public GameView(Context context, MainActivity activity) {
		super(context);
		this.activity = activity;
		getHolder().addCallback(this);
		
		Display display = activity.getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		
		tutorialThread = new TutorialThread(getHolder(), this);
		timeThread = new TimeThread(this);
		
		this.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bacnground));
		initResources();
	}

	private void initResources() {
		paint = new Paint();
		
		chessboardBmp = BitmapFactory.decodeResource(getResources(), R.drawable.chessboard);
		chessmanBackgroundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.chessman);
		winBmp = BitmapFactory.decodeResource(getResources(), R.drawable.win);
		lostBmp = BitmapFactory.decodeResource(getResources(), R.drawable.lost);
		okBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ok);
		vsBmp = BitmapFactory.decodeResource(getResources(), R.drawable.vs);
		rightPointerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.right);
		leftPointerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.left);
		currentBmp = BitmapFactory.decodeResource(getResources(), R.drawable.current);
		exit2Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.exit2);
		sound2Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sound2);
		sound3Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sound3);
		timeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.time);
		redtimeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.redtime);
		
		blackChessman[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heishuai);	// ��˧
		blackChessman[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heiju);		// �ڳ�
		blackChessman[2] = BitmapFactory.decodeResource(getResources(), R.drawable.heima);		// ����
		blackChessman[3] = BitmapFactory.decodeResource(getResources(), R.drawable.heipao);		// ����
		blackChessman[4] = BitmapFactory.decodeResource(getResources(), R.drawable.heishi);		// ��ʿ
		blackChessman[5] = BitmapFactory.decodeResource(getResources(), R.drawable.heixiang);	// ����
		blackChessman[6] = BitmapFactory.decodeResource(getResources(), R.drawable.heibing);	// �ڱ�
		
		redChessman[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hongjiang);	// �콫
		redChessman[1] = BitmapFactory.decodeResource(getResources(), R.drawable.hongju);		// �쳵
		redChessman[2] = BitmapFactory.decodeResource(getResources(), R.drawable.hongma);		// ����
		redChessman[3] = BitmapFactory.decodeResource(getResources(), R.drawable.hongpao);		// ��h
		redChessman[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hongshi);		// ����
		redChessman[5] = BitmapFactory.decodeResource(getResources(), R.drawable.hongxiang);	// ����
		redChessman[6] = BitmapFactory.decodeResource(getResources(), R.drawable.hongzu);		// ����
		
		number[0] = BitmapFactory.decodeResource(getResources(), R.drawable.number0);			// ��ɫ����0
		number[1] = BitmapFactory.decodeResource(getResources(), R.drawable.number1);			// ��ɫ����1
		number[2] = BitmapFactory.decodeResource(getResources(), R.drawable.number2);			// ��ɫ����2
		number[3] = BitmapFactory.decodeResource(getResources(), R.drawable.number3);			// ��ɫ����3
		number[4] = BitmapFactory.decodeResource(getResources(), R.drawable.number4);			// ��ɫ����4
		number[5] = BitmapFactory.decodeResource(getResources(), R.drawable.number5);			// ��ɫ����5
		number[6] = BitmapFactory.decodeResource(getResources(), R.drawable.number6);			// ��ɫ����6
		number[7] = BitmapFactory.decodeResource(getResources(), R.drawable.number7);			// ��ɫ����7
		number[8] = BitmapFactory.decodeResource(getResources(), R.drawable.number8);			// ��ɫ����8
		number[9] = BitmapFactory.decodeResource(getResources(), R.drawable.number9);			// ��ɫ����9
		
		redNumber[0] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber0);		// ��ɫ����0
		redNumber[1] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber1);		// ��ɫ����1
		redNumber[2] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber2);		// ��ɫ����2
		redNumber[3] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber3);		// ��ɫ����3
		redNumber[4] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber4);		// ��ɫ����4
		redNumber[5] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber5);		// ��ɫ����5
		redNumber[6] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber6);		// ��ɫ����6
		redNumber[7] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber7);		// ��ɫ����7
		redNumber[8] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber8);		// ��ɫ����8
		redNumber[9] = BitmapFactory.decodeResource(getResources(), R.drawable.rednumber9);		// ��ɫ����9
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(chessboardBmp, 10, 10, null);
		
		for (int i = 0; i < chessman.length; i++) {
			for (int j = 0; j < chessman[i].length; j++) {
				if (chessman[i][j] != 0) {
					canvas.drawBitmap(chessmanBackgroundBmp, 9+j*34, 10+i*35, null);
					if(chessman[i][j] == 1){//Ϊ��˧ʱ
						canvas.drawBitmap(blackChessman[0], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 2){//Ϊ�ڳ�ʱ
						canvas.drawBitmap(blackChessman[1], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 3){//Ϊ����ʱ
						canvas.drawBitmap(blackChessman[2], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 4){//Ϊ����ʱ
						canvas.drawBitmap(blackChessman[3], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 5){//Ϊ��ʿʱ
						canvas.drawBitmap(blackChessman[4], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 6){//Ϊ����ʱ
						canvas.drawBitmap(blackChessman[5], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 7){//Ϊ�ڱ�ʱ
						canvas.drawBitmap(blackChessman[6], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 8){//Ϊ�콫ʱ
						canvas.drawBitmap(redChessman[0], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 9){//Ϊ�쳵ʱ
						canvas.drawBitmap(redChessman[1], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 10){//Ϊ����ʱ
						canvas.drawBitmap(redChessman[2], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 11){//Ϊ��hʱ
						canvas.drawBitmap(redChessman[3], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 12){//Ϊ����ʱ
						canvas.drawBitmap(redChessman[4], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 13){//Ϊ����ʱ
						canvas.drawBitmap(redChessman[5], 12+j*34, 13+i*35, paint);
					} else if(chessman[i][j] == 14){//Ϊ����ʱ
						canvas.drawBitmap(redChessman[6], 12+j*34, 13+i*35, paint);
					}
				}
			}
		}
		
		canvas.drawBitmap(vsBmp, 10, 360, paint);
		canvas.drawBitmap(timeBmp, 81, 411, paint);
		
		// ���ƺڷ���ʱ��
		int tmp = this.blackTime/60;
		String timeStr = tmp + "";
		if (timeStr.length() < 2) {
			timeStr = "0" + timeStr;
		}
		for (int i = 0; i< 2; i++) {
			int tmpScore = timeStr.charAt(i) - '0';
			canvas.drawBitmap(number[tmpScore], 65+i*7, 412, paint);
		}
		
		tmp = this.blackTime%60;
		timeStr = tmp + "";
		if (timeStr.length() < 2) {
			timeStr = "0" + timeStr;
		}
		for (int i = 0; i< 2; i++) {
			int tmpScore = timeStr.charAt(i) - '0';
			canvas.drawBitmap(number[tmpScore], 85+i*7, 412, paint);
		}
		
		// ��ʼ���ƺ췽ʱ��
		canvas.drawBitmap(redtimeBmp, 262, 410, paint);
		tmp = this.redTime/60;
		timeStr = tmp + "";
		if (timeStr.length() < 2) {
			timeStr = "0" + timeStr;
		}
		for (int i = 0; i< 2; i++) {
			int tmpScore = timeStr.charAt(i) - '0';
			canvas.drawBitmap(redNumber[tmpScore], 247+i*7, 411, paint);
		}
		
		tmp = this.blackTime%60;
		timeStr = tmp + "";
		if (timeStr.length() < 2) {
			timeStr = "0" + timeStr;
		}
		for (int i = 0; i< 2; i++) {
			int tmpScore = timeStr.charAt(i) - '0';
			canvas.drawBitmap(redNumber[tmpScore], 267+i*7, 411, paint);
		}
		
		// ����ָ������һ����ָ��
		if (caiPan == true) {
			canvas.drawBitmap(rightPointerBmp, 155, 420, paint);
		} else {
			canvas.drawBitmap(leftPointerBmp, 120, 420, paint);
		}

		canvas.drawBitmap(currentBmp, (width-currentBmp.getWidth())/2, (height-currentBmp.getHeight())/2+200, paint);
		canvas.drawBitmap(sound2Bmp, 10, getSound2Top(), paint);
		if (activity.isSound) {
			canvas.drawBitmap(sound3Bmp, 80, getSound2Top(), paint);
		}
		
		canvas.drawBitmap(exit2Bmp, (width-exit2Bmp.getWidth()-10), getExit2Top(), paint);
		
		if (status == 1) {
			canvas.drawBitmap(winBmp, (width-winBmp.getWidth())/2, (height-winBmp.getHeight())/2, paint);
			canvas.drawBitmap(okBmp, (width-okBmp.getWidth())/2, getOkBmpTop(winBmp), paint);
		}
		if (status == 2) {
			canvas.drawBitmap(lostBmp, (width-lostBmp.getWidth())/2, (height-lostBmp.getHeight())/2, paint);//����ʧ�ܽ���
			canvas.drawBitmap(okBmp, (width-okBmp.getWidth())/2, getOkBmpTop(lostBmp), paint);	
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight();
			
			if (event.getX()>10 && event.getX()<(sound2Bmp.getWidth()+10) 
					&& event.getY()>getSound2Top() && event.getY()<getSound2Top()+sound2Bmp.getHeight()) {
				activity.isSound = !activity.isSound;//����ȡ��
				if (activity.isSound && activity.gamesound != null && !activity.gamesound.isPlaying()) {
					activity.gamesound.start();
				} else if (!activity.isSound && activity.gamesound != null && activity.gamesound.isPlaying()) {
					activity.gamesound.pause();
				}
			}
			
			if (event.getX()>(width-exit2Bmp.getWidth()-10) && event.getX()<(width-10) 
					&& event.getY()>getExit2Top() 
					&& event.getY()<getExit2Top()+exit2Bmp.getHeight()) {
				activity.handler.sendEmptyMessage(1);
			}
		}
		
		if (status == 1) {
			if (event.getX()>(width-okBmp.getWidth())/2 && event.getX()<(width-okBmp.getWidth())/2+okBmp.getWidth() 
					&& event.getY()>getOkBmpTop(winBmp) 
					&& event.getY()<getOkBmpTop(winBmp)+okBmp.getHeight()) {
				activity.handler.sendEmptyMessage(1);
			}
		} else if (status == 2) {
			if (event.getX()>(width-okBmp.getWidth())/2 && event.getX()<(width-okBmp.getWidth())/2+okBmp.getWidth() 
					&& event.getY()>getOkBmpTop(lostBmp) 
					&& event.getY()<getOkBmpTop(lostBmp)+okBmp.getHeight()) {
				activity.handler.sendEmptyMessage(1);
			}
		} else {
			
		}
		
		return super.onTouchEvent(event);
	}

	private int getOkBmpTop(Bitmap bmp) {
		return (height-bmp.getHeight())/2+bmp.getHeight();
	}

	private int getSound2Top() {
		return (height-sound2Bmp.getHeight())/2+200;
	}

	private int getExit2Top() {
		return (height-exit2Bmp.getHeight())/2+200;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		tutorialThread.setFlag(true);
		tutorialThread.start();
		
		timeThread.setFlag(true);
		timeThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		tutorialThread.setFlag(false);
		timeThread.setFlag(false);
		
		while (retry) {
			try {
				tutorialThread.join();
				timeThread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	class TutorialThread extends Thread {
		private int span = 300; 		// ˯�ߵĺ�����
		private SurfaceHolder surfaceHolder;
		private GameView gameView;
		private boolean flag = false;	// ѭ����־λ

		public TutorialThread(SurfaceHolder surfaceHolder, GameView gameView) {// ������
			this.surfaceHolder = surfaceHolder;
			this.gameView = gameView;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}
		
		public void run() {
			Canvas canvas;
			while (flag) {
				canvas = null;
				try {
					canvas = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						gameView.draw(canvas);
					}
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				
				try {
					Thread.sleep(span);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

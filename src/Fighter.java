/*
 * 作成日: 2004/08/24
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */

/**
 * @author Administrator
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Fighter extends BaseObject{
	protected int nLeft;	// 残機数

	// キー入力のフラグ
	protected boolean bKeyLeft;
	protected boolean bKeyRight;
	protected boolean bKeyUp;
	protected boolean bKeyDown;
	protected int bKeyZ;

	public final static int KB_NONE	=	0;
	public final static int KB_TRIG	= 	1;
	public final static int KB_PUSH	= 	2;
	public final static int KB_PULL	=	3;

	private int numShot;	// 画面表示ショット量
	private int numValidShot; // 現在作れるショット数
	private int shotTimer = 0;

	private Shot shot[];

	// 内部クラス化された自機ショット管理クラス
	// ＊戦闘機はショット生成だけを受け持ち。移動はショット自身が行う
	class Shot extends BaseObject{

		// コンストラクタ
		public Shot()
		{
			super();
		}

		// 弾移動
		public void Move()
		{
			if(!this.isEnable) return;

			if(fY >= 0)
			{
				fX += fVX;
				fY += fVY;
			}
			else
				this.Enable(false);
		}

		// 弾表示
		public void Show(Graphics2D g2)
		{
			if(!this.isEnable) return;

			g2.setPaint(Color.white);
			g2.fill(new Ellipse2D.Double(fX - 10f, fY - 10f, 10f, 20f));

		}
	}

	public Fighter()
	{
		super();
		nLeft = 0;
		bKeyLeft = bKeyRight = bKeyUp = bKeyDown =  false;

		bKeyZ = KB_NONE;

		numShot = 6;
		numValidShot = 6;

		shot = new Shot[numShot];
		for(int i=0; i<numShot; i++)
		{
			shot[i] = new Shot();
		}
	}

	public Shot[] GetShot()
	{
		return shot;
	}

	public int GetNumShot()
	{
		return numShot;
	}


	// このへん一緒の関数にも出来るけど名前の付け方めんどくさいので
	public void Show(Graphics2D g2)
	{
		// ショット
		for(int i=0; i<numShot; i++)
		{
			shot[i].Show(g2);
		}

		// 自機
		if(!isEnable) return;

		try{
			BufferedImage image = ImageIO.read(
				new File("chiruno.png"));
			g2.drawImage(image,null,(int)fX,(int)fY);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	// 移動
	public void Move()
	{
		// ショット
		for(int i=0; i<numShot; i++)
		{
			shot[i].Move();
		}

		if(!isEnable) return;

		if(bKeyLeft)
		{
			if(fX >= 0)
	 			fX -= fVX;
		}
		else if(bKeyRight)
		{
			if(fX <= 600)
				fX += fVX;
		}

		if(bKeyUp)
		{
			if(fY >= - 30)
				fY -= fVY;
		}
		else if(bKeyDown)
		{
			if(fY <= 970)
				fY += fVY;
		}
	}


	// ショット生成
	public void Shoot()
	{
		if(!isEnable) return;

		// ボタンおしっぱじゃなくてボタン最初に押した時
		if(bKeyZ == KB_TRIG)
		{
			shotTimer = 0;
		}

		// ボタン押されとるかー？
		if(bKeyZ == KB_PUSH)
		{
			// 2回に一回作るよ
			if(shotTimer % 2 == 0)
			{
				if(numValidShot>=2)
				{
					// 2コ作りたいので
					for(int i=0; i<2; i++)
					{
						// まず作れるやつあるかどうか見るよ
						for(int j=0; j<numShot; j++)
						{
							if(shot[j].isEnable) continue;
							shot[j].SetVX(2-4*i);
							shot[j].SetVY(-40);
							shot[j].SetPos(Fighter.this.GetX() - 20 + 60*i, Fighter.this.GetY());
							shot[j].Enable(true);
							break;
						}
					}
				}
			}
			shotTimer++;
		}
	}


	// ボタン押してるとき
	public void KeyPressedAnalyze(KeyEvent e)
	{
		if(!isEnable) return;

		switch(e.getKeyCode())
		{
		case KeyEvent.VK_LEFT:
			bKeyLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
			bKeyRight = true;
			break;
		case KeyEvent.VK_UP:
			bKeyUp = true;
			break;
		case KeyEvent.VK_DOWN:
			bKeyDown = true;
			break;
		case KeyEvent.VK_Z:
			bKeyZ	= KB_PUSH;
			break;
		}
	}

	// ボタン離したとき
	public void KeyReleasedAnalyze(KeyEvent e)
	{
		if(!isEnable) return;

		switch(e.getKeyCode())
		{
		case KeyEvent.VK_LEFT:
			bKeyLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			bKeyRight = false;
			break;
		case KeyEvent.VK_UP:
			bKeyUp = false;
			break;
		case KeyEvent.VK_DOWN:
			bKeyDown = false;
			break;
		case KeyEvent.VK_Z:
			bKeyZ	= KB_PULL;
			break;
		}
	}

	// ボタン押した瞬間
	public void KeyTypedAnalyze(KeyEvent e)
	{
		if(!isEnable) return;

		switch(e.getKeyCode())
		{
		case KeyEvent.VK_Z:
			bKeyZ	= KB_TRIG;
			break;
		}
	}

}

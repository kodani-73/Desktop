/*
 * 作成日: 2004/08/29
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

public class Enemy extends BaseObject{
	private int	m_HP;
	private int m_Def;			// 防御力
	private int m_AppearTime;	// 出現時間
	private int m_bulletType;	// 弾タイプ
	private int m_bulletIntvl;	// 発射間隔
	private int m_bulletSpeed;	// 弾速度

	public final static int BL_1WAY_MON	=	0;
	public final static int BL_8WAY_ALL	= 	1;
	public final static int BL_3WAY_FAN	= 	2;
	public final static int KB_2WAY_DI	=	3;
	
	private EnemyManager _manager = null;
	
	public Enemy(EnemyManager em)
	{
		super();
		m_HP = 0;
		m_Def = 0;
		m_AppearTime = 0;
		m_bulletType = 0;
		m_bulletIntvl = 0;
		m_bulletSpeed = 0;
	
		_manager = em;
	}


	public int GetAppearTime()
	{
		return m_AppearTime;
	}

	public void SetHP(int hp)
	{
		m_HP = hp;
	}

	public void SetDEF(int def)
	{
		m_Def = def;
	}

	public void SetBulletType(int type)
	{
		m_bulletType = type;
	}

	public void SetAppearTime(int apptime)
	{
		m_AppearTime = apptime;
	}

	public void SetBulletIntvl(int interval)
	{
		m_bulletIntvl = interval;
	}

	public void SetBulletSpeed(int Speed)
	{
		m_bulletSpeed = Speed;
	}

	public void DecreaseHP()
	{
		m_HP -= (10 - m_Def);

		if(m_HP < 0)
			Enable(false);
	}

	// 弾生成
	public void Fire()
	{
		if(!isEnable) return;

		if(_manager.GetTime() % m_bulletIntvl == 0)
			CreateEimsBullet(3, 20);
	}

	public void Show(Graphics2D g2)
	{
		if(!isEnable) return;

		g2.setPaint(Color.white);
		g2.fill(new Ellipse2D.Double(fX, fY, 20f, 20f));
	}

	// 狙い弾を生成(拡張版)
	public void CreateEimsBullet(int n, float ang)
	{
		Fighter fighter = _manager.GetFighter();
		Bullet[] bullet = _manager.GetBullet();
		
		if(!fighter.IsEnable()) return;
		if(!isEnable) return;

		if(fY > 240) return;

		for(int i=0; i<EnemyManager.BULLET_MAX; i++)
		{
			if(bullet[i].isEnable) continue;

			float dx, dy, d, speed;

			dx = fighter.GetX() + 10 - fX;
			dy = fighter.GetY() + 10 - fY;

			speed = this.m_bulletSpeed;

			d = (float)Math.sqrt(dx*dx + dy*dy);

			for(int j = 0; i<EnemyManager.BULLET_MAX && j<n; i++, j++){
			bullet[i].SetPos(fX, fY);
			bullet[i].SetVX(((int)(dx*Math.cos(Math.PI*ang*j/180) - dy*Math.sin(Math.PI*ang*j/180))/d)*speed);
			bullet[i].SetVY(((int)(dx*Math.sin(Math.PI*ang*j/180) + dy*Math.cos(Math.PI*ang*j/180))/d)*speed);
			bullet[i].Enable(true);
			}

			break;
		}
	}
}

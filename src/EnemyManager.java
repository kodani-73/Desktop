import java.awt.Graphics2D;

public class EnemyManager {
	public final static int ENEMY_MAX	= 	30;
	public final static int BULLET_MAX		=	50;
	
	private Enemy[]	_enemy = new Enemy[ENEMY_MAX];
	public Enemy[] GetEnemy(){return _enemy;}
	private Bullet[] _bullet = new Bullet[BULLET_MAX];
	public Bullet[] GetBullet(){ return _bullet;}
	
	private Fighter _fighter = null;
	public Fighter GetFighter(){return _fighter;}
	
	private StageAnalyze _stage = null;
	public StageAnalyze GetStage(){return _stage;}
	
	private int _time = 0;
	public int GetTime(){return _time;}
	
	public EnemyManager(MainGameState main)
	{
		_fighter = main.GetFighter();
		_stage = main.GetStage();
		init();
	}
	
	public void init()
	{		
		for(int i=0;i<BULLET_MAX;i++)
		{
			_bullet[i] = new Bullet();
		}
	}

	public void update(int timer)
	{
		_time = timer;
		
		EnemyCreate();
		BulletCreate();
		
		EnemyMove();
		BulletMove();
	}
	
	public void Show(Graphics2D g2)
	{
		EnemyShow(g2);
		BulletShow(g2);
	}
	
	// 敵移動
	public void EnemyMove()
	{
		for(int i=0; i<ENEMY_MAX; i++)
		{
			if(_enemy[i] == null) return;

			if(!_enemy[i].IsEnable()) continue;

			_enemy[i].Move();

			if(((_enemy[i].GetX() >= 530)||(_enemy[i].GetX() <= -50))||((_enemy[i].GetY() >= 1050)||(_enemy[i].GetY() <= -50)))
				_enemy[i].Enable(false);
		}
	}

	// 弾移動
	public void BulletMove()
	{
		for(int i=0; i<BULLET_MAX; i++)
		{
			if(_bullet[i] == null) return;

			if(!_bullet[i].IsEnable()) continue;

			_bullet[i].Move();

			if(((_bullet[i].GetX() >= 620)||(_bullet[i].GetX() <= -50))||((_bullet[i].GetY() >= 1050)||(_bullet[i].GetY() <= -50)))
				_bullet[i].Enable(false);
		}
	}
 
	public void EnemyCreate()
	{
		// 
		for(int i=0; i<_stage.GetStringNumber(); i++)
		{
			if(Integer.parseInt(_stage.GetScenario().get(i)[0]) == _time)
			{
				for(int j=0; j<ENEMY_MAX; j++)
				{
					if(_enemy[j] == null)
					{
						_enemy[j] = _stage.GetTemporaryEnemy(this,i);
						_enemy[j].Enable(true);
						break;
					}	
					else if(_enemy[j].IsEnable()) continue;

					_enemy[j] = _stage.GetTemporaryEnemy(this,i);
					_enemy[j].Enable(true);
					break;
				}
			}
		}
	}

	// 敵ショット
	public void BulletCreate()
	{
		for(int i=0; i<ENEMY_MAX; i++)
		{
			if(_enemy[i] == null) return;
			if(!_enemy[i].IsEnable()) continue;

			_enemy[i].Fire();
		}
	}
	
	public void EnemyShow(Graphics2D g2)
	{
		for(int i=0; i<ENEMY_MAX; i++)
		{
			if(_enemy[i] == null) return;
			if(!_enemy[i].IsEnable()) continue;
			
			_enemy[i].Show(g2);
		}
	}

	public void BulletShow(Graphics2D g2)
	{
		for(int i=0; i<BULLET_MAX; i++)
		{
			if(_bullet[i] == null) return;
			if(!_bullet[i].IsEnable()) continue;
			
			_bullet[i].Show(g2);
		}
	}
	
	//
	public boolean HitCheck()
	{
		boolean rtn = false;
		
		HitCheckEnemyAndShot();
		rtn = HitCheckEnemyAndFighter() | HitCheckBulletAndFighter();
		
		return rtn;
	}

	// 敵と自機の判定
	private boolean HitCheckEnemyAndFighter()
	{
		if(!_fighter.IsEnable()) return false;

		for(int i=0; i<ENEMY_MAX; i++)
		{
			if(_enemy[i] == null || !_enemy[i].IsEnable()) continue;

			float dx, dy, width, height;
			
			dx = _enemy[i].GetX() - _fighter.GetX();
			dy = _enemy[i].GetY() - _fighter.GetY() - 23;

			width  = 30;
			height = 70;

			if((Math.abs(dx) <= width)&&(Math.abs(dy) <= height))
			{
				_enemy[i].DecreaseHP();
				_fighter.Enable(false);
				return true;
			}
		}
			
		return false;
	}

	// 敵と自弾の判定
	private void HitCheckEnemyAndShot()
	{
		for(int i=0;i<_fighter.GetNumShot();i++)
		{
			// 弾有効でなかったら次へ
			if(!_fighter.GetShot()[i].IsEnable()) continue;

			// 弾有効だったら敵全部と弾に関しての判定をする
			for(int j=0;j<ENEMY_MAX;j++)
			{
				// 画面内に敵居なかったら飛ばす
				if(_enemy[j] == null || !_enemy[j].IsEnable()) continue;
					float dx, dy, width, height;
				dx = _enemy[j].GetX() - _fighter.GetShot()[i].GetX() - 5;
				dy = _enemy[j].GetY() - _fighter.GetShot()[i].GetY();

				width = 50;
				height = 50;

				if((Math.abs(dx) <= width)&&(Math.abs(dy) <= height))
				{
					_enemy[j].DecreaseHP();
					_fighter.GetShot()[i].Enable(false);
					break;
				}
			}
		}
	}

	// 敵弾と自機の判定
	private boolean HitCheckBulletAndFighter()
	{
		if(!_fighter.IsEnable()) return false;

		for(int i=0; i<BULLET_MAX; i++)
		{
			if(_bullet[i] == null || !_bullet[i].IsEnable()) continue;
				float dx, dy, width, height;
			dx = _bullet[i].GetX() - _fighter.GetX();
			dy = _bullet[i].GetY() - _fighter.GetY() - 10;

			width = 20;
			height = 20;

			// 当たりました
			if((Math.abs(dx) <= width)&&(Math.abs(dy) <= height))
			{
				_bullet[i].Enable(false);
				_fighter.Enable(false);
				return true;
			}
		}		
		return false;
	}
}

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class MainGameState implements ModeState{

	private Fighter	_fighter;
	public Fighter GetFighter(){return _fighter;}
	
	// ステージデータ読み込み用
	private StageAnalyze _analyze;
	public StageAnalyze GetStage(){return _analyze;}

	// ゲーム内タイマー
	private int _gameTimer;
	public int GetTime(){return _gameTimer;}

	// 敵キャラの管理用
	private EnemyManager _emanager;
	
	public MainGameState()
	{
		init();
	}
	
	// 初期化用
	public void init()
	{		
		// 戦闘機を用意するよー
		_fighter = new Fighter();

		// 戦闘機のパラメータだよー
		// 何かのファイルから読み込んだりしてもいい
		_fighter.Enable(true);
		_fighter.SetPos(250, 500);
		_fighter.SetVX(16.0f);
		_fighter.SetVY(16.0f);

		// ステージデータだよー
		// ステージデータ読み込みは、ステージをstateパターンで実装してその中でやってもいい
		_analyze	= new StageAnalyze();
		_analyze.Analyze("stage1.txt");

		// 敵情報だよー
		_emanager = new EnemyManager(this);
		
		// プレイ中の時間経過
		_gameTimer = 0;
	}

	@Override
	public void Show(Graphics2D g2) {
		// TODO Auto-generated method stub

		// 自機と敵の表示
		_fighter.Show(g2);
		_emanager.Show(g2);
		
		// 時間の表示
		g2.setColor(Color.white);
		g2.drawString("経過時間:" + String.valueOf(_gameTimer), 10, 30);
	}

	@Override
	public void run(GameManager gm) {
		// 1000秒過ぎたら終わり 
		// ボスとか追加するときはここを無くしてやらないといけないかな
		if(_gameTimer == 1000) gm.ChangeMode(new ExitState());

		// 移動処理	
		_fighter.Move();
		
		// 自機ショット射出
		_fighter.Shoot();
		
		// 敵のアップデート
		_emanager.update(_gameTimer++);

		// あたり判定
		if(_emanager.HitCheck()){
			// 弾か敵に当たったら終了する
			gm.ChangeMode(new TitleState());
			//gm.ChangeMode(new TitleState());
		}	
	}
	
	@Override
	public void KeyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		_fighter.KeyPressedAnalyze(arg0);
	}

	@Override
	public void KeyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		_fighter.KeyReleasedAnalyze(arg0);		
	}

	@Override
	public void KeyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		_fighter.KeyTypedAnalyze(arg0);
	}
	

}

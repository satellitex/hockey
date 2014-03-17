package com.example.hockey;

import java.util.LinkedList;
import java.util.Scanner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class GameMgr {
	
	private FpsController fps = new FpsController();
	private LinkedList<Task> _backList = new LinkedList<Task>();//背景等
	private LinkedList<Task> _mainList = new LinkedList<Task>();//ゲーム中に使うタスク
	private Ready ready;//ゲーム前に前面に現れるタスク
	private GameResult result;//ゲーム結果を表示するタスク
	private Pack pack;
	private LinkedList<Mallet> _malletList = new LinkedList<Mallet>();//まれっとリスト
	private LinkedList<EnemyMallet> _enemyList = new LinkedList<EnemyMallet>(); 
	private Status _state;	//体力
	private Connect connect;
	
	private int pre_malletcount;//前回の生存マレットの数
	
	private boolean start_flag;//ゲームが始まっている時に立つ
	private boolean end_flag;//ゲームが終わった跡に立つ
	private boolean sinend_flag;//ゲームを終えていい時に立つ
	private boolean send_flag;//送る時に立つ
	private boolean reset_flag;//リセット時煮立てるフラグ
	private boolean the_end_flag;//完全終了フラグ
	private boolean tmp_theend;//上の手むぷ

	private Scanner input;
	private InputMgr _input;
	
	//マレットの追加更新に使うもの
	private Circle[] TmpMallet;	//一時保存するマレット
	private int malletup_flag;//0:追加更新無し,1:更新中,2更新後,3追加中
	private int mallet_state;//マレットの削除フラグのビット列
	private int tmpmallet_nums;
		
	public GameMgr(Connect c) {
		Log.d("koko","koko In GameMgr()");
		connect = c;
		start_flag=false;
		end_flag=false;
		the_end_flag=false;
		tmp_theend=false;

		//前回のマレットの数
		pre_malletcount = 0;

		//マレット添付
		TmpMallet = new Circle[5];
		
		fps = new FpsController();
		_state = new Status();
		
		
		//入力
		_input = new InputMgr(this);
		
		//パック
		pack = new Pack(this,_enemyList);

		//背景
		_backList.add( new GameBackground() );
		_backList.add( new Kline() );
		
		//メイン
        _mainList.add( new MalletPointFrame() );
        _mainList.add( new MalletPoint(this,_state) );
        
        //前面
        ready = new Ready();
        result = new GameResult();
	}
	
	public void StartRead(){ 
		connect.StartRead();
	}
	
	public void init(){
		start_flag=false;
		end_flag=false;
		sinend_flag=false;
		
		pre_malletcount = 0;
		
		ready.init();
		pack.init();
		result.init();
		_malletList.clear();
		for(int i=0;i<_mainList.size();i++){
			_mainList.get(i).init();
		}
	}
	
	//取得系
	public Ready getReady(){ return ready; }
	public Connect getConnect(){ return connect; }
	public Pack getPack(){ return pack; }
	public GameResult getResult(){ return result; }
	public boolean isTheEnd(){ return the_end_flag; }
	
	//スタート系
	public boolean isStart(){ return start_flag; }
	public void gameStart(){ start_flag = true; }

	//チェック系
	public boolean isEnd(){ return end_flag; }
	public boolean issinEnd(){ return sinend_flag; }
	
	//セット系
	public void gameEnd(){ end_flag = true;}
	public void setLose(){ result.setLose(); }
	public void setWin(){ result.setWin(); }
	public void setReset(){ reset_flag=true; }
	//セット系２	
	public void startMalletUpdate(){ malletup_flag=1; tmpmallet_nums=0; }
	public void endMalletupdate(){ if( malletup_flag == 1 ) malletup_flag=2; }
	public void setMalletState(int st){ mallet_state = st; }
	public int isMalletUpdate(){ return malletup_flag; }
	public void setSendStart(){ send_flag=true; }
	public void TheEnd(){ tmp_theend=true; }
    
	//マレットを追加
    public int AddMallet(Circle mallet){
    	int bitflag = 0;
    	for(int i=0;i<_malletList.size();i++){
    		if( GeneralCalc.CheckCircleInCircle(mallet,_malletList.get(i).getCircle()) ){
    			bitflag |= (1<<i);
    		}
    	}
    	
    	if( _malletList.size() < 5 && bitflag == 0 && _state.getHp() > 1f ){
    		_state.DecreaseHp(1f);
    		TmpMallet[tmpmallet_nums] = new Circle(mallet);
//    		_malletList.add( mallet );
    		bitflag |= (1<<(_malletList.size()+tmpmallet_nums)); 
    		tmpmallet_nums++;
    	}
    	return bitflag;
    }
    
    //フラグからマレット削除
    public void KillMallet(int malletflag){
    	for(int i=0;i<_malletList.size();i++){
    		if( (malletflag & (1<<i)) == 0 || _state.getHp() <= 0f){
    			_malletList.get(i).kill();
    		} else {
       			_malletList.get(i).revive();
    		}
    	}
    }
    
    //マレットの数取得
    public int CountMallet(){ return _malletList.size(); }
    public Mallet GetMallet(int id){
    	return _malletList.get(id);
    }
    
    public boolean onUpdate() {

    		Log.d("koko","koko In GameMgr.onUpdate()");
    		
    		Scanner tmps = null;
    		send_flag = false;
    		
    		//データを持ってくる
    		tmps = connect.FastCall();
    		if( connect.isServer() ){
    			input = tmps;
    		}
    		
    		//入力された情報を反映
    		if( input != null ){
    			_input.onUpdate(input);
    		}
    		
    		if( reset_flag ){
    			this.init();
    			reset_flag=false;
    		}
    		
    		//背面
    		fps.onUpdate();
            for(int i=0; i<_backList.size(); i++){
                    if(_backList.get(i).onUpdate() == false){ //更新失敗なら
                            _backList.remove(i);              //そのタスクを消す
                            i--;
                    }
            }

            if( tmp_theend ){
            	Log.d("koko","koko the End Ok");
            	the_end_flag = true;
            }
            
            if( the_end_flag ){
            	connect.startSend();
            	connect.sendString("5");
            } else if( end_flag ){//ゲーム終了後の結果
            	connect.startSend();
            	connect.sendString("3");
            	
            	if( result.isLose() ) connect.sendString("0");
            	else connect.sendString("1");
            	
            	if( result.onUpdate() ) {
            		sinend_flag = true;
            	}
            } else if( start_flag ){//ゲーム中の処理
            	
            	int survivalmalletnums = 0;
            	
            	
            	//マレットの追加処理
            	Log.d("koko","koko before add mallet");
            	while( malletup_flag == 1 );
            	Log.d("koko","koko before go mallet");
            	if( malletup_flag == 2 ){
            		malletup_flag = 3;
                	Log.d("koko","koko before in mallet "+tmpmallet_nums);
            		for(int i=0;i<tmpmallet_nums;i++){
            			Log.d("koko","koko TmpMalle "+i+" :: ( "+TmpMallet[i].getX()+" , "+TmpMallet[i].getY()+" )");
            			_malletList.add(new Mallet(TmpMallet[i].getX(),TmpMallet[i].getY()));
            		}
            		Log.d("koko","koko mallet_state : "+Integer.toBinaryString(mallet_state));
            		this.KillMallet(mallet_state);
            		Log.d("koko","koko mallet update end");
            	}
        		malletup_flag = 0;	//更新無し
            	
        		//自分のマレットを更新
	            for(int i=0; i<_malletList.size(); i++){
	                if( _malletList.get(i).getSurvival() ){
	                	survivalmalletnums |= (1<<i);
	                }
	                if(_malletList.get(i).onUpdate() == false){ //更新失敗なら
	                	Log.d("mallet","delete_mallet : "+ i );
	                    _malletList.remove(i);              //そのタスクを消す
	                    i--;
	                }
	            }
        		Log.d("koko","koko My mallet update end");
	            
	            //敵のマレットを更新
	            for(int i=0;i<_enemyList.size();i++){
	            	_enemyList.get(i).onUpdate();
	            }
        		Log.d("koko","koko Enemy mallet update end");
	            
	            pack.setMalletState(survivalmalletnums);
	            pack.onUpdate();
        		Log.d("koko","koko Pack update end");
        		
        		//CASE ４：
        		if( send_flag ){
		        	connect.startSend();
		    		connect.sendString("4");
		    		Vec v = pack.getVec();
		    		connect.sendString(String.format("%f %f %f %f",pack.getHx(),pack.getHy(),v.getX(),v.getY()));
		    		Log.d("koko","koko send end");
        		}
	            
	            for(int i=0; i<_mainList.size(); i++){
	                if(_mainList.get(i).onUpdate() == false){ //更新失敗なら
	                        _mainList.remove(i);              //そのタスクを消す
	                        i--;
	                }
	            }
	            
	            //前回の生存マレットの数を今回のものに上書き
	            pre_malletcount = survivalmalletnums;
	            
            } else {//ゲームスタート前の情報
            	connect.startSend();
            	connect.sendString("0");
	            //前面
	            if( ready.onUpdate() == false ){
	            	connect.sendString("4");
	            } else if( ready.isHa() ){
	            	connect.sendString("3");
	            } else if( ready.isYoi() ){
	            	connect.sendString("2");
	            } else if( ready.isOkOk() ){
	            	connect.sendString("1");
	            } else {
	            	connect.sendString("0");	            	
	            }
            }
            
            
            tmps = connect.LastCall();
    		if( connect.isClient() ){
    			input = tmps;
    		}
            return true;
    }

    public void onDraw(Canvas c) {
    		Log.d("koko","koko onDraw 0");
            c.drawColor(Color.WHITE);       //白で塗りつぶす
            //背面
            for(int i=0; i<_backList.size(); i++){
                    _backList.get(i).onDraw(c);//描画
            }
            //メイン
            if( end_flag ){//結果
            	//前面
            	result.onDraw(c);
            } else if( start_flag ){
	            for(int i=0; i<_malletList.size(); i++){
	                _malletList.get(i).onDraw(c);//描画
	            }
	            pack.onDraw(c);
	            for(int i=0; i<_mainList.size(); i++){
	                _mainList.get(i).onDraw(c);//描画
	            }
            }else {//スタート前
            	//前面
            	ready.onDraw(c);
            }
    		Log.d("koko","koko onDraw 1");
    }

}

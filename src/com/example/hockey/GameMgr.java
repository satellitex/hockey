package com.example.hockey;

import java.util.LinkedList;
import java.util.Scanner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class GameMgr {
	
	private Task Home;
	private FpsController fps = new FpsController();
	private LinkedList<Task> _backList = new LinkedList<Task>();//背景等
	private LinkedList<Task> _mainList = new LinkedList<Task>();//ゲーム中に使うタスク
	private Ready ready;//ゲーム前に前面に現れるタスク
	private Pack pack;
	private LinkedList<Mallet> _malletList = new LinkedList<Mallet>();//まれっとリスト
	private LinkedList<EnemyMallet> _enemyList = new LinkedList<EnemyMallet>(); 
	private Status _state;	//体力
	private Connect connect;
	
	private int pre_malletcount;//前回の生存マレットの数
	
	private boolean start_flag;//ゲームが始まっている時に立つ
	private boolean update_flag;//向こうから更新がきたら立つ
	private boolean send_flag;//送る時に立つ

	private Scanner input;
	private InputMgr _input;
	
	//マレットの追加更新に使うもの
	private Circle[] TmpMallet;	//一時保存するマレット
	private int malletup_flag;//0:追加更新無し,1:更新中,2更新後,3追加中
	private int tmpmallet_nums;
		
	public GameMgr(Connect c) {
		Log.d("koko","koko In GameMgr()");
		connect = c;
		start_flag=false;
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
		_backList.add( new Kline() );
		
		//メイン
        _mainList.add( new MalletPointFrame() );
        _mainList.add( new MalletPoint(this,_state) );
        
        //前面
        ready = new Ready();
	}
	
	public void StartRead(){ 
		connect.StartRead();
	}
	
	public Ready getReady(){ return ready; }
	public Connect getConnect(){ return connect; }
	
	public boolean isStart(){ return start_flag; }
	public void gameStart(){ start_flag = true; }
	
	public Pack getPack(){ return pack; }
	
	public void startMalletUpdate(){ malletup_flag=1; tmpmallet_nums=0; }
	public void endMalletupdate(){ if( malletup_flag == 1 ) malletup_flag=0; }
	public int isMalletUpdate(){ return malletup_flag; }
    
	//マレットを追加
    public int AddMallet(Circle mallet){
    	int bitflag = 0;
    	for(int i=0;i<_malletList.size();i++){
    		if( GeneralCalc.CheckCircleInCircle(mallet,_malletList.get(i).getCircle()) ){
    			bitflag |= (1<<i);
    		}
    	}
    	
    	if( _malletList.size() < 5 && bitflag == 0 && _state.getHp() > 1f ){
    		malletup_flag = 2;
    		_state.DecreaseHp(1f);
    		TmpMallet[tmpmallet_nums] = mallet;
//    		_malletList.add( mallet );
    		bitflag |= (1<<(_malletList.size()-1)); 
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
    		update_flag = false;
    		send_flag = false;
    		
    		//データを持ってくる
    		tmps = connect.FastCall();
    		if( connect.isServer() ){
    			input = tmps;
    		}
    		
    		//入力された情報を反映
    		if( input != null ){
    			_input.onUpdate(input);
    			update_flag = true;
    		}
    		
    		//背面
    		fps.onUpdate();
            for(int i=0; i<_backList.size(); i++){
                    if(_backList.get(i).onUpdate() == false){ //更新失敗なら
                            _backList.remove(i);              //そのタスクを消す
                            i--;
                    }
            }

            if( start_flag ){
            	
            	int survivalmalletnums = 0;
            	
            	
            	//マレットの追加処理
            	while( malletup_flag == 1 );
            	if( malletup_flag == 2 ){
            		malletup_flag = 3;
            		for(int i=0;i<tmpmallet_nums;i++){
            			_malletList.add(new Mallet(TmpMallet[i].getX(),TmpMallet[i].getY()));
            		}
            		send_flag = true;
            	}
        		malletup_flag = 0;	//更新無し
            	
        		//自分のマレットを更新
	            for(int i=0; i<_malletList.size(); i++){
	                if(_malletList.get(i).onUpdate() == false){ //更新失敗なら
	                	Log.d("mallet","delete_mallet : "+ i );
	                    _malletList.remove(i);              //そのタスクを消す
	                    i--;
	                }
	                if( _malletList.get(i).getSurvival() ){
	                	survivalmalletnums++;
	                }
	            }
	            if( survivalmalletnums != pre_malletcount ){
	            	send_flag = true;
	            }
	            
	            //敵のマレットを更新
	            for(int i=0;i<_enemyList.size();i++){
	            	_enemyList.get(i).onUpdate();
	            }

	            //クライアント側の送る処理
	            if( connect.isClient() ){
	            	connect.startSend();
            		connect.sendString("1");
            		connect.sendString(String.format("%d",survivalmalletnums) );
            		for(int i=0,j=0;i<survivalmalletnums;i++){
            			Mallet m;
            			while( true ){
            				m = _malletList.get(j);
            				if( m.getSurvival() ) break;
            				j++;
            			}
            			connect.sendString(String.format("%f %f %f",m.getHx(),m.getHy(),m.getCounter()));
            		}
            	}
	            
	            pack.onUpdate();
	            
	            //サーバー側の送る処理
            	if( connect.isServer() ){//サーバーなら識別子２
                	connect.startSend();
            		connect.sendString("2");

            		connect.sendString(String.format("%d",survivalmalletnums) );
            		for(int i=0,j=0;i<survivalmalletnums;i++){
            			Mallet m;
            			while( true ){
            				m = _malletList.get(j);
            				if( m.getSurvival() ) break;
            				j++;
            			}
            			connect.sendString(String.format("%f %f %f",m.getHx(),m.getHy(),m.getCounter()));
            		}
            		
            		Vec v = pack.getVec();
            		connect.sendString(String.format("%f %f %f",pack.getHx(),pack.getHy(),v.getX(),v.getY()));
            	}
            	
	            
	            
	            for(int i=0; i<_mainList.size(); i++){
	                if(_mainList.get(i).onUpdate() == false){ //更新失敗なら
	                        _mainList.remove(i);              //そのタスクを消す
	                        i--;
	                }
	            }
	            
	            //前回の生存マレットの数を今回のものに上書き
	            pre_malletcount = survivalmalletnums;
	            
            } else {
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
            if( start_flag ){
	            for(int i=0; i<_malletList.size(); i++){
	                _malletList.get(i).onDraw(c);//描画
	            }
	            pack.onDraw(c);
	            for(int i=0; i<_backList.size(); i++){
	                _mainList.get(i).onDraw(c);//描画
	            }
            } else {
            	//前面
            	ready.onDraw(c);
            }
    		Log.d("koko","koko onDraw 1");
    }

}

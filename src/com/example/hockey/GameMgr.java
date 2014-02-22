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
	private Status _state;	//体力
	private Connect connect;
	
	private boolean start_flag;

	private Scanner input;
	private InputMgr _input;
		
	public GameMgr(Connect c) {
		Log.d("koko","koko In GameMgr()");
		connect = c;
		start_flag=false;
		
		fps = new FpsController();
		_state = new Status();
		
		//入力
		_input = new InputMgr(this);
		
		//パック
		pack = new Pack(this);

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
	
	public boolean isStart(){ return start_flag; }
	public void gameStart(){ start_flag = true; }
    
	//マレットを追加
    public int AddMallet(Mallet mallet){
    	int bitflag = 0;
    	for(int i=0;i<_malletList.size();i++){
    		if( GeneralCalc.CheckCircleInCircle(mallet.getCircle(),_malletList.get(i).getCircle()) ){
    			bitflag |= (1<<i);
    		}
    	}
 
    	if( _malletList.size() < 5 && bitflag == 0 && _state.getHp() > 1f ){
    		_state.DecreaseHp(1f);
    		_malletList.add( mallet );
    		bitflag |= (1<<(_malletList.size()-1)); 
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
    		
    		
    		//データを持ってくる
    		tmps = connect.FastCall();
    		if( tmps != null ){
    			input = tmps;
    		}
    		
    		//入力された情報を反映
    		if( input != null ){
    			_input.onUpdate(input);
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
	            for(int i=0; i<_malletList.size(); i++){
	                if(_malletList.get(i).onUpdate() == false){ //更新失敗なら
	                	Log.d("mallet","delete_mallet : "+ i );
	                        _malletList.remove(i);              //そのタスクを消す
	                        i--;
	                }
	            }
	            
	            pack.onUpdate();
	            
	            for(int i=0; i<_mainList.size(); i++){
	                if(_mainList.get(i).onUpdate() == false){ //更新失敗なら
	                        _mainList.remove(i);              //そのタスクを消す
	                        i--;
	                }
	            }
            } else {
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
    		if( tmps != null ){
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

package com.example.hockey;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class GameMgr {

	private LinkedList<Task> _taskList = new LinkedList<Task>(); //タスクリスト
	private LinkedList<Mallet> _malletList = new LinkedList<Mallet>();//まれっとリスト
	private Status _state;	//体力
	
	public GameMgr() {
		_state = new Status();
        _taskList.add( new FpsController() );
        _taskList.add( new TouchPlayer(this) );
        _taskList.add( new Pack(this) );
        _taskList.add( new Kline() );
        _taskList.add( new MalletPointFrame() );
        _taskList.add( new MalletPoint(this,_state) );
	}
    
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
    
    public boolean onUpdate() {
            for(int i=0; i<_taskList.size(); i++){
                    if(_taskList.get(i).onUpdate() == false){ //更新失敗なら
                            _taskList.remove(i);              //そのタスクを消す
                            i--;
                    }
            }

            for(int i=0; i<_malletList.size(); i++){
                if(_malletList.get(i).onUpdate() == false){ //更新失敗なら
                	Log.d("mallet","delete_mallet : "+ i );
                        _malletList.remove(i);              //そのタスクを消す
                        i--;
                }
            }
            
            return true;
    }

    public void onDraw(Canvas c) {
            c.drawColor(Color.WHITE);       //白で塗りつぶす
            for(int i=0; i<_taskList.size(); i++){
                    _taskList.get(i).onDraw(c);//描画
            }
            for(int i=0; i<_malletList.size(); i++){
                _malletList.get(i).onDraw(c);//描画
        }
    }

}

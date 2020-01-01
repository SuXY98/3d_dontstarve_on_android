package com.example.a3d_dontstarve_on_android;

import android.content.Context;
import android.util.Log;

import com.example.a3d_dontstarve_on_android.Fruit.Fruit;
import com.example.a3d_dontstarve_on_android.Monster.Monster;
import com.example.a3d_dontstarve_on_android.Tree.Tree;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

enum Type{
    MONSTER,FRUIT,TREE
}

class objAttri{
    public Vector3f pos;
    public Type type;
    objAttri(Vector3f p,Type t){
        pos = p;
        type = t;
    }
}

public class ObjManager {
    Monster monster;
    Fruit fruit;
    Tree tree;
    public Vector<objAttri> objs;
    public ObjManager(Context context){
        monster = new Monster(context);
        fruit = new Fruit(context);
        tree = new Tree(context);
        objs = new Vector<objAttri>();
        for(int i = 0;i < 4;i++){
            for(int j = 0;j < 4;j++){
                float randx = (float)(Math.random() -0.5) * 450;
                float randz = (float)(Math.random() -0.5) * 450;
                while((randx < 30 && randx > -30 && randx < -180 && randx > 180)
                        && (randz < 30 && randz > -30 && randz < -180 && randz > 180)){
                    randx = (float)(Math.random() -0.5) * 450;
                    randz = (float)(Math.random() -0.5) * 450;
                }
                objAttri tmpFruit = new objAttri(new Vector3f(randx,0,randz),Type.MONSTER);
                objs.add(tmpFruit);
            }
        }
        for(int i = 0;i < 3;i++){
            for(int j = 0;j < 3;j++){
                float randx = (float)(Math.random() -0.5) * 450;
                float randz = (float)(Math.random() -0.5) * 450;
                while((randx < 30 && randx > -30 && randx < -180 && randx > 180)
                        && (randz < 30 && randz > -30 && randz < -180 && randz > 180)){
                    randx = (float)(Math.random() -0.5) * 450;
                    randz = (float)(Math.random() -0.5) * 450;
                }
                objAttri tmpFruit = new objAttri(new Vector3f(randx,0,randz),Type.TREE);
                objs.add(tmpFruit);
            }
        }
        for(int i = 0;i < 6;i++){
            for(int j = 0;j < 6;j++){
                float randx = (float)(Math.random() -0.5) * 450;
                float randz = (float)(Math.random() -0.5) * 450;
                while((randx < 30 && randx > -30 && randx < -180 && randx > 180)
                        && (randz < 30 && randz > -30 && randz < -180 && randz > 180)){
                    randx = (float)(Math.random() -0.5) * 450;
                    randz = (float)(Math.random() -0.5) * 450;
                }
                objAttri tmpFruit = new objAttri(new Vector3f(randx,0,randz),Type.FRUIT);
                objs.add(tmpFruit);
            }
        }
    }
    public void Draw(float[] VPMatrix,Vector3f campos){
        float WalkTime = ((GlobalTimer.getCurrentMS() / 10)% 360);
        float tmpwalk = (float)(Math.cos(Math.toRadians(WalkTime)));
        Vector3f walkvecter = new Vector3f(tmpwalk,0,tmpwalk).normalize().scale(0.1f);
        for(int i = 0;i < objs.size();i++){
            objAttri tmp = objs.elementAt(i);
            if(tmp.type == Type.MONSTER){
                if(campos.distance(tmp.pos) > 100){
                    continue;
                }
                if(campos.distance(tmp.pos) < 10){
                    //范围检测
                    //System.out.print("obj manager" + "dis tance" + campos.distance(tmp.pos)+"\n");
                    tmp.pos = tmp.pos.plus(campos.minus(tmp.pos).normalize().scale(0.1f));
                    objs.set(i,tmp);
                }else{
                    //随机走动
                    tmp.pos = tmp.pos.plus(walkvecter);
                    objs.set(i,tmp);
                }
                monster.draw(VPMatrix,tmp.pos);
            }else if(tmp.type == Type.FRUIT){
                if(campos.distance(tmp.pos) > 100){
                    continue;
                }
                fruit.draw(VPMatrix,tmp.pos);
            }else {
                if(campos.distance(tmp.pos) > 100){
                    continue;
                }
                tree.draw(VPMatrix,tmp.pos);
            }
        }
    }

    public Vector<objAttri> GetObjsAttri(){
        return objs;
    }

    public void addFruit(){
        float randx = (float)(Math.random() -0.5) * 450;
        float randz = (float)(Math.random() -0.5) * 450;
        while((randx < 30 && randx > -30 && randx < -180 && randx > 180)
                && (randz < 30 && randz > -30 && randz < -180 && randz > 180)){
            randx = (float)(Math.random() -0.5) * 450;
            randz = (float)(Math.random() -0.5) * 450;
        }
        objAttri tmpFruit = new objAttri(new Vector3f(randx,0,randz),Type.FRUIT);
        objs.add(tmpFruit);
    }

    public void deleteObj(int index){
        objs.remove(index);
    }
}

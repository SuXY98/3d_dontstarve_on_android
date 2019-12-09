package com.example.a3d_dontstarve_on_android.World.Objs;

import com.example.a3d_dontstarve_on_android.Vector2f;
import com.example.a3d_dontstarve_on_android.Vector3f;

import java.util.Vector;

public class CubeModel extends BaseModel {
    public CubeModel(boolean hasTexture){
        super(hasTexture);

        this.normals = new Vector<Vector3f>(){{
            add(new Vector3f(1, 0, 0));
            add(new Vector3f(-1, 0, 0));
            add(new Vector3f(0, 1, 0));
            add(new Vector3f(0, -1, 0));
            add(new Vector3f(0, 0, 1));
            add(new Vector3f(0, 0, -1));
        }};

        this.points = new Vector<Vector3f>(){{
            add(new Vector3f(0, 0, 0));
            add(new Vector3f(1, 0, 0));
            add(new Vector3f(0, 1, 0));
            add(new Vector3f(1, 1, 0));
            add(new Vector3f(0, 0, 1));
            add(new Vector3f(1, 0, 1));
            add(new Vector3f(0, 1, 1));
            add(new Vector3f(1, 1, 1));
        }};


        this.texture = new Vector<Vector2f>(){{
            add(new Vector2f(0, 0));
            add(new Vector2f(1, 0));
            add(new Vector2f(0, 1));
            add(new Vector2f(1, 1));
        }};


        this.planes = new Vector<RenderPoints []>(){{
            add(new RenderPoints[]{ new RenderPoints(0, 3,1),
                                    new RenderPoints(4,3,3),
                                    new RenderPoints(5,3,2)}) ;
            add(new RenderPoints[]{ new RenderPoints(0, 3,1),
                                    new RenderPoints(5,3,2),
                                    new RenderPoints(1,3,0)}) ;

            add(new RenderPoints[]{ new RenderPoints(0, 5,3),
                                    new RenderPoints(2,5,2),
                                    new RenderPoints(1,5,1)}) ;
            add(new RenderPoints[]{ new RenderPoints(1, 5,1),
                                    new RenderPoints(2,5,2),
                                    new RenderPoints(3,5,0)}) ;

            add(new RenderPoints[]{ new RenderPoints(1, 0,1),
                                    new RenderPoints(5,0,3),
                                    new RenderPoints(7,0,2)}) ;
            add(new RenderPoints[]{ new RenderPoints(1, 0,1),
                                    new RenderPoints(7,0,2),
                                    new RenderPoints(3,0,0)}) ;

            add(new RenderPoints[]{ new RenderPoints(5, 4,1),
                                    new RenderPoints(4,4,3),
                                    new RenderPoints(6,4,2)}) ;
            add(new RenderPoints[]{ new RenderPoints(5, 4,1),
                                    new RenderPoints(6,4,2),
                                    new RenderPoints(7,4,0)}) ;

            add(new RenderPoints[]{ new RenderPoints(3, 2,1),
                                    new RenderPoints(7,2,3),
                                    new RenderPoints(6,2,2)}) ;
            add(new RenderPoints[]{ new RenderPoints(3, 2,1),
                                    new RenderPoints(6,2,2),
                                    new RenderPoints(2,2,0)}) ;

            add(new RenderPoints[]{ new RenderPoints(2, 1,1),
                                    new RenderPoints(6,1,3),
                                    new RenderPoints(4,1,2)}) ;
            add(new RenderPoints[]{ new RenderPoints(2, 1,1),
                                    new RenderPoints(4,1,2),
                                    new RenderPoints(0,1,0)}) ;
        }};

    }
}

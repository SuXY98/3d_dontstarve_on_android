package com.example.a3d_dontstarve_on_android.World.Objs;

import com.example.a3d_dontstarve_on_android.Vector2f;
import com.example.a3d_dontstarve_on_android.Vector3f;

public class BallModel extends BaseModel {

    private float resolution;

    public BallModel(float angleStep){
        super();
        this.resolution = angleStep;
        setBallData();
    }

    private void setBallData(){
        int count = 0;
        for(float i=-90;i<90+resolution;i+=resolution){
            float [] r = new float[]{(float)Math.cos(i * Math.PI / 180.0), (float)Math.cos((i + resolution) * Math.PI / 180.0)};
            float [] h = new float []{(float)Math.sin(i * Math.PI / 180.0), (float)Math.sin((i + resolution) * Math.PI / 180.0)};
            for (float j = 0.0f; j <360.0f+resolution; j +=resolution ) {
                float [] cos = new float []{(float) Math.cos(j * Math.PI / 180.0), (float) Math.cos((j+resolution) * Math.PI / 180.0)};
                float [] sin = new float []{-(float) Math.sin(j * Math.PI / 180.0), -(float) Math.sin((j+resolution) * Math.PI / 180.0)};

                points.add(new Vector3f(r[0] * sin[0], r[0] * cos[0],h[0] ));
                normals.add(new Vector3f(r[0] * sin[0], r[0] * cos[0],h[0] ));
                texture.add(new Vector2f(r[0] * sin[0], r[0] * cos[0]));

                points.add(new Vector3f(r[0] * sin[1], r[0] * cos[1],h[0] ));
                normals.add(new Vector3f(r[0] * sin[1], r[0] * cos[1],h[0] ));
                texture.add(new Vector2f(r[0] * sin[1], r[0] * cos[1]));

                points.add(new Vector3f(r[1] * sin[0], r[1] * cos[0],h[1] ));
                normals.add(new Vector3f(r[1] * sin[0], r[1] * cos[0],h[1] ));
                texture.add(new Vector2f(r[1] * sin[0], r[1] * cos[0]));

                planes.add(new RenderPoints[]{
                        new RenderPoints(count, count, count),
                        new RenderPoints(count + 1, count + 1, count + 1),
                        new RenderPoints(count + 2, count + 2, count + 2)
                });
                count += 3;

                points.add(new Vector3f(r[1] * sin[0], r[1] * cos[0],h[1] ));
                normals.add(new Vector3f(r[1] * sin[0], r[1] * cos[0],h[1] ));
                texture.add(new Vector2f(r[1] * sin[0], r[1] * cos[0]));

                points.add(new Vector3f(r[0] * sin[1], r[0] * cos[1],h[0] ));
                normals.add(new Vector3f(r[0] * sin[1], r[0] * cos[1],h[0] ));
                texture.add(new Vector2f(r[0] * sin[1], r[0] * cos[1]));

                points.add(new Vector3f(r[1] * sin[1], r[1] * cos[1], h[1] ));
                normals.add(new Vector3f(r[1] * sin[1], r[1] * cos[1], h[1] ));
                texture.add(new Vector2f(r[1] * sin[1], r[1] * cos[1]));

                planes.add(new RenderPoints[]{
                        new RenderPoints(count, count, count),
                        new RenderPoints(count + 1, count + 1, count + 1),
                        new RenderPoints(count + 2, count + 2, count + 2)
                });
                count += 3;
            }
        }
    }
}

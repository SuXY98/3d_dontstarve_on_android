# Instruction of Model
when a model is created, before being pushed into world vector, it must finish initialization works,that is;
+ fill the vertex, plane, Ks, Ka, Kd, normal data(may use methods extended by yourself)
+ if has texture
    + fill texture content(load pictures and set parameters)
    + set texture coordinates manually, or offer texture coordinate generation matrix

+ then add it into world
+ if you want to draw an object, create an object which refers the model (with model index in the vector),
then add to object vector
+ any models in the world won't be deleted, any object can be removed.

## feature can modified after push into world
Totally, any public method from BaseModel can called, so that these params can be modified.
+ Ka, Ks, Kd for model
+ open/close texture, simply modify openTexture attribute.
+ Texture parameters(not implement yet)

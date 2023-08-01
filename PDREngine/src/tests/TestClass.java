package tests;

import engine.MainEngine;
import entity.Entity;
import exceptions.GeneralException;
import property.Property;
import property.PropertyForEntity;
import property.Value;
import range.Range;
import termination.Termination;
import world.World;

public class TestClass {
    public static void main(String[] args) {
    }

    public static void createWorld(MainEngine engine){
        try{
            World testWorld = new World(new Termination(480, 10));
            Property env1 = new Property("e1", "decimal", new Range(10, 100));
            Property env2 = new Property("e2", "boolean");
            Property env3 = new Property("e3", "float", new Range(10.4f, 100.2f));
            Property env4 = new Property("e4", "string");
            testWorld.addEnviroment(env1);
            testWorld.addEnviroment(env2);
            testWorld.addEnviroment(env3);
            testWorld.addEnviroment(env4);
            Entity entity1 = new Entity("ent-1", 100);
            entity1.addProperty(new PropertyForEntity("p1","decimal",new Range(0,100),new Value(false,0)));
            entity1.addProperty(new PropertyForEntity("p2","float",new Range(0,100),new Value(true)));
            entity1.addProperty(new PropertyForEntity("p3","boolean",new Value(true)));
            entity1.addProperty(new PropertyForEntity("p4","string",new Range(0,100),new Value(false,"example")));
            testWorld.addEntity(entity1);
            engine.addWorld(testWorld);
//            System.out.println(engine.printCurrentWorld());
//
//            System.out.println();
//
//             move test world to old sim and put test world 1
//
//            engine.moveWorld();

//            World testWorld1 = new World(new Termination(580, 10));
//            Property env5 = new Property("e1", "decimal", new Range(10,100));
//            Property env6 = new Property("e2", "boolean");
//            Property env7 = new Property("e3", "float", new Range(10.2f, 10.4f));
//            Property env8 = new Property("e4", "string");
//            testWorld1.addEnviroment(env5);
//            testWorld1.addEnviroment(env6);
//            testWorld1.addEnviroment(env7);
//            testWorld1.addEnviroment(env8);
//            Entity ent5 = new Entity("ent-5" ,100);
//            ent5.addProperty(new PropertyForEntity("p5","decimal",new Range(0,100),new Value(false,1)));
//            ent5.addProperty(new PropertyForEntity("p6","float",new Range(1,100),new Value(true)));
//            ent5.addProperty(new PropertyForEntity("p7","boolean",new Value(true)));
//            ent5.addProperty(new PropertyForEntity("p8","string",new Range(0,100),new Value(false,"example")));
//            testWorld1.addEntity(ent5);
//            engine.addWorld(testWorld1);
//            System.out.println(engine.printCurrentWorld());
//            engine.moveWorld();
//
//
//            System.out.println(engine.getOldSimulationsInfo());
        }
        catch(GeneralException e){
            System.out.println(e.getErrorMsg());
        }

        /*
        *
        * */

    }
}



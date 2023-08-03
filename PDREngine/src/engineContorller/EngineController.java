package engineContorller;

import dto.DtoOldSimulationResponse;
import dto.DtoResponse;
import engine.MainEngine;
import interfaces.InterfaceMenu;
import world.World;

public class EngineController implements InterfaceMenu{

        private MainEngine engine;

        public EngineController() {
            this.engine = new MainEngine();
        }


    @Override
    public void createWorld() {

    }

    @Override
    public String showCurrentSimulation() {
        return null;
    }

    @Override
    public void RunSingleSimulation() {

    }

    @Override
    public String printPastSimulation(int indexOfSimulation, int userDisplay) {
            if (userDisplay == 1)
            {
                return engine.getSimulationsByQuantity(indexOfSimulation);
            }

            if (userDisplay == 2)
            {

            }
            return null;
    }

    public DtoOldSimulationResponse getAllOldSimulationsInfo()
    {
        return engine.getOldSimulationsInfo();
    }

    public DtoResponse checkXmlFileValidation(String xmlPath){

        DtoResponse response = engine.parseXmlToSimulation(xmlPath);
        World world = response.getCurrentWorldSimulation();
        if(world != null)
        {
            engine.addWorld(world);
        }
        return response;
    }
}

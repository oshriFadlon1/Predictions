package engine;

import dto.DtoResponse;
import world.World;
import exceptions.GeneralException;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainEngine {
    private List<World> allSimulations; // current running simulation

    private List<World> oldSimulation; // old simulation with results

    private Map<Integer, String> simulationId2CurrentTimeAndDate;// get old World simulation info need to take index from map - 1;


    public MainEngine() {
        this.allSimulations = new ArrayList<>();
        this.oldSimulation = new ArrayList<>();
        this.simulationId2CurrentTimeAndDate = new HashMap<>();
    }

    public List<World> getAllSimulations() {
        return allSimulations;
    }

    public void setAllSimulations(List<World> allSimulations) {
        this.allSimulations = allSimulations;
    }

    public List<World> getOldSimulation() {
        return oldSimulation;
    }

    public void setOldSimulation(List<World> oldSimulation) {
        this.oldSimulation = oldSimulation;
    }

    public Map<Integer, String> getSimulationId2CurrentTimeAndDate() {
        return simulationId2CurrentTimeAndDate;
    }

    public void setSimulationId2CurrentTimeAndDate(Map<Integer, String> simulationId2CurrentTimeAndDate) {
        this.simulationId2CurrentTimeAndDate = simulationId2CurrentTimeAndDate;
    }

    private void RunSingleSimulation(int simulationId) {

    }

    public void addWorld(World juiceWrld)
    {
        this.allSimulations.add(juiceWrld);
    }

    public String  printCurrentWorld(){
        if (!allSimulations.isEmpty())
        {
            return allSimulations.get(0).toString();
        }
        return null;
    }
    public void moveWorld() {
        oldSimulation.add(this.allSimulations.remove(0));
        simulationId2CurrentTimeAndDate.put(oldSimulation.size(), getCurrentTimeAndDateInTheFormat());
    }

    private String getCurrentTimeAndDateInTheFormat() {
        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");

        return sdf.format(currentDate);
    }

    public String getOldSimulationsInfo(){
        StringBuilder res = new StringBuilder();
        for(Integer key: simulationId2CurrentTimeAndDate.keySet()){
            res.append(key).append(": ");
            String value = simulationId2CurrentTimeAndDate.get(key);
            res.append(value);
            res.append("\n");
        }

        return res.toString();
    }

    public DtoResponse parseXmlToSimulation(String xmlPath) {
//
//        try {
//            call function tryToReadXml(xmlPath)
        // succed to load xml info create DtoResponse (null, Constans.SUCCEED_LOAD_FILE);
//        } catch (generalException e) {
//            return new DtoResponse(null, e.exceptionMessage)
//        }
        return null;
    }
    public void tryToReadXml(String xmlPath) throws GeneralException{
        //here we will try to read the xml.
    }

}

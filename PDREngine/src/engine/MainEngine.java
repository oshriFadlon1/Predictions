package engine;

import dto.DtoResponse;
import world.World;
import exceptions.GeneralException;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainEngine {
    private List<World> allSimulations; // current running simulation

    private List<World> oldSimulation; // old simulation with results

    private Map<Integer, String> simulationId2CurrentTimeAndDate;


    public MainEngine() {
        this.allSimulations = new ArrayList<>();
        this.oldSimulation = new ArrayList<>();
        this.simulationId2CurrentTimeAndDate = new HashMap<>();
    }

    private void RunSingleSimulation(int simulationId) {

    }

    private String getCurrentTimeAndDateInTheFormat() {
        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");

        return sdf.format(currentDate);
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

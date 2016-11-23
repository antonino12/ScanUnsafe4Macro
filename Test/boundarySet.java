// STAR-CCM+ macro: boundarySet.java
// Written by STAR-CCM+ 11.02.009
package macro;

import java.util.*;
import star.common.*;
import star.base.neo.*;
import star.energy.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.openide.util.Exceptions;


public class boundarySet extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

      //System.err.println("234234");

    FileTable fileTable_0 = 
      (FileTable) simulation_0.getTableManager().createFromFile(resolvePath("HTC_Table.csv")); //Read in the HTC table (currently assumes table is in same directory as macro/sim file. User full directory if needed)

    //Here, we use a Buffered Reader to scan the table and split it using commas.
    String csvFile = simulation_0.getSessionDir() + File.separator + "Part_List.csv"; //Set table for parts list. Again, assumes it is in the current working directory.
    String line = ",|\\n"; //Defines what constitues the end of a line, in this case, a comma or the end of a line.
    String csvSplit = ","; //Defines how to split the CSV file. In this case, split using commas.

    //Load table
    try (ByteArrayInputStream br = new ByteArrayInputStream(new FileReader(csvFile))) {

    	//Loop through table
        while ((line = br.readLine()) != null) {

            //Use comma as separator to split
            String current_region[] = line.split(csvSplit); //Set the current region based on string from csv table.

            Region region_0 = 
      			simulation_0.getRegionManager().getRegion(current_region[0]); //Define region to operate on based on current_region

		    Iterator<Boundary> itBoundary = region_0.getBoundaryManager().getObjects().iterator(); //Setup iterator to iterator over all boundaries in the region

			//Loop over boundaries
            while (itBoundary.hasNext()) {

	            Boundary boundary_0 = itBoundary.next(); //Query Current Boundary Name

	            simulation_0.println("Assigning HTC values to Boundary: " + boundary_0);

				boundary_0.getConditions().get(WallThermalOption.class).setSelected(WallThermalOption.Type.CONVECTION);//Set type to convection

				AmbientTemperatureProfile ambientTemperatureProfile_0 = 
				    boundary_0.getValues().get(AmbientTemperatureProfile.class);

				ambientTemperatureProfile_0.setMethod(XyzTabularScalarProfileMethod.class);

				ambientTemperatureProfile_0.getMethod(XyzTabularScalarProfileMethod.class).setTable(fileTable_0);

				ambientTemperatureProfile_0.getMethod(XyzTabularScalarProfileMethod.class).setData("Temp"); //Set ambient temperature to "Temp" column in table

				HeatTransferCoefficientProfile heatTransferCoefficientProfile_0 = 
					boundary_0.getValues().get(HeatTransferCoefficientProfile.class);

				heatTransferCoefficientProfile_0.setMethod(XyzTabularScalarProfileMethod.class);

				heatTransferCoefficientProfile_0.getMethod(XyzTabularScalarProfileMethod.class).setTable(fileTable_0);

				heatTransferCoefficientProfile_0.getMethod(XyzTabularScalarProfileMethod.class).setData("HTC"); //Set HTC to "HTC" column in table

            }
        }

    }

    catch (IOException e) {
      e.printStackTrace();
    }
  }
}

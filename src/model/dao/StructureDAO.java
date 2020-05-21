package model.dao;

import model.mo.Structure;
import java.util.ArrayList;

public interface StructureDAO {
    /**
     * For this web application, it was thought that the structure to be managed is unique therefore it will always be
     * present in the STRUCTURE table only one single structure, therefore ONE SINGLE UNIQUE ROW.
     */

    Structure fetchStructure();
}
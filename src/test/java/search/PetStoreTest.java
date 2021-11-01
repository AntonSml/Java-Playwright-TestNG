package search;

import base.BaseTests;
import org.testng.annotations.Test;


public class PetStoreTest extends BaseTests {

    @Test(priority = 1)
    public void addNewPetTest() throws InterruptedException {
        mainPage.addTestData()
                .checkId();
    }

    @Test(priority = 2)
    public void petDataUpdate() {
        mainPage.updatePetData();
    }

    @Test(priority = 3)
    public void checkPetUpdatedData() {
        mainPage.checkUpdatedPetData();
    }

    @Test(priority = 4)
    public void deletePetData() {
        mainPage.deletePetDataFromStore();
    }

    @Test(priority = 5)
    public void deleteNonExistentPet() {
        mainPage.deleteNonExistentPetData();
    }

    @Test(priority = 6)
    public void checkInvalidPID() {
        mainPage.checkInvalidPetsID();
    }
}

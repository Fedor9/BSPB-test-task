import helper.PetshopApiHelper;
import io.restassured.path.json.JsonPath;
import model.Pet;
import model.PetCategory;
import model.PetStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

import static helper.PetshopApiHelper.getPetsByStatus;

public class TestPetshop {

    @Test
    public void createPet() {
        //create pet request
        Pet pet = new Pet(0, new PetCategory(0, "category"), "doggo", Collections.emptyList(), Collections.emptyList(), PetStatus.available);
        JsonPath createPetResponse = PetshopApiHelper.addPet(pet);
        //get id of pet
        String petId = createPetResponse.getString("id");

        //check pet in list
        JsonPath allPets = getPetsByStatus(PetStatus.available, PetStatus.pending, PetStatus.sold);
        Assert.assertTrue(allPets.getList("id", String.class).contains(petId));
    }

    @Test
    public void deletePet() {
        //Get first pet from available list
        JsonPath allPets = getPetsByStatus(PetStatus.available, PetStatus.pending, PetStatus.sold);
        String petId = allPets.getString("[0].id");

        //delete pet
        PetshopApiHelper.deletePet(petId);

        //check that pet removed from list
        allPets = getPetsByStatus(PetStatus.available, PetStatus.pending, PetStatus.sold);
        Assert.assertFalse(allPets.getList("id", String.class).contains(petId));
    }
}

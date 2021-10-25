package pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import pojo.Category;
import pojo.Pet;
import pojo.Tag;

import java.util.Arrays;
import java.util.Properties;

import static com.microsoft.playwright.Page.WaitForSelectorOptions.State.ATTACHED;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.get;
import static org.testng.Assert.assertEquals;
import static org.hamcrest.Matchers.*;

public class MainPage {

    private Page page;
    private Pet pet;
    private Properties properties;
    private int id;

    private String ADD_NEW_PET_FIELD = "//*[@id='operations-pet-addPet']";
    private String TRY_IT_OUT_BUTTON = "//button[@class='btn try-out__btn']";
    private String EXECUTE_BUTTON = "//button[@class='btn execute opblock-control__btn']";
    private String TEST_PARAMETERS = "//textarea[@class='body-param__text']";


    public MainPage(Page page) {
        this.page = page;
        pet = new Pet();
        properties = new Properties();
        id = Integer.parseInt(RandomStringUtils.randomNumeric(5));
    }

    @Step("Open 'Add new pet to the store' menu")
    public MainPage openMenu() {
        page.click(ADD_NEW_PET_FIELD);
        page.focus(ADD_NEW_PET_FIELD);
        return this;
    }

    @Step("Click 'Try it out' button")
    public MainPage clickTryItOutButton() {
        page.click(TRY_IT_OUT_BUTTON);
        return this;
    }

    @Step("Click 'Execute' button")
    public MainPage clickExecuteButton() {
        page.click(EXECUTE_BUTTON);
        page.focus(EXECUTE_BUTTON);
        return this;
    }

    @Step("Click on test parameters area")
    public MainPage clearOnTestParametersArea() {
        page.click(TEST_PARAMETERS);
        page.fill(TEST_PARAMETERS,"");
        return this;
    }

    @Step("Check that 'Add new pet to the store' element is displayed")
    public MainPage checkAddNewPetElement() {
        var expectedState = new Page.WaitForSelectorOptions().withState(ATTACHED);
        page.waitForSelector(ADD_NEW_PET_FIELD, expectedState);
        return this;
    }

    @Step("Check that 'Try it out' button is displayed")
    public MainPage checkTryItOutButton() {
        var expectedState = new Page.WaitForSelectorOptions().withState(ATTACHED);
        page.waitForSelector(TRY_IT_OUT_BUTTON, expectedState);
        return this;
    }

    @Step("Check that test parameters area is displayed")
    public MainPage checkTestParametersArea() {
        var expectedState = new Page.WaitForSelectorOptions().withState(ATTACHED);
        page.waitForSelector(TEST_PARAMETERS, expectedState);
        return this;
    }

    public MainPage checkId() {
        checkNewIDExist();
        return this;
    }


    public MainPage addTestData() {
        checkAddNewPetElement();
        openMenu();
        checkTryItOutButton();
        clickTryItOutButton();
        checkTestParametersArea();
        clearOnTestParametersArea();
        setNewPetCorrectData();
        clickExecuteButton();
        return this;
    }

    @Step("Set correct data")
    public MainPage setNewPetCorrectData() {
        Category category = new Category();
        category.setId(id);
        category.setName("Guard category");

        Tag tag = new Tag();
        tag.setId(id);
        tag.setName("Guard");

        pet.setId(id);
        pet.setName("Jack");
        pet.setStatus("available");
        pet.setCategory(category);
        pet.setTags(Arrays.asList(tag));
        pet.setPhotoUrls(Arrays.asList("https://placedog.net/640/480?random"));

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(pet);

        page.fill(TEST_PARAMETERS, json);
        return this;
    }

    @Step("Check that new pet ID created")
    public MainPage checkNewIDExist() {
        get(baseURI + "/pet/" + pet.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .log()
                .body()
                .body("id", equalTo(pet.getId()))
                .body("name", equalToCompressingWhiteSpace(pet.getName()))
                .body("status", equalToCompressingWhiteSpace(pet.getStatus()))
                .body("category.id",equalTo(pet.getCategory().getId()))
                .body("category.name",equalTo(pet.getCategory().getName()))
                .body("tags[0].id",equalTo(pet.getTags().get(0).getId()))
                .body("tags[0].name",equalTo(pet.getTags().get(0).getName()))
                .body("photoUrls[0]", containsStringIgnoringCase("https://placedog.net/640/480?random"));

        return this;
    }

    @Step("Update pet data")
    public MainPage updatePetData() {
        RestAssured.given()
                .when()
                .contentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                .formParam("name", "Thor")
                .formParam("status", "pending")
                .post("/pet/" + pet.getId())
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200);
        return this;
    }

    @Step("Check updated data")
    public MainPage checkUpdatedPetData() {
        JsonPath jsonPath = RestAssured.given()
                .when()
                .get("/pet/" + pet.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .extract().body().jsonPath();

        assertEquals(jsonPath.get("name"), "Thor");
        assertEquals(jsonPath.get("status"), "pending");

        return this;
    }

    @Step("Delete pet's data from store")
    public MainPage deletePetDataFromStore() {
        RestAssured.given()
                .when()
                .header("api_key", "special-key")
                .delete("/pet/" + pet.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath();

        return this;
    }

    @Step("Delete non-existent pet data")
    public MainPage deleteNonExistentPetData() {
        RestAssured.given()
                .when()
                .header("api_key", "special-key")
                .delete("/pet/" + pet.getId())
                .then()
                .assertThat()
                .statusCode(404);
        return this;
    }

    @Step("Check invalid ID not exist")
    public MainPage checkInvalidPetsID() {
        Response response =
                get("/pet/" + 456789);
        assertEquals(response.getStatusCode(), 200);
        return this;
    }
}